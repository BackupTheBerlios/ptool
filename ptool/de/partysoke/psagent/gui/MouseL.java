/*
 * Created on 22.01.2004
 * by Enrico Tröger
  */

package de.partysoke.psagent.gui;

import java.awt.event.*;
import javax.swing.*;

import de.partysoke.psagent.*;
import de.partysoke.psagent.util.*;

public class MouseL
extends MouseAdapter
implements MouseListener
{
	private JTable parentTable;
	private ShowEvents se;
	
	public MouseL(JTable table_tmp, ShowEvents se_tmp) {
		parentTable = table_tmp;
		se = se_tmp;
	}
	
	public void mousePressed(MouseEvent event)
	{
		if (event.getClickCount() == 2) {
			
			Details det = null;
			if (parentTable.getName().equals("userevents")) {
				det = new DetailsUser(Start.getMe(), parentTable.getSelectedRow(), se);
			}
			else {
				det = new DetailsMain(Start.getMe(), parentTable.getSelectedRow(), se);
			}
			if (Define.doDebug())
				Base.LogThis("Details-Dialog ID: "+parentTable.getSelectedRow(), true);
			det.setVisible(true);
				
				
		}
	}
}
