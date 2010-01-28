package entities;

import graphics.ScreenManager;
import graphics.Sprite;
import graphics.Texte;

import java.io.Serializable;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import logique.Application;
import logique.PlayerManager;

/**
 * Cette classe représente une usine. C'est une ville qui possède la propriété de pouvoir produire des vaccins et traitements.
 *
 */
public class Usine extends Ville implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Traitement> traitements = new ArrayList<Traitement>();

	private ArrayList<Vaccin> vaccins = new ArrayList<Vaccin>();
	private int productionRateVaccins = 2;
	private int productionRateTraitements = 10;
	
	public Usine(Zone zone, String nom, int x, int y) {
		super(zone, nom, x, y);
		this.retireHabitantsSains(this.getHabitantsSains());
	}

	public void produit() {
		Application a = Application.getInstance();
		productionRateTraitements = (int) (3 * Math.log(1 + a.getGame().getPopulationInfectee()));
		productionRateVaccins = (int) (0.5 * Math.log(1 + a.getGame().getPopulationInfectee()));

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
		int height = usine.getHeight();
		int width = usine.getWidth();
		
		if(PlayerManager.getInstance().getTargetedVille() == this)
			hl_usine.draw(pos_x, pos_y);
		else
			usine.draw(pos_x, pos_y);
		
		if (PlayerManager.getInstance().getTargetedVille() == this) {
			// Affichage info
			int encart_pos_y = ScreenManager.getInstance().getOrigineEncartY() + 15;
			new Texte("Usine de "+this.getNom()).draw(10, encart_pos_y + 20);
			if(getStocksTraitements().isEmpty())
				new Texte("Stock Traitements 0").draw(10,encart_pos_y + 40);
			else
				new Texte("Stock Traitements " + getStocksTraitements().get(0).getStock()).draw(10,encart_pos_y + 40);
				
			if(getStocksVaccins().isEmpty())
				new Texte("Stock Vaccins 0").draw(10,encart_pos_y + 60);
			else
				new Texte("Stock Vaccins " + getStocksVaccins().get(0).getStock()).draw(10,encart_pos_y + 60);
		}
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glPushMatrix();
		GL11.glTranslatef(pos_x, pos_y, 0);
		
		
		// On dessine la barre de stock
		int dx = 0;
		for(StockTraitement stock : getStocksTraitements()) {
			float p = 0.5f*(float)stock.stock/(float)stock.capacite_max;
			GL11.glColor3f(0,0,0);
			GL11.glLineWidth(6);
	    	GL11.glBegin(GL11.GL_LINES);
			{
		      GL11.glVertex2f( dx + 3 + width/2, 1 + height/2 );
		      GL11.glVertex2f( dx + 3 + width/2, -1 - height/2);
			}
			GL11.glEnd();
			GL11.glLineWidth(4);
	    	GL11.glBegin(GL11.GL_LINES);
			{
				if(stock.stock>0) {
					GL11.glColor3f(0.5f+p,0.5f+p,0.5f+p);
					 GL11.glVertex2f( 3 + width/2, +height/2 );
				      GL11.glVertex2f( 3 + width/2, +height/2 - height*2*p);
				}
				else {
					GL11.glColor3f(1,0,0);
				    GL11.glVertex2f( 3 + width/2, -2 + height/2 );
				    GL11.glVertex2f( 3 + width/2, 2 - height/2);
				}
			}
			GL11.glEnd();
			
			dx+=6;
		}
		
		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	
	public ArrayList<Traitement> getTraitements() {
		return traitements;
	}

	public ArrayList<Vaccin> getVaccins() {
		return vaccins;
	}
}

