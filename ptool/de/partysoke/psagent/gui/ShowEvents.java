/*
 * Created on 03.04.2004
 * by Enrico Tröger
 */


package de.partysoke.psagent.gui;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import de.partysoke.psagent.*;
import de.partysoke.psagent.util.*;

class ShowEvents
extends JDialog
implements ActionListener
{
    protected String[][] userEvents;	// Unterklasse DetailsUser benötigt das
    private static JTable table;
	private JButton del_button = new JButton("L\u00F6schen");
	private int startLength = 0;
	private MWnd parent;
	
	public ShowEvents(MWnd parent) {
		super(parent, "Eigene Events anzeigen", true);
		Point parloc = parent.getLocation();
		setBounds(parloc.x + 40, parloc.y + 40,550,250);
		this.getRootPane().setLayout(new BorderLayout());
		this.parent = parent;

		// Tabelle (und ScrollPane) erzeugen
		table = new JTable();
		JScrollPane scrollp = new JScrollPane(table,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		JPanel tp = new JPanel(new GridLayout(1,1));
		
		// Tabellen-Eigenschaften
		table.setName("userevents");
		table.setToolTipText("Events-Tabelle (eigene Events)");
		table.addMouseListener(new MouseL(table, this));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// Tabelle füllen
		userEvents = Base.parseUserEvents(FileIO.readFile(Define.getUserEADFileName(), false));
		startLength = userEvents.length;
		fillTable();
		    
		tp.add(scrollp);
		this.getRootPane().add(tp, BorderLayout.CENTER);

		//Ende-Button-Panel
		JPanel button_panel = new JPanel(new FlowLayout());

		del_button.addActionListener(this);
		if (table.getRowCount() == 0)
		    del_button.setEnabled(false);

		button_panel.add(del_button);
		
		JButton close_button = new JButton("Schlie\u00DFen");
		close_button.addActionListener(this);
		button_panel.add(close_button);
		
		this.getRootPane().add(button_panel, BorderLayout.SOUTH);
		
		//Window-Listener
		addWindowListener(
			new WindowAdapter() {
				public void windowClosing(WindowEvent event)
				{
				    if (userEvents.length != startLength)
					    writeEventsToFile();
				    endDialog();
				}
			}
		);
	}
	
	/**
	 * Liest die gespeicherten User-Events ein und gibt diese als
	 * string-Array wieder zurück.  
	 * @return events-liste
	 */
	public String[][] parseUserData(String[][] tmp) {
		
		
		String[][] result = new String[userEvents.length][5];
		for (int j=0; j < tmp.length; j++) {
			result[j][0]=tmp[j][0];
			result[j][1]=tmp[j][1];
			// 			 tmp[j][2] -> PLZ
			result[j][2]=tmp[j][3]+"."+tmp[j][4]+"."+tmp[j][5];
			result[j][3]=tmp[j][7];
			result[j][4]=tmp[j][6];
		}
		
		return result;
		
	}


	/**
	 * Gibt die angegebene "Zeile" aus dem Events-Array zurück.
	 * @return events
	 */
	public String[] getEvent(int zeile) {
		//System.out.println(Base.print_all(userEvents[zeile]));
	    return userEvents[zeile];
	}
	
	public void fillTable() {
		table.setModel(new JTModel(Define.getSpalten(),this.parseUserData(userEvents)));
	}
	
	public void actionPerformed(ActionEvent event) {
		if (event.getActionCommand().equals("Schlie\u00DFen")) {
			if (userEvents.length != startLength)
			    this.writeEventsToFile();
		    this.endDialog();
		}
		if (event.getActionCommand().equals("L\u00F6schen")) {
			// Prüfen, ob ein Eintrag gewählt ist
			if (table.getSelectedRow()<0) {
				Base.showBox(this, "Kein Element ausgew\u00E4hlt!", 4);
			}
			else {
				
				// Abfrage
				if (Base.showYNBox(this, "Das gew\u00E4hlte Element wirklich l\u00F6schen?") == 0) {
				
					// Löschvorgang
					userEvents = Base.deleteArrayElement(userEvents, table.getSelectedRow());
					// userEventsCount in Base updaten, Statusbar updaten
					Base.setUserEventsCount();
					parent.updateStatusBarEventlabel();
					if (Define.doDebug()) {
					    Base.LogThis("L\u00F6schen des Events war erfolgreich, ID: " 
						+ table.getSelectedRow(), true);
					}
					this.fillTable();
					// Löschvorgang Ende
					// Prüfen, ob noch Events vorhanden sind
					if (table.getRowCount() == 0)
					    del_button.setEnabled(false);
	
				} // Abfrage Ende
			}
		}	// Prüfung auf ausgewähltes Element

	}

	void endDialog() {
		setVisible(false);
		dispose();
	}
	
	/**
	 * Schreibt die aktuellen Events (ohne die gelöschten) in die user.dat
	 *
	 */
	void writeEventsToFile() {
	    // umbauen in base und teile auslagern nach FileIO
	    String toAdd = "";
	    FileOutputStream file;
	    
	    try {
	        int l;
	        String zeit;
	        String cost;
	        String text;
	        for (int i=0; i < userEvents.length; i++) {
	            l = userEvents[i].length;
	            if (l > 10) zeit = userEvents[i][10];
		        else zeit = "";
	            if (l > 11) cost = userEvents[i][11];
		        else cost = "";
	            if (l > 12) text = userEvents[i][12];
		        else text = "";
		        
	            // name|ort|plz|tag|monat|jahr|bands|kat|loc|orga|zeit|cost|text
	            toAdd += userEvents[i][0] + "|" + userEvents[i][1] + "|" + userEvents[i][2] + "|" + userEvents[i][3] + 
	            "|" + userEvents[i][4] + "|" + userEvents[i][5]+ "|" + userEvents[i][6] + "|" + userEvents[i][7] +
	            "|" + userEvents[i][8] + "|" + userEvents[i][9] + "|" + zeit + "|" + cost + "|" + text + "\r\n";
	        }
	        
	        //toAdd = toAdd.replaceAll("\r\n", "<br>");
	    
	        file = new FileOutputStream(Define.getUserEADFileName());
	        file.write(toAdd.getBytes(Define.getEncoding()));
	        file.close();
	        if (Define.doDebug())
	            Base.LogThis(Define.getUserEADFileName() + " wurde geschrieben.", true);
	    }
	    catch (Exception e) {
	        Base.LogThis("Fehler beim Speichern der Events (UserEvents)", true);
	        if (Define.doDebug())
	            Base.LogThis(e.toString(), true);
	    }		
	
	}
	
}
