package connexion;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import entities.Joueur;

public class Send {

	public void sendData(Object o, List<Joueur> jList) {
		ObjectOutputStream ooStream;
		OutputStream oStream;
		try {
			for (int i = 0; i < jList.size(); i++) {
				oStream = jList.get(i).getSocket().getOutputStream();
				ooStream = new ObjectOutputStream(oStream);
				ooStream.writeObject(o);
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void sendData(Object o, Socket s) {
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
