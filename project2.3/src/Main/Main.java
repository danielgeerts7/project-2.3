package Main;

import Controller.GameController;
import Model.Config;
import View.StartView;
import View.TicTacToeView;
import View.GameView;
import View.ReversiView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	
	private static Stage primaryReference = null;

	public enum SceneType { START, REVERSI, TICTACTOE };
  
	@Override
	public void start(Stage primaryStage) throws Exception {		
		StartView menu = new StartView();
		Scene scene = new Scene(menu);
		
		primaryStage.setTitle(Config.APP_NAME); // Set the stage title
		primaryStage.setScene(scene); // Place the scene in the stage
		primaryStage.setResizable(false);
        primaryStage.centerOnScreen();
		primaryStage.show(); // Display the stage
		
		primaryReference = primaryStage;
	}
	
	/**
	 * Handles every command that the server has send
	 * @param command is the message received from the server
	 */
	public static GameView switchScene(SceneType scenetype, boolean playRemote) {
		GameView game = null;
		switch (scenetype) {
		case START:
			System.out.println("----> start view");
			StartView start = new StartView();
			primaryReference.getScene().setRoot(start);
			break;
		case REVERSI:
			System.out.println("----> reversi view");
			ReversiView reversi = new ReversiView(playRemote);
			new GameController(reversi, playRemote);
			primaryReference.getScene().setRoot(reversi);
			game = reversi;
			break;
		case TICTACTOE:
			System.out.println("----> tictactoeview");
			TicTacToeView tictactoe = new TicTacToeView(playRemote);
			new GameController(tictactoe, playRemote);
			primaryReference.getScene().setRoot(tictactoe);
			break;
		}
		return game;
	}

	public static void main(String[] args) throws Exception {
		launch(args);
	}
}
