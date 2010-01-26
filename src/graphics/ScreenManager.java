package graphics;

import entities.*;
import java.util.ArrayList; 

import org.lwjgl.LWJGLException;
//import org.lwjgl.Sys;
import org.lwjgl.examples.spaceinvaders.TextureLoader;
//import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public class ScreenManager {
	static ScreenManager instance = new ScreenManager();
	
	TextureLoader textureLoader;
	
	private static int screen_height = 800;
	private static int screen_width = 600;
	private static boolean fullscreen = false;
	
	private String WINDOW_TITLE = "4GI_flu";
	
	private Carte carte;
	private ArrayList<Joueur> joueurs;
	private ArrayList<Transfert> transferts;
	
	private ScreenManager()
	{
		initialize();
	}
	
	public static void setProperties(int height, int width, boolean is_fullscreen)
	{
		screen_height = height;
		screen_width = width;
		fullscreen = is_fullscreen; 
		
		if(instance != null)
			getInstance().initialize();
		else
			getInstance();
	}
	
	public static ScreenManager getInstance()
	{
		return instance;
	}
	
	public void draw()
	{
		carte.draw(0, 0, screen_height, screen_width);
				
		for(Joueur joueur : joueurs)
		{
			joueur.draw(0,0,1,1);
		}
		
		for(Transfert transfert : transferts)
		{
			transfert.draw(0, 0, screen_height, screen_width);
		}
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
	
	public TextureLoader getTextureLoader()
	{
		return textureLoader;
	}
	
	/**
	* Sets the display mode for fullscreen mode
	*/
	private boolean setDisplayMode()
	{
		try
		{
			// get modes
			DisplayMode[] dm = org.lwjgl.util.Display.getAvailableDisplayModes(screen_width, screen_height, -1, -1, -1, -1, 60, 60);
			 
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
	  
	public void setCarte(Carte carte)
	{
		this.carte = carte;
	}
	
	public void setJoueurs(ArrayList<Joueur> joueurs)
	{
		this.joueurs = joueurs;
	}
	
	public void setTransferts(ArrayList<Transfert> transferts)
	{
		this.transferts = transferts;
	}
	
}
