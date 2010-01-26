import java.util.ArrayList;
import entities.*;



public class GameLogic {
	/*TODO: Calibrer TAUX_MIGRATION*/
	private final static float TAUX_MIGRATION = 1000.0f; 
	
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
		/* Mise à jour du temps */
		
		
		/* Calcul des déplacement de population */
		
		for(Zone zone_origine : carte.getZones()){
			for(Ville ville_origine: zone_origine.getVilles()){
				
				/* Pour chaque ville, on calcul le nombre d'habitants qui partent vers chaque ville */
				float distance = 0.0f;
				int flux = 0;
				int flux_infecte = 0;
				float taux_infection = ville_origine.getHabitants_infectes()/ville_origine.getHabitants();
				float taux_immunisation = ville_origine.getHabitants_immunises()/ville_origine.getHabitants();
				
				for(Zone zone_dest : carte.getZones()){
					for(Ville ville_dest : zone_dest.getVilles()){ /* Woot 4 boucles imbriquées */
						distance = Ville.distance(ville_origine, ville_dest);
						
						/*Inserer ici une formule magique */
						flux = (int) (TAUX_MIGRATION/distance); 
						
						/*TODO: Trouver une meilleur formule pour le nombre de migrants infectés*/
						flux_infecte = (int)(taux_infection*flux);
						 
						/* Mise à jour de la population totale */
						if(ville_origine.getHabitants()>=flux){
							ville_dest.ajouteHabitants(flux);
							ville_dest.retireHabitants(flux);
						}else{
							ville_dest.ajouteHabitants(ville_origine.getHabitants());
							ville_dest.retireHabitants(ville.origine.getHabitants());
						}
						
						/* Mise à jour de la population infectée */
						if(ville_origine.getHabitants_infectes()>=flux_infecte){
							ville_dest.setHabitants_infectes(ville_dest.getHabitants_infectes()+flux_infecte);
							ville_origine.setHabitants_infectes(ville_origine.getHabitants_infectes()-flux_infecte);
						}else{
							ville_dest.setHabitants_infectes(ville_origine.getHabitants_infectes());
							ville_origine.setHabitants_infectes(0);
						}
						
						/* Mise à jour de la population immunisée */
						
					}
				}
				
			}
		}
		
		// Mise à jour des stocks
		
		// Mise à jour des infections
		
	}

}
