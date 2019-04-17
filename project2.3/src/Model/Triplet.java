package Model;

/**
 * object which contains 3 values.
 * @author Created by Casper
 *
 */
public class Triplet {
	
	private int x;
	private int y;
	private int weight;
	
	/**
	 * initialize a new Triplet with values
	 * @param x : x value
	 * @param y : y value
	 * @param weight : weight value
	 */
	public Triplet(int x, int y, int weight) {
		this.x = x;
		this.y = y;
		this.weight = weight;
	}
	
	/**
	 * @return value of x
	 */
	public int getX() { return x; }
	
	/**
	 * @return value of y
	 */
    public int getY() { return y; }
    
    /**
	 * @return value of weight
	 */
    public int getWeight() { return weight; }

}
