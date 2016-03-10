package haplan;
import java.util.*;
import java.io.*;


/**
 * @package haplan
 */

/**
 * The list of all characters.  See NameMapEntry for the contents of each
 * entry.  Can generate an HTML table representing the name map entries.
 */

public class NameTable
{
    private Vector<NameMapEntry> nameList;
    private HashMap<String, NameMapEntry> nameMap;
    private int bitMap;
    private CharacterList characters;

    /**
     * @param s		String with sections delimited by semicolons

     *			and/or vertical bars, as documented in the array
     *			characterMap.
     */
    public NameTable(String fname, CharacterList chrs)
    {
	characters = chrs;
	readNameMap(fname);
    }

    private void readNameMap(String fname)
    {
	nameList = new Vector<NameMapEntry>();
	nameMap = new HashMap<String, NameMapEntry>();
	ParamsReader namesReader = new ParamsReader(fname);
	Vector<String> mapLines = namesReader.readFile();

	for (String line : mapLines) {
	    NameMapEntry entry = new NameMapEntry(line, characters);
	    this.nameList.add(entry);
	    String name = entry.getMname().toLowerCase();
	    nameMap.put(name.toLowerCase(), entry);
	}
    }

    public HTMLthing getTableHTML(String[] titleRow)
    {
	HTMLblock outBlock = new HTMLblock("table");
	outBlock.setClass("NameTable");

	NameMapEntry hdr = new NameMapEntry(titleRow);
	outBlock.add(hdr.getHTML());

	for (NameMapEntry ent : nameList) {
	    outBlock.add(ent.getHTML());
	}
	return outBlock;
    }

    private String getNameWithGender(String name)
    {
	String lcname = name.toLowerCase();
	NameMapEntry entry = nameMap.get(lcname);
	NameAndGender ng;
	if (entry == null) {
	    System.err.printf("H/sex name unknown: %s\n", name);
	    ng = new NameAndGender(name, CharacterList.IXMale);
	} else {
	    ng = entry.getNameAndGender();
	}
	return getNameWithGender(ng);
    }

    private String getNameWithGender(NameAndGender ng)
    {
	char gender = ng.getGenderChar();
String nm = ng.getName();
if (nm.equalsIgnoreCase("leo")) {
debug.enable(0);
debug.debugNL("NameTable::getNameAndGender: %s: %c", nm, gender);
debug.disable();
}
	String fmt = "<span class='gender'> %c </span> &nbsp;&nbsp;%s";
	String name = ng.getName();
	return (String.format(fmt, gender, name));
    }

    public HTMLthing getHsHTML()
    {
	HTMLblock Hs = new HTMLblock();
	Hs.setClass("HList");
	Vector<String> nameList = characters.getHNames();
	for (String name : nameList) {
	    String gNameBlock = getNameWithGender(name);
	    HTMLblock HLine = new HTMLblock();
	    HLine.add(gNameBlock);
	    Hs.add(HLine);
	}
	return Hs;
    }

    public HTMLthing getSexHTML()
    {
	HTMLblock sexList = new HTMLblock();
	sexList.setClass("sexList");
	Vector<NamePair> sexPairs = characters.getSexPairs();
	for (NamePair pair : sexPairs) {
	    String name1 = pair.getName1();
	    String nwg1 = getNameWithGender(name1);
	    String name2 = pair.getName2();
	    String nwg2 = getNameWithGender(name2);
	    // yyy String gn1 = getNameWithGender(name1);
	    // yyy String gn2 = getNameWithGender(name2);

	    // yyy NameMapEntry entry1 = nameMap.get(name1);
	    // yyy String gn1 = entry.getNameAndSex();
	    // yyy NameMapEntry entry2 =nameMap.get(name2);
	    // yyy String gn2 = entry2.getNameAndSex();

	    HTMLblock sexLine = new HTMLblock();
	    sexLine.add(nwg1);
	    sexLine.add(" : ");
	    sexLine.add(nwg2);
	    sexList.add(sexLine);
	}
	return sexList;
    }

}
