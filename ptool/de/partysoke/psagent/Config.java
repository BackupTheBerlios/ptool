/*
 * Created on 29.07.2004
 * (eigentlich schon früher, aber das Design war komplett anders, schlechter)
 */

package de.partysoke.psagent;

import java.security.*;
import java.io.*;
import java.awt.*;

import de.partysoke.psagent.download.*;
import de.partysoke.psagent.util.*;


/**
 * Klasse zum Erzeugen des Config-Objekts, darin sind alle Konfigurationsvariablen des Programms und deren Getter/Setter-Methoden.<br>
 * Außerdem stellt sie die Methoden zum Lesen und Schreiben des Config-Datei zur Verfügung
 * @author Enrico Tröger
 */
public class Config {
	
    /** Name des Programms */
    private final String name = Define.getOwnName();

    /** OS-Ermittlung */
    private final String sys = Base.getOS() == Define.WINDOWS_OS ? "1" : "0"; 
    
    /** ist die Config noch aktuell? */
    private boolean recent = true; 
    
    /** existiert die Config-Datei? */
    private boolean exists; 
    
    /** Value-Indizes-Mapping (Ersatz für String-Indizes) */
    private final int INDEX_CONST = 0;
    private final int INDEX_LF = 1;
    private final int INDEX_USER = 2;
    private final int INDEX_PASS = 3;
    private final int INDEX_EMAIL = 4;
    private final int INDEX_URL = 5;
    private final int INDEX_TEL = 6;
    private final int INDEX_PURL = 7;
    private final int INDEX_PHOST = 8;
    private final int INDEX_PPORT = 9;
    private final int INDEX_PUSER = 10;
    private final int INDEX_PPASS = 11;
    private final int INDEX_WININFO = 12;
    private final int INDEX_WININFOUE = 13;
    private final int INDEX_WINSAVE = 14;
    private final int INDEX_VERSION = 15;
    private final int INDEX_COLWIDTH = 16;
    private final int INDEX_AUTO = 17;
    private final int INDEX_SPLASH = 18;
    private final int INDEX_SYSTRAY = 19;
    private final int INDEX_VIEW = 20;
    private final int INDEX_VIEW_COL = 21;
    
    
    /** 
     * 2D-String-Array, beinhaltet alle Config-Variablen, Struktur:<br>
     * [section][key][value]
     */
    private String[][] values = { 
	        { name, "const", ""},
			{ name, "lf", "win"},
			{ name, "username", ""},
			{ name, "password", ""},
			{ name, "email", ""},
			{ name, "url", ""},
			{ name, "telefon", ""},
			{ name, "purl", ""},
			{ name, "phost", ""},
			{ name, "pport", ""},
			{ name, "puser", ""},
			{ name, "ppass", ""},
    		{ name, "wininfo", Define.getWinInfo()},
    		{ name, "wininfo_ue", Define.getWinInfoUE()},
    		{ name, "win_save", "1"},
    		{ name, "version", Define.getFullVersionAsString()},
    		{ name, "column_widths", ""},
    		{ name, "auto_upload", "0"},
    		{ name, "splash", "1"},
    		{ name, "systray", sys},
    		{ name, "view", "table"},
    		{ name, "view_col", "0"}
    		};
    

	/**
	 * Ließt die INI-Datei ein und stellt deren Variablen dem Programm
	 * in Form eines Arrays zur Verfügung
	 * (Falls INI-Datei nicht existiert, werden Std.-Werte benutzt)
	 */
	public Config() {

		try {
		    IniHandler ini = new IniHandler(Define.getConfigFilename());
		    // Versionsprüfung, Config muss selbe Version wie Programm haben
		    // wenn recent false ist, werde obige Std.Werte benutzt, und die
		    // Config beim Beenden neu geschrieben
		    recent = ! Base.parseVersionString(
		                ini.getPropertyString(
		                        values[INDEX_VERSION][0],
		                        values[INDEX_VERSION][1], "")
		             );

		    if (! recent) {
		        // Config veraltet -> Löschen
		        new File(Define.getConfigFilename()).delete();
		        if (Define.doDebug()) new Logger("Config veraltet.");
		    }
		    for (int i = 0; i < values.length && recent; i++) {
			    values[i][2]=ini.getPropertyString(values[i][0], values[i][1], values[i][2]);
			}

		    this.exists = true;
		} 
		catch (IOException e) {
		    this.exists = false;
	        if (Define.doDebug()) {
		        new Logger("Fehler: Config konnte nicht gelesen werden.", true);
		        if (Define.doDebug > 1) new Logger(e.toString(), true);
		    }
		}
	}
	

