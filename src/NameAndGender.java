package haplan;
import java.util.*;
import java.io.*;

/**
 * @package haplan
 */

/**
 * A holder for two character names.
 */

public class NameAndGender
{
    private String name;
    private int gender;

    public NameAndGender(String nm, int g)
    {
if (nm.equalsIgnoreCase("leo")) {
debug.enable(0);
debug.debugNL("NameAndGender: %s: %d", nm, g);
debug.disable();
}
	name = nm;
	gender = g;
    }

    public String getName() { return name; }
    public int getGender() { return gender; }
    public char getGenderChar()
    {
	return ((gender == CharacterList.IXMale) ? 'M' : 'F');
    }
		
    public int getGenderInt() { return (gender); }
}

