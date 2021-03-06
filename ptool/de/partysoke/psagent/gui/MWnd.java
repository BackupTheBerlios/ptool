/*
 * Created on 12.12.2003
 * by Enrico Tröger
 */

package de.partysoke.psagent.gui;


import java.awt.*;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.tree.*;

import de.partysoke.psagent.*;
import de.partysoke.psagent.util.*;


/**
 * Klasse zum Erzeugen des Hauptfensters,
 * mit L&F-Behandlung, Systray-Support und Tabellen-Update
 * 
 * @author Enrico Tröger
 */
public class MWnd extends JFrame
{
	private JLabel label_bottom = new JLabel(Define.getNoEvents(), JLabel.LEFT);
	private static JTable table = new JTable();
	private JTree tree = new JTree();
	private JScrollPane treeSpane;
	private DetailsTree detailsPanel;
  	private Config conf = Start.getConf();
  	private MWndHandler listener = new MWndHandler(true, this);
  	private GetAddData getData = null;
	private StatusBar statusBarPanel;
  	private boolean canceled = false;
  	private DefaultTableCellRenderer renders;
    private JMenu ret_fm;
    private JMenu ret_em;
    private JMenu ret_lf;
    private Systray systray;
    private boolean systray_active;
    private String oldView;
    
  	/**
  	 * Konstruktor, der das Fenster aufbaut und die Komponenten lädt
  	 */
  	public MWnd()
  	{
  	    super(Define.getOwnName() + " " + Define.getVersionAsString());
  	    getContentPane().setLayout(new BorderLayout(10, 5));
  	    JFrame.setDefaultLookAndFeelDecorated(true);
  	    this.assignIcon();
	
  	    oldView = conf.getView();
  	    
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
  	    JLabel title = new JLabel(Define.getOwnName() + " - Das PartySOKe.de - Offline-Tool", JLabel.CENTER);
  	    JLabel label_leer = new JLabel(" ");
	    label_leer.setFont(font);
	    JLabel label_leer2 = new JLabel(" ");
	    label_leer2.setFont(font);
  	    
	    label_panel.add(label_leer, BorderLayout.NORTH);
  	    label_panel.add(title, BorderLayout.CENTER);
  	    label_panel.add(label_leer2, BorderLayout.SOUTH);
	
  	    
  	    // Statusbar
  	    statusBarPanel = new StatusBar(label_bottom);
  	      	    
  	    // Zusammensetzen - Teil 1
  	    getContentPane().add(label_panel, BorderLayout.NORTH);

  	    
  	    if (conf.getView().equals("tree")) {
  	        /* Event-Baum */
  	  	    treeSpane = new JScrollPane(tree);
  			this.tree.getSelectionModel().setSelectionMode(
  			        TreeSelectionModel.SINGLE_TREE_SELECTION);
  			this.tree.addTreeSelectionListener(listener);
  			this.tree.expandPath(this.tree.getSelectionPath());
  			this.tree.setRootVisible(true);

  			detailsPanel = new DetailsTree();
  	  	    
  			JSplitPane treeSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
  			treeSplitPane.setLeftComponent(treeSpane);
  			treeSplitPane.setRightComponent(detailsPanel);
  			
  			// Zusammensetzen - Teil 2
  			getContentPane().add(treeSplitPane,BorderLayout.CENTER);
  	  	}
  	    else {
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
  	  	    
  	  	    // Zusammensetzen - Teil 2
  	  	    getContentPane().add(scrollp,BorderLayout.CENTER);
  	    }
  	    
  	    // Zusammensetzen - Teil 3
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
  	      new Logger("Look&Feel wurde nicht gefunden, Standard wird verwendet.");
  	    }
  	    SwingUtilities.updateComponentTreeUI(this);
  	  new Logger("Gew\u00E4hltes Look&Feel: " + conf.getLF(), true);
  	  this.updateFileMenuLF();
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
  	 * Gibt das Panel zurück, in dem Event-Details in der Event-Baum-Ansicht stehen
  	 * @return detailsPanel
  	 */
  	public DetailsTree getDetailsPanel() {
  	    return detailsPanel;
  	}
  	
  	
  	/**
  	 * wird von fillTable() aufgerufen und füllt den Event-Baum mit Daten
  	 * @param events
  	 */
  	private void fillTree(String[][] events) {

  	    // wenn die Blätter den Namen darstellen sollen 0, ansonsten 1
  	    // es kommt Define.VIEW_COL_NAME oder Define.VIEW_COL_LOC zurück,
  	    // was genau auf den Index des Arrays passt
  	    int column = conf.getViewCol();
  	    
  	    // Der Baum
	    Vector dates = new Vector();
	    for (int i = 0; i < events.length; i++) {
	        if (! dates.contains(events[i][2]))
	            dates.add(events[i][2]);
	    }
	    DefaultMutableTreeNode root, date;
		root = new DefaultMutableTreeNode("PartySOKe.de - Events");
		for (int i = 0; i < dates.size(); i++) {
			date = new DefaultMutableTreeNode(dates.get(i));
			root.add(date);
			for (int j = 0; j < events.length; j++) {
			    if (events[j][2].equals(dates.get(i))) {
			        date.add(new MyTreeNode(events[j][column], j));
			    }
			}
		}
		//JTree erzeugen
		this.tree.setModel(new DefaultTreeModel(root));
		this.tree.expandPath(this.tree.getSelectionPath());
		        
		// Baum komplett erweitern und somit in Scrollpane einpassen
		for (int i = root.getChildCount(); i >= 1; i--) {
		    this.tree.expandRow(i);
	    }
		Dimension size = this.tree.getPreferredSize();
		size.width += 30;
		this.treeSpane.getViewport().setPreferredSize(size);
		
		this.detailsPanel.changeEvent(0);
  	}

  	
  	/**
  	 * Holt sich die Daten aus der Daten-Datei, und trägt sie in die Tabelle ein
  	 * TODO: fillIrgendwas() machen, und daraus dann fillTable() oder fillTree()
  	 * aufrufen
  	 */
  	public void fillTable() {
  	    boolean success = true;
  	    String[][] daten;
  	    
  	    if (conf.exists() && conf.isRecent() && Base.check_file(this, conf)) {
  	        daten = Base.getAll();
  	        if (daten.length == 0) {
  	            success = false;
  	        }
  	        else {
  	            if (conf.getView().equals("tree")) {
  	                this.fillTree(daten);
  	            }
  	            else {
  	                table.setRowHeight(18);
  	                table.setModel(new JTModel(Define.getSpalten(), daten));
  	                //Base.calcColumnWidths(table);
  	                // Spalten-Eigenschaften anpassen
  	                readColumnWidths();
  	                TableColumnModel c = table.getColumnModel();
  	                //c.addColumnModelListener(listener);
  	                TableColumn col;
  	                // Datum
  	                col = c.getColumn(2);
  	                col.setResizable(false);
  	                col.setCellRenderer(renders);
  	                col.setMaxWidth(80);
  	                col.setPreferredWidth(80);
  	                // Kategorie
  	                col = c.getColumn(3);
  	                col.setResizable(false);
  	                col.setCellRenderer(renders);
  	                col.setMaxWidth(50);
  	                col.setPreferredWidth(50);
  	            }
	            
  	        }
  	        label_bottom.setText(" Stand des Datenbestands: " + Base.getTimeOfArray());
  	        statusBarPanel.setEventLabel();
  	    }
  	    else {
  	      new Logger("Fehler: Datenfile konnte nicht gelesen werden.", true);
  	      success = false;
  	    }
  	    
  	    if (!success) {
            // Keine Events vorhanden (kein Download, Daten zu alt, ...)
            daten = new String[1][3];
            if (conf.getView().equals("tree")) {
                daten[0][0] = "Fehler!";
                daten[0][2] = "Fehler!";
                fillTree(daten);
            }
            else {
                daten[0][0] = "Keine Daten vorhanden! Daten-Update erforderlich";
                table.setRowHeight(50);
                
                table.setModel(new JTModel(new String[1], daten));
                // Zelleninhalt zentrieren
                DefaultTableCellRenderer render = new DefaultTableCellRenderer();
                render.setHorizontalAlignment(SwingConstants.CENTER);
                table.getColumnModel().getColumn(0).setCellRenderer(render);
            }

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
  	    
  	    if (data.equals("motif")) {
  	        return "com.sun.java.swing.plaf.motif.MotifLookAndFeel";	
  	    }
  	    else if (data.equals("win")) {
  	        return "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
  	    }
  	    else if (data.equals("metal")) {
  	        return "javax.swing.plaf.metal.MetalLookAndFeel";	
  	    }
  	    else if (data.equals("mac")) {
  	        return "com.sun.java.swing.plaf.mac.MacLookAndFeel";	
  	    }
  	   	else if (data.equals("kunststoff")) {
  	   	    return "com.incors.plaf.kunststoff.KunststoffLookAndFeel";	
      	}
  	   	else return "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
	
  	}
  
  	/**
  	 * Prüft, ob das angegebene L&F dem aktuellen entspricht 
  	 * @param data
  	 * @return isLF
  	 */
  	public boolean isLF(String data) {
  	    if (data.equals(conf.getLF()))
  	        return true;
  	   	else 
  	   	    return false;
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
	        this.ret_fm.getItem(2).setEnabled(false);
	        this.ret_em.getItem(1).setEnabled(false);
	    }
	    else {
	        this.ret_fm.getItem(2).setEnabled(true);
	        this.ret_em.getItem(1).setEnabled(true);
	    }
	}
	
