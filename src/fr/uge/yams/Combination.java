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


	default double probability (int numDicesMissing) {
		if (numDicesMissing < 0 || numDicesMissing > 6){
			throw new IllegalArgumentException();
		}
	
		// pour les 4 combi de type same 
		// la proba est (1/6)^numDiceMissing puisqu'il faut tomber sur les meme dés
		double pow = 1.0;
		
		for (int i = 0; i < numDicesMissing; i++){
			pow = pow * 1/6;
		}
		
		return pow;
	}

	default double coefficient (Board board) {
		Objects.requireNonNull(board);

		// plus la proba est grande plus le coef sera grand 
		// on multiplie aussi par le score pour choisir les meilleurs scores
		return score(board)*probability(dicesMissing(board).size());
	}
	
	// renvoie une liste non modifiable contenant les dices qui doivent etre relancer 
	// pour espérer valider la combinaison 
	// attention on revoie bien la position des dés dans fiveDice 
	default List<Integer> dicesMissing(Board board){
		Objects.requireNonNull(board);

		if (isValid(board)){
			return List.of();
		}

		// la liste des index des dés qui on le plus d'occurence 
		var dicesMaxOcc = board.dicesMaxOcc();
		
		// les dés qu'on peut reroll sont donc 
		// tous les dés qui ne sont pas dans 
		
		var dicesToReroll = new ArrayList<Integer>();
		for (int i = 0; i < 5; i++){
			if (!dicesMaxOcc.contains(i)){
				dicesToReroll.add(i + 1);
			}
		}
		return List.copyOf(dicesToReroll);
	}	
}
