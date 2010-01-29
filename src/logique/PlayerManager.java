package logique;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import entities.*;
import graphics.ScreenManager;

public class PlayerManager {

	private static PlayerManager instance = new PlayerManager();

	private Ville selected;
	private int pourcentage; //le pourcentage à envoyer lors du transfert (se change à la molette)
	private boolean vaccin = true;

	private int selected_menu;	// 0=>start 1=>aide 2=>credits
	private boolean key_lock; // Ceci permet de ne pas interpreter 2x de suite la même touche
	private boolean mouse_lock;
	
	private PlayerManager() {
		selected = null;
		selected_menu = 0;
		pourcentage = 50;
		key_lock = false;
		mouse_lock = false;
	}

	public int getSelectedMenu() {
		return selected_menu;
	}
	
	// Return l'etat selectionné ou 3 si aucun etat n'est selectioné
	public int update_menu() {
		if(!key_lock) {			
			if(Keyboard.isKeyDown(Keyboard.KEY_UP)) {
				selected_menu--;
				
				if(selected_menu<0) {
					selected_menu = 2;
				}
				
				key_lock = true;
			}
			else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
				selected_menu++;
					
				if(selected_menu>2) {
					selected_menu = 0;
				}
				
				key_lock = true;
			}
			else if(Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
				key_lock = true;
				
				return selected_menu;
			}
			else if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
				key_lock = true;
				
				return selected_menu;
			}
			else if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				key_lock = true;
				Application.getInstance().quit();
				return 3;
			}
		}
		else {
			// Si on a locké le clavier on attend que les touches soient relachées 
			key_lock = Keyboard.isKeyDown(Keyboard.KEY_UP)
						|| Keyboard.isKeyDown(Keyboard.KEY_DOWN)
						|| Keyboard.isKeyDown(Keyboard.KEY_RETURN)
						|| Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)
						|| Keyboard.isKeyDown(Keyboard.KEY_SPACE);
		}
		
		if(!mouse_lock) {
			// Si on clique on passe au menu suivant
			int pointed_menu = -1;
			
			// On cherche le menu pointé
			ScreenManager screen = ScreenManager.getInstance();
			int x = Mouse.getX()-screen.getOrigineCarteX();
			int y = screen.getScreenHeight()-Mouse.getY()-screen.getOrigineCarteY();
			
			if(y>288-43 && y<369-43) {
				pointed_menu = 0;
				selected_menu = 0;
			}
			if(y>288+96-43 && y<369+96-43) {
				pointed_menu = 1;
				selected_menu = 1;
			}
			if(y>288+2*96-43 && y<369+2*96-43) {
				pointed_menu = 2;
				selected_menu = 2;
			}
			
			// On detecte le clic
			if(Mouse.isButtonDown(0)) {
				mouse_lock = true;
				if(pointed_menu != -1) {
					return selected_menu;
				}
			}
		}
		else {
			// si on a locké la souris on attend que le bouton soit relaché 
			mouse_lock = Mouse.isButtonDown(0);		
		}
		return 3;
	}
	
	public int update_submenu (int ret) {
		// On traite le clavier
		if(!key_lock) {
			// Sur escape on retourne à l'ecran principal
			if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				key_lock = true;
				return 3;
			}
			// Sur espace ou entree on passe a l'ecran suivant 
			if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)
			|| Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
				key_lock = true;
				return ret+1;
			}
		}
		else {
			// Si on a locké le clavier on attend que les touches soient relachées 
			key_lock = Keyboard.isKeyDown(Keyboard.KEY_UP)
						|| Keyboard.isKeyDown(Keyboard.KEY_DOWN)
						|| Keyboard.isKeyDown(Keyboard.KEY_RETURN)
						|| Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)
						|| Keyboard.isKeyDown(Keyboard.KEY_SPACE);
		}
		// On traite la souris
		if(!mouse_lock) {
			// Si on clique on passe au menu suivant
			if(Mouse.isButtonDown(0)) {
				mouse_lock = true;
				return ret+1;
			}
		}
		else {
			// si on a locké la souris on attend que le bouton soit relaché 
			mouse_lock = Mouse.isButtonDown(0);
		}
		return ret;
	}
	
	public void update() {
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			Application.getInstance().quit();
		}
		
		if (Mouse.isButtonDown(1) || Mouse.isButtonDown(0)) {
			if(selected == null) {
				selected = getTargetedVille();
				System.out.println(selected);
			}

			pourcentage += Mouse.getDWheel()/15;

			if(pourcentage > 100) {
				pourcentage = 100;
			}
			else if(pourcentage < 0) {
				pourcentage = 0;
			}

			vaccin = Mouse.isButtonDown(1);
		} else {
			if(selected != null) {
				Ville released = getTargetedVille();
				if(released != null) {
					if (vaccin) {
						if (!selected.getStocksVaccins().isEmpty()) {
							if(selected.getStocksVaccins().get(0).getStock()>0) {
								Stock nouveau_stock = new StockVaccin(selected.getStocksVaccins().get(0).getStock()*pourcentage/100,
										selected.getStocksVaccins().get(0).getVaccin());
								Application.getInstance().getGame().creerTransfert(selected,released,nouveau_stock);
							}
						}
					} else {
						if (!selected.getStocksTraitements().isEmpty()) {
							if(selected.getStocksTraitements().get(0).getStock()>0) {
								Stock nouveau_stock = new StockTraitement(selected.getStocksTraitements().get(0).getStock()*pourcentage/100,
										selected.getStocksTraitements().get(0).getTraitement());
								Application.getInstance().getGame().creerTransfert(selected,released,nouveau_stock);
							}
						}
					}
				}
				selected = null;
			}
			else {
				selected = null;
			}
		}
		ScreenManager.getInstance().setSelected(selected,!vaccin);
	}

	public static PlayerManager getInstance() {
		return instance;
	}

	public Ville getTargetedVille()
	{
		for(Zone  z : Application.getInstance().getGame().getCarte().getZones()) {
			if(z.getUsine().isOnCity(Mouse.getX()-ScreenManager.getInstance().getOrigineCarteX(),
					ScreenManager.getInstance().getScreenHeight() - Mouse.getY())) {
				return z.getUsine();
			}
			for(Ville v : z.getVilles()) {
				if(v.isOnCity(Mouse.getX()-ScreenManager.getInstance().getOrigineCarteX(),
						ScreenManager.getInstance().getScreenHeight() - Mouse.getY())) {
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
