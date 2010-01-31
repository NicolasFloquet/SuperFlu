package graphics;

import java.util.HashMap;

public class Sprites {
	private static Sprites instance;
	private TextureLoader textureLoader;
	private HashMap<String, Sprite> sprites;
	
	private Sprites() {
		textureLoader = new TextureLoader();
		sprites = new HashMap<String, Sprite>();
	}
	
	public static Sprites getInstance() {
		if (instance == null) {
			instance = new Sprites();
		}
		return instance;
	}
	
	public Sprite getSprite(String ref)
	{
		if (!sprites.containsKey(ref)) {
			sprites.put(ref, new Sprite(textureLoader,"ressources/"+ref));
		}
		return sprites.get(ref);
	}
}
