package haplan;
import java.util.*;
import java.io.*;

/**
 * @package haplan
 */

/**
 *
 */
public class SexList
{
// private int MAX_REPS = 10;
    static final public int IXFemale = 0;
    static final public int IXMale = 1;
    static final public int IXsexFF = 0;
    static final public int IXsexMF = 1;
    static final public int IXsexMM = 2;
	    // List of all H or X characters
    private Set<String> nameSet;
    private Vector<String> nameList;
    private Vector<String> HNames;
    private Vector<NamePair> sexPairs;
    private HashMap<String, Integer> name2Ix;
    private ConstraintList constraints;
    private int score;
    private int bestCombo;
    private boolean comboSearchComplete;

    /**
     * @param HFname	Filename: list of Hs (input)
     * @param sexFname	Filename: list of sex scenes (input)
     */
    public SexList(String HFName, String sexFName)
    {
	readHs(HFName);
	readSexScenes(sexFName);
	this.constraints = new ConstraintList();
	comboSearchComplete = false;
    }

    /**
     * the story.
     * Build the lists of characters that appear in Hs in the story.
     * I use a Set so that a given character appears only once in the list,
     * even if he/she appears multiple times in the story.
     * @param HFName	Filename: a list of H characters
     */
    protected void readHs(String HFName)
    {
	ParamsReader HReader = new ParamsReader(HFName);
	this.nameSet = new HashSet<String>();
	Vector<String> HLines = HReader.readFile();
	this.HNames = new Vector<String>();
	for (String name : HLines) {
	    name = name.toLowerCase();
	    this.HNames.add(name);
	    this.nameSet.add(name.toLowerCase());
	}
    }

    /**
     * Build the lists of characters that appear in sex scenes in the
     * story.
     * I use a Set so that a given character appears only once in the list,
     * even if he/she appears multiple times in the story.
     * @param sexFName	Filename: list of sex scenes (input)
     */
    public void readSexScenes(String sexFName)
    {
	ParamsReader sexReader = new ParamsReader(sexFName);
	Vector<String> sexLines = sexReader.readFile();
	this.sexPairs = new Vector<NamePair>();
	int n = 0;
	for (String s : sexLines) {
	    // ignore pairs that start with a minus sign "-"
	    if (s.startsWith("-")) {
		continue;
	    }
	    ++n;
	    String[] pair = s.split("/");
	    if (pair.length != 2) {
		Integer i = new Integer(n);
		debug.error("File: %s: line %d: invalid input: '%s'",
		     sexFName, n, s);
	    }
	    pair[0] = pair[0].toLowerCase();
	    pair[1] = pair[1].toLowerCase();
	    this.sexPairs.add(new NamePair(pair[0], pair[1])); 
	    this.nameSet.add(pair[0]);
	    this.nameSet.add(pair[1]);
	}
    }

    /**
     * Read in the constraints file.
     * @param cnstrFName	File name: list of constraints
     */
    public void setConstraints(ConstraintList cnlist)
    {
	this.constraints = cnlist;
    }

    /**
     * Build the indexes that map names to their positions in the list of
     * names. These indexes will later be used to associate each name with
     * a bit in the bitset (represented as an int) that is used for testing
     * the MF state of the characters.
     * @return bitmask corresponding to the index-position of those
     * characters who are constrained to a predetermined gender,
     * represented as an int.
     */
    private int buildNameIndexes()
    {
	this.nameList = new Vector<String>(this.nameSet);
	Vector<String> cnNames = constraints.getNames();

	// bit: The mask bit for each constrained character
	int bit = 1 << this.nameSet.size();
	int constrainedBits = 0;
	for (String cname : cnNames) {
	    this.nameList.add(cname);
	    int mf = this.constraints.getMFByName(cname);
	    if (mf != 0) {
		constrainedBits |= bit;
	    }
	    bit <<= 1;	// shift bit one left for next character
	}

	this.name2Ix = new HashMap<String, Integer>();
	int ix = 0;
	for (String name : this.nameList) {
	    this.name2Ix.put(name, new Integer(ix++));
	}

// debug.enable(0);
// debug.debugNL("name2Ix:\n" + this.name2Ix.toString()); 
// debug.debugNL("constrainedBits: " + String.format("%08x", constrainedBits));
	return constrainedBits;
    }

