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

public abstract class GameView extends SuperView {

	private static Player player1 = null;
	private static Player player2 = null;
	
	private static Polygon playersTurn = null;
	private static boolean matchInit = false;

	public GameView() {
		super();

		player1 = new Player();
		player1.setMinSize(200, 300);
		player1.setTranslateX(100);
		player1.setTranslateY(200);

		player2 = new Player();
		player2.setMinSize(200, 300);
		player2.setTranslateX(100);
		player2.setTranslateY(400);
		
		playersTurn = new Polygon();
		playersTurn.getPoints().addAll(new Double[]{
            0.0, 0.0,
            0.0, 30.0,
            30.0, 15.0 });

		super.addChild(2, player1);
		super.addChild(2, player2);
		super.addChild(2, playersTurn);

		Button btn_forfeit = new Button("Forfeit");
		btn_forfeit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				System.out.println("Player has forfeit!");
				ClientSocket.getInstance(true).forfeit();
			}
		});
		btn_forfeit.setTranslateX(100);
		btn_forfeit.setTranslateY(600);
		super.addChild(2, btn_forfeit);
	}

	@Override
	protected void update() {

	}

	public static void updateSuperView(HashMap<String, String> map) {
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
						ClientSocket.getInstance(false).forfeit();
						Main.switchScene(Main.SceneType.START);
					}

					@Override
					public void clickedNo() {

					}
				});
			}
		});
		
		matchInit = true;
	}

	protected void updatePlayersScore(String name, int score) {
		if (player1.getName().equals(name)) {
			player1.addScore(score);
		} else if (player2.getName().equals(name)) {
			player2.addScore(score);
		}
	}
	
	protected void updatePlayersTurn(String name) {
		if (player1.getName().equals(name)) {
			playersTurn.setTranslateX(25);
			playersTurn.setTranslateY(player1.getTranslateY());
		} else if (player2.getName().equals(name)) {
			playersTurn.setTranslateX(25);
			playersTurn.setTranslateY(player2.getTranslateY());
		}
	}
	
	public static Player getPlayer1() {
		return player1;
	}
	
	public static Player getPlayer2() {
		return player2;
	}
	
	public static boolean isCreated() {
		return matchInit;
	}
	
	public abstract void updateBoardView(SuperGame game);
}
