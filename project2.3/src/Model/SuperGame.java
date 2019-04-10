package Model;

/**
 * SuperGame contains some methods that needs to be implemented in every (sub)game
 *
 * @author Daniel Geerts
 * @since 2019-04-06
 */
public abstract class SuperGame {

	public abstract Board getBord();
	
	public abstract boolean containsValidMove(int x, int y);

}
