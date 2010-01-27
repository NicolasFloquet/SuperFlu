package connexion;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.concurrent.Semaphore;

public class Receive extends Thread {
	public final static String SERVER_HOSTNAME = "localhost";
	public final static int COMM_PORT = 5050; // socket port for client comms

	private Socket socket;
	private Object o;
	private Semaphore sem = new Semaphore(0);
	private boolean aborting = false;

	public Receive(Socket socket) {
		this.socket = socket;
	}

	void close() {
		aborting = true;
	}

	public void run() {
		InputStream iStream = null;
		ObjectInputStream oiStream;
		while (!aborting) {
			try {
				iStream = socket.getInputStream();
				oiStream = new ObjectInputStream(iStream);
				o = oiStream.readObject();
				sem.tryAcquire();
				sem.release();
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}

	public Object getData() {
		try {
			sem.acquire();
			sem.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return o;
	}

	public Object getDataBlock() {
		InputStream iStream = null;
		ObjectInputStream oiStream;
		try {
			iStream = socket.getInputStream();
			oiStream = new ObjectInputStream(iStream);
			o = oiStream.readObject();
		} catch (IOException e) {
			System.err.println("error:");
			e.printStackTrace();
			System.exit(1);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return o;
	}
}