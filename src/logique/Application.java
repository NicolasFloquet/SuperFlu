package logique;

import java.util.Timer;
import java.util.TimerTask;


import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import graphics.ScreenManager;

public class Application
{
	/* Periode du timer */
	private final static int TIMER_PERIOD = 100;
	
	private class UpdateTask extends TimerTask {
		
		public void run() {
		game.updateServeur(1);
			// TODO : réseau
		}
	}
	
	private static Application instance = new Application(); 
	private ScreenManager screen;
	private GameLogic game;
	
	private boolean running;
	private Timer timer;	

	
	private Application()
	{
		screen = ScreenManager.getInstance();
		game = new GameLogic();
		game.creerEpidemie();
		running = false;
		timer = new Timer();
		timer.scheduleAtFixedRate(new UpdateTask(), 0, TIMER_PERIOD);
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
		screen.setGameLogic(game);
		
		if (args.length == 3) {
			screen.setProperties(Integer.valueOf(args[0]), Integer.valueOf(args[1]), Boolean.valueOf(args[2]));
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
		timer.cancel();

	}
	
	public GameLogic getGame() {
		return game;
	}

	public void setGame(GameLogic game) {
		this.game = game;
	}
	
}
