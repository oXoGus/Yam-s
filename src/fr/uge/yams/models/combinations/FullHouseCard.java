package fr.uge.yams.models.combinations;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import fr.uge.yams.models.Board;
import fr.uge.yams.models.CardCombination;

public record FullHouseCard() implements CardCombination {

	@Override
	public int score(Board board) {
		Objects.requireNonNull(board);
		return 70;
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
	public List<Integer> elementsMissing(Board board){
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
			if (occ.get(board.getElementsList().get(i).rank())==1) {
				missing.add(i + 1);
			}
		}

		return List.copyOf(missing);

	}

	@Override
	public String toString(){
		return "Full House";
	}

	@Override
	public String howToObtain() {
		return "Three of a kind + a pair";
	}
}
