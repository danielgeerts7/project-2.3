package Model;

/**
 * a board object for the game Reversi
 * @author Created by Casper
 *
 */
public class Board {
	
	public char[][] bord;
	
	/**
	 * create an empty board
	 * @param size the size the new board has to be
	 */
	public Board(int size) {
		bord = new char[size][size];
		for(int x = 0; x < size; x++) {
			for(int y = 0; y < size; y++) {
				bord[x][y] = ReversiGame.EMPTY;
			}
		}
	}
	
	/**
	 * returns the current board
	 * @return the current board
	 */
	public char[][] getBord(){
		return bord;
	}
	
	/**
	 * returns a cloned board
	 * @return a cloned board
	 */
	public char[][] getCloneBoard(){
		return bord.clone();
	}
	
	/**
	 * set a given board as the current board
	 * @param newB new board
	 */
	public void setBord(char[][] newB){
		this.bord = newB;
	}
	
	
}
