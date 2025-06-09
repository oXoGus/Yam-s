package fr.uge.yams.models.combinations;

import java.util.Objects;

import fr.uge.yams.models.Board;
import fr.uge.yams.models.Combination;

public record FourOfAKind() implements Combination {

	// utilisation de la méthode score abstraite par défaut
	
	@Override
	public boolean isValid(Board board) {
		Objects.requireNonNull(board);

		return board.maxOcc() >= 4;
	}

	@Override
	public String toString(){
		return "Four of a kind";
	}

	@Override
	public String howToObtain() {
		return "At least four dice the same";
	}
	
	@Override
	public String scoreInfo(Board board){
		return "Sum of all dices";
	}
}
