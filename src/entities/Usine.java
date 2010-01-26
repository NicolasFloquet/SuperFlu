package entities;

import java.util.ArrayList;

/**
 * Cette classe représente une usine. C'est une ville qui possède la propriété de pouvoir produire des vaccins et traitements.
 *
 */
public class Usine extends Ville {

	private ArrayList<Traitement> traitements;
	private ArrayList<Vaccin> vaccins;
	private int productionRateVaccins = 100;
	private int productionRateTraitements = 1000;
	
	public Usine(String nom, int x, int y) {
		super(nom, x, y);
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
