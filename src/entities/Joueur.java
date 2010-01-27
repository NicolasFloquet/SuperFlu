package entities;

import java.io.Serializable;
import java.net.Socket;

/**
 * Classe qui contient les informations relatives à un joueur (score, zone)
 *
 */
public class Joueur implements graphics.Drawable, Serializable,Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Zone zone;
	private int score;
	private Socket socket;
	
	public void setZone(Zone zone) {
		this.zone = zone;
	}

	public Zone getZone() {
		return zone;
	}
	
	public int getScore() {
		return score;
	}
	
	@Override
	public void draw() {
		// TODO Auto-generated method stub
		
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
