/*
 * Created on 09.12.2003
 * by Enrico Tröger
 */

package de.partysoke.psagent.download;

import java.io.*;

import de.partysoke.psagent.*;
import de.partysoke.psagent.gui.*;
import de.partysoke.psagent.util.*;


public class DownloadThread extends Thread
{
	// Klassenvariablen
    protected static String addr = "http://ptool.berlios.de/ptool_web/";
    //protected static String addr = "http://mars.lan.eht:666/ptool_web/";		// nur für Testzwecke
	private static final String file1 = "ptool-";
	private static final String file2 = "getead-";
	protected static final String file3 = "sendUE-";
	protected static final String ending = ".php";
	private static String failure;
	
	
	// Instanzvariablen
	private DownDialog parent;
	protected boolean running = false;
	
	
	/**
	 * Default-Konstruktor
	 */
	public DownloadThread() {
		this.parent = null;
	}
	
	/**
	 * Konstruktor
	 */
	public DownloadThread(DownDialog d) {
		this.parent = d;
	}
	
	
	/**
	 * Ändert den Server, von dem alles geladen wird, nur eine Entwickler-Option
	 * @param neuer Server
	 */
	public static void setServer(String server) {
		addr = server;
	}
	
	/**
	 * Gibt die Url wieder, die für die Userdaten-Abfrage nötig ist.
	 * @return versionAddr
	 */
	public static String getUserDataAddr() {
		
		Config conf = Start.getConf();
		return addr+file1+Define.getVersionAsString()+ending+"?user="+conf.getUsername()+"&pass="+conf.getPassword()+"&nc=3";
	}
	
	/**
	 * Gibt die Url wieder, die für die Versionskontrolle nötig ist.
	 * @return versionAddr
	 */
	public static String getVersionAddr() {
		return addr+file1+Define.getVersionAsString()+ending+"?nc=2";
	}


	/**
	 * Holt die News oder die Event-Rohdaten aus dem Netz und speichert sie lokal unter filename ab
	 * param filename
	 */
	public static void getData2(String filename) throws IOException {
		Config conf = Start.getConf();
		String uri = (filename.equals(Define.getNewsFileName())) ? file1 : file2;
	
		NetIO.getFromUrlToFile(
		        addr+uri+Define.getVersionAsString()+ending+"?user="+conf.getUsername()+"&pass="+conf.getPassword()+"&nc=4",
		        Define.getUA(),
		        filename);
		
		
	}
	
	/**
	 * Holt die Events aus dem Netz und speichert sie lokal unter filename ab
	 * param filename
	 */
	private static void getData1(String filename) throws IOException {

		Config conf = Start.getConf();
				
		NetIO.getFromUrlToFile(
		        addr+file1+Define.getVersionAsString()+ending+"?user="+conf.getUsername()+"&pass="+conf.getPassword(),
		        "Mozilla/5.0 " + Define.getOwnName() + "/" + Define.getVersionAsString() + " " + Define.getVersionAsString() + " " + System.getProperty("os.version"),
		        filename, 1024, true);
	
	}
	
	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public static String getKey() throws IOException{
		Config conf = Start.getConf();
		
		return getKeyFromString(code(
		        NetIO.getFromUrlToStream(
		                addr+file1+Define.getVersionAsString()+ending+"?user="+conf.getUsername()+"&pass="+conf.getPassword()+"&nc=1",
		                Define.getUA()).toString(),
		        17));
	}
	
	/**
	 * "Erweitert" den Key zum Speichern in der Config-Datei
	 * @param String key
	 * @return String string
	 */
	public static String getKeyToSave(String key) {
		int[] digits = Base.getRandomDigits(6);
		// +""+ - sonst werden die Ints addiert anstatt konkateniert
		String string=digits[0]+""+digits[1]+""+digits[2]+key+digits[3]+""+digits[4]+""+digits[5];
		return code(string,67);
	}
	
	/**
	 * "Reduziert" den Key zum Laden aus der Config-Datei
	 * @param String encKey
	 * @return String key
	 */
	public static String getKeyFromSave(String encKey) {
		encKey=code(encKey,67);
		String key="";
		key+=encKey.charAt(3);
		key+=encKey.charAt(4);
		
		return key;
	}
	
	public static String getKeyFromString(String temp) {
		
		String target="";
		if (temp.equals("1") || temp.equals("2")) {
			failure = temp;
		    return "";
		}
		else {
		    target+=temp.charAt(0);
		    target+=temp.charAt(1);
			if (Define.doDebug>1) {	// Debug-Mode
			    new Logger("decodierter-master-key: " + temp, true);
			    new Logger("Key:" + target, true);
			}
			temp = null;
			return target;
		}
	
	}
	
	public static String code(String msg, int key) 
	{
		String result="";
		for (int i = 0; i < msg.length(); ++i) {
			result+=( (char) (msg.charAt(i) ^ key));
		}
		return result;
	}

	/**
	 * Bricht den Download ab!
	 */
	public void destroy() {
	    this.running = false;
	    new Logger("Download abgebrochen...", true);
	    if (Define.doDebug>1)
	        new Logger("this.running = " + this.running, true);
		
	}
	
	public void run()
	{
	    this.running = true;
	    
	    if (Define.doDebug())
	        new Logger("Download l\u00E4uft...", true);
		
	    String keyStatus = "";
	    try {
			
		    parent.ProgressBarNext(5);
			
		    if (this.running) {
		        keyStatus = DownloadThread.getKey();
			    parent.ProgressBarNext(10);
		    }
			
		    if (this.running) {
		        if (Start.getConf().getAutoUpdate()  && Base.getUserEventsCount() > 0) {
		            new UploadThread(null).sendUserEvents();
		            Base.readUserEventsCount();
		        }
		        parent.ProgressBarNext(5);
		    }
		    
		    if (this.running) {
		        getData1(Define.getEventsFile());
		        parent.ProgressBarNext(20);
		    }
		    
		    if (this.running) {
		        getData2(Define.getNewsFileName());
		        parent.ProgressBarNext(20);
		    }
		    
		    if (this.running) {
		        getData2(Define.getEADFileName());
		        parent.ProgressBarNext(20);
		    }
		    
		    if (this.running) {
		        Config conf = Start.getConf();
	  	  		if (conf.geteMail().equals("") && conf.getTelNr().equals("")  && conf.getUrl().equals("") ) {
	  	  	    	Base.getUserData();
	  	  		}
	  	  		parent.ProgressBarNext(20);
		    }

		} 
		catch (IOException e) {
			if (Define.doDebug>1)
			    new Logger("Exception beim Download: " + e.toString(), true);
		}
		if (this.running)
		    this.parent.finish(keyStatus, failure);

		if (this.running)
		    if (Define.doDebug())
		        new Logger("Download fertig (100%).", true);
		
		this.running = false;
	}

	
}
