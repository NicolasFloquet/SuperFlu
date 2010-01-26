package entities;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Cette classe répresente une zone. Cette zone contient une liste de villes.
 *
 */
public class Zone {
	private int id;
	private String nom;
	private Joueur joueur;
	private ArrayList<Ville> villes = new ArrayList<Ville>();
	private Usine usine;
	
	public Zone(int id) {
		this.id = id;
		chargeVilles();
	}
	
	private void chargeVilles() {
		String filepath = "zone" + id + ".data";
		
		try {
			BufferedReader buff = new BufferedReader(new FileReader(filepath));
			String line;
			
			nom = line = buff.readLine();
			
			while ((line = buff.readLine()) != null) {
				String tab[] = line.split(" ");
				
				if (tab.length == 3) {
					villes.add(new Ville(tab[0], Integer.valueOf(tab[1]), Integer.valueOf(tab[2])));
				} else {
					System.err.println("Erreur lecture " + filepath + "continue quand même...");
				}
			}
		} catch (FileNotFoundException e) {
			System.err.println("Fichier " + filepath + " introuvable ! Aucune ville chargée pour cette zone.");
		} catch (IOException e) {
			System.err.println("Erreur à la lecture de " + filepath + ".");
		}
	}
	
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
