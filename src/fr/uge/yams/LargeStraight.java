package fr.uge.yams;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
	public List<Integer> dicesMissing (Board board) {
		ArrayList<Integer> lst = new ArrayList<Integer>();
		var fiveDiceLst = board.fiveDice();
		for (Integer i = 0; i<6; i++) {
			if (fiveDiceLst.get(i).value()<=5) {
				if (fiveDiceLst.contains(new Dice(fiveDiceLst.get(i).value()+1))) {
					if (!lst.contains(i)){
						lst.add(i);
					}
					var next = fiveDiceLst.indexOf(new Dice(fiveDiceLst.get(i).value()+1));
					if (!lst.contains(next)) {
						lst.add(next);
					}
				}
			}
		}
		return List.copyOf(lst);
	}

	public String toString(String state) {
		Objects.requireNonNull(state);
		
		// on utilise l'état pour faire un affichage dynamique
		return "| L    | "+ state + "| Large Straight  | Five sequential dice                   | 40              |\n";
	}

}
