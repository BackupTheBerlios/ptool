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

class Options
extends JDialog
implements ActionListener, KeyListener
{
    private Config conf = Start.getConf();
	private MWnd wnd;
	private ButtonGroup group = new ButtonGroup();
	private JTextField txt_user = new JTextField();
	private JTextField txt_pw = new JTextField();
	private JTextField txt_email = new JTextField();
	private JTextField txt_url = new JTextField();
	private JTextField txt_tel = new JTextField();
	private JTextField txt_purl = new JTextField();
	private JTextField txt_phost = new JTextField();
	private JTextField txt_pport = new JTextField();
	private JTextField txt_puser = new JTextField();
	private JTextField txt_ppass = new JTextField();
	private JCheckBox spr = new JCheckBox("Splashscreen aktivieren", conf.getSplash());
	private JCheckBox sys= new JCheckBox("Icon im Systray anzeigen (nur unter Windows)", conf.getSystray());
	private JCheckBox swi = new JCheckBox("Fenster-Position speichern", conf.getSaveWinInfo());
	private JCheckBox aup= new JCheckBox("Automatischer Upload eigener Events bei Download", conf.getAutoUpdate());

	public Options(MWnd parent)
	{
	    super(parent,"Optionen",true);
	    Point parloc = parent.getLocation();
	    setBounds(parloc.x + 30, parloc.y + 30,400,340);
	    getContentPane().setBackground(Color.lightGray);
	    getContentPane().setLayout(new BorderLayout());
	    setResizable(false);
	
	    wnd = parent;
	
	    // Titel-Label
	    JPanel titel_panel = new JPanel();
	    titel_panel.add(new JLabel("Optionen", JLabel.CENTER));
	    // Haupt-Panel
	
	    // L&F-Optionen
	    JPanel lf_top_panel = new JPanel(new BorderLayout());
	    JPanel north_panel = new JPanel();
	    north_panel.add(new JLabel("W\u00E4hle hier ein Look & Feel (Aussehen des Programms)", JLabel.LEFT));
	    JPanel south_panel = new JPanel(new GridLayout(3,2));
	    // Fügt die Radio-Buttons in das Panel ein
	    this.addRadios(south_panel);
	
	    lf_top_panel.add(north_panel,BorderLayout.NORTH);
	    lf_top_panel.add(south_panel,BorderLayout.CENTER);
	
	    // Member-Optionen
	    JPanel mb_top_panel = new JPanel(new BorderLayout());
	    JPanel mb_north_panel = new JPanel();
	    mb_north_panel.add(new JLabel("Gebe hier Deine Benutzerdaten (von PartySOKe.de) an", JLabel.LEFT));
	    JPanel mb_south_panel = new JPanel(new BorderLayout());
	    mb_south_panel.add(new JLabel(" "),BorderLayout.NORTH);
	    mb_south_panel.add(addMemberLabels(),BorderLayout.CENTER);
	
	    mb_top_panel.add(mb_north_panel,BorderLayout.NORTH);
	    mb_top_panel.add(new JLabel(" "),BorderLayout.EAST);
	    mb_top_panel.add(new JLabel(" "),BorderLayout.WEST);
	    mb_top_panel.add(mb_south_panel,BorderLayout.CENTER);
	
	    // Proxy-Optionen
	    JPanel p_top_panel = new JPanel(new BorderLayout());
	    JPanel p_north_panel = new JPanel();
	    p_north_panel.add(new JLabel("Gebe hier einen Proxy an, falls Du einen verwendest", JLabel.LEFT));
	    JPanel p_south_panel = new JPanel(new BorderLayout());
	    p_south_panel.add(new JLabel(" "),BorderLayout.NORTH);
	    p_south_panel.add(addProxyLabels(),BorderLayout.CENTER);
	
	    p_top_panel.add(p_north_panel,BorderLayout.NORTH);
	    p_top_panel.add(new JLabel(" "),BorderLayout.EAST);
	    p_top_panel.add(new JLabel(" "),BorderLayout.WEST);
	    p_top_panel.add(p_south_panel,BorderLayout.CENTER);
	
	    // Sonstige Optionen
	    JPanel misc_top_panel = new JPanel(new BorderLayout());
	
	    JPanel misc_north_panel = new JPanel(new BorderLayout());
	    misc_north_panel.add(new JLabel("\n"), BorderLayout.NORTH);
	    // Layout-Manager für die einzelnen Optionen
	    JPanel misc_opt_panel = new JPanel(new GridLayout(4,1));
	    // Wenn OS != Windows, dann ist Systray deaktiviert
	    if (Base.getOS() != Define.WINDOWS_OS) sys.setEnabled(false); 
	    misc_opt_panel.add(spr);
	    misc_opt_panel.add(sys);
	    misc_opt_panel.add(swi);
	    //misc_opt_panel.add(aup);
	    misc_opt_panel.add(new JLabel());
	
	    misc_north_panel.add(misc_opt_panel, BorderLayout.CENTER);
	
	    misc_top_panel.add(misc_north_panel, BorderLayout.NORTH);
	
	    // In Tabs einfügen
	    JTabbedPane tabs = new JTabbedPane();
	    tabs.addTab("Look & Feel", null, lf_top_panel );
	    tabs.addTab("Benutzerdaten", null, mb_top_panel );
	    tabs.addTab("Proxy", null, p_top_panel );
	    tabs.addTab("Sonstiges", null, misc_top_panel );

	    JPanel opt_panel = new JPanel(new BorderLayout());
	    opt_panel.add(tabs,BorderLayout.CENTER);	
		
	    // Button-Panel
	    JPanel button_panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	    JButton button = new JButton("Hilfe");
	    button.addActionListener(this);
	    button_panel.add(button);
	
	    button = new JButton("OK");
	    button.addActionListener(this);
	    button_panel.add(button);
	
	    button = new JButton("Abbrechen");
	    button.addActionListener(this);
	    button_panel.add(button);
	
	    // Elemente anordnen
	    this.getContentPane().add(titel_panel,BorderLayout.NORTH);
	    this.getContentPane().add(opt_panel,BorderLayout.CENTER);
	    this.getContentPane().add(button_panel,BorderLayout.SOUTH);
	
     	// Tooltips erzeugen
     	// Proxy
	    txt_purl.setToolTipText(Help.OPTIONS_PROXY_URL);
     	txt_phost.setToolTipText(Help.OPTIONS_PROXY_HOST);
     	txt_pport.setToolTipText(Help.OPTIONS_PROXY_PORT);
     	txt_puser.setToolTipText(Help.OPTIONS_PROXY_USER);
     	txt_ppass.setToolTipText(Help.OPTIONS_PROXY_PASS);
     	// Zugangsdaten
     	txt_user.setToolTipText(Help.OPTIONS_USER);
     	txt_pw.setToolTipText(Help.OPTIONS_PASS);
     	// Sonstige
     	swi.setToolTipText(Help.OPTIONS_WINSAVE);
     	sys.setToolTipText(Help.OPTIONS_SYSTRAY);
     	spr.setToolTipText(Help.OPTIONS_SPLASH);

     	this.keyReleased(null);
     	
		//Window-Listener
		this.addWindowListener(
		        new WindowAdapter() {
		            public void windowClosing(WindowEvent event)
		            {
		                endDialog();
		            }
		        }
		);
		//pack();
	}
  
	private JPanel addProxyLabels() {
    	
     	JPanel panel = new JPanel(new GridLayout(10,2));
     	
     	JLabel lab_purl = new JLabel("Proxy-Url:");
     	txt_purl.setText(conf.getProxyUrl());
     	txt_purl.addKeyListener(this);
     	panel.add(lab_purl);
     	panel.add(txt_purl);
     	
     	panel.add(new JLabel(" "));
     	panel.add(new JLabel(" "));
     	
     	JLabel lab_phost = new JLabel("Hostname:");
     	String tmp = conf.getProxyHost();
     	//if (tmp.equals("noProxy")) tmp = "";
     	txt_phost.setText(tmp);
     	txt_phost.addKeyListener(this);

     	panel.add(lab_phost);
     	panel.add(txt_phost);
     	
     	panel.add(new JLabel(" "));
     	panel.add(new JLabel(" "));
     	
     	JLabel lab_pport = new JLabel("Port:");
     	txt_pport.setText(conf.getProxyPort());
     	if (tmp.equals("")) txt_pport.setEnabled(false);
     	
     	panel.add(lab_pport);
     	panel.add(txt_pport);
     	
     	panel.add(new JLabel(" "));
     	panel.add(new JLabel(" "));
     	
     	JLabel lab_puser = new JLabel("Username:");
     	txt_puser.setText(conf.getProxyUser());
     	if (tmp.equals("")) txt_puser.setEnabled(false);
     	
     	panel.add(lab_puser);
     	panel.add(txt_puser);
     	
     	panel.add(new JLabel("(nur falls erforderlich)"));
     	panel.add(new JLabel(" "));
     	
     	JLabel lab_ppass = new JLabel("Passwort:");
     	txt_ppass.setText(conf.getProxyPass());
     	if (tmp.equals("")) txt_ppass.setEnabled(false);
     	
     	panel.add(lab_ppass);
     	panel.add(txt_ppass);
     	
     	panel.add(new JLabel("(nur falls erforderlich)"));
     	panel.add(new JLabel(" "));
     	
     	return panel;
    }
    
	private JPanel addMemberLabels() {
    	
     	JPanel panel = new JPanel(new GridLayout(9,2));
     	
     	JLabel lab_user = new JLabel("Username:");
     	txt_user.setText(conf.getUsername());
     	if (!conf.getUsername().equals("")) {
     		txt_user.setEnabled(false);
     	}
     	panel.add(lab_user);
     	panel.add(txt_user);
     	
     	panel.add(new JLabel(" "));
     	panel.add(new JLabel(" "));
     	
     	JLabel lab_pw = new JLabel("Passwort:");
     	if (!conf.getPassword().equals("")) {
     		txt_pw.setText("(wird hier nicht angezeigt)");
     	}
     	panel.add(lab_pw);
     	panel.add(txt_pw);
     	
     	panel.add(new JLabel(" "));
     	panel.add(new JLabel(" "));
     	
     	JLabel lab_email = new JLabel("eMail:");
     	txt_email.setText(conf.geteMail());
     	txt_email.setEnabled(false);
     	panel.add(lab_email);
     	panel.add(txt_email);
     	
     	panel.add(new JLabel(" "));
     	panel.add(new JLabel(" "));
     	
     	JLabel lab_url = new JLabel("Homepage:");
     	txt_url.setText(conf.getUrl());
     	txt_url.setEnabled(false);
     	panel.add(lab_url);
     	panel.add(txt_url);
     	
     	panel.add(new JLabel(" "));
     	panel.add(new JLabel(" "));
     	
     	JLabel lab_tel = new JLabel("Telefon:");
     	txt_tel.setText(conf.getTelNr());
     	txt_tel.setEnabled(false);
     	panel.add(lab_tel);
     	panel.add(txt_tel);
     	
     	return panel;
    }
    
  
	private void addRadios(JPanel panel) {
  	
	    JRadioButton rb1 = new JRadioButton("Standard (Metal)",conf.getLF().equals("metal"));
	    rb1.setActionCommand(rb1.getText());
	    rb1.setEnabled(wnd.isAvailableLookAndFeel(wnd.getLongLF("metal")));
	    group.add(rb1);
	    panel.add(rb1);
  	
	    JRadioButton rb2 = new JRadioButton("Kunststoff",conf.getLF().equals("kunststoff"));
	    rb2.setActionCommand(rb2.getText());
	    rb2.setEnabled(wnd.isAvailableLookAndFeel(wnd.getLongLF("kunststoff")));
	    group.add(rb2);
	    panel.add(rb2);
  	
	    JRadioButton rb3 = new JRadioButton("Motif",conf.getLF().equals("motif"));
	    rb3.setActionCommand(rb3.getText());
	    rb3.setEnabled(wnd.isAvailableLookAndFeel(wnd.getLongLF("motif")));
	    group.add(rb3);
	    panel.add(rb3);
  	
	    JRadioButton rb4 = new JRadioButton("Mac",conf.getLF().equals("mac"));
	    rb4.setActionCommand(rb4.getText());
	    rb4.setEnabled(wnd.isAvailableLookAndFeel(wnd.getLongLF("mac")));
	    group.add(rb4);
	    panel.add(rb4);
  	
	    JRadioButton rb5 = new JRadioButton("Windows",conf.getLF().equals("win"));
	    rb5.setActionCommand(rb5.getText());
	    rb5.setEnabled(wnd.isAvailableLookAndFeel(wnd.getLongLF("win")));
	    group.add(rb5);
	    panel.add(rb5);
  	  	
	}
  
	public void actionPerformed(ActionEvent event)
	{
	    if (event.getActionCommand().equals("Abbrechen")) {
	        endDialog();
	    }
	    else if (event.getActionCommand().equals("Hilfe")) {
	        Base.showBox(this, Help.OPTIONS_HELP, 2);
	    }
	    else if (event.getActionCommand().equals("OK")) {
	    	ButtonModel selected = group.getSelection();
	        if (selected != null) {
	            String value= selected.getActionCommand().toLowerCase();
	            if (selected.getActionCommand().equals("Windows")) {
	                value="win";
	            }
	            if (selected.getActionCommand().equals("Standard (Metal)")) {
	                value="metal";
	            }
	            if (Define.doDebug()) {
	                new Logger("Selektiert: " + value, true);
	            }
	            // L&F aktualisieren
	            conf.setLF(value);
	            wnd.updateLF();
	        }
	        // Member-Daten aktualisiseren
	        if (conf.getUsername().equals("")) {
	            conf.setUsername(txt_user.getText());
	        }
	        conf.setPassword(txt_pw.getText());
		
	        // Proxy-Daten aktualisiseren
	        //if (!txt_phost.getText().equals("")) {
	        	conf.setProxyUrl(txt_purl.getText());
	        	conf.setProxyHost(txt_phost.getText());
				conf.setProxyPort(txt_pport.getText());
				conf.setProxyUser(txt_puser.getText());
				conf.setProxyPass(txt_ppass.getText());
				//}
			//else {
				//conf.setProxyHost("");
		    //}
		
		    // Splashscreen aktivieren?
		    conf.setSplash(spr.isSelected());
		    // Fenster-Infos speichern?
		    conf.setSaveWinInfo(swi.isSelected());
		    // AutoUpload?
		    conf.setAutoUpdate(aup.isSelected());
		    // Systray?
		    conf.setSystray(sys.isSelected());
		    // Dialog schließen
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

	// Methoden aus KeyListener
	public void keyTyped(KeyEvent e)  {}

	public void keyPressed(KeyEvent e)  {}

	public void keyReleased(KeyEvent e)  {
	    if (txt_purl.getText().length() > 0) {
	        txt_phost.setEnabled(false);
	        txt_pport.setEnabled(false);
	        txt_puser.setEnabled(false);
	        txt_ppass.setEnabled(false);
	    }
	    else {
	        txt_phost.setEnabled(true);
	        txt_pport.setEnabled(false);
	        txt_puser.setEnabled(false);
	        txt_ppass.setEnabled(false);
	    }
	    if (txt_purl.getText().length() == 0 && txt_phost.getText().length() > 0) {
	        txt_purl.setEnabled(false);
	        txt_pport.setEnabled(true);
	        txt_puser.setEnabled(true);
	        txt_ppass.setEnabled(true);
	    }
	    else {
	        txt_purl.setEnabled(true);
	        txt_pport.setEnabled(false);
	        txt_puser.setEnabled(false);
	        txt_ppass.setEnabled(false);
	    }
    }
 
}
