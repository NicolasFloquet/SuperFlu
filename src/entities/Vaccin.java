package entities;

import java.io.Serializable;

/**
 * Cette classe représente un type de Vaccin.
 *
 */
public class Vaccin implements Serializable{
	private Virus virus;
	
	public Vaccin(Virus virus) {
		this.virus = virus;
	}
	
	public Virus getVirus() {
		return virus;
	}
}
