package entities;

import java.io.Serializable;

/**
 * Cette classe représente un virus et contient ses caractéristiques.
 * @author max
 *
 */
public class Virus implements Serializable{
	private String nom;
	
	public Virus(String nom) {
		this.nom = nom;
	}
	
	public String getNom() {
		return nom;
	}
}
