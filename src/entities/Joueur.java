package entities;

import graphics.ScreenManager;
import graphics.Texte;

import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import logique.Application;

/**
 * Classe qui contient les informations relatives à un joueur (score, zone)
 *
 */
public class Joueur implements graphics.Drawable, Serializable,Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Zone> zone = new ArrayList<Zone>();
	private int score;
	private Socket socket;
	
	public void setZone(List<Zone> zone) {
		this.zone = zone;
	}

	public List<Zone> getZone() {
		return zone;
	}
	
	public int getScore() {
		return score;
	}
	
	@Override
	public void draw() {
		int encart_pos_y = ScreenManager.getInstance().getOrigineEncartY() + 15;
		int milieu_ecran = ScreenManager.getInstance().getScreenWidth()/2;
		Joueur joueur = Application.getInstance().getJoueur();
		
		 int offset = 10;
		 for(Zone z : joueur.getZone()) {
			new Texte(z.getNom() + ":").draw(milieu_ecran, encart_pos_y + offset, 0.5f, 0.0f, 0.0f, 0.0f);
			new Texte("Habitants ").draw(milieu_ecran+50, encart_pos_y + offset + 10, 0.5f, 0.0f, 0.0f, 0.0f);
			new Texte("Habitants infectes ").draw(milieu_ecran+50, encart_pos_y + offset + 20, 0.5f, 0.0f, 0.0f, 0.0f);
			new Texte("Habitants morts ").draw(milieu_ecran+50, encart_pos_y + offset  + 30, 0.5f, 0.0f, 0.0f, 0.0f);
		 	offset+=10;
		 }


	}
	
	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	public Joueur clone(){
		try {
			return (Joueur) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
