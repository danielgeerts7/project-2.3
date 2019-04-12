package View;

import java.util.ArrayList;
import java.util.List;

import Model.Board;
import Model.Config;
import Model.SuperGame;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class TicTacToeView extends GameView {
	
	private boolean playable = true;
	private boolean turnX = true;
	private Tile[][] board = new Tile[3][3];
	private List<Combo> combos = new ArrayList<>();
	
	private Pane pane = new Pane();
	
	public TicTacToeView() {
		super();
		
		pane.setPrefSize(450, 450);
//		pane.setAlignment(Pos.CENTER_RIGHT);
		
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				Tile tile = new Tile();
				tile.setTranslateX(j * 150);
				tile.setTranslateY(i * 150);
				
				pane.getChildren().add(tile);
				
				board[j][i] = tile;
			}
		}
		
		//Horizontaal 3 op een rij
				for (int y = 0; y < 3; y++) {
					combos.add(new Combo(board[0][y], board[1][y], board[2][y]));
				}
				
				//Verticaal 3 op een rij
				for (int x = 0; x < 3; x++) {
					combos.add(new Combo(board[x][0], board[x][1], board[x][2]));
				}
				
				//Diagonaal 3 op een rijd
				combos.add(new Combo(board[0][0], board[1][1], board[2][2]));
				combos.add(new Combo(board[2][0], board[1][1], board[0][2]));


		
//		constructGamePane();
		
		super.addChild(1, pane);
	}
	
	private void checkState() {
		for (Combo combo : combos) {
			if (combo.isComplete()) {
				System.out.println("We have a winner!");
				playWinAnimation(combo);
				playable = false;
				break;
			}
		}
	}
	
	private void playWinAnimation(Combo combo) {
		Line line = new Line();
		line.setStartX(combo.tiles[0].getCenterX());
		line.setStartY(combo.tiles[0].getCenterY());
		line.setEndX(combo.tiles[0].getCenterX());
		line.setEndY(combo.tiles[0].getCenterY());
		
		pane.getChildren().add(line);
		
		Timeline timeline = new Timeline();
		timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1),
				new KeyValue(line.endXProperty(), combo.tiles[2].getCenterX()),
				new KeyValue(line.endYProperty(), combo.tiles[2].getCenterY())));
		timeline.play();
	}
	
	private class Tile extends StackPane {
		private Text text = new Text();
		
		public Tile() {
			Rectangle border = new Rectangle(150, 150);
			border.setFill(null);
			border.setStroke(Color.BLACK);
			
			text.setFont(Font.font(72));
			
			setAlignment(Pos.CENTER);
			getChildren().addAll(border, text);
			
			setOnMouseClicked(event -> {
				if (!playable)
					return;
				
				
				if (event.getButton() == MouseButton.PRIMARY) {
					if (!turnX) {
						drawO();
						turnX = true;
						checkState();
						return;
					}
					else {
						drawX();
						turnX = false;
						checkState();
						return;
						}
					}
			});
		}
		
		public double getCenterX() {
			return getTranslateX() + 75;
		}
		
		public double getCenterY() {
			return getTranslateY() + 75;
		}
		
		public String getValue() {
			return text.getText();		
		}
		
		private void drawX() {
			text.setText("X");
		}
		
		private void drawO() {
			text.setText("O");
		}
	}
	
	private class Combo {
		private Tile[] tiles;
		
		public Combo(Tile...tiles) {
			this.tiles = tiles;
		}
		
		public boolean isComplete() {
			if (tiles[0].getValue().isEmpty())
				return false;
			
			return tiles[0].getValue().equals(tiles[1].getValue())
					&& tiles[0].getValue().equals(tiles[2].getValue());		
		}
	}
	
	@Override
	protected void update() {
		
	}
	
	@Override
	public void updateBoardView(SuperGame game) {
//		pane.getChildren().clear();
//		if (game != null) {
//			Board b = game.getBord();
//			for (int i = 0; i < b.bord.length; i++) {
//				for (int j = 0; j < b.bord[i].length; j++) {
//					pane.add(new ImageView(new Image("File:img/white_tile.png", 70, 70, false, false)), i, j);
//				}
//			}
//		}
	}
	
//	private void constructGamePane() {
//		for (int i = 0; i < 3; i++) {
//			for (int j = 0; j < 3; j++) {
//				pane.add(new ImageView(new Image("File:img/white_tile.png", 70, 70, false, false)), i, j);
//			}
//		}
//	}

}
