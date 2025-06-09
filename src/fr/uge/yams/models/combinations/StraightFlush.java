package fr.uge.yams.models.combinations;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import fr.uge.yams.models.Board;
import fr.uge.yams.models.CardCombination;
import fr.uge.yams.models.Games;

public class StraightFlush implements CardCombination {
   
    @Override
	public int score(Board board) {
		Objects.requireNonNull(board);
		return 90;
	}


	@Override
	public boolean isValid(Board board) {
		Objects.requireNonNull(board);

		// n'importe quel séquence de 5 cartes de la meme couleur 
		return board.isAllSameSuit() && board.elementsFormingSeq().size() == 5;
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
		if (numMissing < 0 || numMissing > 6){
			throw new IllegalArgumentException();
		}

		if (numMissing == 0){
			return 1;
		}

		return Games.fact(numMissing)/13.0;
	}

	@Override
	public String howToObtain() {
		return "Five consecutive cards, same suit";
	}

	@Override
	public String toString() {
		return "Straight Flush";
	}
}
