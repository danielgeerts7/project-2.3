package View;

import java.util.HashMap;

import Controller.ClientSocket;
import Main.Main;
import Model.Config;
import View.Popup.PopupYesNo;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class GameView extends SuperView {
	//TODO: ReversiView maken en TicTacToeView

	private GridPane player1 = null;
	private GridPane player2 = null;

	public GameView(Stage stage) {
		super();

		player1 = new GridPane();
		player1.setMinSize(200, 300);
		player1.setTranslateX(100);
		player1.setTranslateY(200);
		
		player2 = new GridPane();
		player2.setMinSize(200, 300);
		player2.setTranslateX(100);
		player2.setTranslateY(400);
		
		super.addChild(2, player1);
		super.addChild(2, player2);
		
		constructScorePane();
	}

	@Override
	protected void update() {

	}

	public void constructScorePane() {
		Button forfeitP1 = new Button("Forfeit");		
		forfeitP1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				System.out.println("You lose!");
			}
		});
		Label name = new Label("Player 1");
		player1.add(name, 0, 0);
		player1.add(new Label("Score: "), 0, 1);
		player1.add(forfeitP1, 0, 2);
		
		Label opp_name = new Label("Player 2");
		player2.add(opp_name, 0, 1);
		player2.add(new Label("Score: "), 0, 2);
	}
	
	public static void updateSuperView(HashMap<String, String> map) {
		
		if (map != null) {
			String player = map.get("PLAYERTOMOVE");
			String game = map.get("GAMETYPE");
			String opponent = map.get("OPPONENT");
			
			setSubscriptionLabel(game);

			Label opp = new Label("You are playing against: " + opponent);
			//player1.add(opp, 10, 2);
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
}
