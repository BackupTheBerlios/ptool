/*
 * Created on 09.12.2004
 * by Enrico Tröger
 */


package de.partysoke.psagent.util;

import java.io.*;
import java.net.*;
import java.util.zip.GZIPInputStream;

import de.partysoke.psagent.Define;


/**
 * Klasse zum Schreiben und Lesen von Netzwerk-Streams<br>
 * (Hier liegt das ganze Netzwerk-IO-Handling)
 * 
 * @author Enrico Tröger
 */

public class NetIO {

    
    public static void getFromUrlToFile(String uri, String ua, String file, int bufferlen, boolean unzipStream) throws IOException {
        				
		URL url = new URL(uri);
		HttpURLConnection con = (HttpURLConnection)url.openConnection();
		con.addRequestProperty("User-Agent", ua);
		InputStream in;
		if (unzipStream) in = new GZIPInputStream(con.getInputStream()); 
		else in = con.getInputStream();
		
		if (in.available()>0) {
			FileOutputStream out = new FileOutputStream(file);
			int len;
			byte[] b = new byte[bufferlen];
			while ((len = in.read(b)) > 0) {
       			out.write(b, 0, len);
	        }
			out.close();
			b = null;
			out = null;
		}
		in.close();
		url = null;
		in = null;
    }
    
    public static void getFromUrlToFile(String uri, String ua, String file) throws IOException  {
        getFromUrlToFile(uri, ua, file, 1024, false);
    }
    
    public static void getFromUrlToFile(String uri, String ua, String file, int blen) throws IOException  {
        getFromUrlToFile(uri, ua, file, blen, false);
    }
    
    public static OutputStream getFromUrlToStream(String uri, String ua, int bufferlen) throws IOException {
		
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    	URL url = new URL(uri);
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.addRequestProperty("User-Agent", ua);
        InputStream in = con.getInputStream();

        if (in.available()>0) {
            int len;
            byte[] b = new byte[bufferlen];
            while ((len = in.read(b)) > 0) {
                buffer.write(b, 0, len);
            }
            b = null;
        }
        in.close();
        url = null;
        in = null;
        return buffer;
    }
    
    public static OutputStream getFromUrlToStream(String uri, String ua) throws IOException {
        return getFromUrlToStream(uri, ua, 256);
    }

    
    public static OutputStream sendToUrlGetResponse(String uri, String ua, String toSend)
    throws IOException {
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        URL url = new URL(uri);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setDoInput(true);
    	con.setDoOutput(true);
    	con.addRequestProperty("User-Agent", ua);
        con.setRequestMethod("POST");
    	con.connect();

    	OutputStream os = con.getOutputStream();
    	    
    	os.write(toSend.getBytes(Define.getEncoding()));
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

	    if (Define.doDebug > 1) new Logger("HTTP Status-Code(sendUserEvents): " + con.getResponseCode());
	    con.disconnect();

        return out;
    }
    
}
