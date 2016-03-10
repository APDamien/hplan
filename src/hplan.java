package haplan;
import java.util.*;
import java.io.*;

/**
 * @package haplan
 */

/**
 * Determine the best combination of sexes for the characters in my
 * story
 *	I'm doing a rewrite with both male and
 *	female students.  The purpose of this program is to determine the
 *	"best" combination, based on a system that assigns different scores
 *	to Hs (M vs. F) and sex scenes (MM, MF, or FF).
 * The main program here finds that combination (I hope) and prints out the
 * results.
 */

public class hplan
{
    // parameter files
    /**
     * @var H_LIST	Filename: list of H characters one per line.
     * @var SEX_LIST	Filename: list of pairs of characters in sex scenes
     *				in the story, one per line.
     * @var CONSTRAINT_LIST	Filename: list of characters whose sex is
     *				constrained.
     * @var SCORE_RULES		Filename: score values for Hs and
     *				sex scenes
     * @var CHARACTER_MAP	Filename: list of character names. In most
     *				cases, each line contains three sections:
     *				a male name, a female name, and the species
     * @var H_OUTPUT		Output file: list of names chosen for the
     *				Hs.
     * @var SEX_OUTPUT		Output file: list of names chosen for sex
     *				scenes
     * @var CHAR_OUTPUT		Output file: sex and chosen names for all
     *				characters in the story.
     */
    public static final String H_LIST = "params/hlist.txt";
    public static final String SEX_LIST = "params/sexlist.txt";
    public static final String SCORE_RULES = "params/scores.txt";
    public static final String CONSTRAINT_LIST = "params/constraints.txt";
    public static final String CHARACTER_MAP = "params/charmap.txt";

    public static final String H_OUTPUT = "outh.html";
    public static final String SEX_OUTPUT = "outsex.html";
    public static final String CHAR_OUTPUT = "outchars.html";
    public static final MersenneTwister twister = new MersenneTwister();

    private CharacterList characters;
    private NameTable nametable;
    private int bestScore;	// The best score found
    private int bestCombo;	// combo value that yields bestScore
    /*********************************************************
    private ScoreCalculator scoreCalc;
    **********************************************************/

    /**
     * Main: Find the best combination of genders for the characters, then
     * print the results.
     */
    public static void main(String[] args)
    {
	hplan plan = new hplan();
	int bestCombo = plan.findBestCombo();
// debug.enable(0);
// debug.debugNL("printResults");
	plan.printResults();
    }

    public hplan()
    {
	characters =
		new CharacterList(hplan.H_LIST, hplan.SEX_LIST);
    }

    /**
     * Find the best combination of genders. The file SCORE_RULES
     * determines the relative value of various combinations.  The
     * genders that can be assigned are subject to the rules in the file
     * CONSTRAINT_LIST.
     * Method:
     *   1. Read in the constraint list, then use it to set the rules in
     *      the character list.
     *   2. Read in the scoring rules.
     *   3. Ask the CharacterList to find the best combination.
     */
    private int findBestCombo()
    {
	PrintStream stdout = System.out;
	ConstraintList constraints = new ConstraintList(hplan.CONSTRAINT_LIST);
	characters.setConstraints(constraints);

	int nfree = characters.getNumFreeNames();
	int ntotal = characters.getNumTotalNames();

	stdout.printf("Number of characters to try: %d\n", nfree);
	stdout.printf("Total 'active' characters: %d\n", ntotal);

	Scoring scoring = new Scoring(hplan.SCORE_RULES);

	bestCombo = characters.findBestCombo(scoring);
	bestScore = characters.getScore();
	stdout.printf("score: %d\tcombo %08x\n", bestScore, bestCombo);
	return bestCombo;
    }

    /**
     * Generate the output as a web page
     *   1. The title
     *   2. The summary: how many H characters of each sex, how many of each
     *   kind of sex scene (FF, MF, MM)
     * Three lists of characters from the story.
     *   3. All the named characters in the story, most with male and female
     *      versions of their names.  A few have the same name in the FF,
     *      MM, and MF versions of the story.
     *   4. The names of H characters
     *   5. Pairs of characters who appear in sex scenes in the story
     */

    private void printResults()
    {
// debug.enable(0);
	HTMLpage charPage = new HTMLpage("Character Map");
	charPage.add("<h2>Rewrite Plan</h2>");
	HTMLthing summary = genSummaries();
	charPage.add(summary);
	nametable = new NameTable(hplan.CHARACTER_MAP, characters);
	HTMLthing table = genNametable();
	charPage.add(table);
	HTMLthing hslists = genHSlists();
	charPage.add(hslists);
	PrintWriter writer = null;
	try {
	    writer = new PrintWriter(CHAR_OUTPUT);
	} catch (FileNotFoundException e) {
		debug.printException(e);
		System.exit(1);
	}
	charPage.write(writer);
    }


    /**
     * Generate the summary numbers
     */
    private HTMLthing genSummaries()
    {

	HTMLblock summary = new HTMLblock();

	int[] HCounts = characters.getHCounts();

	summary.add("<h3>Summary</h3>");
	summary.add("<div><span class='summaryType'>Score:&nbsp;</span>");
	summary.add(String.format("%d", bestScore));
	summary.add("</div>");
	summary.add("<div><span class='summaryType'>Hs:&nbsp;</span>");
	summary.add("<span class='sumCount'>Male: ");
	summary.add(Integer.toString(HCounts[CharacterList.IXMale]));
	summary.add("</span>, ");
	summary.add("<span class='sumCount'>Female: ");
	summary.add(Integer.toString(HCounts[CharacterList.IXFemale]));
	summary.add("</span></div>");


	int[] sexCounts = characters.getSexCounts();
	summary.add("<div><span class='summaryType'>Sex scenes:&nbsp;</span>&nbsp;");
	summary.add("<span class='sumCount'>FF: ");
	summary.add(Integer.toString(sexCounts[CharacterList.IXsexFF]));
	summary.add("</span>, ");
	summary.add("<span class='sumCount'>MF: ");
	summary.add(Integer.toString(sexCounts[CharacterList.IXsexMF]));
	summary.add("</span>, ");
	summary.add("<span class='sumCount'>MM: ");
	summary.add(Integer.toString(sexCounts[CharacterList.IXsexMM]));
	summary.add("</span></div>");
	return summary;
    }

    /**
     * Generate the lists of Hs and sex scenes
     */
    private HTMLthing genHSlists()
    {
	HTMLblock hslist = new HTMLblock();
	hslist.setClass("hslist");
	hslist.add("<h4>List of Hs</h4>");
	hslist.add(nametable.getHsHTML());
	hslist.add("<h4>List of Sex Scenes</h4>");
	hslist.add(nametable.getSexHTML());
	return hslist;
    }

    /**
     * Generate the character map
     */
    private HTMLthing genNametable()
    {
	// Generate the character map
	String[] titleRow = { "MM name", "MF name" };
	return nametable.getTableHTML(titleRow);
    }
}
