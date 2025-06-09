package fr.uge.yams.models.combinations;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import fr.uge.yams.models.Board;
import fr.uge.yams.models.Combination;
import fr.uge.yams.models.Games;

public record LargeStraight() implements Combination {

	@Override
	public int score(Board board) {
		Objects.requireNonNull(board);
		return 40;
	}

	@Override
	public boolean isValid(Board board) {
		Objects.requireNonNull(board);
		// 4 dés conséctif
		
		// on test les 3 combinaisons possibles
		// 1, 2, 3, 4, 5
		// 2, 3, 4, 5, 6
		var occList = board.occurence();

		if (occList.get(1) > 0 && occList.get(2) > 0 && occList.get(3) > 0 && occList.get(4) > 0 && occList.get(5) > 0){
			return true;
		}
		if (occList.get(2) > 0 && occList.get(3) > 0 && occList.get(4) > 0 && occList.get(5) > 0 && occList.get(6) > 0){
			return true;
		}
		return false;
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
	
	@Override
	public String toString(){
		return "Large Straight";
	}

	@Override
	public String howToObtain() {
		return "Five sequential dice";
	}

}
