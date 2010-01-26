package entities;

/**
 * Cette classe représente un type de traitement. 
 *
 */
public class Traitement {
	private Virus virus;
	private float efficacite; // TODO

	public Traitement(Virus virus) {
		this.virus = virus;
	}
	
	public Virus getVirus() {
		return virus;
	}
	
}
