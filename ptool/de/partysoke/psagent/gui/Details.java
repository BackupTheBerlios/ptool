/*
 * Created on 21.01.2004
 * by Enrico Tröger
 */

package de.partysoke.psagent.gui;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

abstract class Details
extends Dialog
implements ActionListener
{
  //private String lastModified = "";
  protected JPanel up_panel = new JPanel(new BorderLayout());
  protected JPanel main_panel = new JPanel(new BorderLayout());
  
  public Details(JFrame parent, int id, ShowEvents se)  {
	super(parent,"Details",true);
	Point parloc = parent.getLocation();
	setBounds(parloc.x + 30, parloc.y + 30,400,380);
	setLayout(new BorderLayout());

	String[] spalte = this.getSpalte(id, se);
	
	// Opening-Panel
	up_panel.add(new JLabel(" "),BorderLayout.NORTH);
	up_panel.add(new JLabel("Detail-Ansicht f\u00FCr Event \"" + spalte[1] + "\"", JLabel.CENTER),BorderLayout.CENTER);
	
	// Rand-Panel

	String detail_text="<html><body>"+
		"<table border=\"0\" cellpadding=\"2\" cellspacing=\"0\" align=\"left\">"+
    	"<tr><td valign=\"top\"><font face=\"Verdana\"><b>Name:</b></font></td>"+
		"<td><font face=\"Verdana\">"+spalte[1]+"</font></td></tr>"+
    	"<tr><td valign=\"top\"><font face=\"Verdana\"><b>Datum:</b></font></td>"+
    	"<td><font face=\"Verdana\">"+spalte[5]+"</font></td></tr>"+
		spalteOrt(spalte) +
		"<tr><td valign=\"top\"><font face=\"Verdana\"><b>Kategorie:</b></font></td>"+
    	"<td><font face=\"Verdana\">"+spalte[3]+"</font></td></tr>"+
    	"<tr><td valign=\"top\"><font face=\"Verdana\"><b>Band(s):</b></font></td>"+
    	"<td><font face=\"Verdana\">"+spalte[4]+"</font></td></tr>"+
    	"<tr><td valign=\"top\"><font face=\"Verdana\"><b>Zeit:</b></font></td>"+
	    "<td><font face=\"Verdana\">"+spalte[9]+"</font></td></tr>"+
	    "<tr><td valign=\"top\"><font face=\"Verdana\"><b>Eintritt:</b></font></td>"+
	    "<td><font face=\"Verdana\">"+spalte[11]+"</font></td></tr>"+
	    "<tr><td valign=\"top\"><font face=\"Verdana\"><b>Hinweise:</b></font></td>"+
	    "<td><font face=\"Verdana\">"+spalte[10]+"</font></td></tr>"+
	    spalteLastModified(spalte) + "</table></body></html>";
	
	JLabel lab2 = new JLabel();
	//lab2.setContentType("text/html");
	lab2.setText(detail_text);
	//lab2.setEditable(false);
	main_panel.add(lab2,BorderLayout.CENTER);
	main_panel.add(new JLabel(" "),BorderLayout.WEST);
	main_panel.add(new JLabel(" "),BorderLayout.NORTH);
	main_panel.add(new JLabel(" "),BorderLayout.EAST);
	//main_panel.add(new JLabel(" "),BorderLayout.SOUTH);
	//main_panel.add(data_panel,BorderLayout.CENTER);

	this.addPanel(up_panel, main_panel);
	
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

  protected void endDialog()  {
	setVisible(false);
	dispose();
  }
  
  protected String spalteLastModified(String[] in) {
    	return "";
    }

  public abstract void actionPerformed(ActionEvent event);

  protected abstract void addPanel(JPanel upPanel, JPanel mainPanel);

  protected abstract String[] getSpalte(int id, ShowEvents se);

  protected abstract String spalteOrt(String[] in);
  
}

