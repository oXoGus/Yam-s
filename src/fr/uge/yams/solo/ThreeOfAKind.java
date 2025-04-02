package fr.uge.yams.solo;

import java.util.Objects;

public record ThreeOfAKind() implements Combination {

	// utilisation de la méthode abstraite par défaut
	
	@Override
	public boolean isValid(Board board) {
		Objects.requireNonNull(board);
		return board.maxOcc() >= 3;
	}


	public String toString(int state) {
		// on utilise l'état pour faire un affichage dynamique
		return "| 2   | "+ (state == -1 ? "sacrified " : (state == 1 ? "validate  " : "          ")) + "| Three of a Kind | At least three dice the same           | Sum of all dice |\n";
	}

}
