/*
 * Created on 06.01.2004
 * (rework on 15.12.2004)
 * by Enrico Tröger
 */

package de.partysoke.psagent.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import de.partysoke.psagent.*;
import de.partysoke.psagent.util.*;

public class About
extends JDialog
implements ActionListener
{
  public About(JFrame parent)
  {
	super(parent,"\u00DCber " + Define.getOwnName(),true);
	Point parloc = parent.getLocation();
	setBounds(parloc.x + 30, parloc.y + 30,300,400);
	this.getContentPane().setBackground(Color.lightGray);
	this.getContentPane().setLayout(new BorderLayout());
	setResizable(false);
	
	JPanel panel = new JPanel(new BorderLayout());
	
	// Bildteil
	JLabel img = new JLabel();
	img.setIcon(
	        new ImageIcon(
	                getToolkit().getImage(getClass().getResource(Define.ImageAbout))
	        )
	);
	
	StringBuffer about_text = new StringBuffer();
	about_text.append("\u00A9 2004 by Enrico Tr\u00F6ger f\u00FCr www.PartySOKe.de\n");
	about_text.append("Website: http://ptool.PartySOKe.de\n");
	about_text.append("eMail: ptool@PartySOKe.de\n\n");
	about_text.append("Dieses Programm steht noch unter keiner Lizenz, aber wohl bald etwas \u00E4hnliches der LGPL (Lesser GNU Public License)");
	
	// Textteil
	JTextPane text = new JTextPane();
	text.setEditable(false);
	text.setFont(new Font(null, Font.PLAIN, 11));
	text.setText(about_text.toString());
	
	// Button-Teil
	JPanel buttons = new JPanel();
	buttons.setBackground(Color.white);
	JButton button_newver = new JButton("Neue Version");
	JButton button_close = new JButton("Schlie\u00DFen");
	button_close.addActionListener(this);
	button_newver.addActionListener(this);
	buttons.add(button_newver);
	buttons.add(button_close);
	
	// Panel zusammensetzen
	panel.add(img, BorderLayout.NORTH);
	panel.add(text, BorderLayout.CENTER);
	panel.add(buttons, BorderLayout.SOUTH);
	
	panel.setBackground(Color.white);
	this.getContentPane().add(panel);
	
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

