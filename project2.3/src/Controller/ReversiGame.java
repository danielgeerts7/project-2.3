package Controller;

import java.math.*;
import java.util.*;

public class ReversiGame {
	final static int BOARD_SIZE = 8;
	final static char BLACK = '\u26AB';
	final static char WHITE = '\u26AA';
	final static char EMPTY = '\u2B1c';
	HashSet<Integer> valid_moves = new HashSet<Integer>();
	public Tuple[] offsets = new Tuple[8];
	
	public static void main(String[] args) {
		new ReversiGame();
	}
	
	public ReversiGame() {
		addOffsets();
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
	
	public void startGame() {
		Board bord  = new Board(8);
		
	}
	
	public boolean isValidMove(Board bord, char piece, int x, int y) {
		if(bord.bord[x][y] == EMPTY) { return false; }
		for(int offset = 0; offset < offsets.length; offset++ ) {
			Tuple check = new Tuple(x+offsets[offset].x, y+offsets[offset].y);
			while(0 <= check.x && check.x < BOARD_SIZE-1 && 0 <= check.y && check.y < BOARD_SIZE-1
					&& bord.bord[check.x][check.y] == inverse(piece)) {
				check.x += offsets[offset].x;
				check.y += offsets[offset].y;
				if(bord.bord[check.x][check.y] == piece) {
					return true;
				}
			}
		}
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
	}
	
	public boolean hasValidMove(Board bord, char piece) {
		for(int x = 0; x < BOARD_SIZE; x++) {
			for(int y = 0; y < BOARD_SIZE; y++) {
				if(isValidMove(bord, piece, x, y)) {
					return true;
				}
			}
		}
		return false;
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
	static int x = -1;
	static int y = -1;
	
	public Tuple(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
