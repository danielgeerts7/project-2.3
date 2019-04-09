package Model;

import java.util.ArrayList;

import Controller.ClientSocket;

public class TicTacToeGame extends SuperGame {
	private final static int BOARD_SIZE = 3;
	public final static char CROSS = 'X';
	public final static char CIRCLE = 'O';
	public final static char EMPTY = 'e';
	private Board bord;
	private ArrayList<Tuple> valid_moves = new ArrayList<>();
	private Tuple[] offsets = new Tuple[3];


	public TicTacToeGame() {
//		addOffsets();
		bord = startGame();
	}


//	private void addOffsets() {
//		offsets[0] = new Tuple(0, 1);
//		offsets[1] = new Tuple(0, -1);
//		offsets[2] = new Tuple(1, 0);
//		offsets[3] = new Tuple(1, 1);
//		offsets[4] = new Tuple(1, -1);
//		offsets[5] = new Tuple(-1, 0);
//		offsets[6] = new Tuple(-1, 1);
//		offsets[7] = new Tuple(-1, -1);
//	}

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

	public void doMove(char piece) {
		hasValidMove(piece);
		for (Tuple v : valid_moves) {
			System.out.print("[" + v.x + "," + v.y + "]");
		}
		if (valid_moves.size() <= 0) {
			// Sla beurt over -> stuur naar server
		}
		int rand = (int )(Math.random() * valid_moves.size());
		int x = valid_moves.get(rand).x;
		int y = valid_moves.get(rand).y;
		if (isValidMove(bord, piece, y, x)) {
			int pos = (BOARD_SIZE * y) + x;
			ClientSocket.getInstance(true).sendMove(pos);
			return;
		} else {
			System.out.println("oei oei hij doet het niet!");
		}
	}

	public void receivedMove(char piece, int pos) {
		int x;
		int y;
		x = pos % BOARD_SIZE;
		y = pos / BOARD_SIZE;
		if (isValidMove(bord, piece, y, x)) {
			return;
		} else {
			System.out.println("oei oei hij doet het niet!");
		}
	}

	public boolean isValidMove(Board bord, char piece, int x, int y) {
		if (bord.bord[x][y] != EMPTY) {
			return false;
		}
		return false;
	}

	public boolean hasValidMove(char piece) {
		System.out.println("do: hasValidMove!");
		valid_moves.clear();
		for (int y = 0; y < BOARD_SIZE; y++) {
			for (int x = 0; x < BOARD_SIZE; x++) {
				if (isValidMove(bord, piece, y, x)) {
					valid_moves.add(new Tuple(x, y));
				}
			}
		}
		if (valid_moves.size() > 0) {
			return true;
		} else {
			return false;
		}
	}
}
