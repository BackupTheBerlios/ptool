/*
 * Created on 01.03.2004
 * by Enrico Tröger
 */

package de.partysoke.psagent.gui;


import java.awt.*;

class AddWndStep2Layout implements LayoutManager {

	public AddWndStep2Layout() {
	}

	public void addLayoutComponent(String name, Component comp) {
	}

	public void removeLayoutComponent(Component comp) {
	}

	public Dimension preferredLayoutSize(Container parent) {
		Dimension dim = new Dimension(0, 0);

		Insets insets = parent.getInsets();
		dim.width = 432 + insets.left + insets.right;
		dim.height = 424 + insets.top + insets.bottom;

		return dim;
	}

	public Dimension minimumLayoutSize(Container parent) {
		Dimension dim = new Dimension(0, 0);
		Insets insets = parent.getInsets();
		dim.width = 432 + insets.left + insets.right;
		dim.height = 424 + insets.top + insets.bottom;

		return dim;
	}

	public void layoutContainer(Container parent) {
		Insets insets = parent.getInsets();

		Component c;
		c = parent.getComponent(0);
		if (c.isVisible()) {c.setBounds(insets.left+232,insets.top+376,128,24);}
		c = parent.getComponent(1);
		if (c.isVisible()) {c.setBounds(insets.left+64,insets.top+376,128,24);}
		c = parent.getComponent(2);
		if (c.isVisible()) {c.setBounds(insets.left+8,insets.top+8,408,24);}
		c = parent.getComponent(3);
		if (c.isVisible()) {c.setBounds(insets.left+8,insets.top+48,176,24);}
		c = parent.getComponent(4);
		if (c.isVisible()) {c.setBounds(insets.left+200,insets.top+48,216,24);}
		c = parent.getComponent(5);
		if (c.isVisible()) {c.setBounds(insets.left+200,insets.top+80,216,24);}
		c = parent.getComponent(6);
		if (c.isVisible()) {c.setBounds(insets.left+8,insets.top+192,176,24);}
		c = parent.getComponent(7);
		if (c.isVisible()) {c.setBounds(insets.left+200,insets.top+192,216,24);}
		c = parent.getComponent(8);
		if (c.isVisible()) {c.setBounds(insets.left+8,insets.top+120,176,24);}
		c = parent.getComponent(9);
		if (c.isVisible()) {c.setBounds(insets.left+200,insets.top+120,216,24);}
		c = parent.getComponent(10);
		if (c.isVisible()) {c.setBounds(insets.left+200,insets.top+152,216,24);}
		c = parent.getComponent(11);
		if (c.isVisible()) {c.setBounds(insets.left+8,insets.top+232,176,24);}
		c = parent.getComponent(12);
		if (c.isVisible()) {c.setBounds(insets.left+200,insets.top+232,216,24);}
		c = parent.getComponent(13);
		if (c.isVisible()) {c.setBounds(insets.left+8,insets.top+272,176,24);}
		c = parent.getComponent(14);
		if (c.isVisible()) {c.setBounds(insets.left+200,insets.top+272,216,72);}
	}
}
