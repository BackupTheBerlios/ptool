/*
 * Created on 28.02.2004
 * by Enrico Tröger
 */

package de.partysoke.psagent.gui;

import javax.swing.event.*;

import de.partysoke.psagent.*;
import de.partysoke.psagent.util.*;

public class ListL
implements ListSelectionListener {

	private GetAddData data;
	private AddWndStep1 parent;
	
	public ListL(AddWndStep1 parent_tmp, GetAddData data_tmp) {
		data = data_tmp;
		parent = parent_tmp;
	}
	
	public void valueChanged(ListSelectionEvent event) {
		if (Define.doDebug>2) new Logger("Band-Auswahl: " + event.toString());
		
		if (parent.getListBnd().getSelectedIndex() == data.getBandsLastIndex()) {
			parent.getTextBnd().setEditable(true);
		}
		else {
			parent.getTextBnd().setEditable(false);
		}
	}
}
