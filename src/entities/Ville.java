package entities;

import graphics.ScreenManager;
import graphics.Sprite;
import graphics.Texte;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import logique.PlayerManager;

/**
 * Cette classe représente une ville.
 * @author max
 *
 */
public class Ville implements graphics.Drawable, Serializable {

	private Zone zone;
	private String nom;
	protected int x;
	protected int y;
	private int habitantsSains;
	private int habitantsInfectes;
	private int habitantsImmunises;
	private int habitantsMorts;

	private ArrayList<StockVaccin> stocksVaccins = new ArrayList<StockVaccin>();
	private ArrayList<StockTraitement> stocksTraitements = new ArrayList<StockTraitement>();

	public Ville(Zone zone, String nom, int x, int y) {
		Random r = new Random();

		this.zone = zone;
		this.nom = nom;
		this.x = x;
		this.y = y;
		this.habitantsSains = 1000000 + r.nextInt(500000) - 250000;
		this.habitantsInfectes = 0;
		this.habitantsImmunises = 0;
		this.habitantsMorts = 0;
	}

	public static int distance(Ville depart, Ville arrivee) {
		return (int) Math.sqrt((arrivee.x - depart.x) * (arrivee.x - depart.x) + (arrivee.y - depart.y) * (arrivee.y - depart.y));
	}

	public void ajouteStockVaccin(Vaccin vaccin, int quantite) {
		boolean ajoute = false;

		for (StockVaccin sv : stocksVaccins) {
			if (sv.getVaccin() == vaccin) {
				sv.ajouteStock(quantite);
				ajoute = true;
				break;
			}
		}
		if (!ajoute) {
			stocksVaccins.add(new StockVaccin(quantite, vaccin));
		}
	}
	
	public void retireStockVaccin(Vaccin vaccin, int quantite) {

		for (StockVaccin sv : stocksVaccins) {
			if (sv.getVaccin() == vaccin) {
				sv.retireStock(quantite);
				break;
			}
		}
	}

	public void ajouteStockTraitement(Traitement traitement, int quantite) {
		boolean ajoute = false;

		for (StockTraitement st : stocksTraitements) {
			if (st.getTraitement() == traitement) {
				st.ajouteStock(quantite);
				ajoute = true;
				break;
			}
		}
		if (!ajoute) {
			stocksTraitements.add(new StockTraitement(quantite, traitement));
		}
	}
	
	public void retireStockTraitement(Traitement traitement, int quantite) {

		for (StockTraitement st : stocksTraitements) {
			if (st.getTraitement() == traitement) {
				st.retireStock(quantite);
				break;
			}
		}
	}

	public String getNom() {
		return nom;
	}

	public int getHabitants() {
		if (habitantsSains + habitantsImmunises + habitantsInfectes < 0) {
			System.err.println("ERRRRRRRR");
		}
		return habitantsSains + habitantsImmunises + habitantsInfectes;
	}

	public int getHabitantsSains() {
		return habitantsSains;
	}

	public int getHabitantsImmunises() {
		return habitantsImmunises;
	}

	public int getHabitantsInfectes() {
		return habitantsInfectes;
	}

	public void ajouteHabitantsInfectes(int habitantsInfectes) {
		this.habitantsInfectes += habitantsInfectes;
	}

	public void ajouteHabitantsImmunises(int habitantsImmunises) {
		this.habitantsImmunises += habitantsImmunises;
	}

	public void ajouteHabitantsSains(int habitantsSains) {
		this.habitantsSains += habitantsSains;
	}

	public void retireHabitantsInfectes(int habitantsInfectes) {
		this.habitantsInfectes -= habitantsInfectes;
	}

	public void retireHabitantsImmunises(int habitantsImmunises) {
		this.habitantsImmunises -= habitantsImmunises;
	}

	public void retireHabitantsSains(int habitantsSains) {
		this.habitantsSains -= habitantsSains;
	}

	public int getPourcentageInfectes() {
		return (int)(100 * ((float)habitantsInfectes / getHabitants()));
	}

