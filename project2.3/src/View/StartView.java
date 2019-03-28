package View;

import Controller.ClientSocketController;
import Main.Config;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class StartView extends SuperView {
	
	private GridPane mainpane = null;
	
	public StartView(Stage primaryStage) {
		super();
		
		mainpane = new GridPane();
		mainpane.setAlignment(Pos.CENTER);
		mainpane.setMinSize(Config.WIDTH, Config.HEIGHT);
		mainpane.setHgap(10);
		mainpane.setVgap(10);
		mainpane.setPadding(new Insets(10, 10, 10, 10));
		
		constructLoginPane();
		
		super.getChildren().add(mainpane);
	}

	@Override
	protected void update() {
		
	}
	
	private void clearPane() {
		mainpane.getChildren().clear();
	}
	
	private void constructLoginPane() {
		clearPane();
		
		TextField input_login = new TextField();
		Button btn_login = new Button("Login");
		
		btn_login.setOnAction(new EventHandler<ActionEvent>() {
			@Override
		    public void handle(ActionEvent e) {
		        if (ClientSocketController.getInstance() != null && ClientSocketController.getInstance().loginOnServer(input_login.getText())) {
		        	constructChooseGamePane();
		        }
		    }
		});
		
		mainpane.add(input_login, 0, 0);
		mainpane.add(btn_login, 1, 0);
	}
	
	private void constructChooseGamePane() {
		clearPane();
		
		String[] games = ClientSocketController.getInstance().getGamelist();
		int counter = 1;
		for (String i : games) {
			Label txt_gameName = new Label(i);
			Button btn_chooseGame = new Button("Choose " + i);
			
			btn_chooseGame.setOnAction(new EventHandler<ActionEvent>() {
				@Override
			    public void handle(ActionEvent e) {
			        if (ClientSocketController.getInstance().selectGame(txt_gameName.getText())) {
			        	constructChooseOpponentPane();
			        }
			    }
			});
			
			mainpane.add(txt_gameName, 0, counter);
			mainpane.add(btn_chooseGame, 1, counter);
			counter++;
		}
		// TODO: still awaiting for my master to be coded
	}
	
	private void constructChooseOpponentPane() {
		clearPane();
		
		// TODO: still awaiting for my master to be coded
	}
}
