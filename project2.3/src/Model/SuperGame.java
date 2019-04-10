package Model;

/**
 * SuperGame contains some methods that needs to be implemented in every (sub)game
 *
 * @author Daniel Geerts
 * @since 2019-04-06
 */
public abstract class SuperGame {
	
	public SuperGame() {
		
	}
	
	public void doMove(char piece) {
		
	}
	
	public void receivedMove(char piece, int pos) {
		
	}
	
	public abstract Board getBord();
}
