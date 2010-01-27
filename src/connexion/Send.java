package connexion;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

import entities.Joueur;

public abstract class Send {

	public static void sendData(Object o, List<Joueur> jList) {
		ObjectOutputStream ooStream;
		OutputStream oStream;
		try {
			for (Joueur joueur : jList) {
				System.out.println(joueur);
				oStream = joueur.getSocket().getOutputStream();
				ooStream = new ObjectOutputStream(oStream);
				ooStream.writeObject(o);
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static void sendData(Object o, Socket s) {
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
