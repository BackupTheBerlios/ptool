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
 * $FileName: DatePopup.java$
 * $FileID: 10$
 *
 * Last change:
 * $AuthorName: Rob MacGrogan$
 * $Date: 2005/01/31 19:17:56 $
 * $VerID: 44$
 * $Comment: Added GLPL license text.$
 **************************************************************************/
package com.pallas.swing.date;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.event.PopupMenuListener;


/**
 * Title:   $FileName: DatePopup.java$
 * @version $VerNum: 3$
 * @author $AuthorName: Rob MacGrogan$<br><br>
 * 
 * $Description: Popup calendar for PDate.$<br>
 * $KeyWordsOff: $<br><br>
 */
  class DatePopup implements MouseMotionListener,
             MouseListener, KeyListener{

  protected PDate comboBox;
  protected Calendar calendar;
  protected Popup popup;
  protected JLabel monthLabel;
  protected JPanel days = null;
  protected JPanel calendarPanel = null;
  protected SimpleDateFormat monthFormat = new SimpleDateFormat("MMM yyyy");

  protected Color selectedBackground;
  protected Color selectedForeground;
  protected Color background;
  protected Color foreground;

  private boolean isVisible = false;

  public DatePopup(PDate comboBox) {
    this.comboBox = comboBox;
    calendar = Calendar.getInstance();
    // check Look and Feel
    background = UIManager.getColor("Panel.background");
    foreground = UIManager.getColor("Panel.foreground");
    selectedBackground = UIManager.getColor("ComboBox.selectionBackground");
    selectedForeground = UIManager.getColor("ComboBox.selectionForeground");

    initializePopup();
  }

  public Popup getPopup(){
    return popup;
  }

  //========================================
  // begin ComboPopup method implementations
  //
  public void show() {
System.out.println("Show");    
    try {
      // if setSelectedItem() was called with a valid date, adjust the calendar
      java.util.Date dt = comboBox.getDate();
      if (dt == null){
        dt = new Date();
      }
      calendar.setTime( dt );
    } 
    catch (Exception e) {}
    updatePopup();
System.out.println("Show");    
    popup.show();
System.out.println("Showing");    
    isVisible = true;
  }

  public void hide() {
      popup.hide();
      isVisible = false;
  }

  protected JList list = new JList();
  public JList getList() {
      return list;
  }

  public MouseListener getMouseListener() {
      return this;
  }

  public MouseMotionListener getMouseMotionListener() {
      return this;
  }

  public KeyListener getKeyListener() {
      return this;
  }
  //
  // end ComboPopup method implementations
  //======================================



  //===================================================================
  // begin Event Listeners
  //

  // MouseListener

  public void mousePressed( MouseEvent e ) {}
        public void mouseReleased( MouseEvent e ) {}
  // something else registered for MousePressed
  public void mouseClicked(MouseEvent e) {
            if ( !SwingUtilities.isLeftMouseButton(e) )
                return;
            if ( !comboBox.isEnabled() )
                return;
      togglePopup();
  }

  protected boolean mouseInside = false;
  public void mouseEntered(MouseEvent e) {
      mouseInside = true;
  }
  public void mouseExited(MouseEvent e) {
      mouseInside = false;
  }

  // MouseMotionListener
  public void mouseDragged(MouseEvent e) {}
  public void mouseMoved(MouseEvent e) {}

  // KeyListener
  public void keyPressed(KeyEvent e) {}
  public void keyTyped(KeyEvent e) {}
  public void keyReleased( KeyEvent e ) {
      if ( e.getKeyCode() == KeyEvent.VK_SPACE ||
     e.getKeyCode() == KeyEvent.VK_ENTER ) {
    togglePopup();
      }
  }

  //
  // end Event Listeners
  //=================================================================

  //===================================================================
  // begin Utility methods
  //

  protected void togglePopup() {
    if ( isVisible ) {
      hide();
    } 
    else {
      show();
    }
  }

  //
  // end Utility methods
  //=================================================================

  // Note *** did not use JButton because Popup closes when pressed
  protected JLabel createUpdateButton(final int field, final int amount) {
      final JLabel label = new JLabel();
      final Border selectedBorder = new EtchedBorder();
      final Border unselectedBorder = new EmptyBorder(selectedBorder.getBorderInsets(new JLabel()));
      label.setBorder(unselectedBorder);
      label.setForeground(foreground);
      label.addMouseListener(new MouseAdapter() {
        public void mouseReleased(MouseEvent e) {
      calendar.add(field, amount);
      updatePopup();
        }
        public void mouseEntered(MouseEvent e) {
      label.setBorder(selectedBorder);
        }
        public void mouseExited(MouseEvent e) {
      label.setBorder(unselectedBorder);
        }
    });
      return label;
  }


  protected void initializePopup() {
      JPanel header = new JPanel(); // used Box, but it wasn't Opaque
      header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
      header.setBackground(background);
      header.setOpaque(true);

      JLabel label;
      label = createUpdateButton(Calendar.YEAR, -1);
      label.setText("<<");
      label.setToolTipText("Previous Year");

      header.add(Box.createHorizontalStrut(12));
      header.add(label);
      header.add(Box.createHorizontalStrut(12));

      label = createUpdateButton(Calendar.MONTH, -1);
      label.setText("<");
      label.setToolTipText("Previous Month");
      header.add(label);

      monthLabel = new JLabel("", JLabel.CENTER);
      monthLabel.setForeground(foreground);
      header.add(Box.createHorizontalGlue());
      header.add(monthLabel);
      header.add(Box.createHorizontalGlue());

      label = createUpdateButton(Calendar.MONTH, 1);
      label.setText(">");
      label.setToolTipText("Next Month");
      header.add(label);

      label = createUpdateButton(Calendar.YEAR, 1);
      label.setText(">>");
      label.setToolTipText("Next Year");

      header.add(Box.createHorizontalStrut(12));
      header.add(label);
      header.add(Box.createHorizontalStrut(12));

      calendarPanel = new JPanel(new BorderLayout());
      calendarPanel.setBorder(BorderFactory.createLineBorder(Color.black));
      calendarPanel.setBackground(background);
      calendarPanel.add(BorderLayout.NORTH, header);
      
      popup = PopupFactory.getSharedInstance().getPopup(comboBox, calendarPanel, comboBox.getX(), calcY());
  }

  private int calcY(){
    int y = comboBox.getY() + comboBox.getHeight();
    return y;
  }


  // update the Popup when either the month or the year of the calendar has been changed
  protected void updatePopup() {
      monthLabel.setText( monthFormat.format(calendar.getTime()) );
      if (days != null) {
        calendarPanel.remove(days);
      }
      days = new JPanel(new GridLayout(0, 7));
      days.setBackground(background);
      days.setOpaque(true);

      Calendar setupCalendar = (Calendar) calendar.clone();
      setupCalendar.set(Calendar.DAY_OF_WEEK, setupCalendar.getFirstDayOfWeek());
      for (int i = 0; i < 7; i++) {
    int dayInt = setupCalendar.get(Calendar.DAY_OF_WEEK);
    JLabel label = new JLabel();
    label.setHorizontalAlignment(JLabel.CENTER);
    label.setForeground(foreground);
    if (dayInt == Calendar.SUNDAY) {
        label.setText("Sun");
    } else if (dayInt == Calendar.MONDAY) {
        label.setText("Mon");
    } else if (dayInt == Calendar.TUESDAY) {
        label.setText("Tue");
    } else if (dayInt == Calendar.WEDNESDAY) {
        label.setText("Wed");
    } else if (dayInt == Calendar.THURSDAY) {
        label.setText("Thu");
    } else if (dayInt == Calendar.FRIDAY) {
        label.setText("Fri");
    } else if (dayInt == Calendar.SATURDAY){
        label.setText("Sat");
    }
    days.add(label);
    setupCalendar.roll(Calendar.DAY_OF_WEEK, true);
      }

      setupCalendar = (Calendar) calendar.clone();
      setupCalendar.set(Calendar.DAY_OF_MONTH, 1);
      int first = setupCalendar.get(Calendar.DAY_OF_WEEK);
      for (int i = 0; i < (first - 1); i++) {
    days.add(new JLabel(""));
      }
      for (int i = 1; i <= setupCalendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
    final int day = i;
    final JLabel label = new JLabel(String.valueOf(day));
    label.setHorizontalAlignment(JLabel.CENTER);
    label.setForeground(foreground);
    label.addMouseListener(new MouseListener() {
      public void mousePressed(MouseEvent e) {}
      public void mouseClicked(MouseEvent e) {}
      public void mouseReleased(MouseEvent e) {
          label.setOpaque(false);
          label.setBackground(background);
          label.setForeground(foreground);
          calendar.set(Calendar.DAY_OF_MONTH, day);
                            comboBox.setDate(calendar.getTime());
          //comboBox.setSelectedItem(dateFormat.format(calendar.getTime()));
          hide();
          // hide is called with setSelectedItem() ... removeAll()
          //comboBox.getEditor().getEditorComponent().requestFocus();
      }
      public void mouseEntered(MouseEvent e) {
          label.setOpaque(true);
          label.setBackground(selectedBackground);
          label.setForeground(selectedForeground);
      }
      public void mouseExited(MouseEvent e) {
          label.setOpaque(false);
          label.setBackground(background);
          label.setForeground(foreground);
      }
        });

    days.add(label);
      }

      calendarPanel.add(days, BorderLayout.CENTER);
  }
    }
