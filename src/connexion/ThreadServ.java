package connexion;

import java.net.Socket;

import logique.Application;
import entities.Joueur;
import entities.Transfert;

public class ThreadServ extends Thread {

	private Socket s;
	private boolean running;

	public ThreadServ(Socket s) {
		this.s = s;
		this.running = true;
	}

	public void deconnecte() {
		running = false;
	}
	
	public void run() {
		System.out.println("Création d'un nouveau thread serveur");

		// Reception du serveur
		Application a = Application.getInstance();
		Joueur j = new Joueur();
		j.setZone(a.getNextZone());
		Send.sendData(j, s);
		
		j.setSocket(s);
		a.getGame().ajouterJoueur(j);
		Receive rec = new Receive(s);
		Object o;
		while (running) {
			o = rec.getDataBlock();// reception des donnees blocante
			if (o instanceof Transfert) {
				// ajouter le transfert
				a.getGame().creerTransfert(((Transfert) o).getDepart(), ((Transfert) o).getArrivee(), ((Transfert) o).getStock());
				a.getGame().getTransferts().add((Transfert) o);
			}
		}
	}
}
