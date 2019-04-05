package View;

import java.util.HashMap;

import Controller.ClientSocket;
import Main.Main;
import Model.Config;
import View.Popup.PopupYesNo;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class GameView extends SuperView {

	private static GridPane pane = null;

	public GameView(Stage stage) {
		super();

		pane = new GridPane();
		pane.setAlignment(Pos.CENTER);
		pane.setMinSize(Config.WIDTH, Config.HEIGHT);
		pane.setHgap(5);
		pane.setVgap(5);

		constructGamePane();

		super.addChild(pane);
	}

	@Override
	protected void update() {

	}

	private void constructGamePane() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				pane.add(new ImageView(new Image("File:img/green_tile.png", 70, 70, false, false)), i, j);
			}
		}

//		for (int i = 0; i < 8; i++) {
//			for (char alphabet = 'A'; alphabet < 'H'; alphabet++) {
//				pane.add(new Label(alphabet), i, 9);
//			}
//		}
	}

	public static void updateSuperView(HashMap<String, String> map) {
		
		if (map != null) {
			String player = map.get("PLAYERTOMOVE");
			String game = map.get("GAMETYPE");
			String opponent = map.get("OPPONENT");
			
			setSubscriptionLabel(game);

			Label opp = new Label("You are playing against: " + opponent);
			pane.add(opp, 10, 2);
		}

		btn_back.setOnAction(new EventHandler<ActionEvent>() {
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
