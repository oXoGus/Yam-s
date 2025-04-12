package fr.uge.yams.solo;

import java.util.Objects;

public interface Combination {
	
	// methode de verification
	boolean isValid(Board board);

	// comme c'est le score pour 3 des 7 combinaisons 
	default int score(Board board) {
		Objects.requireNonNull(board);
		return board.sum();
	}
}
