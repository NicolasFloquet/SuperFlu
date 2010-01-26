package connexion;

import java.net.Socket;
import logique.*;

public class ServerController implements ConnexionController{

	public void connect(){
		new Server();
		//new qui s'ocuppe d'envoyer tout l'info a chaque x milisec????
	}
	
	public void send(Object o){
		if(o instanceof GameLogic){
			new Send().sendData(o, Application.getInstance().getGame().getJoueurs());
		}
	}
	
}
