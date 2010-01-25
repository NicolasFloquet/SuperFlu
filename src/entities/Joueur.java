package entities;

public class Joueur implements graphics.Drawable {
	private Zone zone;
	private int score;
	
	public void setZone(Zone zone) {
		this.zone = zone;
	}

	public Zone getZone() {
		return zone;
	}
	
	public int getScore() {
		return score;
	}
	
	@Override
	public void draw(int x, int y, int height, int width) {
		// TODO Auto-generated method stub
		
	}
}
