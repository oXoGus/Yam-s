package fr.uge.yams.solo;

import java.util.Objects;

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


	public String toString(String state) {
		// on utilise l'état pour faire un affichage dynamique
		return "| Y    | "+ state + "| Yahtzee         | All five dice the same                 | 50              |\n";
	}

}
