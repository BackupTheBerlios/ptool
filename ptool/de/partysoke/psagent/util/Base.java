/*
 * Created on 12.12.2003
 * by Enrico Tröger
 */


package de.partysoke.psagent.util;

import java.io.*;
import java.util.*;
import java.text.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

import de.partysoke.psagent.*;
import de.partysoke.psagent.gui.*;
import de.partysoke.psagent.download.*;
import de.partysoke.psagent.util.print.*;

/**
 * Funktions-Container-Klasse, die wichtige Grundfunktionen zum Holen und Verarbeiten,
 * der Daten aus dem Netz, weitere Basisfunktionen des Programms
 * sowie die Schnittstelle zur Klasse DownloadThread bietet. 
 */
	
public class Base {

	private static String timeOfArray="";
	private static String inhalt;
	private static String[][] events;
	private static int eventsCount, maxEventsCount, userEventsCount;
	
	/**
	 * Prüft, ob eine Datei vorhanden und gültig ist, und gibt im Erfolgsfall "true" zurück.
	 * @param file
	 * @return boolean
	 */
	public static boolean check_file(MWnd parent, Config config) {
		File f1 = new File(Define.getEventsFile());
		if (f1.exists()) {
			inhalt=DownloadThread.code(FileIO.readFile(Define.getEventsFile(),false),config.getConst());
			maxEventsCount = parseArraySize(inhalt);
			if (maxEventsCount>0) {
				timeOfArray=inhalt.split("\n")[1];
				return true;
			}
			// Datei oder Übertragung fehlerhaft
			else {
				switch (maxEventsCount) {
					case -1: showBox(parent, Define.getNoDBCon(),1); break;
					case -2: showBox(parent, Define.getWrongUser(),1); break;
					case -4: showBox(parent, Define.getWrongData(),1); break;
					// vorerst auskommentiert, sonst zuviele Fehlermeldungen
					//default: JOptionPane.showMessageDialog(parent, "Es ist ein allgemeiner Fehler aufgetreten.", Define.getOwnName() + " " + Start.getVersionAsString(),JOptionPane.ERROR_MESSAGE);
					
				}
				return false;
			}

		}
		//	Datei existiert nicht
		else {
			//showBox(parent, "Daten-Datei (\""+eventsFile+"\") ist nicht vorhanden.",4);
			return false;
		}
	}
	
		
	
	/**
	 * Gibt die angegebene "Zeile" aus dem Events-Array zurück.
	 * @return events
	 */
	public static String[] getEvent(int zeile) {
		return events[zeile];
	}

	/**
	 * Gibt einige Informationen zum verwendeten Betriebssystem zurück.
	 * @return infos
	 */
	public static String getSystemInfos() {

		StringBuffer infos = new StringBuffer();
		infos.append("System-Informationen:\r\nJava-Version: ");
		infos.append(System.getProperty("java.version"));
		infos.append("\r\nJava-Vendor: ");
		infos.append(System.getProperty("java.vendor"));
		infos.append("\r\nClass-Lib-Version: ");
		infos.append(System.getProperty("java.class.version"));
		infos.append("\r\nVM-Version: ");
		infos.append(System.getProperty("java.vm.version"));
		infos.append("\r\nVM-Vendor: ");
		infos.append(System.getProperty("java.vm.vendor"));
		infos.append("\r\nVm-Name: ");
		infos.append(System.getProperty("java.vm.name"));
		infos.append("\r\nJava-Klassenpfad: ");
		infos.append(System.getProperty("java.class.path"));
		infos.append("\r\nExtension-Verzeichnis: ");
		infos.append(System.getProperty("java.ext.dirs"));
		infos.append("\r\nJava-Installions-Verz.: ");
		infos.append(System.getProperty("java.home"));
		infos.append("\r\nFile-Encoding: ");
		infos.append(System.getProperty("file.encoding"));
		infos.append("\r\nOS: ");
		infos.append(System.getProperty("os.name"));
		infos.append("\r\nOS-Version: ");
		infos.append(System.getProperty("os.version"));
		infos.append("\r\nOS-Architektur: ");
		infos.append(System.getProperty("os.arch"));
		infos.append("\r\nAktuelles Verzeichnis: ");
		infos.append(System.getProperty("user.dir"));
		infos.append("\r\nDatum: ");
		infos.append(getDate(Define.DATE_BOTH));
		infos.append("\r\n");

		return infos.toString();
	}
	

