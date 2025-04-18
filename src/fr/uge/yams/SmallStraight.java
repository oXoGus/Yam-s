package fr.uge.yams;

import java.util.Objects;

public record SmallStraight() implements Combination {

	@Override
	public int score(Board board) {
		Objects.requireNonNull(board);
		return 30;
	}

	@Override
	public boolean isValid(Board board) {
		Objects.requireNonNull(board);
		// 4 dés conséctif
		
		// on test les 3 combinaisons possibles
		// 1, 2, 3, 4
		// 2, 3, 4, 5
		// 3, 4, 5, 6
		var occList = board.occurence();

		if (occList.get(1) > 0 && occList.get(2) > 0 && occList.get(3) > 0 && occList.get(4) > 0){
			return true;
		}
		if (occList.get(2) > 0 && occList.get(3) > 0 && occList.get(4) > 0 && occList.get(5) > 0){
			return true;
		}
		if (occList.get(3) > 0 && occList.get(4) > 0 && occList.get(5) > 0 && occList.get(6) > 0){
			return true;
		}
		return false;
	}

	
	public String toString(String state) {
		Objects.requireNonNull(state);
		
		// on utilise l'état pour faire un affichage dynamique
		return "| S    | "+ state + "| Small Straight  | Four sequential dice                   | 30              |\n";
	}

}
