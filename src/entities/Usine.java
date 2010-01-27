package entities;

import graphics.ScreenManager;
import graphics.Sprite;

import java.io.Serializable;
import java.util.ArrayList;

import logique.PlayerManager;

/**
 * Cette classe représente une usine. C'est une ville qui possède la propriété de pouvoir produire des vaccins et traitements.
 *
 */
public class Usine extends Ville implements Serializable{

	private ArrayList<Traitement> traitements = new ArrayList<Traitement>();
	private ArrayList<Vaccin> vaccins = new ArrayList<Vaccin>();
	private int productionRateVaccins = 100;
	private int productionRateTraitements = 1000;
	
	public Usine(Zone zone, String nom, int x, int y) {
		super(zone, nom, x, y);
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
	
	public void ajouteVaccin(Vaccin vaccin) {
		vaccins.add(vaccin);
	}
	
	public void ajouteTraitement(Traitement traitement) {
		traitements.add(traitement);
	}
	
	@Override
	public void draw() {
		Sprite usine = ScreenManager.getSprite("usine.png");
		Sprite hl_usine = ScreenManager.getSprite("HL_usine.png");
		
		if(PlayerManager.getInstance().getTargetedVille() == this)
			hl_usine.draw(x + ScreenManager.getInstance().getOrigineCarteX(), y + ScreenManager.getInstance().getOrigineCarteY());
		else
			usine.draw(x + ScreenManager.getInstance().getOrigineCarteX(), y + ScreenManager.getInstance().getOrigineCarteY());
	}
}

