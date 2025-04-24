package fr.uge.yams;

import java.util.ArrayList;
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
	//Renvoit les index dans fiveDice des dés à reroll
	public List<Integer> dicesMissing(Board board){
		Objects.requireNonNull(board);
		
		var missing = new ArrayList<Integer>();
		
		// occurence   
		var occ =  board.occurence();
		//L'index i correspond à la valeur i 
		//A l'index i, occurence du dé i 
		for (int i = 0; i<5; i++) {
			//i est l'index des dés dans fiveDices
			//Si l'occurence de la valeur du dé est de 1
			//C'est donc le seul et on renvoit ce dés
			if (occ.get(board.fiveDice().get(i).value())==1) {
				missing.add(i + 1);
			}
		}

		return List.copyOf(missing);

	}

	public String toString(String state) {
		Objects.requireNonNull(state);

		// on utilise l'état pour faire un affichage dynamique
		return "| Fu   | "+ state + "| Full House      | Three of one number and two of another | 25              |\n";
	}

	@Override
	public String toString(){
		return "Full House";
	}
}
