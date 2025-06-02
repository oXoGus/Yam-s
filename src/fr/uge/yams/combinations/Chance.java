package fr.uge.yams.combinations;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import fr.uge.yams.Board;

public record Chance() implements DiceCombination{

	// utilisation de la méthode score abstraite par défaut

	@Override
	public boolean isValid(Board board) {
		Objects.requireNonNull(board);

		// toutes les combinaisons de dé
		return true;
	}

	@Override
	public String code(){
		return "C";
	}

	@Override
	public double coefficient(Board board) {
		Objects.requireNonNull(board);
		return 0; // pour qu'elle ne soit pas choisi mais plutot en dernier recours	
	}

	@Override
	public List<Integer> dicesMissing(Board board) {
		Objects.requireNonNull(board);
		return List.copyOf(new ArrayList<Integer>()); // la combi est tjr valide
	}

	
	public String toString(String state, String score) {
		Objects.requireNonNull(state);
		Objects.requireNonNull(score);
		
		// on utilise l'état pour faire un affichage dynamique
		return "| C    | " + state + "| Chance          | Any combination                        | " + score + " |\n";
	}

	@Override
	public String toString(){
		return "Chance";
	}
	

}
