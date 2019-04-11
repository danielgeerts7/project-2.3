package View;

import Controller.ClientSocket;
import Main.Main;
import Model.Client;
import Model.Config;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 * StartView is the first view when application is started
 *
 * @author Daniel Geerts
 * @since 2019-03-28
 */

public class StartView extends SuperView {

	private GridPane mainpane = null;
	private ListView<String> list = null;

	public StartView() {
		super();
		mainpane = new GridPane();
		mainpane.setAlignment(Pos.CENTER);
		mainpane.setMinSize(Config.WIDTH, Config.HEIGHT);
		mainpane.setHgap(10);
		mainpane.setVgap(10);
		mainpane.setPadding(new Insets(10, 10, 10, 10));

		constructChooseModesPane();

		super.addChild(1, mainpane);
	}

	/**
	 * Clear pane
	 */
	private void clearPane() {
		mainpane.getChildren().clear();
	}
	
	/**
	 * Constructs first view -> Choose a modes of the application
	 * Offline or online (local or remote)
	 */
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
				showRemoteLabels(false);
				showOfflineButtons(true);
				constructChooseOfflineGamePane();
				menu.getBackBtn().setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent e) {
						constructChooseModesPane();
					}
				});
			}
		});
		btn_remote.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (ClientSocket.getInstance(true) != null) {
					constructLoginPane();
					setOnlineLabel(true); // Super -> (this)client is connected with server
				}
			}
		});

		mainpane.add(modesTitle, 0, 0);
		mainpane.add(btn_local, 0, 1);
		mainpane.add(btn_remote, 1, 1);
	}

	/**
	 * Constructs loginPane where user can login onto the server
	 */
	private void constructLoginPane() {
		clearPane();

		showRemoteLabels(true);
		TextField input_login = new TextField(Client.getUsername());
		input_login.setDisable(!Client.getUsername().isEmpty());

		Button btn_login = new Button("Login");

		btn_login.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				String loginname = input_login.getText();
				if (ClientSocket.getInstance(true) != null) {
					int successfull = ClientSocket.getInstance(false).loginOnServer(loginname);
					if (successfull == 1) {
						constructChooseGamePane(loginname);
						setUsernameLabel(loginname); // Super -> set login(username) label
					} else if (successfull == 0) {
						constructChooseGamePane(loginname);
					}
				}
			}
		});
		mainpane.add(input_login, 0, 0);
		mainpane.add(btn_login, 1, 0);

		menu.getBackBtn().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				constructChooseModesPane();
			}
		});
	}

	/**
	 * Constructs GamePane where user can select a game to play (remote)
	 * AI vs remote AI
	 */
	private void constructChooseGamePane(String playerName) {
		clearPane();

		String[] games = ClientSocket.getInstance(true).getGamelist();
		int counter = 1;
		for (String i : games) {
			Label txt_gameName = new Label(i);
			Button btn_chooseGame = new Button("Choose " + i);

			btn_chooseGame.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					if (ClientSocket.getInstance(true) != null) {
						String gamename = txt_gameName.getText();
						ClientSocket.getInstance(false).selectGame(gamename);
						setSubscriptionLabel(gamename); // super -> set Subscription Label
						constructChooseOpponentPane(playerName, gamename);
					}
				}
			});
			mainpane.add(txt_gameName, 0, counter);
			mainpane.add(btn_chooseGame, 1, counter);
			counter++;
		}
		if (Client.getGame().equals("")) {
			Button btn_skipThisView = new Button("Skip view");
			btn_skipThisView.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					constructChooseOpponentPane(playerName, "");
				}
			});

			mainpane.add(btn_skipThisView, 0, 0);
		}

		menu.getBackBtn().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				constructLoginPane();
			}
		});
	}
	
	/**
	 * Constructs GamePane where user can select a game to play (offline)
	 * Client vs offline AI
	 */
	private void constructChooseOfflineGamePane() {
		clearPane();

		String[] games = { Main.SceneType.REVERSI.toString(),  Main.SceneType.TICTACTOE.toString() };
		int counter = 1;
		for (String i : games) {
			Label txt_gameName = new Label(i);
			Button btn_chooseGame = new Button("Choose " + i);

			btn_chooseGame.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					Client.setConnected(false);
					if (i.equals(Main.SceneType.REVERSI.toString())) {
						Main.switchScene(Main.SceneType.REVERSI, false);
					}	else if (i.equals(Main.SceneType.TICTACTOE.toString())) {
						Main.switchScene(Main.SceneType.TICTACTOE, false);
					}
				}
			});
			mainpane.add(txt_gameName, 0, counter);
			mainpane.add(btn_chooseGame, 1, counter);
			counter++;
		}
	}

	/**
	 * Constructs OpponentPane, where a player lobby will be shown
	 * The user can select a player to play against
	 */
	private void constructChooseOpponentPane(String playerName, String game) {
		clearPane();
		String[] opponents = ClientSocket.getInstance(true).getPlayerlist();
		list = new ListView<String>();
		ObservableList<String> items = FXCollections.observableArrayList(opponents);
		list.setItems(items);
		list.setPrefWidth(300);
		list.setPrefHeight(500);
		Button btn_chooseOpponent = new Button("Select a player");
		btn_chooseOpponent.setDisable(true);
		btn_chooseOpponent.minWidth(100);

		if (!game.isEmpty()) {
			list.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
				public void changed(ObservableValue<? extends String> ov, String old_val, String new_val) {
					btn_chooseOpponent.setText("Select " + new_val);
					btn_chooseOpponent.setDisable(false);
					btn_chooseOpponent.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent e) {
							if (ClientSocket.getInstance(true) != null) {
								ClientSocket.getInstance(false).challengeOpponent(new_val, game);
							}
						}
					});
				}
			});
		} else {
			btn_chooseOpponent.setText("Go back and select a game first");
		}

		Button btn_refresh = new Button("Refresh player lobby");
		btn_refresh.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				String[] opponents = ClientSocket.getInstance(true).getPlayerlist();
				ObservableList<String> items = FXCollections.observableArrayList(opponents);
				list.setItems(items);
				if (!game.isEmpty()) {
					btn_chooseOpponent.setText("Select a player");
				}
				btn_chooseOpponent.setDisable(true);
			}
		});

		mainpane.add(list, 0, 0);
		mainpane.add(btn_chooseOpponent, 0, 1);
		mainpane.add(btn_refresh, 0, 2);

		menu.getBackBtn().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				constructChooseGamePane(playerName);
			}
		});
	}
}
