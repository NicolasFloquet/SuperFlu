package connexion;

import java.net.Socket;
import java.util.ArrayList;

import logique.Application;
import entities.*;


public class ThreadServ extends Thread {

	private Socket s;

	public ThreadServ(Socket s) {
		this.s = s;
	}

	// @SuppressWarnings("deprecation")
	public void run() {

		// faire les fonctions de serveur
		Application a = Application.getInstance();
		Joueur j = new Joueur();
		j.setSocket(s);
		a.getGame().ajouterJoueur(j);
		Receive rec = new Receive(s);
		Object o;
		while (true) {
			o = rec.getDataBlock();//reception des donnees blocante
			if(o instanceof Transfert ){
				//acualiser le transfer
				a.getGame().getTransferts().add((Transfert)o);
			}
		}

		/*
		 * Personne personne = new Personne("Benito", "Camelas", 180);
		 * 
		 * Send send = new Send(s); send.sendData(personne);
		 * send.sendData(personne);
		 * 
		 * Receive rec = new Receive(s); rec.start(); personne = (Personne)
		 * rec.getData(); // ///////// // // Affichage // // ///////// //
		 * System.out.println("Personne : "); System.out.println("nom : " +
		 * personne.getNom()); System.out.println("prenom : " +
		 * personne.getPrenom()); System.out.println("taille : " +
		 * personne.getTaille()); System.out.println("Hobbies:");
		 * ArrayList<Hobby> liste = personne.getHobbies(); for (int i = 0; i <
		 * liste.size(); i++) { System.out.println("  > " + liste.get(i)); }
		 * 
		 * rec.close(); rec.stop(); try { s.close(); } catch (IOException e) {
		 * e.printStackTrace(); System.exit(1); }
		 */
	}
}
