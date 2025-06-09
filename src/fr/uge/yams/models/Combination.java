package fr.uge.yams.models;

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


	default double probability (int numGameElementMissing) {
		if (numGameElementMissing < 0 || numGameElementMissing > 5){
			throw new IllegalArgumentException();
		}
	
		// pour les 4 combi de type same 
		// la proba est (1/6)^numDiceMissing puisqu'il faut tomber sur les meme éléments de jeu
		double pow = 1.0;
		
		for (int i = 0; i < numGameElementMissing; i++){
			pow = pow * 1/6;
		}
		
		return pow;
	}

	default double coefficient (Board board) {
		Objects.requireNonNull(board);

		// plus la proba est grande plus le coef sera grand 
		// on multiplie aussi par le score pour choisir les meilleurs scores
		return score(board)*probability(elementsMissing(board).size());
	}
	
	// renvoie une liste non modifiable contenant les dices qui doivent etre relancer 
	// pour espérer valider la combinaison 
	// attention on revoie bien la position des éléments de jeu dans fiveDice 
	default List<Integer> elementsMissing(Board board){
		Objects.requireNonNull(board);

		if (isValid(board)){
			return List.of();
		}

		// la liste des index des éléments de jeu qui on le plus d'occurence 
		var maxOcc = board.elementsMaxOcc();
		
		// les éléments de jeu qu'on peut reroll sont donc 
		// tous les éléments de jeu qui ne sont pas dans 
		
		var elementsToReroll = new ArrayList<Integer>();
		for (int i = 0; i < board.getElementsList().size(); i++){
			if (!maxOcc.contains(i)){
				elementsToReroll.add(i + 1);
			}
		}
		return List.copyOf(elementsToReroll);
	}	

	String howToObtain();

	// pour pouvoir afficher les scores qui dépendent du board
	default String scoreInfo(Board board){
		Objects.requireNonNull(board);
		return String.valueOf(score(board));
	}
}
