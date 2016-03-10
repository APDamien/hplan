package haplan;
import java.util.*;
import java.io.*;

/**
 * @package haplan
 */

/**
 * An HTML element: knows how to write itself out to a PrintWriter
 */

public abstract class HTMLelement implements HTMLthing
{
    private String cl;
    private String id;
    protected String type;
    protected HashMap<String, String> attrs;
    protected PrintWriter writer;

    public HTMLelement()
    {
	init(null);
    }

    public HTMLelement(String type)
    {
	init(type);
    }

    protected void init(String type)
    {
	attrs = new HashMap<String, String>();
	this.type = type;
    }

    public void setAttr(String name, String val)
    {
	attrs.put(name, val);
    }

    public String getAttr(String name)
    {
	return attrs.get(name);
    }

    public void setClass(String cl) { setAttr("class", cl); }
    public String getCl() { return this.getAttr("class"); }
    public void setID(String id) { setAttr("id", id); }
    public String getID() { return this.getAttr("id"); }

    abstract protected void writeContents(PrintWriter wr);

    public void write(PrintWriter wr)
    {
debug.debugNL("HTMLelement %s start", type);
	writeStart(wr);
	writeContents(wr);
	writeEnd(wr);
debug.debugNL("HTMLelement %s end", type);
    }


    protected void writeStart(PrintWriter wr)
    {
	if (this.type == null) {
	    debug.error("HTMLelement: type not set");
	}

	String attrString = "";
	Set<Map.Entry<String,String>> attrlist = attrs.entrySet();
	for (Map.Entry<String,String>attrpair : attrlist) {
	    String name = attrpair.getKey();
	    String val = attrpair.getValue();
	    attrString += String.format(" %s='%s'", name, val);
	}
	wr.printf("<%s %s>\n", type, attrString);
    }

    protected void writeEnd(PrintWriter wr)
    {
	wr.printf("</%s>\n", type);
    }
}

