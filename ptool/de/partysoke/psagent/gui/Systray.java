/*
 * Created on 11.12.2004
 * by Enrico Tröger
*/

package de.partysoke.psagent.gui;

import java.awt.*;
import java.awt.event.*;

import com.jeans.trayicon.*;

import de.partysoke.psagent.*;
import de.partysoke.psagent.util.*;


/**
 * Klasse zum Handeln des Systray-Symbols unter Windows
 * 
 */
public class Systray implements ActionListener {

    private MWnd parent;
    private boolean running = false;
    
    public Systray(MWnd src, Image img) {
	    if (Base.getOS() == Define.WINDOWS_OS) {
	        this.parent = src;
	        try {
	            if (!WindowsTrayIcon.isRunning(Define.getOwnName()))
		        	WindowsTrayIcon.initTrayIcon(Define.getOwnName());
				WindowsTrayIcon sicon = new WindowsTrayIcon(img, 16, 16);
  	  			sicon.setToolTipText(Define.getOwnName() + " " + Define.getVersionAsString());
  	  			sicon.addActionListener(this);
  	  			sicon.setPopup(makePopup());
  	  			sicon.setVisible(true);
  	  			this.running = true;
  	    	}
  	    	catch(InterruptedException e) { }
  	    	catch(TrayIconException f) { }
  	    	//catch(UnsatisfiedLinkError g) { }
	    }
    }
    
    public void actionPerformed(ActionEvent cmd) {
        // dieser Listener reagiert nur auf Klick direkt auf das Symbol
        if (parent.isVisible()) parent.setVisible(false);
        else {
            parent.setVisible(true);
            parent.requestFocus();
        }
    }

    
	// Create the popup menu for each Tray Icon (on right mouse click)
	public TrayIconPopup makePopup() {
		// Make new popup menu
		TrayIconPopup popup = new TrayIconPopup();
		TrayIconPopupSimpleItem item;
		// Add show, about, submenu, separator & exit item
		item = new TrayIconPopupSimpleItem("\u00DCber");
		item.addActionListener(parent.getListener());
		popup.addMenuItem(item);

		item = new TrayIconPopupSimpleItem("Einstellungen");
		item.addActionListener(parent.getListener());
		popup.addMenuItem(item);

		item = new TrayIconPopupSimpleItem("Daten herunterladen");
		item.addActionListener(parent.getListener());
		popup.addMenuItem(item);

		popup.addMenuItem(new TrayIconPopupSeparator());

		// Add exit item
		item = new TrayIconPopupSimpleItem("Beenden");
		item.addActionListener(parent.getListener());
		popup.addMenuItem(item);
		
		return popup;
	}


    
    public void close() {
        WindowsTrayIcon.cleanUp();
    }
    
    
    /**
     * Gibt den Status des Systray-Symbols wieder (unter Windows true oder false,
     * je nach Einstellung, unter Linux immer false)
     * @return running
     */
    public boolean isRunning() {
        return this.running;
    }

}


