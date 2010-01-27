package connexion;

import java.util.ArrayList;

import logique.Application;
import logique.GameLogic;
import entities.Ville;
import entities.Zone;

public class TestConnexion {

	/**
	 * @param args
	 */
	
	/* 
	 * commenter screen = ScreenManager.getInstance(); du constructeur d'Application
	 */

	public static void ecrire(GameLogic g) {
		System.out.println("Obj a envoyer: ");
		System.out.println("time: " + g.getTime());
		ArrayList<Zone> z = g.getCarte().getZones();
		System.out.println("zones:");
		for (int i = 0; i < z.size(); i++) {
			System.out.println("  " + z.get(i).getNom());
			// System.out.println("    score: "+z.get(i).getJoueur().getScore());
			System.out.println("    usine: " + z.get(i).getUsine().getNom()
					+ "(" + z.get(i).getUsine().getX() + ","
					+ z.get(i).getUsine().getY() + ")");
			System.out.println("      " + z.get(i).getUsine().getHabitants()
					+ " habitants");
			System.out.println("      "
					+ z.get(i).getUsine().getHabitantsInfectes() + " infectes");
			System.out.println("      "
					+ z.get(i).getUsine().getHabitantsImmunises()
					+ " inmunises");
			System.out.println("      "
					+ z.get(i).getUsine().getHabitantsSains() + " sains");
			System.out.println("      "
					+ z.get(i).getUsine().getPourcentageInfectes()
					+ "% infectes");
			System.out.println("      "
					+ z.get(i).getUsine().getProductionRateVaccins()
					+ " rate vaccins");
			ArrayList<Ville> v = z.get(i).getVilles();
			for (int j = 0; j < v.size(); j++) {
				System.out.println("    ville: " + v.get(j).getNom() + "("
						+ v.get(j).getX() + "," + v.get(j).getY() + ")");
				System.out.println("      " + v.get(j).getHabitants()
						+ " habitants");
				System.out.println("      " + v.get(j).getHabitantsInfectes()
						+ " infectes");
				System.out.println("      " + v.get(j).getHabitantsImmunises()
						+ " inmunises");
				System.out.println("      " + v.get(j).getHabitantsSains()
						+ " sains");
				System.out.println("      " + v.get(j).getPourcentageInfectes()
						+ "% infectes");
			}
		}
	}

	public static void main(String[] args) {
		Application a = Application.getInstance();
		//ecrire(a.getGame());

		ConnexionController c = new ServerController();
		c.connect();
		try {
			Thread.sleep(50000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Serveur envoye");

		c.send(a.getGame());
		System.out.println("fin");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
