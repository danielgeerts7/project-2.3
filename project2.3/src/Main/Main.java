package Main;

import Controller.SocketController;
import Controller.GameController;
import Model.Config;
import View.StartView;
import View.SuperView;
import View.GameView;
import View.ReversiView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	
	private static Stage primaryReference = null;

	public enum SceneType { START, GAME };
	public enum GameType {REVERSI, TICTACTOE};
  
	@Override
	public void start(Stage primaryStage) throws Exception {		
		StartView menu = new StartView(primaryStage);
		GameView game = new GameView(primaryStage);
		ReversiView reversi = new ReversiView(primaryStage);
		Scene scene = new Scene(menu);
		
		primaryStage.setTitle(Config.APP_NAME); // Set the stage title
		primaryStage.setScene(scene); // Place the scene in the stage
		primaryStage.setResizable(false);
        primaryStage.centerOnScreen();
		primaryStage.show(); // Display the stage
		
		primaryReference = primaryStage;
		
		SocketController.getInstance(true);
	}
	
	public static void switchScene(SceneType scenetype) {
		switch (scenetype) {
		case START:
			System.out.println("----> start view");
			StartView start = new StartView(primaryReference);
			primaryReference.getScene().setRoot(start);
			break;
		case GAME:
			System.out.println("----> game view");
			GameView game = new GameView(primaryReference);
			primaryReference.getScene().setRoot(game);
			break;
		}
	}
	
	public static void switchGame(GameType gametype) {
		switch(gametype) {
		case REVERSI:
			System.out.println("----> reversi game");
			ReversiView reversi = new ReversiView(primaryReference);
			primaryReference.getScene().setRoot(reversi);
			break;
		}
	}

	public static void main(String[] args) throws Exception {
		launch(args);
	}
	
	public static void QuitApp() {
		System.out.println("Quiting application. Bye...");
		Platform.exit();
		System.exit(0);
	}
}
