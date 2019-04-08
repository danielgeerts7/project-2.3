package View;

import Model.Board;
import Model.Config;
import Model.ReversiGame;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class ReversiView extends GameView {

	private GridPane pane = null;
	private ReversiGame game = null;

	public ReversiView() {
		super();

		pane = new GridPane();
		pane.setAlignment(Pos.CENTER);
		pane.setMinSize(Config.WIDTH, Config.HEIGHT);
		pane.setHgap(5);
		pane.setVgap(5);

		constructGamePane();

		super.addChild(1, pane);

		game = new ReversiGame();
	}

	@Override
	protected void update() {
		if (game != null) {
			Board b = game.getBord();
			for (int i = 0; i < b.bord.length; i++) {
				for (int j = 0; j < b.bord[i].length; j++) {
					if (b.bord[i][j] == ReversiGame.BLACK) {
						pane.add(new ImageView(new Image("File:img/black_stone.png", 70, 70, false, false)), i, j);
					}
					if (b.bord[i][j] == ReversiGame.WHITE) {
						pane.add(new ImageView(new Image("File:img/white_stone.png", 70, 70, false, false)), i, j);
					}
					if (b.bord[i][j] == ReversiGame.EMPTY) {

					}
				}
			}
		}
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
}
