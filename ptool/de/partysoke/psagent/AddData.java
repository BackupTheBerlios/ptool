/*
 * Created on 12.02.2004
 * by Enrico Tröger
 */
package de.partysoke.psagent;

public class AddData extends Data {

	private boolean filled;
	
	/**
	 * Konstruktor, der die Daten einliest.
	 *
	 */
	public AddData() {
		super();
		filled = false;
	}
		
	public boolean isFilled () {
		return filled;
	}
	
	public void setStep1 (String name1, String ort1, String plz1, int tag1, int monat1, int jahr1, String bands1, String kat1) {
		super.setStep1(name1, ort1, plz1, tag1, monat1, jahr1, bands1, kat1);
		filled = true;
	}

}
