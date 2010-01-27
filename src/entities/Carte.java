package entities;

import graphics.*;

import java.io.Serializable;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import logique.Application;
import logique.GameLogic;
import logique.PlayerManager;


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
		
		updateCourbes();
		drawCourbes();
		
		fond_map.draw(ScreenManager.getInstance().getOrigineCarteX() + map.getWidth()/2, ScreenManager.getInstance().getOrigineCarteY() + map.getHeight()/2);
		map.draw(ScreenManager.getInstance().getOrigineCarteX() + map.getWidth()/2, ScreenManager.getInstance().getOrigineCarteY() + map.getHeight()/2);
		
		for(Zone zone : zones)
		{
			zone.getUsine().draw();
			for(Ville ville : zone.getVilles())
			{
				ville.draw();
			}
		}
		
		new Texte("Population Mondiale : " + Application.getInstance().getGame().getPopulationMondiale()).draw(10, 10, 255, 255, 255);
		new Texte("Nombre de morts : " + Application.getInstance().getGame().getMortsTotal()).draw(10, 30, 255, 255, 255);
	}

	public ArrayList<Zone> getZones() {
		return zones;
	}

	public void updateCourbes() {
		GameLogic game = Application.getInstance().getGame();
		courbe_infectes[dephasage_courbes] = game.getPopulationInfectee();
		courbe_morts[dephasage_courbes] = game.getMortsTotal();
		courbe_pop[dephasage_courbes] = game.getPopulationMondiale();
		courbe_vaccines[dephasage_courbes] = game.getVaccinesTotal();
		dephasage_courbes++;
		if(dephasage_courbes>=courbe_pop.length) {
			dephasage_courbes = 0;
		}
	}
	
	public void drawCourbes() {
		//dessin de la courbe
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		// On dessine la courbe
		GL11.glPushMatrix();
		GL11.glTranslatef(ScreenManager.getInstance().getOrigineCarteX(),
							ScreenManager.getInstance().getOrigineCarteY(), 0);
		
		int index;
		float max = (float)(
				Application.getInstance().getGame().getMortsTotal()
				+Application.getInstance().getGame().getPopulationMondiale()
				)/ScreenManager.getInstance().getMap().getHeight();
		System.out.println(max);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glColor3f(1.0f,0.5f,0.5f);
		index = dephasage_courbes;
		for(int i=0 ; i<courbe_pop.length ; i++) {
		    GL11.glVertex2f(i,courbe_infectes[index]/max);
			index++;
			if(index>=courbe_pop.length) {
				index=0;
			}
		}
		GL11.glColor3f(0.3f,0.3f,1.0f);
		index = dephasage_courbes;
		for(int i=0 ; i<courbe_pop.length ; i++) {
	    	GL11.glVertex2f(i,courbe_vaccines[index]/max);
			index++;
			if(index>=courbe_pop.length) {
				index=0;
			}
		}
		GL11.glColor3f(1.0f,0.0f,0.0f);
		index = dephasage_courbes;
		for(int i=0 ; i<courbe_pop.length ; i++) {
	    	GL11.glVertex2f(i,courbe_morts[index]/max);
			index++;
			if(index>=courbe_pop.length) {
				index=0;
			}
		}
		GL11.glColor3f(0.0f,1.0f,0.0f);
		index = dephasage_courbes;
		for(int i=0 ; i<courbe_pop.length ; i++) {
		    GL11.glVertex2f(i,courbe_pop[index]/max);
			index++;
			if(index>=courbe_pop.length) {
				index=0;
			}
		}
    	GL11.glEnd();
		GL11.glPopMatrix();	
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
}
