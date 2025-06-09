package fr.uge.yams.models.combinations;

import java.util.Objects;

import fr.uge.yams.models.Board;
import fr.uge.yams.models.Combination;

public record Yahtzee() implements Combination {

	@Override
	public int score(Board board) {
		Objects.requireNonNull(board);
		return 50;
	}
	
	@Override
	public boolean isValid(Board board) {
		Objects.requireNonNull(board);
		return board.maxOcc() == 5;
	}

	@Override
	public String toString(){
		return "Yahtzee";
	}

	@Override
	public String howToObtain() {
		return "All five dice the same";
	}

}
