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
	private static final int longueur_courbe = 100;
	private static final long serialVersionUID = 1L;
	/*private static int courbe_pop[] = null;*/
	private static int courbe_morts[] = null;
	private static int courbe_infectes[] = null;
	private static int courbe_vaccines[] = null;
	private static int dephasage_courbes;
	
	
	@Override
	public String toString() {
		return "Carte [courbe_infectes=" + Arrays.toString(courbe_infectes)
				+ ", courbe_morts=" + Arrays.toString(courbe_morts)
				/*+ ", courbe_pop=" + Arrays.toString(courbe_pop)*/
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
		
		/*courbe_pop = new int[1024];*/
		courbe_morts = new int[longueur_courbe];
		courbe_infectes = new int[longueur_courbe];
		courbe_vaccines = new int[longueur_courbe];
		dephasage_courbes = 0;
		
		for(int i=0 ; i<longueur_courbe ; i++) {
			/*courbe_pop[i] = 0;*/
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
		
		if(a.getGame().getTime()%10 == 0)
			updateCourbes();
		drawCourbes();	
		map.draw(ScreenManager.getInstance().getOrigineCarteX() + map.getWidth()/2, ScreenManager.getInstance().getOrigineCarteY() + map.getHeight()/2,
					0, 1, 0.0f, 0.3f, 0.0f);
		
		// On dessine en clair les zones appartenant au joueur
		if(Application.getInstance().getJoueur() != null) {
			for( Zone z : Application.getInstance().getJoueur().getZones()) {
				Sprite zone;
				if(z.getId() == 1) {
					zone = ScreenManager.getSprite("carte_eur.png");				
				} else if(z.getId() == 2) {
					zone = ScreenManager.getSprite("carte_us.png");
				} else if(z.getId() == 3) {
					zone = ScreenManager.getSprite("carte_ams.png");
				} else if(z.getId() == 4) {
					zone = ScreenManager.getSprite("carte_afr.png");
				} else if(z.getId() == 5) {
					zone = ScreenManager.getSprite("carte_indo.png");
				} else {
					zone = ScreenManager.getSprite("carte_asia.png");	
				}
				zone.draw(ScreenManager.getInstance().getOrigineCarteX() + map.getWidth()/2, ScreenManager.getInstance().getOrigineCarteY() + map.getHeight()/2,
						0, 1, 0.0f, 0.6f, 0.0f);
			}
		}
		
		// On dessine en gris les zones non affectées
		boolean[] affected_zones = {	false,
										false,
										false,
										false,
										false,
										false,
										false};
		for (Joueur joueur : Application.getInstance().getGame().getJoueurs()) {
			for (Zone z : joueur.getZones()) {
				try {
					affected_zones[z.getId()] = true;
				}
				catch (Exception e) {
					
				}
			}
		}
		for(Zone z : zones)
		{
			try {
				if(!affected_zones[z.getId()]) {
					Sprite zone = null;
					if(z.getId() == 1) {
						zone = ScreenManager.getSprite("carte_eur.png");				
					} else if(z.getId() == 2) {
						zone = ScreenManager.getSprite("carte_us.png");
					} else if(z.getId() == 3) {
						zone = ScreenManager.getSprite("carte_ams.png");
					} else if(z.getId() == 4) {
						zone = ScreenManager.getSprite("carte_afr.png");
					} else if(z.getId() == 5) {
						zone = ScreenManager.getSprite("carte_indo.png");
					} else if(z.getId() == 6){
						zone = ScreenManager.getSprite("carte_asia.png");	
					}
					zone.draw(ScreenManager.getInstance().getOrigineCarteX() + map.getWidth()/2, ScreenManager.getInstance().getOrigineCarteY() + map.getHeight()/2,
							0, 1, 0.3f, 0.3f, 0.3f);
				}
			}
			catch (Exception e) {
				
			}
		}
		
		// On dessine les villes
		for(Zone zone : zones)
		{
			zone.getUsine().draw();
			for(Ville ville : zone.getVilles())
			{
				ville.draw();
			}
		}
		
		new Texte("Population Mondiale : " + a.getGame().getPopulationMondiale()).draw(ScreenManager.getInstance().getOrigineCarteX() + 10, ScreenManager.getInstance().getOrigineCarteY() + 10, 1, 1, 1, 1);
		new Texte("Nombre de morts : " + a.getGame().getMortsTotal()).draw(ScreenManager.getInstance().getOrigineCarteX() + 10, ScreenManager.getInstance().getOrigineCarteY() + 30, 1, 1, 1, 1); 
		new Texte("Nombre de malades : " + a.getGame().getPopulationInfectee()).draw(ScreenManager.getInstance().getOrigineCarteX() + 10, ScreenManager.getInstance().getOrigineCarteY() + 50, 1, 1, 1, 1);
		new Texte("Level " + a.getGame().getLevel()).draw(ScreenManager.getInstance().getOrigineCarteX() + ScreenManager.getInstance().getMap().getWidth() - 100, ScreenManager.getInstance().getOrigineCarteY() + 10, 1, 0.2f, 0.2f, 1);
	}

	public ArrayList<Zone> getZones() {
		return zones;
	}

	public void updateCourbes() {
		GameLogic game = Application.getInstance().getGame();
		courbe_infectes[dephasage_courbes] = game.getPopulationInfectee();
		courbe_morts[dephasage_courbes] = game.getMortsTotal();
		/*courbe_pop[dephasage_courbes] = game.getPopulationMondiale();*/
		courbe_vaccines[dephasage_courbes] = game.getVaccinesTotal();
		dephasage_courbes++;
		if(dephasage_courbes>=longueur_courbe) {
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
		float max;/* = (float)(
				a.getGame().getMortsTotal()
				+a.getGame().getPopulationMondiale()
				)/ map_height;*/
		GL11.glBegin(GL11.GL_LINES);
		
		index = dephasage_courbes;
		
		max = (float)(a.getGame().getPopulationMondiale()*a.getGame().getPourcentagePandemic()/100)/ map_height;
		for(int i=0 ; i<longueur_courbe-2 ; i++) {
			index++;
			if(index>=longueur_courbe-2) {
				GL11.glEnd();
				GL11.glBegin(GL11.GL_LINES);
				index=0;	
			}
			GL11.glColor3f(0.7f,0.5f,0.5f);
		    GL11.glVertex2f(1024/longueur_courbe*1.1f*i,map_height-courbe_infectes[index]/max);
		    GL11.glColor3f(0.7f,0.5f,0.5f);
		    GL11.glVertex2f(1024/longueur_courbe*1.1f*(i+1),map_height-courbe_infectes[index+1]/max);
	
		}
		GL11.glEnd();
		GL11.glBegin(GL11.GL_LINES);
		
		
		index = dephasage_courbes%longueur_courbe;
		max = (float)(
		a.getGame().getMortsTotal()
		+a.getGame().getPopulationMondiale()
		)/ map_height;
		for(int i=0 ; i<longueur_courbe-2 ; i++) {
			index++;
			if(index>=longueur_courbe-2) {
				GL11.glEnd();
				GL11.glBegin(GL11.GL_LINES);
				index=0;
			}
	    	GL11.glColor4f(0.3f,0.3f,1.0f,1.0f);
	    	GL11.glVertex2f(1024/longueur_courbe*1.1f*i,map_height-courbe_vaccines[index]/max);
	    	GL11.glColor4f(0.3f,0.3f,1.0f,1.0f);
	    	GL11.glVertex2f(1024/longueur_courbe*1.1f*(i+1),map_height-courbe_vaccines[index+1]/max);

		}
		GL11.glEnd();
		GL11.glBegin(GL11.GL_LINES);
		index = dephasage_courbes%longueur_courbe;
		max = (float)(a.getGame().getPopulationMondiale()*a.getGame().getPourcentageEchec()/100)/map_height;
		for(int i=0 ; i<longueur_courbe-2 ; i++) {
			index++;
			if(index>=longueur_courbe-2) {
				GL11.glEnd();
				GL11.glBegin(GL11.GL_LINES);
				index=0;
			}
				GL11.glColor3f(1.0f,0.0f,0.0f);
				GL11.glVertex2f(1024/longueur_courbe*1.1f*i,map_height-courbe_morts[index]/max);
				GL11.glColor3f(1.0f,0.0f,0.0f);
				GL11.glVertex2f(1024/longueur_courbe*1.1f*(i+1),map_height-courbe_morts[index+1]/max);
			
		}
		/*
		index = dephasage_courbes;
		for(int i=0 ; i<longueur_courbe ; i++) {
			GL11.glColor4f(0.0f,0.0f,0.0f,0.2f);
		    GL11.glVertex2f(i,4+map_height-courbe_pop[index]/max);
		    GL11.glColor4f(0.0f,1.0f,0.0f,1.0f);
		    GL11.glVertex2f(i,map_height-courbe_pop[index]/max);
			index++;
			if(index>=courbe_pop.length) {
				index=0;
			}
		}*/
    	GL11.glEnd();
		GL11.glPopMatrix();	
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
}
