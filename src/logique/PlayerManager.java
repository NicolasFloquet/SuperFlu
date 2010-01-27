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
				// TODO vraiment gerer les transferts
				Ville released = getTargetedVille();
				if(released != null) {
					Application.getInstance().getGame().creerTransfert(selected,released,null);
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

}
