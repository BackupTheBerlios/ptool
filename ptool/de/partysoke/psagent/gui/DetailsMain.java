
/*
 * Created on 15.05.2004
 * by Enrico Tröger
 */

package de.partysoke.psagent.gui;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import de.partysoke.psagent.util.*;


public class DetailsMain extends Details implements ActionListener {
	
	public DetailsMain(JFrame parent, int id, ShowEvents se) {
		super(parent, id, se);
	}
	

	public void actionPerformed(ActionEvent event)  {
		if (event.getActionCommand().equals("Schlie\u00DFen")) {
			endDialog();
	  	}
		
	}
	
	  
	protected void addPanel(JPanel upPanel, JPanel mainPanel) {
		
	  	//Ende-Button-Panel
		JPanel button_panel = new JPanel(new FlowLayout());
		JButton button = new JButton("Schlie\u00DFen");
		button.addActionListener(this);
		button_panel.add(button);

		//JPanel
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(upPanel, BorderLayout.NORTH);
		panel.add(mainPanel, BorderLayout.CENTER);
		panel.add(button_panel, BorderLayout.SOUTH);
		this.getContentPane().add(panel, BorderLayout.CENTER);

	}

	protected String spalteLastModified(String[] in) {
	  	return 
			"<tr><td valign=\"top\"><font face=\"Verdana\"><b>Zuletzt ge\u00E4ndert:</b></font></td>"+
		    "<td><font face=\"Verdana\">"+in[13]+"</font></td></tr>";
	}
	
	protected String spalteOrt(String[] in) {
		return     	
			"<tr><td valign=\"top\"><font face=\"Verdana\"><b>PLZ Ort:</b></font></td>"+
			"<td><font face=\"Verdana\">"+in[6]+" "+in[2]+"</font></td></tr>"+
			"<tr><td valign=\"top\"><font face=\"Verdana\"><b>Kreis:</b></font></td>"+
			"<td><font face=\"Verdana\">"+in[7].toUpperCase()+"</font></td></tr>";
	}
	
	
	protected String[] getSpalte(int id, ShowEvents se) {
		return Base.getEvent(id);
	}
}

