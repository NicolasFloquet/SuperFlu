package connexion;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

import entities.Joueur;

public abstract class Send {

	public static void sendData(Object o, List<Joueur> jList) {

		// System.out.println("send all: "+o);
		// TestConnexion.ecrire((GameLogic)o);

		ObjectOutputStream ooStream;
		OutputStream oStream;
		try {
			for (Joueur joueur : jList) {
				oStream = joueur.getSocket().getOutputStream();
				ooStream = new ObjectOutputStream(oStream);
				ooStream.writeObject(o);
			}

		} catch (SocketException se) {
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static void sendData(Object o, Socket s) {
		System.out.println("send: " + o);
		ObjectOutputStream ooStream;
		OutputStream oStream;
		try {
			oStream = s.getOutputStream();
			ooStream = new ObjectOutputStream(oStream);
			ooStream.writeObject(o);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
