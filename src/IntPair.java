package haplan;
import java.util.*;
import java.io.*;

/**
 * @package haplan
 */

/**
 * A holder for two integers (indexes into the list of character names)
 */

public class IntPair
{
    private int num1;
    private int num2;

    public IntPair()
    {
	num1 = -1;
	num2 = -1;
    }

    public IntPair(int n1, int n2)
    {
	num1 = n1;
	num2 = n2;
    }

    public int get1()
    {
	return num1;
    }

    public int get2()
    {
	return num2;
    }
}
