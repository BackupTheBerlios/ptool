/*
 * Created on 29.01.2004
 * by Enrico Tröger
 */

package de.partysoke.psagent.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import de.partysoke.psagent.*;
import de.partysoke.psagent.util.*;

class News extends JDialog implements ActionListener, TreeSelectionListener
{
    JTextArea textField;
    String[][] newsText;
    
    public News(JFrame parent, String[][] newstmp)
	{
		super(parent,"PartySOKe.de - News",true);
		this.newsText = newstmp;
		Point parloc = parent.getLocation();
		setBounds(parloc.x + 30, parloc.y + 30,500,430);
		this.getContentPane().setLayout(new BorderLayout());
		//setResizable(false);
		
		// Der Baum
	    Vector dates = new Vector();
	    for (int i = 0; i < newsText.length; i++) {
	        if (! dates.contains(newsText[i][0]))
	            dates.add(newsText[i][0]);
	    }
	    DefaultMutableTreeNode root, date, subject;
		root = new DefaultMutableTreeNode("PartySOKe.de");
		for (int i = 0; i < dates.size(); i++) {
			date = new DefaultMutableTreeNode(dates.get(i));
			root.add(date);
			for (int j = 0; j < newsText.length; j++) {
			    if (newsText[j][0].equals(dates.get(i))) {
			        subject = new MyTreeNode(newsText[j][1], j);
			        date.add(subject);
			    }
			}
		}
		//JTree erzeugen und konfigurieren
		JTree tree = new JTree(root);
		tree.getSelectionModel().setSelectionMode(
		        TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addTreeSelectionListener(this);
		tree.expandPath(tree.getSelectionPath());
		tree.setRootVisible(true);

		// TextArea erzeugen
		textField = new JTextArea(newsText[0][2]);
		textField.setWrapStyleWord(true);
		textField.setLineWrap(true);
		JScrollPane spane2 = new JScrollPane(textField);
		        
		// Zusammensetzen
		JLabel lab = new JLabel("PartySOKe.de - News",JLabel.CENTER);
		JScrollPane spane = new JScrollPane(
				tree);/*,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
		);*/
	    
		// Baum komplett erweitern und somit in Scrollpane einpassen
		// Code-Fragment aus tvbrowser.ui.settings.SettingsDialog.java
		// Danke dem TV-Browser-Team
		int categoryCount = root.getChildCount();
	    for (int i = categoryCount; i >= 1; i--) {
	      tree.expandRow(i);
	    }
	    spane.getViewport().setPreferredSize(tree.getPreferredSize());
	    // TV-Browser-Code Ende ;-)
	    
		JPanel treePanel = new JPanel(new BorderLayout(10, 0));
		treePanel.add(spane, BorderLayout.WEST);
		treePanel.add(spane2, BorderLayout.CENTER);
		treePanel.add(new JLabel(""), BorderLayout.EAST);
		
		//Ende-Button-Panel
		JPanel buttonPanel = new JPanel(new FlowLayout());
		JButton button = new JButton("Schlie\u00DFen");
		button.addActionListener(this);
		buttonPanel.add(button);
		
		//JPanel
		JPanel panel = new JPanel(new BorderLayout(0, 5));
		panel.add(lab, BorderLayout.NORTH);
		panel.add(treePanel, BorderLayout.CENTER);
		panel.add(buttonPanel, BorderLayout.SOUTH);
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
	}

	public void valueChanged(TreeSelectionEvent event) {
	    MyTreeNode mtn = null;
	    try {
	        JTree tmp = (JTree)event.getSource();
	        mtn = (MyTreeNode)tmp.getSelectionPath().getLastPathComponent();
	    }
	    catch(ClassCastException cce) {	// passiert bei jedem nicht-Blatt 
	        if (Define.doDebug>1) 
	            new Logger(cce.toString());
	    }
	    catch(NullPointerException npe) {	// es passiert, weiß aber nicht wann 
	        if (Define.doDebug>1) 
	            new Logger(npe.toString());
	    }
	    if (mtn != null) {
	        this.textField.setText(newsText[mtn.getID()][2]);
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

