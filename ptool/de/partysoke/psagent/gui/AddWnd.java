
/*
 * Created on 09.02.2004
 * by Enrico Tröger
 */

// package de.partysoke.psagent.gui;

/*import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class AddWnd
extends Dialog
implements ActionListener
{
  private Config conf = PTool.getConf();
  private MWnd wnd = PTool.getMe();
  private JTextField txt_name = new JTextField();
  private JComboBox cb_ort = new JComboBox();
  private JTextField txt_ort = new JTextField();
  private JList ls_band = new JList();
  private JTextField txt_band = new JTextField();
  private JTextField txt_datum = new JTextField();
  private JTextField txt_kat = new JTextField();
  private JTextField txt_entry = new JTextField();
  private JTextField txt_email = new JTextField();
  private JComboBox cb_loc = new JComboBox();
  private JTextField txt_loc = new JTextField();
  private JComboBox cb_vid = new JComboBox();
  private JTextField txt_vid = new JTextField();
  private JTextField txt_url = new JTextField();
  private JTextField txt_tel = new JTextField();
  private JTextField txt_zeit = new JTextField();
  private JTextArea txt_text = new JTextArea();
  private JTextField txt_fly = new JTextField();
  
  public AddWnd(JFrame parent, AddData data)
  {
	super(parent,"Event eintragen",true);
	Point parloc = parent.getLocation();
	setBounds(parloc.x + 30, parloc.y + 30,400,300);
	setBackground(Color.lightGray);
	setLayout(new BorderLayout());
	setResizable(false);
	
	// Titel-Label
	JPanel titel_panel = new JPanel();
	titel_panel.add(new JLabel("Gebe hier die Daten für das Event an:", JLabel.CENTER));
	
	// Haupt-Panel
	JPanel mb_top_panel = new JPanel(new BorderLayout());
	JPanel mb_north_panel = new JPanel();
	mb_north_panel.add(new JLabel("", JLabel.LEFT));
	JPanel mb_south_panel = new JPanel(new BorderLayout());
	mb_south_panel.add(new JLabel(" "),BorderLayout.NORTH);
	mb_south_panel.add(addMemberLabels(data),BorderLayout.CENTER);
	this.txt_ort.setEditable(false);
	this.txt_loc.setEditable(false);
	this.txt_vid.setEditable(false);
	this.txt_band.setEditable(false);
	
	mb_top_panel.add(mb_north_panel,BorderLayout.NORTH);
	mb_top_panel.add(new JLabel(" "),BorderLayout.EAST);
	mb_top_panel.add(new JLabel(" "),BorderLayout.WEST);
	mb_top_panel.add(mb_south_panel,BorderLayout.CENTER);
	
	// Button-Panel
	JPanel button_panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	JButton button = new JButton("Eintragen");
	button.addActionListener(this);
	
	JButton button2 = new JButton("Abbrechen");
	button2.addActionListener(this);
	button_panel.add(button2);
	button_panel.add(button);
	
	// Elemente anordnen
	add(titel_panel,BorderLayout.NORTH);
	add(mb_top_panel,BorderLayout.CENTER);
	add(button_panel,BorderLayout.SOUTH);
	
	//Window-Listener
	addWindowListener(
	  new WindowAdapter() {
		public void windowClosing(WindowEvent event)
		{
		  endDialog();
		}
	  }
	);
	//pack();
  }
  
 private JPanel addMemberLabels(AddData data) {
  	
	JPanel panel = new JPanel(new GridLayout(9,2));
	
	panel.add(new JLabel("Name:"));
	panel.add(txt_name);
	
	panel.add(new JLabel(" "));
	panel.add(new JLabel(" "));
	
	JLabel lab_ort = new JLabel("Ort:");
	cb_ort.setModel(new DefaultComboBoxModel(data.getOrte()));
	cb_ort.addActionListener(this);
	panel.add(lab_ort);
	panel.add(cb_ort);
   	
	panel.add(new JLabel(" "));
	panel.add(txt_ort);

	panel.add(new JLabel(" "));
	panel.add(new JLabel(" "));
	
	JLabel lab_datum = new JLabel("Datum:");
	panel.add(lab_datum);
	panel.add(txt_datum);
   	
	panel.add(new JLabel(" "));
	panel.add(new JLabel(" "));
   		
	JLabel lab_band = new JLabel("Band(s):");
	ls_band.setListData(data.getBands());
	ls_band.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	ls_band.setVisibleRowCount(3);
	panel.add(lab_band);
	panel.add(new JScrollPane(ls_band,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
	
	panel.add(new JLabel(" "));
	panel.add(txt_band);
	
	
	
	return panel;
  }
  
 
  public void actionPerformed(ActionEvent event)
  {
  	if (event.getActionCommand().equals("Abbrechen")) {
  		endDialog();
  	}
  	if (event.getActionCommand().equals("Eintragen")) {
  	}
  	if (cb_ort.getSelectedItem().equals("Anderer:")) {
  		this.txt_ort.setEditable(true);
  	}
  	else {
  		this.txt_ort.setEditable(false);
  	}
  	if (cb_loc.getSelectedItem().equals("Andere:")) {
  		this.txt_loc.setEditable(true);
  	}
  	else {
  		this.txt_loc.setEditable(false);
  	}
  	if (cb_vid.getSelectedItem().equals("Anderer:")) {
  		this.txt_vid.setEditable(true);
  	}
  	else {
  		this.txt_vid.setEditable(false);
  	}
  }


  void endDialog()
  {
	setVisible(false);
	dispose();
	//((Window)getParent()).toFront();
	//getParent().requestFocus();
  }
}*/
