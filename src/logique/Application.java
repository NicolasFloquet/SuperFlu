package logique;

import java.util.Timer;
import java.util.TimerTask;


import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import graphics.ScreenManager;

public class Application
{
	/* Periode du timer */
	private final static int TIMER_PERIOD = 200;
	
	private class UpdateTask extends TimerTask {
		
		public void run() {
			// TODO Auto-generated method stub
			
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
		this.timer.scheduleAtFixedRate(this.game, 0, TIMER_PERIOD);
	}
	
}
