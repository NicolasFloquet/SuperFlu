package connexion;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Server{
	private final int COMM_PORT = 5050; // socket port for client comms

	private ServerSocket serverSocket;
	private Socket socket;
	private final int MAX_PLAYER = 6; 

	/** Default constructor. */
	public Server() {

		initServerSocket();
		try {
			for(int i = 0;i<MAX_PLAYER;i++){
				// listen for and accept a client connection to serverSocket
				//pour chaque socket accepte cree un nouveau thread
				socket = serverSocket.accept();
				new ThreadServ(socket).start();
			}
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
	
	public Socket getSocket(){
		return this.socket;
	}

	/**
	 * Lance un nouveau serveur
	 */
	public static void main(String[] args) {
		new Server();
	}
}