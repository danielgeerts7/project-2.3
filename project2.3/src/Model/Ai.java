package Model;

public class Ai {
	
//	public static void main(String[] args) {
//		//ReversiGame rg = new ReversiGame();
//		System.out.println("welkom");
//	}
	//public ReversiGame rg;
	
	public Ai() {
		
	}
	
	public int calcMove(ReversiGame rg, Board bord, char piece) {
		Board new_bord = bord;
		if(rg.hasValidMove(new_bord, piece)) {
			for(Tuple v : rg.valid_moves) {
				//rg.doMove(new_bord, piece, (v.x*8+v.y));
			}
		}
		return 19;
	}
}
