package entities;

import java.util.ArrayList;

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
