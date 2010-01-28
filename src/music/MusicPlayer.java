package music;

import java.io.FileInputStream;
import javazoom.jl.player.Player;

public class MusicPlayer {
	
	private Song soft;
	private Song hard;
	
	private class Song extends Thread {
		private FileInputStream file;
		private Player p; 
		private boolean running;
		private boolean playing;
		
		public Song(String title) {
			try {
				file = new FileInputStream(title);
				p = new Player(file);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
			running = true;
			playing = false;
			
			start();
		}
		
		@Override
		public void run() {
			while(running) {
				if(playing) {
					try {
						p.play(1024);
						
						if(p.isComplete()) {
							p.close();
							p = new Player(file);
						}
					}
					catch (Exception e) {
						e.printStackTrace();
						quit();
					}
				}
				else {
					try {
							Thread.sleep(10);
					}
					catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		public void pause() {
			playing = false;
		}
		
		public void play() {
			playing = true;
		}
		
		public void quit() {
			running = false;
		}
	}
	
	public MusicPlayer() {
		
		try {
			soft = new Song("bin/ressources/soft.mp3");
			hard = new Song("bin/ressources/hard.mp3");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void start() {
		soft.play();
	}
	
	public void goPandemic() {
		soft.pause();
		hard.play();
	}
	
	public void backNormal() {
		soft.play();
		hard.pause();
	}
	
	public void quit() {
		soft.quit();
		hard.quit();
	}
}
