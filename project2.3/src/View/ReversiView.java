package View;

import Model.Board;
import Model.Config;
import Model.ReversiGame;
import Model.SuperGame;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class ReversiView extends GameView {

	private GridPane pane = null;

	public ReversiView(boolean playRemote) {
		super(playRemote);

		pane = new GridPane();
		pane.setAlignment(Pos.CENTER);
		pane.setMinSize(Config.WIDTH, Config.HEIGHT);
		pane.setHgap(5);
		pane.setVgap(5);

		constructGamePane();

		super.addChild(1, pane);
	}

	@Override
	public void updateBoardView(SuperGame game) {
		pane.getChildren().clear();
		if (game != null) {
			Board b = game.getBord();
			int black = 0;
			int white = 0;
			for (int i = 0; i < b.bord.length; i++) {
				for (int j = 0; j < b.bord[i].length; j++) {
					if (!playRemote && ReversiGame.isPlayersTurn() && game.containsValidMove(j, i)) {
						ImageView image = new ImageView(new Image("File:img/place_stone.png", 70, 70, false, false));
						image.setUserData(i + "," + j);
						image.setOnMouseClicked(new EventHandler<MouseEvent>() {
							public void handle(final MouseEvent ME) {
								Object obj = ME.getSource();
								if (obj instanceof ImageView) {
									String[] s = ((ImageView) obj).getUserData().toString().split(",");
									ReversiGame.tileIsClicked(Integer.parseInt(s[0]), Integer.parseInt(s[1]));
								}
							}
						});
						pane.add(image, i, j);
					} else {
						if (b.bord[i][j] == ReversiGame.BLACK) {
							pane.add(new ImageView(new Image("File:img/black_stone.png", 70, 70, false, false)), i, j);
							black++;
						}
						if (b.bord[i][j] == ReversiGame.WHITE) {
							pane.add(new ImageView(new Image("File:img/white_stone.png", 70, 70, false, false)), i, j);
							white++;
						}
						if (b.bord[i][j] == ReversiGame.EMPTY) {
							pane.add(new ImageView(new Image("File:img/green_tile.png", 70, 70, false, false)), i, j);
						}
					}
				}
			}
			if (getPlayer1().getColor() == ReversiGame.BLACK) {
				getPlayer1().setScore(black);
				getPlayer2().setScore(white);
			} else {
				getPlayer1().setScore(white);
				getPlayer2().setScore(black);
			}
		}
	}

	private void constructGamePane() {
		/*for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				pane.add(new ImageView(new Image("File:img/green_tile.png", 70, 70, false, false)), i, j);
			}
		}*/

//		for (int i = 0; i < 8; i++) {
//			for (char alphabet = 'A'; alphabet < 'H'; alphabet++) {
//				pane.add(new Label(alphabet), i, 9);
//			}
//		}
	}
}