	/**
	 * Gibt zurück, ob die Config noch aktuell zur Programmversion ist
	 * @return recent
	 */
	public boolean isRecent() {
		return recent;
	}
	
	
	/**
	 * Gibt zurück, ob die Config noch aktuell zur Programmversion ist
	 * @return recent
	 */
	public boolean exists() {
		return exists;
	}
	

	
	/**
	 * Gibt die in der INI-Datei gespeicherte Fenster-Geometrie zurück
	 * @return wini
	 */
	public Rectangle getWinInfo() {
	    String[] tmp = values[INDEX_WININFO][2].split(",");
	    try {
	        return new Rectangle(Integer.parseInt(tmp[0]),Integer.parseInt(tmp[1]),Integer.parseInt(tmp[2]),Integer.parseInt(tmp[3]));
	    }
	    catch(Exception e) {
		    if (Define.doDebug()) {
		        new Logger("Warnung: Config enthielt Fehler (wininfo).", true);
		        new Logger(e.toString(), true);
		    }
	        String[] tmp_2 = Define.getWinInfo().split(",");
	        return new Rectangle(Integer.parseInt(tmp_2[0]),Integer.parseInt(tmp_2[1]),Integer.parseInt(tmp_2[2]),Integer.parseInt(tmp_2[3]));
	    }
	}
	
	/**
	 * Setzt die Fenster-Geometrie
	 * @param wini
	 */
	public void setWinInfo(Rectangle wini) {
		values[INDEX_WININFO][2]=wini.x + "," + wini.y + "," + wini.width + "," + wini.height;
	}
	
	/**
	 * Gibt die in der INI-Datei gespeicherte Fenster-Geometrie(Eintrags-Fenster) zurück
	 * @return wini
	 */
	public Point getWinInfoUE() {
	    String[] tmp = values[INDEX_WININFOUE][2].split(",");
	    try {
	        return new Point(Integer.parseInt(tmp[0]),Integer.parseInt(tmp[1]));
	    }
	    catch(Exception e) {
		    if (Define.doDebug()) {
		        new Logger("Warnung: Config enthielt Fehler (wininfo_ue).", true);
		        new Logger(e.toString(), true);
		    }
	        String[] tmp_2 = Define.getWinInfoUE().split(",");
	        return new Point(Integer.parseInt(tmp_2[0]),Integer.parseInt(tmp_2[1]));
	    }
	}
	
	/**
	 * Setzt die Fenster-Geometrie(Eintrags-Fenster)
	 * @param wini
	 */
	public void setWinInfoUE(Point wini) {
		values[INDEX_WININFOUE][2]=wini.x + "," + wini.y;
	}
	
	
	/**
	 * Ließt die Spaltenbreiten für die Eventstabelle aus
	 * @return wi
	 */
	public int[] getColumnWidths() {
	    String[] tmp = values[INDEX_COLWIDTH][2].split(",");
	    int[] result = new int[tmp.length];
	    try {
	        for (int i = 0; i < tmp.length; i++) {
	            result[i] = Integer.parseInt(tmp[i]);
	        }
	    }
	    catch(Exception e) {
		    if (Define.doDebug()) {
		        new Logger("Warnung: Config enthielt Fehler (column_widths).", true);
		        //new Logger(e.toString(), true);
		    }
	        result[0] = -1;
	    }
	    return result;
	}
	
	/**
	 * Setzt die Spaltenbreiten der Eventstabelle
	 * @param wi
	 */
	public void setColumnWidths(String wi) {
		values[INDEX_COLWIDTH][2]=wi;
	}
	
	
	/**
	 * Gibt die in der INI-Datei gespeicherte Telefon-Nummer zurück
	 * @return telnr
	 */
	public String getTelNr() {
		return values[INDEX_TEL][2];
	}
	
	/**
	 * Setzt die Telefon-Nummer
	 * @param telnr
	 */
	public void setTelNr(String telnr) {
		if (telnr.length()>0) {
		    values[INDEX_TEL][2]=telnr;
		}
	}
	
	/**
	 * Splashscreen anzeigen?
	 * @return splash
	 */
	public boolean getSplash() {
	    if (values[INDEX_SPLASH][2].equals("0")) return false;
	    else return true;
	}
	
