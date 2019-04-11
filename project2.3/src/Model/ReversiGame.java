package Model;

import java.util.ArrayList;

import Controller.ClientSocket;
import Controller.GameController;
import View.GameView;
import View.Popup;

public class ReversiGame extends SuperGame {
	private final static int BOARD_SIZE = 8;
	public final static char BLACK = 'B';
	public final static char WHITE = 'W';
	public final static char EMPTY = 'o';
	private ArrayList<Tuple> valid_moves = new ArrayList<>();
	private Tuple[] offsets = new Tuple[8];
	private Board bord;

	private GameView viewRef = null;
	private boolean playRemote = false;
	private static boolean playerCanMove = true;
	private static boolean tileIsClicked = false;
	private static int tileX, tileY = 0;
	private boolean gameFinished = false;
	private boolean popupAlreadyOpen = false;
	private boolean player1outOfMoves = false;
	private boolean player2outOfMoves = false;

	public ReversiGame(GameView view, boolean playRemote) {
		super();
		this.viewRef = view;
		this.playRemote = playRemote;
		addOffsets();
		bord = startGame();
	}

	/**
	 * This method is called every available frame
	 */
	@Override
	protected void update() {
		if (!playRemote && !gameFinished) {
			if (getPieces(viewRef.getPlayer1().getColor()) + getPieces(viewRef.getPlayer2().getColor()) == 64) {
				gameFinished = player1outOfMoves = player2outOfMoves = true;
			} else if (playerCanMove) {
				hasValidMove(viewRef.getPlayer1().getColor());
				if (valid_moves.size() > 0) {
					player1outOfMoves = false;
					player2outOfMoves = false;
					if (tileIsClicked) {
						int move = (BOARD_SIZE * tileX) + tileY;
						playerCanMove = false;
						tileIsClicked = false;
						GameController.receivedMove(viewRef.getPlayer1().getName(), Integer.toString(move));
					}
				} else {
					System.out.println("Player1: Out of moves");
					player1outOfMoves = true;
					playerCanMove = false;
				}
			} else {
				hasValidMove(viewRef.getPlayer2().getColor());
				if (valid_moves.size() > 0) {
					try {
						Thread.sleep(250);
					} catch (InterruptedException e) {
						e.printStackTrace();
					};
					player1outOfMoves = false;
					player2outOfMoves = false;
					int move = (BOARD_SIZE * valid_moves.get(0).y) + valid_moves.get(0).x;
					playerCanMove = true;
					GameController.receivedMove(viewRef.getPlayer2().getName(), Integer.toString(move));
				} else {
					System.out.println("Computer: Out of moves");
					player2outOfMoves = true;
					playerCanMove = true;
				}
			}
		}

		if (!popupAlreadyOpen && player1outOfMoves && player2outOfMoves) {
			gameFinished = true;
			String text = "";
			if (getPieces(viewRef.getPlayer1().getColor()) + getPieces(viewRef.getPlayer2().getColor()) == 64) {
				text = "Game is over!";
			} else if (player1outOfMoves) {
				text = "You are out of moves, but";
			} else if (player2outOfMoves) {
				text = "Computer is out of moves!";
			} 
			
			if (getPieces(viewRef.getPlayer1().getColor()) > getPieces(viewRef.getPlayer2().getColor())) {
				Popup.getInstance().newPopup(text + " You win", Popup.Type.WIN);
			} else if (getPieces(viewRef.getPlayer1().getColor()) < getPieces(viewRef.getPlayer2().getColor())) {
				Popup.getInstance().newPopup(text + " You lose", Popup.Type.LOSS);
			} else if (getPieces(viewRef.getPlayer1().getColor()) == getPieces(viewRef.getPlayer2().getColor())) {
				Popup.getInstance().newPopup(text + " Draw", Popup.Type.DRAW);
			}
			popupAlreadyOpen = true;
		}
	}

	public static void tileIsClicked(int x, int y) {
		tileIsClicked = true;
		tileX = x;
		tileY = y;
	}

	public static boolean isPlayersTurn() {
		return playerCanMove;
	}

	public static char inverse(char piece) {
		if (piece == WHITE) {
			return BLACK;
		} else {
			return WHITE;
		}
	}

