package fr.uge.yams.solo;

import java.util.Objects;

public record FourOfAKind() implements Combination {

	// utilisation de la méthode score abstraite par défaut
	
	@Override
	public boolean isValid(Board board) {
		Objects.requireNonNull(board);

		return board.maxOcc() >= 4;
	}


	public String toString(int state) {
		// on utilise l'état pour faire un affichage dynamique
		return "| 3   | "+ (state == -1 ? "sacrified " : (state == 1 ? "validate  " : "          ")) + "| Four of a Kind  | At least four dice the same            | Sum of all dice |\n";
	}

}
