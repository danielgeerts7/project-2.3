package View;

import java.util.HashMap;

import Controller.ClientSocket;
import Main.Main;
import Model.Client;
import Model.Player;
import Model.ReversiGame;
import Model.SuperGame;
import View.Popup.PopupYesNo;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.shape.Polygon;

/**
 * GameView views the current game that is playing
 *
 * @author Daniel Geerts
 * @since 2019-04-06
 */
public abstract class GameView extends SuperView {

	private Player player1 = null;
	private Player player2 = null;

	private Polygon playersTurn = null;
	private static boolean matchInit = false;
	
	protected boolean playRemote = false;
	protected boolean playerCanMove = true;

	public GameView(boolean playRemote) {
		super();
		this.playRemote = playRemote;

		player1 = new Player();
		player1.setMinSize(200, 300);
		player1.setTranslateX(100);
		player1.setTranslateY(200);

		player2 = new Player();
		player2.setMinSize(200, 300);
		player2.setTranslateX(100);
		player2.setTranslateY(400);

		playersTurn = new Polygon();
		playersTurn.getPoints().addAll(new Double[] { 0.0, 0.0, 0.0, 30.0, 30.0, 15.0 });

		super.addChild(2, player1);
		super.addChild(2, player2);
		super.addChild(2, playersTurn);

		Button btn_forfeit = new Button("Forfeit");
		btn_forfeit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				System.out.println("Player has forfeit!");
				if (playRemote) {
					ClientSocket.getInstance(true).forfeit();
				} else {
					Popup.getInstance().newPopup("You gave up!", Popup.Type.OK);
					Main.switchScene(Main.SceneType.START, false);
				}
			}
		});
		btn_forfeit.setTranslateX(100);
		btn_forfeit.setTranslateY(600);
		super.addChild(5, btn_forfeit);
		
		if (!playRemote) {
			showRemoteLabels(false);
			showOfflineButtons(true);
			
			menu.getBackBtn().setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					Main.switchScene(Main.SceneType.START, false);
				}
			});
		}
	}

	/**
	 * Update all information that is stored in SuperView
	 * 
	 * @param map contains the message received by the server at the begin of a
	 *            match
	 */
	public void updateSuperView(HashMap<String, String> map) {
		if (map != null) {
			String player = map.get("PLAYERTOMOVE");
			String game = map.get("GAMETYPE");
			String opponent = map.get("OPPONENT");

			setSubscriptionLabel(game);

			player1.setName(Client.getUsername());
			player2.setName(opponent);
			if (Client.getUsername().equals(player)) {
				playersTurn.setTranslateX(25);
				playersTurn.setTranslateY(player1.getTranslateY());
				player1.setColor(ReversiGame.BLACK);
				player2.setColor(ReversiGame.WHITE);
			} else {
				playersTurn.setTranslateX(25);
				playersTurn.setTranslateY(player2.getTranslateY());
				player1.setColor(ReversiGame.WHITE);
				player2.setColor(ReversiGame.BLACK);
			}
		}

		menu.getBackBtn().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				Popup.getInstance().newPopup("Wanna giveup and go back to menu?", Popup.Type.YESNO, new PopupYesNo() {
					@Override
					public void clickedYes() {
						if (ClientSocket.getInstance(false) != null) {
							ClientSocket.getInstance(false).forfeit();
						}
						Main.switchScene(Main.SceneType.START, false);
					}

					@Override
					public void clickedNo() {

					}
				}, "");
			}
		});

		matchInit = true;
	}

	/**
	 * Updates the triangle that points to the current player thats needs to make a
	 * move
	 * 
	 * @param name of Player that needs to send a move
	 */
	public void updatePlayersTurn(String name) {
		if (!player1.getName().equals(name)) {
			playersTurn.setTranslateX(25);
			playersTurn.setTranslateY(player1.getTranslateY());
		} else if (!player2.getName().equals(name)){
			playersTurn.setTranslateX(25);
			playersTurn.setTranslateY(player2.getTranslateY());
		}
	}

	public Player getPlayer1() {
		return player1;
	}

	public Player getPlayer2() {
		return player2;
	}

	public static boolean isCreated() {
		return matchInit;
	}

	public abstract void updateBoardView(SuperGame game);
}
