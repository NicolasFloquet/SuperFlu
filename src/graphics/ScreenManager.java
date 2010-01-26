package graphics;

import entities.*;

import logique.GameLogic;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
//import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public class ScreenManager {
	static ScreenManager instance = new ScreenManager();
	
	TextureLoader textureLoader;
	
	private int screen_height;
	private int screen_width;
	private boolean fullscreen = false;
	
	private static long timerTicksPerSecond;
	/** The time at which the last rendering looped started from the point of view of the game logic */
	private long lastLoopTime;
	/** The time since the last record of fps */
	private long lastFpsTime = 0;
	/** The recorded fps */
	private int fps;
	
	private String WINDOW_TITLE = "4GI_flu";
	
	private GameLogic gameLogic = null;
	
	private ScreenManager()
	{
		screen_height = 600;
		screen_width = 800;
		fullscreen = false;
		
		timerTicksPerSecond = Sys.getTimerResolution();
		lastLoopTime = getTime();
		initialize();
	}
	
	public void setProperties(int height, int width, boolean is_fullscreen)
	{
		screen_height = height;
		screen_width = width;
		fullscreen = is_fullscreen; 
		
		try
		{
			Display.setFullscreen(fullscreen);
			
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			 
			GL11.glOrtho(0, screen_width, screen_height, 0, -1, 1);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();
			GL11.glViewport(0, 0, screen_width, screen_height);
		}
		catch (LWJGLException le)
		{
			System.out.println("Game exiting - exception in initialization:");
			le.printStackTrace();
			//Application.getApplication().quit();
			return;
		}
	}
	
	public static ScreenManager getInstance()
	{
		return instance;
	}
	
	public static long getTime() 
	{
		// we get the "timer ticks" from the high resolution timer
		// multiply by 1000 so our end result is in milliseconds
		// then divide by the number of ticks in a second giving
		// us a nice clear time in milliseconds
		return (Sys.getTime() * 1000) / timerTicksPerSecond;
	}
	
	public void draw()
	{
		Display.sync(60);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		long delta = getTime() - lastLoopTime;
		lastLoopTime = getTime();
		lastFpsTime += delta;
		fps++;
		
		// update our FPS counter if a second has passed
		if (lastFpsTime >= 1000)
		{
			Display.setTitle(WINDOW_TITLE + " (FPS: " + fps + ")");
			lastFpsTime = 0;
			fps = 0;
		}
		
		gameLogic.getCarte().draw(0, 0, screen_height, screen_width);
				
		for(Joueur joueur : gameLogic.getJoueurs())
		{
			joueur.draw(0,0,1,1);
		}
		
		for(Transfert transfert : gameLogic.getTransferts())
		{
			transfert.draw(0, 0, screen_height, screen_width);
		}
		
		Display.update();
	}
	
	/**
	* Initialize the common elements for the game
	*/
	public void initialize()
	{
		// initialize the window beforehand
		try
		{
			setDisplayMode();
			Display.setTitle(WINDOW_TITLE);
			Display.setFullscreen(fullscreen);
			Display.create();
			 
			// grab the mouse, don't want that hideous cursor when we're playing!
			//Mouse.setGrabbed(true);
			 
			// enable textures since we're going to use these for our sprites
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			 
			// disable the OpenGL depth test since we're rendering 2D graphics
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			 
			GL11.glOrtho(0, screen_width, screen_height, 0, -1, 1);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();
			GL11.glViewport(0, 0, screen_width, screen_height);
			 
			textureLoader = new TextureLoader();
		}
		catch (LWJGLException le)
		{
			System.out.println("Game exiting - exception in initialization:");
			le.printStackTrace();
			//Application.getApplication().quit();
			return;
		}
	}
	
	public static Sprite getSprite(String ref)
	{
		return new Sprite(getInstance().textureLoader,"ressources/"+ref);
	}
	
	/**
	* Sets the display mode for fullscreen mode
	*/
	private boolean setDisplayMode()
	{
		try
		{
			// get modes
			DisplayMode[] dm = org.lwjgl.util.Display.getAvailableDisplayModes(-1, -1, -1, -1, -1, -1, 60, 60);
			 
			org.lwjgl.util.Display.setDisplayMode(dm, new String[] {
				"width=" + screen_width,
				"height=" + screen_height,
				"freq=" + 60,
				"bpp=" + org.lwjgl.opengl.Display.getDisplayMode().getBitsPerPixel()
			});
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("Unable to enter fullscreen, continuing in windowed mode");
		}
		
		return false;
	}
	  
	public void setGameLogic(GameLogic gameLogic) {
		this.gameLogic = gameLogic; 
	}
	
}
