package Main;

import View.StartView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {		
		StartView menu = new StartView(primaryStage);
		Scene scene = new Scene(menu);
		
		primaryStage.setTitle(Config.APP_NAME); // Set the stage title
		primaryStage.setScene(scene); // Place the scene in the stage
		primaryStage.setResizable(false);
        primaryStage.centerOnScreen();
		primaryStage.show(); // Display the stage
	}

	public static void main(String[] args) throws Exception {
		launch(args);
	}
}
