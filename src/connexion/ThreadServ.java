package connexion;

import java.net.Socket;

import logique.Application;
import entities.Joueur;
import entities.Transfert;

public class ThreadServ extends Thread {

	private Socket s;

	public ThreadServ(Socket s) {
		this.s = s;
	}

	public void run() {
		System.out.println("Création d'un nouveau thread serveur");

		// Reception du serveur
		Application a = Application.getInstance();
		Joueur j = new Joueur();
		j.setSocket(s);
		a.getGame().ajouterJoueur(j);
		Receive rec = new Receive(s);
		Object o;
		while (true) {
			o = rec.getDataBlock();// reception des donnees blocante
			if (o instanceof Transfert) {
				// ajouter le transfert
				a.getGame().getTransferts().add((Transfert) o);
			}
		}
	}
}
