package music;

import java.io.FileNotFoundException;
import java.io.InputStream;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class MusicPlayer {

	private Song soft;
	private Song hard;

	private class Song extends Thread {
		private InputStream file;
		private Player p; 
		private boolean running;
		private boolean playing;

		public Song(String title) throws FileNotFoundException, JavaLayerException {
			file = getClass().getClassLoader().getResourceAsStream(title);
			p = new Player(file);

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
							file.reset();
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
			soft = new Song("ressources/soft.mp3");
			hard = new Song("ressources/hard.mp3");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void start() {
		if (soft != null) {
			soft.play();
		}
	}

	public void goPandemic() {
		if (soft != null) {
			soft.pause();
			hard.play();
		}
	}

	public void backNormal() {
		if (soft != null) {
			soft.play();
			hard.pause();
		}
	}

	public void quit() {
		if (soft != null) {
			soft.quit();
			hard.quit();
		}
	}
}
