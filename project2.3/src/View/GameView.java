package View;

import java.util.HashMap;

import Controller.ClientSocket;
import Main.Main;
import Model.Client;
import Model.Player;
import View.Popup.PopupYesNo;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.shape.Polygon;

public class GameView extends SuperView {

	private static Player player1 = null;
	private static Player player2 = null;
	
	private static Polygon playersTurn = null;

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
				System.out.println("You lose!");
				ClientSocket.getInstance(true).forfeit();
			}
		});
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

			if (Client.getUsername().equals(player)) {
				player1.setName(player);
				player2.setName(opponent);
				playersTurn.setTranslateX(25);
				playersTurn.setTranslateY(player1.getTranslateY());
			} else if (Client.getUsername().equals(opponent)) {
				player1.setName(opponent);
				player2.setName(player);
				playersTurn.setTranslateX(25);
				playersTurn.setTranslateY(player2.getTranslateY());
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
}