    /**
     * @return bitmask corresponding to the index-position of the
     * H characters, represented as an int.
     */
    private int[] getHBits()
    {
	int nH = HNames.size();
	int[] HBits = new int[nH];

	for (int ix = 0; ix < nH; ix++) {
	    String name = this.HNames.get(ix);
	    HBits[ix] = 1 << this.name2Ix.get(name);
	}
	return HBits;
    }

    /**
     * Get the array of bit mask <i>pairs</i> corresponding to the
     * characters who appear in sex scenes.
     * @return array of bit mask pairs. Each mask corresponds to the
     * index-position of a character who participates in a sex scene.
     */
    private IntPair[] getSexMasks()
    {
	int nSex = sexPairs.size();
	IntPair[] sexMasks = new IntPair[nSex];

	for (int i = 0; i < nSex; i++) {
	    NamePair pair = this.sexPairs.get(i);
	    String name1 = pair.getName1();
	    String name2 = pair.getName2();
	    int ix1 = this.name2Ix.get(name1);
	    int ix2 = this.name2Ix.get(name2);
	    int bit1 = 1 << ix1;
	    int bit2 = 1 << ix2;
// String fmt = "%d %s-%s; indexes: %d %d; bits %08x %08x\n";
// String msg = String.format(fmt, i, name1, name2, ix1, ix2, bit1, bit2);
// debug.debugNL(msg);
	    sexMasks[i] = new IntPair(bit1, bit2);
	}
	return sexMasks;
    }

    /**
     * Get the score of just one combo
     * @param scoring	Rules for scoring characters by sex
     * @param bits	The combo to be scored
     * @return array of two integers: the scores for Hs and sex
     * scenes corresponding to the given bit mask.
     */
    public int[] scoreCombo(Scoring scoring, int bits)
    {
	int[] HBits = getHBits();
	IntPair[] sexMasks = getSexMasks();
	int hscore = scoring.getHScore(bits, HBits);
	int sscore = scoring.getSexScore(bits, sexMasks);
	int[] scores = {hscore, sscore};
	return scores;
    }

    /**
     * Search for the combination of male and female characters that
     * produces the highest score.
     * @param scoring	Rules for scoring characters by sex
     * @return bitmask corresponding to the best combination found.  For
     * each character, the corresponding bit is 0 for female, 1 for male.
     * The low-order bit (0x00000001) corresponds to the first character
     * in the list of Hs; the highest-order bit (1<<n) corresponds to
     * the character who appears last in the lists of sex scenes, where n
     * is the number of characters in the combined lists (Hs, sex
     * scenes).
     */
    public int findBestCombo(Scoring scoring)
    {
	int bestScore = Integer.MIN_VALUE;	// Best score found so far
	int combo = -1;		// Bitmap of the best combination so far
	int nCombos = getNumFreeNames();

	// Computing the best score requires brute force examination of 
	// several million combinations of male and female characters.
	// Build indexes from names to numbers (and vice versa) so each
	// iteration will take as little time as possible.
	// scoreLoopMax represents all the non-constrained characters'
	// being male.  Counting from 0 to scoreLoopMax will try all
	// possible combinations.

	int scoreLoopMax = (1 << nCombos) - 1;
	int constrainedBits = buildNameIndexes();
	int[] HBits = getHBits();
	IntPair[] sexMasks = getSexMasks();


	/**
	 * @var int bits	bit vector (an array of bits in a single
	 * word) that is being treated like an integer in this for loop.
	 * Why?  Because my story has about 25 significant characters, and
	 * I want to try all possible combinations of each being male or
	 * female.  I could write a recursive function with backtracking,
	 * but if you count from 0 to 2^n-1, the binary representation will
	 * include all 2^n combinations of 1s and 0s, where  "n" is the
	 * number of unconstrained characters (not constrained to be one
	 * particular sex).  Thus, all permitted combinations will be
	 * tested.
	 */
	for (int tBits = 0; tBits <= scoreLoopMax; tBits++) {
	    int bits = tBits | constrainedBits;
	    int score = scoring.getHScore(bits, HBits) +
			scoring.getSexScore(bits, sexMasks);
	    if (score > bestScore) {
		bestScore = score;
		combo = bits;
	    }
	}

	this.score = bestScore;
	bestCombo = combo;
	comboSearchComplete = true;
	return bestCombo;
    }

