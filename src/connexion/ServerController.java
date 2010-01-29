package connexion;

import java.util.List;

import entities.Joueur;
import logique.GameLogic;

public class ServerController implements ConnexionController {

	private Server server;
	private int nbjoueurs = 6;
	
	public boolean connect() {
		server = new Server(nbjoueurs);
		//server = new Server();
		server.start();
		
		return true;
	}


	public void setNbjoueurs(int nbjoueurs) {
		if(nbjoueurs<=6){
			this.nbjoueurs = nbjoueurs;
		}else{
			this.nbjoueurs = 6;
		}
	}


	public void send(Object o) {
		if (o instanceof GameLogic) {
			GameLogic g = ((GameLogic) o).clone();
			List<Joueur> jList = g.getJoueurs();
			
			//mettre a null tous les sockets car il ne sont pas serializables, et on n'a pas besoin
			for(int i=0;i<jList.size();i++){
				jList.get(i).setSocket(null);
			}
			Send.sendData(g, ((GameLogic) o).getJoueurs());
		}
	}

	public void deconnection() {
		
	}

}
