/*
 * Created on 10.02.2004
 * by Enrico Tröger
 */

package de.partysoke.psagent.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

import de.partysoke.psagent.*;
import de.partysoke.psagent.util.*;

class AddWndStep1
extends JDialog
implements ActionListener
{
	private JTextField text_name;
	private JTextField text_ort;
	private String[][] ortarray;
	private JFormattedTextField text_tag;
	private JFormattedTextField text_monat;
	private JFormattedTextField text_jahr;
	private JTextField text_bnd;
	private JList list_bnd;
	private JComboBox cb_ort;
	private JComboBox cb_kat;
	private JScrollPane sp_list_bnd;
	private JLabel label_name;
	private JLabel label_ort;
	private JLabel label_datum;
	private JLabel label_bnd;
	private JLabel label_kat;
	private JButton button_ok;
    private JButton button_cancel;
    private int BandsLastIndex = 0;
    private int OrteLastIndex = 0;
	private AddData adddata;
	private GetAddData getdata;
	private MWnd parent;
	
    
    // Konstruktor
    public AddWndStep1(MWnd parent_tmp, GetAddData data, AddData adata) {
    	super(parent_tmp,"Event eintragen",true);
    	AddWndStep1Layout customLayout = new AddWndStep1Layout();
		
    	parent = parent_tmp;
		adddata = adata;
		getdata = data;
		
        this.getContentPane().setLayout(customLayout);
        
		if (adata.isFilled()) {
			String[] tmp = adata.getStep1();
			String text="Event: "+tmp[0]+" in "+tmp[1]+", am "+tmp[2];
			System.out.println(text);
		}
		
		label_name = new JLabel("Name");
        this.getContentPane().add(label_name);
        
        label_ort = new JLabel("Ort");
        this.getContentPane().add(label_ort);

        text_name = new JTextField();
        this.getContentPane().add(text_name);

        text_ort = new JTextField();
        this.getContentPane().add(text_ort);

        ortarray = data.getOrte();
        String[] orte = new String[ortarray[0].length];
        for (int i = 0; i < ortarray[0].length; i++) {
            if (i < (ortarray[0].length-1)) 
                orte[i] = ortarray[0][i] + " (" + ortarray[1][i] + ")";
            else 
                orte[i] = ortarray[0][i];
        }
        cb_ort = new JComboBox();
        cb_ort.setModel(new DefaultComboBoxModel(orte));
        cb_ort.addActionListener(this);
        this.getContentPane().add(cb_ort);

        label_datum = new JLabel("Datum");
        this.getContentPane().add(label_datum);

        text_tag = new JFormattedTextField(createFormatter("##"));
        this.getContentPane().add(text_tag);

        label_bnd = new JLabel("Band(s)");
        this.getContentPane().add(label_bnd);

        DefaultListModel listModel_list_bnd = new DefaultListModel();
        list_bnd = new JList(listModel_list_bnd);
        list_bnd.setListData(data.getBands());
        list_bnd.addListSelectionListener(new ListL(this, data));
        list_bnd.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        sp_list_bnd = new JScrollPane(list_bnd);
        this.getContentPane().add(sp_list_bnd);

        text_bnd = new JTextField();
        this.getContentPane().add(text_bnd);

        label_kat = new JLabel("Kategorie");
        this.getContentPane().add(label_kat);

        cb_kat = new JComboBox();
        cb_kat.setModel(new DefaultComboBoxModel(data.getKats()));
        this.getContentPane().add(cb_kat);

        button_ok = new JButton("Weiter");
        button_ok.addActionListener(this);
        this.getContentPane().add(button_ok);

        button_cancel = new JButton("Abbrechen");
        button_cancel.addActionListener(this);
		this.getContentPane().add(button_cancel);

        text_monat = new JFormattedTextField(createFormatter("##"));
        this.getContentPane().add(text_monat);

        text_jahr = new JFormattedTextField(createFormatter("####"));
        this.getContentPane().add(text_jahr);

        setSize(this.getPreferredSize());
        
        // Textfelder auf auto-off setzen
        this.text_ort.setEditable(false);
        this.text_bnd.setEditable(false);
        // LastIndex für Event-Handler der Bandauswahl setzen
        this.BandsLastIndex = data.getBandsLastIndex();
        this.OrteLastIndex = data.getOrteLastIndex();
        
        //Window-Listener
        this.addWindowListener(
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
    	
		boolean dateOk = true;
		boolean allOk = true;
    	
    	// Name prüfen
    	if (text_name.getText().length()<3) {
    		Base.showBox(this, "Bitte gebe einen Event-Namen an!", 4);
			allOk=false;
    	}
    	// Ort prüfen
    	if ((cb_ort.getSelectedObjects().length==0) ||
    		(cb_ort.getSelectedIndex()==this.OrteLastIndex &&
    		 text_ort.getText().length()==0)) {
    		if (allOk) {
    			Base.showBox(this, "Bitte gebe einen Ort an!", 4);
				allOk=false;
			} 
    	}
    	// Tag prüfen
    	String txt_tag = text_tag.getText();
    	if (txt_tag.length()>0 && txt_tag.length()<3) {
    		int tag=0;
    		try { tag = Integer.parseInt(txt_tag); }
    		catch (NumberFormatException nfe) {	dateOk=false;	}
    		if (tag<1 || tag>31) { dateOk=false; }
    	}
    	else {
			dateOk=false;
    	}
    	// Monat prüfen
    	String txt_monat = text_monat.getText();
    	if (txt_monat.length()>0 && txt_monat.length()<3) {
    		int monat=0;
    		try { monat = Integer.parseInt(txt_monat); }
    		catch (NumberFormatException nfe) {	dateOk=false;	}
    		if (monat<1 || monat>12) { dateOk=false; }
    	}
    	else {
			dateOk=false;
    	}
    	// Jahr prüfen
    	String txt_jahr = text_jahr.getText();
    	if (txt_jahr.length()>0 && txt_jahr.length()<5) {
    		int jahr=0;
    		try { jahr = Integer.parseInt(txt_jahr); }
    		catch (NumberFormatException nfe) {	dateOk=false;	}
    		if (jahr<Base.getDateAsInt(Define.DATE_YEAR) || jahr>2038) { dateOk=false; }
    	}
    	else {
    		dateOk=false;
    	}
		if (allOk && !dateOk) { 
			Base.showBox(this,  "Bitte gebe ein g\u00FCltiges Datum an!", 4);
			allOk=false;
		} 

		// Band prüfen
		if ((list_bnd.getSelectedIndices().length==0) ||
			(list_bnd.getSelectedIndex()==this.BandsLastIndex &&
			 text_bnd.getText().length()==0)) {
			if (allOk) {
				Base.showBox(this, "Bitte gebe mind. eine Band an (wenn keine Band spielt, dann \"Keine\" ausw\u00C4hlen)!", 4);
				allOk=false;
			} 
		}
		return allOk;		
    }
    
    
    public void actionPerformed(ActionEvent event)
	{
		if (event.getActionCommand().equals("Abbrechen")) {
			endDialog();
			parent.setCanceled(true);
		} 
    	
    	if (event.getActionCommand().equals("Weiter")) {
			if (checkForms()) {
			    String ort = text_ort.getText();
			    String plz = "";
				if (ort.equals("")) {
				    ort =  ortarray[0][cb_ort.getSelectedIndex()];
				    plz =  ortarray[1][cb_ort.getSelectedIndex()];
				}
				String bands = "";
				if (list_bnd.getSelectedIndex()==this.BandsLastIndex) bands = text_bnd.getText();
				else {
					Object[] tmp = list_bnd.getSelectedValues();
					for (int i = 0; i < tmp.length; i++) {
						if (i == (tmp.length-1)) bands += tmp[i].toString();
						else bands+=tmp[i].toString()+", ";
					}
				}
				adddata.setStep1(text_name.getText(), ort, plz, Integer.parseInt(text_tag.getText()),  Integer.parseInt(text_monat.getText()), 
												Integer.parseInt(text_jahr.getText()), bands, cb_kat.getSelectedItem().toString());
				endDialog();
				
			}	// if (checkforms())
    		
    	}
    	if (cb_ort.getSelectedItem().equals("Anderer:")) {
    		this.text_ort.setEditable(true);    	
    	}
    	else {
    		this.text_ort.setEditable(false);
    	}
    }

	public JTextField getTextBnd() {
		return this.text_bnd;
	}
    
	public JList getListBnd() {
		return this.list_bnd;
	}
    
	public GetAddData getGetData() {
		return this.getdata;
	}
    
	protected MaskFormatter createFormatter(String s) {
	    MaskFormatter formatter = null;
	    try {
	        formatter = new MaskFormatter(s);
	        formatter.setPlaceholderCharacter('0');
	    } catch (java.text.ParseException exc) {}
	    return formatter;
	}
	
    void endDialog()
	{
    	setVisible(false);
    	dispose();
    	//((Window)getParent()).toFront();
    	//getParent().requestFocus();
    }
}

