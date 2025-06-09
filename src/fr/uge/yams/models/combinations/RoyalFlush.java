package fr.uge.yams.models.combinations;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import fr.uge.yams.models.Board;
import fr.uge.yams.models.CardCombination;

public class RoyalFlush implements CardCombination {
   
    @Override
	public int score(Board board) {
		Objects.requireNonNull(board);
		return 250;
	}


	@Override
	public boolean isValid(Board board) {
        

		Objects.requireNonNull(board);

        // il faut que toutes les cartes soient de meme suit
        if (!board.isAllSameSuit()){
            return false;
        }
		
        // occurence() renvoie les éléments sans distinctions des couleurs 
        var occList = board.occurence();


		// une combinaison possible par couleurs
        // A K Q J 10
        if (occList.get(1) > 0 && occList.get(13) > 0 && occList.get(12) > 0 && occList.get(11) > 0 && occList.get(10) > 0) {
            return true;
        }
		return false;
	}

	@Override
	public List<Integer> elementsMissing(Board board) {
		Objects.requireNonNull(board);

		var cardsKept = board.elementsFormingSeq();
		var res = new ArrayList<Integer>();
		
		for (int i = 1; i < 6; i++){
			if (!cardsKept.contains(i)){
				res.add(i);
			}
		}

		return List.copyOf(res);
	}

	@Override
	public double probability (int numMissing) {
		return 0.00000154;
	}

	@Override
	public String howToObtain() {
		return "A K Q J 10, same suit";
	}

	@Override
	public String toString() {
		return "Royal Flush";
	}
}
