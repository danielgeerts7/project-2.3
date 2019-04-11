package Controller;

import Model.ReversiGame;
import View.GameView;

public class GameController {
	private static ReversiGame game = null;
	private static GameView viewRef = null;

	public GameController(GameView view, boolean playRemote) {
		game = new ReversiGame(view, playRemote);
		viewRef = view;
		if (!playRemote) {

			viewRef.getPlayer1().setColor(ReversiGame.BLACK);
			viewRef.getPlayer2().setColor(ReversiGame.WHITE);
			viewRef.getPlayer1().setName("Player 1");
			viewRef.getPlayer2().setName("Computer");
			viewRef.updatePlayersTurn(viewRef.getPlayer2().getName());
		}
		viewRef.updateBoardView(game);
	}

	public static void doMove() {
		game.doMove(viewRef.getPlayer1().getColor());
	}

	public static void receivedMove(String player, String move) {
		if (player.equals(viewRef.getPlayer1().getName())) {
			game.receivedMove(viewRef.getPlayer1().getColor(), Integer.parseInt(move));
		} else {
			game.receivedMove(viewRef.getPlayer2().getColor(), Integer.parseInt(move));
		}
		viewRef.updateBoardView(game);
		viewRef.updatePlayersTurn(player);
	}
}
