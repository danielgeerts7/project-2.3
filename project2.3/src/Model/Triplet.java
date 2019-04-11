package Model;

public class Triplet {
	
	private int x;
	private int y;
	private int weight;
	
	public Triplet(int first, int second, int third) {
		this.x = first;
		this.y = second;
		this.weight = third;
	}
	
	public int getX() { return x; }
    public int getY() { return y; }
    public int getWeight() { return weight; }

}
