package haplan;
import java.util.*;
import java.io.*;

/**
 * @package haplan
 */

/**
 * Specialized exception -- just a version of RuntimeException, so it
 * doesn't have to be declared in a "throws" clause in the files that can
 * cause it.
 */

public class HPException extends RuntimeException
{
    public HPException()
    {
	super();
    }

    public HPException(String msg)
    {
	super(msg);
    }

    public HPException(String msg, Throwable cause)
    {
	super(msg, cause);
    }

    public HPException(Throwable cause)
    {
	super(cause);
    }

}
