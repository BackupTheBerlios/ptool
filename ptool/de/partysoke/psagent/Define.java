/*
 * Created on 12.10.2004
 * by Enrico Tröger
*/

package de.partysoke.psagent;

/**
 * Klasse zum Definieren von globalen Konstanten
 * 
 */

public class Define {

    /** Name der Anwendung */
    private static final String ownName = "PSAgent";
	
	/** festgelegtes Debuglevel (Release = 0, Beta = 1, Devel = 2|3) */
	public static int doDebug=1;
	
    /** Version der Anwendung */
	private static final int[] version = { 1 , 0 , 3 };
	
	/** Programm-weites Encoding */
	private static final String encoding = "UTF-8";
	//public static final String encoding = "ISO-8859-1";
	
	/** Url von PartySOKe.de */
	private static final String url_partysoke = "http://www.PartySOKe.de";
	
	/** Url des Forums */
	private static final String url_forum = "http://forum.PartySOKe.de";
	
	/** Url der Anwendung */
	private static final String url_self = "http://psagent.PartySOKe.de";
	
	/** User-Agent(für HTTP-Verbindungen) der Anwendung */
	private static final String ua = "Mozilla/5.0 " + ownName + "/" + getVersionAsString();
	
	/** Bezeichnungen der Spalten der Events-Tabelle */
	private static final String[] spalten = { "Name","Ort","Datum","Genre","Band(s)" };
	
	/** Default-Position des Fensters */
    private static final String wininfo = "50,50,650,600";

	/** Default-Position des Eintrags-Fensters */
    private static final String wininfo_ue = "100,100";


    /** Home-Dir, wo die Daten-Dateien und Einstellungen gespeichert werden */
	private static final String homeDir = System.getProperty("user.home") + 
										 System.getProperty("file.separator") +
										 ".psagent" +
										 System.getProperty("file.separator");
	
	/** Datei, in der die Event-Daten sind */
	private static final String eventsFile = Define.homeDir() + "psagent.dat";
	
	/** Datei, in der die Konfigurations-Optionen stehen */
	private static final String configFile = Define.homeDir() + "psagentrc";
	
	/** Datei, in der die Debug-Informationen stehen */
	private static final String debugFile = "debug.txt";
	
	/** Datei, in der die News-Daten sind */
	private static final String newsFile = Define.homeDir() + "news.dat";
	
	/** Datei, in der die User-Event-Roh-Daten sind */
	private static final String eadFile = Define.homeDir() + "ead.dat";

	/** Datei, in der die User-Events sind */
	private static final String usereadFile = Define.homeDir() + "user.dat";

	/** temporäre Dati, die beim Drucken erzeugt wird */
	private static final String tmpFile = System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + "psagent.tmp";
	

    /** Meldung bei Fehler in der Verbindung */
	private static final String noCon = "Es ist ein Fehler aufgetreten.\nBesteht eine Internet-Verbindung?";

    /** Meldung bei Fehler in der DB-Verbindung */
	private static final String noDBCon = "Es konnte keine Verbindung zur Datenbank aufgebaut werden. Probiere es sp\u00E4ter nocheinmal.";

    /** Meldung bei falschen Userdaten */
	private static final String wrongUser = "Deine Benutzerdaten sind falsch.";

    /** Meldung bei Fehler in der Datenverarbeitung */
	private static final String miscFailure = "Es ist ein unerwarteter Fehler aufgetreten.";

    /** Meldung bei Fehler in der Datenverarbeitung */
	private static final String wrongData = "Es ist ein Fehler beim Verarbeiten der Daten aufgetreten.";

    /** Meldung bei Fehler beim Event-Eintragen */
	private static final String failedEvent = "Folgende Events konnten nicht eingetragen werden:\n";

	/** Meldung bei Erfolg beim Event-Eintragen */
	private static final String goodEvent = "Alle Events wurden erfolgreich eingetragen.";

	/** Meldung, wenn noch keine Events vorhanden */
	private static final String noEvents = 	"Es sind noch keine Daten vorhanden, bitte Daten herunterladen (Strg+U).";

    
    
    /** Used to identify the windows platform. */
    private static final String WIN_ID = "Windows";

    /** The default system browser under windows. */
    private static final String WIN_PATH = "rundll32";

