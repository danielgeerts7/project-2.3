package Model;

import java.util.ArrayList;

import Controller.ClientSocket;
import Controller.GameController;
import View.GameView;
import View.Popup;

/**
 * This is the game Reversi that can be played. 
 * @author Created by Casper
 *
 */
public class ReversiGame extends SuperGame {
	private final static int BOARD_SIZE = 8;
	public final static char BLACK = '\u26AB';
	public final static char WHITE = '\u26AA';
	public final static char EMPTY = '\u2B1c';
	private ArrayList<Tuple> valid_moves = new ArrayList<>();
	private ArrayList<Integer> weight = new ArrayList<Integer>();
	private Tuple[] offsets = new Tuple[8];
	private Board bord;
	private Greedy greedy;
	private int infinity = 999999999;
	private int maxDepth = 4;

	private GameView viewRef = null;
	private boolean playRemote = false;
	private static boolean playerCanMove = true;
	private static boolean tileIsClicked = false;
	/**
	 * Start the game Reversi, add the offsets and create a new board. if there are no valid moves left the amount of pieces each player has
	 * are counted and the player with the most piece of the board at that moment is the winner. 
	 */
	private static int tileX, tileY = 0;
	private boolean gameFinished = false;
	private boolean popupAlreadyOpen = false;
	private boolean player1outOfMoves = false;
	private boolean player2outOfMoves = false;

	public ReversiGame(GameView view, boolean playRemote) {
		super();
		this.viewRef = view;
		this.playRemote = playRemote;
		addOffsets();
		bord = startGame();
		greedy = new Greedy();
	}

	/**
	 * This method is called every available frame
	 */
	@Override
	protected void update() {
		if (!playRemote && !gameFinished) {
			if (getPieces(viewRef.getPlayer1().getColor()) + getPieces(viewRef.getPlayer2().getColor()) == 64) {
				gameFinished = player1outOfMoves = player2outOfMoves = true;
			} else if (playerCanMove) {
				hasValidMove(bord, viewRef.getPlayer1().getColor());
				if (valid_moves.size() > 0) {
					player1outOfMoves = false;
					player2outOfMoves = false;
					if (tileIsClicked) {
						int move = (BOARD_SIZE * tileX) + tileY;
						playerCanMove = false;
						tileIsClicked = false;
						GameController.receivedMove(viewRef.getPlayer1().getName(), Integer.toString(move));
					}
				} else {
					System.out.println("Player1: Out of moves");
					player1outOfMoves = true;
					playerCanMove = false;
				}
			} else {
				hasValidMove(bord, viewRef.getPlayer2().getColor());
				if (valid_moves.size() > 0) {
					try {
						Thread.sleep(250);
					} catch (InterruptedException e) {
						e.printStackTrace();
					};
					player1outOfMoves = false;
					player2outOfMoves = false;
					int move = (BOARD_SIZE * valid_moves.get(0).y) + valid_moves.get(0).x;
					playerCanMove = true;
					GameController.receivedMove(viewRef.getPlayer2().getName(), Integer.toString(move));
				} else {
					System.out.println("Computer: Out of moves");
					player2outOfMoves = true;
					playerCanMove = true;
				}
			}
		}

		if (!popupAlreadyOpen && player1outOfMoves && player2outOfMoves) {
			gameFinished = true;
			String text = "";
			if (getPieces(viewRef.getPlayer1().getColor()) + getPieces(viewRef.getPlayer2().getColor()) == 64) {
				text = "Game is over!";
			} else if (player1outOfMoves) {
				text = "You are out of moves, but";
			} else if (player2outOfMoves) {
				text = "Computer is out of moves!";
			} 
			
			if (getPieces(viewRef.getPlayer1().getColor()) > getPieces(viewRef.getPlayer2().getColor())) {
				Popup.getInstance().newPopup(text + " You win", Popup.Type.WIN);
			} else if (getPieces(viewRef.getPlayer1().getColor()) < getPieces(viewRef.getPlayer2().getColor())) {
				Popup.getInstance().newPopup(text + " You lose", Popup.Type.LOSS);
			} else if (getPieces(viewRef.getPlayer1().getColor()) == getPieces(viewRef.getPlayer2().getColor())) {
				Popup.getInstance().newPopup(text + " Draw", Popup.Type.DRAW);
			}
			popupAlreadyOpen = true;
		}
	}

