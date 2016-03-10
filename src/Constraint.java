package haplan;
import java.util.*;
import java.io.*;

/**
 * @package haplan
 */

/**
 * Object representing a constraint on the sex of a character. Basically a
 * pair (String, int): the character's name and the gender that character
 * is required to have.
 */

public class Constraint
{
    private String name;
    private int mf;		// 0 (female) or 1 (male)

    /**
     * @param cnline	A line from the constraints input file
     */
    public Constraint(String cnline)
    {
// PrintStream stdout = System.out;
// stdout.printf("Constraint: line: %s\n", cnline);
	String[] cnArray = cnline.split(" ");
	if (cnArray.length != 2) {
	    debug.error("Invalid constraint: '%s'", cnline);
	}

	this.name = cnArray[0].toLowerCase();;
	this.mf = Scoring.mf2int(cnArray[1].charAt(0));
    }

    public String getName() { return this.name; }
    public int getMF() { return this.mf; }

}
