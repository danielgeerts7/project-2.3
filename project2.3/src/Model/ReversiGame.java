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
public class ReversiGame extends SuperGame {
	private final static int BOARD_SIZE = 8;
	public final static char BLACK = 'B';
	public final static char WHITE = 'W';
	public final static char EMPTY = 'o';
	private ArrayList<Tuple> valid_moves = new ArrayList<>();
	private ArrayList<Integer> weight = new ArrayList<>(); 
	private Tuple[] offsets = new Tuple[8];
	private Board bord;
	private Greedy greedy;

	/**
	 * Start the game Reversi, add the offsets and create a new board. if there are no valid moves left the amount of pieces each player has
	 * are counted and the player with the most piece of the board at that moment is the winner. 
	 */
	public ReversiGame() {
		addOffsets();
		bord = startGame();
		greedy = new Greedy();
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
	 * get the current status of the game
	 * @return current status of the game
	 */
	public Board getBord() {
		return bord;
	}

	/**
	 * Place a piece on the board
	 * @param bord  current status of the game
	 * @param piece who's turn it is
	 */
	public void doMove(char piece) {
		hasValidMove(piece);
		weight.clear();
		for (Tuple v : valid_moves) {
			System.out.print("[" + v.x + "," + v.y + "]");
			placePiece(bord, piece, v.y, v.x, false);
		}
		if (valid_moves.size() <= 0) {
			// Sla beurt over -> stuur naar server
		}
		List<Tuple> greedy_moves = greedy.greedyMove(weight, valid_moves);
		int rand = new Random().nextInt(greedy_moves.size());
		int x = greedy_moves.get(rand).x;
		int y = greedy_moves.get(rand).y;
		if (isValidMove(bord, piece, y, x)) {
			int pos = (BOARD_SIZE * y) + x;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
			placePiece(bord, piece, y, x, true);
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

	/**
	 * Place the move that the player has chosen to do. 
	 * @param bord  current status of the game
	 * @param piece who's move it is
	 * @param x     x position
	 * @param y		y position
	 * @param place if true place the piece, if false don't place the piece but checking the amount of flips
	 */
	public void placePiece(Board bord, char piece, int x, int y, boolean place) {
		int aantal = 0;
		if(place) { bord.bord[x][y] = piece; }
		else { } 
		
		for(Tuple offset: offsets) {
			Tuple check = new Tuple(x+offset.x, y+offset.y);
			while(0 <= check.x && check.x < BOARD_SIZE && 0 <= check.y && check.y < BOARD_SIZE) {
				if(bord.bord[check.x][check.y] == EMPTY) { break; }
				if(bord.bord[check.x][check.y] == piece) {
					if(place) {
						flip(bord, piece, x , y, offset, place);
					}
					else {
						aantal += flip(bord, piece, x , y, offset, place);
					}
					break;
				}
				check.x += offset.x;
				check.y += offset.y;
			}
		}
		if(!place) {
//			if(x == 0 || x == 7 || y == 0 || y == 7) {
//				aantal +=10;
//			}
//			if(x == 0 && y == 1) { aantal -= 15; } else if(x == 1 && y == 0) {  aantal -= 15; } 
//			else if(x == 1 && y == 1) { aantal -= 20; } else if(x == 6 && y == 0) { aantal -= 15; }
//			else if(x == 7 && y == 1) { aantal -= 15; } else if(x == 6 && y == 1) { aantal -= 20; }
//			else if(x == 0 && y == 6) { aantal -= 15; } else if(x == 1 && y == 7) { aantal -= 15; }
//			else if(x == 1 && y == 6) { aantal -= 20; } else if(x == 6 && y == 6) { aantal -= 20; }
//			else if(x == 6 && y == 7) { aantal -= 15; } else if(x == 7 && y == 6) { aantal -= 15; } 
//			
//			else if(x == 0 && y == 0) { aantal += 30; } else if(x == 0 && y == 7) { aantal += 30; }
//			else if(x == 7 && y == 0) { aantal += 30; } else if(x == 7 && y == 7) { aantal += 30; }
//			else { }
			
			
			if(x == 0 && y == 0) { aantal += 100; } else if(x == 1 && y == 0) { aantal -= 20; }
			else if(x == 2 && y == 0) { aantal += 10; } else if(x == 3 && y == 0) { aantal += 5; }
			else if(x == 4 && y == 0) { aantal += 5; } else if(x == 5 && y == 0) { aantal += 10; }
			else if(x == 6 && y == 0) { aantal -= 20; } else if(x == 7 && y == 0) { aantal += 100; }
			
			else if(x == 0 && y == 1) { aantal -= 20; } else if(x == 1 && y == 1) { aantal -= 50; }
			else if(x == 2 && y == 1) { aantal -= 2; } else if(x == 3 && y == 1) { aantal -= 2; }
			else if(x == 4 && y == 1) { aantal -= 2; } else if(x == 5 && y == 1) { aantal -= 2; }
			else if(x == 6 && y == 1) { aantal -= 50; } else if(x == 7 && y == 1) { aantal -= 20; }
			
			else if(x == 0 && y == 2) { aantal += 10; } else if(x == 1 && y == 2) { aantal -= 2; }
			else if(x == 2 && y == 2) { aantal -= 1; } else if(x == 3 && y == 2) { aantal -= 1; }
			else if(x == 4 && y == 2) { aantal -= 1; } else if(x == 5 && y == 2) { aantal -= 1; }
			else if(x == 6 && y == 2) { aantal -= 2; } else if(x == 7 && y == 2) { aantal += 10; }
			
			else if(x == 0 && y == 3) { aantal += 5; } else if(x == 1 && y == 3) { aantal -= 2; }
			else if(x == 2 && y == 3) { aantal -= 1; } else if(x == 3 && y == 3) { aantal -= 1; }
			else if(x == 4 && y == 3) { aantal -= 1; } else if(x == 5 && y == 3) { aantal -= 1; }
			else if(x == 6 && y == 3) { aantal -= 2; } else if(x == 7 && y == 3) { aantal += 5; }
			
			else if(x == 0 && y == 4) { aantal += 5; } else if(x == 1 && y == 4) { aantal -= 2; }
			else if(x == 2 && y == 4) { aantal -= 1; } else if(x == 3 && y == 4) { aantal -= 1; }
			else if(x == 4 && y == 4) { aantal -= 1; } else if(x == 5 && y == 4) { aantal -= 1; }
			else if(x == 6 && y == 4) { aantal -= 2; } else if(x == 7 && y == 4) { aantal += 5; }
			
			else if(x == 0 && y == 5) { aantal += 10; } else if(x == 1 && y == 5) { aantal -= 2; }
			else if(x == 2 && y == 5) { aantal -= 2; } else if(x == 3 && y == 5) { aantal -= 1; }
			else if(x == 4 && y == 5) { aantal -= 1; } else if(x == 5 && y == 5) { aantal -= 1; }
			else if(x == 6 && y == 5) { aantal -= 2; } else if(x == 7 && y == 5) { aantal += 10; }
			
			else if(x == 0 && y == 6) { aantal -= 20; } else if(x == 1 && y == 6) { aantal -= 50; }
			else if(x == 2 && y == 6) { aantal -= 2; } else if(x == 3 && y == 6) { aantal -= 2; }
			else if(x == 4 && y == 6) { aantal -= 2; } else if(x == 5 && y == 6) { aantal -= 2; }
			else if(x == 6 && y == 6) { aantal -= 50; } else if(x == 7 && y == 6) { aantal -= 20; }
			
			else if(x == 0 && y == 7) { aantal += 100; } else if(x == 1 && y == 7) { aantal -= 20; }
			else if(x == 2 && y == 7) { aantal += 10; } else if(x == 3 && y == 7) { aantal += 5; }
			else if(x == 4 && y == 7) { aantal += 5; } else if(x == 5 && y == 7) { aantal += 10; }
			else if(x == 6 && y == 7) { aantal -= 20; } else if(x == 7 && y == 7) { aantal += 100; }
			else { }
			
			System.out.println(" : " + aantal);
			weight.add(aantal);
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
	public int flip(Board bord, char piece, int x, int y, Tuple offset, boolean place) {
		int aantal = 0;
		Tuple check = new Tuple(x+offset.x, y+offset.y);
		while(bord.bord[check.x][check.y] == inverse(piece)) {
			if(place) {
				bord.bord[check.x][check.y] = piece;
			} else {
				aantal++;
			}
			check.x += offset.x;
			check.y += offset.y;
		}
		return aantal;
	}

	/**
	 * check if a player has a valid move to do
	 * @param bord  current status of the game
	 * @param piece who's turn it is
	 * @return true if has valid move, false if not
	 */
	public boolean hasValidMove(char piece) {
		//System.out.println("do: hasValidMove!");
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
