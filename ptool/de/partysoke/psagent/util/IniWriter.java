
/*
 * Created on 29.07.2004
 * by Enrico Tröger
 */


package de.partysoke.psagent.util;

import java.io.*;
import java.util.*;

import de.partysoke.psagent.*;

/**
 * Klasse zum Schreiben der Config-Datei, legt auch bei nicht-existieren diese neu an.
 */
public class IniWriter {

    Writer tmp, out;
    String[] inhalt;
    String[][] values;
    Vector zusatz;
    String fileName;
    Config parent;
    
    public IniWriter(String[][] values_tmp, String fileName) throws IOException {
        
        this.values = values_tmp;
        this.fileName = fileName;
        this.zusatz = new Vector();
        
        // Pruefen, ob Datei schon existiert
        File f = new File(fileName);
        if (! f.exists()) {
            f.createNewFile();
            this.createNewConfigFile();
        }
        
        tmp = FileIO.readFileToWriter(fileName, false);
        inhalt = tmp.toString().split("\n");
        fill();
    }
    
    private void fill() {
        boolean matched;
        for (int i=0; i < values.length; i++) {
            // suche zeile in der key is, und ersetze value
            matched = false;
            for (int j=0; j < inhalt.length; j++) {
                //if (inhalt[j].startsWith(values[i][1])) {	// scheiß Lösung, mit startsWith
                if (inhalt[j].split("=")[0].equals(values[i][1])) {
                    inhalt[j]=values[i][1] + "=" + values[i][2];
                    matched = true;
                }
            }
            if (! matched) {
                zusatz.add(values[i][1] + "=" + values[i][2]);
            }
        }
    }
    
    public void close() throws IOException {
        out = new BufferedWriter(new FileWriter(fileName));
        for (int j=0; j < inhalt.length; j++) {
            out.write(inhalt[j] + "\n");
        }
        for (Enumeration e = zusatz.elements(); e.hasMoreElements(); ) {
            out.write((String) e.nextElement() + "\n");
        }
        out.close();
    }
    
    private void createNewConfigFile() throws IOException {
        BufferedWriter newFile = new BufferedWriter(new FileWriter(fileName));
        newFile.write("; " + Define.getOwnName() + " - ConfigFile\n");
        newFile.write("; Datei wird automatisch erzeugt,  nicht ver\u00E4ndern!\n");
        newFile.write("[" + Define.getOwnName() + "]\n");
        for (int i = 0; i < values.length; i++) {
            newFile.write(values[i][1] + "=\n");
        }
        newFile.close();
    }
}
