package haplan;
import java.util.*;
import java.io.*;

/**
 * @package haplan
 */

/**
 * Read a file of parameters.  All the files have superficially similar
 * formats:
 *   . One entry per line
 *   . optional comment lines beginning with "//"
 *   . leading and trailing whitespace ignored
 *
 * Some of the files have entries that consist of multiple parts,
 * separated by a delimiter that is unique to each file.  The ParamsReader
 * class delivers an array of strings (one per line); further parsing of
 * the entries is the responsibility of the class/method that uses the
 * file.
 */

public class ParamsReader
{
    private String fName;
    private BufferedReader reader;

    public ParamsReader(String fileName)
    {
	try {
	    fName = fileName;
	    FileReader input = new FileReader(fileName);
	    reader = new BufferedReader(input);
	} catch (Exception e) {
	    throw new HPException(e);
	}
    }

    public Vector<String> readFile()
    {
	try {
	    Vector<String> vec = new Vector<String>();
	    String line;
	    while ((line = reader.readLine()) != null) {
		line = line.trim();
		if (line.isEmpty() || line.startsWith("//")) {
		    continue;		// Ignore comments and blank lines
		}
		vec.add(line);
	    }
	    return vec;
	} catch (Exception e) {
	    throw new HPException(e);
	}
    }
}
