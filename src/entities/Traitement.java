package entities;

import java.io.Serializable;

/**
 * Cette classe représente un type de traitement. 
 *
 */
public class Traitement implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Virus virus;
	//private float efficacite; // TODO

	public Traitement(Virus virus) {
		this.virus = virus;
	}
	
	public Virus getVirus() {
		return virus;
	}
	
}
