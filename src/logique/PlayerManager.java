package logique;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import entities.*;
import graphics.ScreenManager;

public class PlayerManager {

	private static PlayerManager instance = new PlayerManager();
	private int[] konami_code = {Keyboard.KEY_UP,
								Keyboard.KEY_UP,
								Keyboard.KEY_DOWN,
								Keyboard.KEY_DOWN,
								Keyboard.KEY_LEFT,
								Keyboard.KEY_RIGHT,
								Keyboard.KEY_LEFT,
								Keyboard.KEY_RIGHT,
								Keyboard.KEY_B,
								Keyboard.KEY_A,
								Keyboard.KEY_RETURN}; // Le dernier est un vieux hack stout
	
	private int currentKonami;
	private boolean konamiLock;
	private boolean konamiUnlocked;

	private Ville selected;
	private int pourcentage; //le pourcentage à envoyer lors du transfert (se change à la molette)
	private boolean vaccin = true;

	private int selected_menu;	// 0=>start 1=>aide 2=>credits
	private boolean key_lock; // Ceci permet de ne pas interpreter 2x de suite la même touche
	private boolean mouse_lock;
	private Ville targetedVille;
	
	private PlayerManager() {
		selected = null;
		selected_menu = 0;
		pourcentage = 50;
		key_lock = false;
		mouse_lock = false;
		
		currentKonami = 0;
		konamiLock = false;
		konamiUnlocked = false;
	}

	public int getSelectedMenu() {
		return selected_menu;
	}
	
	public boolean isKonami() {
		return konamiUnlocked;
	}
	
	private void updateKonami() {
		if(!konamiLock) {
			if(Keyboard.isKeyDown(Keyboard.KEY_UP)) {
				if(konami_code[currentKonami] == Keyboard.KEY_UP) {
					currentKonami++;
				} else {
					currentKonami = 0;
				}
				konamiLock = true;
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
				if(konami_code[currentKonami] == Keyboard.KEY_DOWN) {
					currentKonami++;
				} else {
					currentKonami = 0;
				}
				konamiLock = true;
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
				if(konami_code[currentKonami] == Keyboard.KEY_LEFT) {
					currentKonami++;
				} else {
					currentKonami = 0;
				}
				konamiLock = true;
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
				if(konami_code[currentKonami] == Keyboard.KEY_RIGHT) {
					currentKonami++;
				} else {
					currentKonami = 0;
				}
				konamiLock = true;
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
				if(konami_code[currentKonami] == Keyboard.KEY_A) {
					currentKonami++;
				} else {
					currentKonami = 0;
				}
				konamiLock = true;
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_B)) {
				if(konami_code[currentKonami] == Keyboard.KEY_B) {
					currentKonami++;
				} else {
					currentKonami = 0;
				}
				konamiLock = true;
			}
		} else {
			konamiLock = Keyboard.isKeyDown(Keyboard.KEY_UP)
						|| Keyboard.isKeyDown(Keyboard.KEY_DOWN)
						|| Keyboard.isKeyDown(Keyboard.KEY_LEFT)
						|| Keyboard.isKeyDown(Keyboard.KEY_RIGHT)
						|| Keyboard.isKeyDown(Keyboard.KEY_B)
						|| Keyboard.isKeyDown(Keyboard.KEY_A);
		}
		
		if(currentKonami >= konami_code.length-1) {
			konamiUnlocked = !konamiUnlocked;
			currentKonami = 0;
		}
	}
	
	// Return l'etat selectionné ou 3 si aucun etat n'est selectioné
	public int update_menu() {
		updateKonami();
		
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
				
				//piege on retourne 4 si on devait retourner 2 (pour zapper les pages de l'aide)
				return selected_menu==2?4:selected_menu;
			}
			else if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
				key_lock = true;
				
				//piege on retourne 4 si on devait retourner 2 (pour zapper les pages de l'aide)
				return selected_menu==2?4:selected_menu;
			}
			else if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				key_lock = true;
				Application.getInstance().quit();
				return -1;
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
					return selected_menu==2?4:selected_menu;
				}
			}
		}
		else {
			// si on a locké la souris on attend que le bouton soit relaché 
			mouse_lock = Mouse.isButtonDown(0);		
		}
		return -1;
	}
	
	public int update_submenu (int ret) {
		updateKonami();
		
		// On traite le clavier
		if(!key_lock) {
			// Sur escape on retourne à l'ecran principal
			if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				key_lock = true;
				return -1;
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
		updateKonami();
		
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			Application.getInstance().quit();
		}
		
		targetedVille = findTargetedVille();
		
		if (Mouse.isButtonDown(1) || Mouse.isButtonDown(0)) {
			if(selected == null) {
				selected = targetedVille;
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
				Ville released = targetedVille;
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

	private Ville findTargetedVille()
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
	
	public Ville getTargetedVille() {
		return targetedVille;
	}

	public int getPourcentageVoulu() {
		return pourcentage;
	}

}
