/*
 * Created on 08.12.2004
 * by Enrico Tröger
 */

package de.partysoke.psagent.util;

import java.io.*;

import de.partysoke.psagent.*;

/**
 * Einfache Klasse zum Loggen von Meldungen
 */

public class Logger {

    /**
	 * Gibt text aus, wenn show gesetzt ist, und schreibt text in die Log-Datei
	 * @param text
	 * @param show
	 */
    public Logger(String text, boolean show) {
        StringWriter caller = new StringWriter();
        if (show) {
		    if (Define.doDebug()) {
		        // mal noch evtl. verbessern
		        StackTraceElement[] trace = new Throwable().getStackTrace();
		        String[] klasse = trace[1].getClassName().split("\\.");
		        caller.write(klasse[klasse.length-1] + "." + trace[1].getMethodName());
			    FileIO.appendToFile(Define.getDebugFileName(), 
			            Base.getDate(Define.DATE_BOTH) + " " + caller + ": " + text + "\r\n");
			    caller.write(": ");
		    }
		        
		    System.out.println(caller + text);
		}
		else {
		    FileIO.appendToFile(Define.getDebugFileName(), text + "\r\n");
		}
        caller = null;
	}
	
    
	/**
	 * Gibt text aus und schreibt text in die Log-Datei
	 * @param text
	 * @param show
	 */
    public Logger(String text) {
        new Logger(text, true);
	}
	
	/**
	 * Gibt text aus und schreibt text in die Log-Datei
	 * @param text
	 * @param show
	 */
    public Logger(int text) {
        new Logger(String.valueOf(text), true);
	}
	

}
