package connexion;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	public final static String SERVER_HOSTNAME = "localhost";
	public final static int COMM_PORT = 5050; // socket port for client comms

	Socket socket;
	int iterator = 0;

	/** Default constructor. */
	public Client() {
		try {
			this.socket = new Socket(SERVER_HOSTNAME, COMM_PORT);
		} catch (UnknownHostException uhe) {
			uhe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public Socket getSocket() {
		return this.socket;
	}

	/**
	 * Main test
	 */
	/*
	public static void main(String[] args) {
		Receive rec = new Receive();
		Socket s = new Client().getSocket();
		Personne personne = (Personne) rec.getData(s);
		// ///////// //
		// Affichage //
		// ///////// //
		System.out.println("Personne : ");
		System.out.println("nom : " + personne.getNom());
		System.out.println("prenom : " + personne.getPrenom());
		System.out.println("taille : " + personne.getTaille());
		System.out.println("Hobbies:");
		ArrayList<Hobby> liste = personne.getHobbies();
		for (int i = 0; i < liste.size(); i++) {
			System.out.println("  > " + liste.get(i));
		}
		
		personne.setNom("Bien");
		personne.setPrenom("reçu");
		new Send(s).sendData(personne);
	}*/
}