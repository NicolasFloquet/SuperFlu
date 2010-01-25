
public class Ville {

	private int x;
	private int y;
	
	public Ville(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public static int distance(Ville depart, Ville arrivee) {
		return (int) Math.sqrt((arrivee.x - depart.x) * (arrivee.x - depart.x) + (arrivee.y - depart.y) * (arrivee.y - depart.y));
	}

}