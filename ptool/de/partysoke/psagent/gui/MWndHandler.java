
/*
 * Created on 17.05.2004
 * by Enrico Tröger
 */

package de.partysoke.psagent.gui;

import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.table.*;

import de.partysoke.psagent.*;
import de.partysoke.psagent.util.*;

public class MWndHandler extends WindowClosingAdapter implements ActionListener, TableColumnModelListener {

	MWnd src;
  	TableColumnModel tcm;

	public MWndHandler(boolean exit, MWnd wnd) {
		super(exit,wnd);
		src = wnd;
		tcm = src.getTable().getColumnModel();
	}

	public void actionPerformed(ActionEvent event)
	{

	  	String cmd = event.getActionCommand();
		
	  	if (cmd.equals("Daten herunterladen")) {
			Update dlg = new Update(src);
			dlg.setVisible(true);
			// Tabelle mit neuen Werten "updaten", nur wenn
			// Vorgang nicht abgebrochen wurde 
			if (! dlg.isCanceled()) {
			    src.fillTable();
				// Event-Rohdaten neu einlesen
			    src.createGetData();
			}
			dlg = null;
		}
	  	else if (cmd.equals("Eigene Events hochladen")) {
			// füllen!!!			
		}
	  	else if (cmd.equals("News lesen")) {
			
			String text = Base.parseNews();
			if (text.equals("")) {
				Base.showBox("Es sind noch keine News verf\u00FCgbar. Klicke bitte auf \"Update\", um Dir die Daten herunterzuladen.",1);
			}
			else {
				News news = new News(src, text);
				news.setVisible(true);
				news = null;
			}
			text = null;
		}
		else if (cmd.equals("Event eintragen")) {
			
		    if (src.getGetData() == null) {
		        src.createGetData();
			}
			if (src.getGetData().isCreated()) {
				AddData data = new AddData();	// Container für eingegebene Daten
			    GetAddData tmp = src.getGetData();	// Container für vorgegebene Daten
				AddWndStep1 add = new AddWndStep1(src, tmp, data);
				add.setVisible(true);
				if (!src.isCanceled()) {
					AddWndStep2 add2 = new AddWndStep2(src, tmp, data);
					add2.setVisible(true);
					add2 = null;
					// wenn nicht abgebrochen wurde, data schließen(Event auf Platte wegschreiben)
					if (!src.isCanceled()) data.close();
					// userEventsCount in Base updaten, Statusbar updaten
				    Base.readUserEventsCount();
					src.updateStatusBarEventlabel();
				}
				add = null;
				data = null;
				
			}
		}
		else if (cmd.equals("Events ansehen")) {
			ShowEvents show = new ShowEvents(src);
			show.setVisible(true);
			show = null;
		}
		
		else if (cmd.equals("Einstellungen")) {
			Options opt = new Options(src);
			opt.setVisible(true);
			opt = null;
		}
		else if (cmd.equals("\u00DCber")) {
			About abt = new About(src);
			abt.setVisible(true);
			abt = null;
		}
		else if (cmd.equals("PartySOKe.de")) {
		    OpenUrl.openURL(Define.getUrl_partysoke());
		}
		else if (cmd.equals("PartySOKe.de - Forum")) {
			OpenUrl.openURL(Define.getUrl_forum());
		}
		else if (cmd.equals(Define.getOwnName() + "-Webseite")) {
		    OpenUrl.openURL(Define.getUrl_self());
		}
		else if (cmd.equals("Nach neuer Version suchen")) {
			Base.searchNewVersion(src);
		}
		else if (cmd.equals("Beenden")) {
		    src.shutDown();
		}
		// L&F aktualisieren
		else if (cmd.equals("Windows")) {
			src.getConfig().setLF("win");
			src.updateLF();
		}
		else if (cmd.equals("Standard (Metal)")) {
			src.getConfig().setLF("metal");
			src.updateLF();
		}
		else if (cmd.equals("Mac")) {
			src.getConfig().setLF(cmd.toLowerCase());
			src.updateLF();
		}
		else if (cmd.equals("Motif")) {
			src.getConfig().setLF(cmd.toLowerCase());
			src.updateLF();
		}
		else if (cmd.equals("Kunststoff")) {
			src.getConfig().setLF(cmd.toLowerCase());
			src.updateLF();
		}
		System.gc();
	}
	int i = 0;
	public void columnAdded(TableColumnModelEvent e) {}
	public void columnMarginChanged(ChangeEvent e) {
	    TableColumn col;
	    // Datum
	    col = tcm.getColumn(2);
	    col.setPreferredWidth(50);
	    // Kategorie
	    col = tcm.getColumn(3);
	    col.setPreferredWidth(50);

	}
	public void columnMoved(TableColumnModelEvent e) {}
	public void columnRemoved(TableColumnModelEvent e) {}
	public void columnSelectionChanged(ListSelectionEvent e) {}

	
	
	

}
