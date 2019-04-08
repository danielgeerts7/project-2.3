package Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Random;
import java.util.*;

/**
 * This is the game Reversi that can be played. 
 * @author Created by Casper
 *
 */
public class ReversiGame {
	final static int BOARD_SIZE = 8;
	final static char BLACK = '\u26AB';
	final static char WHITE = '\u26AA';
	final static char EMPTY = '\u2B1c';
	private ArrayList<Tuple> valid_moves = new ArrayList<>();
	private ArrayList<Integer> weight = new ArrayList<>(); 
	private Tuple[] offsets = new Tuple[8];
	private Board bord;
	public ReversiGame rg;
	private Greedy greedy;
	private static char piece;
	
	/**
	 * Start the game Reversi, add the offsets and create a new board. if there are no valid moves left the amount of pieces each player has
	 * are counted and the player with the most piece of the board at that moment is the winner. 
	 */
	public ReversiGame() {
		addOffsets();
		Board bord = startGame();
		greedy = new Greedy();
		piece = BLACK;
		while(hasValidMove(bord, piece)) {
			doMove(bord, piece);
			if(hasValidMove(bord, inverse(piece))) {
				piece = inverse(piece);
			}
		}
		printBoard(bord);
		int black = 0;
		int white = 0;
		for(char[] b : bord.bord) {
			for(char c : b) {
				if(c == WHITE) { white += 1; }
				if(c == BLACK) { black += 1; }
			}
		}
		if(black == white) {
			System.out.println("It's a tie!");
		}
		else {
			char winner;
			System.out.println("Black: " + black);
			System.out.println("White: " + white);
			if(black > white) { winner = BLACK; }
			else { winner = WHITE; }
			System.out.println(winner + "is the winner");
		}
	}
	
	/**
	 * Inverses the current piece to the next piece
	 * @param  piece current piece
	 * @return next  piece
	 */
	public char inverse(char piece) {
		if(piece == WHITE) { return BLACK; } 
		else { return WHITE; }
	}
	
	/**
	 * Adding the offsets for the possible moves
	 */
	private void addOffsets() {
		offsets[0] = new Tuple(0,1);  offsets[1] = new Tuple(0,-1);
		offsets[2] = new Tuple(1,0);  offsets[3] = new Tuple(1,1);
		offsets[4] = new Tuple(1,-1); offsets[5] = new Tuple(-1,0);
		offsets[6] = new Tuple(-1,1); offsets[7] = new Tuple(-1,-1);
	}
	
	/**
	 * create new Reversi board with the start position
	 * @return new bord
	 */
	public Board startGame() {
		Board bord  = new Board(8);
		for(int x = 0; x < BOARD_SIZE; x++) {
			for(int y = 0; y < BOARD_SIZE; y++) {
				bord.bord[x][y] = EMPTY;
			}
		}
		int half = BOARD_SIZE/2;
		bord.bord[half-1][half-1] = WHITE;
		bord.bord[half][half] = WHITE;
		bord.bord[half][half-1] = BLACK;
		bord.bord[half-1][half] = BLACK;
		return bord;
		
	}
	
	/**
	 * prints the current positions of each piece
	 * @param bord : current status of the game
	 */
	public void printBoard(Board bord) {
		int row = 0;
		System.out.println(" 0  1 2 3  4 5 6 7");
		for(char[] b : bord.bord) {
			System.out.print(row++);
			for(char c : b) {
				System.out.print(c);
			}
			System.out.print("\n");
		}
	}
	
	/**
	 * Place a piece on the board
	 * @param bord  current status of the game
	 * @param piece who's turn it is
	 */
	public void doMove(Board bord, char piece) {
		int x;
		int y;
		printBoard(bord);
		while(true){
			try {	
				for(Tuple v : valid_moves) {
					System.out.print("["+v.x+","+v.y+"]");
					placePiece(bord, piece, v.y, v.x, false);
				}
				List<Integer> coordinates = greedy.getMove(getBiggest(), valid_moves);
				x = coordinates.get(0);
				y = coordinates.get(1);
			
				if(isValidMove(bord, piece, y, x)) {
					placePiece(bord, piece, y ,x, true);
					return;
				}
				else {
					System.out.println("oei oei hij doet het niet!");
				}
			}
			catch (NumberFormatException | AssertionError | IndexOutOfBoundsException | InputMismatchException ex ) {
				System.out.println("Oei oei");
			}
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
		if(bord.bord[x][y] != EMPTY) { return false; }
		for(int offset = 0; offset < offsets.length; offset++ ) {
			Tuple check = new Tuple(x+offsets[offset].x, y+offsets[offset].y);
			while(0 <= check.x && check.x < BOARD_SIZE-1 && 0 <= check.y && check.y < BOARD_SIZE-1 && bord.bord[check.x][check.y] == inverse(piece)) {
				check.x += offsets[offset].x;
				check.y += offsets[offset].y;
				try {
					Character steen1 = bord.bord[check.x][check.y];
					Character steen2 = piece;
					if(steen1.equals(steen2)) {
						return true;
					}
				}
				catch (IndexOutOfBoundsException e) {
					
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
	public boolean hasValidMove(Board bord, char piece) {
		valid_moves.clear();
		weight.clear();
		for(int y = 0; y < BOARD_SIZE; y++) {
			for(int x = 0; x < BOARD_SIZE; x++) {
				if(isValidMove(bord, piece, y, x)) {
					valid_moves.add(new Tuple(x,y));
				}
			}
		}
		if(valid_moves.size() > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * get the move with most flips at this moment
	 * @return index of the move with the most flips
	 */
	public int getBiggest() {
		ArrayList<Integer> a = weight;
		int biggestIndex = 0;
		if(a.size() < 1) {
			return 0;
		} else {
			int big = Collections.max(a);
			for(int i = 0; i < a.size(); i++) {
				if(a.get(i) == big) {
					biggestIndex = i;
				}
			}
		}
		return biggestIndex;
	}
	
	/**
	 * get the current status of the game
	 * @return current status of the game
	 */
	public Board getBoard() { return bord; }
	
	/**
	 * get who's turn it is
	 * @return who's turn it is
	 */
	public static char getTurn() { return piece; }
}


