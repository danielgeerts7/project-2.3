package Model;

import Model.TicTacToeGame;
import javafx.geometry.Pos;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Tile extends StackPane{
	private static Text text = new Text();
	
	public boolean playable = true;
	public boolean turnX = true;
	
	public Tile() {
		Rectangle border = new Rectangle(150, 150);
		border.setFill(null);
		border.setStroke(Color.BLACK);
		
		text.setFont(Font.font(72));
		
		setAlignment(Pos.CENTER);
		getChildren().addAll(border, text);
		
		setOnMouseClicked(event -> {
			if (!playable)
				return;
			
			
			if (event.getButton() == MouseButton.PRIMARY) {
				if (!turnX) {
					drawO();
					turnX = true;
					Model.TicTacToeGame.checkState();
					return;
				}
				else {
					drawX();
					turnX = false;
					Model.TicTacToeGame.checkState();
					return;
					}
				}
		});
	}
	
	public double getCenterX() {
		return getTranslateX() + 75;
	}
	
	public double getCenterY() {
		return getTranslateY() + 75;
	}
	
	public String getValue() {
		return text.getText();		
	}
	
	private void drawX() {
		text.setText("X");
	}
	
	private void drawO() {
		text.setText("O");
	}
}