    /** The flag to display a url. */
    private static final String WIN_FLAG = "url.dll,FileProtocolHandler";

    /** The default browser under unix. */
    private static final String UNIX_PATH = "netscape";

    /** The default browser under unix. */
    private static final String UNIX_ALT_PATH = "firefox";

    /** The default browser under unix. */
    private static final String UNIX_ALT2_PATH = "mozilla";

    /** The default browser under unix. */
    private static final String UNIX_ALT3_PATH = "konqueror";

    /** The flag to display a url. */
    private static final String UNIX_FLAG = "-remote openURL";

    
    public static final int DATE_DATE = 1;
    public static final int DATE_TIME = 2;
    public static final int DATE_DMY = 3;
    public static final int DATE_BOTH = 4;
    public static final int DATE_DAY = 4;
    public static final int DATE_MONTH = 5;
    public static final int DATE_YEAR = 6;
    public static final int DATE_HOUR = 7;
    public static final int DATE_MINUTE = 8;
    public static final int DATE_SECOND = 9;

    public static final int EVENTDOWNLOAD = 0;
    public static final int EVENTUPLOAD = 1;

    public static final int VIEW_COL_NAME = 0;
    public static final int VIEW_COL_LOC = 1;

    private static final String EventDownloadTitle = "Daten herunterladen";
    private static final String EventUploadTitle = "Eigene Events hochladen";
    
    public static final String ImageIcon = "images/icon.jpg";
    public static final String TrayIcon = "images/tray.jpg";
    public static final String ImageSplash = "images/splash.jpg";
    public static final String ImageAbout = "images/about.jpg";
    
    
    /** Konstante, falls das OS Windows ist  */
    public static final int WINDOWS_OS = 0;
    /** Konstante, falls das OS Linux ist  */
    public static final int LINUX_OS = 1;
    /** Konstante, falls das OS weder Windows noch Linux ist  */
    public static final int OTHER_OS = 2;

    
    /*
     * Getter-Methoden für die Konstanten 
     */
    
    
	/**
	 * Gibt einen String über ein nicht-eingetragenes Event zurück.
	 * @return miscFailure
	 */
    public static String getFailedEvent() {
        return failedEvent;
    }

	/**
	 * Gibt einen String über einen nicht-erwarteten Fehler zurück.
	 * @return miscFailure
	 */
    public static String getMiscFailure() {
        return miscFailure;
    }

	/**
	 * Gibt einen String über eine nicht-bestehende Internetverbindung zurück.
	 * @return noCon
	 */
    public static String getNoCon() {
        return noCon;
    }

    /**
  	 * Gibt einen String über eine nicht-bestehende DB-Verbindung zurück.
  	 * @return encoding
  	 */
  	public static String getNoDBCon() {
      return noDBCon;
    }
  	
	/**
  	 * Gibt einen String über falsche Userdaten zurück.
  	 * @return encoding
  	 */
  	public static String getWrongUser() {
      return wrongUser;
    }
  	
	/**
  	 * Gibt einen String über nichtlesbare Daten zurück.
  	 * @return encoding
  	 */
  	public static String getWrongData() {
      return wrongData;
    }
  	
	/**
  	 * Gibt das Encoding als String zurück
  	 * @return encoding
  	 */
  	public static String getEncoding() {
      return encoding;
    }
  	
	/**
  	 * Gibt zurück, ob Debug-Ausgaben erfolgen sollen
  	 * @return debugging
  	 */
  	public static boolean doDebug() {
      return (doDebug>0) ? true : false;
    }
  	
  	
    /**
     * Gibt das Home-Verzeichnis zurück
     * @return homdir
     */
    public static String homeDir() {
    	return homeDir;
    }
    
    /**
     * Gibt die Kopf-Spalten der Tabelle als String-Array zurück
     * @return spalten
     */
    public static String[] getSpalten() {
        return spalten;
    }

    /**
     * Gibt die aktuelle Programmversion als String(mit Punkten) zurück
     * @return version
     */
    public static String getVersionAsString() {
    		return version[0]+"."+version[1];
    }
    
    /**
     * Gibt die aktuelle Programmversion als String(mit Punkten) zurück
     * @return version
     */
    public static String getFullVersionAsString() {
    		return version[0]+"."+version[1]+"."+version[2];
    }
    
