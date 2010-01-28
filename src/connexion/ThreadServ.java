package connexion;

import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

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
	private boolean end = false;
	private int nbjoueurs = 6;

	public ThreadServ(Socket s,int nbjoueurs) {
		this.s = s;
		this.nbjoueurs = nbjoueurs;
	}

	public void deconnecte() {
		Application.getInstance().quit();
	}

	public void run() {
		System.out.println("Création d'un nouveau thread serveur");

		// Reception du serveur
		Application a = Application.getInstance();
		Joueur j = new Joueur();
		
		System.out.print("zone: ");
		List<Zone> zListe = new ArrayList<Zone>();
		int extra = 6%nbjoueurs;
		for(int i=0;i<6/nbjoueurs;i++){
			Zone z = a.getNextZone();
			System.out.print(z.getNom()+"  ");
			zListe.add(z);
			if((extra!=0)&&(z.getId()<=extra+1)){
				z = a.getNextZone();
				zListe.add(z);
				System.out.print(z.getNom()+"  ");
			}
		}
		
		j.setZone(zListe);
		Send.sendData(j, s);

		j.setSocket(s);
		a.getGame().ajouterJoueur(j);
		Receive rec = new Receive(s);
		Object o = null;
		while ((Application.getInstance().isRunning()) && (!end)) {
			try {
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
							for (Vaccin vac : vaList) {
								if (vac.getVirus().getNom().equals(
										((StockVaccin) stock).getVaccin()
												.getVirus().getNom())) {
									((StockVaccin) ((Transfert) o).getStock())
											.setVaccin(vac);
								}
							}
						} else if (stock instanceof StockTraitement) {
							ArrayList<Traitement> trList = u.getTraitements();
							for (Traitement tr : trList) {
								if (tr.getVirus().getNom().equals(
										((StockTraitement) stock)
												.getTraitement().getVirus()
												.getNom())) {
									((StockTraitement) ((Transfert) o)
											.getStock()).setTraitement(tr);
								}
							}
						}

					}

					System.out.print(depart.getNom());
					System.out.println(" -> " + arrivee.getNom());
					if ((depart != null) && (arrivee != null)) {
						if (stock instanceof StockVaccin) {
							depart.retireStockVaccin(((StockVaccin) stock)
									.getVaccin(), stock.getStock());
						} else if (stock instanceof StockTraitement) {
							depart.retireStockTraitement(
									((StockTraitement) stock).getTraitement(),
									stock.getStock());
						}
						Transfert transport = new Transfert(depart, arrivee,
								stock, ((Transfert) o).getTemps_depart());
						a.getGame().getTransferts().add(transport);
					}
				}
			} catch (SocketException e) {
				System.err.println("user deconnecte");
				end = true;
				a.getGame().getJoueurs().remove(j);
			}
		}
	}
}