	/**
	 * Mise à jour des données de la ville.
	 */
	public void update() {
		//System.out.println("Update !");
		float transmission = 1.2f;
		float perteImmunite = 0.01f;
		float mortalite = 0.01f;

		if (getHabitants() > 0) {

			// Infection :
			/*System.out.println("Infection :");*/
			int nouveauxHabitantsInfectes = (int) (habitantsInfectes * transmission * (habitantsSains / getHabitants()));
			habitantsSains -= nouveauxHabitantsInfectes;
			habitantsInfectes += nouveauxHabitantsInfectes;
			/*System.out.println("nouveaux : " + nouveauxHabitantsInfectes + " infectés (total) " + habitantsInfectes);*/

			// Utilisation des traitements :
			/*System.out.println("Traitements :");*/
			if (stocksTraitements.size() > 0) {
				if (habitantsInfectes > stocksTraitements.get(0).getStock()) {
					habitantsInfectes -= stocksTraitements.get(0).getStock();
					stocksTraitements.get(0).utilise(stocksTraitements.get(0).getStock());
					/*System.out.println("Utilise : " + stocksTraitements.get(0).getStock());*/
				} else {
					stocksTraitements.get(0).utilise(habitantsInfectes);
					/*System.out.println("Utilise : " + habitantsInfectes);*/
				}
			}

			// Perte immunité :
			/*System.out.println("Perte immunité : ");*/
			habitantsImmunises -= (int) (habitantsImmunises * perteImmunite);
			/*System.out.println(habitantsImmunises);*/

			// Utilisation des vaccins (sur les personnes saines) :
			/*System.out.println("Utilisation vaccins : ");*/
			if (stocksVaccins.size() > 0) {
				int nouveauxHabitantsImmunises = Math.min(habitantsSains, stocksVaccins.get(0).getStock());
				stocksVaccins.get(0).utilise(nouveauxHabitantsImmunises);
				habitantsSains -= nouveauxHabitantsImmunises;
				habitantsImmunises += nouveauxHabitantsImmunises;
				/*System.out.println("immunisés : " + habitantsImmunises);*/
			}

			/*System.out.println("Mortalité :");*/
			// Mortalité :
			int nouveauxHabitantsMorts = (int) (habitantsInfectes * mortalite);
			habitantsInfectes -= nouveauxHabitantsMorts;
			habitantsMorts += nouveauxHabitantsMorts;
			/*System.out.println("morts " + habitantsMorts);*/
		}
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean isOnCity(int x, int y) {
		return (x-this.x)*(x-this.x)+(y-this.y)*(y-this.y) < 500;
	}

	@Override
	public void draw() {
		Sprite ville = ScreenManager.getSprite("ville.png");
		Sprite hl_ville = ScreenManager.getSprite("HL_ville.png");

		int pos_x = x + ScreenManager.getInstance().getOrigineCarteX();
		int pos_y = y + ScreenManager.getInstance().getOrigineCarteY();
		int height = ville.getHeight();
		int width = ville.getWidth();
			
		if(PlayerManager.getInstance().getTargetedVille() == this)
			hl_ville.draw(pos_x, pos_y);
		else
			ville.draw(pos_x, pos_y);

		Texte t = new Texte("ABC");
		t.draw(pos_x, pos_y);
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glPushMatrix();
		GL11.glTranslatef(pos_x, pos_y, 0);
		
		// On dessine la barre de contamination
    	GL11.glColor3f(0,0,0);
		GL11.glLineWidth(6);
    	GL11.glBegin(GL11.GL_LINES);
		{
	      GL11.glVertex2f( -width/2 - 1, 3 + height/2);
	      GL11.glVertex2f( width/2 + 1, 3 + height/2);
		}
		GL11.glEnd();
		float p = (float)getPourcentageInfectes()/100.0f;
		GL11.glColor3f(p,1-p,0);
		GL11.glLineWidth(2);
    	GL11.glBegin(GL11.GL_LINES);
		{
	      GL11.glVertex2f( -width*p/2 + 1, 3 + height/2);
	      GL11.glVertex2f( width*p/2 - 1 , 3 + height/2);
		}
		GL11.glEnd();
		
		
		// On dessine la barre de stock
		for(StockTraitement stock : stocksTraitements) {
			p = 0.5f*stock.stock/stock.capacite_max;
			GL11.glColor3f(0,0,0);
			GL11.glLineWidth(6);
	    	GL11.glBegin(GL11.GL_LINES);
			{
		      GL11.glVertex2f( 3 + width/2, -1 + height/2 );
		      GL11.glVertex2f( 3 + width/2, 1 - height/2);
			}
			GL11.glEnd();
			
			GL11.glLineWidth(2);
	    	GL11.glBegin(GL11.GL_LINES);
			{
				if(stock.stock>0) {
					GL11.glColor3f(0.5f+p,0.5f+p,0.5f+p);
				    GL11.glVertex2f( 3 + width/2, -2 + height*p );
				    GL11.glVertex2f( 3 + width/2, 2 - height*p);
				}
				else {
					GL11.glColor3f(1,0,0);
				    GL11.glVertex2f( 3 + width/2, -2 + height/2 );
				    GL11.glVertex2f( 3 + width/2, 2 - height/2);
				}
			}
			GL11.glEnd();
		}
		
		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public ArrayList<StockVaccin> getStocksVaccins() {
		return stocksVaccins;
	}

	public ArrayList<StockTraitement> getStocksTraitements() {
		return stocksTraitements;
	}

	public Zone getZone() {
		return zone;
	}

	@Override
	public String toString() {
		return nom+":\n"
		           +"-Sains = "+this.habitantsSains+"\n"
		           +"-Malades ="+this.habitantsInfectes+"\n"
		           +"-Morts = "+this.habitantsMorts+"\n";
	}
}
