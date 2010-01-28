package logique;

import java.util.Timer;
import java.util.TimerTask;

import music.MusicPlayer;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import connexion.ClientController;
import connexion.ConnexionController;
import connexion.ServerController;
import entities.Joueur;
import entities.Transfert;
import entities.Zone;

import graphics.ScreenManager;

public class Application
{
	/* Periode du timer */
	private final static int TIMER_PERIOD = 200;
	private int zone = -1;
	private class UpdateTask extends TimerTask {

		public void run() {
			if (isServer) {
				game.updateServeur(10);
				c.send(game);
			} else {
				game.updateClient(1);
			}
			
			if(game.getEtat()!=GameLogic.etatJeu.EN_COURS) {
				this.cancel();
			}
				
		}
	}

	private static Application instance = new Application(); 
	private ScreenManager screen;
	private GameLogic game;
	private Joueur joueur;

	private boolean isServer = true;
	private boolean running;
	private Timer timer;
	private ConnexionController c;

	private Application()
	{
		game = new GameLogic();
		game.creerEpidemie();
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

	public void quit()
	{
		running = false;

		//TODO : permettre de tuer le thread qui accept sur le socket pour pouvoir fermer proprement
		System.exit(0);
	}

	public void run(String[] args)
	{	
		if (args.length > 0) {
			isServer = Boolean.valueOf(args[0]);
		}

		if (!isServer) {
			screen = ScreenManager.getInstance();
			screen.initialize();
			if (args.length == 4) {
				screen.setProperties(Integer.valueOf(args[1]), Integer.valueOf(args[2]), Boolean.valueOf(args[3]));
			}
		}

		timer = new Timer();
		if (isServer) {
			c = new ServerController();
			c.connect();
			timer.scheduleAtFixedRate(new UpdateTask(), 0, TIMER_PERIOD);
		} else {
			c = new ClientController();
			c.connect();
			timer.scheduleAtFixedRate(new UpdateTask(), 0, TIMER_PERIOD/10);
		}

		boolean pandemic = false;
		MusicPlayer player = null;
		
		if(!isServer) {
			player = new MusicPlayer();
			player.start();
		}
		
		running = true;
		if (isServer) {
			while(running && game.getEtat()==GameLogic.etatJeu.EN_COURS) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {}
			}
		}
		else {
			while(running && game.getEtat()==GameLogic.etatJeu.EN_COURS)
			{
				screen.draw();
				// Gestion des inputs
				PlayerManager.getInstance().update();
		
				// On detecte si on vient d'atteindre l'etat pandemique 
				if(game.isPandemic() && !pandemic) {
					pandemic = true;
					player.goPandemic();
				}
				else if(!game.isPandemic() && pandemic) {
					pandemic = false;
					player.backNormal();
				}
				
				if((Display.isCloseRequested()) || (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)))
				{
					quit();
				}
			}
		}

		if (isServer) {
			timer.cancel();
			c.deconnection();
		}
		else {
			player.quit();
		}
	}

	public GameLogic getGame() {
		return game;
	}

	public void setGame(GameLogic game) {
		this.game = game;
	}

	public boolean isServeur() {
		return isServer;
	}

	public void setJoueur(Joueur joueur) {
		this.joueur = joueur;
	}

	public Joueur getJoueur() {
		return joueur;
	}

	public void sendTransfert(Transfert t){
		c.send(t);
	}

	public Zone getNextZone(){
		zone++;
		return game.getCarte().getZones().get(zone);
	}
}
