/*
 * Created on 08.12.2004
 * by Enrico Tröger
 */

 
package de.partysoke.psagent.download;

import java.io.*;

import de.partysoke.psagent.*;
import de.partysoke.psagent.gui.*;
import de.partysoke.psagent.util.*;



public class UploadThread extends DownloadThread {
	
    // Instanzvariablen
	private UploadDialog parent;
	private int rc = -4;
	private StringWriter failed = new StringWriter();
    
	
	/**
	 * Konstruktor
	 */
	public UploadThread(UploadDialog d) {
		this.parent = d;
	}
	

	public void sendUserEvents() throws IOException {
		
	    OutputStream out;
	    String[] userEvents = 
	        Base.parseUserEvents1D(
	                FileIO.readFile(
	                        Define.getUserEADFileName(), false
	                )
	        );
	    Config conf = Start.getConf();
		
		String tmp = "";
		for (int i=0; i < userEvents.length; i++) {
		    userEvents[i] = userEvents[i].replaceAll("&", "und");
		    tmp += "a" + i + "=" + userEvents[i] + "&";
		}

	    out = NetIO.sendToUrlGetResponse(
	            addr+file3+Define.getFullVersionAsString()+ending+"?user="+conf.getUsername()+"&pass="+conf.getPassword()+"&nc=4",
	            Define.getUA(),
	            tmp
	    );
	        

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
		        failed.write(tmpEvent[0] + " in " + tmpEvent[1] + " am " + 
		        			tmpEvent[3] + "." + tmpEvent[4] + "." + tmpEvent[5] + "\n");
		    }
		}
		catch (RuntimeException e) { 
		    if (Define.doDebug > 1) new Logger("sendUserEvents: " + e.toString());
		}
		
		// eingetragene Events löschen
		if (failedEvents[0] == -1) {
		    rc = -1;
		    if (!FileIO.deleteFile(Define.getUserEADFileName()))
		        new Logger("UserEvents konnten nicht gelöscht werden!");
		}
		// erfolgreiche Events löschen
		else if (failedEvents[0] >= 0) {
		    rc = 1;
		    userEvents = Base.deleteArrayElements(userEvents, failedEvents);
		    //DebugPrint.print_r(userEvents,"\n");
		    Base.writeEventsToFile1D(userEvents);
		}
	}
	
	
	/**
	 * Bricht den Download ab!
	 */
	public void destroy() {
	    this.running = false;
	    new Logger("Upload abgebrochen...", true);
	    if (Define.doDebug>1)
	        new Logger("this.running = " + this.running, true);
		
	}
	
	public void run()
	{
	    this.running = true;
	    
	    if (Define.doDebug())
	        new Logger("Event-Upload l\u00E4uft...", true);
		
	    // Häßlich, aber man soll meinen schönen Dialog
	    // wenigstens kurz auch bei schnellen Verbindungen sehen
	    try {
	        sleep(1500);
	    }
	    catch (InterruptedException e) {
	        if (Define.doDebug()) new Logger(e.toString());
	    }
	    
	    try {
			this.sendUserEvents();
		} 
		catch (IOException e) {
			if (Define.doDebug>2) {
			    //new Logger("Exception beim Upload: " + e.toString(), true);
			}
		}
		
		if (this.running) {
		    if (this.parent != null) this.parent.finish(rc, failed);
		}
		
		if (this.running)
		    if (Define.doDebug())
		        new Logger("Event-Upload fertig.", true);
		
		this.running = false;
	}


}
