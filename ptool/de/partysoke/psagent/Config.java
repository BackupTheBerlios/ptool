/*
 * Created on 29.07.2004
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
    private String sys = Base.getOS() == Define.WINDOWS_OS ? "1" : "0"; 
    
    /** 
     * 2D-String-Array, beinhaltet alle Config-Variablen, Struktur:<br>
     * [section][key][value]
     */
    private String[][] values = { 
	        { name, "const", ""},
			{ name, "lf", "win"},
			{ name, "wininfo", Define.getWininfo()},
			{ name, "win_save", "1"},
			{ name, "auto_upload", "0"},
			{ name, "splash", "1"},
			{ name, "systray", sys},
			{ name, "username", ""},
			{ name, "password", ""},
			{ name, "email", ""},
			{ name, "url", ""},
			{ name, "telefon", ""},
			{ name, "phost", ""},
			{ name, "pport", ""},
			{ name, "puser", ""},
			{ name, "ppass", ""} };

	/**
	 * Ließt die INI-Datei ein und stellt deren Variablen dem Programm zur Verfügung
	 * in Form eines Arrays
	 * (Falls INI-Datei nicht existiert, werden Std.-Werte benutzt)
	 */
	public Config() {

		try {
		    IniHandler ini = new IniHandler(Define.getConfigFilename());
			for (int i = 0; i < values.length; i++) 
			    values[i][2]=ini.getPropertyString(values[i][0], values[i][1], values[i][2]);
		} 
		catch (IOException e) {
		    if (Define.doDebug()) {
		        new Logger("Fehler: Config konnte nicht gelesen werden.", true);
		        new Logger(e.toString(), true);
		    }
		}

	}
	
	/**
	 * Gibt die in der INI-Datei gespeicherte Fenster-Geometrie zurück
	 * @return wini
	 */
	public Rectangle getWinInfo() {
	    String[] tmp = values[2][2].split(",");
	    try {
	        return new Rectangle(Integer.parseInt(tmp[0]),Integer.parseInt(tmp[1]),Integer.parseInt(tmp[2]),Integer.parseInt(tmp[3]));
	    }
	    catch(Exception e) {
		    if (Define.doDebug()) {
		        new Logger("Warnung: Config enthielt Fehler (wininfo).", true);
		        new Logger(e.toString(), true);
		    }
	        String[] tmp_2 = Define.getWininfo().split(",");
	        return new Rectangle(Integer.parseInt(tmp_2[0]),Integer.parseInt(tmp_2[1]),Integer.parseInt(tmp_2[2]),Integer.parseInt(tmp_2[3]));
	    }
	}
	
	/**
	 * Setzt die Fenster-Geometrie
	 * @param wini
	 */
	public void setWinInfo(Rectangle wini) {
		values[2][2]=wini.x + "," + wini.y + "," + wini.width + "," + wini.height;
	}
	
	
	/**
	 * Gibt die in der INI-Datei gespeicherte Telefon-Nummer zurück
	 * @return telnr
	 */
	public String getTelNr() {
		return values[11][2];
	}
	
	/**
	 * Setzt die Telefon-Nummer
	 * @param telnr
	 */
	public void setTelNr(String telnr) {
		if (telnr.length()>0) {
		    values[11][2]=telnr;
		}
	}
	
	/**
	 * Splashscreen anzeigen?
	 * @return splash
	 */
	public boolean getSplash() {
	    if (values[5][2].equals("0")) return false;
	    else return true;
	}
	
	/**
	 * Splashscreen anzeigen?
	 * @param show
	 */
	public void setSplash(boolean show) {
		if (show) values[5][2]="1";
		else values[5][2]="0";
	}
	
	/**
	 * userEvents bei Download uploaden?
	 * @return update
	 */
	public boolean getAutoUpdate() {
	    if (values[4][2].equals("0")) return false;
	    else return true;
	}
	
	/**
	 * userEvents bei Download uploaden?
	 * @param update
	 */
	public void setAutoUpdate(boolean update) {
		if (update) values[4][2]="1";
		else values[4][2]="0";
	}
	
	/**
	 * userEvents bei Download uploaden?
	 * @return update
	 */
	public boolean getSystray() {
	    if (values[6][2].equals("0")) return false;
	    else return true;
	}
	
	/**
	 * userEvents bei Download uploaden?
	 * @param update
	 */
	public void setSystray(boolean update) {
		if (update) values[6][2]="1";
		else values[6][2]="0";
	}
	
	/**
	 * Fenster-Geometrie speichern?
	 * @return save
	 */
	public boolean getSaveWinInfo() {
	    if (values[3][2].equals("0")) return false;
	    return true;
	}
	
	/**
	 * Fenster-Geometrie speichern?
	 * @param save
	 */
	public void setSaveWinInfo(boolean save) {
		if (save) values[3][2]="1";
		else values[3][2]="0";
	}
	
	/**
	 * Gibt die in der INI-Datei gespeicherte Homepage zurück
	 * @return homepage
	 */
	public String getUrl() {
		return values[10][2];
	}
	
	/**
	 * Setzt die Homepage
	 * @param url
	 */
	public void setUrl(String url) {
		if (url.length()>0) {
		    values[10][2]=url;
		}
	}
	
	/**
	 * Gibt die in der INI-Datei gespeicherte eMail-Adresse zurück
	 * @return email
	 */
	public String geteMail() {
		return values[9][2];
	}
	
	/**
	 * Setzt die eMail-Adresse
	 * @param email
	 */
	public void seteMail(String email) {
		if (email.length()>0) {
		    values[9][2]=email;
		}
	}
	
	/**
	 * Gibt den in der INI-Datei gespeicherten ProxyHost zurück
	 * @return proxyHost
	 */
	public String getProxyHost() {
		return values[12][2];
	}
	
	/**
	 * Setzt den ProxyHost
	 * @param proxyHost
	 */
	public void setProxyHost(String proxyHost) {
	    values[12][2]=proxyHost;
	}
	
	/**
	 * Gibt den in der INI-Datei gespeicherten ProxyHost zurück
	 * @return proxyHost
	 */
	public String getProxyPort() {
		return values[13][2];
	}
	
	/**
	 * Setzt den ProxyPort
	 * @param proxyPort
	 */
	public void setProxyPort(String proxyPort) {
	    values[13][2]=proxyPort;
	}
	
	/**
	 * Gibt den in der INI-Datei gespeicherten ProxyUser zurück
	 * @return proxyUser
	 */
	public String getProxyUser() {
		return values[14][2];
	}
	
	/**
	 * Setzt den ProxyHost
	 * @param proxyHost
	 */
	public void setProxyUser(String proxyUser) {
	    values[14][2]=proxyUser;
	}
	
	/**
	 * Gibt den in der INI-Datei gespeicherten ProxyUser zurück
	 * @return proxyUser
	 */
	public String getProxyPass() {
		return values[15][2];
	}
	
	/**
	 * Setzt den ProxyHost
	 * @param proxyHost
	 */
	public void setProxyPass(String proxyPass) {
	    values[15][2]=proxyPass;
	}
	
	/**
	 * Gibt das in der INI-Datei gespeicherte Passwort zurück
	 * @return password
	 */
	public String getPassword() {
		return values[8][2];
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
				values[8][2]=new_pw;
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
		return values[7][2];
	}
	
	/**
	 * Setzt den Usernamen
	 * @param username
	 */
	public void setUsername(String user) {
		if (user.length()>4) {
		    values[7][2]=user;
		}
	}
	
	/**
	 * Gibt das in der INI-Datei gespeicherte Look&Feel zurück
	 * @return L&F
	 */
	public String getLF() {
		return values[1][2];
	}
	
	/**
	 * Speichert das Argument key in der INI-Datei
	 * @param key
	 */
	public void setLF(String laf) {
	    values[1][2]=laf;
	}
	
	
	/**
	 * Gibt den Key zurück
	 * @return key
	 */
	public int getKey() {
		try {
			return Integer.parseInt(DownloadThread.getKeyFromSave(values[0][2]));
		}
		catch (Exception e) {
			if (Define.doDebug>1) {
			    new Logger("Failed Key: " + values[0][2], true);
			    new Logger(e.toString(), true);
			}
			return 0;
		}
	}
	
	/**
	 * Setzt den Key auf den angegebenen
	 * @param key
	 * @return boolean
	 */
	public boolean setKey(String key) {
		if (!key.equals("")) {
		    values[0][2]=DownloadThread.getKeyToSave(key);
			if (Define.doDebug>1) {	// Debug-Mode
			    new Logger("saved Key:" + values[0][2], true);
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
