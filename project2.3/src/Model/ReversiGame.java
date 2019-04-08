package Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Random;
import java.util.*;

public class ReversiGame {
	final static int BOARD_SIZE = 8;
	final static char BLACK = '\u26AB';
	final static char WHITE = '\u26AA';
	final static char EMPTY = '\u2B1c';
	public ArrayList<Tuple> valid_moves = new ArrayList<>();
	public ArrayList<Integer> weight = new ArrayList<>(); 
	public Tuple[] offsets = new Tuple[8];
	private Board bord;
	public Ai ai;
	public ReversiGame rg;
	
	public static void main(String[] args) {
		for(int i = 0; i < 1; i++) {
			ReversiGame rg = new ReversiGame();
		}
	}
	
	public ReversiGame() {
		ai = new Ai();
		addOffsets();
		Board bord = startGame();
		char piece = BLACK;
		while(hasValidMove(bord, piece)) {
			//System.out.println(ai.calcMove(bord, piece));
			doMove(bord, piece, ai.calcMove(rg, bord, piece));
			//gameLoop(bord, piece);
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
	
	public char inverse(char piece) {
		if(piece == WHITE) { return BLACK; } 
		else { return WHITE; }
	}
	
	private void addOffsets() {
		offsets[0] = new Tuple(0,1);
		offsets[1] = new Tuple(0,-1);
		offsets[2] = new Tuple(1,0);
		offsets[3] = new Tuple(1,1);
		offsets[4] = new Tuple(1,-1);
		offsets[5] = new Tuple(-1,0);
		offsets[6] = new Tuple(-1,1);
		offsets[7] = new Tuple(-1,-1);
	}
	
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
	
	public Board getBoard() {
		return bord;
	}
	
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
	
	public void doMove(Board bord, char piece, int move) {
		int x = move/8;
		int y = move%8;
//		printBoard(bord);
//		if(isValidMove(bord, piece, y, x)) {
//			placePiece(bord, piece, y ,x, true);
//			return;
//		}
//		else {
//			System.out.println("oei oei hij doet het niet!");
//		}
		if(isValidMove(bord, piece, y, x)) {
			placePiece(bord, piece, y ,x, true);
			return;
		}
	}
	
	public void gameLoop(Board bord, char piece) {
		int x;
		int y;
		while(true){
			try {
			if(piece == BLACK) {
//					printBoard(bord);
//					for(Tuple v : valid_moves) {
//						System.out.print("["+v.x+","+v.y+"]");
//						placePiece(bord, piece, v.y, v.x, false);
//					}
//					System.out.print("\n");
//					Scanner reader = new Scanner(System.in);
//					System.out.println(piece + ", Enter a coordinate: ");
//					String coordinate = reader.nextLine();
//					List<String> coordinates = Arrays.asList(coordinate.split(","));
//					x = Integer.parseInt(coordinates.get(0));
//					y = Integer.parseInt(coordinates.get(1));
					int randomInt = new Random().nextInt(valid_moves.size());
					//int biggest = getBiggest(weight);
					x = valid_moves.get(randomInt).x;
					y = valid_moves.get(randomInt).y;
				}
				else {
					printBoard(bord);
					for(Tuple v : valid_moves) {
						System.out.print("["+v.x+","+v.y+"]");
						placePiece(bord, piece, v.y, v.x, false);
					}
					//int randomInt = new Random().nextInt(valid_moves.size());
					int biggest = getBiggest(weight);
					x = valid_moves.get(biggest).x;
					y = valid_moves.get(biggest).y;
					System.out.println(Collections.max(weight));
				}
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
	
	public int getBiggest(ArrayList<Integer> a) {
		int biggestIndex = 0;
		if(a.size() < 1) {
			return 0;
		} else {
			int big = Collections.max(a);
			for(int i = 0; i < a.size(); i++) {
//				if(a.get(i) < a.indexOf(biggestIndex)) {
//					biggestIndex = i;
//					System.out.println(a.get(i) + " is groter dan " + a.indexOf(biggestIndex));
//				}
				if(a.get(i) == big) {
					biggestIndex = i;
				}
			}
		}
		return biggestIndex;
	}
}


