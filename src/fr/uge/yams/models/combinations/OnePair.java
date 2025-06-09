package fr.uge.yams.models.combinations;

import java.util.Objects;

import fr.uge.yams.models.Board;
import fr.uge.yams.models.CardCombination;

public record OnePair() implements CardCombination {

	@Override
	public int score(Board board) {
		Objects.requireNonNull(board);

		return 20;
	}
    
	@Override
	public boolean isValid(Board board) {
		Objects.requireNonNull(board);
		
        return board.occurence().contains(2);
	}


	@Override
	public String toString(){
		return "One Pair";
	}

	@Override
	public String howToObtain() {
		return "Two cards if the same rank";
	}
}