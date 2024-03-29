package logique;

import java.net.Socket;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import music.MusicPlayer;

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
	/* Période du timer */
	private final static int TIMER_PERIOD = 200;
	private int zone = -1;
	private class UpdateTask extends TimerTask {

		public void run() {
			if (isServer) {
				synchronized (game) {
					game.updateServeur(10);
					c.send(game);
				}
			} else {
				game.updateClient(1);
			}
			
			if(game.getEtat()!=GameLogic.EtatJeu.EN_COURS) {
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
	private Timer timer = new Timer();
	private ConnexionController c;
	private int nbjoueurs = 6;
	private int deconnectes = 0;
	private String ip;
	private String pseudo = "";
	private MusicPlayer player;
	private boolean pandemic;
	private boolean started = false;

	private Application()
	{
		game = new GameLogic();
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
	
	public void JoueurDeconnecte(Socket s){
		deconnectes++;
		if(deconnectes >= nbjoueurs){
			instance = new Application();
			String[] args = new String[2];
			args[0] = "true";
			args[1] = ""+nbjoueurs;
			instance.run(args);
			this.quit();
		}else{
			List<Zone> zList = null;
			List<Joueur> jList = getGame().getJoueurs();
			for(Joueur j: jList){
				zList = j.getZones();
				if(j.getSocket() == s){
					jList.remove(j);
					break;
				}
			}
			int minInfectee = Integer.MAX_VALUE;
			Joueur minInfect = null;
			for(Zone z:zList){
				minInfect = null;
				minInfectee = Integer.MAX_VALUE;
				for(Joueur j:jList){
					int infectee = 0;
					for(Zone zj:j.getZones()){
						infectee += zj.getPopulation_infectee();
					}
					if(infectee < minInfectee){
						minInfectee = infectee;
						minInfect = j;
					}
				}
				if (minInfect != null) {
					minInfect.getZones().add(z);
				}
			}
		}
	}

	public void run(String[] args)
	{	
		nbjoueurs = 6;
		if (args.length > 0) {
			isServer = Boolean.valueOf(args[0]);
			if(isServer && args.length > 1){
				nbjoueurs = Integer.valueOf(args[1]);
			}
		}
		
		ip = "localhost";
		

		if (isServer) {
			c = new ServerController();
			((ServerController)c).setNbjoueurs(nbjoueurs);
			c.connect();

			running = true;
			while(running && (game.getEtat()==GameLogic.EtatJeu.EN_COURS || game.getEtat()==GameLogic.EtatJeu.WAIT)) {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {}
			}

			timer.cancel();
			c.deconnection();
		} else {
			if(args.length>0){
				ip = args[0];
			}
			if (args.length > 1) {
				pseudo = args[1];
			}
			screen = ScreenManager.getInstance();
			screen.initialize();
			if (args.length == 5) {
				screen.setProperties(Integer.valueOf(args[2]), Integer.valueOf(args[3]), Boolean.valueOf(args[4]));
			}	
			
			afficheMenu();
			boucleJeu();
			
			player.quit();

			int ret = 0;
			while(running && ret==0) { 
				screen.draw();
				ret = PlayerManager.getInstance().update_submenu(0);
			}
			quit();
		}
	}

	private void boucleJeu() {
		pandemic = false;

		player = new MusicPlayer();
		player.start();
		//
		// Boucle du jeu
		//
		while(running && (game.getEtat()==GameLogic.EtatJeu.EN_COURS || game.getEtat()==GameLogic.EtatJeu.WAIT))
		{
			screen.runNextAction();
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
			
			if(Display.isCloseRequested())
			{
				quit();
			}
		}
	}

	private void afficheMenu() {
		boolean connected = false;
		while(!connected) {
			int menu_type = -1;
			//
			// Boucle du menu
			//
			running = true;
			while(running && menu_type!=0) {
				
				screen.runNextAction();
				
				switch(menu_type) {
					case 1 :
					case 2 :
					case 3 :
						screen.draw_aide(menu_type);
						menu_type = PlayerManager.getInstance().update_submenu(menu_type);
						break;
					case 4 :
						screen.draw_credits();
						menu_type = PlayerManager.getInstance().update_submenu(menu_type);
						break;
					default :
						screen.draw_menu();
						menu_type = PlayerManager.getInstance().update_menu();
						break;
				}
				if((Display.isCloseRequested()))
				{
					quit();
				}
			}
			
			c = new ClientController(ip, pseudo);
			if(c.connect()) {
				connected = true;
			}
			else {
				int ret = 0;
				while(running && ret==0) { 
					screen.draw_error_connexion();
					ret = PlayerManager.getInstance().update_submenu(0);
				}
			}
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

	public synchronized Zone getNextZone(){
		zone++;
		if (zone >= game.getCarte().getZones().size()) {
			return null;
		}
		return game.getCarte().getZones().get(zone);
	}

	public void startGame() {
		started = true;
		System.out.println("Partie lancée !");
		if (isServeur()) {
			game.creerEpidemie();
			game.start();
			timer.scheduleAtFixedRate(new UpdateTask(), 0, TIMER_PERIOD);
		} else {
			timer.scheduleAtFixedRate(new UpdateTask(), 0, TIMER_PERIOD/10);
		}
	}
	
	public int getNbjoueurs() {
		return nbjoueurs;
	}

	public boolean isStarted() {
		return started;
	}
}