	/**
	 * Splashscreen anzeigen?
	 * @param show
	 */
	public void setSplash(boolean show) {
		if (show) values[INDEX_SPLASH][2]="1";
		else values[INDEX_SPLASH][2]="0";
	}
	
	/**
	 * userEvents bei Download uploaden?
	 * @return update
	 */
	public boolean getAutoUpdate() {
	    if (values[INDEX_AUTO][2].equals("0")) return false;
	    else return true;
	}
	
	/**
	 * userEvents bei Download uploaden?
	 * @param update
	 */
	public void setAutoUpdate(boolean update) {
		if (update) values[INDEX_AUTO][2]="1";
		else values[INDEX_AUTO][2]="0";
	}
	
	/**
	 * Systray-Support aktivieren?
	 * @return systray
	 */
	public boolean getSystray() {
	    if (values[INDEX_SYSTRAY][2].equals("0")) return false;
	    else return true;
	}
	
	/**
	 * Systray-Support aktivieren?
	 * @param systray
	 */
	public void setSystray(boolean systray) {
		if (systray) values[INDEX_SYSTRAY][2]="1";
		else values[INDEX_SYSTRAY][2]="0";
	}
	
	/**
	 * Ort oder Event-Name im Baum als Blatt darstellen
	 * @return col
	 */
	public int getViewCol() {
	    if (values[INDEX_VIEW_COL][2].equals("1")) return Define.VIEW_COL_LOC;
	    return Define.VIEW_COL_NAME;
	}
	
	/**
	 * Ort oder Event-Name im Baum als Blatt darstellen
	 * @param col
	 */
	public void setViewCol(int col) {
		if (col == Define.VIEW_COL_LOC) values[INDEX_VIEW_COL][2]="1";
		else values[INDEX_VIEW_COL][2]="0";
	}
	
	/**
	 * Fenster-Geometrie speichern?
	 * @return save
	 */
	public boolean getSaveWinInfo() {
	    if (values[INDEX_WINSAVE][2].equals("0")) return false;
	    return true;
	}
	
	/**
	 * Fenster-Geometrie speichern?
	 * @param save
	 */
	public void setSaveWinInfo(boolean save) {
		if (save) values[INDEX_WINSAVE][2]="1";
		else values[INDEX_WINSAVE][2]="0";
	}
	
	/**
	 * Gibt die in der INI-Datei gespeicherte Homepage zurück
	 * @return homepage
	 */
	public String getUrl() {
		return values[INDEX_URL][2];
	}
	
	/**
	 * Setzt die Homepage
	 * @param url
	 */
	public void setUrl(String url) {
		if (url.length()>0) {
		    values[INDEX_URL][2]=url;
		}
	}
	
	/**
	 * Gibt die in der INI-Datei gespeicherte eMail-Adresse zurück
	 * @return email
	 */
	public String geteMail() {
		return values[INDEX_EMAIL][2];
	}
	
	/**
	 * Setzt die eMail-Adresse
	 * @param email
	 */
	public void seteMail(String email) {
		if (email.length()>0) {
		    values[INDEX_EMAIL][2]=email;
		}
	}
	
	/**
	 * Gibt die in der INI-Datei gespeicherte ProxyUrl zurück
	 * @return proxyHost
	 */
	public String getProxyUrl() {
		return values[INDEX_PURL][2];
	}
	
	/**
	 * Setzt die ProxyUrl
	 * @param proxyUrl
	 */
	public void setProxyUrl(String proxyURL) {
	    values[INDEX_PURL][2]=proxyURL;
	}
	
	/**
	 * Gibt den in der INI-Datei gespeicherten ProxyHost zurück
	 * @return proxyHost
	 */
	public String getProxyHost() {
		return values[INDEX_PHOST][2];
	}
	
	/**
	 * Setzt den ProxyHost
	 * @param proxyHost
	 */
	public void setProxyHost(String proxyHost) {
	    values[INDEX_PHOST][2]=proxyHost;
	}
	
	/**
	 * Gibt den in der INI-Datei gespeicherten ProxyPort zurück
	 * @return proxyHost
	 */
	public String getProxyPort() {
		return values[INDEX_PPORT][2];
	}
	
	/**
	 * Setzt den ProxyPort
	 * @param proxyPort
	 */
	public void setProxyPort(String proxyPort) {
	    values[INDEX_PPORT][2]=proxyPort;
	}
	
