/*
 * Created on 12.12.2003
 * by Enrico Tröger
 */

package de.partysoke.psagent.gui;


import java.awt.*;

import javax.swing.*;
import javax.swing.table.*;

import de.partysoke.psagent.*;
import de.partysoke.psagent.util.*;


/**
 * Klasse zum Erzeugen des Hauptfensters,
 * mit L&F-Behandlung und Tabellen-Update
 * 
 * @author Enrico Tröger
 */
public class MWnd extends JFrame
{
	private JLabel label_bottom = new JLabel(Define.getNoEvents(), JLabel.LEFT);
	private static JTable table = new JTable();
  	private Config conf = Start.getConf();
  	private MWndHandler listener = new MWndHandler(true, this);
  	private GetAddData getData = null;
	private StatusBar statusBarPanel;
  	private boolean canceled = false;
  	private DefaultTableCellRenderer renders;
    private JMenu ret_fm;
    private JMenu ret_em;
    private Systray systray;
    private boolean systray_active;
    
  	/**
  	 * Konstruktor, der das Fenster aufbaut und die Komponenten lädt
  	 */
  	public MWnd()
  	{
  	    super(Define.getOwnName() + " " + Define.getVersionAsString());
  	    getContentPane().setLayout(new BorderLayout());
  	    JFrame.setDefaultLookAndFeelDecorated(true);
  	    this.assignIcon();
	
  	    // Menü
  	    JMenuBar menubar = new JMenuBar();
  	    menubar.add(createFileMenu()); 
  	    menubar.add(createAddMenu());
  	    menubar.add(createOptionsMenu()); 
  	    menubar.add(createAboutMenu()); 
  	    this.setJMenuBar(menubar);
	
  	    // Titel-Label, mit häßlichem Quickhack, um die Höhe zu bestimmen
  	    JPanel label_panel = new JPanel(new BorderLayout());
  	    Font font = new Font(null, Font.PLAIN, 5);
  	    JLabel title = new JLabel(Define.getOwnName() + " - Das PartySOKe.de - Offline-Tool ", JLabel.CENTER);
  	    JLabel label_leer = new JLabel(" ");
	    label_leer.setFont(font);
	    JLabel label_leer2 = new JLabel(" ");
	    label_leer2.setFont(font);
  	    
	    label_panel.add(label_leer, BorderLayout.NORTH);
  	    label_panel.add(title, BorderLayout.CENTER);
  	    label_panel.add(label_leer2, BorderLayout.SOUTH);
	
  	    JScrollPane scrollp = new JScrollPane(table,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	
  	    // Tabellen-Eigenschaften
  	    table.setName("allevents");
  	    table.setToolTipText("Events-Tabelle");
  	    table.addMouseListener(new MouseL(table, null));
  	    table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
  	    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// Renderer für Tabelle bauen
  	    renders = new DefaultTableCellRenderer();
  	    renders.setHorizontalAlignment(SwingConstants.CENTER);
  	    
  	    // Tabelle mit Werten füllen, falls vorhanden
  	    //this.fillTable();

  	    
  	    // Statusbar
  	    statusBarPanel = new StatusBar(label_bottom);
  	      	    
  	    // Zusammensetzen
  	    getContentPane().add(label_panel, BorderLayout.NORTH);
  	    getContentPane().add(scrollp,BorderLayout.CENTER);
  	    getContentPane().add(statusBarPanel,BorderLayout.SOUTH);
		
  	    //Window-Listener
  	    addWindowListener(listener);
	
  	}

  	/**
  	 * Wechselt das L&F auf das in der Config-Datei angebenene
  	 */
  	public void updateLF() {
  	    try {
  	        UIManager.setLookAndFeel(this.getLongLF(conf.getLF()));
  	    } catch (Exception e) { 
  	      new Logger("Look&Feel wurde nicht gefunden, Standard wird verwendet.", true);
  	    }
  	    SwingUtilities.updateComponentTreeUI(this);
  	  new Logger("Gew\u00E4hltes Look&Feel: " + conf.getLF(), true);
  	}
  
  	/**
  	 * lädt das Symbol in den Windows-Systray
  	 */
  	public void initSystray() {
  	    if (conf.getSystray()) {
  	        this.systray = new Systray(
  	            this,
  	            getToolkit().getImage(getClass().getResource(Define.TrayIcon))
  	    		);
  	        if (this.systray.isRunning()) systray_active = true;
  	    }
  	}
  	
  	
  	/**
  	 * Weißt dem aktuellen Fenster ein Icon zu
  	 */
  	private void assignIcon()
  	{
  	    Image img = getToolkit().getImage(getClass().getResource(Define.ImageIcon));
  	  	this.setIconImage(img);
   	}
  
  	/**
  	 * Holt sich die Daten aus der Daten-Datei, und trägt sie in die Tabelle ein
  	 */
  	public void fillTable() {
  	
  	    if (Base.check_file(this, conf)) {
  	        String[] spaltenNamen = Define.getSpalten();
  	        String[][] inhalt = Base.getAll();
  	        table.setModel(new JTModel(spaltenNamen,inhalt));
  	        inhalt = null;
  	        //Base.calcColumnWidths(table);
  		    // Spalten-Eigenschaften anpassen
  	        /*TableColumnModel c = table.getColumnModel();
  	        c.addColumnModelListener(listener);
  		    TableColumn col;
  		    // Datum
  		    col = c.getColumn(2);
  		    col.setResizable(false);
  		    col.setCellRenderer(renders);
  		    // Kategorie
  		    col = c.getColumn(3);
  		    col.setResizable(false);
  		    col.setCellRenderer(renders);
  		    */
  	        label_bottom.setText(" Stand des Datenbestands: " + Base.getTimeOfArray());
  	        statusBarPanel.setEventLabel();
  	    }
  	    else {
  	      new Logger("Fehler: Datenfile konnte nicht gelesen werden.", true);
  	    }
  	    table.getTableHeader().updateUI();
  	    System.gc();
  	}
  
  	/**
  	 * Gibt den vollqualifierten L&F-Namen des angebenen L&Fs zurück 
  	 * @param data
  	 * @return LongLF
  	 */
  	public String getLongLF(String data) {
  	    String laf="com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
  	
  	    if (data.equals("motif")) {
  	        laf="com.sun.java.swing.plaf.motif.MotifLookAndFeel";	
  	    }
  	    else if (data.equals("win")) {
  	        laf="com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
  	    }
  	    else if (data.equals("metal")) {
  	        laf="javax.swing.plaf.metal.MetalLookAndFeel";	
  	    }
  	    else if (data.equals("mac")) {
  	        laf="com.sun.java.swing.plaf.mac.MacLookAndFeel";	
  	    }
  	   	else if (data.equals("kunststoff")) {
  	  	    laf="com.incors.plaf.kunststoff.KunststoffLookAndFeel";	
      	}

  	    return laf;	
  	}
  
  	/**
  	 * Prüft, ob das L&F laf auf der aktuellen(zur Laufzeit) Plattform verfügbar ist
  	 * @param laf
  	 * @return isAvaiable
  	 */
  	public boolean isAvailableLookAndFeel(String laf) {
  		try {
  		    Class lnfClass = Class.forName(laf);
  		    LookAndFeel newLAF = (LookAndFeel)(lnfClass.newInstance());
  		    return newLAF.isSupportedLookAndFeel();
  		
  		} catch(Exception e) {
  		    return false;
  		}
  	}
  
  
  	/**
   	* Gibt die Tabelle zurück
   	* @return table
   	*/
  	public JTable getTable() {
      return table;
  	}
  
  	/**
   	* Gibt das Objekt des ActionListeners zurück
   	* @return listener
   	*/
  	public MWndHandler getListener() {
      return listener;
  	}
  	
    /**
     * Gibt den Status des Systray-Symbols wieder (unter Windows true oder false,
     * je nach Einstellung, unter Linux immer false)
     * @return systray_active
     */
  	public boolean isSystray_active() {
      return this.systray_active;
  	}
  	

  
  	/**
  	 * Gibt das Config-Objekt zurück
  	 * @return conf
  	 */
  	public Config getConfig() {
  	    return conf;
  	}
  
 	/**	Gibt canceled zurück
	 * @return canceled
	 */
	public boolean isCanceled() {
		return canceled;
	}
	
	/**	Setzt canceled.
	 * @param canceled
	 */
	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}
	
