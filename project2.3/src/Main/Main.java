package Main;

import Controller.ClientSocket;
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
	
	private static String gametype = "";
	
	private static GameController controller = null;
  
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
		
		ClientSocket.getInstance(true);
	}
	
	public static void switchScene(SceneType scenetype) {
		switch (scenetype) {
		case START:
			System.out.println("----> start view");
			StartView start = new StartView();
			primaryReference.getScene().setRoot(start);
			break;
		case REVERSI:
			System.out.println("----> reversi view");
			ReversiView reversi = new ReversiView();
			gametype = "reversi";
			controller = new GameController(reversi, gametype);
			primaryReference.getScene().setRoot(reversi);
			break;
		case TICTACTOE:
			System.out.println("----> tic-tac-toe view");
			TicTacToeView tictactoe = new TicTacToeView();
			gametype = "tic-tac-toe";
			controller = new GameController(tictactoe, gametype);
			primaryReference.getScene().setRoot(tictactoe);
			break;
		}
		
	}

	public static void main(String[] args) throws Exception {
		launch(args);
	}
}
