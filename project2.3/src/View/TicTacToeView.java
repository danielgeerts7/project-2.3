package View;

import Model.Board;
import Model.Config;
import Model.SuperGame;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class TicTacToeView extends GameView {
	
	private GridPane pane = null;
	
	public TicTacToeView() {
		super();
		
		pane = new GridPane();
		pane.setAlignment(Pos.CENTER);
		pane.setMinSize(Config.WIDTH, Config.HEIGHT);
		pane.setHgap(0);
		pane.setVgap(0);
		
		super.addChild(1, pane);
	}
	
	@Override
	protected void update() {
		
	}
	
	@Override
	public void updateBoardView(SuperGame game) {
		pane.getChildren().clear();
		if (game != null) {
			Board b = game.getBord();
			for (int i = 0; i < b.bord.length; i++) {
				for (int j = 0; j < b.bord[i].length; j++) {
					pane.add(new ImageView(new Image("File:img/white_tile.png", 70, 70, false, false)), i, j);
				}
			}
		}
	}

}
