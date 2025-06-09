package fr.uge.yams.models.combinations;

import java.util.Objects;

import fr.uge.yams.models.Board;
import fr.uge.yams.models.CardCombination;

public record ThreeOfAKindCard() implements CardCombination {

	@Override
	public int score(Board board) {
		Objects.requireNonNull(board);

		return 40;
	}
	
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
		return " At least three cards of the same rank";
	}
}
