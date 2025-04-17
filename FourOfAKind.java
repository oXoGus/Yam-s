package fr.uge.yams;

import java.util.Objects;

public record FourOfAKind() implements Combination {

	// utilisation de la méthode score abstraite par défaut
	
	@Override
	public boolean isValid(Board board) {
		Objects.requireNonNull(board);

		return board.maxOcc() >= 4;
	}


	public String toString(String state, String score) {
		Objects.requireNonNull(state);
		Objects.requireNonNull(score);

		// on utilise l'état pour faire un affichage dynamique
		return "| Fo   | "+ state + "| Four of a Kind  | At least four dice the same            | " + score + " |\n";
	}

}
