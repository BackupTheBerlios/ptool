/*
 * Created on 17.12.2003
 * by Enrico Tröger
  */

/**
 * Klasse zum Schließen von Fenstern "per Kreuz"
 * übernommen von "GoTo Java 2"
 */


package de.partysoke.psagent.gui;

import java.awt.event.*;

public class WindowClosingAdapter
	extends WindowAdapter
	{
	  private boolean exitSystem;
	  private MWnd src;
	  
	  /**
	   * Erzeugt einen WindowClosingAdapter zum Schließen 
	   * des Fensters. Ist exitSystem true, wird das komplette
	   * Programm beendet.
	   */
	  public WindowClosingAdapter(boolean exitSystem)
	  {
	  	this.exitSystem = exitSystem;
	  }
	  
	  public WindowClosingAdapter(boolean exitSystem, MWnd wnd)
	  {
	  	this.exitSystem = exitSystem;
	  	this.src = wnd;
	  }
	  
	  /**
	   * Erzeugt einen WindowClosingAdapter zum Schließen 
	   * des Fensters. Das Programm wird nicht beendet.
	   */
	  public WindowClosingAdapter()
	  {
		this(false);
	  }
  
	  public void windowClosing(WindowEvent event)
	  {
		if (exitSystem)
		    src.shutDown();
		else {
		  	event.getWindow().setVisible(false);
			event.getWindow().dispose();
		}
	  }
}
