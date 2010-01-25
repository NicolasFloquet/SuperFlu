import java.util.ArrayList;


public class GameLogic {
	private static GameLogic instance = null;
	
	private ArrayList<Joueur> joueurs;			// Liste des joueurs de la partie
	private ArrayList<Transfert> transferts;	// Liste des transferts en cours
	private Carte carte;
	
	
	private GameLogic(){
		joueurs = new ArrayList<Joueur>();
		
		transferts = new ArrayList<Transfert>();
		
	}	
	
	public static GameLogic getInstance(){
		if(instance == null){
			instance = new GameLogic();
		}
		return instance;
	}
	
	public void Update(){
		
	}

}
