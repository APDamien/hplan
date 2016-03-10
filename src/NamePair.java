package haplan;
import java.util.*;
import java.io.*;

/**
 * @package haplan
 */

/**
 * A holder for two character names.
 */

public class NamePair
{
    private String name1;
    private String name2;

    public NamePair()
    {
	name1 = null;
	name2 = null;
    }

    public NamePair(String n1, String n2)
    {
	name1 = n1;
	name2 = n2;
    }

    public String getName1()
    {
	return name1;
    }

    public String getName2()
    {
	return name2;
    }
}
