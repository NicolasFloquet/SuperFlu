package graphics;

import entities.*;
import java.util.ArrayList; 

public class ScreenManager {
	static ScreenManager instance = new ScreenManager();
	
	private int screen_height;
	private int screen_width;
	
	private Carte carte;
	private ArrayList<Joueur> joueurs;
	private ArrayList<Ville> villes;
	private ArrayList<Transfert> transferts;
	
	private ScreenManager()
	{
		
	}
	
	public static ScreenManager getInstance()
	{
		return instance;
	}
	
	public void draw()
	{
		carte.draw(0, 0, screen_height, screen_width);
		
		for(Ville ville : villes)
			ville.draw(0,0,1,1);
		
		for(Joueur joueur : joueurs)
			joueur.draw(0,0,1,1);
		
		for(Transfert transfert : transferts)
			transfert.draw(0, 0, 1, 1);
	}
}
