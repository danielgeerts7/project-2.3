package View;

import Model.Tile;
import Model.Board;
import Model.Config;
import Model.ReversiGame;
import Model.SuperGame;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.util.Duration;

public class TicTacToeView extends GameView {
	
	private Pane pane = new Pane();
	private Tile[][] board = new Tile[3][3];

	
	public TicTacToeView(boolean playRemote) {
		super(playRemote);
		
		pane.setPrefSize(450, 450);
		pane.setPadding(value);
		
		
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				Tile tile = new Tile();
				tile.setTranslateX(j * 150);
				tile.setTranslateY(i * 150);
				
				pane.getChildren().add(tile);
				
				board[j][i] = tile;
				
			}
		}
		
		super.addChild(1, pane);
		
		
	}
	
	@Override
	public void updateBoardView(SuperGame game) {
		
	}
	
//	public void playWinAnimation(Combo combo) {
//		Line line = new Line();
//		line.setStartX(combo.tiles[0].getCenterX());
//		line.setStartY(combo.tiles[0].getCenterY());
//		line.setEndX(combo.tiles[0].getCenterX());
//		line.setEndY(combo.tiles[0].getCenterY());
//		
//		pane.getChildren().add(line);
//		
//		Timeline timeline = new Timeline();
//		timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1),
//				new KeyValue(line.endXProperty(), combo.tiles[2].getCenterX()),
//				new KeyValue(line.endYProperty(), combo.tiles[2].getCenterY())));
//		timeline.play();
//	}
	

}
