/*
 * Created on 29.01.2004
 * by Enrico Tröger
  */

package de.partysoke.psagent.gui;


import javax.swing.*;
import java.awt.*;

public class MyLabel
extends JLabel
{
	MyLabel (String text, int horAlgm, Font fnt) {
		super(text,horAlgm);
		setFont(fnt);
	}
	
	MyLabel (String text, int horAlgm) {
		super(text,horAlgm);
	}
	
	MyLabel (String text, Font fnt) {
		super(text);
		setFont(fnt);
	}
	
	MyLabel (String text, int horAlgm, int width, int height) {
		super(text,horAlgm);
		setSize(width,height);
	}
}