	/**
	 * Liest den Status für den Menüpunkt "News lesen" (0), "Events drucken"(1) und
	 * "Eigenes Event eintragen" neu ein
	 */
	public void updateFileMenuNews() {
	    if (Base.getEventsCount() < 1) {
	        this.ret_fm.getItem(0).setEnabled(false);
	        this.ret_fm.getItem(1).setEnabled(false);
	        this.ret_em.getItem(0).setEnabled(false);
	    }
	    else {
	        this.ret_fm.getItem(0).setEnabled(true);
	        this.ret_fm.getItem(1).setEnabled(true);
	        this.ret_em.getItem(0).setEnabled(true);
	    }
	}
	
	/**
	 * Liest den Status für die L&Fs neu ein, und aktualisiert das Menü
	 * TODO: mal überarbeiten!!!
	 */
	public void updateFileMenuLF() {
	    this.ret_lf.getItem(0).setSelected(false);
	    this.ret_lf.getItem(1).setSelected(false);
	    this.ret_lf.getItem(2).setSelected(false);
	    this.ret_lf.getItem(3).setSelected(false);
	    this.ret_lf.getItem(4).setSelected(false);
	    
	    if (conf.getLF().equals("metal")) {
	       this.ret_lf.getItem(0).setSelected(true); 
	    }
	    else if (conf.getLF().equals("kunststoff")) {
	       this.ret_lf.getItem(1).setSelected(true); 
	    }
	    else if (conf.getLF().equals("motif")) {
	        this.ret_lf.getItem(2).setSelected(true);
	    }
	    else if (conf.getLF().equals("mac")) {
	        this.ret_lf.getItem(3).setSelected(true);
	    }
	    else if (conf.getLF().equals("win")) {
	        this.ret_lf.getItem(4).setSelected(true);
	    }
	}
		
