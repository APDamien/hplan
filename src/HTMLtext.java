package haplan;
import java.util.*;
import java.io.*;

/**
 * @package haplan
 */

/**
 * Text to be included in HTML.  Needed because the HTMLblock container
 * requires all sub-nodes to be of type "HTMLelement".
 */

public class HTMLtext extends HTMLelement
{

    private String text;

    public HTMLtext()
    {
	super();
	text = "";
    }

    public HTMLtext(String s)
    {
	super();
	text = s;
    }

    public void write(PrintWriter writer)
    {
	writer.print(text);
    }

    public void writeContents(PrintWriter writer)
    {
    }

    public void add(String s)
    {
	if (!text.isEmpty()) {
	    text += "\n";
	}
	text += s;
    }

    public void add(Object obj)
    {
	text += obj.toString();
    }

    public void add(int i)
    {
	add(new Integer(i));
    }

    public void add(long i)
    {
	add(new Long(i));
    }

    public void add(double i)
    {
	add(new Double(i));
    }

}
