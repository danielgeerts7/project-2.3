package Model;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class Player extends GridPane {

	private Label label_name = null;
	private Label label_score = null;
	private int score = 0;
	
	public Player() {
		label_name = new Label("Player without name");
		label_score = new Label("Score: " + score);
		this.add(label_name, 0, 0);
		this.add(label_score, 0, 1);
	}
	
	public void setName(String newName) {
		label_name.setText("Player: " + newName);
	}
	
	public String getName() {
		return label_name.getText();
	}
	
	public void setScore(int score) {
		this.score = score;
		updateScore();
	}
	
	public void addScore(int score) {
		this.score += score;
		updateScore();
	}
	
	public int getScore() {
		return score;
	}
	
	private void updateScore() {
		label_score.setText("Score: " + score);
	}
}
