/*
 * Created on 17.12.2003
 * by Enrico Tröger
  */

package de.partysoke.psagent.gui;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import de.partysoke.psagent.*;
import de.partysoke.psagent.util.*;

class Update
extends JDialog
implements ActionListener
{
    private boolean canceled = false;
    
  public Update(JFrame parent)
  {
	super(parent,"Daten herunterladen",true);
	Point parloc = parent.getLocation();
	setBounds(parloc.x + 30, parloc.y + 30,400,110);
	setBackground(Color.lightGray);
	this.getRootPane().setLayout(new BorderLayout());
	setResizable(false);
	//JPanel
	JPanel panel = new JPanel(new BorderLayout());
	JPanel lab_panel = new JPanel(new BorderLayout());
	JLabel lab = new JLabel("Daten jetzt aus dem Internet herunterladen?",JLabel.CENTER);
	lab_panel.add(lab, BorderLayout.NORTH);
	lab_panel.add(new JLabel("(Eine Verbindung zum Internet muss bereits bestehen)",JLabel.CENTER), BorderLayout.CENTER);
	lab_panel.add(new JLabel(" "), BorderLayout.SOUTH);
	JButton button = new JButton("Download");
	
	//Ende-Button-Panel
	JPanel button_panel = new JPanel(new FlowLayout());
	JButton button2 = new JButton("Schlie\u00DFen");
	button.addActionListener(this);
	button2.addActionListener(this);
	button_panel.add(button);
	button_panel.add(button2);
	
	panel.add(lab_panel, BorderLayout.NORTH);
	panel.add(button_panel, BorderLayout.CENTER);
	this.getRootPane().add(panel, BorderLayout.CENTER);
	
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

  public void actionPerformed(ActionEvent event)
  {
	if (event.getActionCommand().equals("Schlie\u00DFen")) {
	    this.canceled = true;
	    endDialog();
	}
	if (event.getActionCommand().equals("Download")) {
		Config conf = Start.getConf();
		if (conf.getUsername().equals("") || conf.getPassword().equals("")) {
			Base.showBox(this, "Deine Benutzerdaten sind falsch.",1);
			endDialog();
		}
		else {
			DownDialog d = new DownDialog(this);
			d.setVisible(true);
		}
	 }
  }


  void endDialog()
  {
	setVisible(false);
	dispose();
	//((Window)getParent()).toFront();
	//getParent().requestFocus();
  }
  
  /**
   * Gibt an, ob der Vorgang abgebrochen wurde.
   * @return canceled.
   */
  public boolean isCanceled() {
      return canceled;
  }
  /**
   * Gibt an, ob der Vorgang abgebrochen wurde.
   */
  public void setCanceled() {
      this.canceled = true;
  }
}
