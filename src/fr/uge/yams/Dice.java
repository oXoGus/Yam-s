package fr.uge.yams;

import java.io.IOException;
import java.util.Random;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Shape;

public record Dice(int value) implements GameElement{

	public Dice {
		if (value > 6 || value < 1) {
			throw new IllegalArgumentException();
		}
	}

	public Dice() {
		this(new Random().nextInt(6) + 1);
	}

	public Dice reroll() {
		return new Dice();
	}

	// pour afficher le dé
	@Override
	public Node shape() {
		try {
			// on récupère la liste des shapes des dés 
			AnchorPane dices = FXMLLoader.load(getClass().getResource("/fr/uge/yams/views/dices.fxml"));
			
			// les dés sont de 1 a 6 
			return dices.getChildren().get(value - 1);

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
