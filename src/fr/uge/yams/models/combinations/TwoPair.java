package fr.uge.yams.models.combinations;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import fr.uge.yams.models.Board;
import fr.uge.yams.models.CardCombination;

public record TwoPair() implements CardCombination {

	@Override
	public int score(Board board) {
		Objects.requireNonNull(board);

		return 30;
	}
	
    @Override
	public boolean isValid(Board board) {
		Objects.requireNonNull(board);
		
        var occ = board.occurence();
        
        int twoOccCount = 0;
        for (var rankOcc : occ){
            if (rankOcc == 2){
                twoOccCount++;
            }
        }
        return twoOccCount == 2;
	}

	@Override
	public List<Integer> elementsMissing(Board board) {
		Objects.requireNonNull(board);

		var elementsToReroll = new ArrayList<Integer>();

		var elements = board.getElementsList();
		
		// si y'a déja une pair on la reroll pas sinon on reroll tout
		if (board.occurence().contains(2)){
			var rankPair = board.occurence().indexOf(2);

			for (int i = 0; i < elements.size() ; i++){
				if (elements.get(i).rank() != rankPair){
					elementsToReroll.add(i + 1);
				}
			}
			return List.copyOf(elementsToReroll);
		} 
		// sinon
		for (int pos = 1; pos - 1 < elements.size(); pos++){
			elementsToReroll.add(pos);
		}
		return List.copyOf(elementsToReroll);
	}


	@Override
	public String toString(){
		return "two Parir";
	}

	@Override
	public String howToObtain() {
		return "Two different pairs";
	}


}