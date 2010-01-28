package entities;

import java.io.Serializable;

/**
 * Cette classe représente un stock de traitements.
 *
 */
public class StockTraitement extends Stock implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Traitement traitement;
	
	public StockTraitement(int stock, Traitement traitement) {
		super(stock);
		this.traitement = traitement;
	}
	
	public Traitement getTraitement() {
		return traitement;
	}
	
	public void setTraitement(Traitement t) {
		this.traitement = t;
	}
}
