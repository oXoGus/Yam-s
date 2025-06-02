package fr.uge.yams.combinations;

import java.util.Objects;

import fr.uge.yams.Board;

public record FourOfAKind() implements DiceCombination {

	// utilisation de la méthode score abstraite par défaut
	
	@Override
	public boolean isValid(Board board) {
		Objects.requireNonNull(board);

		return board.maxOcc() >= 4;
	}

	@Override
	public String code(){
		return "Fo";
	}

	@Override
	public String toString(String state, String score) {
		Objects.requireNonNull(state);
		Objects.requireNonNull(score);

		// on utilise l'état pour faire un affichage dynamique
		return "| Fo   | "+ state + "| Four of a Kind  | At least four dice the same            | " + score + " |\n";
	}

	@Override
	public String toString(){
		return "Four of a kind";
	}

	

}
