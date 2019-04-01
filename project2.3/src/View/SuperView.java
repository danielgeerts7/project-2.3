package View;

import Controller.ClientSocketController;
import Model.Config;
import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
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
 * @author Daniël Geerts
 * @since 2019-03-28
 */
public abstract class SuperView extends Pane {

	private Label online_label = null;
	private Label login_label = null;
	protected Button backToStartView = null;
	protected String username = "";

	public SuperView() {
		this.setBackground(
				new Background(new BackgroundFill(Config.BACKGROUND_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));

		AnimationTimer animator = new AnimationTimer() {
			@Override
			public void handle(long now) {
				update();
				if (ClientSocketController.getInstance(false) != null
						&& ClientSocketController.getInstance(true).getSocket() != null
						&& ClientSocketController.getInstance(true).getSocket().isConnected()) {
					setOnlineLabel(true);
				} else {
					setOnlineLabel(false);
				}
			}
		};
		animator.start();

		online_label = new Label();
		this.setOnlineLabel(false);
		online_label.setTranslateX(Config.WIDTH * 0.85);
		super.getChildren().add(online_label);

		login_label = new Label();
		this.setLoginAsLabel("");
		login_label.setTranslateX(Config.WIDTH * 0.85);
		login_label.setTranslateY(25);
		super.getChildren().add(login_label);
		
		backToStartView = new Button("Go back");
		backToStartView.setTranslateX(Config.WIDTH /2);
		backToStartView.setTranslateY(25);
		super.getChildren().add(backToStartView);
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

	protected void setLoginAsLabel(String name) {
		if (name.equals("")) {
			login_label.setText(String.format("User is not logged in yet"));
			login_label.setTextFill(Color.DARKRED);
		} else {
			login_label.setText(String.format("Logged in as: " + name));
			login_label.setTextFill(Color.GREEN);
		}
	}

	protected void showRemoteLabels(boolean doShow) {
		online_label.setVisible(doShow);
		login_label.setVisible(doShow);
		backToStartView.setVisible(doShow);
	}

	/*
	 * This method is called every available frame
	 */
	protected abstract void update();
}
