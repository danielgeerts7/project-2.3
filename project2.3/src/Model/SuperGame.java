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

	public abstract Board getBord();
	public void doMove(char color) {
		// TODO Auto-generated method stub
		
	}
	public void receivedMove(char color, int parseInt) {
		// TODO Auto-generated method stub
		
	}
}
