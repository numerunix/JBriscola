package org.numerone.altervista.jbriscola;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class BriscoPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5278549526708739259L;
	private Giocatore Utente, Computer;
	private Font f;
	private FontMetrics fm;
	private BufferedImage img;
	private Mazzo mazzo;
	private	Point p1, p2;
	private ElaboratoreCarteBriscola helper;
	private int spaziatura, spaziaturay;
	ResourceBundle Bundle;
	public BriscoPanel(Giocatore u, Giocatore c, Mazzo m, ElaboratoreCarteBriscola b, boolean primaUtente, Font font, ResourceBundle bundle) throws FileNotFoundException, IOException {
		if (System.getProperty("os.name").contains("Windows"))
			spaziaturay=60;
		else
			spaziaturay=100;
		Bundle=bundle;
		Utente=u;
		Computer=c;
		f=font;
		fm=null;
		mazzo=m;
		helper=b;
		spaziatura=10;
		CaricaImmagine();
		if (!primaUtente)
			Computer.Gioca(0);
	}
	
	public void SetUtente(Giocatore u) {
		Utente=u;
	}
	
	public void SetComputer(Giocatore c) {
		Computer = c;
	}
	
	public void SetMazzo(Mazzo m) {
		mazzo=m;
	}
	
	public void SetElaboratoreCarteBriscola(ElaboratoreCarteBriscola b) {
		helper=b;
	}
	
	public void CaricaImmagine() throws FileNotFoundException, IOException  {
		String s=Carta.GetPathCarte()+"retro carte mazzo.jpg";
		File f=new File(s);
		if (!f.exists())
			throw new FileNotFoundException(Bundle.getString("theFile")+s+Bundle.getString("doesntExists"));
		else
			img=ImageIO.read(f);
	}
	
	public int controllaCoordinate(int x, int y) {
		int starty=spaziaturay+fm.getHeight(), stopy=starty+img.getWidth(), temp;
		if (starty<=y && y<=stopy) {
			temp=img.getHeight()+spaziatura;
			temp=x/temp;
			return temp;
		}
		return -1;

	}
	
	public void setFont(Font font) {
		f=font;
		repaint();
	}
	
	public  Point getDimensioni() {
		Point p=Computer.Paint(this, null, fm, spaziatura);
	    String stringaCarte=Bundle.getString("inDeckRemains")+mazzo.GetNumeroCarteStr()+Bundle.getString("cards");
	    p.x=p.x+fm.stringWidth(stringaCarte)+10+img.getWidth();
	    return p;
	}
	
	public void paintComponent(Graphics gr) {
		if (fm==null)
			fm=gr.getFontMetrics();
		Graphics2D g = (Graphics2D)gr;
		g.setFont(f);
	    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    String stringaCarte=Bundle.getString("inDeckRemains")+mazzo.GetNumeroCarteStr()+Bundle.getString("cards");
		p1=Utente.Paint(this, g, fm, spaziatura);
		p2=Computer.Paint(this, g, fm, spaziatura);
		p2.x = p1.x + fm.stringWidth(stringaCarte)+10;
		g.drawString(Bundle.getString("pointsOf")+Utente.GetNome()+": "+Utente.GetPunteggioStr(), p1.x, p1.y);
		p1.y=p1.y+fm.getHeight()+spaziatura;
		g.drawString(Bundle.getString("pointsOf")+Computer.GetNome()+": "+Computer.GetPunteggioStr(), p1.x, p1.y);
		p1.y=p1.y+fm.getHeight()+spaziatura;
		g.drawString(stringaCarte, p1.x, p1.y);
		p1.y=p1.y+fm.getHeight()+spaziatura;
		g.drawString(Bundle.getString("trumpSuit")+Bundle.getString(Carta.GetSemeStr(helper.GetCartaBriscola())), p1.x, p1.y);
		if (mazzo.GetNumeroCarte()>0) {
			p1.x=p1.x+spaziatura*3;
			p1.y=p1.y+fm.getHeight()+spaziatura;
			g.drawImage(Carta.GetImmagine(helper.GetCartaBriscola()), p1.x, p1.y, this);
			g.drawImage(img, p1.x-img.getHeight()/2+spaziatura, p1.y+img.getWidth()-img.getHeight(), this);
		}
	}	
}
