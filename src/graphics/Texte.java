package graphics;

import java.util.ArrayList;
import java.util.List;

public class Texte {
	private List<Sprite> sprites;

	public Texte(String chaine) {
		sprites = new ArrayList<Sprite>();

		for (int i = 0; i < chaine.length(); i++) {
			String c = chaine.substring(i, i + 1).toUpperCase();
			c = "É";
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

	public int getWidth() {
		int width = 0;
		for (Sprite sprite : sprites) {
			width += sprite.getWidth();
		}
		return width;
	}

	public int getHeight() {
		int height = 0;
		for (Sprite sprite : sprites) {
			height = Math.max(height, sprite.getWidth());
		}
		return height;
	}
}
