package entities;

import java.io.Serializable;

/**
 * Classe abstraite qui représente un stock. Cette classe est étendue par StockTraitement et StockVaccin pour être spécialisé.
 * 
 */
public abstract class Stock implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int capacite_max = 20000;
	private int stock = 0;

	public Stock(int stock, int capacite_max) {
		this.capacite_max = capacite_max;
		this.stock = Math.min(capacite_max, stock);
	}

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
	
	public void retireStock(int valeur) {	
		if (stock-valeur < 0) {
			stock = 0;
		} else {
			stock = stock - valeur;
		}
	}
}
