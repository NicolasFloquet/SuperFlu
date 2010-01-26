import java.io.IOException;
import java.net.Socket;


public class ThreadServ extends Thread{

	Socket s;
	
	
	public ThreadServ(Socket s) {
		this.s = s;
		System.out.println("new thread");
	}


	public void run(){
		
		//faire les fonctions de serveur
		/*
		Personne personne = new Personne("Benito", "Camelas", 180);
		new Send(s).sendData(personne);
		
		Receive rec = new Receive();
		personne = (Personne) rec.getData(s);
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
		}*/
		try {
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
