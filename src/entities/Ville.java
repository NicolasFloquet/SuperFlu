package entities;

import java.util.ArrayList;
import java.util.Random;

/**
 * Cette classe représente une ville.
 * @author max
 *
 */
public class Ville implements graphics.Drawable {

	private int x;
	private int y;
	private int habitantsSains;
	private int habitantsInfectes;
	private int habitantsImmunises;

	private ArrayList<StockVaccin> stocksVaccins;
	private ArrayList<StockTraitement> stocksTraitements;
	
	public Ville(int x, int y, int habitants_infectes, int habitants_immunises) {
		Random r = new Random();
		
		this.x = x;
		this.y = y;
		this.habitantsSains = 1000000 + r.nextInt(500000) - 250000;
		this.habitantsInfectes = habitants_infectes;
		this.habitantsImmunises = habitants_immunises;
	}
	
	public static int distance(Ville depart, Ville arrivee) {
		return (int) Math.sqrt((arrivee.x - depart.x) * (arrivee.x - depart.x) + (arrivee.y - depart.y) * (arrivee.y - depart.y));
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
		return habitantsSains + habitantsImmunises + habitantsInfectes;
	}
	
	public int getHabitantsSains() {
		return habitantsSains;
	}
	
	public int getHabitantsImmunises() {
		return habitantsImmunises;
	}

	public int getHabitantsInfectes() {
		return habitantsInfectes;
	}

	public void ajouteHabitantsInfectes(int habitantsInfectes) {
		this.habitantsInfectes += habitantsInfectes;
	}

	public void ajouteHabitantsImmunises(int habitantsImmunises) {
		this.habitantsImmunises += habitantsImmunises;
	}

	public void ajouteHabitantsSains(int habitantsSains) {
		this.habitantsSains += habitantsSains;
	}
	
	public void retireHabitantsInfectes(int habitantsInfectes) {
		this.habitantsInfectes -= habitantsInfectes;
	}

	public void retireHabitantsImmunises(int habitantsImmunises) {
		this.habitantsImmunises -= habitantsImmunises;
	}

	public void retireHabitantsSains(int habitantsSains) {
		this.habitantsSains -= habitantsSains;
	}
	
	public int getPourcentageInfectes() {
		return (int)(100 * ((float)habitantsInfectes / getHabitants()));
	}
	
	/**
	 * Mise à jour des données de la ville.
	 */
	public void update() {
		// Infection :
		
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	@Override
	public void draw(int x, int y, int height, int width) {
		// TODO Auto-generated method stub
		
	}
	
}
