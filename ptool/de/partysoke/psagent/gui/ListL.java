/*
 * Created on 28.02.2004
 * by Enrico Tröger
 */
package de.partysoke.psagent.gui;

import javax.swing.event.*;

import de.partysoke.psagent.*;

public class ListL
implements ListSelectionListener {

	private GetAddData data;
	private AddWndStep1 parent;
	
	public ListL(AddWndStep1 parent_tmp, GetAddData data_tmp) {
		data = data_tmp;
		parent = parent_tmp;
	}
	
	public void valueChanged(ListSelectionEvent event) {
		if (Define.doDebug>1) System.out.println(event.toString());
		
		if (event.getLastIndex() == data.getBandsLastIndex()) {
			parent.getTextBnd().setEditable(true);
		}
		else {
			parent.getTextBnd().setEditable(false);
		}
	}
}
