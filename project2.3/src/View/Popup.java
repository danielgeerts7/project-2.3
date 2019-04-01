package View;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Popup is a Singleton class So a popup can occur throughout the project the
 * whole application
 *
 * @author Daniël Geerts
 * @since 2019-03-28
 */
public final class Popup {

	public enum Type {
		DEBUG, OK, YESNO, WIN, LOSE, DRAW
	};

	private int width = 425;
	private int height = 175;
	private String img_okPath = "File:img/OK.png";
	private String img_debugPath = "File:img/DEBUG.png";
	private String img_YesNoPath = "File:img/YESNO.png";
	private String img_winPath = "File:img/WIN.png";
	private String img_losePath = "File:img/LOSE.png";
	private String img_drawPath = "File:img/DRAW.png";

	private static Popup instance = null;

	public static Popup getInstance() {
		if (instance == null) {
			instance = new Popup();
		}
		return instance;
	}

	private Popup() {

	}

	public void newPopup(String text, Type type) {
		newPopup(text, type, null);
	}

	public void newPopup(String text, Type type, PopupYesNo yesno) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				final Stage newStage = new Stage();
				newStage.setTitle("Popup: " + type.toString().toLowerCase());
				Pane pane = new Pane();
				pane.setMinSize(width, height);

				Label message = new Label(text);
				message.setAlignment(Pos.CENTER);
				moveItem(message, width*.3, height*0.25);
				pane.getChildren().add(message);
				
				String image = "";

				switch (type) {
				case OK:
					image = img_okPath;
					Button btn_ok = new Button("OK");
					btn_ok.setAlignment(Pos.CENTER);
					moveItem(btn_ok, width*.5, height*.7);
					pane.getChildren().add(btn_ok);
					btn_ok.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent e) {
							newStage.close();
						}
					});
					break;
				case YESNO:
					image = img_YesNoPath;
					Button btn_yes = new Button("YES");
					Button btn_no = new Button("NO");
					pane.getChildren().add(btn_yes);
					pane.getChildren().add(btn_no);
					btn_yes.setAlignment(Pos.CENTER);
					btn_no.setAlignment(Pos.CENTER);
					moveItem(btn_yes, width*.6, height*0.7);
					moveItem(btn_no, width*.4, height*0.7);

					btn_yes.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent e) {
							if (yesno != null) {
								yesno.clickedYes();
							}
							newStage.close();
						}
					});

					btn_no.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent e) {
							if (yesno != null) {
								yesno.clickedNo();
							}
							newStage.close();
						}
					});
					break;
				case DEBUG:
					image = img_debugPath;
					Button btn_debug = new Button("OK");
					moveItem(btn_debug, width*.5, height*.7);
					pane.getChildren().add(btn_debug);
					btn_debug.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent e) {
							newStage.close();
						}
					});
					break;
				case WIN:
					image = img_winPath;

					break;
				case LOSE:
					image = img_losePath;

					break;
				case DRAW:
					image = img_drawPath;

					break;
				}
				
				ImageView error = getImageView(image);
				moveItem(error, width*.02, height*.2);
				pane.getChildren().add(error);

				Scene stageScene = new Scene(pane, width, height);
				newStage.setScene(stageScene);
				newStage.show();
			}
		});
	}

	private ImageView getImageView(String path) {
		Image image = new Image(path);
		ImageView pic = new ImageView();
		pic.setFitWidth(100);
		pic.setFitHeight(100);
		pic.setImage(image);
		return pic;
	}
	
	private void moveItem(Node n, double x, double y) {
		n.setTranslateX(x);
		n.setTranslateY(y);
	}

	public interface PopupYesNo {
		void clickedYes();

		void clickedNo();
	}
}
