/*
 * Created on 15.05.2004
 * @author Enrico Tröger
 *
 */

package de.partysoke.psagent.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import de.partysoke.psagent.Define;
import de.partysoke.psagent.util.*;


public class DetailsUser extends Details implements ActionListener {
	
	int id = 0;
	ShowEvents owner;
	MWnd parent;
	
	public DetailsUser(MWnd parent, int tmp_id, ShowEvents se) {
		super(parent, tmp_id, se);
		this.parent = parent;
		owner = se;
		id = tmp_id;
	}
	

	public void actionPerformed(ActionEvent event)  {
		if (event.getActionCommand().equals("Schlie\u00DFen")) {
			endDialog();
	  	}
		if (event.getActionCommand().equals("L\u00F6schen")) {
			//if () {
			owner.userEvents = Base.deleteArrayElement(owner.userEvents, id);
		    //System.out.println("L\u00F6schen des Events war erfolgreich.");
			Base.setUserEventsCount();
			parent.updateStatusBarEventlabel();

			//}
			//else {
			//	System.out.println("L\u00F6schen des Events fehlgeschlagen, ID: " + id);
			//	Base.showBox(this, "L\u00F6schen des Events fehlgeschlagen.", 1);
			//}
			if (Define.doDebug()) {
			    new Logger("L\u00F6schen des Events war erfolgreich, ID: " + id);
			}
			owner.fillTable();
			endDialog();
	  	}
	}
	  
	protected void addPanel(JPanel upPanel, JPanel mainPanel) {
		
	  	//Ende-Button-Panel
		JPanel button_panel = new JPanel(new FlowLayout());
		JButton button = new JButton("L\u00F6schen");
		button.addActionListener(this);
		button_panel.add(button);
		
		button = new JButton("Schlie\u00DFen");
		button.addActionListener(this);
		button_panel.add(button);
		
		//JPanel
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(upPanel, BorderLayout.NORTH);
		panel.add(mainPanel, BorderLayout.CENTER);
		panel.add(button_panel, BorderLayout.SOUTH);
		this.getContentPane().add(panel, BorderLayout.CENTER);

	}
	
	protected String spalteOrt(String[] in) {
		return     	
			"<tr><td valign=\"top\"><font face=\"Verdana\"><b>Ort:</b></font></td>"+
			"<td><font face=\"Verdana\">"+in[2]+"</font></td></tr>";
	}
	
	protected String[] getSpalte(int id, ShowEvents se) {
		String[] tmp = se.getEvent(id);
		String[] result;
		
		result = new String[14];
		result[0] = null;	// id
		result[1] = tmp[0];	// Name
		result[2] = tmp[1];	// Ort
		result[3] = tmp[7];	// Kategorie
		result[4] = tmp[6];	// Bands
		result[5] = tmp[3]+"."+tmp[4]+"."+tmp[5];	// Datum
		result[6] = tmp[2];	// PLZ
		result[7] = null;	// Kreis
		if (tmp.length < 10)
		    result[8] = "";
		else
		    result[8] = tmp[9];	// Veranstalter
		if (tmp.length < 11)
		    result[9] = "";
		else
		    result[9] = tmp[10];	// Zeit
		if (tmp.length < 12)
		    result[11] = "";
		else
		    result[11] = tmp[11];	// Text
		if (tmp.length < 13)
		    result[10] = "";
		else
		    result[10] = tmp[12];	// Eintritt
		result[12] = null;	// Flyer
		result[13] = null;	// LastModified
		
		return result;

	}
}
