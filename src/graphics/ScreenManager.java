package graphics;

import java.util.ArrayList;

public class ScreenManager {
	static ScreenManager instance = new ScreenManager();
	
	private int screen_height;
	private int screen_width;
	
	private Drawable carte;
	private ArrayList<Drawable> joueurs;
	private ArrayList<Drawable> villes;
	
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
		
		for(Drawable ville : villes)
			ville.draw(0,0,1,1);
		
		for(Drawable joueur : joueurs)
			joueur.draw(0,0,1,1);
	}
}
