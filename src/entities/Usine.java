package entities;

import java.util.ArrayList;

public class Usine extends Ville {

	private ArrayList<Traitement> traitements;
	private ArrayList<Vaccin> vaccins;
	private int productionRateVaccins = 100;
	private int productionRateTraitements = 1000;
	
	public Usine(int x, int y) {
		super(x, y);
	}

	public void produit() {
		for (Traitement traitement : traitements) {
			ajouteStockTraitement(traitement, productionRateTraitements);
		}
		for (Vaccin vaccin : vaccins) {
			ajouteStockVaccin(vaccin, productionRateVaccins);
		}
	}


	public int getProductionRateVaccins() {
		return productionRateVaccins;
	}
	
	public void setProductionRate(int productionRateVaccins) {
		this.productionRateVaccins = productionRateVaccins;
	}
}
