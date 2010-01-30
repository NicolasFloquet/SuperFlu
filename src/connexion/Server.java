package connexion;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import logique.Application;

public class Server extends Thread{
	private final int COMM_PORT = 5050; // socket port for client comms

	private ServerSocket serverSocket;
	private ArrayList<Socket> socketList = new ArrayList<Socket>();
	private int nbjoueurs = 6;
	private boolean running = true;

	public Server(int nbjoueurs){
		System.out.println(nbjoueurs+" joueurs");
		this.nbjoueurs = nbjoueurs;
	}
	
	public void run() {

		initServerSocket();
		try {
			for (int i = 0; (i < nbjoueurs) && running; i++) {
				// listen for and accept a client connection to serverSocket
				// pour chaque socket accepte cree un nouveau thread
				socketList.add(serverSocket.accept());
			}
			Application.getInstance().startGame();
			System.out.println("creer les threads: "+socketList.size());
			for (Socket s:socketList){
				new ThreadServ(s,nbjoueurs).start();
			}
			serverSocket.close();
		} catch (SecurityException se) {
			se.printStackTrace();
			System.exit(1);
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.exit(1);
		}
	}

	/** Initialize a server socket for communicating with the client. */
	private void initServerSocket() {
		try {
			this.serverSocket = new java.net.ServerSocket(COMM_PORT);
			assert this.serverSocket.isBound();
			if (this.serverSocket.isBound()) {
				System.out.println("SERVER inbound data port "
						+ this.serverSocket.getLocalPort()
						+ " is ready and waiting for client to connect...");
			}
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.exit(1);
		}
	}

	public ArrayList<Socket> getSocketList() {
		return this.socketList;
	}
	
	public void deconnecte() {
		running = false;
	}

	/**
	 * Lance un nouveau serveur
	 */
	public static void main(String[] args) {
		new Server(6);
	}
}