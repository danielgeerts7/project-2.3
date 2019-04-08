package Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import Controller.ClientSocket;
import View.GameView;

import java.util.Random;
import java.util.*;

public class ReversiGame {
	private final static int BOARD_SIZE = 8;
	public final static char BLACK = 'B';
	public final static char WHITE = 'W';
	public final static char EMPTY = 'o';
	private static ArrayList<Tuple> valid_moves = new ArrayList<>();
	private static Tuple[] offsets = new Tuple[8];
	private static Board bord;

	public ReversiGame() {
		addOffsets();
		bord = startGame();
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

	public Board getBord() {
		return bord;
	}

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
			return;
		} else {
			System.out.println("oei oei hij doet het niet!");
		}
	}

	public static boolean isValidMove(Board bord, char piece, int x, int y) {
		if (bord.bord[x][y] != EMPTY) {
			return false;
		}
		for (int offset = 0; offset < offsets.length; offset++) {
			Tuple check = new Tuple(x + offsets[offset].x, y + offsets[offset].y);
			while (0 <= check.x && check.x < BOARD_SIZE - 1 && 0 <= check.y && check.y < BOARD_SIZE - 1
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

	public static void flip(Board bord, char piece, int x, int y, Tuple offset) {
		Tuple check = new Tuple(x + offset.x, y + offset.y);
		while (bord.bord[check.x][check.y] == inverse(piece)) {
			bord.bord[check.x][check.y] = piece;
			check.x += offset.x;
			check.y += offset.y;
		}
	}

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
