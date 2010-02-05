package connexion;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;

import entities.Joueur;

public abstract class Send {

	public static void sendData(final Object o, List<Joueur> jList) {

		// System.out.println("send all: "+o);
		// TestConnexion.ecrire((GameLogic)o);
		//System.out.println("send: "+((GameLogic)o).getCarte());

		Iterator<Joueur> it = jList.iterator();
		while(it.hasNext()) {
			final Joueur joueur = it.next();
			new Thread(new Runnable() {
				public void run() {
					try {				
						OutputStream oStream;
						ObjectOutputStream ooStream;
						oStream = joueur.getSocket().getOutputStream();
						ooStream = new ObjectOutputStream(oStream);
						ooStream.writeObject(o);
					} catch(ConcurrentModificationException cme){
						//TODO <- Reviser catch
						System.err.println("catch");
						cme.printStackTrace();
						/*try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}finally{
							sendData(o,jList);
						}*/
					}catch (SocketException se) {
						se.printStackTrace();
					} catch (IOException e) {
						System.err.println("cathc 2");
						e.printStackTrace();
						System.exit(1);
					}
				}
			}).start();

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
