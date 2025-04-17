package fr.uge.yams;

import java.util.Objects;

public interface Combination {
	
	// methode de verification
	boolean isValid(Board board);

	// comme c'est le score pour 3 des 7 combinaisons 
	default int score(Board board) {
		Objects.requireNonNull(board);
		return board.sum();
	}

	default int probability (int dicesMissing) {
		if (dicesMissing < 0 || dicesMissing > 6){
			throw new IllegalArgumentException();
		}
		return dicesMissing*(1/6);
	}

	default int coefficient (Board board, int dicesMissing) {
		return score(board)*probability(dicesMissing);
	}
}
