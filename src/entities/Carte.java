package entities;

import graphics.*;

import java.util.ArrayList;

import org.lwjgl.Sys;


/**
 * Classe qui représente la carte et qui est composée de Zones.
 *
 */
public class Carte implements graphics.Drawable {

	private ArrayList<Zone> zones = new ArrayList<Zone>();
	
	public Carte() {
		zones.add(new Zone(1));
		zones.add(new Zone(2));
		zones.add(new Zone(3));
		zones.add(new Zone(4));
		zones.add(new Zone(5));
		zones.add(new Zone(6));
	}
	
	@Override
	public void draw(int x, int y, int height, int width)
	{
		//int dx = (int)(60.0*Math.cos(Sys.getTime()/60));
		//int dy = (int)(40.0*Math.sin(Sys.getTime()/60));
		float phi = 0.0f; //0.6f*Sys.getTime();
		
		Sprite test = ScreenManager.getSprite("test.png");
		test.draw(250, 500, phi);
	}

	public ArrayList<Zone> getZones() {
		return zones;
	}

}
