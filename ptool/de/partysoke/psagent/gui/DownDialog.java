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
import de.partysoke.psagent.download.*;


public class DownDialog extends JDialog implements ActionListener
{
	private Config conf = Start.getConf();
	private Update parent;
	private JProgressBar bar;
	private DownloadThread th;
	private JButton button;
	private JLabel lab;
	
  public DownDialog(Update parent)
  {
	super(parent,"Daten herunterladen",true);
	this.parent = parent;
	parent.endDialog();
	Point parloc = parent.getLocation();
	setBounds(parloc.x + 30, parloc.y + 30,450,115);
	setBackground(Color.lightGray);
	this.getRootPane().setLayout(new BorderLayout(10,10));
	//JPanel
	JPanel panel = new JPanel(new BorderLayout());
	this.lab = new JLabel("Download l\u00E4uft...",JLabel.CENTER);
	
	// JProgressBar 
	this.bar = new JProgressBar();
	this.bar.setValue(0);
	this.bar.setStringPainted(true);
	//this.bar.setString("");
	//this.bar.setIndeterminate(true);
	this.bar.setBorder(BorderFactory.createLoweredBevelBorder());

	JPanel bar_panel = new JPanel(new BorderLayout(15,5));
	bar_panel.add(new JLabel(""), BorderLayout.NORTH);
	bar_panel.add(new JLabel(""), BorderLayout.EAST);
	bar_panel.add(new JLabel(""), BorderLayout.WEST);
	bar_panel.add(new JLabel(""), BorderLayout.SOUTH);
	bar_panel.add(bar, BorderLayout.CENTER);
	
	panel.add(lab, BorderLayout.NORTH);
	panel.add(bar_panel, BorderLayout.CENTER);
	
	// DownLoadThread starten
	this.th = new DownloadThread(this);
	this.th.start();
	
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
	//pack();
  }

  public void actionPerformed(ActionEvent event)
  {
	if (event.getActionCommand().equals("Schlie\u00DFen")) {
		endDialog();
		try {
			/*if (Base.getNewVersion()) {
				String msg="Es ist eine neue " + Define.getOwnName() + "-Version verfügbar: "+Start.getNewVersionAsString()+"\nSchaue bitte auf " + Define.getUrl_self() + ", um die neue Version herunterzuladen.";
				JOptionPane.showMessageDialog(null, msg, Define.getOwnName() + " " + Start.getVersionAsString(),JOptionPane.INFORMATION_MESSAGE);
			}*/
		}
		catch (Exception e) {}
	}
	if (event.getActionCommand().equals("Abbrechen")) {
		//endDialog();
		this.parent.setCanceled();
	    try {
	        this.th.destroy();
	        this.th.interrupt();	// ka ob das nötig is
		    this.th = null;
		    this.button.setText("Schlie\u00DFen");
		    this.bar.setIndeterminate(false);
		    this.bar.setString(null);
		    this.bar.setValue(0);
		    this.lab.setText("Abgebrochen durch Benutzer.");
		}
		catch (Exception e) { }
	}
  }
  
  
  public void ProgressBarNext(int value) {
    bar.setValue(bar.getValue()+value);
  	if (Define.doDebug() && bar.getValue()<100)
  	  new Logger("Download-Fortschritt: " + bar.getValue() + "%");
  }
  
 
  public void finish(String key, String fail) {
  	int action = 0;
  	this.button.setText("Schlie\u00DFen");
  	if (conf.setKey(key)) {
  	  this.lab.setText("Fertig!");
	}
	else {
		this.lab.setText("Es ist ein Fehler aufgetreten.");
		if (fail.equals("1")) action = -3;
		else if (fail.equals("2")) action = -2;
		else action = -5;
		new Logger("setKey() == false");
	}	
  	
  	if (action == -2) Base.showBox(parent, Define.getWrongUser(), 2);
  	else if (action == -3) Base.showBox(parent, Define.getNoDBCon(), 2);
  	else if (action == -4) Base.showBox(parent, Define.getMiscFailure(), 2);
  	else if (action == -5) Base.showBox(parent, Define.getNoCon(), 2);
	
  }
  
  
  private void endDialog()
  {
      this.bar = null;
      this.setVisible(false);
      this.dispose();
  }
	
}
