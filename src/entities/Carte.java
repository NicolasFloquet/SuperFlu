package entities;

import java.util.ArrayList;

/**
 * Classe qui représente la carte et qui est composée de Zones.
 *
 */
public class Carte implements graphics.Drawable {

	private ArrayList<Zone> zones = new ArrayList<Zone>();
	
	@Override
	public void draw(int x, int y, int height, int width) {
				
	}

	public ArrayList<Zone> getZones() {
		return zones;
	}

}
