/*
 * Created on 16.05.2004
 * by Enrico Tröger
 */


package de.partysoke.psagent.gui;

import java.awt.*;
import javax.swing.*;

import de.partysoke.psagent.*;

public class Splash extends JWindow {
	
	Image img;
	
	public Splash() {
		super();
		MediaTracker mt = new MediaTracker(this);
	  	img = getToolkit().getImage(getClass().getResource(Define.ImageSplash));
	  	mt.addImage(img, 0);
	  	try {
	  		mt.waitForAll();
	  	} catch (InterruptedException e) {}
		this.centerMe();
		this.setVisible(true);
	}
	
	public void paint(Graphics g) {
	  	g.drawImage(img, 0, 0, this);
	}
	
	public void endDialog()
	{
		try {
			Thread.sleep(1300);
		}
		catch (Exception e) {}
		setVisible(false);
		dispose();
	}

	
	private void centerMe() {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Point winPos = new Point((screen.width - 320) / 2,
	                           (screen.height - 240) / 2);
		winPos.x = Math.max(0, winPos.x);
		winPos.y = Math.max(0, winPos.y);
	    this.setBounds(winPos.x, winPos.y, 320, 240);
	}


}
