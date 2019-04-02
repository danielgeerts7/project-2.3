package Controller;

import java.math.*;
import java.util.*;
import java.util.Scanner;
import java.util.Random;

public class ReversiGame {
	final static int BOARD_SIZE = 8;
	final static char BLACK = '\u26AB';
	final static char WHITE = '\u26AA';
	final static char EMPTY = '\u2B1c';
	public ArrayList<Tuple> valid_moves = new ArrayList<>();
	public Tuple[] offsets = new Tuple[8];
	
	public static void main(String[] args) {
		new ReversiGame();
	}
	
	public ReversiGame() {
		addOffsets();
		Board bord = startGame();
		char piece = BLACK;
		while(hasValidMove(bord, piece)) {
			gameLoop(bord, piece);
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
	
	public void gameLoop(Board bord, char piece) {
		int x;
		int y;
		printBoard(bord);
		while(true){
			try {
				if(piece == BLACK) {
					for(Tuple v : valid_moves) {
						System.out.print("["+v.x+","+v.y+"]");
					}
					System.out.print("\n");
					Scanner reader = new Scanner(System.in);
					System.out.println(piece + ", Enter a coordinate: ");
					String coordinate = reader.nextLine();
					List<String> coordinates = Arrays.asList(coordinate.split(","));
					x = Integer.parseInt(coordinates.get(0));
					y = Integer.parseInt(coordinates.get(1));
				}
				else {
					int randomInt = new Random().nextInt(valid_moves.size());
					x = valid_moves.get(randomInt).x;
					y = valid_moves.get(randomInt).y;
				}
				if(isValidMove(bord, piece, y, x)) {
					placePiece(bord, piece, y ,x);
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
				//System.out.print("\n" + bord.bord[check.x][check.y]);
				//System.out.print(" == " + piece);
				//System.out.println(check.x + "," + check.y);
				try {
					Character steen1 = bord.bord[check.x][check.y];
					Character steen2 = piece;
					if(steen1.equals(steen2)) {
						//System.out.println("true");
						return true;
					}
				}
				catch (IndexOutOfBoundsException e) {
					
				}
				
			}
		}
		//System.out.println("\n false");
		return false;
	}
	
	public void placePiece(Board bord, char piece, int x, int y) {
		bord.bord[x][y] = piece;
		for(Tuple offset: offsets) {
			Tuple check = new Tuple(x+offset.x, y+offset.y);
			while(0 <= check.x && check.x < BOARD_SIZE && 0 <= check.y && check.y < BOARD_SIZE) {
				if(bord.bord[check.x][check.y] == EMPTY) { break; }
				if(bord.bord[check.x][check.y] == piece) {
					flip(bord, piece, x , y, offset);
					break;
				}
				check.x += offset.x;
				check.y += offset.y;
			}
		}
	}
	
	public void flip(Board bord, char piece, int x, int y, Tuple offset) {
		Tuple check = new Tuple(x+offset.x, y+offset.y);
		while(bord.bord[check.x][check.y] == inverse(piece)) {
			bord.bord[check.x][check.y] = piece;
			check.x += offset.x;
			check.y += offset.y;
		}
	}
	
	public boolean hasValidMove(Board bord, char piece) {
		valid_moves.clear();
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
}

class Board {
	
	public char[][] bord;
	
	public Board(int size) {
		bord = new char[size][size];
		for(int x = 0; x < size; x++) {
			for(int y = 0; y < size; y++) {
				bord[x][y] = ReversiGame.EMPTY;
			}
		}
	}
	
	public char[][] getBord(){
		return bord;
	}
	
}

class Tuple {
	int x = -1;
	int y = -1;
	
	public Tuple(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
