/*
 * Created on 06.01.2004
 * by Enrico Tröger
 */

package de.partysoke.psagent.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import de.partysoke.psagent.*;
import de.partysoke.psagent.util.*;

class About
extends JDialog
implements ActionListener
{
  public About(JFrame parent)
  {
	super(parent,"\u00DCber " + Define.getOwnName(),true);
	Point parloc = parent.getLocation();
	setBounds(parloc.x + 30, parloc.y + 30,400,370);
	this.getContentPane().setBackground(Color.lightGray);
	this.getContentPane().setLayout(new BorderLayout());
	setResizable(false);
	
	String about_text="<div align=\"center\"><table width=\"90%\"><tr><td><font face=\"Verdana\" size=\"2\"><b>" + Define.getOwnName() + "</b> ist ein kleines Java-Programm, mit dem man die Events-Tabelle "+ 
									"von www.PartySOKe.de auf seinen Rechner herunterladen " +
									"kann. So hat man auch dann noch jederzeit Zugriff auf die Events-Tabelle, "+
									"wenn keine Internetverbindung besteht.<br>Ideal f\u00FCr Modem- oder ISDN-Besitzer, "+
									"die keine Flatrate haben.</font></td></tr><tr><td>&nbsp;</td></tr>"+
									"<tr><td><font face=\"Verdana\" size=\"2\">" + Define.getOwnName() + " Version "+ Define.getVersionAsString() +
									"<br><br>by Enrico Tr\u00F6ger<br>eMail: " + Define.getOwnName() + "@PartySOKe.de<br>" + Define.getUrl_self() + "</font></td></tr>"+
									"<tr><td>&nbsp;</td></tr><tr><td><font face=\"Verdana\" size=\"2\">Vielen Dank an Frank, "+
									"der mir mit der Projekt-Webseite und mit dem Programm geholfen hat.</font></td></tr></table>";
	
	// Die Labels
	JLabel lab = new JLabel(Define.getOwnName() + " " + Define.getVersionAsString(),JLabel.CENTER);
	JTextPane lab2 = new JTextPane();
	lab2.setContentType("text/html");
	lab2.setText(about_text);
	lab2.setEditable(false);
		
	//Ende-Button-Panel
	JPanel button_panel = new JPanel(new FlowLayout());
	JButton button = new JButton("Neue Version");
	button.addActionListener(this);
	button_panel.add(button);
	
	button = new JButton("Schlie\u00DFen");
	button.addActionListener(this);
	button_panel.add(button);
	
	//JPanel
	JPanel panel = new JPanel();
	panel.setLayout(new BorderLayout());
	panel.add(lab, BorderLayout.NORTH);
	panel.add(lab2, BorderLayout.CENTER);
	panel.add(button_panel, BorderLayout.SOUTH);
	this.getContentPane().add(panel, BorderLayout.CENTER);
	
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
  		endDialog();
  	}
  	if (event.getActionCommand().equals("Neue Version")) {
  		Base.searchNewVersion(this);
  	}
  }


  void endDialog()
  {
	setVisible(false);
	dispose();
	//((Window)getParent()).toFront();
	//getParent().requestFocus();
  }
}

