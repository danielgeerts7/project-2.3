package Model;

import javafx.animation.AnimationTimer;

/**
 * SuperGame contains some methods that needs to be implemented in every (sub)game
 *
 * @author Daniel Geerts
 * @since 2019-04-06
 */
public abstract class SuperGame {
	
	public SuperGame() {
		AnimationTimer animator = new AnimationTimer() {
			@Override
			public void handle(long now) {
				update();
			}
		};
		animator.start();
	}

	public abstract Board getBord();
	
	public abstract boolean containsValidMove(int x, int y);
	
	
	/**
	 * This method is called every available frame
	 */
	protected abstract void update();

}
