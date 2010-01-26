import java.util.ArrayList;
import entities.*;



public class GameLogic {
	private static GameLogic instance = null;
	
	private ArrayList<Joueur> joueurs;			// Liste des joueurs de la partie
	private ArrayList<Transfert> transferts;	// Liste des transferts en cours
	private Carte carte;
	
	
	private GameLogic(){
		joueurs = new ArrayList<Joueur>();
		transferts = new ArrayList<Transfert>();	
	}	
	
	public static GameLogic getInstance(){
		if(instance == null){
			instance = new GameLogic();
		}
		return instance;
	}
	
	public void Update(){
		/* Calcul des déplacement de population */
		
		for(Zone zone_origine : carte.getZones()){
			for(Ville ville_origine: zone_origine.getVilles()){
				
				/* Pour chaque ville, on calcul le nombre d'habitants qui partent vers chaque ville */
				float distance = 0.0f;
				int flux = 0;
				for(Zone zone_dest : carte.getZones()){
					for(Ville ville_dest : zone_dest.getVilles()){ /* Woot 4 boucles imbriquées */
						distance = Ville.distance(ville_origine, ville_dest);
						
						/* Inserer ici une formule magique */
						flux = 0; 
	
						ville_dest.setHabitants(ville_dest.getHabitants()+flux);
						ville_dest.setHabitants(ville_dest.getHabitants()-flux);			
					}
				}
				
			}
		}
		
		// Mise à jour des stocks
		
		// Mise à jour des infections
		
	}

}
