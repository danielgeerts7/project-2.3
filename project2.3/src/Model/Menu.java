package Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import Controller.ClientSocket;
import Main.Main;
import View.Popup;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class Menu {

	VBox menuButtons = null;
	private Button btn_help = null;
	private Button btn_logout = null;
	protected static Button btn_back = null;
	private Button btn_exit = null;
	private Button btn_openMenu = null;

	private int btnWidth = 100;
	private int menuSpeed = 6;
	private boolean openMenu = false;
	private boolean menuIsOpen = false;

	public Menu() {
		btn_help = new Button("Help");
		setPosition(btn_help);
		btn_help.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (ClientSocket.getInstance(false) != null) {
					ClientSocket.getInstance(false).help();
				}
			}
		});

		btn_logout = new Button("Logout");
		setPosition(btn_logout);
		btn_logout.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Client.setConnected(false);
				Client.setUsername("");
				Client.setGame("");
				ClientSocket.getInstance(false).logout();
				Main.switchScene(Main.SceneType.START);
				Popup.getInstance().newPopup("You have successfully logged out!", Popup.Type.OK);
			}
		});

		btn_back = new Button("Go back");
		setPosition(btn_back);

		btn_exit = new Button("Exit");
		setPosition(btn_exit);
		btn_exit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (ClientSocket.getInstance(false) != null) {
					ClientSocket.getInstance(false).disconnect();
				}
				Config.QuitApp();
			}
		});

		menuButtons = new VBox();
		btn_openMenu = new Button();
		if (menuIsOpen) {
			btn_openMenu.setText("Close menu");
			menuButtons.setTranslateX(0);
		} else {
			btn_openMenu.setText("Open menu");
			menuButtons.setTranslateX(-btnWidth - 50);
		}
		btn_openMenu.setTranslateX(160);
		btn_openMenu.setTranslateY(10);
		btn_openMenu.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (!openMenu) {
					if (menuIsOpen) {
						// Menu will close
						openMenu = true;
					} else {
						// Menu will open
						openMenu = true;
					}
				}
			}
		});
		btn_openMenu.setStyle("-fx-background-radius: 5em; " + "-fx-background-color: white;");

		menuButtons.setSpacing(5);
		menuButtons.getChildren().addAll(btn_openMenu, btn_back, btn_help, btn_logout, btn_exit);
	}

	private void setPosition(Button b) {
		b.setTranslateX(30);
		b.setTranslateY(-25);
		b.maxWidth(btnWidth);
		b.setMinWidth(btnWidth);
	}

	public void update() {
		if (openMenu) {
			btn_openMenu.setDisable(true);
			if (!menuIsOpen) {
				// Animate: menu opening
				if (menuButtons.getTranslateX() < 0) {
					menuButtons.setTranslateX(menuButtons.getTranslateX() + menuSpeed);
					btn_openMenu.setText("->");
				} else {
					// Animate: menu is opened
					menuButtons.setTranslateX(0);
					openMenu = false;
					menuIsOpen = true;
					btn_openMenu.setDisable(false);
					btn_openMenu.setText("Close menu");
				}
			} else {
				// Animate: menu closing
				if (menuButtons.getTranslateX() > -btnWidth - 50) {
					menuButtons.setTranslateX(menuButtons.getTranslateX() - menuSpeed);
					btn_openMenu.setText("<-");
				} else {
					// Animate: menu is closed
					menuButtons.setTranslateX(-btnWidth - 50);
					openMenu = false;
					menuIsOpen = false;
					btn_openMenu.setDisable(false);
					btn_openMenu.setText("Open menu");
				}
			}
		}
	}

	public void showOnlineButtons(boolean doShow) {
		btn_openMenu.setVisible(doShow);
		btn_back.setVisible(doShow);
		btn_exit.setVisible(doShow);
		btn_help.setVisible(doShow);
		btn_logout.setVisible(doShow);

		if (!menuButtons.getChildren().contains(btn_logout) && !menuButtons.getChildren().contains(btn_help)) {
			menuButtons.getChildren().clear();
			menuButtons.getChildren().addAll(btn_openMenu, btn_back, btn_help, btn_logout, btn_exit);
		}
	}

	public void showOfflineButtons(boolean doShow) {
		btn_openMenu.setVisible(doShow);
		btn_back.setVisible(doShow);
		btn_exit.setVisible(doShow);

		if (menuButtons.getChildren().contains(btn_logout) && menuButtons.getChildren().contains(btn_help)) {
			menuButtons.getChildren().remove(btn_logout);
			menuButtons.getChildren().remove(btn_help);
		}
	}

	public Button getBackBtn() {
		return btn_back;
	}

	public VBox getChildren() {
		return menuButtons;
	}
}
