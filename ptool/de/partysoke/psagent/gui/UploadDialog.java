/*
 * Created on 08.12.2004
 * by Enrico Tröger
 */


package de.partysoke.psagent.gui;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import de.partysoke.psagent.*;
import de.partysoke.psagent.util.*;
import de.partysoke.psagent.download.*;


public class UploadDialog extends JDialog implements ActionListener
{
	private Update parent;
	private JProgressBar bar;
	private JButton button;
	private JLabel lab;
	private UploadThread th;
	
  public UploadDialog(Update parent)
  {
	super(parent,Define.getUpdateTitle(Define.EVENTUPLOAD),true);
	this.parent = parent;
	parent.endDialog();
	Point parloc = parent.getLocation();
	setBounds(parloc.x + 30, parloc.y + 30,450,115);
	setBackground(Color.lightGray);
	this.getRootPane().setLayout(new BorderLayout(10,10));
	//JPanel
	JPanel panel = new JPanel(new BorderLayout());
	this.lab = new JLabel("Events eingetragen...",JLabel.CENTER);
	
	// JProgressBar 
	this.bar = new JProgressBar();
	this.bar.setStringPainted(true);
	this.bar.setString("Verarbeitung läuft");
	this.bar.setIndeterminate(true);
	this.bar.setBorder(BorderFactory.createLoweredBevelBorder());

	JPanel bar_panel = new JPanel(new BorderLayout(15,5));
	bar_panel.add(new JLabel(""), BorderLayout.NORTH);
	bar_panel.add(new JLabel(""), BorderLayout.EAST);
	bar_panel.add(new JLabel(""), BorderLayout.WEST);
	bar_panel.add(new JLabel(""), BorderLayout.SOUTH);
	bar_panel.add(bar, BorderLayout.CENTER);
	
	panel.add(lab, BorderLayout.NORTH);
	panel.add(bar_panel, BorderLayout.CENTER);
	
	
	//	Ende-Button-Panel
	JPanel button_panel = new JPanel(new FlowLayout());
	this.button = new JButton("Abbrechen");
	this.button.addActionListener(this);
	button_panel.add(this.button);
	
	panel.add(button_panel, BorderLayout.SOUTH);
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

	// Events senden
	if (Base.setProxySettings()) {
	    this.th = new UploadThread(this);
	    this.th.start();
	}
	else {
	    this.bar.setIndeterminate(false);
	    this.bar.setString("Abbruch");
	    this.button.setText("Schlie\u00DFen");
	}
	
	//pack();
  }

  public void actionPerformed(ActionEvent event)
  {
	if (event.getActionCommand().equals("Schlie\u00DFen")) {
		endDialog();
	}
	if (event.getActionCommand().equals("Abbrechen")) {
		//endDialog();
		try {
		    this.th.destroy();
	        this.th.interrupt();	// ka ob das nötig is
		    this.th = null;
		    this.button.setText("Schlie\u00DFen");
		    this.bar.setIndeterminate(false);
		    this.bar.setString("Abbruch");
		    this.lab.setText("Abgebrochen durch Benutzer.");
		}
		catch (Exception e) { }
	}
  }
  
  
 
  public void finish(int surc, StringWriter tmp) {
  	int action = surc;
  	this.button.setText("Schlie\u00DFen");
   	this.bar.setIndeterminate(false);
    this.bar.setString("Fehler");
  	
  	if (action == 1 && tmp != null) {
  	    Base.showBox(parent, Define.getFailedEvent() + tmp, 1);
  	}
  	else if (action == -1) {
  	    this.lab.setText(Define.getEventUploadOK());
  	    this.bar.setString("Abgeschlossen");
  	}
  	else if (action == -2) Base.showBox(parent, Define.getWrongUser(), 4);
  	else if (action == -3) Base.showBox(parent, Define.getNoDBCon(), 1);
  	else if (action == -4) Base.showBox(parent, Define.getMiscFailure(), 4);
  	else if (action == -5) Base.showBox(parent, Define.getNoCon(), 1);

  }
  
  
  private void endDialog()
  {
      this.bar = null;
      this.setVisible(false);
      this.dispose();
  }
	
}
