/*
 * Created on 07.12.2004
 * by Enrico Tröger
 */


package de.partysoke.psagent.util;


/**
 * Funktions-Container-Klasse, die wichtige Funktionen zum Ausgeben von Arrays
 * und anderen Sachen enthält und überwiegend Debug-zwecken dient.
 */
	
public class DebugPrint {
    
    
	/**
	 * Debug-Routine, um ein 2d-String-Array auszugeben
	 * @param data
	 * @return String
	 */
	public static String print_all(String[][] data) {
		String output=data.length + "        "  + "\n";
		for (int i=0; i<data.length;i++) {
			for (int j=0; j < data[i].length; j++) {
				output+=data[i][j] + " - ";
			}
			output+="\n";
		}
		return output;	
	}
	
	/**
	 * Allgemeine Methode um 1D-String-Arrays auszugeben
	 * @param data
	 */
	public static String print_all(String[] data) {
		String output="";
		for (int i=0; i<data.length;i++) {
			output+=data[i]+" ";
		}
		return output;
	}
	
	/**
	 * Allgemeine Methode um 1D-String-Arrays auszugeben
	 * mit Trennzeichenangabe
	 * @param data
	 * @param delim
	 */
	public static String print_all(String[] data, String delim) {
		String output="";
		for (int i=0; i<data.length;i++) {
			output+=data[i]+delim;
		}
		return output;
	}
	
	/**
	 * Allgemeine Methode um 1D-Int-Arrays auszugeben
	 * @param data
	 */
	public static String print_all(int[] data) {
		String output="";
		for (int i=0; i<data.length;i++) {
			output+=data[i]+" ";
		}
		return output;
	}

	/**
	 * Debug-Routine, um ein 2d-String-Array auszugeben
	 * @param data
	 * @return String
	 */
	public static void print_r(String[][] data) {
	    System.out.print(data.length + "        "  + "\n");
		for (int i=0; i<data.length;i++) {
			for (int j=0; j < data[i].length; j++) {
			    System.out.print(data[i][j] + " - ");
			}
			System.out.println();
		}	
	}
	
	/**
	 * Allgemeine Methode um 1D-String-Arrays auszugeben
	 * (PHP-like)
	 * @param data
	 */
	public static void print_r(String[] data) {
		for (int i=0; i<data.length;i++) {
		    System.out.print(data[i]+" ");
		}
	}
	
	/**
	 * Allgemeine Methode um 1D-String-Arrays auszugeben
	 * (PHP-like), mit Trennzeichenangabe
	 * @param data
	 * @param delim
	 */
	public static void print_r(String[] data, String delim) {
		for (int i=0; i<data.length;i++) {
		    System.out.print(data[i]+delim);
		}
	}
	
	/**
	 * Allgemeine Methode um 1D-Int-Arrays auszugeben
	 * (PHP-like)
	 * @param data
	 */
	public static void print_r(int[] data) {
		for (int i=0; i<data.length;i++) {
			System.out.print(data[i]+" ");
		}
	}

}
