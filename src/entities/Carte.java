package entities;

import graphics.*;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Classe qui représente la carte et qui est composée de Zones.
 *
 */
public class Carte implements graphics.Drawable, Serializable {

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
	public void draw()
	{		
		Sprite map = ScreenManager.getInstance().getMap();
		Sprite fond_map = ScreenManager.getSprite("fond_carte.png");
		
		fond_map.draw(ScreenManager.getInstance().getOrigineCarteX() + map.getWidth()/2, ScreenManager.getInstance().getOrigineCarteY() + map.getHeight()/2);
		map.draw(ScreenManager.getInstance().getOrigineCarteX() + map.getWidth()/2, ScreenManager.getInstance().getOrigineCarteY() + map.getHeight()/2);
		
		for(Zone zone : zones)
		{
			for(Ville ville : zone.getVilles())
			{
				ville.draw();
			}
		}
	}

	public ArrayList<Zone> getZones() {
		return zones;
	}

}
