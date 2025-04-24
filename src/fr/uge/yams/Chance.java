package fr.uge.yams;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record Chance() implements Combination {

	// utilisation de la méthode score abstraite par défaut
	
	@Override
	public boolean isValid(Board board) {
		Objects.requireNonNull(board);

		// toutes les combinaisons de dé
		return true;
	}

	@Override
	public List<Integer> dicesMissing(Board board) {
		return List.copyOf(new ArrayList<Integer>()); // la combi est tjr valide
	}

	public String toString(String state, String score) {
		Objects.requireNonNull(state);
		Objects.requireNonNull(score);
		
		// on utilise l'état pour faire un affichage dynamique
		return "| C    | "+ state + "| Chance          | Any combination                        | " + score + " |\n";
	}

}
