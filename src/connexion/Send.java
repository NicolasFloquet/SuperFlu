package connexion;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ConcurrentModificationException;
import java.util.List;

import logique.GameLogic;

import entities.Joueur;

public abstract class Send {

	public static void sendData(Object o, List<Joueur> jList) {

		// System.out.println("send all: "+o);
		// TestConnexion.ecrire((GameLogic)o);
		//System.out.println("send: "+((GameLogic)o).getCarte());

		ObjectOutputStream ooStream;
		OutputStream oStream;
		try {
			for (Joueur joueur : jList) {
				oStream = joueur.getSocket().getOutputStream();
				ooStream = new ObjectOutputStream(oStream);
				ooStream.writeObject(o);
			}

		} catch(ConcurrentModificationException cme){
			//TODO <- Reviser catch
			System.err.println("catch");
			//cme.printStackTrace();
			/*try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}finally{
				sendData(o,jList);
			}*/
		}catch (SocketException se) {
		} catch (IOException e) {
			System.err.println("cathc 2");
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static void sendData(Object o, Socket s) {
		//System.out.println("send: " + o);
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
