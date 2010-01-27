package entities;

import java.io.Serializable;

/**
 * Cette classe représente un type de Vaccin.
 *
 */
public class Vaccin implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Virus virus;
	
	public Vaccin(Virus virus) {
		this.virus = virus;
	}
	
	public Virus getVirus() {
		return virus;
	}
}
