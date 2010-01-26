package entities;

import logique.GameLogic;
import graphics.ScreenManager;
import graphics.Sprite;

/**
 * Cette classe représente un transfert de traitements ou vaccins.
 *
 */
public class Transfert implements graphics.Drawable {
	
	private final static float VITESSE = 0.1f;
	
	private final Ville depart;
	private final Ville arrivee;
	private final long temps_depart; // ms
	private final long temps_arrivee; // ms
	
	private final Stock stock;
	
	public Transfert(Ville depart, Ville arrivee, Stock stock, long temps_depart) {
		this.depart = depart;
		this.arrivee = arrivee;
		this.temps_depart = temps_depart;
		this.temps_arrivee = temps_depart + (long)(VITESSE * Ville.distance(depart, arrivee));
		this.stock = stock;
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
		double angle = Math.atan2(arrivee.getY() - depart.getY(), arrivee.getX() - depart.getY());
		double avancement = (GameLogic.getInstance().getTime() - temps_depart) / (temps_arrivee - temps_depart);
		sprite.draw((int)(depart.getX()*(1 - avancement) + arrivee.getX()*avancement) + ScreenManager.getInstance().getOrigineCarteX(), (int)(depart.getY()*(1 - avancement) + arrivee.getY()*avancement)  + ScreenManager.getInstance().getOrigineCarteY(), (float)angle);
	}
}
