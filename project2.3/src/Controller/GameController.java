package Controller;

import Controller.ServerMessageHandler;
import Main.Main;
import Model.Client;
import Model.ReversiGame;
import Model.SuperGame;
import Model.TicTacToeGame;
//import Model.TicTacToeGame;
import View.GameView;

public class GameController {
	private static SuperGame game = null;
	private static GameView viewRef = null;
	
	public GameController(GameView view, String gametype) {
		if (gametype.toLowerCase().contains("reversi")) {
			System.out.println("Reversicontroller");
			game = new ReversiGame();
		}
		else if (gametype.toLowerCase().contains("tic-tac-toe")) {
			System.out.println("TTTController");
			game = new TicTacToeGame();
		}
		viewRef = view;
		viewRef.updateBoardView(game);
	}
	
	public static void doMove(String gametype) {
		if (gametype.toLowerCase().contains("reversi")) {
			game.doMove(GameView.getPlayer1().getColor());
		}
		
		else if (gametype.toLowerCase().contains("tic-tac-toe")) {
			//TODO: iets
		}
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
