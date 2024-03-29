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
		//System.out.println("usine " + nom + " produit");
		Application a = Application.getInstance();
		productionRateTraitements = (int) (5 + 0.2 * Math.sqrt(a.getGame().getPopulationInfectee()));
		productionRateVaccins = (int) (Math.sqrt(a.getGame().getPopulationInfectee()));

		for (Traitement traitement : traitements) {
			//System.out.println("ajoute " + productionRateTraitements);
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

		float couleur;
		if (isMine()) {
			couleur = 1.0f;
		} else {
			couleur = 0.7f;
		}
		if(PlayerManager.getInstance().getTargetedVille() == this)
			hl_usine.draw(pos_x, pos_y, 0, 1, couleur, couleur, couleur);
		else
			usine.draw(pos_x, pos_y, 0, 1, couleur, couleur, couleur);

		if (PlayerManager.getInstance().getTargetedVille() == this) { 
			if(isMine()) {
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
			} else {
				int encart_pos_y = ScreenManager.getInstance().getOrigineEncartY() + 15;
				for (Joueur joueur : Application.getInstance().getGame().getJoueurs()) {
					for (Zone z : joueur.getZones()) {
						if (z.getId() == getZone().getId()) {
							new Texte(nom + " (" + joueur.getPseudo() + ")").draw(10, encart_pos_y + 20);
						}
					}
				}
			}
		}

		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glPushMatrix();
		GL11.glTranslatef(pos_x, pos_y, 0);

		float p;
		// On dessine la barre de stock de traitement
		for(StockTraitement stock : getStocksTraitements()) {
			p = 0.5f*stock.getStock()/stock.getCapacite_max();
			GL11.glColor3f(0,0,0);
			GL11.glLineWidth(6);
			GL11.glBegin(GL11.GL_LINES);
			{
				GL11.glVertex2f( -(3 + width/2), 1 + height/2 );
				GL11.glVertex2f( -(3 + width/2), -1 - height/2);
			}
			GL11.glEnd();
			GL11.glColor3f(0.2f,0.2f,0.5f+p);
			GL11.glLineWidth(4);
			GL11.glBegin(GL11.GL_LINES);
			{
				GL11.glVertex2f( -(3 + width/2), +height/2 );
				GL11.glVertex2f( -(3 + width/2), +height/2 - height*2*p);
			}
			GL11.glEnd();
		}

		// On dessine la barre de stock de vaccins
		for(StockVaccin stock : getStocksVaccins()) {
			p = 0.5f*stock.getStock()/stock.getCapacite_max();
			GL11.glColor3f(0,0,0);
			GL11.glLineWidth(6);
			GL11.glBegin(GL11.GL_LINES);
			{
				GL11.glVertex2f( 3 + width/2, 1 + height/2 );
				GL11.glVertex2f( 3 + width/2, -1 - height/2);
			}
			GL11.glEnd();
			GL11.glColor3f(0.5f+p,0.2f,0.2f);
			GL11.glLineWidth(4);
			GL11.glBegin(GL11.GL_LINES);
			{
				GL11.glVertex2f( (3 + width/2), +height/2 );
				GL11.glVertex2f( (3 + width/2), +height/2 - height*2*p);
			}
			GL11.glEnd();
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

