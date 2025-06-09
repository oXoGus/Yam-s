package fr.uge.yams.models.combinations;

import java.util.Objects;

import fr.uge.yams.models.Board;
import fr.uge.yams.models.CardCombination;

public record FourOfAKindCard() implements CardCombination {

	@Override
	public int score(Board board) {
		Objects.requireNonNull(board);

		return 80;
	}
	
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
		return "At least four cards of the same rank";
	}
}
