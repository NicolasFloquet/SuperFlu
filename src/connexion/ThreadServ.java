package connexion;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;


public class ThreadServ extends Thread{

	Socket s;
	
	
	public ThreadServ(Socket s) {
		this.s = s;
		System.out.println("new thread");
	}


	@SuppressWarnings("deprecation")
	public void run(){
		
		//faire les fonctions de serveur
		
		/*
		Personne personne = new Personne("Benito", "Camelas", 180);
		
		Send send = new Send(s);
		send.sendData(personne);
		send.sendData(personne);
		
		Receive rec = new Receive(s);
		rec.start();
		personne = (Personne) rec.getData();
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
