/*
 * Created on 14.06.2004
 */


package de.partysoke.psagent.util;

import java.io.*;
import java.util.zip.*;

import de.partysoke.psagent.*;

/**
 * Klasse zum Schreiben und Lesen von Dateien<br>
 * (Hier liegt fast das ganze IO-Handling)
 * 
 * @author Enrico Tröger
 */
public class FileIO {

    
	/**
	 * Schreibt text in fileName, löscht die Datei vor dem Schreiben, wenn delete gesetzt ist
	 * 
	 * @param fileName
	 * @param text
	 * @param delete
	 */
	public static void writeToFile(String fileName, String text, boolean delete) {
		try {
		    File file = new File(fileName);
			FileWriter f = new FileWriter(file);
			BufferedWriter buf = new BufferedWriter(f);
		    
		    if (delete) file.delete();
			buf.write(text);
			buf.close();
			f.close();
		}
		catch (IOException e) { if (Define.doDebug()) Base.LogThis(e.toString()); }
	}
	

	/**
	 * @deprecated nur noch als Backup drin
	 * Hängt den String text an die Datei fileName an
	 * 
	 * @param fileName
	 * @param text
	 */
	public static void appendToFile2(String fileName, String text) {
		String tmp = "";
		boolean created = false;
		try {
			File file = new File(fileName);

			// File erzeugen, falls es noch nich existert
			if (file.exists()) {
			
				// Inhalt einlesen
				RandomAccessFile f1 = new RandomAccessFile(file, "r");
				int i=0;
				f1.seek(0);
				while((f1.getFilePointer() < f1.length())) {
					tmp += f1.readLine() + "\r\n";
					i++;
				}
				f1.close();
			} 
			else {
					try {
						file.createNewFile();
						created = true;
					}
					catch (Exception e) {}
			}
			FileOutputStream out = new FileOutputStream(file);
			if (!created) text = tmp + text;
			byte[] b = text.getBytes();
			out.write(b);
			out.close();
		} catch (IOException e) { if (Define.doDebug()) Base.LogThis(e.toString()); }
	}
	
	/**
	 * Hängt den String text an die Datei fileName an.
	 * 
	 * @param fileName
	 * @param text
	 */
	public static void appendToFile(String fileName, String text) {
		try {
		    FileWriter f = new FileWriter(fileName, true);
			BufferedWriter buf = new BufferedWriter(f);
		    
		    buf.write(text);
		    buf.close();
			f.close();
			
		} catch (IOException e) { if (Define.doDebug()) Base.LogThis(e.toString()); }
	}
	
	/**
	 * Ließt den Inhalt der komprimierten Datei file ein, und gibt ihn in einem String zurück.
	 *  
	 * @param file
	 * @return inhalt
	 */
	public static String readZippedFile(String zipfile) {
		
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		int bs = 8192;
		
		try
		{
			GZIPInputStream zipin =	new GZIPInputStream(new FileInputStream(zipfile));

	  		byte b[] = new byte[bs];
	  		int length;
			while ((length = zipin.read(b,0,bs)) != -1) {
				buffer.write(b,0,length );
			}
  			zipin.close();
		}
		catch (IOException e) { if (Define.doDebug()) Base.LogThis(e.toString()); }
		
		return buffer.toString();
	}


	/**
	 * Ließt den Inhalt der Datei file ein, und gibt ihn in einem StringWriter zurück.
	 * Ist returnFailure true, so wird eine Meldung im Fehlerfall zurückgegeben.
	 *  
	 * @param file
	 * @param returnFailure
	 * @return inhalt
	 */
	public static StringWriter readFileToWriter(String file, boolean returnFailure) {
	    StringWriter tmp = new StringWriter();
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
	        int c;
			while ((c = in.read()) != -1)
	            tmp.write(c);

			in.close();
		}
		catch (FileNotFoundException fnf) {
			if (returnFailure) {
			    tmp.write(fnf.toString());
			}
		}
		catch (IOException e) {
			if (returnFailure) {
				tmp.write(e.toString());
			}
		}
		return tmp;

	}
	
	
	/**
	 * Ließt den Inhalt der Datei file ein, und gibt ihn in einem String zurück.
	 * Ist returnFailure true, so wird eine Meldung im Fehlerfall zurückgegeben.
	 *  
	 * @param file
	 * @param returnFailure
	 * @return inhalt
	 */
	public static String readFile(String file, boolean returnFailure) {
		String tmp = "";
		
		try {
		    BufferedReader in = new BufferedReader(new FileReader(file));
			int c;
			while (in.ready())
	            tmp += in.readLine() + "\r\n";
			in.close();
		}
		catch (FileNotFoundException fnf) {
			if (returnFailure) {
				tmp=fnf.toString();
			}
		}
		catch (IOException e) {
			if (returnFailure) {
				tmp=e.toString();
			}
			if (Define.doDebug()) Base.LogThis(e.toString());
		}
		return tmp;
	}

	
	public static boolean deleteFile(String filename) {
	    
	    boolean success = false;
	    File f = new File(filename);
	   
	    success = f.delete();
	   
	    return success;
	    
	}
	
	
	
	
	
}
