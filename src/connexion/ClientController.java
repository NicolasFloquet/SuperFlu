package connexion;

import java.net.ConnectException;
import java.net.Socket;

import entities.Transfert;

public class ClientController implements ConnexionController {

	Socket s;
	String ip="localhost";

	public boolean connect() {
		try {
			s = new Client(ip).getSocket();
			new ThreadClient(s).start();
			return true;
		}
		catch(ConnectException e) {
			return false;
		}
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
