package haplan;
import java.util.*;
import java.io.*;

/**
 * @package haplan
 */

/**
 * An HTML page with a standard header.
 */

public class HTMLpage extends HTMLblock
{

    private String title;

    public HTMLpage()
    {
	super("html");
	title = "";
    }

    public HTMLpage(String ttl)
    {
	super("html");
	title = ttl;
    }

    public void write(PrintWriter wr)
    {
debug.debugNL("HTMLelement %s start", type);
debug.debugNL("title: " + title);
	start(wr);
	super.writeContents(wr);
	end(wr);
debug.debugNL("HTMLelement %s end", type);
    }

    public void setTitle(String ttl)
    {
	title = ttl;
    }


    private void start(PrintWriter wr)
    {
	wr.print(
	"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" " +
	    "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">" +
	"<html xmlns='http://www.w3.org/1999/xhtml' xml:lang='en' lang='en'>\n" +
	"<head>\n" +
	"<meta http-equiv='Content-Type'\n" +
	"       content='text/html; charset=utf-8'/>\n" +
	"<title>\n" + title + "</title>\n" +
	"<link rel='stylesheet' href='charlist.css' type='text/css' />\n" +
	"</head>\n" +
	"<body>\n"
	);
    }

    public void end(PrintWriter wr)
    {
	wr.print("</body>\n");
	wr.print("</html>\n");
	wr.close();
    }

}
