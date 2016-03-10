package haplan;
import java.util.*;
import java.io.*;


/**
 * @package haplan
 */

/**
 * Entry in the list of all characters.  Each entry includes:
 *   . The character's name in the original (FF) version of the story
 *   . The character's name in the furry (MM) version of the story
 *   . The species of the character and/or a description of the
 *     character's "place" in the story.
 */

public class NameMapEntry
{
    private Vector<String> Mnames;	// name(s) in the MM story
    private Vector<String> MFnames;	// name(s) in the MF story
    private String description;		// optional description
    private String cellType;
    private CharacterList characters;
    private int gender;

    /**
     * @param s		String with sections delimited by semicolons
     *			and/or vertical bars, as documented in the array
     *			characterMap.
     */
    public NameMapEntry(String s, CharacterList characters)
    {
	cellType = "td";
	String[] sections = s.split("\\|");
	int nSections = sections.length;
	this.Mnames = new Vector<String>();
	this.MFnames = new Vector<String>();
	for (int i = 0;  i < nSections; i++) {
	    String sect = sections[i];
	    String parts[] = sect.split(";");


	    this.description = "&nbsp;";
	    this.MFnames.add(parts[0]);

	    // If only one part, MF name is the same as MM name
	    // If two or more, part 2 is the MF name
	    if (parts.length < 2) {
		this.Mnames.add(parts[0]);
	    } else {
		this.Mnames.add(parts[1]);
	    }

	    // Third part, if present, is the description/species
	    if (parts.length >= 3) {
		if (i > 0) {
		    debug.error(
		     "description allowed only in the first part: %s", 
		     s
		    );
		}
	    }

	    // No more than 3 parts allowed
	    if (parts.length >= 4) {

		    debug.error("Too many parts: %s", s);

	    }
	}
	String firstName = Mnames.get(0);
	gender = characters.getGender(firstName);
if (firstName.equalsIgnoreCase("leo")) {
debug.enable(0);
debug.debugNL("NameMapEntry: %s: %d", firstName, gender);
debug.disable();
}
    }

    public String getMname()
    {
	return Mnames.get(0);
    }

    public String getMFname()
    {
	return MFnames.get(0);
    }

    public String getKey()
    {
	return getMname().toLowerCase();
    }

    RuntimeException getError(String s, String errmsg)
    {
	String className = "NameMapEntry";
	String fmt = "%s: line: %s\n   %s";
	String msg = String.format(fmt, className, errmsg, s);
	return new RuntimeException(msg);
    }

    /**
     * Create a "header" row: cells formatted with <th> instead of <td>
     * @param row	Array of strings, one per cell.
     */
    public NameMapEntry(String[] row)
    {
	this.Mnames = new Vector<String>();
	Mnames.add(row[0]);
	this.MFnames = new Vector<String>();
	MFnames.add(row[1]);
	// yyy description = (row[2] != null) ? row[2] : "";
	cellType = "th";
    }

    public HTMLthing getHTML()
    {
	// Create a table row with three cells
	HTMLblock tblrow = new HTMLblock("tr");
	HTMLblock cell1 = new HTMLblock(cellType);
	tblrow.add(cell1);
	HTMLblock spacer = new HTMLblock(cellType);
	tblrow.add(spacer);
	HTMLblock cell2 = new HTMLblock(cellType);
	tblrow.add(cell2);
	HTMLblock cell3 = new HTMLblock(cellType);
	tblrow.add(cell3);

	spacer.add("&nbsp;&nbsp;&nbsp;");

	// Put the names and description in their respective cells
	String[] sample = {""};
	boolean isMale = (gender == CharacterList.IXMale);
	HTMLblock m = getGenderedHTML(Mnames.toArray(sample), isMale);
	HTMLblock f = getGenderedHTML(MFnames.toArray(sample), !isMale);
	cell1.add(m);
	cell2.add(f);
	// yyy cell3.add(description);

	return tblrow;
    }

    public NameAndGender getNameAndGender()
    {
	String name;
	if (gender == CharacterList.IXMale) {
	    name = Mnames.get(0);
	} else {
	    name = MFnames.get(0);
	}

	NameAndGender ng = new NameAndGender(name, gender);
	return ng;
    }

    protected HTMLblock getGenderedHTML(String[] names, boolean isMale)
    {
	// xxx handle second, third, etc. names
	String firstName = names[0];
	if (isMale) {
	    HTMLblock bolded = new HTMLblock("b");
	    bolded.add(firstName);
	    return bolded;
	} else {
	    HTMLblock span = new HTMLblock("span");
	    span.add(new HTMLtext(firstName));
	    return span;
	}
    }

}
