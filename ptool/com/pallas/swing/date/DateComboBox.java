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
 * $FileName: DateComboBox.java$
 * $FileID: 8$
 *
 * Last change:
 * $AuthorName: Rob MacGrogan$
 * $Date: 2005/01/31 19:17:56 $
 * $VerID: 80$
 * $Comment: Strip off the time component of date. $
 **************************************************************************/
//////////////////////////////////////////////////////////////
// DateComboBox.java
//////////////////////////////////////////////////////////////

package com.pallas.swing.date;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.plaf.metal.MetalComboBoxUI;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.EmptyBorder;

import com.sun.java.swing.plaf.motif.MotifComboBoxUI;
import com.sun.java.swing.plaf.windows.WindowsComboBoxUI;

import de.partysoke.psagent.util.*;

//////////////////////////////////////////////////////////////

/**
 * Title:   $FileName: DateComboBox.java$
 * @version $VerNum: 10$
 * @author $AuthorName: Rob MacGrogan$<br><br>
 * 
 * $Description: A date control that pops up a calendar.$<br>
 * $KeyWordsOff: $<br><br>
 * 
 * 
 * A date control that pops up a calendar. Derived from code posted at
 * http://softwaredev.earthweb.com/java/article/0,,12082_735291,00.html by
 * Paul Book.<br><br>
 * 
 * Works under java 1.3 and 1.4.1. A bug in 1.4.0 causes very odd behavior
 * under Windows LAF, but should work fine under other LAFs.
 */
public class DateComboBox extends JComboBox {

  //Used only for display.
  protected SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy");

  //Used for formatting what user types.
  private ArrayList formats = new ArrayList();

  /**
   * Defaults to null. And MMM d, yyyy pattern.
   */
  public DateComboBox() {
    super();
    initializeComponent();
  }

  /**
   * Sets initial date of combo box. Defaults pattern to MMM d, yyyy.
   */
  public DateComboBox(java.util.Date date) {
    super();
    initializeComponent();
    setDate(date);
  }

  public DateComboBox(java.util.Date date, String pattern) {
    super();
    initializeComponent();
    setDateFormat(pattern);
    setDate(date);
  }

  public DateComboBox(String pattern) {
    super();
    initializeComponent();
    setDateFormat(pattern);
  }

  public void setName(String s) {
    super.setName(s);
    getEditor().getEditorComponent().setName(s + "-- editor");
  }

  DateFormat getFormat() {
    return dateFormat;
  }

  public void addFormat(DateFormat format){
    formats.add(format);
  }

  public void addFormat(String format){
    formats.add(new SimpleDateFormat(format));
  }

  /**
   * Initializes the DateComboBox.
   */
  private void initializeComponent() {
    super.setEditable(true);

    JTextField field = (JTextField) getEditor().getEditorComponent();
    field.addFocusListener(new DateFocusListener(this));
    //setCalendarIcon();
  }
  
  /**
   * Attempts to parse sDate to a date using all available date formats.
   */
  java.util.Date parseDateString(String sDate)
          throws ParseException{
    java.util.Date value = null;
    //Now try all remaining formats.
    Iterator itr = formats.iterator();
    while(itr.hasNext()){
      DateFormat format = (DateFormat)itr.next();
      try{
        value = format.parse(sDate);
        //if we get here we got a value, so break.
        break;
      }
      catch (ParseException ex2){
        //just don't break.
      }
    }
    if (value == null){
      throw new ParseException("Can't format string " + sDate + " into a date.", 0);
    }
    return value;
  }

  private void setCalendarIcon() {
    Component[] components = getComponents();
    for (int i = 0; i < components.length; i++) {
      if (components[i] instanceof JButton) {
        JButton btn = (JButton) components[i];
        ActionListener[] listeners = btn.getActionListeners();
        JButton calButton = new JButton(getButtonIcon());
        for (int j = 0; j < listeners.length; j++) {
          calButton.addActionListener(listeners[i]);
        }
        remove(btn);
        add(calButton);
        //btn.setIcon(getButtonIcon());
        break;
      }
    }

  }

  private Icon getButtonIcon() {
    java.net.URL imageURL = DateComboBox.class.getResource("calendar.gif");
    Image img = Toolkit.getDefaultToolkit().getImage(imageURL);
    Icon ic = new ImageIcon(img);
    return ic;
  }

  /**
   * This method does nothing. DateComboBox is always editable.
   */
  public void setEditable(boolean editable) {
    //Do nothing.
  }

