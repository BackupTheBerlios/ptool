/*
 * PSwing Utilities -- Nifty Swing Widgets
 * Copyright (C) 2002  Pallas Technology
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 * Pallas Technology
 * 1170 HOWELL MILL RD NW
 * SUITE 306
 * ATLANTA GEORGIA 30318
 * 
 * PHONE 404.983.0623
 * EMAIL info@pallastechnology.com
 * 
 * www.pallastechnology.com
 **************************************************************************
 * $Archive: SwingTools$
 * $FileName: DateFocusListener.java$
 * $FileID: 9$
 *
 * Last change:
 * $AuthorName: Rob MacGrogan$
 * $Date: 2005/01/31 19:17:56 $
 * $VerID: 81$
 * $Comment: Strip off the time component of date. $
 **************************************************************************/
package com.pallas.swing.date;

import java.awt.KeyboardFocusManager;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;

import javax.swing.JTextField;

/**
 * Title:   $FileName: DateFocusListener.java$
 * @version $VerNum: 6$
 * @author $AuthorName: Rob MacGrogan$<br><br>
 * 
 * $Description: Focus listener for DateComboBox component.$<br>
 * $KeyWordsOff: $<br><br>
 */
public class DateFocusListener implements FocusListener {

  private Object prevSelection = null;
  DateComboBox parent = null;
  
  private static final long LOST_INTERVAL = 50;//50 millis.

  public DateFocusListener(DateComboBox parent) {
    this.parent = parent;
  }


  public void focusGained(FocusEvent ev){
    prevSelection = parent.getSelectedItem();
    JTextField fld = (JTextField)parent.getEditor().getEditorComponent();
    fld.selectAll();
  }

  public void focusLost(FocusEvent ev){
    
    //Check if user entered a valid date according to pattern. If so,
    //update the selected value. If not, reset to previous value???
    JTextField fld = (JTextField)parent.getEditor().getEditorComponent();
    String enteredText = fld.getText();
    if (enteredText != null && ! enteredText.trim().equals("")){
      try{
        String sDate = enteredText.trim();
        java.util.Date date = parent.parseDateString(sDate);
        parent.setDate(date);
      }
      catch (java.text.ParseException ex){
        //Invalid date. Update text field with previous value.
        parent.setSelectedItem(prevSelection);
      }
    }
    else{
      //Null date.
      parent.setSelectedItem(null);
    }
  }
  
  
}