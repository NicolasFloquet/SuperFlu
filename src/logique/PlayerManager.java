package logique;

import org.lwjgl.input.Mouse;

import entities.*;
import graphics.ScreenManager;

public class PlayerManager {

	private static PlayerManager instance = new PlayerManager();
	
	private Ville selected;
	private int pourcentage; //le pourcentage à envoyer lors du transfert (se change à la molette)
	
	private PlayerManager() {
		selected = null;
		pourcentage = 50;
	}

	public void update() {
		if(Mouse.isButtonDown(0)) {
			if(selected == null) {
				selected = getTargetedVille(); 
			}
			
			pourcentage += Mouse.getDWheel()/15;
			
			if(pourcentage > 100) {
				pourcentage = 100;
			}
			else if(pourcentage < 0) {
				pourcentage = 0;
			}
				
		}
		else {
			if(selected != null) {
				// TODO vraiment gerer les transferts
				Ville released = getTargetedVille();
				if(released != null) {
					/* TODO La on envoi que des stocks de traitement, il faut aussi gerer les vaccins
					        + Existance de differents traitement (pour un itération future */
					if (!selected.getStocksTraitements().isEmpty()) {
						Stock nouveau_stock = new StockTraitement(selected.getStocksTraitements().get(0).getStock()*pourcentage/100,
																  selected.getStocksTraitements().get(0).getTraitement());
						Application.getInstance().getGame().creerTransfert(selected,released,nouveau_stock);
					}
				}
				selected = null;
			}
			else {
				selected = null;
			}
		}
		ScreenManager.getInstance().setSelected(selected);
	}
	
	public static PlayerManager getInstance() {
		return instance;
	}

	public Ville getTargetedVille()
	{
		for(Zone  z : Application.getInstance().getGame().getCarte().getZones()) {
			for(Ville v : z.getVilles()) {
				if(v.isOnCity(Mouse.getX()-ScreenManager.getInstance().getOrigineCarteX(),
								ScreenManager.getInstance().getMap().getHeight() - (Mouse.getY()-ScreenManager.getInstance().getOrigineCarteY()))) {
					return v;
				}
			}
		}
		return null;
	}
	
	public int getPourcentageVoulu() {
		return pourcentage;
	}

}
