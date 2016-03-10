package haplan;
import java.util.*;
import java.io.*;

/**
 * @package haplan
 */

/**
 * 
 */

public class Scoring
{
    private int[] HScores;
    private int[] sexScores;


    /**
     * Compute the scores for a given set of character sexes
     * @param int n	Genders of the characters, bit-wise:
     *		0 = female
     *		1 = male
     */

    public Scoring(String scoreFN)
    {
	parse(scoreFN);
    }

    private Scoring()
    {
	// make the default constructor unavailable
    }


    protected void parse(String scoreFN)
    {
	ParamsReader reader = new ParamsReader(scoreFN);
	Vector<String> scores = reader.readFile();


	this.HScores = new int[2];
	this.sexScores = new int[4];

	for (String l : scores) {
	    String[] ts = l.split("::");
	    String type = ts[0];
	    String stScore = ts[1];
	    if (stScore.startsWith("+")) {
		stScore = stScore.substring(1);
	    }
	    Integer iscore = Integer.parseInt(stScore);
	    int score = iscore.intValue();
	    char letters[] = type.toCharArray();
	    char hs = letters[0];
	    int ix;
	    if (hs == 'h') {
		// score for a male or a female H character
		char gender = letters[1];
		ix = Scoring.mf2int(gender);
		this.HScores[ix] = score;

	    } else {
		char gender1 = letters[0];
		char gender2 = letters[1];
		ix = Scoring.mf2int(gender1) * 2 + mf2int(gender2);
		this.sexScores[ix] = score;
		ix = Scoring.mf2int(gender2) * 2 + mf2int(gender1);
		this.sexScores[ix] = score;
	    }
	}
    }

    public static int mf2int(char gender)
    {
    
    
	if (gender == 'M') {
	    return CharacterList.IXMale;
	} else if (gender == 'F') {
	    return CharacterList.IXFemale;
	} else {
	    debug.error("mf2int: invalid argument '%c'", gender);
	    return CharacterList.IXFemale;
	}
    }

    public int getHScore(int bits, int[] HBits)
    {
	int score = 0;
	int nHs = HBits.length;
// debug.debugNL("nHs: %d", nHs);
	for (int i = 0;  i < nHs;  i++) {
	    int mask = HBits[i];
	    int mf = ((bits & mask) != 0) ? 1 : 0;
	    score += this.HScores[mf];
// String fmt = "i: %d mask: %8x; mf: %d; score: %d\n";
// debug.debugNL(fmt, i, mask, mf, score);
	}

// debug.debugNL("H: bits: 0x%08x, score: %d\n", bits, score);
	return score;
    }

    public int getSexScore(int bits, IntPair[] sexMasks)
    {
	int score = 0;
	int nSex = sexMasks.length;
// debug.debug("sex bits: %08x, nsex: %d\n", bits, nSex);
	for (int i = 0;  i < nSex;  i++) {
	    int mask1 = sexMasks[i].get1();
	    int mask2 = sexMasks[i].get2();
	    int mf1 = ((bits & mask1) != 0) ? 1 : 0;
	    int mf2 = ((bits & mask2) != 0) ? 1 : 0;
	    int scoreIx = mf1 * 2 + mf2;

	    int scorePart = this.sexScores[scoreIx];
// debug.debugNL("masks: %08x %08x, mf1: %d, mf2: %d, scoreIx: %d, score: %d",
// mask1, mask2, mf1, mf2, scoreIx, scorePart);
	    score += scorePart;
	}
// debug.debugNL("sex: score: %d\n", score);
	return score;
    }
}