	private void addOffsets() {
		offsets[0] = new Tuple(0, 1);
		offsets[1] = new Tuple(0, -1);
		offsets[2] = new Tuple(1, 0);
		offsets[3] = new Tuple(1, 1);
		offsets[4] = new Tuple(1, -1);
		offsets[5] = new Tuple(-1, 0);
		offsets[6] = new Tuple(-1, 1);
		offsets[7] = new Tuple(-1, -1);
	}

	public Board startGame() {
		Board bord = new Board(8);
		for (int x = 0; x < BOARD_SIZE; x++) {
			for (int y = 0; y < BOARD_SIZE; y++) {
				bord.bord[x][y] = EMPTY;
			}
		}
		int half = BOARD_SIZE / 2;
		bord.bord[half - 1][half - 1] = WHITE;
		bord.bord[half][half] = WHITE;
		bord.bord[half][half - 1] = BLACK;
		bord.bord[half - 1][half] = BLACK;
		return bord;

	}

	private void printBoard(Board bord) {
		int row = 0;
		System.out.println(" 0  1 2 3  4 5 6 7");
		for (char[] b : bord.bord) {
			System.out.print(row++);
			for (char c : b) {
				System.out.print(c);
			}
			System.out.print("\n");
		}
	}

	public Board getBord() {
		return bord;
	}

	public void doMove(char piece) {
		hasValidMove(piece);
		/*for (Tuple v : valid_moves) {
			System.out.print("[" + v.x + "," + v.y + "]");
		}*/
		if (valid_moves.size() <= 0) {
			// TODO: sla beurt over -> stuur naar server
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
		int x = pos % BOARD_SIZE;
		int y = pos / BOARD_SIZE;
		if (isValidMove(bord, piece, y, x)) {
			placePiece(bord, piece, y, x);
			return;
		} else {
			System.out.println("oei oei hij doet het niet!");
		}
	}

	public boolean isValidMove(Board bord, char piece, int x, int y) {
		if (bord.bord[x][y] != EMPTY) {
			return false;
		}
		for (int offset = 0; offset < offsets.length; offset++) {
			Tuple check = new Tuple(x + offsets[offset].x, y + offsets[offset].y);
			while (0 <= check.x && check.x < BOARD_SIZE && 0 <= check.y && check.y < BOARD_SIZE
					&& bord.bord[check.x][check.y] == inverse(piece)) {
				check.x += offsets[offset].x;
				check.y += offsets[offset].y;
				try {
					Character steen1 = bord.bord[check.x][check.y];
					Character steen2 = piece;
					if (steen1.equals(steen2)) {
						return true;
					}
				} catch (IndexOutOfBoundsException e) {

				}

			}
		}
		return false;
	}

	public void placePiece(Board bord, char piece, int x, int y) {
		bord.bord[x][y] = piece;
		for (Tuple offset : offsets) {
			Tuple check = new Tuple(x + offset.x, y + offset.y);
			while (0 <= check.x && check.x < BOARD_SIZE && 0 <= check.y && check.y < BOARD_SIZE) {
				if (bord.bord[check.x][check.y] == EMPTY) {
					break;
				}
				if (bord.bord[check.x][check.y] == piece) {
					flip(bord, piece, x, y, offset);
					break;
				}
				check.x += offset.x;
				check.y += offset.y;
			}
		}
	}

	public static void flip(Board bord, char piece, int x, int y, Tuple offset) {
		Tuple check = new Tuple(x + offset.x, y + offset.y);
		while (bord.bord[check.x][check.y] == inverse(piece)) {
			bord.bord[check.x][check.y] = piece;
			check.x += offset.x;
			check.y += offset.y;
		}
	}

	public boolean hasValidMove(char piece) {
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

	@Override
	public boolean containsValidMove(int x, int y) {
		hasValidMove(BLACK);
		for (int i = 0; i < valid_moves.size(); i++) {
			if (valid_moves.get(i).x == x && valid_moves.get(i).y == y) {
				return true;
			}
		}
		return false;
	}

	private int getPieces(char piece) {
		int amount = 0;
		for (int y = 0; y < BOARD_SIZE; y++) {
			for (int x = 0; x < BOARD_SIZE; x++) {
				if (bord.getBord()[x][y] == piece) {
					amount++;
				}
			}
		}
		return amount;
	}
}
