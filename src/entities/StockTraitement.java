package entities;

import java.io.Serializable;

/**
 * Cette classe représente un stock de traitements.
 *
 */
public class StockTraitement extends Stock implements Serializable{
	
	private final Traitement traitement;
	
	public StockTraitement(int stock, Traitement traitement) {
		super(stock);
		this.traitement = traitement;
	}
	
	public Traitement getTraitement() {
		return traitement;
	}
}
