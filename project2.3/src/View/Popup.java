package View;

import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
* Popup is a Singleton class
* So a popup can occur throughout the project the whole application
*
* @author  Daniël Geerts
* @since   2019-03-28
*/
public final class Popup {
	
	public enum Type { OK, YESNO, DEBUG };
	
	private int width = 400;
	private int height = 200;	
	private String img_errPath = "File:img/error_icon.gif";
	private String img_debugPath = "File:img/debug_icon.gif";
	private String img_helpPath = "File:img/help_icon.gif";
	private List<Stage> popups = new ArrayList<Stage>();
	
	private static Popup instance = null;
		
	public static Popup getInstance() {
		if (instance == null) {
			instance = new Popup();
		}
		return instance;
	}
	
	private Popup() {}
	
	public void newPopup(String text, Type type) {
		Stage newStage = new Stage();
		GridPane pane = new GridPane();
		pane.setAlignment(Pos.CENTER);
		pane.setMinSize(width, height);
		pane.setVgap(10);
		pane.setHgap(10);
		
		Label MESSAGE = new Label(text);
		pane.add(MESSAGE, 1, 0);
		
		switch (type) {
		case OK:
			ImageView error = getImageView(img_errPath);
			pane.add(error, 0, 0);
			Button btn_ok = new Button("OK");
			pane.add(btn_ok, 1, 1);
			btn_ok.setOnAction(new EventHandler<ActionEvent>() {
				@Override
			    public void handle(ActionEvent e) {
					newStage.close();
					popups.remove(newStage);
			    }
			});
			break;
		case YESNO:
			Button btn_yes = new Button("YES");
			Button btn_no = new Button("NO");
			pane.add(btn_yes, 1, 1);
			pane.add(btn_no, 1, 1);

			btn_yes.setOnAction(new EventHandler<ActionEvent>() {
				@Override
			    public void handle(ActionEvent e) {
					newStage.close();
					popups.remove(newStage);
			    }
			});
			
			btn_no.setOnAction(new EventHandler<ActionEvent>() {
				@Override
			    public void handle(ActionEvent e) {
					newStage.close();
					popups.remove(newStage);
			    }
			});
			break;
		case DEBUG:
			ImageView debug = getImageView(img_debugPath);
			pane.add(debug, 0, 0);
			Button btn_debug = new Button("OK");
			pane.add(btn_debug, 1, 1);
			btn_debug.setOnAction(new EventHandler<ActionEvent>() {
				@Override
			    public void handle(ActionEvent e) {
					newStage.close();
					popups.remove(newStage);
			    }
			});
			break;
		}

		Scene stageScene = new Scene(pane, width, height);
		newStage.setScene(stageScene);
		newStage.show();
		popups.add(newStage);
	}
	
	private ImageView getImageView(String path) {
		Image image = new Image(path);
	    ImageView pic = new ImageView();
	    pic.setFitWidth(50);
	    pic.setFitHeight(50);
	    pic.setImage(image);
	    return pic;
	}
}
