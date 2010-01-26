import java.util.ArrayList;
import java.util.Random;

import entities.*;



public class GameLogic {
	/*TODO: Calibrer TAUX_MIGRATION*/
	private final static float TAUX_MIGRATION = 1000.0f; 
	
	private static Random rand; 
	
	private static GameLogic instance = null;
	
	private ArrayList<Joueur> joueurs;			// Liste des joueurs de la partie
	private ArrayList<Transfert> transferts;	// Liste des transferts en cours
	private Carte carte;
	
	
	private GameLogic(){
		rand = new Random();
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
				int flux_sain = 0;
				int flux_infecte = 0;
				int flux_immunise = 0;
				float taux_sain = ville_origine.getHabitantsSains()/ville_origine.getHabitants();
				float taux_infection = ville_origine.getHabitantsInfectes()/ville_origine.getHabitants();
				float taux_immunisation = ville_origine.getHabitantsImmunises()/ville_origine.getHabitants();
				
				for(Zone zone_dest : carte.getZones()){
					for(Ville ville_dest : zone_dest.getVilles()){ /* Woot 4 boucles imbriquées */
						distance = Ville.distance(ville_origine, ville_dest);
						
						/*Inserer ici une formule magique */
						flux = (int) ((rand.nextGaussian()*TAUX_MIGRATION)/distance); 
						
						/*TODO: Trouver une meilleur formule pour le nombre de migrants infectés*/
						flux_sain = (int)(taux_sain*flux);
						flux_infecte = (int)(taux_infection*flux);
						flux_immunise = (int)(taux_immunisation*flux);
						 
						/* Mise à jour de la population saine*/
						if(ville_origine.getHabitantsSains()>=flux_sain){
							ville_dest.ajouteHabitantsSains(flux_sain);
							ville_origine.retireHabitantsSains(flux_sain);
						}else{
							ville_dest.ajouteHabitantsSains(ville_origine.getHabitantsSains());
							ville_origine.retireHabitantsSains(ville_origine.getHabitantsSains());
						}
						
						/* Mise à jour de la population infectée*/
						if(ville_origine.getHabitantsInfectes()>=flux_infecte){
							ville_dest.ajouteHabitantsInfectes(flux_infecte);
							ville_origine.retireHabitantsInfectes(flux_infecte);
						}else{
							ville_dest.ajouteHabitantsInfectes(ville_origine.getHabitantsInfectes());
							ville_origine.retireHabitantsInfectes(ville_origine.getHabitantsInfectes());
						}
												
						/* Mise à jour de la population immunisée */
						if(ville_origine.getHabitantsImmunises()>=flux_immunise){
							ville_dest.ajouteHabitantsImmunises(flux_immunise);
							ville_origine.retireHabitantsImmunises(flux_immunise);
						}else{
							ville_dest.ajouteHabitantsImmunises(ville_origine.getHabitantsImmunises());
							ville_origine.retireHabitantsImmunises(ville_origine.getHabitantsImmunises());
						}
						
					}
				}
				
				/* Mise à jour de la ville */
				ville_origine.update();
				
			}
		}
		
		// Mise à jour des stocks
		
		
	}

}
