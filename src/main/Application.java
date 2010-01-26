package main;
import java.awt.im.InputContext;
import java.util.ArrayList;

import logique.GameLogic;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import graphics.ScreenManager;
import entities.*;

public class Application
{
	private static Application instance = new Application(); 
	private ScreenManager screen;
	private GameLogic game;
	
	private boolean running;

	private Application()
	{
		screen = ScreenManager.getInstance();
		game = GameLogic.getInstance();
		running = false;
	}
	
	public static Application getInstance()
	{
		return instance;
	}
	
	public void run(String[] args)
	{
		screen.setGameLogic(GameLogic.getInstance());
		
		while(running)
		{
			screen.draw();
			
			if((Display.isCloseRequested()) || (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)))
			{
				System.out.println("pif");
				running = false;
			}
			
			System.out.println("paf");
		}
	}
	
	public GameLogic getGame() {
		return game;
	}
}
