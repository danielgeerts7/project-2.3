package View;

import Controller.SocketController;
import Model.Config;
import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
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
	private static String username = "";
	private static String gamePlaying = "";
	
	protected static Button btn_back = null;
	protected static Button btn_help = null;

	public SuperView() {
		this.setBackground(
				new Background(new BackgroundFill(Config.BACKGROUND_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));

		AnimationTimer animator = new AnimationTimer() {
			@Override
			public void handle(long now) {
				update();
				if (SocketController.getInstance(false) != null
						&& SocketController.getInstance(true).getSocket() != null
						&& SocketController.getInstance(true).getSocket().isConnected()) {
					setOnlineLabel(true);
				} else {
					setOnlineLabel(false);
				}
			}
		};
		animator.start();

		this.addChildren();
	}
	
	private void addChildren() {
		clearChildren();
		
		online_label = new Label();
		setOnlineLabel(false);
		online_label.setTranslateX(Config.WIDTH * 0.85);
		super.getChildren().add(online_label);

		login_label = new Label();
		setUsername(username);
		login_label.setTranslateX(Config.WIDTH * 0.85);
		login_label.setTranslateY(25);
		super.getChildren().add(login_label);
		
		subscription = new Label();
		setSubscription(gamePlaying);
		subscription.setTranslateX(Config.WIDTH * 0.85);
		subscription.setTranslateY(50);
		super.getChildren().add(subscription);

		btn_back = new Button("Go back");
		btn_back.setTranslateX((Config.WIDTH / 2) - 100);
		btn_back.setTranslateY(25);
		super.getChildren().add(btn_back);
		
		btn_help = new Button("Help");
		btn_help.setTranslateX((Config.WIDTH / 2));
		btn_help.setTranslateY(25);
		super.getChildren().add(btn_help);
	}
	
	protected void addChild(Node e) {
		super.getChildren().add(1, e);
	}
	
	protected void clearChildren() {
		super.getChildren().clear();
	}

	protected void setOnlineLabel(boolean isOnline) {
		if (isOnline) {
			online_label.setText(String.format("Server is connected"));
			online_label.setTextFill(Color.GREEN);
		} else {
			online_label.setText(String.format("Server not connected"));
			online_label.setTextFill(Color.DARKRED);
		}
	}

	protected void setUsername(String name) {
		if (name.equals("")) {
			login_label.setText(String.format("User is not logged in yet"));
			login_label.setTextFill(Color.DARKRED);
		} else {
			login_label.setText(String.format("Logged in as: " + name));
			login_label.setTextFill(Color.GREEN);
		}
		username = name;
	}

	protected static void setSubscription(String name) {
		if (name.equals("")) {
			subscription.setText(String.format("Not yet subscribed for a game"));
			subscription.setTextFill(Color.DARKRED);
		} else {
			subscription.setText(String.format("subscribed for: " + name));
			subscription.setTextFill(Color.GREEN);
		}
		gamePlaying = name;
	}

	protected void showRemoteLabels(boolean doShow) {
		online_label.setVisible(doShow);
		login_label.setVisible(doShow);
		subscription.setVisible(doShow);
		btn_back.setVisible(doShow);
		btn_help.setVisible(doShow);
	}
	
	protected String getUsername() {
		return username;
	}

	/*
	 * This method is called every available frame
	 */
	protected abstract void update();
}
