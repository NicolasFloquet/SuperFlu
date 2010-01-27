package entities;

import graphics.ScreenManager;
import graphics.Sprite;

import java.io.Serializable;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import logique.PlayerManager;

/**
 * Cette classe représente une usine. C'est une ville qui possède la propriété de pouvoir produire des vaccins et traitements.
 *
 */
public class Usine extends Ville implements Serializable{

	private ArrayList<Traitement> traitements = new ArrayList<Traitement>();
	private ArrayList<Vaccin> vaccins = new ArrayList<Vaccin>();
	private int productionRateVaccins = 100;
	private int productionRateTraitements = 1000;
	
	public Usine(Zone zone, String nom, int x, int y) {
		super(zone, nom, x, y);
	}

	public void produit() {
		for (Traitement traitement : traitements) {
			ajouteStockTraitement(traitement, productionRateTraitements);
		}
		for (Vaccin vaccin : vaccins) {
			ajouteStockVaccin(vaccin, productionRateVaccins);
		}
	}

	public int getProductionRateVaccins() {
		return productionRateVaccins;
	}
	
	public void setProductionRate(int productionRateVaccins) {
		this.productionRateVaccins = productionRateVaccins;
	}
	
	public void ajouteVaccin(Vaccin vaccin) {
		vaccins.add(vaccin);
	}
	
	public void ajouteTraitement(Traitement traitement) {
		traitements.add(traitement);
	}
	
	@Override
	public void draw() {
		Sprite usine = ScreenManager.getSprite("usine.png");
		Sprite hl_usine = ScreenManager.getSprite("HL_usine.png");
		
		int pos_x = x + ScreenManager.getInstance().getOrigineCarteX();
		int pos_y = y + ScreenManager.getInstance().getOrigineCarteY();
			
		if(PlayerManager.getInstance().getTargetedVille() == this)
			hl_usine.draw(pos_x, pos_y);
		else
			usine.draw(pos_x, pos_y);
		
		// On dessine la barre de contamination
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		GL11.glPushMatrix();
		GL11.glTranslatef(pos_x, pos_y, 0);
    	GL11.glColor3f(0,0,0);
		GL11.glLineWidth(6);
    	GL11.glBegin(GL11.GL_LINES);
		{
	      GL11.glVertex2f( -usine.getWidth()/2 - 1, 3 + usine.getHeight()/2);
	      GL11.glVertex2f( usine.getWidth()/2 + 1, 3 + usine.getHeight()/2);
		}
		GL11.glEnd();
		float p = (float)getPourcentageInfectes()/100.0f;
		GL11.glColor3f(p,1-p,0);
		GL11.glLineWidth(2);
    	GL11.glBegin(GL11.GL_LINES);
		{
	      GL11.glVertex2f( -usine.getWidth()*p/2 + 1, 3 + usine.getHeight()/2);
	      GL11.glVertex2f( usine.getWidth()*p/2 - 1 , 3 + usine.getHeight()/2);
		}
		GL11.glEnd();
		GL11.glPopMatrix();
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
}

