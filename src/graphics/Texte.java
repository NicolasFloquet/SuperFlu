package graphics;

import java.util.ArrayList;
import java.util.List;

public class Texte {
	private String chaine;
	private List<Sprite> sprites;
	
	public Texte(String chaine) {
		this.chaine = chaine;
		sprites = new ArrayList<Sprite>();
		
		for (int i = 0; i < chaine.length(); i++) {
			String c = chaine.substring(i, i + 1).toUpperCase();
			
			sprites.add(ScreenManager.getSprite("lettres/" + c + ".png"));
		}
	}
	
	public void draw(int x, int y) {
		int offset = 0;
		for (Sprite sprite : sprites) {
			sprite.draw(x + offset, y);
			offset += sprite.getWidth();
		}
	}
}
