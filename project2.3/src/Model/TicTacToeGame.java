package Model;

import java.util.ArrayList;
import java.util.List;
import Model.Combo;
import Model.Tile;
import View.GameView;

public class TicTacToeGame extends SuperGame{
	
	private static boolean playable = true;
	private static List<Combo> combos = new ArrayList<>();
	private Tile[][] board = new Tile[3][3];
	private boolean turnX = true;
		
	public TicTacToeGame(GameView view, boolean playRemote){
		//Horizontaal 3 op een rij
		for (int y = 0; y < 3; y++) {
			combos.add(new Combo(board[0][y], board[1][y], board[2][y]));				
		}
		//Verticaal 3 op een rij
		for (int x = 0; x < 3; x++) {
			combos.add(new Combo(board[x][0], board[x][1], board[x][2]));
		}
				
		//Diagonaal 3 op een rijd
		combos.add(new Combo(board[0][0], board[1][1], board[2][2]));
		combos.add(new Combo(board[2][0], board[1][1], board[0][2]));

	}


	@Override
	public Board getBord() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean containsValidMove(int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void update() {
		// TODO Auto-generated method stub
		
	}
	
	protected static void checkState() {
		for (Combo combo : combos) {
			if (combo.isComplete()) {
				System.out.println("We have a winner!");
//				View.TicTacToeView.playWinAnimation(combo);
				playable = false;
				break;
			}
		}
	}
}