    /**
     * Get the number of characters who appear in Hs/sex-scenes and
     * whose sex is not fixed by the constraints list.
     * @return number of free (unconstrained) character names
     */
    public int getNumFreeNames()
    {
	return nameSet.size();
    }

    /**
     * Get the total number of characters who appear in
     * Hs/sex-scenes.
     * @return total number of character names
     */
    public int getNumTotalNames()
    {
	return this.nameSet.size() + this.constraints.size();
    }

    /**
     * Get the score for the best combo found
     * @return score corresponding to the combo previously found by
     * findBestCombo
     */
    public int getScore()
    {
	return this.score;
    }

    /**
     * @return array of two ints: female and male H characters
     */
    public int[] getHCounts()
    {
	int[] HBits = getHBits();
	int nHs = HBits.length;
	int[] HCounts = {0, 0};
	for (int i = 0;  i < nHs;  i++) {
	    int mask = HBits[i];
	    int ix = ((bestCombo & mask) != 0) ? IXMale : IXFemale;
	    HCounts[ix]++;
	}

	return HCounts;
    }

    /**
     * Get the number of FF, MF, and MM sex scenes
     * @return array of 3 ints, one for each type of sex scene
     */
    public int[] getSexCounts()
    {
	IntPair[] sexMasks = getSexMasks();
	int nSex = sexMasks.length;
	int[] sexCounts = {0, 0, 0};
	for (int i = 0;  i < nSex;  i++) {
	    int mask1 = sexMasks[i].get1();
	    int mask2 = sexMasks[i].get2();
	    int mf1 = ((bestCombo & mask1) != 0) ? 1 : 0;
	    int mf2 = ((bestCombo & mask2) != 0) ? 1 : 0;
	    // The values are carefully chosen:
	    // in the combo bits, 0 = female, 1 = male.
	    // So 0 + 0 = 0 (FF), 0 + 1 = 1 (MF), 1 + 1 = 2 (MM)
	    sexCounts[mf1 + mf2]++;
	}
	return sexCounts;
    }

    /**
     * Get the gender of the given character, based on the previously
     * computed best combo
     * @return true if the character is male, false if female
     */
    public boolean getGender(String name)
    {
if (name.equalsIgnoreCase("andrew")) {
debug.enable(0);
} else {
debug.disable();
}
debug.debug("getGender: name: %s", name);
	Integer i = name2Ix.get(name);
	if (i == null) {
debug.debugNL(" -- NOT FOUND");
	    float randfloat = hplan.twister.nextFloat();
	    return (randfloat >= 0.75);
	} else {
debug.debugNL("ix: %d", i);
	    return ((bestCombo >> i) & 1) != 0;
	}
    }

    /**
     * Get the list of names of H characters
     * @return Vector<String> list of names
     */
    public Vector<String> getHNames()
    {
	return HNames;
    }

    /**
     * Get the list of names of characters in sex scenes
     * @return list of pairs of names
     */
    public Vector<NamePair> getSexPairs()
    {
	return sexPairs;
    }

}
