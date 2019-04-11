package Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import Controller.ClientSocket;
import Controller.GameController;
import View.GameView;
import View.Popup;

import java.util.Random;
import java.util.*;

public class ReversiGame extends SuperGame {
	private final static int BOARD_SIZE = 8;
	public final static char BLACK = 'B';
	public final static char WHITE = 'W';
	public final static char EMPTY = 'o';
	private ArrayList<Tuple> valid_moves = new ArrayList<>();
	private Tuple[] offsets = new Tuple[8];
	private Board bord;

	private boolean playRemote = false;
	private static boolean playerCanMove = true;
	private static boolean tileIsClicked = false;
	private static int tileX, tileY = 0;
	private static boolean popupAlreadyOpen = false;

	public ReversiGame(boolean playRemote) {
		super();
		this.playRemote = playRemote;
		addOffsets();
		bord = startGame();
	}

	/**
	 * This method is called every available frame
	 */
	@Override
	protected void update() {
		if (!playRemote) {
			if (playerCanMove) {
				hasValidMove(GameView.getPlayer1().getColor());
				if (tileIsClicked) {
					if (valid_moves.size() > 0) {
						int move = (BOARD_SIZE * tileX) + tileY;
						playerCanMove = false;
						tileIsClicked = false;
						GameController.receivedMove(GameView.getPlayer1().getName(), Integer.toString(move));
					} else if (!popupAlreadyOpen) {
						Popup.getInstance().newPopup("You are out of moves! You lose", Popup.Type.LOSS);
						popupAlreadyOpen = true;
					}
				}
			} else {
				hasValidMove(GameView.getPlayer2().getColor());
				if (valid_moves.size() > 0) {
					int move = (BOARD_SIZE * valid_moves.get(0).x) + valid_moves.get(0).y;
					playerCanMove = true;
					GameController.receivedMove(GameView.getPlayer2().getName(), Integer.toString(move));
				} else if (!popupAlreadyOpen) {
					Popup.getInstance().newPopup("Computer out of moves! You won", Popup.Type.WIN);
					popupAlreadyOpen = true;
				}
			}
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
/*
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
*/
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
		int x;
		int y;
		x = pos % BOARD_SIZE;
		y = pos / BOARD_SIZE;
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
			Tuple move = valid_moves.get(i);
			if (move.x == x && move.y == y) {
				return true;
			}
		}
		return false;
	}
}
