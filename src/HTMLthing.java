package haplan;
import java.util.*;
import java.io.*;

/**
 * @package haplan
 */

/**
 * An HTML node -- template for all other HTML objects
 */

public interface HTMLthing
{
    public void setAttr(String name, String val);
    public String getAttr(String name);
    public void setClass(String cl);
    public String getCl();
    public void setID(String cl);
    public String getID();
    public void write(PrintWriter writer);

}

