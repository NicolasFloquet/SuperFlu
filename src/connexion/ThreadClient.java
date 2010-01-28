package connexion;

import java.net.Socket;
import java.net.SocketException;
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
		while (Application.getInstance().isRunning()) {
			try {
				o = rec.getDataBlock();
			} catch (SocketException e) {
				System.err.println("Serveur deconecte");
				System.exit(1);
			}// reception des donnees blocante
			if (o instanceof GameLogic) {
				// acualiser le game
				a.setGame((GameLogic) o);
			} else if (o instanceof Joueur) {
				System.out.println("joueur=> "+((Joueur) o).getZone().getNom());
				List<Zone> zList = a.getGame().getCarte().getZones();
				for(Zone z: zList){
					System.out.println(z.getNom()+"??");
					if(z.getNom().equals(((Joueur) o).getZone().getNom())){
						System.out.println("set joueur: "+z.getNom());
						z.setJoueur((Joueur) o);
						((Joueur)o).setZone(z);
						a.setJoueur((Joueur)o);
						break;
					}
				}
			}
		}
	}
}