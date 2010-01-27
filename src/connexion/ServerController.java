package connexion;

import java.util.List;

import entities.Joueur;
import logique.GameLogic;

public class ServerController implements ConnexionController {

	public void connect() {
		new Server();
	}

	public void send(Object o) {
		if (o instanceof GameLogic) {
			GameLogic g = ((GameLogic) o).clone();
			List<Joueur> jList = g.getJoueurs();
			
			//mettre a null tous les sockets car il ne sont pas serializables, et on n'a pas besoin
			for(int i=0;i<jList.size();i++){
				jList.get(i).setSocket(null);
			}
			
			Send.sendData(g, jList);
		}
	}

}
