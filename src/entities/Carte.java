package entities;

import graphics.*;

import java.util.ArrayList;


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
	public void draw()
	{		
		Sprite map = ScreenManager.getSprite("carte.png");
		map.draw(ScreenManager.getInstance().getOrigineCarteX() + 1024/2, ScreenManager.getInstance().getOrigineCarteY() + 544/2);
		
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
