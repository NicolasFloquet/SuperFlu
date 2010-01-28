package connexion;

import java.net.Socket;

import entities.Transfert;

public class ClientController implements ConnexionController {

	Socket s;
	String ip="localhost";

	public void connect() {
		s = new Client(ip).getSocket();
		new ThreadClient(s).start();
	}

	public void send(Object o) {
		if (o instanceof Transfert) {
			Send.sendData(o, s);
		}
	}
	
	public void setIP(String ip){
		this.ip = ip;
	}

	@Override
	public void deconnection() {
		// TODO Auto-generated method stub
		
	}
}
