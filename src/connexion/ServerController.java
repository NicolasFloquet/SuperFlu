package connexion;

import logique.Application;
import logique.GameLogic;

public class ServerController implements ConnexionController {

	public void connect() {
		new Server();
	}

	public void send(Object o) {
		if (o instanceof GameLogic) {
			GameLogic g = ((GameLogic)o).clone();
			g.setServeur(false);
			
			new Send().sendData(g, Application.getInstance().getGame()
					.getJoueurs());
		}
	}

}
