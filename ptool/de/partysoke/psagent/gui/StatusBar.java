/*
 * Created on 17.05.2004
 * by Enrico Tröger
 */


package de.partysoke.psagent.gui;

import java.awt.*;
import javax.swing.*;

import de.partysoke.psagent.util.*;

public class StatusBar extends JPanel {
	
	private JLabel l_links;
	private JLabel l_rechts;
	
	public StatusBar(JLabel status_label) {
		setLayout(new BorderLayout(3,0));
		setBorder(BorderFactory.createEmptyBorder(0,2,2,2));
		l_links = status_label;
		l_links.setBorder(BorderFactory.createLoweredBevelBorder());
		l_links.setToolTipText("Die Zeit, wo die Daten des letzte Mal vom Server abgefragt wurden.");
		l_rechts = new JLabel();
		l_rechts.setPreferredSize(new Dimension(200,10));
		l_rechts.setOpaque(false);
		l_rechts.setBorder(BorderFactory.createLoweredBevelBorder());
		l_rechts.setToolTipText("Die Anzahl der Events");
		   
		add(l_rechts,BorderLayout.EAST);
		add(l_links,BorderLayout.CENTER);
		this.setPreferredSize(new Dimension(0,20));
	}

	public void setEventLabel() {
	    l_rechts.setText(" Events: " + Base.getEventsCount() + " / Eigene Events: " + Base.getUserEventsCount());
	}
	
}
