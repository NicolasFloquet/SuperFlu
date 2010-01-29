package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import logique.Application;
import logique.GameLogic;

import org.lwjgl.opengl.GL11;

import graphics.ScreenManager;
import graphics.Sprite;
import graphics.Texte;


/**
 * Classe qui représente la carte et qui est composée de Zones.
 *
 */
public class Carte implements graphics.Drawable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int courbe_pop[] = null;
	private int courbe_morts[] = null;
	private int courbe_infectes[] = null;
	private int courbe_vaccines[] = null;
	private int dephasage_courbes;
	
	
	
	@Override
	public String toString() {
		return "Carte [courbe_infectes=" + Arrays.toString(courbe_infectes)
				+ ", courbe_morts=" + Arrays.toString(courbe_morts)
				+ ", courbe_pop=" + Arrays.toString(courbe_pop)
				+ ", courbe_vaccines=" + Arrays.toString(courbe_vaccines)
				+ ", dephasage_courbes=" + dephasage_courbes + "]";
	}

	private ArrayList<Zone> zones = new ArrayList<Zone>();
	
	public Carte() {
		zones.add(new Zone(1));
		zones.add(new Zone(2));
		zones.add(new Zone(3));
		zones.add(new Zone(4));
		zones.add(new Zone(5));
		zones.add(new Zone(6));
		
		courbe_pop = new int[1024];
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
		Sprite fond_map_danger = ScreenManager.getSprite("fond_carte_danger.png");
		
		Application a = Application.getInstance();

		fond_map.draw(ScreenManager.getInstance().getOrigineCarteX() + map.getWidth()/2, ScreenManager.getInstance().getOrigineCarteY() + map.getHeight()/2);
		if(Application.getInstance().getGame().isPandemic()) {
			fond_map_danger.draw(ScreenManager.getInstance().getOrigineCarteX() + map.getWidth()/2, ScreenManager.getInstance().getOrigineCarteY() + map.getHeight()/2,
									0,1,1,1,1,1-((float)(ScreenManager.getTime()%1000))/1000.0f);
		}
		updateCourbes();
		drawCourbes();	
		map.draw(ScreenManager.getInstance().getOrigineCarteX() + map.getWidth()/2, ScreenManager.getInstance().getOrigineCarteY() + map.getHeight()/2,
					0, 1, 0.0f, 0.3f, 0.0f);
		
		if(Application.getInstance().getJoueur() != null) {
			for( Zone z : Application.getInstance().getJoueur().getZone()) {
				Sprite zone;
				if(z.getNom().equals("Europe")) {
					zone = ScreenManager.getSprite("carte_eur.png");				
				} else if(z.getNom().equals("AmeriqueDuNord")) {
					zone = ScreenManager.getSprite("carte_us.png");
				} else if(z.getNom().equals("AmeriqueDuSud")) {
					zone = ScreenManager.getSprite("carte_ams.png");
				} else if(z.getNom().equals("Afrique")) {
					zone = ScreenManager.getSprite("carte_afr.png");
				} else if(z.getNom().equals("Oceanie")) {
					zone = ScreenManager.getSprite("carte_indo.png");
				} else {
					zone = ScreenManager.getSprite("carte_asia.png");	
				}
				zone.draw(ScreenManager.getInstance().getOrigineCarteX() + map.getWidth()/2, ScreenManager.getInstance().getOrigineCarteY() + map.getHeight()/2,
						0, 1, 0.0f, 0.6f, 0.0f);
			}
		}
		
		for(Zone zone : zones)
		{
			zone.getUsine().draw();
			for(Ville ville : zone.getVilles())
			{
				ville.draw();
			}
		}
		
		new Texte("Population Mondiale : " + a.getGame().getPopulationMondiale()).draw(10, 10, 1, 255, 255, 255);
		new Texte("Nombre de morts : " + a.getGame().getMortsTotal()).draw(10, 30, 1, 255, 255, 255); 
		new Texte("Nombre de malades : " + a.getGame().getPopulationInfectee()).draw(10, 50, 1, 255, 255, 255);
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
		GL11.glEnable(GL11.GL_POINT_SMOOTH);
		GL11.glPointSize(3);
		
		// On dessine la courbe
		GL11.glPushMatrix();
		GL11.glTranslatef(ScreenManager.getInstance().getOrigineCarteX(),
							ScreenManager.getInstance().getOrigineCarteY(), 0);
		
		Application a = Application.getInstance();
		int index;
		int map_height = ScreenManager.getInstance().getMap().getHeight();
		float max = (float)(
				a.getGame().getMortsTotal()
				+a.getGame().getPopulationMondiale()
				)/ map_height;
		GL11.glBegin(GL11.GL_POINTS);
		index = dephasage_courbes;
		for(int i=0 ; i<courbe_pop.length ; i++) {
			GL11.glColor4f(0.0f,0.0f,0.0f,0.2f);
		    GL11.glVertex2f(i,4+map_height-courbe_infectes[index]/max);
			GL11.glColor3f(0.7f,0.5f,0.5f);
		    GL11.glVertex2f(i,map_height-courbe_infectes[index]/max);
			index++;
			if(index>=courbe_pop.length) {
				index=0;
			}
		}
		index = dephasage_courbes;
		for(int i=0 ; i<courbe_pop.length ; i++) {
			GL11.glColor4f(0.0f,0.0f,0.0f,0.2f);
	    	GL11.glVertex2f(i,4+map_height-courbe_vaccines[index]/max);
	    	GL11.glColor4f(0.3f,0.3f,1.0f,1.0f);
	    	GL11.glVertex2f(i,map_height-courbe_vaccines[index]/max);
			index++;
			if(index>=courbe_pop.length) {
				index=0;
			}
		}
		index = dephasage_courbes;
		for(int i=0 ; i<courbe_pop.length ; i++) {
			GL11.glColor4f(0.0f,0.0f,0.0f,0.2f);
	    	GL11.glVertex2f(i,4+map_height-courbe_morts[index]/max);
	    	GL11.glColor4f(1.0f,0.0f,0.0f,1.0f);
	    	GL11.glVertex2f(i,map_height-courbe_morts[index]/max);
			index++;
			if(index>=courbe_pop.length) {
				index=0;
			}
		}
		index = dephasage_courbes;
		for(int i=0 ; i<courbe_pop.length ; i++) {
			GL11.glColor4f(0.0f,0.0f,0.0f,0.2f);
		    GL11.glVertex2f(i,4+map_height-courbe_pop[index]/max);
		    GL11.glColor4f(0.0f,1.0f,0.0f,1.0f);
		    GL11.glVertex2f(i,map_height-courbe_pop[index]/max);
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
