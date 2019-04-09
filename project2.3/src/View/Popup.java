package View;

import java.util.HashMap;

import Main.Main;
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
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Popup is a Singleton class So a popup can occur throughout the project the
 * whole application
 *
 * @author Daniel Geerts
 * @since 2019-03-28
 */
public final class Popup {

	public enum Type {
		DEBUG, OK, YESNO, WIN, LOSS, DRAW
	};

	private int width = 425;
	private int height = 200;
	private String img_okPath = "File:img/OK.png";
	private String img_debugPath = "File:img/DEBUG.png";
	private String img_YesNoPath = "File:img/YESNO.png";
	private String img_winPath = "File:img/WIN.png";
	private String img_losePath = "File:img/LOSE.png";
	private String img_drawPath = "File:img/DRAW.png";
	
	public HashMap<String, Stage> popups = null;

	private static Popup instance = null;

	public static Popup getInstance() {
		if (instance == null) {
			instance = new Popup();
		}
		return instance;
	}

	private Popup() {
		popups = new HashMap<String, Stage>();
	}

	public void newPopup(String text, Type type) {
		newPopup(text, type, null, "");
	}

	public void newPopup(String text, Type type, PopupYesNo yesno, String hash) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				final Stage newStage = new Stage();
				newStage.setTitle("Popup: " + type.toString().toLowerCase());
				Pane pane = new Pane();
				pane.setMinSize(width, height);

				String image = "";
				boolean addOKbtn = false;

				switch (type) {
				case OK:
					image = img_okPath;
					addOKbtn = true;
					break;
				case YESNO:
					image = img_YesNoPath;
					addOKbtn = false;
					Button btn_yes = new Button("YES");
					Button btn_no = new Button("NO");
					pane.getChildren().add(btn_yes);
					pane.getChildren().add(btn_no);
					btn_yes.setAlignment(Pos.CENTER);
					btn_no.setAlignment(Pos.CENTER);
					moveItem(btn_yes, width * .6, height * .8);
					moveItem(btn_no, width * .4, height * .8);
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
					addOKbtn = true;
					break;
				case WIN:
					image = img_winPath;
					addOKbtn = true;
					break;
				case LOSS:
					image = img_losePath;
					addOKbtn = true;
					break;
				case DRAW:
					image = img_drawPath;
					addOKbtn = true;
					break;
				}
				
				Label message = new Label(addEnterInString(text, 45));
				message.setAlignment(Pos.CENTER);
				moveItem(message, width * .3, height * 0.15);
				pane.getChildren().add(message);
				
				if (addOKbtn) {
					Button btn_ok = new Button("OK");
					btn_ok.setAlignment(Pos.CENTER);
					moveItem(btn_ok, width * .5, height * .8);
					pane.getChildren().add(btn_ok);
					btn_ok.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent e) {
							newStage.close();
							if (type.equals(Type.WIN) || type.equals(Type.LOSS) || type.equals(Type.DRAW)) {
								Platform.runLater(new Runnable() {
									@Override
									public void run() {
										Main.switchScene(Main.SceneType.START);
									}
								});
							}
						}
					});
				}

				ImageView error = getImageView(image);
				moveItem(error, width * .02, height * .2);
				pane.getChildren().add(error);

				Scene stageScene = new Scene(pane, width, height);
				newStage.setScene(stageScene);
				newStage.show();
				
				if (!hash.isEmpty()) {
					popups.put(hash, newStage);
				}
			}
		});
	}

	private String addEnterInString(String text, int maxLineLength) {
		int line = 1;
		int last_space = 0;
		int begin_sub = 0;
		int offset = 0;
		String newText = "";

		for (int i = 0; i < text.length(); i++) {
			if (text.charAt(i) == ' ') {
				last_space = i;
			}
			if (text.charAt(i) == '\n') {
				offset = i;
			}
			if (offset + (line * maxLineLength) == i) {
				newText += text.substring(begin_sub, last_space);
				newText += "\n";
				begin_sub = last_space + 1;
				line++;
			}
			if (i == text.length() - 1) {
				newText += text.substring(begin_sub, i + 1);
			}
		}
		return newText;
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
