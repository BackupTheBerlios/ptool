/*
 * Created on 09.12.2003
 * by Enrico Tröger
 */

package de.partysoke.psagent.download;

import java.net.*;
import java.io.*;

import de.partysoke.psagent.*;
import de.partysoke.psagent.gui.*;
import de.partysoke.psagent.util.*;

public class DownloadThread extends Thread
{
	// Klassenvariablen
    //private static String addr = "http://ptool.berlios.de/ptool_web/";
	private static String addr = "http://mars.lan.eht:666/ptool_web/";		// nur für Testzwecke
	private static final String file1 = "ptool-";
	private static final String file2 = "getead-";
	private static final String file3 = "sendUE-";
	private static final String ending = ".php";

	private static String failed = "";
	
	// Instanzvariablen
	private DownDialog parent;
	private boolean running = false;
	
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

	public static int sendUserEvents() throws IOException {
		
	    int rc = -4;
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    String[] userEvents = 
	        Base.parseUserEvents1D(
	                FileIO.readFile(
	                        Define.getUserEADFileName(), false
	                )
	        );
	    Config conf = Start.getConf();
		
	    URL url = new URL(addr+file3+Define.getVersionAsString()+ending+"?user="+conf.getUsername()+"&pass="+conf.getPassword()+"&nc=4");
	    HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setRequestMethod("POST");
		con.connect();

		OutputStream os = con.getOutputStream();
		    
		String tmp = "";
		for (int i=0; i < userEvents.length; i++) {
		    userEvents[i] = userEvents[i].replaceAll("&", "und");
		    tmp += "a" + i + "=" + userEvents[i] + "&";
		}
	    os.write(tmp.getBytes(Define.getEncoding()));
		os.flush();   
		os.close();

		// Rückgabe des Scriptes
		InputStream is = con.getInputStream();
		if (is.available()>0) {
		    int len;
		    byte[] b = new byte[100];
			while ((len = is.read(b)) != -1) {
				out.write(b, 0, len);
			}
			out.close();
		}
	    is.close();

	    if (Define.doDebug > 1) Base.LogThis("HTTP Status-Code(sendUserEvents): " + con.getResponseCode());
	    con.disconnect();

	    if (out.toString().equals("-2")) rc = -2;
	    else if (out.toString().equals("-3")) rc = -3;
	    String[] failedEventsTemp =  out.toString().split("\\|");
	    int[] failedEvents = new int[failedEventsTemp.length];
	    failedEvents[0] = -4;
	    
	    // Fehler finden & Fehlermeldung erzeugen
  	    String[] tmpEvent;
	    try {
		    for (int x = 0; x < failedEventsTemp.length; x++) {
		        failedEvents[x] = Integer.parseInt(failedEventsTemp[x]);
		        tmpEvent = userEvents[x].split("\\|");
		        failed += tmpEvent[0] + " in " + tmpEvent[1] + " am " + 
		        			tmpEvent[3] + "." + tmpEvent[4] + "." + tmpEvent[5] + "\n";
		    }
		}
		catch (NumberFormatException e) { 
		    if (Define.doDebug > 1) Base.LogThis("sendUserEvents: " + e.toString());
		}
		
		// eingetragene Events löschen
		if (failedEvents[0] == -1) {
		    rc = -1;
		    if (!FileIO.deleteFile(Define.getUserEADFileName()))
		            Base.LogThis("UserEvents konnten nicht gelöscht werden!");
		}
		// erfolgreiche Events löschen
		else if (failedEvents[0] >= 0) {
		    rc = 1;
		    userEvents = Base.deleteArrayElements(userEvents, failedEvents);
		}
		return rc;

	}
	
