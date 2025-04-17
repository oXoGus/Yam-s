package fr.uge.yams;

import java.util.List;
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

	@Override
	public List<Integer> dicesMissing(Board board){
		
		// occurence   
		var occ =  board.occurence();

	}

	public String toString(String state) {
		Objects.requireNonNull(state);

		// on utilise l'état pour faire un affichage dynamique
		return "| Fu   | "+ state + "| Full House      | Three of one number and two of another | 25              |\n";
	}

}
