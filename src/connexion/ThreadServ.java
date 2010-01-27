package connexion;

import java.net.Socket;

import logique.Application;
import entities.Joueur;
import entities.StockTraitement;
import entities.StockVaccin;
import entities.Transfert;



public class ThreadServ extends Thread {

	private Socket s;

	public ThreadServ(Socket s) {
		this.s = s;
	}

	public void deconnecte() {
		Application.getInstance().quit();
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
		while (Application.getInstance().isRunning()) {
			o = rec.getDataBlock();// reception des donnees blocante
			if (o instanceof Transfert) {
				// ajouter le transfert
				if(((Transfert) o).getStock() instanceof StockVaccin) {
					((Transfert) o).getDepart().retireStockVaccin(((StockVaccin) ((Transfert) o).getStock()).getVaccin(), ((Transfert) o).getStock().getStock());
				}
				else if (((Transfert) o).getStock() instanceof StockTraitement) {
					((Transfert) o).getDepart().retireStockTraitement(((StockTraitement) ((Transfert) o).getStock()).getTraitement(), ((Transfert) o).getStock().getStock());
				}		
				a.getGame().getTransferts().add((Transfert) o);
			}
		}
	}
}
