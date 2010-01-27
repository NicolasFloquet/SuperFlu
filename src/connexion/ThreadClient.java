package connexion;

import java.net.Socket;

import entities.Joueur;

import logique.Application;
import logique.GameLogic;

public class ThreadClient extends Thread {

	private Socket s;

	public ThreadClient(Socket s) {
		this.s = s;
	}

	public void run() {
		// reception de client
		Application a = Application.getInstance();
		Receive rec = new Receive(s);
		Object o;
		while (true) {
			o = rec.getDataBlock();// reception des donnees blocante
			if (o instanceof GameLogic) {
				// acualiser le game
				a.setGame((GameLogic) o);
			} else if (o instanceof Joueur) {
				a.setJoueur((Joueur) o);
			}
		}
	}
}
