package entities;

/**
 * Cette classe représente un type de Vaccin.
 *
 */
public class Vaccin {
	private Virus virus;
	
	public Vaccin(Virus virus) {
		this.virus = virus;
	}
	
	public Virus getVirus() {
		return virus;
	}
}
