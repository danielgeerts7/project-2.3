package View;

import Controller.SocketController;
import Main.Main;
import Model.Config;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

 /* StartView is the first view when application is started
 *
 * @author Daniel Geerts
 * @since 2019-03-28
 */

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

		constructChooseModesPane();

		super.getChildren().add(1, mainpane);
	}

	@Override
	protected void update() {

	}

	private void clearPane() {
		mainpane.getChildren().clear();
	}

	private void constructChooseModesPane() {
		clearPane();

		showRemoteLabels(false);
		Label modesTitle = new Label("Choose modes:");
		Button btn_local = new Button("Local (You vs AI)");
		Button btn_remote = new Button("Remote (AI vs AI)");

		btn_local.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				// Do something
				clearPane();
			}
		});
		btn_remote.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (SocketController.getInstance(true) != null) {
					constructLoginPane();
				}
			}
		});

		mainpane.add(modesTitle, 0, 0);
		mainpane.add(btn_local, 0, 1);
		mainpane.add(btn_remote, 1, 1);
	}

	private void constructLoginPane() {
		clearPane();

		showRemoteLabels(true);
		TextField input_login = new TextField(username);
		input_login.setDisable(!username.isEmpty());

		Button btn_login = new Button("Login");

		btn_login.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				String loginname = input_login.getText();
				if (SocketController.getInstance(true) != null) {
					int successfull = SocketController.getInstance(true).loginOnServer(loginname);
					if (successfull == 1) {
						username = loginname;
						constructChooseGamePane(username);
						setOnlineLabel(true); // Super -> (this)client is connected with server
						setLoginAsLabel(username); // Super -> set login label
					} else if (successfull == 0) {
						constructChooseGamePane(username);
					}
				}
			}
		});
		mainpane.add(input_login, 0, 0);
		mainpane.add(btn_login, 1, 0);

		super.backToStartView.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				constructChooseModesPane();
			}
		});

		super.helpMeServer.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (SocketController.getInstance(false) != null) {
					SocketController.getInstance(false).help();
				}
			}
		});
	}

	private void constructChooseGamePane(String playerName) {
		clearPane();

		String[] games = SocketController.getInstance(true).getGamelist();
		int counter = 1;
		for (String i : games) {
			Label txt_gameName = new Label(i);
			Button btn_chooseGame = new Button("Choose " + i);

			btn_chooseGame.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					String gamename = txt_gameName.getText();
					if (SocketController.getInstance(true) != null) {
						boolean successfull = SocketController.getInstance(true).selectGame(gamename);
						if (successfull) {
							setSubscription(gamename);
							constructChooseOpponentPane(playerName, gamename);
						}
					}
				}
			});
			mainpane.add(txt_gameName, 0, counter);
			mainpane.add(btn_chooseGame, 1, counter);
			counter++;
		}

		super.backToStartView.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				constructLoginPane();
			}

		});
	}

	private void constructChooseOpponentPane(String playerName, String game) {
		clearPane();

		String[] opponents = SocketController.getInstance(true).getPlayerlist();
		int counter = 1;
		for (String enemie : opponents) {
			if (!enemie.equals(playerName)) {
				Label txt_opponentsName = new Label(enemie);
				Button btn_chooseOpponent = new Button("Choose " + enemie);

				btn_chooseOpponent.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent e) {
						String opponent = txt_opponentsName.getText();
						if (SocketController.getInstance(true) != null) {
							boolean successfull = SocketController.getInstance(true).challengeOpponent(opponent,
									game);
							if (successfull) {
								// constructChooseOpponentPane();
								System.out.println("Come at me " + opponent + ", you pussy!");
								//Main.switchScene(Main.SceneType.GAME);
							}
						}
					}
				});

				mainpane.add(txt_opponentsName, 0, counter);
				mainpane.add(btn_chooseOpponent, 1, counter);
			}
			counter++;
		}

		super.backToStartView.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				constructChooseGamePane(playerName);
			}
		});
	}
}