	/** Weist getData ein entsprechendes Objekt zu.
	 * @param getData
	 */
	public void createGetData() {
		this.getData = new GetAddData(conf);
	}

	/**
	 * Gibt das GetAddData-Objekt zurück
	 * @return getData
	 */
	public GetAddData getGetData() {
		return getData;
	}
  
	/**
	 * Liest die Daten für das Event-Label in der StatusBar neu ein
	 * und ruft updateFileMenu() auf.
	 */
	public void updateStatusBarEventlabel() {
	    this.statusBarPanel.setEventLabel();
	    updateFileMenuUE();
	}
	
	/**
	 * Liest den Status für den Menüpunkt "Eigene Events hochladen" und "Events ansehen"
	 * neu ein
	 */
	public void updateFileMenuUE() {
	    if (Base.getUserEventsCount() == 0) {
	        this.ret_fm.getItem(1).setEnabled(false);
	        this.ret_em.getItem(1).setEnabled(false);
	    }
	    else {
	        this.ret_fm.getItem(1).setEnabled(true);
	        this.ret_em.getItem(1).setEnabled(true);
	    }
	}
	
	/**
	 * Liest den Status für den Menüpunkt "News lesen" neu ein
	 */
	public void updateFileMenuNews() {
	    if (Base.getEventsCount() == 0) {
	        this.ret_fm.getItem(0).setEnabled(false);
	    }
	    else {
	        this.ret_fm.getItem(0).setEnabled(true);
	    }
	}
	
	
    
