package entities;

import java.util.ArrayList;

/**
 * Cette classe représente une ville.
 * @author max
 *
 */
public class Ville implements graphics.Drawable {

	private int x;
	private int y;
	private int habitants;
	private int habitants_infectes;

	private ArrayList<StockVaccin> stocksVaccins;
	private ArrayList<StockTraitement> stocksTraitements;
	
	public Ville(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public static int distance(Ville depart, Ville arrivee) {
		return (int) Math.sqrt((arrivee.x - depart.x) * (arrivee.x - depart.x) + (arrivee.y - depart.y) * (arrivee.y - depart.y));
	}

	@Override
	public void draw(int x, int y, int height, int width) {
		// TODO Auto-generated method stub
		
	}

	public void ajouteStockVaccin(Vaccin vaccin, int quantite) {
		boolean ajoute = false;
		
		for (StockVaccin sv : stocksVaccins) {
			if (sv.getVaccin() == vaccin) {
				sv.ajouteStock(quantite);
				ajoute = true;
				break;
			}
		}
		if (!ajoute) {
			stocksVaccins.add(new StockVaccin(quantite, vaccin));
		}
	}
	
	public void ajouteStockTraitement(Traitement traitement, int quantite) {
		boolean ajoute = false;
		
		for (StockTraitement st : stocksTraitements) {
			if (st.getTraitement() == traitement) {
				st.ajouteStock(quantite);
				ajoute = true;
				break;
			}
		}
		if (!ajoute) {
			stocksTraitements.add(new StockTraitement(quantite, traitement));
		}
	}
	
	public int getHabitants() {
		return habitants;
	}

	public void setHabitants(int habitants) {
		this.habitants = habitants;
	}

	public int getHabitants_infectes() {
		return habitants_infectes;
	}

	public void setHabitants_infectes(int habitantsInfectes) {
		habitants_infectes = habitantsInfectes;
	}

	public int getPourcentageInfectes() {
		return (int)(100 * ((float)habitants_infectes / habitants));
	}
	
}
