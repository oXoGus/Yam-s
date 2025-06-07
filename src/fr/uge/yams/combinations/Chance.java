package fr.uge.yams.combinations;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import fr.uge.yams.Board;

public record Chance() implements Combination{

	// utilisation de la méthode score abstraite par défaut

	@Override
	public boolean isValid(Board board) {
		Objects.requireNonNull(board);

		// toutes les combinaisons de dé
		return true;
	}

	@Override
	public String code(){
		return "C";
	}

	@Override
	public double coefficient(Board board) {
		Objects.requireNonNull(board);
		return 0; // pour qu'elle ne soit pas choisi mais plutot en dernier recours	
	}

	@Override
	public List<Integer> elementsMissing(Board board) {
		Objects.requireNonNull(board);
		return List.copyOf(new ArrayList<Integer>()); // la combi est tjr valide
	}

	@Override
	public String toString(){
		return "Chance";
	}

	@Override
	public String howToObtain() {
		return "Any combination of dices";
	}
	
	@Override
	public String scoreInfo(Board board){
		return "Sum of all dices";
	}

}
