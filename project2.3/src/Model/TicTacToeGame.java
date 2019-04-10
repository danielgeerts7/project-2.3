package Model;

//import java.util.ArrayList;
//import Controller.ClientSocket;

public class TicTacToeGame extends SuperGame {
	private final static int BOARD_SIZE = 3;
	public final static char CROSS = 'X';
	public final static char CIRCLE = 'O';
	public final static char EMPTY = 'e';
	private Board bord;
//	private ArrayList<Tuple> valid_moves = new ArrayList<>();
//	private Tuple[] offsets = new Tuple[3];
	
	public TicTacToeGame() {
		bord = startGame();
	}
	
	public Board startGame() {
		Board bord = new Board(3);
		for (int x = 0; x < BOARD_SIZE; x++) {
			for (int y = 0; y < BOARD_SIZE; y++) {
				bord.bord[x][y] = EMPTY;
			}
		}
		
		return bord;
	}
	
	public Board getBord() {
		return bord;
	}
	
}