	/**
	 * Schließt das Hauptfenster, und beendet das Programm.
	 */
	public void shutDown() {
	    this.setVisible(false);
	    this.conf.setWinInfo(this.getBounds());
	    this.conf.writeFile();
		this.dispose();
		if (this.systray_active) systray.close();
		System.exit(0);
	}
  
	/**
	 * File-Menü erzeugen
	 */
	private JMenu createFileMenu()
	{
	    ret_fm = new JMenu(Define.getOwnName());
	    ret_fm.setMnemonic('P');
	    JMenuItem mi;
	    // News
	    mi = new JMenuItem("News lesen", 'e');
	    setCtrlAccelerator(mi, 'N');
	    mi.addActionListener(listener);
	    ret_fm.add(mi);
	    // Events-Update
	    mi = new JMenuItem("Eigene Events hochladen", 'i');
	    setCtrlAccelerator(mi, 'U');
	    mi.addActionListener(listener);
	    mi.setEnabled(false);
	    ret_fm.add(mi);
	    // Update
	    mi = new JMenuItem("Daten herunterladen", 'a');
	    setCtrlAccelerator(mi, 'D');
	    mi.addActionListener(listener);
	    ret_fm.add(mi);
	    ret_fm.addSeparator();
	    // Beenden
	    mi = new JMenuItem("Beenden", 'b');
	    setCtrlAccelerator(mi, 'X');
	    setAltAccelerator(mi, 'X');
	    mi.addActionListener(listener);
	    ret_fm.add(mi);
	    return ret_fm;
	}
  
