package fr.uge.yams.solo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class ScoreSheet {

	private final HashMap<Combination, Integer> validateCombinations = new HashMap<>();

	// pas besoin de stocker le score 
	private final ArrayList<Combination> sacrifiedCombination = new ArrayList<>();

	// fonction pour verifier si le patterne et le board sont valide
	// pour éviter la répétition de code dans addCombination et sacrifyCombination 
	public void verification(Combination pattern, Board board){
		Objects.requireNonNull(pattern);
		Objects.requireNonNull(board);

		// gestion des erreurs
		if (validateCombinations.containsKey(pattern)) {
			throw new IllegalArgumentException("already a score for this combination");
		}
		if (sacrifiedCombination.contains(pattern)){
			throw new IllegalArgumentException("this combiantion is already sacrified");
		}
		if (!pattern.isValid(board)){
			throw new IllegalArgumentException("this combination is not valid with this board");
		}
	}

	public void addCombination(Combination pattern, Board board) {
		verification(pattern, board);

		validateCombinations.put(pattern, pattern.score(board));
	}

	public void sacrifyCombiation(Combination pattern, Board board){
		verification(pattern, board);
		
		sacrifiedCombination.add(pattern);
	}

	public boolean isSacrified(Combination pattern){
		Objects.requireNonNull(pattern);
		return sacrifiedCombination.contains(pattern);
	}

	public boolean isValidate(Combination pattern){
		Objects.requireNonNull(pattern);
		return validateCombinations.containsKey(pattern);
	}

	// pour l'affichage dynamique du toString()
	public int state(Combination pattern){
		Objects.requireNonNull(pattern);
		if (isValidate(pattern)){
			return 1;
		}
		if (isSacrified(pattern)){
			return -1;
		}
		return 0;
	}

	public int scoreTotal() {
		return validateCombinations.values().stream().mapToInt(Integer::intValue).sum();
	}


	// refaire le toString sous forme de tableau
	@Override
	public String toString() {
		String s;
		String sep = "+-----+-----------+-----------------+----------------------------------------+-----------------+\n";
		
		
		// titres des colonnes
		s = sep;
		s += "| num | state     | name            | descripton                             | score           |\n";
		s += sep;
		s += new Chance().toString(state(new Chance()));
		s += new ThreeOfAKind().toString(state(new ThreeOfAKind()));
		s += new FourOfAKind().toString(state(new FourOfAKind()));
		s += new FullHouse().toString(state(new FullHouse()));
		s += new SmallStraight().toString(state(new SmallStraight()));
		s += new LargeStraight().toString(state(new LargeStraight()));
		s += new Yahtzee().toString(state(new Yahtzee()));
		s += sep;
		return s;

	}


	public static void main(String[] args) {
		var scoreSheet = new ScoreSheet();
		var board = new Board(List.of(1, 2, 3, 4, 5));
		scoreSheet.addCombination(new LargeStraight(), board);
		
		scoreSheet.addCombination(new SmallStraight(), board);
		System.out.println(scoreSheet);
		System.out.println(scoreSheet.scoreTotal());
	}

}
