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
 * $FileName: PDate.java$
 * $FileID: 12$
 *
 * Last change:
 * $AuthorName: Rob MacGrogan$
 * $Date: 2005/01/31 19:17:56 $
 * $VerID: 45$
 * $Comment: Added GLPL license text.$
 **************************************************************************/
package com.pallas.swing.date;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;

/**
 * Title:   $FileName: PDate.java$
 * @version $VerNum: 2$
 * @author $AuthorName: Rob MacGrogan$<br><br>
 * 
 * $Description: Date control with popup calendar. Will
 *              supercede DateComboBox someday, but for now, this class does not
 *              work properly.$<br>
 * $KeyWordsOff: $<br><br>
 */
public class PDate extends JComponent {

  private static final String ICON_NAME = "calendar.gif";
  
  private Date dateValue = null;
  private DatePopup popup = null;
  private JTextField textField = null;
  private JButton button = null;
  

  public PDate(){
    initializeComponent();
  }  


  private void initializeComponent(){
    textField = new JTextField();
    popup = new DatePopup(this);
    button = buildButton();    
    
    setLayout(new FlowLayout(FlowLayout.LEFT));

    add(textField);
    add(button);
    setVisible(true);
  }
  
  public Date getDate(){
    return dateValue;
  }
  
  public void setDate(Date date){
    dateValue = date;
  }
  
  
  private JButton buildButton(){
    JButton btn = new JButton(getButtonIcon());
    btn.addActionListener(
        new ActionListener(){
          public void actionPerformed(ActionEvent ev){
            popup.show();
          }
        }
    );
    return btn;
  }
  
  private Icon getButtonIcon(){
    java.net.URL imageURL = PDate.class.getResource(ICON_NAME);
    Image img = Toolkit.getDefaultToolkit().getImage(imageURL);
    Icon ic = new ImageIcon(img);
    return ic;
  }

  public static void main(String[] args){
    try{
      JFrame frame = new JFrame();
      frame.getContentPane().setLayout(new GridLayout(1, 2));
      frame.getContentPane().add(new JLabel("test: "));
      frame.getContentPane().add(new PDate());
      frame.addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent e) {
            System.exit(0);
        }
          });
    
      frame.setSize(500, 200);
      frame.show();
      
    }
    catch (Throwable thr){
      thr.printStackTrace();
    }
  }

}
