package fr.uge.yams.combinations;

import java.util.Objects;

import fr.uge.yams.Board;

public record TwoPair() implements Combination {
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
	public String code(){
		// code pour parse les combinisons dans le terminal
		return "T";
	}

	public String toString(String state, String score) {
		Objects.requireNonNull(state);
		Objects.requireNonNull(score);
		
		// on utilise l'état pour faire un affichage dynamique
		return "| T    | "+ state + "| Three of a Kind | At least three dice the same           | " + score + " |\n";
	}

	@Override
	public String toString(){
		return "Three of a kind";
	}
}