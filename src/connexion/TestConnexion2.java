package connexion;

import logique.Application;

public class TestConnexion2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		ConnexionController c = new ClientController();
		c.connect();

		try {
			Thread.sleep(50000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("client");
		Application a = Application.getInstance();
		TestConnexion.ecrire(a.getGame());
		System.out.println("\n\n\n");

		System.out.println("fin");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
