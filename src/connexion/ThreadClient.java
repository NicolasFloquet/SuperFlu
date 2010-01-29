package connexion;

import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import logique.Application;
import logique.GameLogic;
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
				System.err.println("Serveur deconecte");
				System.exit(1);
			}// reception des donnees blocante
			if (o instanceof GameLogic) {
				
				// acualiser le game
				//System.out.println(((GameLogic)o).getCarte());
				a.setGame((GameLogic) o);
			} else if (o instanceof Joueur) {
				List<Zone> zList = a.getGame().getCarte().getZones();
				List<Zone> zList2 = new ArrayList<Zone>();
				for(Zone z: zList){
					for(Zone zon: ((Joueur)o).getZone()){
						if(z.getNom().equals(zon.getNom())){
							zList2.add(zon);
							z.setJoueur((Joueur) o);
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