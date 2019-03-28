package View;

import Main.Config;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class StartView extends SuperView {
	
	private GridPane mainpane = null;
	private TextField input_login = null;
	private Button btn_login = null;

	public StartView(Stage primaryStage) {
		super();
		
		mainpane = new GridPane();
		mainpane.setAlignment(Pos.CENTER);
		mainpane.setMinSize(Config.WIDTH, Config.HEIGHT);
		mainpane.setPadding(new Insets(10, 10, 10, 10));
		
		addButtons();
		
		super.getChildren().add(mainpane);
	}

	@Override
	protected void update() {
		// TODO Auto-generated method stub
	}
	
	private void addButtons() {
		input_login = new TextField();
		btn_login = new Button("Login");
		btn_login.setOnAction(new EventHandler<ActionEvent>() {
			@Override
		    public void handle(ActionEvent e) {
		        client.LoginOnServer(input_login.getText());
		    }
		});
		
		mainpane.add(input_login, 0, 0);
		mainpane.add(btn_login, 1, 0);
	}
}