  public void setDateFormat(SimpleDateFormat dateFormat) {
    this.dateFormat = dateFormat;
  }

  public void setDateFormat(String pattern) {
    dateFormat = new SimpleDateFormat(pattern);
  }

  /**
   * Returns a new Date with the same calendar date as the Date passed in, 
   * but with the time changed to 12:00 AM.
   */
  public static java.util.Date stripTime(java.util.Date dt){
    java.util.Date dtStripped = null;
    Calendar cal = Calendar.getInstance();
    cal.setTime(dt);
    int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MONTH);
    int day = cal.get(Calendar.DAY_OF_MONTH);
    
    Calendar calStripped = Calendar.getInstance();
    calStripped.clear();
    calStripped.set(year, month, day, 23, 59, 59);
    
    return calStripped.getTime(); 
  }


  public void setDate(java.util.Date date) {
    if (date != null) {
      date = stripTime(date);
      CBDate cbDate = new CBDate();
      cbDate.setDate(date);
      cbDate.setFormat(dateFormat.toPattern());
      setSelectedItem(cbDate);
    }
    else {
      setSelectedItem(null);
    }
  }

  public java.util.Date getDate() {
    java.util.Date date = null;
    try {
      Object o = getSelectedItem();
      if (o instanceof CBDate) {
        CBDate cbDate = (CBDate) o;
        date = cbDate.getDate();
      }
      else {
          new Logger("The value of the DateComboBox is not a Date.");
          throw new java.text.ParseException("The value of the DateComboBox is not a Date.", 0);
      }
    }
    catch (ParseException ex) {
      date = null;
    }
    return date;
  }

  public void setSelectedItem(Object item) {
    // Could put extra logic here or in renderer when item is instanceof Date, Calendar, or String
    // Dont keep a list ... just the currently selected item
    removeAllItems(); // hides the popup if visible
    addItem(item);
    super.setSelectedItem(item);
  }

  public void updateUI() {
    ComboBoxUI cui = (ComboBoxUI) UIManager.getUI(this);
    if (cui instanceof MetalComboBoxUI) {
      cui = new MetalDateComboBoxUI();
    }
    else if (cui instanceof MotifComboBoxUI) {
      cui = new MotifDateComboBoxUI();
    }
    else if (cui instanceof WindowsComboBoxUI) {
      cui = new WindowsDateComboBoxUI();
    }
    setUI(cui);
    //super.updateUI();
    initializeComponent();
  }

  // Inner classes are used purely to keep DateComboBox component in one file
  //////////////////////////////////////////////////////////////
  // UI Inner classes -- one for each supported Look and Feel
  //////////////////////////////////////////////////////////////

  class MetalDateComboBoxUI extends MetalComboBoxUI {
    protected ComboPopup createPopup() {
      return new DatePopup((DateComboBox) comboBox);
    }
  }

  class WindowsDateComboBoxUI extends WindowsComboBoxUI {
    protected ComboPopup createPopup() {
      return new DatePopup((DateComboBox) comboBox);
    }
  }

  class MotifDateComboBoxUI extends MotifComboBoxUI {
    protected ComboPopup createPopup() {
      return new DatePopup((DateComboBox) comboBox);
    }
  }

  class CBDate {
    private java.util.Date date = null;
    private SimpleDateFormat format = null;

    public java.util.Date getDate() {
      return date;
    }
    public void setDate(java.util.Date dt) {
      date = dt;
    }
    public void setFormat(String s) {
      format = new SimpleDateFormat(s);
    }

    public String toString() {
      return format.format(date);
    }
  }

  //////////////////////////////////////////////////////////////
  // DatePopup inner class
  //////////////////////////////////////////////////////////////

  class DatePopup implements ComboPopup, MouseMotionListener, MouseListener, KeyListener, PopupMenuListener {

    protected DateComboBox comboBox;
    protected Calendar calendar;
    protected JPopupMenu popup;
    protected JLabel monthLabel;
    protected JPanel days = null;
    protected SimpleDateFormat monthFormat = new SimpleDateFormat("MMM yyyy");

    protected Color selectedBackground;
    protected Color selectedForeground;
    protected Color background;
    protected Color foreground;

    public DatePopup(DateComboBox comboBox) {
      this.comboBox = comboBox;
      calendar = Calendar.getInstance();
      // check Look and Feel
      background = UIManager.getColor("Panel.background");
      foreground = UIManager.getColor("Panel.foreground");
      selectedBackground = UIManager.getColor("ComboBox.selectionBackground");
      selectedForeground = UIManager.getColor("ComboBox.selectionForeground");

      initializePopup();
    }

    //========================================
    // begin ComboPopup method implementations
    //
    public void show() {
      try {
        // if setSelectedItem() was called with a valid date, adjust the calendar
        java.util.Date dt = comboBox.getDate();
        calendar.setTime(dt);
      }
      catch (Exception e) {
      }
      updatePopup();
      popup.show(comboBox, 0, comboBox.getHeight());
    }

    public void hide() {
      popup.setVisible(false);
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

    public boolean isVisible() {
      return popup.isVisible();
    }

    public void uninstallingUI() {
      popup.removePopupMenuListener(this);
    }

    //
    // end ComboPopup method implementations
    //======================================

    //===================================================================
    // begin Event Listeners
    //

    // MouseListener

    public void mousePressed(MouseEvent e) {
    }
    public void mouseReleased(MouseEvent e) {
    }
    // something else registered for MousePressed
    public void mouseClicked(MouseEvent e) {
      if (!SwingUtilities.isLeftMouseButton(e))
        return;
      if (!comboBox.isEnabled())
        return;
      if (comboBox.isEditable()) {
        //comboBox.getEditor().getEditorComponent().requestFocus();
      }
      else {
        //comboBox.requestFocus();
      }
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
    public void mouseDragged(MouseEvent e) {
    }
    public void mouseMoved(MouseEvent e) {
    }

    // KeyListener
    public void keyPressed(KeyEvent e) {
    }
    public void keyTyped(KeyEvent e) {
    }
    public void keyReleased(KeyEvent e) {
      if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER) {
        togglePopup();
      }
    }

    /**
     * Variables hideNext and mouseInside are used to
     * hide the popupMenu by clicking the mouse in the JComboBox
     */
    public void popupMenuCanceled(PopupMenuEvent e) {
    }
    protected boolean hideNext = false;
    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
      hideNext = mouseInside;
    }
    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
    }

    //
    // end Event Listeners
    //=================================================================

    //===================================================================
    // begin Utility methods
    //

    protected void togglePopup() {
      if (isVisible() || hideNext) {
        hide();
      }
      else {
        show();
      }
      hideNext = false;
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

      popup = new JPopupMenu();
      popup.setBorder(BorderFactory.createLineBorder(Color.black));
      popup.setLayout(new BorderLayout());
      popup.setBackground(background);
      popup.addPopupMenuListener(this);
      popup.add(BorderLayout.NORTH, header);
    }

    // update the Popup when either the month or the year of the calendar has been changed
    protected void updatePopup() {
      monthLabel.setText(monthFormat.format(calendar.getTime()));
      if (days != null) {
        popup.remove(days);
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
        }
        else if (dayInt == Calendar.MONDAY) {
          label.setText("Mon");
        }
        else if (dayInt == Calendar.TUESDAY) {
          label.setText("Tue");
        }
        else if (dayInt == Calendar.WEDNESDAY) {
          label.setText("Wed");
        }
        else if (dayInt == Calendar.THURSDAY) {
          label.setText("Thu");
        }
        else if (dayInt == Calendar.FRIDAY) {
          label.setText("Fri");
        }
        else if (dayInt == Calendar.SATURDAY) {
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
          public void mousePressed(MouseEvent e) {
          }
          public void mouseClicked(MouseEvent e) {
          }
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

      popup.add(BorderLayout.CENTER, days);
      popup.pack();
    }
  }

  //////////////////////////////////////////////////////////////
  // This is only included to provide a sample GUI
  //////////////////////////////////////////////////////////////
  public static void main(String args[]) {
    JFrame f = new JFrame();
    Container c = f.getContentPane();
    c.setLayout(new FlowLayout());
    c.add(new JLabel("Date 1:"));
    DateComboBox box = new DateComboBox();
    box.setName("One");
    c.add(box);
    c.add(new JLabel("Date 2:"));
    DateComboBox dcb = new DateComboBox();
    dcb.setName("Two");
    c.add(dcb);
    f.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });

    try {
      String sLookAndFeel = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
      UIManager.setLookAndFeel(sLookAndFeel);
      SwingUtilities.updateComponentTreeUI(f);
    }
    catch (Exception ex) {
    }

    f.setSize(500, 200);
    f.show();
  }

}
