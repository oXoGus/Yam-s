package fr.uge.yams;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public interface Combination {
	
	// methode de verification
	boolean isValid(Board board);

	// comme c'est le score pour 3 des 7 combinaisons 
	default int score(Board board) {
		Objects.requireNonNull(board);
		return board.sum();
	}

	default double probability (int dicesMissing) {
		if (dicesMissing < 0 || dicesMissing > 6){
			throw new IllegalArgumentException();
		}
		return dicesMissing*(1/6);
	}

	default double coefficient (Board board, int dicesMissing) {
		Objects.requireNonNull(board);

		return score(board)*probability(dicesMissing);
	}
	
	// renvoie une liste non modifiable contenant les dices qui doivent etre relancer 
	// pour espérer valider la combinaison 
	// attention on revoie bien l'index des dés dans fiveDice 
	default List<Integer> dicesMissing(Board board){

		// default pour les 4 combi de type Same
		
		// la liste des index des dés qui on le plus d'occurence 
		var dicesMaxOcc = board.dicesMaxOcc();
		
		// les dés qu'on peut reroll sont donc 
		// tous les dés qui ne sont pas dans
		
		var dicesToReroll = new ArrayList<Integer>();
		for (int i = 0; i < 5; i++){
			if (!dicesMaxOcc.contains(i)){
				dicesToReroll.add(i);
			}
		}
		return List.copyOf(dicesToReroll);
	}
}
