package View;

import Model.Client;
import Model.Config;
import Model.Menu;
import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * SuperView contains all features that every scene needs In this case, a update
 * function and online label
 *
 * @author Daniel Geerts
 * @since 2019-03-28
 */
public abstract class SuperView extends Pane {

	private static Label online_label = null;
	private static Label login_label = null;
	private static Label subscription = null;
	
	protected static Menu menu = null;

	public SuperView() {
		this.setBackground(
				new Background(new BackgroundFill(Config.BACKGROUND_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));

		AnimationTimer animator = new AnimationTimer() {
			@Override
			public void handle(long now) {
				update();
				if (menu != null) {
					menu.update();
				}
			}
		};
		animator.start();

		this.addChildren();
	}

	/**
	 * addChildren of the SuperView
	 * This does happen to every sub-view
	 */
	private void addChildren() {
		clearChildren();

		this.addLabels();
		
		menu = new Menu();
		super.getChildren().add(menu.getChildren());
	}

	/**
	 * Adds all labels to every sub-view
	 */
	private void addLabels() {
		online_label = new Label();
		setOnlineLabel(Client.isConnected());
		online_label.setTranslateX(Config.WIDTH * 0.85);
		super.getChildren().add(online_label);

		login_label = new Label();
		setUsernameLabel(Client.getUsername());
		login_label.setTranslateX(Config.WIDTH * 0.85);
		login_label.setTranslateY(25);
		super.getChildren().add(login_label);

		subscription = new Label();
		setSubscriptionLabel(Client.getGame());
		subscription.setTranslateX(Config.WIDTH * 0.85);
		subscription.setTranslateY(50);
		super.getChildren().add(subscription);
	}

	/**
	 * Add a new Child on a specifiek index
	 * @param index is the layer that the Node needs to be on
	 * @param e Node to be added to the children
	 */
	protected void addChild(int index, Node e) {
		super.getChildren().add(index, e);
	}

	/**
	 * Clear scene
	 */
	protected void clearChildren() {
		super.getChildren().clear();
	}

	/**
	 * SetOnlineLabel -> set the online label to connected or not connected
	 * @param isOnline boolean of the Client is connected with the server or not
	 */
	protected static void setOnlineLabel(boolean isOnline) {
		if (isOnline) {
			online_label.setText(String.format("Server is connected"));
			online_label.setTextFill(Color.GREEN);
		} else {
			online_label.setText(String.format("Server not connected"));
			online_label.setTextFill(Color.DARKRED);
		}
		Client.setConnected(isOnline);
	}

	/**
	 * SetUsernameLabel -> set the username label
	 * @param name The name of the logged in user
	 */
	protected void setUsernameLabel(String name) {
		if (name.equals("")) {
			login_label.setText(String.format("User is not logged in yet"));
			login_label.setTextFill(Color.DARKRED);
		} else {
			login_label.setText(String.format("Logged in as: " + name));
			login_label.setTextFill(Color.GREEN);
		}
		Client.setUsername(name);
	}

	/**
	 * SetSubscirptionLabel -> set which game you want or is playing
	 * @param name Name of the game
	 */
	protected static void setSubscriptionLabel(String name) {
		if (name.equals("")) {
			subscription.setText(String.format("Not yet subscribed for a game"));
			subscription.setTextFill(Color.DARKRED);
		} else {
			subscription.setText(String.format("subscribed for: " + name));
			subscription.setTextFill(Color.GREEN);
		}
		Client.setGame(name);
	}

	/**
	 * Show remoteLabels or hide
	 * @param doShow states if the labels need to be hidden of showing
	 */
	protected void showRemoteLabels(boolean doShow) {
		online_label.setVisible(doShow);
		login_label.setVisible(doShow);
		subscription.setVisible(doShow);
		menu.showOnlineButtons(doShow);
	}

	/**
	 * Show or hide menu buttons
	 * @param doShow do show menu buttons or hide them
	 */
	protected void showButtons(boolean doShow) {
		menu.showOfflineButtons(doShow);
	}
	
	/**
	 * This method is called every available frame
	 */
	protected abstract void update();
}