	/**
	 * Gibt den in der INI-Datei gespeicherten ProxyUser zurück
	 * @return proxyUser
	 */
	public String getProxyUser() {
		return values[INDEX_PUSER][2];
	}
	
	/**
	 * Setzt den ProxyHost
	 * @param proxyHost
	 */
	public void setProxyUser(String proxyUser) {
	    values[INDEX_PUSER][2]=proxyUser;
	}
	
	/**
	 * Gibt den in der INI-Datei gespeicherten ProxyUser zurück
	 * @return proxyUser
	 */
	public String getProxyPass() {
		return values[INDEX_PPASS][2];
	}
	
	/**
	 * Setzt den ProxyHost
	 * @param proxyHost
	 */
	public void setProxyPass(String proxyPass) {
	    values[INDEX_PPASS][2]=proxyPass;
	}
	
	/**
	 * Gibt das in der INI-Datei gespeicherte Passwort zurück
	 * @return password
	 */
	public String getPassword() {
		return values[INDEX_PASS][2];
	}

	/**
	 * Verschlüsselt und setzt das Passwort
	 * @param password
	 */
	public void setPassword(String pw) {
		if (pw.length()>3 && !pw.equals("(wird hier nicht angezeigt)")) {
			try {
				MessageDigest md = MessageDigest.getInstance("md5");
				md.update(pw.getBytes());
				byte[] tmp = md.digest();
				String new_pw="";
				for (int i = 0; i < tmp.length; ++i) {	new_pw+=Base.toHexString(tmp[i]); }
				values[INDEX_PASS][2]=new_pw;
			}
			catch (NoSuchAlgorithmException e) { 
				if (Define.doDebug()) { 
				    new Logger(e.toString(), true);
				}
			}
		}
	}
	
	/**
	 * Gibt den in der INI-Datei gespeicherten Usernamen zurück
	 * @return username
	 */
	public String getUsername() {
		return values[INDEX_USER][2];
	}
	
	/**
	 * Setzt den Usernamen
	 * @param username
	 */
	public void setUsername(String user) {
		if (user.length()>4) {
		    values[INDEX_USER][2]=user;
		}
	}
	
	/**
	 * Gibt das in der INI-Datei gespeicherte Look&Feel zurück
	 * @return L&F
	 */
	public String getLF() {
		return values[INDEX_LF][2];
	}
	
	/**
	 * Speichert das Argument key in der INI-Datei
	 * @param laf
	 */
	public void setLF(String laf) {
	    values[INDEX_LF][2]=laf;
	}
	
	
	/**
	 * Gibt das Anzeige-Format für die Events zurück
	 * @return view
	 */
	public String getView() {
		return values[INDEX_VIEW][2];
	}
	
	/**
	 * Speichert das Anzeige-Format für die Events in der INI-Datei
	 * @param view
	 */
	public void setView(String view) {
	    values[INDEX_VIEW][2]=view;
	}
	
	
	/**
	 * Gibt die Konstante zurück
	 * @return const
	 */
	public int getConst() {
		try {
			return Integer.parseInt(DownloadThread.getConstFromSave(values[INDEX_CONST][2]));
		}
		catch (Exception e) {
			if (Define.doDebug>1) {
			    new Logger("Failed const: " + values[INDEX_CONST][2], true);
			    new Logger(e.toString(), true);
			}
			return 0;
		}
	}
	
	/**
	 * Setzt die Konstante auf die angegebene
	 * @param const
	 * @return boolean
	 */
	public boolean setConst(String data) {
		if (!data.equals("")) {
		    values[INDEX_CONST][2]=DownloadThread.getKeyToSave(data);
			if (Define.doDebug>1) {	// Debug-Mode
			    new Logger("saved Key:" + values[INDEX_CONST][2], true);
			}
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Schreibt die Einstellungen in die INI-Datei
	 */
	public void writeFile() {
		try {
		    IniWriter ini = new IniWriter(values, Define.getConfigFilename());
		    ini.close();
			new Logger("Einstellungen gespeichert.",true);
		}
		catch (Exception e) {
		    new Logger("Speichern fehlgeschlagen",true);
			if (Define.doDebug>1) {	// Debug-Mode
			    new Logger(" (" + e.toString() + ")", true);
			}
			new Logger(".", true);
		}
	
	}

}
