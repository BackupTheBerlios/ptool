/*
 * Created on 29.01.2004
 * by Enrico Tröger
 */

package de.partysoke.psagent.gui;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class News
extends Dialog
implements ActionListener
{
	public News(JFrame parent, String newsText)
	{
		super(parent,"Neuigkeiten",true);
		Point parloc = parent.getLocation();
		setBounds(parloc.x + 30, parloc.y + 30,500,580);
		setLayout(new BorderLayout());
		//setResizable(false);
		
		// Die Labels
		JLabel lab = new JLabel("PartySOKe.de - News",JLabel.CENTER);
		JScrollPane spane = new JScrollPane(
				new JLabel(newsText,JLabel.CENTER),
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
		);
		//JLabel spane = new JLabel(newsText,JLabel.CENTER);
		
		//Ende-Button-Panel
		JPanel button_panel = new JPanel(new FlowLayout());
		JButton button = new JButton("Schlie\u00DFen");
		button.addActionListener(this);
		button_panel.add(button);
		
		//JPanel
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(lab, BorderLayout.NORTH);
		panel.add(spane, BorderLayout.CENTER);
		panel.add(button_panel, BorderLayout.SOUTH);
		add(panel, BorderLayout.CENTER);
		
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


	void endDialog()
	{
		setVisible(false);
		dispose();
		//((Window)getParent()).toFront();
		//getParent().requestFocus();
	}
}

