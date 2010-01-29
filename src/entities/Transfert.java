package entities;

import java.io.Serializable;

import logique.Application;
import graphics.ScreenManager;
import graphics.Sprite;

/**
 * Cette classe représente un transfert de traitements ou vaccins.
 *
 */
public class Transfert implements graphics.Drawable,Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final static float VITESSE = 0.8f;

	private final Ville depart;
	private final Ville arrivee;
	private final long temps_depart; // ms
	private final long temps_arrivee; // ms
	private final boolean direct;
	
	private final Stock stock;
	
	public Transfert(Ville depart, Ville arrivee, Stock stock, long temps_depart) {
		this.depart = depart;
		this.arrivee = arrivee;
		this.temps_depart = temps_depart;
		this.stock = stock;
		
		int d1 = Ville.distance_carre_sens1(depart, arrivee);
		int d2 = Ville.distance_carre_sens2(depart, arrivee);
		
		this.direct = d1 <= d2;
		
		if (direct) {
			this.temps_arrivee = temps_depart + (long)(Math.sqrt(d1) / VITESSE);
		} else {
			this.temps_arrivee = temps_depart + (long)(Math.sqrt(d2) / VITESSE);
		}	
	}
	
	public Ville getArrivee() {
		return arrivee;
	}
	
	public Ville getDepart() {
		return depart;
	}
	
	public long getTemps_arrivee() {
		return temps_arrivee;
	}
	
	public long getTemps_depart() {
		return temps_depart;
	}

	public Stock getStock() {
		return stock;
	}
	
	@Override
	public void draw() {
		
		Sprite sprite = ScreenManager.getSprite("avion.png");
		
		int depart_x, arrivee_x;
		if (depart.getX() < arrivee.getX()) {
			depart_x = depart.getX() + (direct ? 0 : 1024);
			arrivee_x = arrivee.getX();
		} else {
			depart_x = depart.getX();
			arrivee_x = arrivee.getX() + (direct ? 0 : 1024);
		}
		
		double angle = (180/Math.PI) * Math.atan2(arrivee.getY() - depart.getY(), arrivee_x - depart_x);
		double avancement = ((double)(Application.getInstance().getGame().getTime() - temps_depart)) / ((double)(temps_arrivee - temps_depart));
		float zoom = 0.25f + 0.75f*(float)Math.sin(avancement*Math.PI);
		

		sprite.draw((int)(depart_x*(1 - avancement) + arrivee_x*avancement) % 1024 + ScreenManager.getInstance().getOrigineCarteX(), (int)(depart.getY()*(1 - avancement) + arrivee.getY()*avancement)  + ScreenManager.getInstance().getOrigineCarteY(), (float)angle, zoom);
	}
}
