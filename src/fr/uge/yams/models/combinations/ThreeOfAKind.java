package fr.uge.yams.models.combinations;

import java.util.Objects;

import fr.uge.yams.models.Board;
import fr.uge.yams.models.Combination;

public record ThreeOfAKind() implements Combination {

	// utilisation de la méthode abstraite par défaut
	
	@Override
	public boolean isValid(Board board) {
		Objects.requireNonNull(board);
		return board.maxOcc() >= 3;
	}


	@Override
	public String toString(){
		return "Three of a kind";
	}

	@Override
	public String howToObtain() {
		return " At least three dice the same";
	}

	@Override
	public String scoreInfo(Board board){
		return "Sum of all dices";
	}

}
