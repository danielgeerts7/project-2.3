package Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import Controller.ClientSocket;
import View.GameView;

import java.util.Random;
import java.util.*;

/**
 * This is the game Reversi that can be played. 
 * @author Created by Casper
 *
 */
public class ReversiGame {
	private final static int BOARD_SIZE = 8;
	public final static char BLACK = 'B';
	public final static char WHITE = 'W';
	public final static char EMPTY = 'o';
	private static ArrayList<Tuple> valid_moves = new ArrayList<>();
	private ArrayList<Integer> weight = new ArrayList<>(); 
	private static Tuple[] offsets = new Tuple[8];
	private static Board bord;
	private Greedy greedy;

	/**
	 * Start the game Reversi, add the offsets and create a new board. if there are no valid moves left the amount of pieces each player has
	 * are counted and the player with the most piece of the board at that moment is the winner. 
	 */
	public ReversiGame() {
		addOffsets();
		bord = startGame();
	}
	
	/**
	 * Inverses the current piece to the next piece
	 * @param  piece current piece
	 * @return next  piece
	 */
	public static char inverse(char piece) {
		if (piece == WHITE) {
			return BLACK;
		} else {
			return WHITE;
		}
	}
	
	/**
	 * Adding the offsets for the possible moves
	 */
	private void addOffsets() {
		offsets[0] = new Tuple(0, 1);  offsets[1] = new Tuple(0, -1);
		offsets[2] = new Tuple(1, 0);  offsets[3] = new Tuple(1, 1);
		offsets[4] = new Tuple(1, -1); offsets[5] = new Tuple(-1, 0);
		offsets[6] = new Tuple(-1, 1); offsets[7] = new Tuple(-1, -1);
	}

	/**
	 * create new Reversi board with the start position
	 * @return new bord
	 */
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

	/**
	 * prints the current positions of each piece
	 * @param bord : current status of the game
	 */
	public void printBoard(Board bord) {
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
	
	/**
	 * Place a piece on the board
	 * @return returns the current status of the board
	public Board getBord() {
		return bord;
	}

	/**
	 * Place a piece on the board
	 * @param bord  current status of the game
	 * @param piece who's turn it is
	 */
	public static void doMove(char piece) {
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
			// placePiece(bord, piece, y, x);
			System.out.println("x: " + x + ", y: " + y);
			int pos = (BOARD_SIZE * y) + x;
			ClientSocket.getInstance(true).sendMove(pos);
			return;
		} else {
			System.out.println("oei oei hij doet het niet!");
		}
	}

	public static void receivedMove(char piece, int pos) {
		int x;
		int y;
		x = pos % BOARD_SIZE;
		y = pos / BOARD_SIZE;
		if (isValidMove(bord, piece, y, x)) {
			placePiece(bord, piece, y, x);
			// ClientSocket.getInstance(true).sendMove(pos);
			return;
		} else {
			System.out.println("oei oei hij doet het niet!");
		}
	}

	/**
	 * Check if the given move is valid or not
	 * @param bord  current status of the game
	 * @param piece who's move it is
	 * @param x     x position on the board
	 * @param y     y position on the board
	 * @return true or false if the move is valid or not
	 */
	public static boolean isValidMove(Board bord, char piece, int x, int y) {
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
	
	/**
	 * Place the move that the player has chosen to do. 
	 * @param bord  current status of the game
	 * @param piece who's move it is
	 * @param x     x position
	 * @param y		y position
	 * @param place if true place the piece, if false don't place the piece but checking the amount of flips
	 */
	public static void placePiece(Board bord, char piece, int x, int y) {
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

	/**
	 * flip the piece in the valid direction
	 * @param bord   current status of the game 
	 * @param piece  who's turn it is
	 * @param x		 x position
	 * @param y 	 y position
	 * @param offset direction of pieces to flip
	 * @param place  place if true place the piece, if false don't place the piece but checking the amount of flips
	 * @return
	 */
	public static void flip(Board bord, char piece, int x, int y, Tuple offset) {
		Tuple check = new Tuple(x + offset.x, y + offset.y);
		while (bord.bord[check.x][check.y] == inverse(piece)) {
			bord.bord[check.x][check.y] = piece;
			check.x += offset.x;
			check.y += offset.y;
		}
	}
	
	/**
	 * check if a player has a valid move to do
	 * @param bord  current status of the game
	 * @param piece who's turn it is
	 * @return true if has valid move, false if not
	 */
	public static boolean hasValidMove(char piece) {
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
