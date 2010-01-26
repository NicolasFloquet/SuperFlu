package entities;

/**
 * Classe abstraite qui représente un stock. Cette classe est étendue par StockTraitement et StockVaccin pour être spécialisé.
 * 
 */
public abstract class Stock {
	protected int capacite_max = 5000;
	protected int stock = 0;
	
	public int getStock() {
		return stock;
	}
	
	public int getCapacite_max() {
		return capacite_max;
	}
	
	public int ajouteStock(int valeur) {
		int reste = 0;
		
		if (valeur + stock > capacite_max) {
			reste = (valeur + stock) - capacite_max;
			stock = capacite_max;
		} else {
			reste = 0;
			stock = stock + valeur;
		}
		
		return reste;
	}
}
