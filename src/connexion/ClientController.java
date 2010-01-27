package connexion;

import java.net.Socket;

import entities.Transfert;

public class ClientController implements ConnexionController {

	Socket s;

	public void connect() {
		s = new Client().getSocket();
		new ThreadClient(s).start();
	}

	public void send(Object o) {
		if (o instanceof Transfert) {
			Send.sendData(o, s);
		}
	}

	@Override
	public void deconnection() {
		// TODO Auto-generated method stub
		
	}
}
