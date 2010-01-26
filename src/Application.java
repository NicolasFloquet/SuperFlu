import java.util.ArrayList;

import logique.GameLogic;

import org.lwjgl.opengl.Display;

import graphics.ScreenManager;
import entities.*;

public class Application
{
	private static Application instance = new Application(); 
	private ScreenManager screen;
	private GameLogic game;
	
	private Application()
	{
		screen = ScreenManager.getInstance();
		game = GameLogic.getInstance();
	}
	
	public static Application getInstance()
	{
		return instance;
	}
	
	public void run(String[] args)
	{
		Carte carte = new Carte();
		ArrayList<Joueur> joueurs = new ArrayList<Joueur>();
		ArrayList<Transfert> transferts = new ArrayList<Transfert>();

		screen.setGameLogic(GameLogic.getInstance());
		
		while(!Display.isCloseRequested())
		{
			screen.draw();
		}
	}
}