	/**
	 * Holt die News oder die Event-Rohdaten aus dem Netz und speichert sie lokal unter "Dateiname" ab
	 * param file
	 */
	public static boolean getData2(String file) throws IOException {
		Config conf = Start.getConf();
		String uri;
		if (file.equals(Define.getNewsFileName())) { uri=file1; }
		else { uri=file2; }
				
		URL url = new URL(addr+uri+Define.getVersionAsString()+ending+"?user="+conf.getUsername()+"&pass="+conf.getPassword()+"&nc=4");
		HttpURLConnection con = (HttpURLConnection)url.openConnection();
		con.addRequestProperty("User-Agent", "Mozilla/5.0 " + Define.getOwnName() + "/" + Define.getVersionAsString());
		InputStream in = con.getInputStream();
		
		if (in.available()>0) {
			FileOutputStream out = new FileOutputStream(file);
		    int len;
			byte[] b = new byte[100];
			while ((len = in.read(b)) != -1) {
				out.write(b, 0, len);
			}
			out.close();
			b = null;
			out = null;
		}
		in.close();
		conf = null;
		url = null;
		in = null;
		return true;
		
	}
	
	private static void getData1(String file) throws IOException {

		Config conf = Start.getConf();
		String dos = System.getProperty("os.version");
		String ver = Define.getVersionAsString();
		URL url = new URL(addr+file1+Define.getVersionAsString()+ending+"?user="+conf.getUsername()+"&pass="+conf.getPassword());
		HttpURLConnection con = (HttpURLConnection)url.openConnection();
		con.addRequestProperty("User-Agent", "Mozilla/5.0 " + Define.getOwnName() + "/" + Define.getVersionAsString() + " " + ver + " " + dos);
		InputStream in = con.getInputStream();
				
		if (in.available()>0) {
			FileOutputStream out = new FileOutputStream(file);
			int len;
			byte[] b = new byte[4096];
			while ((len = in.read(b)) > 0) {
       			out.write(b, 0, len);
	        }
			out.close();
			b = null;
			out = null;
		}
		in.close();
		in = null;
		conf = null;
		url = null;
		
	
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
	
	public static String getKey() throws IOException{
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		Config conf = Start.getConf();
		URL url = new URL(addr+file1+Define.getVersionAsString()+ending+"?user="+conf.getUsername()+"&pass="+conf.getPassword()+"&nc=1");
		//System.out.println(url.toString());
		HttpURLConnection con = (HttpURLConnection)url.openConnection();
		con.addRequestProperty("User-Agent", "Mozilla/5.0 " + Define.getOwnName() + "/" + Define.getVersionAsString());
		InputStream in = con.getInputStream();
		int len;
		byte[] b = new byte[100];
		while ((len = in.read(b)) != -1) {
			buffer.write(b,0,len);			
		}
		in.close();
		in = null;
		conf = null;
		url = null;
		
		return getKeyFromString(code(buffer.toString(),17));
	}
	
	public static String getKeyFromString(String temp) {
		
		String target="";
		if (temp.equals("<")) {
			return "";
		}
		else {
		    target+=temp.charAt(0);
		    target+=temp.charAt(1);
			if (Define.doDebug>1) {	// Debug-Mode
				Base.LogThis("decodierter-master-key: " + temp, true);
				Base.LogThis("Key:" + target, true);
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

	public void destroy() {
	    this.running = false;
	    Base.LogThis("Download abgebrochen...", true);
	    if (Define.doDebug>1)
			Base.LogThis("this.running = " + this.running, true);
		
	}
	
	public void run()
	{
	    this.running = true;
	    
	    if (Define.doDebug())
			Base.LogThis("Download l\u00E4uft...", true);
		
	    String keyStatus = "";
	    int rc = -4;
		try {
			
		    parent.ProgressBarNext(5);
			
		    if (this.running && Base.getUserEventsCount() > 0) {
		        rc = sendUserEvents();
		        parent.ProgressBarNext(15);
		    }
		    else {
		        rc = -1;
		        parent.ProgressBarNext(15);
		    }
		    
		    if (this.running) {
		        keyStatus = DownloadThread.getKey();
			    parent.ProgressBarNext(15);
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
	  	  		parent.ProgressBarNext(10);
		    }

		} 
		catch (IOException e) {
			if (Define.doDebug>2)
			    Base.LogThis("Exception beim Download: " + e.toString(), true);
		}
		if (this.running)
		    this.parent.finish(keyStatus, rc, failed);

		if (this.running)
		    if (Define.doDebug())
				Base.LogThis("Download fertig (100%)...", true);
		
		this.running = false;
	}

	
}
