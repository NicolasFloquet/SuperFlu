package connexion;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import logique.Application;
import logique.GameLogic;
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
	private boolean err = false;
	private Joueur joueur;

	public ThreadServ(Socket s, int nbjoueurs) {
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
		joueur = new Joueur();
		
		System.out.print("zone: ");
		List<Zone> zListe = new ArrayList<Zone>();
			
		int extra = 6%nbjoueurs;
		synchronized (Application.getInstance()) {	
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
		}
		
		joueur.setZone(zListe);
		Send.sendData(joueur, s);

		joueur = new Joueur();
		joueur.setZone(zListe);
		joueur.setSocket(s);

		a.getGame().ajouterJoueur(joueur);
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

					System.out.println(depart.getNom()+" -> " + arrivee.getNom());
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
				} else if (o instanceof Joueur) {
					joueur.setPseudo(((Joueur) o).getPseudo());
					System.out.println("Joueur " + joueur.getPseudo() + " est ready !");
					
					GameLogic g = Application.getInstance().getGame().clone();
					List<Joueur> jList = g.getJoueurs();
					
					//mettre a null tous les sockets car il ne sont pas serializables, et on n'a pas besoin
					for(int i=0;i<jList.size();i++){
						jList.get(i).setSocket(null);
					}
					
					synchronized (Application.getInstance().getGame().getJoueurs()) {	
						Send.sendData(g, Application.getInstance().getGame().getJoueurs());
					}
				}
			} catch (SocketException e) {
				if(!err) err=true;
				else if(s.getChannel()==null){
					System.err.println(" joueur deconnecte ");
					a.JoueurDeconnecte(s);
					end = true;
					a.getGame().getJoueurs().remove(joueur);
				}else err = false;
			} catch (IOException e) {
				System.err.println("perte connexion avec l'utilisateur");
				end = true;
				a.getGame().getJoueurs().remove(joueur);
			}
		}
	}
}
