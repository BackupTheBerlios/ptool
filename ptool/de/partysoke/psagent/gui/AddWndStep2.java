/*
 * Created on 01.03.2004
 * by Enrico Tröger
 */


package de.partysoke.psagent.gui;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import de.partysoke.psagent.*;
import de.partysoke.psagent.util.*;

public class AddWndStep2 
extends JDialog
implements ActionListener
{
    private JButton button_ok;
	private JButton button_cancel;
	private JLabel label_oview;
	private JLabel label_loc;
	private JComboBox cb_loc;
	private JTextField text_loc;
	private JLabel label_zeit;
	private JTextField text_zeit;
	private JLabel label_vid;
	private JComboBox cb_vid;
	private JTextField text_vid;
	private JLabel label_cost;
	private JTextField text_cost;
	private JLabel label_text;
	private JTextArea text_text;
	private JScrollPane sp_text_text;
	private int LocsLastIndex = 0;
	private int VidsLastIndex = 0;
	private String cost;
	private AddData adddata;
    private MWnd parent;
	 
	public AddWndStep2(MWnd parent_tmp, GetAddData gdata, AddData adata) {
		super(parent_tmp,"Event eintragen (2)",true);
    	AddWndStep2Layout customLayout = new AddWndStep2Layout();
    	this.setLocation(parent_tmp.getConfig().getWinInfoUE());
		
    	parent = parent_tmp;
		adddata = adata;
		
		this.getContentPane().setLayout(customLayout);

		button_ok = new JButton("Weiter");
		button_ok.addActionListener(this);
		this.getContentPane().add(button_ok);

		button_cancel = new JButton("Abbrechen");
		button_cancel.addActionListener(this);
		this.getContentPane().add(button_cancel);

		String[] tmp = adata.getStep1();
		String text="Event: "+tmp[0]+" in "+tmp[1]+", am "+tmp[2];
		label_oview = new JLabel(text);
		this.getContentPane().add(label_oview);

		label_loc = new JLabel("Location");
		this.getContentPane().add(label_loc);

		cb_loc = new JComboBox();
		cb_loc.setModel(new DefaultComboBoxModel(gdata.getLocs(adata.getOrt())));
		cb_loc.addActionListener(this);
		this.getContentPane().add(cb_loc);

		text_loc = new JTextField("");
		this.getContentPane().add(text_loc);

		label_zeit = new JLabel("Zeit");
		this.getContentPane().add(label_zeit);

		text_zeit = new JTextField("");
		this.getContentPane().add(text_zeit);
		
		label_vid = new JLabel("Veranstalter");
		this.getContentPane().add(label_vid);

		
		cb_vid = new JComboBox();
		cb_vid.setModel(new DefaultComboBoxModel(gdata.getOrgas()));
		cb_vid.addActionListener(this);
		this.getContentPane().add(cb_vid);
                
		text_vid = new JTextField("");
		this.getContentPane().add(text_vid);

		label_cost = new JLabel("Eintritt (in Euro)");
		this.getContentPane().add(label_cost);

		text_cost = new JTextField("");
		this.getContentPane().add(text_cost);

		label_text = new JLabel("zus\u00E4tzliche Informationen");
		this.getContentPane().add(label_text);

		text_text = new JTextArea("");
		sp_text_text = new JScrollPane(text_text);
		this.getContentPane().add(sp_text_text);

		setSize(this.getPreferredSize());
		
		// Textfelder auf auto-off setzen
		this.text_loc.setEditable(false);
		this.text_vid.setEditable(false);
		// LastIndex für Event-Handler der Bandauswahl setzen
		this.LocsLastIndex = gdata.getLocsLastIndex();
		this.VidsLastIndex = gdata.getOrgasLastIndex();
               
		//Window-Listener
		addWindowListener(
			new WindowAdapter() {
				public void windowClosing(WindowEvent event)
				{
					endDialog();
				}
			}
		);
  }
	    
	public Dimension getPreferredSize() {
		Dimension dim = super.getPreferredSize();
		double height = dim.getHeight()+15;
		dim.setSize(dim.getWidth(), height);
		return dim;
	}
 
	private boolean checkForms() {
    	
	 boolean allOk = true;
    	
	  // Eintritt prüfen
	 cost = text_cost.getText().replaceAll(",","\\.");
	 if (cost.length()>0) {
		 try {
		 	Float.parseFloat(cost);
		 }
		 catch (NumberFormatException nfe) {
			 Base.showBox(this, "Bitte gebe einen g\u00FCltigen Preis (oder keinen) an!", 4);
			 allOk=false;
		 }
	 }	// if (cost.length()>0)
	 //	 Location prüfen
	 if ((cb_loc.getSelectedIndex()==this.LocsLastIndex &&
		 text_loc.getText().length()==0)) {
		 if (allOk) {
			 Base.showBox(this, "Bitte gebe eine Location an!", 4);
			 allOk=false;
		 } 
	 }    		
	 //	 Veranstalter prüfen
	 if ((cb_vid.getSelectedIndex()==this.VidsLastIndex &&
		 text_vid.getText().length()==0)) {
		 if (allOk) {
			 Base.showBox(this, "Bitte gebe einen Veranstalter an!", 4);
			 allOk=false;
		 } 
	 }    		
	 return allOk;		
 	}
    
	public void actionPerformed(ActionEvent event2)
	{
		if (event2.getActionCommand().equals("Abbrechen")) {
			endDialog();
			parent.setCanceled(true);
		} 
    	
		if (event2.getActionCommand().equals("Weiter")) {
			if (checkForms()) {
				String loc = text_loc.getText();
				if (loc.equals("")) loc = cb_loc.getSelectedItem().toString();
				String vid = text_vid.getText();
				if (vid.equals("")) vid = cb_vid.getSelectedItem().toString();
				adddata.setStep2(loc, vid, text_zeit.getText(), cost, text_text.getText());
				endDialog();
			}
		}
		if (cb_loc.getSelectedItem().equals("Andere:")) {
			this.text_loc.setEditable(true);    	
		}
		else {
			this.text_loc.setEditable(false);
		}
		if (cb_vid.getSelectedItem().equals("Anderer:")) {
			this.text_vid.setEditable(true);    	
		}
		else {
			this.text_vid.setEditable(false);
		}

	}

	
	void endDialog()
	{
		setVisible(false);
		dispose();
		if (parent.getConfig().getSaveWinInfo())
		    parent.getConfig().setWinInfoUE(this.getLocation());
		//((Window)getParent()).toFront();
		//getParent().requestFocus();
	}
}

