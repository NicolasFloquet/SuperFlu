package entities;

public abstract class Stock {
	protected int capacite_max = 5000;
	protected int stock = 0;
	
	public int getStock() {
		return stock;
	}
	
	public int getCapacite_max() {
		return capacite_max;
	}
}
