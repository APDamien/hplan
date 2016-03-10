package haplan;
import java.util.*;
import java.io.*;

/**
 * @package haplan
 */

/**
 * A holder for a pair: a name (String) and an integer
 */

public class NameInt
{
    private String name;
    private int number;

    public NameInt()
    {
	name = null;
	number = Integer.MIN_VALUE;
    }

    public NameInt(String nm, int n)
    {
	name = nm;
	number = n;
    }

    public String getName()
    {
	return name;
    }

    public int getIx()
    {
	return number;
    }
}
