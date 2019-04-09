package Model;

public class Board {
	
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
