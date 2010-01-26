package logique;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import entities.*;

public class GameLogic extends TimerTask {
	/*TODO: Calibrer TAUX_MIGRATION*/
	private final static float TAUX_MIGRATION = 0.01f;
	private final static int TIMER_PERIOD = 200;
	private static GameLogic instance = null;

	private Random rand = new Random(); 
	private boolean mode_serveur = false;

	private ArrayList<Joueur> joueurs;			// Liste des joueurs de la partie
	private ArrayList<Transfert> transferts;	// Liste des transferts en cours
	private ArrayList<Virus> virus;
	private Carte carte;

	/* Variables liées au timer */
	private long time;
	private int time_unit;
	private Timer timer;

	private GameLogic() {
		joueurs = new ArrayList<Joueur>();
		transferts = new ArrayList<Transfert>();
		carte = new Carte();

		time = 0;
		time_unit = 100;
		timer = new Timer();
		timer.scheduleAtFixedRate(this, 0, TIMER_PERIOD);
	}	

	public static GameLogic getInstance(){
		if (instance == null) {
			instance = new GameLogic();
		}
		return instance;
	}

	public void run() {
		if(!Application.getInstance().isRunning()) {
			timer.cancel();
		}
		else {
			if (mode_serveur)
				updateServeur(time_unit);
			else
				updateClient(time_unit);
		}
	}

	public void creerEpidemie()
	{
		Traitement traitement;
		Vaccin vaccin;
		Zone rand_zone;
		Ville rand_ville;

		/* Créer un nouveau virus */
		Virus nouveau_virus = new Virus("Grippe du serpent +3 of the doom");
		virus.add(nouveau_virus);

		/* Créer les traitements et vaccins correspondants */
		vaccin = new Vaccin(nouveau_virus);
		traitement = new Traitement(nouveau_virus);

		/* Les ajouter aux usines */
		for(Zone z : carte.getZones()){
			z.getUsine().ajouteTraitement(traitement);
			z.getUsine().ajouteVaccin(vaccin);
		}

		/* Choisir le point de départ de l'épidemie aléatoirement */
		rand_zone = carte.getZones().get(rand.nextInt(carte.getZones().size()));
		rand_ville = rand_zone.getVilles().get(rand.nextInt(rand_zone.getVilles().size()));
		rand_ville.ajouteHabitantsInfectes((int)(rand_ville.getHabitants()*0.001));

		/* MOUHOUHOUHOHOHAHAHAHAHAHAHAHA */

	}

	public void updateServeur(long elapsed_time){
		/* Mise à jour du temps */
		time += elapsed_time;

		for(Transfert t : transferts){

			/* Si le temps est écoulé, le transfert est fini, et on ajoute son contenu à la ville destinataire */
			if(time>t.getTemps_arrivee()){

				Ville arrive = t.getArrivee();

				if(t.getStock() instanceof StockTraitement){
					StockTraitement stock_traitement = (StockTraitement) t.getStock();
					arrive.ajouteStockTraitement(stock_traitement.getTraitement(), stock_traitement.getStock());
				}

				else if(t.getStock() instanceof StockVaccin){
					StockVaccin stock_vaccin = (StockVaccin) t.getStock();
					arrive.ajouteStockVaccin(stock_vaccin.getVaccin(), stock_vaccin.getStock());
				}

				/* Le transfert est fini, on le supprime de la liste */
				transferts.remove(t);
			}
		}

		/* Calcul des déplacement de population */	
		for(Zone zone_origine : carte.getZones()){

			/* Mise à jour de la production de l'usine */
			zone_origine.getUsine().produit();

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
						flux = (int) ((rand.nextGaussian()*TAUX_MIGRATION*ville_origine.getHabitants())/distance); 

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
	}

	public void updateClient(long elapsed_time) {

	}

	public void creerTransfert(Ville depart, Ville arrivee, Stock stock){
		transferts.add(new Transfert(depart, arrivee, stock, time));
	}

	public ArrayList<Transfert> getTransferts()
	{
		return transferts;
	}

	public void ajouterJoueur(Joueur nouveau_joueur)
	{
		joueurs.add(nouveau_joueur);
	}

	public ArrayList<Joueur> getJoueurs()
	{
		return joueurs;
	}

	public void setServeur()
	{
		mode_serveur = true;
	}

	public boolean isServeur()
	{
		return mode_serveur;
	}

	public Carte getCarte(){
		return carte;
	}

	public long getTime(){
		return time;
	}
}
