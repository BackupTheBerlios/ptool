/*
 * Created on 12.02.2004
 * by Enrico Tröger
  */
package de.partysoke.psagent;

import de.partysoke.psagent.download.*;
import de.partysoke.psagent.util.*;

public class GetAddData {

    private String[] orte;
    private String[] plz;
	private String[] locs;
	private String[] loc_ids;
	private String[] loc_orte;
	private String[] orgas;
	private String[] orga_ids;
	private String[] bands;
	private String[] kats;
	private int locsLastIndex;
	private boolean created;
	
	/**
	 * Konstruktor, der die Daten einliest.
	 *
	 */
	public GetAddData(Config config) {
		String[] source = DownloadThread.code(FileIO.readZippedFile(Define.getEADFileName()),config.getKey()).split("\n");
	    
		if (source[0].equals("FEHLER!")) { 
				Base.showBox("Um Events eintragen zu k\u00F6nnen, musst Du erst die Orte und Bands runterladen. "+
											"Klicke bitte auf \"Update\", um Dir die Daten herunterzuladen.",1);
				this.created = false;
		}
		else if (source[0].equals("")) {
		    this.created = false;
		}
		else {
		    	orte = source[0].split("\\|");
				orte[orte.length-1]="Anderer:";
				plz = source[1].split("\\|");
				plz[plz.length-1]="";
				bands = source[2].split("\\|");
				bands[bands.length-1]="Andere:";
				bands[bands.length-2]="Keine";
				orgas = source[3].split("\\|");
				orga_ids = source[4].split("\\|");
				orgas[orgas.length-2]="Unbekannt";
				orgas[orgas.length-1]="Anderer:";
				locs = source[5].split("\\|");
				loc_orte = source[6].split("\\|");
				loc_ids = source[7].split("\\|");
				kats = source[8].split("\\|");
				locsLastIndex = 0;
				created = true;
				
		}
   
	}
	
	
	// Getter-Methoden für die einzelnen Arrays
	
	/**
	 * Gibt den Status des Parsen der Daten zurück
	 * return created
	 */
	public boolean isCreated() {
		return this.created;
	}
	
	/**
	 * Gibt das Array orte zurück
	 * return orte
	 */
	public String[][] getOrte() {
		String[][] result = { this.orte, this.plz };
	    return result;
	}
	
	/**
	 * Gibt das Array bands zurück
	 * return bands
	 */
	public String[] getBands() {
		return this.bands;
	}

	/**
	 * Gibt den letzten Index des Arrays locs zurück
	 * return locs
	 */
	public int getLocsLastIndex() {
		return this.locsLastIndex;
	}
	
	
	/**
	 * Gibt den letzten Index des Arrays orgas zurück
	 * return orgas
	 */
	public int getOrgasLastIndex() {
		return this.orgas.length-1;
	}
	
	
	
	/**
	 * Gibt den letzten Index des Arrays orte zurück
	 * return orte
	 */
	public int getOrteLastIndex() {
		return this.orte.length-1;
	}
	
	
	/**
	 * Gibt den letzten Index des Arrays bands zurück
	 * return bands
	 */
	public int getBandsLastIndex() {
		return this.bands.length-1;
	}
	
	/**
	 * Gibt das Array kats zurück
	 * return kats
	 */
	public String[] getKats() {
		return this.kats;
	}
	
	/**
	 * Gibt das Array orgas zurück
	 * return orgas
	 */
	public String[] getOrgas() {
		return this.orgas;
	}
	
	/**
	 * Gibt das Array orga_ids zurück
	 * return orga_ids
	 */
	public String[] getOrgaIDs() {
		return this.orga_ids;
	}
	
	/**
	 * Gibt das Array locs zurück
	 * return locs
	 */
	public String[] getLocs(String ort) {
		// "Lücken-Array"
		String[] tmpLocs = new String[locs.length];
		int j = 0;
		// Locations, die zum angegebenen Ort passen filtern
		for (int i = 0; i < locs.length; i++) {
			// Filter!
			if (loc_orte[i].equals(ort)) {
				tmpLocs[j] = locs[i];
				j++;
			}
		}
		// sauberes, lückenloses Array
		String [] resultLocs = new String[j+2];
		int z = 0;
		for (int h = 0; h < tmpLocs.length; h++) {
			if ((tmpLocs[h]!=null) && (!tmpLocs[h].equals(""))) {
				resultLocs[z] = tmpLocs[h];
				z++;
			}
		}
		resultLocs[j]="Unbekannt";
		resultLocs[j+1]="Andere:";
		this.locsLastIndex = resultLocs.length - 1;
		return resultLocs;
	}
	
	/**
	 * Gibt das Array loc_ids zurück
	 * return loc_ids
	 */
	public String[] getLocIDs() {
		return this.loc_ids;
	}
	
	/**
	 * debug routine
	 */
	public void printAll() {
	    DebugPrint.print_r(loc_ids, " ");
	    DebugPrint.print_r(loc_orte, " ");
	    DebugPrint.print_r(plz, " ");
	    DebugPrint.print_r(orga_ids, " ");
	    DebugPrint.print_r(orgas, " ");
	}
}
