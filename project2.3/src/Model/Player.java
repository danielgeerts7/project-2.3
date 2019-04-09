package Model;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class Player extends GridPane {

	private Label label_name = null;
	private Label label_score = null;
	private Label label_color = null;
	private String name = "";
	private int score = 0;
	private char color = 0;
	
	public Player() {
		label_name = new Label("Player without name");
		label_score = new Label("Score: " + score);
		label_color = new Label("Color: " + color);
		this.add(label_name, 0, 0);
		this.add(label_score, 0, 1);
		this.add(label_color, 0, 2);
	}
	
	public void setName(String newName) {
		name = newName;
		updateName();
	}
	
	public String getName() {
		return name;
	}
	
	private void updateName() {
		label_name.setText("Player: " + name);
	}
	
	public void setScore(int score) {
		this.score = score;
		updateScore();
	}
	
	public int getScore() {
		return score;
	}
	
	private void updateScore() {
		label_score.setText("Score: " + score);
	}
	
	public void setColor(char col) {
		color = col;
		updateColor();
	}
	
	public char getColor() {
		return color;
	}
	
	private void updateColor() {
		if (color == ReversiGame.BLACK) {
			label_color.setText("Color: BLACK");
		} else if (color == ReversiGame.WHITE) {
			label_color.setText("Color: WHITE");
		}
	}
}
