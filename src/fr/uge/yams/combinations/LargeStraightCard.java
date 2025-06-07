package fr.uge.yams.combinations;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import fr.uge.yams.Board;
import fr.uge.yams.Games;

public record LargeStraightCard() implements Combination {

	@Override
	public int score(Board board) {
		Objects.requireNonNull(board);
		return 40;
	}

	@Override
	public boolean isValid(Board board) {
		Objects.requireNonNull(board);
		
		// si toutes les cartes se suivent
		return board.elementsFormingSeq().size() == 5;
	}

	@Override
	public List<Integer> elementsMissing(Board board) {
		Objects.requireNonNull(board);

		var dicesKept = board.elementsFormingSeq();
		var res = new ArrayList<Integer>();
		
		// on créer la list contenant les position 
		// qui ne sont pas des dicesKept
		for (int i = 1; i < 6; i++){
			if (!dicesKept.contains(i)){
				res.add(i);
			}
		}

		return List.copyOf(res);
	}

	@Override
	public String code(){
		// code pour parse les combinisons dans le terminal
		return "L";
	}

	@Override
	public double probability (int numDicesMissing) {
		if (numDicesMissing < 0 || numDicesMissing > 6){
			throw new IllegalArgumentException();
		}

		if (numDicesMissing == 0){
			return 1;
		}
	
		// pour les combi de type seq 
		// le premier dé a numDicesMissing/6 de chance de tomber un dé qui convient
		// le deuxieme dé a (numDicesMissing - 1)/6
		// ...
		// donc factoriel de numDicesMissing/6
		return Games.fact(numDicesMissing)/6.0;
	}

	public String toString(String state, String score) {
		Objects.requireNonNull(state);
		Objects.requireNonNull(score);
		
		// on utilise l'état pour faire un affichage dynamique
		return "| L    | "+ state + "| Large Straight  | Five sequential dice                   | 40              |\n";
	}
	
	@Override
	public String toString(){
		return "Large Straight";
	}

	@Override
	public String howToObtain() {
		return "Any combination of dices";
	}

}
