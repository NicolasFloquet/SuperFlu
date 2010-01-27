package entities;

import java.io.Serializable;

/**
 * Cette classe représente un stock de vaccin.
 *
 */
public class StockVaccin extends Stock implements Serializable {

	private Vaccin vaccin;
	
	public StockVaccin(int stock, Vaccin vaccin) {
		this.stock = stock;
		this.vaccin = vaccin;
	}

	public Vaccin getVaccin() {
		return vaccin;
	}
}
