package entities;

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
	
	public Transfert(Ville depart, Ville arrivee, long temps_depart) {
		this.depart = depart;
		this.arrivee = arrivee;
		this.temps_depart = temps_depart;
		this.temps_arrivee = temps_depart + (long)(VITESSE * Ville.distance(depart, arrivee));
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

	@Override
	public void draw(int x, int y, int height, int width) {
		// TODO Auto-generated method stub
		
	}
}
