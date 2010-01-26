package entities;

import java.util.ArrayList;

/**
 * Cette classe répresente une zone. Cette zone contient une liste de villes.
 *
 */
public class Zone {
	private Joueur joueur;
	private ArrayList<Ville> villes = new ArrayList<Ville>();
	private Usine usine;
	
	public void setJoueur(Joueur joueur) {
		this.joueur = joueur;
	}
	
	public Usine getUsine() {
		return usine;
	}
	
	public Joueur getJoueur() {
		return joueur;
	}
	
	public ArrayList<Ville> getVilles() {
		return villes;
	}
}
