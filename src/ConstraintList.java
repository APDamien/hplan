package haplan;
import java.util.*;
import java.io.*;

/**
 * @package haplan
 */

/**
 * List of constraints to be applied to the characters.  Each constraint
 * specifies a character name and the required gender (male or female) for
 * that character.
 */

public class ConstraintList
{
    private Vector<Constraint> list;

    public ConstraintList()
    {
	this.list = new Vector<Constraint>();
    }

    /**
     * @param cnFName	Filename: the constraints input file
     */
    public ConstraintList(String cnFName)
    {
	ParamsReader constraintsReader = new ParamsReader(cnFName);
	Vector<String> constraintLines = constraintsReader.readFile();
	this.list = new Vector<Constraint>();

	for (String l : constraintLines) {
	    Constraint cn = new Constraint(l);
	    this.list.add(cn);
	}

    }

    /**
     * Add a Constraint to the list
     * @param cn	The contraint to be added
     */
    public void add(Constraint cn)
    {
	list.add(cn);
    }

    /**
     * Get the names of characters that have constraints applied to them.
     * @return list (Vector) of names (strings)
     */
    public Vector<String> getNames()
    {
	Vector<String> names = new Vector<String>();
	for (Constraint cn : this.list) {
	    names.add(cn.getName());
	}
	return names;
    }

    /**
     * Get the constrained sex of a character
     * @param name	Name of the character
     * @return		0 for female, 1 for male, 0 if unconstrained
     */
    public int getMFByName(String name)
    {
	for (Constraint cn : this.list) {
	    if (cn.getName() == name) {
		return cn.getMF();
	    }
	}
	debug.error("ConstraintList::getMFByName: unknown name: '%s'",
		    name);
	return CharacterList.IXFemale;
    }

    /**
     * Get the length of the Constraint list
     * @return	number of Constraints in the list
     */
    public int size()
    {
	return this.list.size();
    }

}
