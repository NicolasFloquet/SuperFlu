package logique;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import entities.Carte;
import entities.Joueur;
import entities.Stock;
import entities.StockTraitement;
import entities.StockVaccin;
import entities.Traitement;
import entities.Transfert;
import entities.Vaccin;
import entities.Ville;
import entities.Virus;
import entities.Zone;

public class GameLogic implements Cloneable, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/* TODO: Calibrer TAUX_MIGRATION */
	private final static float TAUX_MIGRATION = 0.1f;

	private Random rand = new Random();

	private List<Joueur> joueurs; // Liste des joueurs de la partie
	private List<Transfert> transferts; // Liste des transferts en cours
	private List<Virus> virus;
	private Carte carte;

	private int mortsTotal = 0;
	private int populationMondiale = 0;
	private int vaccinesTotal = 0;
	private int populationInfectee = 0;

	/* Variables liées au timer */
	private long time;

	public enum etatJeu {
		EN_COURS, GAGNE, PERDU
	};

	public GameLogic() {
		joueurs = Collections.synchronizedList(new ArrayList<Joueur>());
		transferts = Collections.synchronizedList(new ArrayList<Transfert>());
		virus = Collections.synchronizedList(new ArrayList<Virus>());
		carte = new Carte();

		time = 0;
	}

	public void creerEpidemie() {
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
		for (Zone z : carte.getZones()) {
			if (z.getUsine() != null) {
				z.getUsine().ajouteTraitement(traitement);
				z.getUsine().ajouteVaccin(vaccin);
			}
		}

		/* Choisir le point de départ de l'épidemie aléatoirement */
		rand_zone = carte.getZones().get(rand.nextInt(carte.getZones().size()));

		rand_ville = rand_zone.getVilles().get(
				rand.nextInt(rand_zone.getVilles().size()));

		rand_ville = rand_zone.getVilles().get(
				rand.nextInt(rand_zone.getVilles().size()));
		// rand_ville.ajouteHabitantsInfectes((int)(rand_ville.getHabitants()*0.01));
		rand_ville.ajouteHabitantsInfectes(1000);

		/* MOUHOUHOUHOHOHAHAHAHAHAHAHAHA */

	}

	public synchronized etatJeu updateServeur(long elapsed_time) {
		/* Mise à jour du temps */
		time += elapsed_time * 10;

		mortsTotal = 0;
		populationMondiale = 0;
		populationInfectee = 0;
		vaccinesTotal = 0;
		for (Zone zone_origine : carte.getZones()) {
			for (Ville ville : zone_origine.getVilles()) {
				mortsTotal += ville.getHabitantsMorts();
				populationMondiale += ville.getHabitants();
				populationInfectee += ville.getHabitantsInfectes();
				vaccinesTotal += ville.getHabitantsImmunises();
			}
		}

		Iterator<Transfert> it = transferts.iterator();
		while (it.hasNext()) {
			Transfert t = it.next();
			if (time > t.getTemps_arrivee()) {
				Ville arrive = t.getArrivee();

				if (t.getStock() instanceof StockTraitement) {
					StockTraitement stock_traitement = (StockTraitement) t
							.getStock();
					arrive.ajouteStockTraitement(stock_traitement
							.getTraitement(), stock_traitement.getStock());
				}

				else if (t.getStock() instanceof StockVaccin) {
					StockVaccin stock_vaccin = (StockVaccin) t.getStock();
					arrive.ajouteStockVaccin(stock_vaccin.getVaccin(),
							stock_vaccin.getStock());
				}

				it.remove();
				System.out.println("Transfert fini.");
			}
		}

		/* Calcul des déplacement de population */
		for (Zone zone_origine : carte.getZones()) {

			/* Mise à jour de la production de l'usine */
			if (zone_origine.getUsine() != null) {
				zone_origine.getUsine().produit();
			}

			for (Ville ville_origine : zone_origine.getVilles()) {

				if (ville_origine.getHabitants() == 0) {
					continue;
				}

				/*
				 * Pour chaque ville, on calcul le nombre d'habitants qui
				 * partent vers chaque ville
				 */
				float distance = 0.0f;

				int flux = 0;
				int flux_sain = 0;
				int flux_infecte = 0;
				int flux_immunise = 0;
				float taux_sain = (float) ville_origine.getHabitantsSains()
						/ (float) ville_origine.getHabitants();
				float taux_infection = (float) ville_origine
						.getHabitantsInfectes()
						/ (float) ville_origine.getHabitants();
				float taux_immunisation = (float) ville_origine
						.getHabitantsImmunises()
						/ (float) ville_origine.getHabitants();

				for (Zone zone_dest : carte.getZones()) {
					for (Ville ville_dest : zone_dest.getVilles()) { /*
																	 * Woot 4
																	 * boucles
																	 * imbriquées
																	 */

						if (ville_dest == ville_origine
								|| ville_dest == zone_dest.getUsine()) {
							continue;
						}

						distance = Ville.distance(ville_origine, ville_dest);

						/*
						 * distance = 1; LOL
						 */

						/* Inserer ici une formule magique */
						flux = (int) ((rand.nextFloat() * TAUX_MIGRATION * ville_origine
								.getHabitants()) / (distance + getPopulationInfectee() / 50f));

						/*
						 * TODO: Trouver une meilleur formule pour le nombre de
						 * migrants infectés
						 */
						flux_sain = (int) (taux_sain * flux);
						flux_infecte = (int) (taux_infection * flux);
						flux_immunise = (int) (taux_immunisation * flux);

						/*
						 * System.out.println("flux sain " + flux_sain);
						 * System.out.println("flux infecte " + flux_infecte);
						 * System.out.println("flux immunise " + flux_immunise);
						 */

						/* Mise à jour de la population saine */
						if (ville_origine.getHabitantsSains() >= flux_sain) {
							ville_dest.ajouteHabitantsSains(flux_sain);
							ville_origine.retireHabitantsSains(flux_sain);
						} else {
							ville_dest.ajouteHabitantsSains(ville_origine
									.getHabitantsSains());
							ville_origine.retireHabitantsSains(ville_origine
									.getHabitantsSains());
						}

						/* Mise à jour de la population infectée */
						if (ville_origine.getHabitantsInfectes() >= flux_infecte) {
							ville_dest.ajouteHabitantsInfectes(flux_infecte);
							ville_origine.retireHabitantsInfectes(flux_infecte);
						} else {
							ville_dest.ajouteHabitantsInfectes(ville_origine
									.getHabitantsInfectes());
							ville_origine.retireHabitantsInfectes(ville_origine
									.getHabitantsInfectes());
						}

						/* Mise à jour de la population immunisée */
						if (ville_origine.getHabitantsImmunises() >= flux_immunise) {
							ville_dest.ajouteHabitantsImmunises(flux_immunise);
							ville_origine
									.retireHabitantsImmunises(flux_immunise);
						} else {
							ville_dest.ajouteHabitantsImmunises(ville_origine
									.getHabitantsImmunises());
							ville_origine
									.retireHabitantsImmunises(ville_origine
											.getHabitantsImmunises());
						}

					}
				}

				/* Mise à jour de la ville */
				ville_origine.update();

			}
		}
		return jeuFini();
	}

	public synchronized void creerTransfert(Ville depart, Ville arrivee,
			Stock stock) {
		
		if (stock instanceof StockVaccin) {
			depart.retireStockVaccin(((StockVaccin) stock).getVaccin(), stock
					.getStock());
		} else if (stock instanceof StockTraitement) {
			depart.retireStockTraitement(((StockTraitement) stock)
					.getTraitement(), stock.getStock());
		}
		Transfert transport = new Transfert(depart, arrivee, stock, time);
		transferts.add(transport);

		if (!isServeur()) {
			Application.getInstance().sendTransfert(transport);
		}
	}

	public synchronized List<Transfert> getTransferts() {
		return transferts;
	}

	public void ajouterJoueur(Joueur nouveau_joueur) {
		joueurs.add(nouveau_joueur);
	}

	public List<Joueur> getJoueurs() {
		return joueurs;
	}

	public boolean isServeur() {
		return Application.getInstance().isServeur();
	}

	public Carte getCarte() {
		return carte;
	}

	public long getTime() {
		return time;
	}

	public int getPopulationInfectee() {
		return populationInfectee;
	}

	public int getPopulationMondiale() {
		return populationMondiale;
	}

	public int getMortsTotal() {
		return mortsTotal;
	}

	public int getVaccinesTotal() {
		return vaccinesTotal;
	}

	public etatJeu jeuFini() {
		etatJeu etat = etatJeu.EN_COURS;

		if (populationInfectee == 0)
			etat = etatJeu.GAGNE;
		if (mortsTotal == (populationMondiale + mortsTotal) / 10)
			etat = etatJeu.PERDU;

		return etat;
	}

	public GameLogic clone() {
		try {
			GameLogic g = (GameLogic) super.clone();
			ArrayList<Joueur> j = new ArrayList<Joueur>();
			for (int i = 0; i < joueurs.size(); i++) {
				j.add(this.joueurs.get(i).clone());
			}
			g.joueurs = j;
			return g;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
}
