package de.partysoke.psagent;

import de.partysoke.psagent.gui.*;
import de.partysoke.psagent.util.*;
import de.partysoke.psagent.download.*;

/**
 * Hauptklasse der Anwendung, enthält die main-Methode und einige statische Methoden
 * 
 * @author Enrico Tröger
 */

public class Start

{
	private static int[] newVersion = { 0 , 0 , 0 };
	private static Config conf;
	private static MWnd wnd;
	
	public static void main(String[] args) {
	    // Verzeichnis für Dateien anlegen/prüfen
	    Base.makeDir();
	    
	    conf = new Config();
	    
	    // Parameter verarbeiten & Debugging initialisieren
	    doParameters(args);
	    initDebug();
	    
	    // Splashscreen anzeigen, wenn das in der Config steht
	    Splash sp = null;
	    if (conf.getSplash()) sp = new Splash();
  	
	    // Hauptfenster erzeugen
	    wnd = new MWnd();
	    
	    
	    // System-Properties setzen
	    System.setProperty("file.encoding", Define.getEncoding());
	    setProxySettings();

	    // diverse Vorbereitungen
	    wnd.setBounds(conf.getWinInfo());
	    wnd.updateLF();
	    wnd.fillTable();
	    Base.readUserEventsCount();
	    wnd.updateStatusBarEventlabel();
	    wnd.updateFileMenuNews();
	    wnd.initSystray();
	    
	    // Splashscreen schließen, Hauptfenster anzeigen
	    wnd.setVisible(true);
	    if (conf.getSplash()) sp.endDialog();

	}
  	

	/**
	 * Setzt die neue Programmversion in ein int-Array
	 * @param newV
	 */
	public static void setNewVersion(int[] newV) {
	    newVersion=newV;
	}
  
	/**
	 * Gibt die neue Programmversion als String zurück
	 * @return newVersion
	 */
	public static String getNewVersionAsString() {
	    return String.valueOf(newVersion[0])+"."+String.valueOf(newVersion[1])+"."+String.valueOf(newVersion[2]);
	}
  
	/**
	 * Gibt die aktuelle Konfiguration als Config-Objekt zurück
	 * @return version
	 */
	public static Config getConf() {
	    return conf;
	}
  
	/**
	 * Gibt das Objekt MWnd (Hauptfenster) zurück
	 * @return wnd
	 */
	public static MWnd getMe() {
	    return wnd;
	}
  
	private static void doParameters(String[] arg) { 	
	    // Verbosity ausgeben
	    if (arg.length>0) {
	        if (arg[0].equals("--verbose") || arg[0].equals("-v")) {
	            if (Define.doDebug < 1) Define.doDebug=1;
	        }
	   
	        // Very Verbosity ausgeben
	        else if (arg[0].equals("-vv")) {
	            if (Define.doDebug < 2) Define.doDebug=2;
	        }
  	
	        // Version ausgeben
	        else if (arg[0].equals("--version") || arg[0].equals("-V")) {
	            System.out.println(Define.getOwnName() + " Version: " + Define.getVersionAsString());
	            System.exit(0);
	        }
  	
	        // Version ausgeben
	        else if (arg[0].equals("--server") || arg[0].equals("-s")) {
	            DownloadThread.setServer(arg[1]);
	        }
  	
	        // Hilfe ausgeben
	        else if (arg[0].equals("--help") || arg[0].equals("-h")) {
	            System.out.println(Define.getOwnName() + "  - Das PartySOKe.de Offline-Tool");
	            System.out.println();
	            System.out.println("-h\t--help\t\t- zeigt diese Hilfe an");
	            System.out.println("-V\t--version\t- gibt Informationen ueber die Version des Programms aus");
	            System.out.println("-v\t--verbose\t- gibt mehr Status-Meldungen aus");
	            System.out.println();
	            System.exit(0);
	        }
	
	        // Argument noch nicht behandelt?
	        else {
	            System.out.println("Ungueltiger Parameter \""+arg[0]+"\"");
	            System.out.println("Aufruf: " + Define.getOwnName() + " -hvV");	
	            System.exit(0);			
	        }	
	    }
	}
  	
	public static void initDebug() {
		// Debug-Logging initialisieren (vorherige Datei löschen, Infos dazuschreiben) 
    	if (Define.doDebug()) {
    	    FileIO.writeToFile(Define.getDebugFileName(),"");
    	    new Logger(Define.getOwnName() + " " + Define.getVersionAsString() + " Debug-File\r\nAchtung: Debugging aktiviert, "
    	            + "dies kann das Programm stark verlangsamen.\r\nBitte nur nach Aufforderung benutzen!\r\n"
    	            + "Diese Datei bitte unbedingt bei Bug-Reports mitschicken.\r\n", false);
    	    new Logger(Base.getSystemInfos(),false);
    	}
	}

	public static void setProxySettings() {
  	    if (! conf.getProxyHost().equals("") ) {
  	        System.out.println("proxy set");
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
  	}
    

}