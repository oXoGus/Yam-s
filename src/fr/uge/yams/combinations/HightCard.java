package fr.uge.yams.combinations;

import java.util.Objects;

import fr.uge.yams.Board;

public record HightCard() implements Combination {
    
	@Override
	public int score(Board board) {
		Objects.requireNonNull(board);
		int bestRank = 0;

		// on revoie le rank de la carte la plus haute
		for (var rank : board.occurence()){
			if (rank > 1){
				bestRank = rank;
			}
		}
		return bestRank;
	}
	
	@Override
	public boolean isValid(Board board) {
		return true;
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