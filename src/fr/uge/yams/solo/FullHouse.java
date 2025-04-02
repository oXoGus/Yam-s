package fr.uge.yams.solo;

import java.util.Objects;

public record FullHouse() implements Combination {

	@Override
	public int score(Board board) {
		Objects.requireNonNull(board);
		return 25;
	}

	@Override
	public boolean isValid(Board board) {
		Objects.requireNonNull(board);

		// si au moins 3 dés on la meme valeur 
		// et les deux autres on la meme valeur 
		var occList = board.occurence();
		return occList.contains(3) && occList.contains(2);
	}

	public String toString(int state) {
		// on utilise l'état pour faire un affichage dynamique
		return "| 4   | "+ (state == -1 ? "sacrified " : (state == 1 ? "validate  " : "          ")) + "| Full House      | Three of one number and two of another | 25	       |\n";
	}

}