	/**
	 * About-Menü erzeugen
	 */
	private JMenu createAboutMenu()
	{
	    JMenu ret = new JMenu("Hilfe");
	    ret.setMnemonic('H');
	    JMenuItem mi;
	    // Neue Version
	    mi = new JMenuItem("Nach neuer Version suchen", 'n');
	    mi.addActionListener(listener);
	    ret.add(mi);
	    // PartySOKe.de
	    mi = new JMenuItem("PartySOKe.de", 'p');
	    mi.addActionListener(listener);
	    ret.add(mi);
	    // Forum-PartySOKe.de
	    mi = new JMenuItem("PartySOKe.de - Forum", 'f');
	    mi.addActionListener(listener);
	    ret.add(mi);
	    // $SELF-Seite
	    mi = new JMenuItem(Define.getOwnName() + "-Webseite", 'a');
	    mi.addActionListener(listener);
	    ret.add(mi);
	    // Separator
	    ret.addSeparator();
	    // Über
	    mi = new JMenuItem("\u00DCber", 'b');
	    mi.addActionListener(listener);
	    ret.add(mi);
	    return ret;
	}

	/**
	 * Eintrags-Menü erzeugen
	 */
	private JMenu createAddMenu()
	{
	    ret_em = new JMenu("Eigene Events");
	    ret_em.setMnemonic('E');
	    JMenuItem mi;
	    // Eintragen
	    mi = new JMenuItem("Event eintragen", 'e');
	    setCtrlAccelerator(mi, 'E');
	    mi.addActionListener(listener);
	    ret_em.add(mi);
	    // Ansehen
	    mi = new JMenuItem("Events ansehen", 'a');
	    setCtrlAccelerator(mi, 'A');
	    mi.addActionListener(listener);
	    ret_em.add(mi);
	    return ret_em;
	}
  
	/**
	 * Optionen-Menü erzeugen
	 */
	private JMenu createOptionsMenu()  {
	    JMenu ret = new JMenu("Optionen");
	    ret.setMnemonic('O');
	    JMenuItem mi;
	    // Optionen-Dialog
	    mi = new JMenuItem("Einstellungen", 'i');
	    setCtrlAccelerator(mi, 'O');
	    mi.addActionListener(listener);
	    ret.add(mi);
	    // Separator
	    ret.addSeparator();
  	
	    // L&F-Wahl - Untemenü
	    JMenu m = new JMenu("Look&Feel");
	    m.setMnemonic('L');
	    // Die verschienden L&Fs
	    mi = new JMenuItem("Standard (Metal)");
	    mi.setEnabled(this.isAvailableLookAndFeel(this.getLongLF("metal")));
	    mi.addActionListener(listener);
	    m.add(mi);
	    // Die verschienden L&Fs
	    mi = new JMenuItem("Kunststoff");
	    mi.setEnabled(this.isAvailableLookAndFeel(this.getLongLF("kunststoff")));
	    mi.addActionListener(listener);
	    m.add(mi);
	    // Die verschienden L&Fs
	    mi = new JMenuItem("Motif");
	    mi.setEnabled(this.isAvailableLookAndFeel(this.getLongLF("motif")));
	    mi.addActionListener(listener);
	    m.add(mi);
	    // Die verschienden L&Fs
	    mi = new JMenuItem("Mac");
	    mi.setEnabled(this.isAvailableLookAndFeel(this.getLongLF("mac")));
	    mi.addActionListener(listener);
	    m.add(mi);
  	
	    // Die verschienden L&Fs
	    mi = new JMenuItem("Windows");
	    mi.setEnabled(this.isAvailableLookAndFeel(this.getLongLF("win")));
	    mi.addActionListener(listener);
	    m.add(mi);
  	
	    // Untermenü hinzufügen
	    ret.add(m);
	    return ret;
	}
  
	/**
	 * Fügt mi die Taste acc als Event hinzu (?)
	 * @param mi
	 * @param acc
	 */
	private void setCtrlAccelerator(JMenuItem mi, char acc)  {
	    KeyStroke ks = KeyStroke.getKeyStroke(acc, Event.CTRL_MASK);
	    mi.setAccelerator(ks);
	}
  
	/**
	 * Fügt mi die Taste acc als Event hinzu (?)
	 * @param mi
	 * @param acc
	 */
	private void setAltAccelerator(JMenuItem mi, char acc)  {
	    KeyStroke ks = KeyStroke.getKeyStroke(acc, Event.ALT_MASK);
	    mi.setAccelerator(ks);
	}
  
	
}