	public static void tileIsClicked(int x, int y) {
		tileIsClicked = true;
		tileX = x;
		tileY = y;
	}

	public static boolean isPlayersTurn() {
		return playerCanMove;
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
		hasValidMove(bord, piece);
		weight.clear();
		for (Tuple v : valid_moves) {
			System.out.print("[" + v.x + "," + v.y + "]");
			placePiece(bord, piece, v.y, v.x, false);
		}
		Thread t1 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				Triplet position = maxValue(bord, 1, piece, valid_moves, weight);
				int x = position.getX();
				int y = position.getY();
//				System.out.println(x);
//				System.out.println(y);
				printBoard(bord);
				if (isValidMove(bord, piece, y, x)) {
					int pos = (BOARD_SIZE * y) + x;
					ClientSocket.getInstance(true).sendMove(pos);
					return;
				} else {
					System.out.println("oei oei hij doet het niet!");
				}
				
			}
		});
		t1.start();
	}

	public void receivedMove(char piece, int pos) {
		int x = pos % BOARD_SIZE;
		int y = pos / BOARD_SIZE;
		if (isValidMove(bord, piece, y, x)) {
			bord = placePiece(bord, piece, y, x, true);
			return;
		} else {
			System.out.println("oei hij doet het niet!");
		}
	}
	
	
	public <T> ArrayList<T> cloneList(ArrayList<T> lijst){
		ArrayList<T> new_list = new ArrayList<>();
		for(T l : lijst) {
			new_list.add(l);
		}
		return new_list;
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
		Board bord5 = new Board(8);
		bord5.setBord(bord.getCloneBoard());
		if (bord5.bord[x][y] != EMPTY) {
			return false;
		}
		for (int offset = 0; offset < offsets.length; offset++) {
			Tuple check = new Tuple(x + offsets[offset].x, y + offsets[offset].y);
			while (0 <= check.x && check.x < BOARD_SIZE && 0 <= check.y && check.y < BOARD_SIZE
					&& bord5.bord[check.x][check.y] == inverse(piece)) {
				check.x += offsets[offset].x;
				check.y += offsets[offset].y;
				try {
					Character steen1 = bord5.bord[check.x][check.y];
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
	public Board placePiece(Board bord, char piece, int x, int y, boolean place) {
		Board bord3 = new Board(8);
		bord3.setBord(bord.getCloneBoard());
		int aantal = 0;
		if(place) { bord3.bord[x][y] = piece; }
		else { } 
		
		for(Tuple offset: offsets) {
			Tuple check = new Tuple(x+offset.x, y+offset.y);
			while(0 <= check.x && check.x < BOARD_SIZE && 0 <= check.y && check.y < BOARD_SIZE) {
				if(bord3.bord[check.x][check.y] == EMPTY) { break; }
				if(bord3.bord[check.x][check.y] == piece) {
					if(place) {
						flip(bord3, piece, x , y, offset, place);
					}
					else {
						aantal += flip(bord3, piece, x , y, offset, place);
					}
					break;
				}
				check.x += offset.x;
				check.y += offset.y;
			}
		}
		if(!place) {
			if     (x == 0 && y == 0) { aantal += 99;} else if(x == 1 && y == 0) { aantal -= 8;}
			else if(x == 2 && y == 0) { aantal += 8; } else if(x == 3 && y == 0) { aantal += 6; }
			else if(x == 4 && y == 0) { aantal += 6; } else if(x == 5 && y == 0) { aantal += 8; }
			else if(x == 6 && y == 0) { aantal -= 8; } else if(x == 7 && y == 0) { aantal += 99;}
			
			else if(x == 0 && y == 1) { aantal -= 8; } else if(x == 1 && y == 1) { aantal -= 24;}
			else if(x == 2 && y == 1) { aantal -= 4; } else if(x == 3 && y == 1) { aantal -= 3; }
			else if(x == 4 && y == 1) { aantal -= 3; } else if(x == 5 && y == 1) { aantal -= 4; }
			else if(x == 6 && y == 1) { aantal -= 24;} else if(x == 7 && y == 1) { aantal -= 8; }
			
			else if(x == 0 && y == 2) { aantal += 8; } else if(x == 1 && y == 2) { aantal -= 4; }
			else if(x == 2 && y == 2) { aantal += 7; } else if(x == 3 && y == 2) { aantal += 4; }
			else if(x == 4 && y == 2) { aantal += 4; } else if(x == 5 && y == 2) { aantal += 7; }
			else if(x == 6 && y == 2) { aantal -= 4; } else if(x == 7 && y == 2) { aantal += 8; }
			
			else if(x == 0 && y == 3) { aantal += 6; } else if(x == 1 && y == 3) { aantal -= 3; }
			else if(x == 2 && y == 3) { aantal += 4; } else if(x == 3 && y == 3) { aantal += 0; }
			else if(x == 4 && y == 3) { aantal += 0; } else if(x == 5 && y == 3) { aantal += 4; }
			else if(x == 6 && y == 3) { aantal -= 3; } else if(x == 7 && y == 3) { aantal += 6; }
			
			else if(x == 0 && y == 4) { aantal += 6; } else if(x == 1 && y == 4) { aantal -= 3; }
			else if(x == 2 && y == 4) { aantal += 4; } else if(x == 3 && y == 4) { aantal += 0; }
			else if(x == 4 && y == 4) { aantal += 0; } else if(x == 5 && y == 4) { aantal += 4; }
			else if(x == 6 && y == 4) { aantal -= 3; } else if(x == 7 && y == 4) { aantal += 6; }
			
			else if(x == 0 && y == 5) { aantal += 8; } else if(x == 1 && y == 5) { aantal -= 4; }
			else if(x == 2 && y == 5) { aantal += 7; } else if(x == 3 && y == 5) { aantal += 4; }
			else if(x == 4 && y == 5) { aantal += 4; } else if(x == 5 && y == 5) { aantal += 7; }
			else if(x == 6 && y == 5) { aantal -= 4; } else if(x == 7 && y == 5) { aantal += 8; }
			
			else if(x == 0 && y == 6) { aantal -= 8; } else if(x == 1 && y == 6) { aantal -= 24;}
			else if(x == 2 && y == 6) { aantal -= 4; } else if(x == 3 && y == 6) { aantal -= 3; }
			else if(x == 4 && y == 6) { aantal -= 3; } else if(x == 5 && y == 6) { aantal -= 4; }
			else if(x == 6 && y == 6) { aantal -= 24;} else if(x == 7 && y == 6) { aantal -= 8; }
			
			else if(x == 0 && y == 7) { aantal += 99;} else if(x == 1 && y == 7) { aantal -= 8;}
			else if(x == 2 && y == 7) { aantal += 8; } else if(x == 3 && y == 7) { aantal += 6; }
			else if(x == 4 && y == 7) { aantal += 6; } else if(x == 5 && y == 7) { aantal += 8; }
			else if(x == 6 && y == 7) { aantal -= 8; } else if(x == 7 && y == 7) { aantal += 99;}
			else { }
			
			weight.add(aantal);
		}
		return bord3;
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
		Board bord4 = new Board(8);
		bord4.setBord(bord.getCloneBoard());
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
	public boolean hasValidMove(Board bord6, char piece) {
		//System.out.println("do: hasValidMove!");
		valid_moves.clear();
		for (int y = 0; y < BOARD_SIZE; y++) {
			for (int x = 0; x < BOARD_SIZE; x++) {
				if (isValidMove(bord6, piece, y, x)) {
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
	
	
	public ArrayList<Tuple> checkIfHasValidMove(Board bord, char piece) {
		Board bord6 = new Board(8);
		bord6.setBord(bord.getCloneBoard());
		ArrayList<Tuple> vm = new ArrayList<>();
		for (int y = 0; y < BOARD_SIZE; y++) {
			for (int x = 0; x < BOARD_SIZE; x++) {
				if (isValidMove(bord6, piece, y, x)) {
					vm.add(new Tuple(x, y));
				}
			}
		}
		return vm;
	}
	
	public Triplet maxValue(Board bord, int depth, char piece, ArrayList<Tuple> vm2, ArrayList<Integer> w2) {
		Board bord7 = new Board(8);
		bord7.setBord(bord.getCloneBoard());
		if(checkIfHasValidMove(bord7, piece).size() < 1 && checkIfHasValidMove(bord7, inverse(piece)).size() < 1 || depth > maxDepth) {
			return new Triplet(0, 0, 0);
		}
		int max = -infinity;
		int j = 0;
		ArrayList<Tuple> vm3 = cloneList(vm2);
		ArrayList<Integer> w3 = cloneList(w2);
		Triplet t;
		for(int i = 0; i < vm3.size(); i++) {
			Board bord2 = new Board(8);
			bord2.setBord(bord7.getCloneBoard());
			bord7 = placePiece(bord2, piece, vm3.get(i).y, vm3.get(i).x, false);
			Triplet t3 = minValue(bord7, depth+1, inverse(piece), vm3, w3);
			if((w3.get(i)+t3.getWeight()) > max) { 
				max = w3.get(i)+t3.getWeight();
				j = i;
			}
		}
		t = new Triplet(vm3.get(j).x, vm3.get(j).y, max);
		return t;
	}
	
	
	
	public Triplet minValue(Board bord, int depth, char piece, ArrayList<Tuple> vm4, ArrayList<Integer> w4) {
		Board bord8 = new Board(8);
		bord8.setBord(bord.getCloneBoard());
		if(checkIfHasValidMove(bord8, piece).size() < 1 && checkIfHasValidMove(bord8, inverse(piece)).size() < 1 || depth > maxDepth) {
			return new Triplet(0, 0, 0);
		}
		int min = infinity;
		int j = 0;
		Triplet t;
		ArrayList<Tuple> vm5 = cloneList(vm4);
		ArrayList<Integer> w5 = cloneList(w4);
		for(int i = 0; i < vm5.size(); i++) {
			Board bord3 = new Board(8);
			bord3.setBord(bord8.getCloneBoard());
			bord3 = placePiece(bord8, piece, vm5.get(i).y, vm5.get(i).x, false);
			Triplet t2 = maxValue(bord3, depth+1, inverse(piece), vm5, w5);
			if((t2.getWeight()+w5.get(i)) < min) { 
				min = w5.get(i)+t2.getWeight();
				j = i;
			}
		}
		t = new Triplet(vm5.get(j).x, vm5.get(j).y, min);
		return t;
	}
	

	@Override
	public boolean containsValidMove(int x, int y) {
		hasValidMove(bord, BLACK);
		for (int i = 0; i < valid_moves.size(); i++) {
			if (valid_moves.get(i).x == x && valid_moves.get(i).y == y) {
				return true;
			}
		}
		return false;
	}

	private int getPieces(char piece) {
		int amount = 0;
		for (int y = 0; y < BOARD_SIZE; y++) {
			for (int x = 0; x < BOARD_SIZE; x++) {
				if (bord.getBord()[x][y] == piece) {
					amount++;
				}
			}
		}
		return amount;
	}
}
