package haplan;
import java.util.*;
import java.io.*;
/**
 * @package haplan
 */

/**
 * A block of HTML nodes. Knows how to write itself out
 */

public class HTMLblock extends HTMLelement
{

    private Vector<HTMLthing> elements;

    public HTMLblock()
    {
	super("div");
	init();
    }

    public HTMLblock(HTMLthing el)
    {
	super();
	init();
	add(el);
    }

    protected HTMLblock(String type)
    {
	super(type);
	init();
    }

    protected void init()
    {
	elements = new Vector<HTMLthing>();
    }

    protected void writeContents(PrintWriter wr)
    {
debug.debugNL("writeContents: " + type + " start");
	for (HTMLthing el : elements) {
	    el.write(wr);
	}
debug.debugNL("writeContents: " + type + " end");
    }

    public int count() { return elements.size(); }


    public void add(String s)
    {
	elements.add(new HTMLtext(s));
    }

    public void add(HTMLthing el)
    {
	elements.add(el);
    }

}

