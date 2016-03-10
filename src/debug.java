package haplan;
import java.util.*;
import java.io.*;

/**
 * @package haplan
 */

/**
 * Debugging output class. All data and methods static
 */

public class debug
{
    public static final String DBG_FILENAME = "log";
    private static String fname = debug.DBG_FILENAME;
    private static PrintStream file = null;
    private static String outbuf = null;
    private static boolean enabled = false;

    public static void enable()
    {
	debug.file = System.err;
	debug.enabled = true;
	debug.outbuf = new String();
    }

    public static void enable(String fn)
    {
	debug.fname = fn;
	debug.enabled = true;
    }

    public static void enable(int n)
    {
	debug.fname = DBG_FILENAME;
	debug.enabled = true;
    }

    public static void disable()
    {
	debug.enabled = false;
    }

    public static void open()
    {
	if (debug.outbuf == null) {
	    try {
		debug.file = new PrintStream(debug.fname);
		debug.outbuf = new String();
	    } catch (FileNotFoundException e) {
		debug.printException(e);
		System.exit(1);
	    }
	}
    }

    public static void debug(String fmt, Object... args)
    {
	if (!debug.enabled) {
	    return;
	}
	debug.open();
	String msg = String.format(fmt, args);
	if (debug.outbuf.length() + msg.length() > 72) {
	    debug.NL();
	}
	debug.outbuf = debug.outbuf + msg;
    }

    public static void debugNL(String fmt, Object... args)
    {
	debug.debug(fmt, args);
	debug.NL();
    }

    public static void debugNL()
    {
	debug.NL();
    }

    public static void NL()
    {
	if (!debug.enabled) {
	    return;
	}
	debug.open();
	debug.file.print(debug.outbuf);
	debug.file.println("\n");
	debug.outbuf = new String();
    }

    public static void printException(Throwable e)
    {
	PrintStream stdout = System.out;
	stdout.printf("%s\n", e.toString());
	StackTraceElement[] trace = e.getStackTrace();
	for (int i = 0; i < 6; i++) {
	    if (i >= trace.length) {
		break;
	    }
	    StackTraceElement el = trace[i];
	    stdout.printf("%s\n", el.toString());
	}
    }

    public static void error(String fmt, Object... args)
    {
	String msg;
	try {
	    msg = String.format(fmt, args);
	} catch (Throwable e) {
	    msg = fmt;
	    for (int i = 0;  i < args.length; i++) {
		msg = msg + " + " + args[i].toString();
	    }
	}
	throw new HPException(msg);
    }

}
