package entities;

import java.io.Serializable;

/**
 * Cette classe représente un stock de vaccin.
 *
 */
public class StockVaccin extends Stock implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Vaccin vaccin;
	
	public StockVaccin(int stock, Vaccin vaccin) {
		super(stock);
		capacite_max = 500000;
		this.vaccin = vaccin;
	}

	public Vaccin getVaccin() {
		return vaccin;
	}
	
	public void setVaccin(Vaccin v) {
		this.vaccin = v;
	}
}
