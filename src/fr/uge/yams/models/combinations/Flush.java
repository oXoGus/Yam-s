package fr.uge.yams.models.combinations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import fr.uge.yams.models.Board;
import fr.uge.yams.models.CardCombination;
import fr.uge.yams.models.Suit;

public record Flush() implements CardCombination {

	@Override
	public int score(Board board) {
		Objects.requireNonNull(board);
		return 60;
	}

	@Override
	public boolean isValid(Board board) {
		Objects.requireNonNull(board);
		
		// si toutes les cartes sont de la même couleur
		return board.isAllSameSuit();
	}

	

	@Override
	public List<Integer> elementsMissing(Board board){
		Objects.requireNonNull(board);
				
		// on prend fait un HashMap avec pour clé les Suit et pour valeur la liste des pos qui on ces suit
		HashMap<Suit, ArrayList<Integer>> suitPos = new HashMap<>();
		for (int i = 0; i < board.getElementsList().size(); i++){
			var card = board.getElementsList().get(i);
			if (suitPos.putIfAbsent(card.suit(), new ArrayList<>(List.of(i + 1))) != null){
				suitPos.get(card.suit()).add(i + 1);
			}
		}

		// on récup le suit qui a le plus de cartes
		Suit suitKepped = null;
		for (var entry : suitPos.entrySet()){
			if (entry.getValue().size() > suitPos.getOrDefault(suitKepped, new ArrayList<>()).size()){
				suitKepped = entry.getKey();
			}
		}

		var elementsMissing = new ArrayList<Integer>();
		for (var entry : suitPos.entrySet()){
			if (!entry.getKey().equals(suitKepped)){
				elementsMissing.addAll(entry.getValue());
			}
		}

		return List.copyOf(elementsMissing);

	}

	@Override
	public String toString(){
		return "Flush";
	}

	@Override
	public String howToObtain() {
		return "Five cards, same suit, any order";
	}
}
