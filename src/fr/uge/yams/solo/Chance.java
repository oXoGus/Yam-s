package fr.uge.yams.solo;

import java.util.Objects;

public record Chance() implements Combination {

	// utilisation de la méthode score abstraite par défaut
	
	@Override
	public boolean isValid(Board board) {
		Objects.requireNonNull(board);

		// toutes les combinaisons de dé
		return true;
	}


	public String toString(int state) {
		// on utilise l'état pour faire un affichage dynamique
		return "| 1   | "+ (state == -1 ? "sacrified " : (state == 1 ? "validate  " : "          ")) + "| Chance          | Any combination                        | sum of all dice |\n";
	}

}
