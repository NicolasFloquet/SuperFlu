package entities;

import graphics.ScreenManager;
import graphics.Texte;

import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


/**
 * Classe qui contient les informations relatives à un joueur (score, zone)
 *
 */
public class Joueur implements graphics.Drawable, Serializable,Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Zone> zones = new ArrayList<Zone>();
	private int score;
	private Socket socket;
	private String pseudo;

	public Joueur(String pseudo) {
		this.pseudo = pseudo;
	}

	public Joueur() {
		this("");
	}

	public void setZone(List<Zone> zone) {
		this.zones = zone;
	}

	public List<Zone> getZones() {
		return zones;
	}

	public int getScore() {
		return score;
	}

	@Override
	public void draw() {
		int encart_pos_y = ScreenManager.getInstance().getOrigineEncartY() + 15;
		int milieu_ecran = ScreenManager.getInstance().getScreenWidth()/2;

		int offsety = 10;
		int offsetx = 0;
		for(Zone z : zones) {
			
			new Texte(z.getNom() + ":").draw(milieu_ecran + offsetx, encart_pos_y + offsety, 1f, 0.0f, 0.0f, 0.0f);
			new Texte("Habitants :"+z.getPopulation()).draw(milieu_ecran+50 + offsetx, encart_pos_y + offsety + 14, 0.8f, 0.0f, 0.0f, 0.0f);
			new Texte("Habitants infectés :"+(int)(100*(float)z.getPopulation_infectee()/z.getPopulation()) + "%").draw(milieu_ecran+50 + offsetx, encart_pos_y + offsety + 28, 0.8f, 0.0f, 0.0f, 0.0f);
			new Texte("Habitants morts :"+(int)(100*(float)z.getPopulation_morte()/z.getPopulation())+ "%" ).draw(milieu_ecran+50 + offsetx, encart_pos_y + offsety  + 42, 0.8f, 0.0f, 0.0f, 0.0f);

			offsety+=60;

			if (offsety >= 160) {
				offsety = 10;
				offsetx += 250;
			}
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

	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

	public String getPseudo() {
		return pseudo;
	}
}
