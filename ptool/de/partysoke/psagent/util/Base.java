/*
 * Created on 12.12.2003
 * by Enrico Tröger
 */


package de.partysoke.psagent.util;

import java.io.*;
import java.util.*;
import java.text.*;
import java.net.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

import de.partysoke.psagent.*;
import de.partysoke.psagent.gui.*;
import de.partysoke.psagent.download.*;

/**
 * Funktions-Container-Klasse, die wichtige Grundfunktionen zum Holen und Verarbeiten,
 * der Daten aus dem Netz, weitere Basisfunktionen des Programms
 * sowie die Schnittstelle zur Klasse DownloadThread bietet. 
 */
	
public class Base {

	private static String timeOfArray="";
	private static String inhalt;
	private static String[][] events;
	private static int eventsCount;
	private static int userEventsCount;
	
	/**
	 * Prüft, ob eine Datei vorhanden und gültig ist, und gibt im Erfolgsfall "true" zurück.
	 * @param file
	 * @return boolean
	 */
	public static boolean check_file(MWnd parent, Config config) {
		File f1 = new File(Define.getEventsFile());
		if (f1.exists()) {
			inhalt=DownloadThread.code(FileIO.readZippedFile(Define.getEventsFile()),config.getKey());
			eventsCount = parseArraySize(inhalt);
			if (eventsCount>0) {
				timeOfArray=inhalt.split("\n")[1];
				return true;
			}
			// Datei oder Übertragung fehlerhaft
			else {
				switch (eventsCount) {
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

		String datum = getDate(Define.DATE_BOTH);
		
		String infos=
		"System-Informationen:\r\nJava-Version: "+System.getProperty("java.version")+"\r\n"+
		"Java-Vendor: "+System.getProperty("java.vendor")+"\r\n"+
		"Class-Lib-Version: "+System.getProperty("java.class.version")+"\r\n"+
		"VM-Version: "+System.getProperty("java.vm.version")+"\r\n"+
		"VM-Vendor: "+System.getProperty("java.vm.vendor")+"\r\n"+
		"Vm-Name: "+System.getProperty("java.vm.name")+"\r\n"+
		"Java-Klassenpfad: "+System.getProperty("java.class.path")+"\r\n"+
		"Extension-Verzeichnis: "+System.getProperty("java.ext.dirs")+"\r\n"+
		"Java-Installions-Verz.: "+System.getProperty("java.home")+"\r\n"+
		"File-Encoding: "+System.getProperty("file.encoding")+"\r\n"+
		"OS: "+System.getProperty("os.name")+"\r\n"+
		"OS-Version: "+System.getProperty("os.version")+"\r\n"+
		"OS-Architektur: "+System.getProperty("os.arch")+"\r\n"+
		"Aktuelles Verzeichnis: "+System.getProperty("user.dir")+"\r\n"+
		"Datum: "+datum+"\r\n";
		return infos;
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
	 * Zeigt eine MessageBox (JOptionPane). Typen:
	 * 1 - Error-Message<br>
	 * 2 - Information<br>
	 * 3 - Question<br>
	 * 4 - Warning<br>
	 * @param text
	 * @param type
	 */
	public static void showBox(String text, int type) {
		int typ=4;
		switch (type) {
			case 1: typ=JOptionPane.ERROR_MESSAGE; break;
			case 2: typ=JOptionPane.INFORMATION_MESSAGE; break;
			case 3: typ=JOptionPane.QUESTION_MESSAGE; break;
			case 4: typ=JOptionPane.WARNING_MESSAGE; break;
		}
		JOptionPane.showMessageDialog(null, text, Define.getOwnName() + " " + Define.getVersionAsString(),typ);
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
	 * 01.01.2004 12:25
	 * @param was
	 * @return datum
	 */
	public static String getDate(int format) {
		
	    String result = "dd.MM.yyyy HH:MM:ss";
		
		switch (format) {
			case Define.DATE_DATE: result = "dd.MM.yyyy"; break;
			case Define.DATE_TIME: result = "HH:MM:ss"; break;
			case Define.DATE_BOTH: result = "dd.MM.yyyy HH:MM:ss"; break;
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
	
	/* PANIC es gibt keinen Aufrufer mehr *g*
	public static String[][] parseEAD(String source) {
		
		// Anzahl der User-Events(=Array-Länge) ermitteln
		StringTokenizer tok = new StringTokenizer(source, "\r\n");
		int size = tok.countTokens();
		System.out.println(size);
		// Array deklarieren und initialisieren
		//String[][] result = parseData(source, size); 
		String[] tmp = new String[size];
		String[][] result = new String[size][12];
		tmp=source.split("\n");
		for (int i=0; i < size; i++) {
			result[i]=tmp[i].split("\\|");
		}
		
		return result;
		
	}*/
	
	
	/**
	 * Zerlegt die Zeilen der News-Datei in ein Array.
	 * @param source
	 * @return result
	 */
	public static String parseNews()
	{
		Config conf = Start.getConf();
	    String source = DownloadThread.code(FileIO.readZippedFile(Define.getNewsFileName()),conf.getKey());
		String[] tmp = source.split("\\|");
		source = null;
		
		if (Define.doDebug>2) {	// Debug-Mode
			Base.LogThis(DebugPrint.print_all(tmp), true);
		}
		
		String result="";
		try {
			result = "<html><body>"+
			"<table border=\"0\" cellpadding=\"2\" cellspacing=\"0\" align=\"left\" width=\"475\">"+
			"<tr><td valign=\"top\"><font face=\"Verdana\"><b>Datum:</b></font></td>"+
			"<td><font face=\"Verdana\">News-Text:</font></td></tr>"+
			"<tr><td>&nbsp;</td><td></td></tr>"+
			"<tr><td valign=\"top\"><font face=\"Verdana\"><b>"+tmp[0]+"</b></font></td>"+
			"<td><font face=\"Verdana\"><i>"+tmp[1]+"</i></font></td></tr>"+
			"<tr><td></td><td><font face=\"Verdana\">"+tmp[2]+"</font></td></tr>"+
			"<tr><td>&nbsp;</td><td></td></tr>"+
			"<tr><td valign=\"top\"><font face=\"Verdana\"><b>"+tmp[3]+"</b></font></td>"+
			"<td><font face=\"Verdana\"><i>"+tmp[4]+"</i></font></td></tr>"+
			"<tr><td></td><td><font face=\"Verdana\">"+tmp[5]+"</font></td></tr>"+
			"<tr><td>&nbsp;</td><td></td></tr>"+
			"<tr><td valign=\"top\"><font face=\"Verdana\"><b>"+tmp[6]+"</b></font></td>"+
			"<td><font face=\"Verdana\"><i>"+tmp[7]+"</i></font></td></tr>"+
			"<tr><td></td><td><font face=\"Verdana\">"+tmp[8]+"</font></td></tr>"+
			"<tr><td>&nbsp;</td><td></td></tr>"+
			"<tr><td valign=\"top\"><font face=\"Verdana\"><b>"+tmp[9]+"</b></font></td>"+
			"<td><font face=\"Verdana\"><i>"+tmp[10]+"</i></font></td></tr>"+
			"<tr><td></td><td><font face=\"Verdana\">"+tmp[11]+"</font></td></tr>"+
			"<tr><td>&nbsp;</td><td></td></tr>"+
			"<tr><td valign=\"top\"><font face=\"Verdana\"><b>"+tmp[12]+"</b></font></td>"+
			"<td><font face=\"Verdana\"><i>"+tmp[13]+"</i></font></td></tr>"+
			"<tr><td></td><td><font face=\"Verdana\">"+tmp[14]+"</font></td></tr>"+
			"<tr><td>&nbsp;</td><td></td></tr>"+
			"</table></body></html>";
		}
		catch (Exception e) {  }
		
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
	 * Gibt die Anzahl der Events zurück, die in der User-Events-Tabelle sind
	 * @return userEventsCount
	 */
	public static void readUserEventsCount() {
	    String source = FileIO.readFile(Define.getUserEADFileName(), false);
	    if (!source.equals("")) {
	        userEventsCount = Base.parseUserEvents1D(source).length;
	    }
	    else {
	        userEventsCount = 0;
	    }
	    
	    if (Define.doDebug > 1) Base.LogThis("userEventsCount: " + userEventsCount);
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
			Base.LogThis("Fehlercode: " + error, true);
		}

		if (error==0) {
			String result="";
			try{
				int max = Integer.parseInt(String.valueOf(source.charAt(1)));
				for (int i=1; i <= max; i++) {
					result+=source.charAt(i+1);
				}
				if (Define.doDebug>1) {
					Base.LogThis("Stellen der Eintragsanzahl: " + max, true);
					Base.LogThis("Eintragsanzahl: " + result, true);
				}
				return Integer.parseInt(result);
			}
			catch (Exception e) {
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
	 * Zerlegt die Zeilen des Datenfile (source) in ein 2D-Array anhand der Anzahl der Zeilen (size).
	 * @param source
	 * @param size
	 * @return String[][]
	 */
	private static String[][] parseData(String source, int size)
	{
		String[] tmp = new String[size];
		String[][] result = new String[size][14];
		tmp=source.split("\n");
		
		for (int i=0; i < size+2; i++) {
			if (i>1) {
				result[i-2]=tmp[i].split("\\|");
				for (int j=1; j < 6; j++) {
					result[i-2][j]=result[i-2][j].replaceAll("<br>"," - ");
					result[i-2][j]=result[i-2][j].replaceAll("<BR>"," - ");
				}
			} 
		}
		if (Define.doDebug>2) {	// Debug-Mode
			Base.LogThis(DebugPrint.print_all(result), true);
		}
		
		tmp  = null;
		return result;
	}
	
	/**
	 * Hauptroutine für das Aufbereiten der Daten.
	 */
	public static String[][] getAll() {
		
		//	Array, in das sämtliche Events kommen, füllen
		String[][] result = new String[eventsCount][5];
		events = new String[eventsCount][15];
		events=Base.parseData(inhalt,eventsCount);
		
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

	
	public static boolean getNewVersion() 
	throws Exception {
		String ver="";
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		URL url = new URL(DownloadThread.getVersionAddr());
		InputStream in = url.openStream();
		if (in.available()>0) {
			int len;
			byte[] b = new byte[100];
			while ((len = in.read(b)) != -1) {
				out.write(b,0,len);
			}
		}
		in.close();
		out.close();
		ver=DownloadThread.code(out.toString(),17);
		out  = null;
		in  = null;
		
		// Version analysieren
		String[] tmp2 = ver.split(",");
		int[] tmpVersion = new int[3];
		tmpVersion[0]=Integer.parseInt(tmp2[0]);
		tmpVersion[1]=Integer.parseInt(tmp2[1]);
		tmpVersion[2]=Integer.parseInt(tmp2[2]);
		tmp2  = null;
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
	public static void getUserData() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			URL url = new URL(DownloadThread.getUserDataAddr());
			InputStream in = url.openStream();
			if (in.available()>0) {
				int len;
				byte[] b = new byte[100];
				while ((len = in.read(b)) != -1) {
					out.write(b,0,len);
				}
				b  = null;
			}
			in.close();
			in  = null;
			out.close();
		} 
		catch (IOException e) {
			if (Define.doDebug()) {
				Base.LogThis(e.toString(), true);
			} 
		}
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
	 * Löscht alle Elemente mit einer ID aus id aus dem Array und verschiebt den Rest um eins nach vorn,
	 * so bleibt das Array konsistent
	 * 
	 * @param array
	 * @param indezes
	 * @return neues Array
	 */
	public static String[] deleteArrayElements(String[] array, int[] indezes) {
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
	            if (Define.doDebug > 1) System.out.println("deleteArrayElements: "+e.toString());
	        }
	        return tmp;
		}
		else {
			return new String[0];
		}
		
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
					LogThis("Verzeichnis " + Define.homeDir() + " erfolgreich angelegt.", true);
			}
			else {
				if (Define.doDebug()) 
					LogThis("Verzeichnis " + Define.homeDir() + " konnte nicht angelegt werden.",true);
			}
		}
	  	
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
					Base.LogThis("Neue Version verf\u00FCgbar: " + Start.getNewVersionAsString(),false);
				String msg="Es ist eine neue " + Define.getOwnName() + "-Version verf\u00FCgbar: "+Start.getNewVersionAsString()+"\nSchaue bitte auf " + Define.getUrl_self() + ", um die neue Version herunterzuladen.";
				Base.showBox(parent, msg, 2);
			}
			else {
				Base.showBox(parent, "Du hast die aktuellste Version von " + Define.getOwnName() + ".", 2);
			}
		}
		catch (Exception e) { 
			if (Define.doDebug>1)
				Base.LogThis("Exception bei Versions-Kontrolle: " + e, true);
			Base.showBox(parent, Define.getNoCon(), 2);
		}

	}


	/**
	 * Gibt text aus, wenn show gesetzt ist, und schreibt text in die Log-Datei
	 * @param text
	 * @param show
	 */
	public static void LogThis(String text, boolean show) {
		if (Define.doDebug()) {
		    StringWriter tmp = FileIO.readFileToWriter(Define.getDebugFileName(), false);
		    if (show) {
		        tmp.write(getDate(Define.DATE_BOTH) + ": " + text + "\r\n");
		        System.out.println(text);
		    }
		    else
		        tmp.write(text + "\r\n");
		    FileIO.writeToFile(Define.getDebugFileName(), tmp.toString(), false);
		}
		else
		    System.out.println(text);
	}
	
	/**
	 * Gibt text aus und schreibt text in die Log-Datei
	 * @param text
	 * @param show
	 */
	public static void LogThis(String text) {
	    LogThis(String.valueOf(text), true);
	}
	
	/**
	 * Gibt text aus und schreibt text in die Log-Datei
	 * @param text
	 * @param show
	 */
	public static void LogThis(int text) {
	    LogThis(String.valueOf(text), true);
	}
	

	/**
	 * Ändert die Groessen aller Spalten, so dass alle Werte komplett sichtbar 
	 * sind.
	 * 'Geklaut' aus de.comp.lang.java, etwas modifizert
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

