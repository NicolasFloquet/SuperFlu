package connexion;

import java.net.Socket;
import java.util.ArrayList;

import logique.Application;
import entities.Joueur;
import entities.Stock;
import entities.StockTraitement;
import entities.StockVaccin;
import entities.Traitement;
import entities.Transfert;
import entities.Usine;
import entities.Vaccin;
import entities.Ville;
import entities.Zone;



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

			Ville depart = null;
			Ville arrivee = null;
			
			Stock stock = ((Transfert) o).getStock();
			ArrayList<Zone> zList = a.getGame().getCarte().getZones();
			for (Zone z : zList) {
				ArrayList<Ville> vList = z.getVilles();
				for (Ville v : vList) {
					if (v.getNom().equals(
							((Transfert) o).getDepart().getNom())) {
						depart = v;
					}
					if (v.getNom().equals(
							((Transfert) o).getArrivee().getNom())) {
						arrivee = v;
					}
				}
				
				Usine u = z.getUsine();
				if (u.getNom().equals(
						((Transfert) o).getDepart().getNom())) {
					depart = u;
				}
				if (u.getNom().equals(
						((Transfert) o).getArrivee().getNom())) {
					arrivee = u;
				}
				if (stock instanceof StockVaccin) {
					ArrayList<Vaccin> vaList = u.getVaccins();
					for(Vaccin vac: vaList){
						if(vac.getVirus().getNom().equals(((StockVaccin) stock).getVaccin().getVirus().getNom())){
							((StockVaccin)((Transfert) o).getStock()).setVaccin(vac);
						}
					}
				} else if (stock instanceof StockTraitement) {
					ArrayList<Traitement> trList = u.getTraitements();
					for(Traitement tr: trList){
						if(tr.getVirus().getNom().equals(((StockTraitement) stock).getTraitement().getVirus().getNom())){
							((StockTraitement)((Transfert) o).getStock()).setTraitement(tr);
						}
					}
				}
			}

			

			System.out.println("  *"+depart.getNom());
			System.out.println("  *"+arrivee.getNom());
			if ((depart != null)&&(arrivee !=null)) {
				if (stock instanceof StockVaccin) {
					depart.retireStockVaccin(((StockVaccin) stock)
							.getVaccin(), stock.getStock());
				} else if (stock instanceof StockTraitement) {
					depart.retireStockTraitement(((StockTraitement) stock)
							.getTraitement(), stock.getStock());
				}
				Transfert transport = new Transfert(depart, arrivee, stock,
						((Transfert) o).getTemps_depart());
				a.getGame().getTransferts().add(transport);
			}
		}
	}
}
}

