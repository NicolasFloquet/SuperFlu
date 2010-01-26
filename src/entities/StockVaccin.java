package entities;

/**
 * Cette classe représente un stock de vaccin.
 *
 */
public class StockVaccin extends Stock {

	private Vaccin vaccin;
	
	public StockVaccin(int stock, Vaccin vaccin) {
		this.stock = stock;
		this.vaccin = vaccin;
	}

	public Vaccin getVaccin() {
		return vaccin;
	}
}
