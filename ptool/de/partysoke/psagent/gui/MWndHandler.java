
/*
 * Created on 17.05.2004
 * by Enrico Tröger
 */

package de.partysoke.psagent.gui;

import java.awt.event.*;

import javax.swing.JTree;
import javax.swing.event.*;
import javax.swing.table.*;

import com.jeans.trayicon.*;

import de.partysoke.psagent.*;
import de.partysoke.psagent.util.*;

public class MWndHandler extends WindowClosingAdapter implements ActionListener, TableColumnModelListener, TreeSelectionListener {

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
        if (src.isSystray_active()) {
            // das ist eine Art Wrapper, um die Kommandos aus dem Systray
            // hier verfügbar zu machen, very dirty!!!
            try {
                TrayIconPopupSimpleItem sys = (TrayIconPopupSimpleItem)event.getSource();
                cmd = sys.getName();
            }
	  		catch(ClassCastException e) {}
        }
		
	  	if (cmd.equals("Daten herunterladen")) {
			Update dlg = new Update(src, Define.EVENTDOWNLOAD);
			dlg.setVisible(true);
			// Tabelle mit neuen Werten "updaten", nur wenn
			// Vorgang nicht abgebrochen wurde 
			if (! dlg.isCanceled()) {
			    src.fillTable();
				// Event-Rohdaten neu einlesen
			    try {
			        src.createGetData();
			    }
			    catch(RuntimeException e) {
			        if (Define.doDebug > 1) new Logger(e.toString());
			    }
			}
			dlg = null;
			src.updateFileMenuNews();
		}
	  	else if (cmd.equals("Eigene Events hochladen")) {
			// füllen!!!			
			Update dlg = new Update(src, Define.EVENTUPLOAD);
			dlg.setVisible(true);
	  	    
	  	    /*if (this.running && Base.getUserEventsCount() > 0) {
		        rc = sendUserEvents();
		        parent.ProgressBarNext(15);
		    }
		    else {
		        rc = -1;
		        parent.ProgressBarNext(15);
		    }
		    */
		    
	  	    
	  	    Base.readUserEventsCount();
			src.updateStatusBarEventlabel();
			dlg = null;
		}
	  	else if (cmd.equals("Events drucken")) {
	  	    Base.print();
		}
	  	else if (cmd.equals("News lesen")) {
			
			/*String text = ;
			if (text.equals("")) {
				Base.showBox("Es sind noch keine News verf\u00FCgbar. Klicke bitte auf \"Update\", um Dir die Daten herunterzuladen.",1);
			}
			else {
				*/
	  	    	News news = new News(src, Base.parseNews());
				news.setVisible(true);
				news = null;
			//}
			//text = null;
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
	    // wird zur Zeit nicht benutzt (vergessen wozu das gedient hat)
	    TableColumn col;
	    // Datum
	    col = tcm.getColumn(2);
	    col.setPreferredWidth(30);
	    // Kategorie
	    col = tcm.getColumn(3);
	    col.setPreferredWidth(30);

	}
	public void columnMoved(TableColumnModelEvent e) {}
	public void columnRemoved(TableColumnModelEvent e) {}
	public void columnSelectionChanged(ListSelectionEvent e) {}

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
	        src.getDetailsPanel().changeEvent(mtn.getID());
	    }
    }

	
	
	

}
