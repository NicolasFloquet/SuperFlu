package graphics;

import entities.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import logique.Application;
import logique.PlayerManager;

public class ScreenManager {
	static ScreenManager instance = new ScreenManager();
	
	TextureLoader textureLoader;
	
	private int screen_height;
	private int screen_width;
	private boolean fullscreen;
	
	private static long timerTicksPerSecond;
	/** The time at which the last rendering looped started from the point of view of the game logic */
	private long lastLoopTime;
	/** The time since the last record of fps */
	private long lastFpsTime = 0;
	/** The recorded fps */
	private int fps;
	
	private String WINDOW_TITLE = "4GI_flu";
	
	private Sprite map = null;
	
	private Ville selected_ville;
	
	private ScreenManager()
	{
		screen_width = 800;
		screen_height = 600;
		fullscreen = false;
		
		selected_ville = null;
		
		timerTicksPerSecond = Sys.getTimerResolution();
		lastLoopTime = getTime();
		initialize();
	}
	
	public void setProperties(int width, int height, boolean is_fullscreen)
	{
		screen_height = height;
		screen_width = width;
		fullscreen = is_fullscreen; 
		
		try
		{
			setDisplayMode();
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
	
	public Sprite getMap() {
		if (map == null) {
			map = getSprite("carte.png");
		}
		return map;
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
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
    	GL11.glColor3f(1,1,1);
    	GL11.glBegin(GL11.GL_QUADS);
		{
	      GL11.glVertex2f(0,0);
	      GL11.glVertex2f(screen_width,0);
	      GL11.glVertex2f(screen_width,screen_height);
	      GL11.glVertex2f(0,screen_height);
		}
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		Application.getInstance().getGame().getCarte().draw();
		
		for(Joueur joueur : Application.getInstance().getGame().getJoueurs())
		{
			joueur.draw();
		}
		
		for(Transfert transfert : Application.getInstance().getGame().getTransferts())
		{
			transfert.draw();
		}
		
		if(selected_ville != null) {
			drawTransfertWidget(selected_ville);
		}

		Display.update();
	}

	private void drawTransfertWidget(Ville selected_ville) {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		
		// On dessine le chemin
		GL11.glPushMatrix();
		GL11.glTranslatef(0, 0, 0);
    	GL11.glColor3f(0.3f,0.4f,0.5f);
		GL11.glLineWidth(5);
    	GL11.glBegin(GL11.GL_LINES);
		{
	      GL11.glVertex2f(getOrigineCarteX()+selected_ville.getX(),
	    		  			getOrigineCarteY()+selected_ville.getY());
	      GL11.glVertex2f(Mouse.getX(),screen_height-Mouse.getY());
		}
		GL11.glEnd();
		GL11.glPopMatrix();
		
		// On dessine l'anneau
		GL11.glPushMatrix();
		GL11.glTranslatef(getOrigineCarteX()+selected_ville.getX(),
							getOrigineCarteY()+selected_ville.getY(), 0);
    	GL11.glColor3f(0.3f,0.4f,0.5f);
		GL11.glLineWidth(3);
    	GL11.glBegin(GL11.GL_LINES);
		{
			float pourcentage = (float)PlayerManager.getInstance().getPourcentageVoulu()/100.0f;
			float old_dx = 20.0f;
			float old_dy = 0.0f;
			for(int i=1 ; i<=32 ; i++)
			{
				float dx = (float)(20*Math.cos(pourcentage*Math.PI*i/16.0f));
				float dy = (float)(20*Math.sin(pourcentage*Math.PI*i/16.0f));
				
				GL11.glVertex2f(old_dx, old_dy);
				GL11.glVertex2f(dx, dy);
				
				old_dx = dx;
				old_dy = dy;
			}
		}
		GL11.glEnd();
		GL11.glPopMatrix();
		// On dessine la cible
		Ville target = PlayerManager.getInstance().getTargetedVille();
		if(target != null && target != selected_ville) {
			GL11.glPushMatrix();
			GL11.glTranslatef(getOrigineCarteX()+target.getX(), getOrigineCarteY()+target.getY(), 0);
	    	GL11.glColor3f(0.3f,0.4f,0.5f);
			GL11.glLineWidth(3);
	    	GL11.glBegin(GL11.GL_LINE_LOOP);
			{
					GL11.glVertex2f(0, 20);
					GL11.glVertex2f(20, 0);
					GL11.glVertex2f(0, -20);
					GL11.glVertex2f(-20, 0);
			}
			GL11.glEnd();
			GL11.glPopMatrix();
		}
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
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
			
			GL11.glEnable(GL11.GL_BLEND) ;
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA) ;
			
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
	
	public int getOrigineCarteX() {
		return screen_width/2 - map.getWidth()/2;
	}
	
	public int getOrigineCarteY() {
		return 0;
	}
	
	public int getEncartHeight() {
		return screen_height-map.getHeight();
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

	public int getScreenHeight() {
		return screen_height;
	}

	public void setSelected(Ville v) {
		selected_ville = v;
	}
}
