/*
 * Created on 30.03.2004
 * by Enrico Tröger
 *
 * Oberklasse für AddData
 *
 */
package de.partysoke.psagent;

import de.partysoke.psagent.util.*;

public class Data {
	protected String name;
	private String ort;
	private String plz;
	private int tag;
	private int monat;
	private int jahr;
	private String datum;
	private String loc;
	private String orga;
	private String bands;
	private String kat;
	private String cost;
	private String zeit;
	private String text;

	/**
	 * Default-Konstruktor (von AddData benutzt)
	 */
	Data () {
		name = "";
		ort = "";
		ort = "";
		tag = 0;
		monat = 0;
		jahr = 0;
		datum = "";
		bands = "";
		orga = "";
		loc = "";
		kat = "";
		text = "";
		cost = "";
		zeit = "";
	}
	
	/**
	 * Konstruktor, um Objekt zu füllen
	 */
	Data (String ename, String eort, String eplz, int etag, int emonat,
		 int ejahr, String eband, String ekat, String eloc, 
		 String eorga, String ezeit, String ecost, String etext) {
		
		name = ename;
		ort = eort;
		plz = eplz;
		tag = etag;
		monat = emonat;
		jahr = ejahr;
		datum = "";
		bands = eband;
		orga = eorga;
		loc = eloc;
		kat = ekat;
		text = etext;
		cost = ecost;
		zeit = ezeit;
	}
	
	public String getOrt () {
		return ort;
	}
	
	public String[] getStep1 () {
		String[] result = new String[5];
	
		result[0] = name;
		result[1] = ort;
		result[2] = datum;
		result[3] = bands;
		result[4] = kat;
		
		return result;
	}
	
	public void setStep1 (String name1, String ort1, String plz1, int tag1, int monat1, int jahr1, String bands1, String kat1) {
		name = name1;
		ort = ort1;
		plz = plz1;
		tag = tag1;
		monat = monat1;
		jahr = jahr1;
		datum = tag + "." + monat + "." + jahr;
		bands = bands1;
		kat = kat1;
	}
	
	public void setStep2 (String loc1, String orga1, String zeit1, String cost1, String text1) {
		loc = loc1;
		orga = orga1;
		zeit = zeit1;
		cost = cost1;
		text = text1;
	}
	
	public void close() {
	    String tagS = String.valueOf(tag);
	    String monatS = String.valueOf(monat);
	    
	    if (tag < 10) tagS = "0" + tag;
	    if (monat < 10) monatS = "0" + monat;
	    
	    String toAdd = name + "|" + ort + "|" + plz + "|" + tagS + "|" + monatS + "|" + jahr + "|" + bands+ "|" + kat + "|" + loc +
								 "|" + orga + "|" + zeit + "|" + cost + "|" + text;
		toAdd = toAdd.replaceAll("\n", "<br>");
		FileIO.appendToFile(Define.getUserEADFileName(), toAdd + "\r\n");
	}

	public String[] toStringArray() {
		return new String[5];
	}
	
}
