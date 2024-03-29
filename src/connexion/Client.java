package connexion;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

import entities.Joueur;

public class Client {
	private final static int COMM_PORT = 5050; // socket port for client comms

	private Socket socket;
	
	/** Default constructor. 
	 * @throws Exception */
	public Client(String ip, String pseudo) throws ConnectException {
		try {
			this.socket = new Socket(ip, COMM_PORT);
			
			Send.sendData(new Joueur(pseudo), socket);
		} catch (ConnectException ce){
			System.err.println("Le serveur est deconnecte ou il y a deja 6 joueurs");
			throw ce;
		} catch (UnknownHostException uhe) {
			uhe.printStackTrace();
			throw new ConnectException();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			throw new ConnectException();
		}
	}

	public Socket getSocket() {
		return this.socket;
	}

	/**
	 * Main test
	 */
	/*
	 * @SuppressWarnings("deprecation") public static void main(String[] args) {
	 * 
	 * Socket s = new Client().getSocket(); Receive rec = new Receive(s);
	 * rec.start();
	 * 
	 * Personne personne = (Personne) rec.getData(); // ///////// // //
	 * Affichage // // ///////// // System.out.println("Personne : ");
	 * System.out.println("nom : " + personne.getNom());
	 * System.out.println("prenom : " + personne.getPrenom());
	 * System.out.println("taille : " + personne.getTaille());
	 * System.out.println("Hobbies:"); ArrayList<Hobby> liste =
	 * personne.getHobbies(); for (int i = 0; i < liste.size(); i++) {
	 * System.out.println("  > " + liste.get(i)); }
	 * 
	 * personne.setNom("Bien"); personne.setPrenom("re�u"); new
	 * Send(s).sendData(personne);
	 * 
	 * rec.close(); rec.stop(); try { s.close(); } catch (IOException e) {
	 * e.printStackTrace(); System.exit(1); } }
	 */
}