    /**
     * Ließt die Spaltenbreiten ein, und schreibt sie in die Config
     */
	private void saveColumnWidths() {
        TableColumnModel c = table.getColumnModel();
	    TableColumn col;
	    StringBuffer widths = new StringBuffer();
        for (int i = 0; i < c.getColumnCount(); i++) {
    	    col = c.getColumn(i);
    	    widths.append(col.getWidth());
    	    if (i != c.getColumnCount() - 1) widths.append(",");
        }
	    conf.setColumnWidths(widths.toString());
    }
	
    /**
     * Ließt die Spaltenbreiten ein, und ändert sie in der Tabelle
     * TODO: evtl. auftretende Exception untersuchen, wie kann die auftreten???
     */
	private void readColumnWidths() {
        TableColumnModel c = table.getColumnModel();
	    TableColumn col;
	    int[] widths = conf.getColumnWidths();
        if (widths[0] != -1) {
            try {
                for (int i = 0; i < c.getColumnCount(); i++) {
                    col = c.getColumn(i);
                    col.setPreferredWidth(widths[i]);
                }
            }
            catch (ArrayIndexOutOfBoundsException e) {
               new Logger("Schreibe bitte eine Mail an info@partsoke.de" +
                       		"mit dieser Ausgabe! Bitte, das ist wichtig.\n" + e); 
            }
         }
    }
	
	/**
	 * Schließt das Hauptfenster, und beendet das Programm.
	 */
	public void shutDown() {
	    this.setVisible(false);
	    if (this.oldView.equals("table") && table.getColumnCount() > 1)
	        this.saveColumnWidths();
	    if (this.conf.getSaveWinInfo()) this.conf.setWinInfo(this.getBounds());
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
	    // Drucken
	    mi = new JMenuItem("Events drucken", 'i');
	    setCtrlAccelerator(mi, 'P');
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
  	
	    // L&F-Wahl - Untermenü
	    ret_lf = new JMenu("Look&Feel");
	    ret_lf.setMnemonic('L');
	    
	    // Die verschienden L&Fs
	    mi = new JRadioButtonMenuItem("Standard (Metal)", isLF("metal"));
	    mi.setEnabled(this.isAvailableLookAndFeel(this.getLongLF("metal")));
	    mi.addActionListener(listener);
	    ret_lf.add(mi);
	    
	    // Die verschienden L&Fs
	    mi = new JRadioButtonMenuItem("Kunststoff", isLF("kunststoff"));
	    mi.setEnabled(this.isAvailableLookAndFeel(this.getLongLF("kunststoff")));
	    mi.addActionListener(listener);
	    ret_lf.add(mi);
	    
	    // Die verschienden L&Fs
	    mi = new JRadioButtonMenuItem("Motif", isLF("motif"));
	    mi.setEnabled(this.isAvailableLookAndFeel(this.getLongLF("motif")));
	    mi.addActionListener(listener);
	    ret_lf.add(mi);
	    
	    // Die verschienden L&Fs
	    mi = new JRadioButtonMenuItem("Mac", isLF("mac"));
	    mi.setEnabled(this.isAvailableLookAndFeel(this.getLongLF("mac")));
	    mi.addActionListener(listener);
	    ret_lf.add(mi);
  	
	    // Die verschienden L&Fs
	    mi = new JRadioButtonMenuItem("Windows", isLF("win"));
	    mi.setEnabled(this.isAvailableLookAndFeel(this.getLongLF("win")));
	    mi.addActionListener(listener);
	    ret_lf.add(mi);
  	
	    // Untermenü hinzufügen
	    ret.add(ret_lf);
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