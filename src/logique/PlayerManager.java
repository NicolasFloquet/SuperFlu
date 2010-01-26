package logique;

import org.lwjgl.input.Mouse;

import entities.*;
import graphics.ScreenManager;

public class PlayerManager {

	private static PlayerManager instance = new PlayerManager();
	
	private Ville selected;
	
	private PlayerManager() {
		selected = null;
	}

	public void update() {
		if(Mouse.isButtonDown(0)) {
			if(selected == null) {
				selected = getTargetedVille(); 
			}
		}
		else {
			if(selected != null) {
				// TODO ajouter un transfert
			}
			else {
				selected = null;
			}
		}
	}
	
	public static PlayerManager getInstance() {
		return instance;
	}

	public Ville getTargetedVille()
	{
		for(Zone  z : Application.getInstance().getGame().getCarte().getZones()) {
			for(Ville v : z.getVilles()) {
				if(v.isOnCity(Mouse.getX()-ScreenManager.getInstance().getOrigineCarteX(),
								Mouse.getY()-ScreenManager.getInstance().getOrigineCarteY())) {
					return v;
				}
			}
		}
		return null;
	}

}
