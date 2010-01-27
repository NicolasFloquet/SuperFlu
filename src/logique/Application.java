package logique;

import java.util.Timer;
import java.util.TimerTask;


import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import connexion.ClientController;
import connexion.ServerController;
import entities.Joueur;

import graphics.ScreenManager;

public class Application
{
	/* Periode du timer */
	private final static int TIMER_PERIOD = 100;

	private class UpdateTask extends TimerTask {

		public void run() {
			if (isServer) {
				game.updateServeur(1);
				//serverController.send(game);
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
	private ServerController serverController;	
	private ClientController clientController;

	private Application()
	{
		screen = ScreenManager.getInstance();
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

	public void run(String[] args)
	{	
		if (args.length > 0) {
			isServer = Boolean.valueOf(args[0]);
		}
		
		if (args.length == 4) {
			screen.setProperties(Integer.valueOf(args[1]), Integer.valueOf(args[2]), Boolean.valueOf(args[3]));
		}
		
		if (isServer) {
			serverController = new ServerController();
			//serverController.connect();
			timer = new Timer();
			timer.scheduleAtFixedRate(new UpdateTask(), 0, TIMER_PERIOD);
		} else {
			clientController = new ClientController();
			clientController.connect();
		}

		/** TEST **/
		game.creerTransfert(game.getCarte().getZones().get(0).getVilles().get(0),
				game.getCarte().getZones().get(0).getVilles().get(1), 
				null);

		game.creerTransfert(game.getCarte().getZones().get(0).getVilles().get(1),
				game.getCarte().getZones().get(0).getVilles().get(2), 
				null);

		running = true;
		while(running)
		{
			screen.draw();

			if((Display.isCloseRequested()) || (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)))
			{
				running = false;
			}
		}
		
		if (isServer) {
			timer.cancel();
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

}
