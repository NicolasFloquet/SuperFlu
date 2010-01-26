package logique;
import java.awt.im.InputContext;
import java.util.ArrayList;


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
	
	public boolean isRunning()
	{
		return running;
	}
	
	public void run(String[] args)
	{
		screen.setGameLogic(GameLogic.getInstance());
		
		running = true;
		while(running)
		{
			screen.draw();
			
			if((Display.isCloseRequested()) || (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)))
			{
				running = false;
			}
		}
	}
}
