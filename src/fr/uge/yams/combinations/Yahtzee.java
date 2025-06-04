package fr.uge.yams.combinations;

import java.util.Objects;

import fr.uge.yams.Board;

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

	@Override
	public String code(){
		// code pour parse les combinisons dans le terminal
		return "Y";
	}

	public String toString(String state, String score) {
		Objects.requireNonNull(state);
		Objects.requireNonNull(score);
		// on utilise l'état pour faire un affichage dynamique
		return "| Y    | "+ state + "| Yahtzee         | All five dice the same                 | 50              |\n";
	}
	
	@Override
	public String toString(){
		return "Yahtzee";
	}

}
