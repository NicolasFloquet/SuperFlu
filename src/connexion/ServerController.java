package connexion;

import logique.Application;
import logique.GameLogic;

public class ServerController implements ConnexionController {

	public void connect() {
		new Server();
	}

	public void send(Object o) {
		if (o instanceof GameLogic) {
			new Send().sendData(o, Application.getInstance().getGame()
					.getJoueurs());
		}
	}

}
