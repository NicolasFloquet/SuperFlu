package entities;

/**
 * Cette classe représente un stock de traitements.
 *
 */
public class StockTraitement extends Stock {
	
	private final Traitement traitement;
	
	public StockTraitement(int stock, Traitement traitement) {
		this.stock = stock;
		this.traitement = traitement;
	}
	
	public Traitement getTraitement() {
		return traitement;
	}
	
	public void utiliseTraitement(int n) {
		stock -= n;
	}
}
