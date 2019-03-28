package View;

import Main.Config;
import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public abstract class SuperView extends Pane {

	private Label online_label = null;

	public SuperView() {
		this.setBackground(new Background(new BackgroundFill(Config.BACKGROUND_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));

		AnimationTimer animator = new AnimationTimer() {
			@Override
			public void handle(long now) {
				update();
				// updateOnlineLabel(now);
			}
		};
		animator.start();

		online_label = new Label();
		online_label.setTranslateX(Config.WIDTH - 150);
		setOnlineLabel(false);
		super.getChildren().add(online_label);
	}

	public void setOnlineLabel(boolean isOnline) {
		if (isOnline) {
			online_label.setText(String.format("Server is connected"));
			online_label.setTextFill(Color.GREEN);
		} else {
			online_label.setText(String.format("Server not connected"));
			online_label.setTextFill(Color.DARKRED);
		}
	}

	/*
	 * This method is called every available frame
	 */
	protected abstract void update();
}
