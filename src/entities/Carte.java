package entities;

import graphics.*;

import java.io.Serializable;
import java.util.ArrayList;

import logique.Application;


/**
 * Classe qui représente la carte et qui est composée de Zones.
 *
 */
public class Carte implements graphics.Drawable, Serializable {

	private int courbe_pop[];
	private int courbe_morts[];
	private int courbe_infectes[];
	private int courbe_vaccines[];
	private int dephasage_courbes;
	
	private ArrayList<Zone> zones = new ArrayList<Zone>();
	
	public Carte() {
		zones.add(new Zone(1));
		zones.add(new Zone(2));
		zones.add(new Zone(3));
		zones.add(new Zone(4));
		zones.add(new Zone(5));
		zones.add(new Zone(6));
		
		courbe_pop = new int[ScreenManager.getInstance().getMap().getWidth()];
		courbe_morts = new int[courbe_pop.length];
		courbe_infectes = new int[courbe_pop.length];
		courbe_vaccines = new int[courbe_pop.length];
		dephasage_courbes = 0;
		
		for(int i=0 ; i<courbe_pop.length ; i++) {
			courbe_pop[i] = 0;
			courbe_morts[i] = 0;
			courbe_infectes[i] = 0;
			courbe_vaccines[i] = 0;
		}
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
		
		new Texte("Population Mondiale : " + Application.getInstance().getGame().getPopulationMondiale()).draw(10, 10);
		new Texte("Nombre de morts : " + Application.getInstance().getGame().getMortsTotal()).draw(10, 30);
	}

	public ArrayList<Zone> getZones() {
		return zones;
	}

}
