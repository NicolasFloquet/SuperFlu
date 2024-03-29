package connexion;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import logique.Application;
import logique.GameLogic;
import logique.GameLogic.EtatJeu;
import entities.Joueur;
import entities.Zone;

public class ThreadClient extends Thread {

	private Socket s;

	public ThreadClient(Socket s) {
		this.s = s;
	}

	public void run() {
		// reception de client

		Application a = Application.getInstance();
		Receive rec = new Receive(s);
		Object o = null;
		while (a.isRunning()) {
			try {
				o = rec.getDataBlock();
			} catch (SocketException e) {
				System.err.println("Serveur deconnecte");
				System.exit(1);
			}// reception des donnees blocante
			catch (IOException e) {
				System.err.println("Connexion au serveur perdue !");
				break;
			}
			if (o instanceof GameLogic) {
				
				// acualiser le game
				a.setGame((GameLogic) o);
				if (Application.getInstance().isStarted() == false && ((GameLogic) o).getEtat() == EtatJeu.EN_COURS) {
					Application.getInstance().startGame();
				}
				
				// XXX : ugly
				for(Joueur j:a.getGame().getJoueurs()){
					if(j.getZones().get(0).getNom().equals(a.getJoueur().getZones().get(0).getNom())){
						a.setJoueur(j);
						break;
					}
				}
				
			} else if (o instanceof Joueur) {
				System.out.println("Joueur reçu!");
				List<Zone> zList = a.getGame().getCarte().getZones();
				List<Zone> zList2 = new ArrayList<Zone>();
				for(Zone z: zList){
					for(Zone zon: ((Joueur)o).getZones()){
						if(z.getNom().equals(zon.getNom())){
							zList2.add(zon);
							System.out.println(zon.getNom() + ":" + zon.getPopulation_infectee());
						}
					}
				}
				System.out.println(zList2);
				((Joueur)o).setZone(zList2);
				a.setJoueur((Joueur)o);
			}
		}
	}
}