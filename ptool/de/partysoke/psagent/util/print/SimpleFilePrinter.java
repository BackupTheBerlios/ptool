/*
 * Created on 18.01.2005
 * by Enrico Tröger
*/


package de.partysoke.psagent.util.print;

import java.awt.*;
import java.awt.print.*;
import java.io.*;

import de.partysoke.psagent.*;

public class SimpleFilePrinter implements Printable
{
	//---Konstanten--------------------------------------
	private static final int RESMUL = 4;

	//---Membervariablen---------------------------------
	private PrinterJob       pjob;
	private PageFormat       pageformat;
	private FilePrintHelper  fph;
	private String           fname;
	private String heading = "Events von PartySOKe.de (" + Define.getOwnName() +
  							" " + Define.getVersionAsString();
	private RandomAccessFile in;

	//---Konstruktoren-----------------------------------
	public SimpleFilePrinter(String fname)
	{
	    this.pjob  = PrinterJob.getPrinterJob();
	    this.fname = fname;
	}

	public boolean setupJobOptions()
	{
	    boolean result = pjob.printDialog();
	    
	    this.pageformat = pjob.defaultPage();
	    this.pageformat.setOrientation(PageFormat.PORTRAIT);
	    Paper paper = new Paper();
	    paper.setSize(595.275590551181, 841.8897637795276); // DIN A4
	    paper.setImageableArea(40, 40, 555.27559, 801.88976);
	    this.pageformat.setPaper(paper);
	    pjob.setPrintable(this, this.pageformat);
	    pjob.setJobName(Define.getOwnName() + " - Druckjob");
	    return result;
	}

	public void printFile()
	throws PrinterException, IOException
	{
	    fph = new FilePrintHelper();
	    in = new RandomAccessFile(fname, "r");
	    pjob.print();
	    in.close();
	}

	//---Implementierung von Printable-------------------
	public int print(Graphics g, PageFormat pf, int page)
	throws PrinterException
	{
	    int ret = PAGE_EXISTS;
	    String line = null;
	    try {
	        if (fph.knownPage(page)) {
	            in.seek(fph.getFileOffset(page));
	            line = in.readLine();
	        } else {
	            long offset = in.getFilePointer();
	            line = in.readLine();
	            if (line == null) {
	                ret = NO_SUCH_PAGE;
	            } else {
	                fph.createPage(page);
	                fph.setFileOffset(page, offset);
	            }
	        }
	        if (ret == PAGE_EXISTS) {
	            //Seite ausgeben, Grafikkontext vorbereiten
	            Graphics2D g2 = (Graphics2D)g; 
	            g2.scale(1.0 / RESMUL, 1.0 / RESMUL);
	            int ypos = (int)pf.getImageableY() * RESMUL;
	            int xpos = ((int)pf.getImageableX() + 2) * RESMUL;
	            int yd = 12 * RESMUL;
	            int ymax = ypos + (int)pf.getImageableHeight() * RESMUL - yd;
	            //Seitentitel ausgeben
	            ypos += yd; 
	            g2.setColor(Color.black);
	            g2.setFont(new Font("Monospaced", Font.BOLD, 10 * RESMUL));
	            g.drawString(heading + ", Seite " + (page + 1) + ")", xpos, ypos);
	            g.drawLine(
	                    xpos, 
	                    ypos + 6 * RESMUL, 
	                    xpos + (int)pf.getImageableWidth() * RESMUL, 
	                    ypos + 6 * RESMUL
	            );
	            ypos += 2 * yd;
	            //Zeilen ausgeben
	            //g2.setColor(new Color(0, 0, 127)); 
	            g2.setFont(new Font("Monospaced", Font.PLAIN, 9 * RESMUL));
	            while (line != null) {
	                g.drawString(line, xpos, ypos);
	        	    ypos += yd;
	                if (ypos >= ymax) {
	                    break;
	                }
	                line = in.readLine();
	            }
	        }
	    } catch (IOException e) {
	        throw new PrinterException(e.toString());
	    }
	    return ret;
	}

}