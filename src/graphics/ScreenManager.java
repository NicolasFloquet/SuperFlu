package graphics;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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
	
	/*
	 *  La parallelisation du chargement des textures ne fonctionne pas
	 *  Cette version s'execute en mode bloquant  
	 */
	private class Preloader extends Thread {
		public Preloader() {
			start();
		}

		/*public void start() {
			run();
		}*/
		
		public void run() {
			getSprite("aide.png");
			getSprite("aide2.png");
			getSprite("aide3.png");
			getSprite("aide4.png");
			getSprite("avion.png");
			getSprite("carte.png");
			getSprite("carte_afr.png");
			getSprite("carte_ams.png");
			getSprite("carte_asia.png");
			getSprite("carte_eur.png");
			getSprite("carte_indo.png");
			getSprite("carte_us.png");
			getSprite("credits.png");
			getSprite("error.png");
			getSprite("fond_carte.png");
			getSprite("fond_carte_danger.png");
			getSprite("gameover.png");
			getSprite("HL_usine.png");
			getSprite("HL_ville.png");
			getSprite("infected.png");
			getSprite("menu.png");
			getSprite("seringue.png");
			getSprite("usine.png");
			getSprite("victory.png");
			getSprite("ville.png");
			getSprite("lettres/0.png");
			getSprite("lettres/1.png");
			getSprite("lettres/2.png");
			getSprite("lettres/3.png");
			getSprite("lettres/4.png");
			getSprite("lettres/5.png");
			getSprite("lettres/6.png");
			getSprite("lettres/7.png");
			getSprite("lettres/8.png");
			getSprite("lettres/9.png");	
		}
	}
	
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
	
	private String WINDOW_TITLE = "SuperFlu";
	
	private Sprite map = null;
	
	private Ville selected_ville;
	private boolean transfertIsTraitement;

	private Application application;

	private Sprite[] dna;

	private static long threadId = -1;

	private static List<Runnable> actionsList = Collections.synchronizedList(new LinkedList<Runnable>());
	
	private ScreenManager()
	{
		screen_width = 1024;
		screen_height = 768;
		fullscreen = false;
		
		selected_ville = null;
		
		timerTicksPerSecond = Sys.getTimerResolution();
		lastLoopTime = getTime();
		
		application = Application.getInstance();
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
	
	public void drawKonami() {
		if(PlayerManager.getInstance().isKonami()) {
			getSprite("test.png").draw(screen_width/2, screen_height/2,0,1,1,1,1,0.5f);
		}
	}
	
	public void draw_error_connexion() {
		Display.sync(60);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
    	GL11.glColor3f(0.4f,0.4f,0.4f);
    	GL11.glBegin(GL11.GL_QUADS);
		{
	      GL11.glVertex2f(0,0);
	      GL11.glVertex2f(screen_width,0);
	      GL11.glVertex2f(screen_width,screen_height);
	      GL11.glVertex2f(0,screen_height);
		}
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		Sprite fond = getSprite("menu.png");
		Sprite message = getSprite("error.png");
		fond.draw(getOrigineCarteX()+fond.getWidth()/2, getOrigineCarteY()+fond.getHeight()/2,0,1,0.5f,0.5f,0.5f);
		message.draw(getOrigineCarteX()+fond.getWidth()/2, getOrigineCarteY()+fond.getHeight()/2);
		Display.update();
	}
	
	public void draw_aide(int page) {
		Display.sync(60);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
    	GL11.glColor3f(0.4f,0.4f,0.4f);
    	GL11.glBegin(GL11.GL_QUADS);
		{
	      GL11.glVertex2f(0,0);
	      GL11.glVertex2f(screen_width,0);
	      GL11.glVertex2f(screen_width,screen_height);
	      GL11.glVertex2f(0,screen_height);
		}
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		Sprite fond = getSprite("aide.png");
		
		switch (page) {
		case 1:
			fond = getSprite("aide.png");
			break;
		case 2:
			fond = getSprite("aide2.png");
			break;
		case 3:
			fond = getSprite("aide3.png");
			break;
		default:
			break;
		}
		fond.draw(getOrigineCarteX()+fond.getWidth()/2, getOrigineCarteY()+fond.getHeight()/2);
		
		drawKonami();
		
		Display.update();
	}
	
	public void draw_credits() {
		Display.sync(60);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
    	GL11.glColor3f(0.4f,0.4f,0.4f);
    	GL11.glBegin(GL11.GL_QUADS);
		{
	      GL11.glVertex2f(0,0);
	      GL11.glVertex2f(screen_width,0);
	      GL11.glVertex2f(screen_width,screen_height);
	      GL11.glVertex2f(0,screen_height);
		}
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		Sprite fond = getSprite("credits.png");
		fond.draw(getOrigineCarteX()+fond.getWidth()/2, getOrigineCarteY()+fond.getHeight()/2);
		dna[(int)((getTime()/80)%10)].draw(getOrigineCarteX()+710, getOrigineCarteY()+300);
		
		drawKonami();
		
		Display.update();
	}
	
	public void draw_menu() {
		Display.sync(60);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
    	GL11.glColor3f(0.4f,0.4f,0.4f);
    	GL11.glBegin(GL11.GL_QUADS);
		{
	      GL11.glVertex2f(0,0);
	      GL11.glVertex2f(screen_width,0);
	      GL11.glVertex2f(screen_width,screen_height);
	      GL11.glVertex2f(0,screen_height);
		}
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		Sprite fond = getSprite("menu.png");
		Sprite selected = getSprite("seringue.png");
		float transp = 0.85f + 0.5f*(float)Math.sin(2*Math.PI*((float)(getTime()%1000))/1000);
		fond.draw(getOrigineCarteX()+fond.getWidth()/2, getOrigineCarteY()+fond.getHeight()/2);
		int offset = 96*PlayerManager.getInstance().getSelectedMenu();
		selected.draw(getOrigineCarteX()+304, getOrigineCarteY()+288+offset, 0, 1, 1, 1, 1, transp);
		
		drawKonami();
		
		Display.update();
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
    	GL11.glColor3f(0.4f,0.4f,0.4f);
    	GL11.glBegin(GL11.GL_QUADS);
		{
	      GL11.glVertex2f(0,0);
	      GL11.glVertex2f(screen_width,0);
	      GL11.glVertex2f(screen_width,screen_height);
	      GL11.glVertex2f(0,screen_height);
		}
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);

		application.getGame().getCarte().draw();
		if (application.getJoueur() != null) {
			application.getJoueur().draw();
		}
		
		for(Transfert transfert : application.getGame().getTransferts())
		{
			synchronized (application.getGame()) {
				transfert.draw();
			}
		}
		
		if(selected_ville != null) {
			drawTransfertWidget(selected_ville, transfertIsTraitement);
		}

		switch(Application.getInstance().getGame().getEtat()) {
			case GAGNE :
				getSprite("victory.png").draw(getOrigineCarteX()+map.getWidth()/2, getOrigineCarteY()+map.getHeight()/2);
				break;
			case PERDU :
				getSprite("gameover.png").draw(getOrigineCarteX()+map.getWidth()/2, getOrigineCarteY()+map.getHeight()/2);
				break;
			default :
				break;
		}
		
		drawKonami();
		
		Display.update();
	}
	
	public void runNextAction() {
		if (actionsList.size() > 0) {
			Runnable r = actionsList.get(0);
			actionsList.remove(0);
			r.run();
			synchronized (this) {
				notify();
			}
		}
	}
	
	public static void invokeLater(Runnable r) {
		actionsList.add(r);
	}
	
	public static void invokeLaterAndWait(Runnable r) {
		actionsList.add(r);
		try {
			synchronized (ScreenManager.getInstance()) {
				ScreenManager.getInstance().wait();
			}
		} catch (InterruptedException e) {}
	}
	
	public static boolean isEventDispatchThread() {
		return threadId == -1 || threadId == Thread.currentThread().getId();
	}

	private void draw_loading() {
		Display.sync(60);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
    	GL11.glColor3f(0.0f,0.0f,0.0f);
    	GL11.glBegin(GL11.GL_QUADS);
		{
	      GL11.glVertex2f(0,0);
	      GL11.glVertex2f(screen_width,0);
	      GL11.glVertex2f(screen_width,screen_height);
	      GL11.glVertex2f(0,screen_height);
		}
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		Sprite fond = getSprite("loading.png");
		fond.draw(screen_width/2, screen_height/2);
		
		Display.update();
	}
	
	private void drawTransfertWidget(Ville selected_ville, boolean isTraitement) {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		
		// On dessine le chemin
		GL11.glPushMatrix();
		GL11.glTranslatef(0, 0, 0);
		
		if(isTraitement) {
			GL11.glColor3f(0.2f,0.2f,1.0f);
		} else {
			GL11.glColor3f(1.0f,0.2f,0.2f);
		}
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
		threadId = Thread.currentThread().getId();
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
		
		// Affichage d'un ecran de chargement
		draw_loading();
		
		// Chargement des textures
		dna = new Sprite[10];
		for(int i=0 ; i<10 ; i++) {
			dna[i]=getSprite("dna"+ (i+1) +".png");
		}
		
		new Preloader();
	}
	
	public static Sprite getSprite(String ref)
	{
		return Sprites.getInstance().getSprite(ref);
	}
	
	public int getOrigineCarteX() {
		return screen_width/2 - getMap().getWidth()/2;
	}
	
	public int getOrigineCarteY() {
		return 0;
	}
	
	public int getOrigineEncartY() {
		return getMap().getHeight();
	}
	
	/**
	* Sets the display mode for fullscreen mode
	*/
	private boolean setDisplayMode()
	{
		try
		{
			// get modes
			DisplayMode[] dm = org.lwjgl.util.Display.getAvailableDisplayModes(-1, -1, -1, -1, -1, -1, -1, -1);
			 
			System.out.println("Modes supportés");
			for(DisplayMode mode : dm) {
				System.out.println(mode);
			}
			
			screen_height = org.lwjgl.opengl.Display.getDisplayMode().getHeight();
			screen_width = org.lwjgl.opengl.Display.getDisplayMode().getWidth();
			
			org.lwjgl.util.Display.setDisplayMode(dm, new String[] {
				"width=" + screen_width,
				"height=" + screen_height,
				"freq=" + org.lwjgl.opengl.Display.getDisplayMode().getFrequency(),
				"bpp=" + org.lwjgl.opengl.Display.getDisplayMode().getBitsPerPixel()
			});
			Display.setFullscreen(true);
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
	
	public int getScreenWidth() {
		return screen_width;
	}

	public void setSelected(Ville v, boolean isTraitement) {
		selected_ville = v;
		transfertIsTraitement = isTraitement;
	}
}
