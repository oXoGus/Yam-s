package fr.uge.yams.models.combinations;

import java.util.Objects;

import fr.uge.yams.models.Board;
import fr.uge.yams.models.CardCombination;

public record HightCard() implements CardCombination {
    
	@Override
	public int score(Board board) {
		Objects.requireNonNull(board);
		return 10;
	}
	
	@Override
	public boolean isValid(Board board) {
		return true;
	}

	@Override
	public String toString(){
		return "High Card";
	}

	@Override
	public String howToObtain() {
		return "Any combination of cards";
	}
	


	
}