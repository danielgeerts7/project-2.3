package Controller;

import Model.Client;
import Model.ReversiGame;
import View.GameView;

public class GameController {
	private static ReversiGame game = null;
	private static GameView viewRef = null;
	
	public GameController(GameView view) {
		game = new ReversiGame();
		viewRef = view;
		viewRef.updateBoardView(game);
	}
	
	public static void doMove() {
		game.doMove(GameView.getPlayer1().getColor());
	}
	
	public static void receivedMove(String player, String move) {
		if (player.equals(Client.getUsername())) {
			game.receivedMove(GameView.getPlayer1().getColor(), Integer.parseInt(move));
		} else {
			game.receivedMove(GameView.getPlayer2().getColor(), Integer.parseInt(move));
		}
		viewRef.updateBoardView(game);
		viewRef.updatePlayersTurn(player);
	}
}
