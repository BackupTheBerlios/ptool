
/*
 * Created on 17.06.2004
 * by Enrico Tröger
 */


package de.partysoke.psagent.util;

import java.io.*;
import java.util.*;

/**
 * This class can read properties files in Microsoft .ini file style and provides
 * an interface to read string, integer and boolean values.  The .ini files has the
 * following structure:
 * <pre>; a comment
 * [section]
 * key=value</pre>
 * 
 * @author Stefan Matthias Aust (sma@3plus4.de)
 * @version 1
 */
public class IniHandler {
    private Map sections = new HashMap();

    public IniHandler(String pathname) throws FileNotFoundException, IOException {
        this(new FileReader(pathname));
    }
    
    public IniHandler(Reader input) throws FileNotFoundException, IOException {
        initialize(new BufferedReader(input));
    }

    private void initialize(BufferedReader r) throws IOException {
        String section = null, line;
        while ((line = r.readLine()) != null) {
            line = line.trim();
            if (line.equals("") || line.startsWith(";")) {
                continue;
            }
            if (line.startsWith("[")) {
                if (!line.endsWith("]")) {
                    throw new IOException("] expected in section header");
                }
                section = line.substring(1, line.length() - 1).toLowerCase();
            } else if (section == null) {
                throw new IOException("[section] header expected");
            } else {
                int index = line.indexOf('=');
                if (index < 0) {
                    throw new IOException("key/value pair without =");
                }
                String key = line.substring(0, index).trim().toLowerCase();
                String value = line.substring(index + 1).trim();
                Map map = (Map) sections.get(section);
                if (map == null) {
                    sections.put(section, (map = new HashMap()));
                }
                map.put(key, value);
            }
        }
    }

    public String getPropertyString(String section, String key, String defaultValue) {
        Map map = (Map) sections.get(section.toLowerCase());
        if (map != null) {
            String value = (String) map.get(key.toLowerCase());
            if (value != null) {
                return value;
            }
        }
        return defaultValue;
    }

    public int getPropertyInt(String section, String key, int defaultValue) {
        String s = getPropertyString(section, key, null);
        if (s != null) {
            return Integer.parseInt(s);
        }
        return defaultValue;
    }

    public boolean getPropertyBool(String section, String key, boolean defaultValue) {
        String s = getPropertyString(section, key, null);
        if (s != null) {
            return s.equalsIgnoreCase("true");
        }
        return defaultValue;
    }
}