    /**
     * Gibt die aktuelle Programmversion als Integer-Feld zurück
     * @return version
     */
    public static int[] getVersion() {
    	return version;
    }
  
    /**
	 * Gibt den Namen der Events-Datei zurück.
	 * @return events-Dateiname
	 */
	public static String getEventsFile() {
		return eventsFile;
	}

    /**
	 * Gibt den Namen der ini-Datei zurück.
	 * @return ini-Dateiname
	 */
	public static String getConfigFilename() {
		return configFile;
	}

	/**
	 * Gibt den Namen der Debug-Log-Datei zurück.
	 * @return debugFile
	 */
	public static String getDebugFileName() {
		return debugFile;
	}

	/**
	 * Gibt den Namen der News-Datei zurück.
	 * @return newsFile
	 */
	public static String getNewsFileName() {
		return newsFile;
	}

	/**
	 * Gibt den Namen der Event-Daten-Datei zurück.
	 * @return eadFile
	 */
	public static String getEADFileName() {
		return eadFile;
	}


	/**
	 * Gibt den Namen der User-Event-Daten-Datei zurück.
	 * @return eadFile
	 */
	public static String getUserEADFileName() {
		return usereadFile;
	}

    /**
     * @return Returns the ownName.
     */
    public static String getOwnName() {
        return ownName;
    }
    /**
     * @return Returns the url_forum.
     */
    public static String getUrl_forum() {
        return url_forum;
    }
    /**
     * @return Returns the url_partysoke.
     */
    public static String getUrl_partysoke() {
        return url_partysoke;
    }
    /**
     * @return Returns the url_self.
     */
    public static String getUrl_self() {
        return url_self;
    }
    /**
     * @return Returns the usereadFile.
     */
    public static String getUsereadFile() {
        return usereadFile;
    }
    
    /**
     * @return Returns the wininfo.
     */
    public static String getWinInfo() {
        return wininfo;
    }

    /**
     * @return Returns the wininfo.
     */
    public static String getWinInfoUE() {
        return wininfo_ue;
    }

    /**
     * @return Returns the UNIX_ALT_PATH.
     */
    public static String getUNIX_ALT_PATH() {
        return UNIX_ALT_PATH;
    }
    /**
     * @return Returns the UNIX_ALT2_PATH.
     */
    public static String getUNIX_ALT2_PATH() {
        return UNIX_ALT2_PATH;
    }
    /**
     * @return Returns the UNIX_ALT3_PATH.
     */
    public static String getUNIX_ALT3_PATH() {
        return UNIX_ALT3_PATH;
    }
    
    /**
     * @return Returns the UNIX_FLAG.
     */
    public static String getUNIX_FLAG() {
        return UNIX_FLAG;
    }
    
    /**
     * @return Returns the UNIX_PATH.
     */
    public static String getUNIX_PATH() {
        return UNIX_PATH;
    }
    
    /**
     * @return Returns the WIN_FLAG.
     */
    public static String getWIN_FLAG() {
        return WIN_FLAG;
    }
    
    /**
     * @return Returns the WIN_ID.
     */
    public static String getWIN_ID() {
        return WIN_ID;
    }
    
    /**
     * @return Returns the WIN_PATH.
     */
    public static String getWIN_PATH() {
        return WIN_PATH;
    }

    /**
     * Gibt den Titel des Update-Dialogs in Abhängigkeit von dem Modus
     * (Events runterladen, hochladen)
     * @param mode
     * @return Titel
     */
    public static String getUpdateTitle(int mode) {
        if (mode == Define.EVENTDOWNLOAD) return EventDownloadTitle;
        else return EventUploadTitle;
    }

    
    /**
     * @return Returns the goodEvent.
     */
    public static String getEventUploadOK() {
        return goodEvent;
    }

    
    /**
     * @return Returns the noEvents.
     */
    public static String getNoEvents() {
        return noEvents;
    }

    
    /**
     * @return Returns the ua.
     */
    public static String getUA() {
        return ua;
    }
    
    /**
     * @return Returns the tmpFile.
     */
    public static String getTmpFile() {
        return tmpFile;
    }
}