	/**
	 * Zeigt eine YesNoBox (JOptionPane).
	 * @param text
	 * @param type
	 */
	public static int showYNBox(Component parent, String text) {
		return JOptionPane.showConfirmDialog(parent, text, Define.getOwnName() + " " + Define.getVersionAsString(),
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
	}

	/**
	 * Zeigt eine MessageBox (JOptionPane). Typen:<br>
	 * 1 - Error-Message<br>
	 * 2 - Information<br>
	 * 3 - Question<br>
	 * 4 - Warning<br>
	 * @param text
	 * @param type
	 */
	public static void showBox(String text, int type) {
	    showBox(null, text, type);
	}

	/**
	* Zeigt eine MessageBox (JOptionPane). Typen:<br>
	* 1 - Error-Message<br>
	* 2 - Information<br>
	* 3 - Question<br>
	* 4 - Warning<br>
	* @param parent
	* @param text
	* @param type
	*/
	public static void showBox(Component parent, String text, int type) {
		int typ=4;
		switch (type) {
			case 1: typ=JOptionPane.ERROR_MESSAGE; break;
			case 2: typ=JOptionPane.INFORMATION_MESSAGE; break;
			case 3: typ=JOptionPane.QUESTION_MESSAGE; break;
			case 4: typ=JOptionPane.WARNING_MESSAGE; break;
		}
		JOptionPane.showMessageDialog(parent, text, Define.getOwnName() + " " + Define.getVersionAsString(),typ);
	}

	
	/**
	 * Gibt einen Teil oder ein vollständiges Datum aus
	 * @param was
	 * @return datum
	 */
	public static String getDate(int format) {
		
	    String result = "dd.MM.yyyy HH:mm:ss";
		
		switch (format) {
			case Define.DATE_DATE: result = "dd.MM.yyyy"; break;
			case Define.DATE_TIME: result = "HH:mm:ss"; break;
			case Define.DATE_DMY: result = "yyyyMMdd"; break;
			case Define.DATE_BOTH: result = "dd.MM.yyyy HH:mm:ss"; break;
		}
		GregorianCalendar cal = new GregorianCalendar();
	    SimpleDateFormat sdf = new SimpleDateFormat(result);
	    return sdf.format(cal.getTime());
	}
	
	/**
	 * Gibt einen Teil des Datum als int zurück
	 * @param was
	 * @return datum
	 */
	public static int getDateAsInt(int format) {
		
		int datum=0;
		GregorianCalendar cal = new GregorianCalendar();
		switch (format) {
			case Define.DATE_YEAR: datum=cal.get(Calendar.YEAR); break;
			case Define.DATE_MONTH: datum=cal.get(Calendar.MONTH)+1; break;
			case Define.DATE_DAY: datum=cal.get(Calendar.DAY_OF_MONTH); break;
			case Define.DATE_HOUR: datum=cal.get(Calendar.HOUR_OF_DAY); break;
			case Define.DATE_MINUTE: datum=cal.get(Calendar.MINUTE); break;
			case Define.DATE_SECOND: datum=cal.get(Calendar.SECOND); break;
		}
		return datum;
	}
	
	/**
	 * Gibt die Uhrzeit, der letzten Datenabfrage aus (= Stand der Datenbank)
	 * @return Zeit-String
	 */
	public static String getTimeOfArray()
	{
		return timeOfArray;
	}
	
	
	/**
	 * Zerlegt die Zeilen der News-Datei in ein Array.
	 * @param source
	 * @return result
	 */
	public static String[][] parseNews()
	{
		Config conf = Start.getConf();
	    String source = DownloadThread.code(FileIO.readZippedFile(Define.getNewsFileName()),conf.getConst());
		String[] tmp = source.split("\\|");
		String[][] result = new String[(tmp.length/3)][3];
		source = null;
		int j = 0;
		
		for (int i = 0; i < (tmp.length-2); i += 3) {
		    result[j][0] = tmp[i];	// Datum
		    result[j][1] = tmp[i + 1];	// Betreff
		    result[j++][2] = tmp[i + 2];	// Text
		}

		return result;
	}

	
	/**
	 * Zerlegt die Zeilen der News-Datei in ein Array.
	 * @param source
	 * @return result
	 */
	public static StringBuffer parseNews2()
	{
		Config conf = Start.getConf();
	    String source = DownloadThread.code(FileIO.readZippedFile(Define.getNewsFileName()),conf.getConst());
		String[] tmp = source.split("\\|");
		source = null;

		StringBuffer result = new StringBuffer();
		try {
			result.append("<html><body>");
			result.append("<table border=\"0\" cellpadding=\"2\" cellspacing=\"0\" align=\"left\" width=\"475\">");
			result.append("<tr><td valign=\"top\"><font face=\"Verdana\"><b>Datum:</b></font></td>");
			result.append("<td><font face=\"Verdana\">News-Text:</font></td></tr>");
			result.append("<tr><td>&nbsp;</td><td></td></tr>");
			result.append("<tr><td valign=\"top\"><font face=\"Verdana\"><b>"+tmp[0]+"</b></font></td>");
			result.append("<td><font face=\"Verdana\"><i>"+tmp[1]+"</i></font></td></tr>");
			result.append("<tr><td></td><td><font face=\"Verdana\">"+tmp[2]+"</font></td></tr>");
			result.append("<tr><td>&nbsp;</td><td></td></tr>");
			result.append("<tr><td valign=\"top\"><font face=\"Verdana\"><b>"+tmp[3]+"</b></font></td>");
			result.append("<td><font face=\"Verdana\"><i>"+tmp[4]+"</i></font></td></tr>");
			result.append("<tr><td></td><td><font face=\"Verdana\">"+tmp[5]+"</font></td></tr>");
			result.append("<tr><td>&nbsp;</td><td></td></tr>");
			result.append("<tr><td valign=\"top\"><font face=\"Verdana\"><b>"+tmp[6]+"</b></font></td>");
			result.append("<td><font face=\"Verdana\"><i>"+tmp[7]+"</i></font></td></tr>");
			result.append("<tr><td></td><td><font face=\"Verdana\">"+tmp[8]+"</font></td></tr>");
			result.append("<tr><td>&nbsp;</td><td></td></tr>");
			result.append("<tr><td valign=\"top\"><font face=\"Verdana\"><b>"+tmp[9]+"</b></font></td>");
			result.append("<td><font face=\"Verdana\"><i>"+tmp[10]+"</i></font></td></tr>");
			result.append("<tr><td></td><td><font face=\"Verdana\">"+tmp[11]+"</font></td></tr>");
			result.append("<tr><td>&nbsp;</td><td></td></tr>");
			result.append("<tr><td valign=\"top\"><font face=\"Verdana\"><b>"+tmp[12]+"</b></font></td>");
			result.append("<td><font face=\"Verdana\"><i>"+tmp[13]+"</i></font></td></tr>");
			result.append("<tr><td></td><td><font face=\"Verdana\">"+tmp[14]+"</font></td></tr>");
			result.append("<tr><td>&nbsp;</td><td></td></tr>");
			result.append("</table></body></html>");
		}
		catch (RuntimeException e) {  }
		
		return result;
	}
	
	/**
	 * Gibt die Anzahl der Events zurück, die in der Tabelle sind
	 * @return eventsCount
	 */
	public static int getEventsCount() {
	    return eventsCount;
	}
		
	/**
	 * Ließt die Anzahl der User-Events ein, die in der User-Events-Datei stehen
	 */
	public static void readUserEventsCount() {
	    String source = FileIO.readFile(Define.getUserEADFileName(), false);
	    if (!source.equals("")) {
	        userEventsCount = Base.parseUserEvents1D(source).length;
	    }
	    else {
	        userEventsCount = 0;
	    }
	}
	
	/**
	 * Gibt die Anzahl der UserEvents zurück.
	 * @return userEventsCount
	 */
	public static int getUserEventsCount() {
	    return userEventsCount;
	}
	
	/**
	 * Reduziert die Anzahl der User-Events um 1 und gibt die Anzahl zurück.
	 * @return userEventsCount
	 */
	public static int setUserEventsCount() {
	    return --userEventsCount;
	}
	
	/**
	 * Zerlegt die Datei mit den UserEvents in ein 2D-Array
	 * @param source
	 * @return userevents
	 */
	public static String[][] parseUserEvents(String source) {
		
		if (userEventsCount > 0) {
		    // Array deklarieren und initialisieren
			String[] tmp = new String[userEventsCount];
			String[][] result = new String[userEventsCount][12];
			tmp=source.split("\r\n");
			for (int i=0; i < userEventsCount; i++) {
			    result[i]=tmp[i].split("\\|");
			}
			return result;
		}
		else {
		    return new String[0][0];
		}
	}

	/**
	 * Zerlegt die Datei mit den UserEvents in ein 1D-Array
	 * @param source
	 * @return userevents
	 */
	public static String[] parseUserEvents1D(String source) {
		
		// Anzahl der User-Events(=Array-Länge) ermitteln
		StringTokenizer tok = new StringTokenizer(source, "\r\n");
		int size = tok.countTokens();
		
		// Array deklarieren und initialisieren
		String[] tmp = new String[size];
		tmp=source.split("\r\n");
		
		return tmp;
		
	}

	public static int getIndexByValue(String[] haystack, String needle) {
	    int j;
	    boolean found = false;
	    
	    for(j = 0; j < haystack.length; j++) {
	        if (haystack[j].equals(needle)) {
	            found = true;
	            break;
	        }
	    }
	    if (found) return j;
	    else return -1;
	}
	
	/**
	 * Gibt die Anzahl der Events (=Größe des Arrays events) zurück.
	 * Falls ein Fehler aufgetreten ist, wird ein Fehlercode zurückgegeben.
	 * (-1: keine DB-Verbindung, -2 falsche User-Daten, -3 sonstiger Fehler)
	 * @return int
	 */
	private static int parseArraySize(String source)
	{
		int error=0;
		try {	
			error=Integer.parseInt(String.valueOf(source.charAt(0)));
		}
		catch (Exception e) { error=4; }
		
		if (Define.doDebug() && error > 0) {	// Debug-Mode
		    new Logger("Fehlercode: " + error, true);
		}

		if (error==0) {
			String result="";
			try{
				int max = Integer.parseInt(String.valueOf(source.charAt(1)));
				for (int i=1; i <= max; i++) {
					result+=source.charAt(i+1);
				}
				if (Define.doDebug>1) {
				    new Logger("Stellen der Eintragsanzahl: " + max, true);
				    new Logger("Eintragsanzahl: " + result, true);
				}
				return Integer.parseInt(result);
			}
			catch (RuntimeException e) {
				if (Define.doDebug > 1) new Logger(e.toString());
			    return -4;
			}
		}
		else {
			int error_code;
			switch(error) {
				case 1: error_code = -1; break;
				case 2: error_code = -2; break;
				default: error_code = -5;
			}
			return error_code;
		}
	}
	
	/**
	 * Prüft, ob das angegebene Datum in der Vergangenheit liegt, 
	 * und gibt dann true zurück.
	 * @param date
	 * @param today
	 * @return
	 */
	private static boolean isOldEvent(String date, String today) {
	    // date: 12.12.2004 in 20041212 umwandeln
	    String[] tmp = date.split("\\.");
	    date = tmp[2] + tmp[1] + tmp[0];
	    try {
		    if (Integer.parseInt(date) < Integer.parseInt(today))
		        return true;
		    else
		        return false;
		}
		catch(NumberFormatException e) { return true; }

	}
	
	/**
	 * Zerlegt die Zeilen des Datenfile (source) in ein 2D-Array anhand der Anzahl der Zeilen (size).
	 * @param source
	 * @param size
	 * @return events
	 */
	private static String[][] parseData(String source)
	{
	    String[] tmp = source.split("\n");
	    String[] tmp2;
		Vector result = new Vector();
		String today = getDate(Define.DATE_DMY);
		
		for (int i=0; i < tmp.length; i++) {
			if (i>1) {
			    tmp2 = tmp[i].replaceAll("<br>"," - ").split("\\|");
			    // Event überspringen, wenn es vergangen ist(tmp2[5] ist das Datum)
			    if (! isOldEvent(tmp2[5], today)) {
					result.add(tmp2);
			    }
			} 
		}
		tmp  = null;
		String[][] finalResult = new String[result.size()][14];
		for (int x = 0; x < result.size(); x++) {
		    finalResult[x] = (String[])result.get(x);
		}
		return finalResult;
	}
	
	/**
	 * Hauptroutine für das Aufbereiten der Daten.
	 */
	public static String[][] getAll() {
		
		//	Array, in das sämtliche Events kommen, füllen
		events = Base.parseData(inhalt);
		eventsCount = events.length;
		String[][] result = new String[eventsCount][5];
		
		// Array in Tabellen-Form umwandeln
		for (int i=0; i < events.length; i++) {
				result[i][0]=events[i][1];
				result[i][1]=events[i][2];
				result[i][2]=events[i][5];
				result[i][3]=events[i][3];
				result[i][4]=events[i][4];
		}
		
		inhalt = null;
		return result;
			
	}

	
	public static boolean getNewVersion() throws IOException {
		OutputStream out = NetIO.getFromUrlToStream(
		        DownloadThread.getVersionAddr(),
		        Define.getUA()
		        );
		
		String ver=DownloadThread.code(out.toString(),17);
		out  = null;
		
		// Version analysieren
		String[] tmp = ver.split(",");
		int[] tmpVersion = new int[3];
		tmpVersion[0]=Integer.parseInt(tmp[0]);
		tmpVersion[1]=Integer.parseInt(tmp[1]);
		tmpVersion[2]=Integer.parseInt(tmp[2]);
		tmp  = null;
		ver  = null;
		
		int [] curV=Define.getVersion();
		if ((tmpVersion[0]>curV[0]) || (tmpVersion[1]>curV[1]) || (tmpVersion[2]>curV[2])) {
		    Start.setNewVersion(tmpVersion);
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Holt Benutzer-Daten (eMail, Url, Teln.Nr.) vom Server und speichert sie.
	 */
	public static void getUserData() throws IOException {
		OutputStream out = NetIO.getFromUrlToStream(
		        DownloadThread.getUserDataAddr(),
		        Define.getUA());
		
		String[] tmp=DownloadThread.code(out.toString(),0).split("\\|");
		Config conf = Start.getConf();
		conf.seteMail(tmp[0]);
		conf.setUrl(tmp[1]);
		conf.setTelNr(tmp[2]);
		
	}
	
	
	public static String toHexString(byte b) {
	     int value = (b & 0x7F) + (b < 0 ? 128 : 0);
	     String ret = (value < 16 ? "0" : "");
	     ret += Integer.toHexString(value).toUpperCase();
	     return ret;
	}
	
	
	/**
	 * Löscht das Element mit der ID id aus dem Array und verschiebt den Rest um eins nach vorn,
	 * so bleibt das Array konsistent
	 * 
	 * @param array
	 * @param index
	 * @return neues Array
	 */
	public static String[][] deleteArrayElement(String[][] array, int index) {
		if ((index >= 0) && (index < array.length)) {
			String[][] tmp = new String[array.length-1][array[0].length];
			int j = 0, i;
			for (i = 0; i < array.length; i++) {
				if (i != index) {
					tmp[j] = array[i];
					j++;
				}
			}
			return tmp;
		}
		else {
			return new String[0][0];
		}
		
	}

	/**
	 * Löscht alle Elemente mit einer ID aus indezes aus dem Array und verschiebt den Rest um eins nach vorn,
	 * so bleibt das Array konsistent
	 * 
	 * @param array
	 * @param indezes
	 * @return neues Array
	 */
	public static String[] deleteArrayElementsReverse(String[] array, int[] indezes) {
	    if ((indezes.length >= 0) && (indezes.length <= array.length)) {
	        int i, j = 0;
	        String[] tmp = new String[array.length-indezes.length];
		    
	        // zu entfernende Indizes markieren
	        for (i = 0; i < indezes.length; i++) {
	            if (indezes[i] >= 0) array[indezes[i]] = "--mark--";
	        }
	        
	        // alles nicht markierte behalten (in tmp schreiben)
	        try {
	            for (i = 0; i < array.length; i++) {
	                if (! array[i].equals("--mark--")) {
	                	tmp[j] = array[i];
			        	j++;
			    	}
	            }
	        }
	        catch(Exception e) { 
	            if (Define.doDebug > 1) new Logger(e.toString());
	        }
	        return tmp;
		}
		else {
			return new String[0];
		}
		
	}

	/**
	 * Behält alle Elemente des Arrays, deren Index in indezes steht
	 * 
	 * @param array
	 * @param indezes
	 * @return neues Array
	 */
	public static String[] deleteArrayElements(String[] array, int[] indezes) {
	    if ((indezes.length >= 0) && (indezes.length <= array.length)) {
	        int x, i, j = 0;
	        String[] tmp = new String[indezes.length];
		    
	        try {
	            for (i = 0; i < array.length; i++) {
	                for (j = 0; j < indezes.length; j++) {
	    	            if (i == indezes[j]) {
	    	                tmp[j] = array[i];
	    	                j++;
	    	            }
			    	}
	            }
	        }
	        catch(Exception e) { 
	            if (Define.doDebug > 1) new Logger(e.toString());
	        }
	        return tmp;
		}
		else {
			return new String[0];
		}
		
	}

	/**
	 * Schreibt die aktuellen Events (ohne die gelöschten) in die user.dat
	 *
	 */
	public static void writeEventsToFile(String[][] userEvents) {
	    StringWriter toAdd = new StringWriter();
	    
	    int l;
	    String zeit;
	    String cost;
	    String text;
	    for (int i=0; i < userEvents.length; i++) {
	        l = userEvents[i].length;
	        if (l > 10) zeit = userEvents[i][10];
		    else zeit = "";
	        if (l > 11) cost = userEvents[i][11];
		    else cost = "";
	        if (l > 12) text = userEvents[i][12];
		    else text = "";
		        
	        // name|ort|plz|tag|monat|jahr|bands|kat|loc|orga|zeit|cost|text
	        toAdd.write(userEvents[i][0] + "|" + userEvents[i][1] + "|" + userEvents[i][2] + "|" + userEvents[i][3] + 
	        "|" + userEvents[i][4] + "|" + userEvents[i][5]+ "|" + userEvents[i][6] + "|" + userEvents[i][7] +
	        "|" + userEvents[i][8] + "|" + userEvents[i][9] + "|" + zeit + "|" + cost + "|" + text + "\r\n");
	    }
	        
	    //toAdd = toAdd.replaceAll("\r\n", "<br>");
	    FileIO.writeToFile(Define.getUserEADFileName(), toAdd.toString());
	
	}

	/**
	 * Schreibt die aktuellen Events (ohne die fehlerhaften) in die user.dat
	 *
	 */
	public static void writeEventsToFile1D(String[] userEvents) {
	    StringWriter toAdd = new StringWriter();
	    
	    for (int i=0; i < userEvents.length; i++) {
	        toAdd.write(userEvents[i] + "\r\n");
	    }
	        
	    //toAdd = toAdd.replaceAll("\r\n", "<br>");
	    FileIO.writeToFile(Define.getUserEADFileName(), toAdd.toString());
	
	}

	
	/**
	 * Prüft, ob der Ordner für die Daten-Dateien und Einstelllungen existiert,
	 * wenn nicht, wird er erstellt. 
	 *
	 */
	public static void makeDir() {
		File f = new File(Define.homeDir());
		if (!f.exists()) {
			if (f.mkdir()) {
				if (Define.doDebug()) 
				    new Logger("Verzeichnis " + Define.homeDir() + " erfolgreich angelegt.", true);
			}
			else {
				if (Define.doDebug()) 
				    new Logger("Verzeichnis " + Define.homeDir() + " konnte nicht angelegt werden.",true);
			}
		}
	  	
	}

    /**
     * Findet grob das Betriebssystem
     * @return os
     */
	public static final int getOS() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.indexOf("windows") >= 0) return Define.WINDOWS_OS;
        if (os.indexOf("linux") >= 0) return Define.LINUX_OS;
        return Define.OTHER_OS;
    }


	/**
	 * Generiert n Zufallszahlen und gibt sie in einem Int-Array zurück.
	 * @param Anzahl der Zahlen
	 * @return Zufallszahlen
	 */
	public static int[] getRandomDigits(int n) {
		
		int[] random=new int[n];
		for (int i=0; i < n; i++) {
			random[i]=(int) (Math.random() * n);
		}
		return random;
				
	}
	
	/**
	 * Prüft auf eine neue Version und reagiert entsprechend mit Dialogboxen (JOptionPane)
	 * @param parent
	 */
	public static void searchNewVersion(Component parent) {
		try {
			if (Base.getNewVersion()) {
				if (Define.doDebug())
				    new Logger("Neue Version verf\u00FCgbar: " + Start.getNewVersionAsString(),false);
				String msg="Es ist eine neue " + Define.getOwnName() + "-Version verf\u00FCgbar: "+Start.getNewVersionAsString()+"\nSchaue bitte auf " + Define.getUrl_self() + ", um die neue Version herunterzuladen.";
				Base.showBox(parent, msg, 2);
			}
			else {
				Base.showBox(parent, "Du hast die aktuellste Version von " + Define.getOwnName() + ".", 2);
			}
		}
		catch (IOException e) { 
			if (Define.doDebug>1) new Logger(e.toString());
			Base.showBox(parent, Define.getNoCon(), 2);
		}
		catch (RuntimeException e) {}

	}

	
	/**
	 * Bereitet die Daten aus dem Events-Array so auf, dass sie gedruckt werden können
	 *
	 */
	public static void preparePrinting() {
	    
	    int maxLineLen = 95;
	    StringBuffer text = new StringBuffer();
	    StringBuffer event;
	    
	    // Datums-Vektor bauen (sowas wie group by - Klausel aus mysql)
	    Vector dates = new Vector();
	    for (int i = 0; i < events.length; i++) {
	        if (! dates.contains(events[i][5]))
	            dates.add(events[i][5]);
	    }
	    // durch das ganze Array gehen, und Datumsgruppen erstellen
	    for (int j = 0; j < dates.size(); j++) {
		    text.append(dates.get(j));
		    text.append("\n----------\n");
	        for (int i = 0; i < events.length; i++) {
		       //DebugPrint.print_ri(events[i],"\n");
	           if (events[i][5].equals(dates.get(j))) {
	               event = new StringBuffer();
	               event.append("- ");
	               event.append(events[i][1]);
	               event.append(" in ");
	               event.append(events[i][2]); 
		           if (! events[i][4].equals("")) {
		               event.append(", Band(s): ");
		               if (events[i][4].length() > 100) {
		                   event.append(events[i][4].substring(0, 98));
		                   event.append("...");
		               }
		               else {
		                   event.append(events[i][4]);
		               }
		           }
		           if (! events[i][9].equals("")) {
		               event.append(", Zeit: ");
		               event.append(events[i][9]);
		           }
		           if (event.length() > maxLineLen) {
		               text.append(event.substring(0, maxLineLen));
		               text.append("\n  ");
		               text.append(event.substring(maxLineLen));
		           }
		           else {
			           text.append(event);
		           }
		           text.append("\n");
		       }

		    }
		    if (j < dates.size() - 1) text.append("\n");
	    }

	    FileIO.writeToFile(Define.getTmpFile(), text.toString());

	}
	
	/**
	 * Bereitet die Daten aus dem Events-Array so auf, dass sie gedruckt werden können
	 *
	 */
	public static void preparePrinting2() {
	    
	    StringBuffer text = new StringBuffer();
	    
	    // Datums-Vektor bauen (sowas wie group by - Klausel aus mysql)
	    Vector dates = new Vector();
	    for (int i = 0; i < events.length; i++) {
	        if (! dates.contains(events[i][5]))
	            dates.add(events[i][5]);
	    }
	    // durch das ganze Array gehen, und Datumsgruppen erstellen
	    for (int j = 0; j < dates.size(); j++) {
		    text.append(dates.get(j));
		    text.append("\n----------\n");
	        for (int i = 0; i < events.length; i++) {
		       //DebugPrint.print_ri(events[i],"\n");
	           if (events[i][5].equals(dates.get(j))) {
	               text.append("- ");
	               text.append(events[i][1]);
		           text.append(" in ");
		           text.append(events[i][2]); 
		           if (! events[i][4].equals("")) {
	                   text.append(", Band(s): ");
		               if (events[i][4].length() > 100) {
		                   text.append(events[i][4].substring(0, 98));
		                   text.append("...");
		               }
		               else {
		                   text.append(events[i][4]);
		               }
		           }
		           if (! events[i][9].equals("")) {
		               text.append(", Zeit: ");
		               text.append(events[i][9]);
		           }
		           text.append("\n");
		       }

		    }
		    if (j < dates.size() - 1) text.append("\n");
	    }

	    FileIO.writeToFile(Define.getTmpFile(), text.toString());

	}
	
	/**
	 * Druckt die Events-Tabelle
	 */
	public static void print() {

	    preparePrinting();
	    SimpleFilePrinter sfp = new SimpleFilePrinter(Define.getTmpFile());
	    if (sfp.setupJobOptions()) {
	        try {
	            sfp.printFile();
	        } catch (Exception e) {
	            System.err.println(e.toString());
	        }
	    }
	    new File(Define.getTmpFile()).delete();
	}
    
	/**
	 * Findet aus einer Url mit einem pac-File den entsprechenden Proxy
	 * @param filename
	 * @return Array aus proxy-Url und proxy-Port
	 */
	public static String[] getProxyFromPAC(String filename) {
        String[] proxy = { "", "" };
        //String[] file = FileIO.readFile(filename, false).split("\r\n");
        try {
            String[] file = NetIO.getFromUrlToStream(filename, Define.getUA()).
        				toString().split("\n");
        
            for (int i = 0; i < file.length; i++) {
                file[i] = file[i].replaceAll("\t", "");
                file[i] = file[i].trim();
            	if (! file[i].startsWith("//") && file[i].matches(".*PROXY.*")) {
                	proxy = file[i].split(" ")[2].split(";")[0].split(":");
            	}
        	}
        }
        catch (IOException e) { new Logger(e.toString()); }
        if (Define.doDebug > 1) new Logger("Proxy: " + DebugPrint.print_all(proxy));
        return proxy;
    }

	
	/**
	 * Ändert die Proxy-Einstellungen
	 * (holt sich nötigenfalls die Einstellungen aus einer .pac)<br>
	 * TODO: Design(returnType, Weiterverarbeitung in den Dialogen) evtl. nochmal
	 * überarbeiten
	 */
	public static boolean setProxySettings() {
  	    Config conf = Start.getConf();
  	    
  	    if (! conf.getProxyUrl().equals("") ) {
  	        String[] values = getProxyFromPAC(conf.getProxyUrl());
  	        // Versuchen den Port zu einem integer zu machen, als Prüfung
  	        // für das Parsen, etwas dirty aber was solls
  	        try {
  	            Integer.parseInt(values[1]);
  	        }
  	        catch (NumberFormatException e) {
  	            showBox(Help.PROXY_FAIL, 1);
  	            return false;
  	        }
  	        conf.setProxyUrl("");
  	        conf.setProxyHost(values[0]);
  	        conf.setProxyPort(values[1]);
  	    }
  	    
  	    if (! conf.getProxyHost().equals("") ) {
  	        System.setProperty("http.proxyHost",     conf.getProxyHost());
  	        System.setProperty("http.proxyPort",     conf.getProxyPort());
  	        System.setProperty("http.proxyUser",     conf.getProxyUser());
  		    System.setProperty("http.proxyPassword", conf.getProxyPass());
  		}
  	    else {
  	        System.setProperty("http.proxyHost", "");
  	        System.setProperty("http.proxyPort", "");  	        
  	    }
  	    if (Define.doDebug())
  	      new Logger("Proxy-Einstellungen ge\u00E4ndert.", true);
  	    
  	    return true;
  	}
    


	/**
	 * Ändert die Groessen aller Spalten, so dass alle Werte komplett sichtbar 
	 * sind.
	 * stammt von http://www.chka.de/swing/table/cell-sizes.html
	 * 
	 * @param table
	 */
	public static void calcColumnWidths(JTable table)
	{
		JTableHeader header = table.getTableHeader();
		TableCellRenderer defaultHeaderRenderer = null;

		if (header != null)
			defaultHeaderRenderer = header.getDefaultRenderer();

		TableColumnModel columns = table.getColumnModel();
		TableModel data = table.getModel();

		int margin = columns.getColumnMargin(); // only JDK1.3
		int rowCount = data.getRowCount();

		for (int i = columns.getColumnCount() - 1; i >= 0; --i)
		//for (int i = 4 - 1; i >= 2; --i)
		{
			TableColumn column = columns.getColumn(i);
			int columnIndex = column.getModelIndex();
			int width = -1; 
			TableCellRenderer h = column.getHeaderRenderer();
			
			if (h == null)
				h = defaultHeaderRenderer;
			
			if (h != null) // Not explicitly impossible
			{
				Component c = h.getTableCellRendererComponent
				(table, column.getHeaderValue(),
						false, false, -1, i);
				
				width = c.getPreferredSize().width;
			}
			
			for (int row = rowCount - 1; row >= 0; --row)
			{
				TableCellRenderer r = table.getCellRenderer(row, i);
				
				Component c = r.getTableCellRendererComponent
				(table,
						data.getValueAt(row, columnIndex),
						false, false, row, i);
				
				width = Math.min(width, c.getPreferredSize().width);
			}

			if (width >= 0)
				column.setPreferredWidth(width + margin); // <1.3: without margin
		}

	}
	
}

