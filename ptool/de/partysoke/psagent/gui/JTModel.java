/*
 * Created on 21.12.2003
 * by Enrico Tröger
  */


package de.partysoke.psagent.gui;

import javax.swing.table.*;
import de.partysoke.psagent.*;

class JTModel
extends AbstractTableModel {
	
    private String[] columnNames;
    private Object[][] data;

    JTModel(String[] col_data, Object[][] row_data) {
    	columnNames=col_data;
    	data=row_data;
    }

    JTModel(String[] col_data, Data[] row_data) {
    	columnNames=col_data;
    	for (int i=0; i < row_data.length; i++) {
    		data[i]=(Object[]) row_data[i].toStringArray();
    	}
    	
    }
    
    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return data.length;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        return data[row][col];
    }

    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

}

