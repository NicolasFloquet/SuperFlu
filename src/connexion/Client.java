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
			System.exit(1);
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.exit(1);
		}
	}
	
	public Socket getSocket() {
		return this.socket;
	}

	/**
	 * Main test
	 */
	/*
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		
		Socket s = new Client().getSocket();
		Receive rec = new Receive(s);
		rec.start();
		
		Personne personne = (Personne) rec.getData();
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
		
		rec.close();
		rec.stop();
		try {
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}*/
}