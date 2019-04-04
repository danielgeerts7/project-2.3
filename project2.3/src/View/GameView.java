package View;

import Model.Config;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class GameView extends SuperView  {
	//TODO: ReversiView maken en TicTacToeView
	
	private GridPane player1 = null;
	private GridPane player2 = null;
	
	public GameView(Stage stage) {
		super();
		
		player1 = new GridPane();
		player1.setAlignment(Pos.CENTER);
		player1.setMinSize(300, Config.HEIGHT);
		player1.setHgap(5);
		player1.setVgap(5);
		
		player2 = new GridPane();
		player2.setAlignment(Pos.CENTER_RIGHT);
		player2.setMinSize(1200, Config.HEIGHT);
		player2.setHgap(5);
		player2.setVgap(5);
		
		constructScorePane();
		
		super.getChildren().add(player1);
		super.getChildren().add(player2);
	}
	
	@Override
	protected void update() {
	
	}
	
	public void constructScorePane() {
		GridPane textPaneP1 = new GridPane();
		GridPane textPaneP2 = new GridPane();
		player1.add(textPaneP1, 0, 1);
		player2.add(textPaneP2, 0, 1);
		
		Button forfeitP1 = new Button("Forfeit");
		Button forfeitP2 = new Button("Forfeit");
		
		forfeitP1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				System.out.println("You lose!");
			}
		});
		
		forfeitP2.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				System.out.println("You lose!");
			}
		});
		
		textPaneP1.setStyle("-fx-border-color: red");
		textPaneP1.setPadding(new Insets(20.5, 21.5, 22.5, 23.5));
		textPaneP1.setHgap(25);
		textPaneP1.setVgap(35);
		
		textPaneP1.add(new Label("Player 1"), 0, 0);
		textPaneP1.add(new Label("Score: "), 0, 3);
		textPaneP1.add(forfeitP1, 0, 5);
		
		textPaneP2.setStyle("-fx-border-color: red");
		textPaneP2.setPadding(new Insets(20.5, 21.5, 22.5, 23.5));
		textPaneP2.setHgap(25);
		textPaneP2.setVgap(35);
		
		textPaneP2.add(new Label("Player 2"), 0, 0);
		textPaneP2.add(new Label("Score: "), 0, 3);
		textPaneP2.add(forfeitP2, 0, 5);
	}
}
