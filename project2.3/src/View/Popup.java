package View;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.AnimationTimer;
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
	
	public enum Type { ERROR, OK, DEBUG };
	
	private int width = 400;
	private int height = 200;	
	private String img_errPath = "File:img/error_icon.gif";
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
		
		switch (type) {
		case ERROR:
			ImageView ERROR = getImageView(img_errPath);
			Label MESSAGE = new Label(text);
			Button OK = new Button("OK");
			pane.add(ERROR, 0, 0);
			pane.add(MESSAGE, 1, 0);
			pane.add(OK, 1, 1);
			
			OK.setOnAction(new EventHandler<ActionEvent>() {
				@Override
			    public void handle(ActionEvent e) {
					newStage.close();
					popups.remove(newStage);
			    }
			});			
			break;
		case OK:
			Label MESSAGE1 = new Label("OK: " + text);
			Button OK1 = new Button("OK");
			pane.add(MESSAGE1, 1, 0);
			pane.add(OK1, 1, 1);
			
			OK1.setOnAction(new EventHandler<ActionEvent>() {
				@Override
			    public void handle(ActionEvent e) {
					newStage.close();
					popups.remove(newStage);
			    }
			});
		case DEBUG:
			Label MESSAGE2 = new Label("DEBUG: " + text);
			Button OK2 = new Button("OK");
			pane.add(MESSAGE2, 1, 0);
			pane.add(OK2, 1, 1);
			
			OK2.setOnAction(new EventHandler<ActionEvent>() {
				@Override
			    public void handle(ActionEvent e) {
					newStage.close();
					popups.remove(newStage);
			    }
			});
			break;
		}
		
		final AnimationTimer animator = new AnimationTimer() {
			@Override
			public void handle(long now) {
				if (popups.size() < 0) {
					popups.get(0).requestFocus();
					this.stop();
				}
			}
		};
		animator.start();

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
