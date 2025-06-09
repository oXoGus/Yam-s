package fr.uge.yams.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class ScoreSheet {

	// la listes des combinaisons pour cette scoreSheet
	private final List<Combination> combinations;

	private final HashMap<Combination, Integer> validateDiceCombinations = new HashMap<>();

	// pas besoin de stocker le score 
	private final ArrayList<Combination> sacrifiedDiceCombination = new ArrayList<>();

	// on utilise la surcharge pour etre sur que la liste des combinaison 
	// soit entièrement de DiceCombination 
	// ou de CardCombination
	public ScoreSheet(List<Combination> combinations){
		Objects.requireNonNull(combinations);

		if (combinations.isEmpty()){
			throw new IllegalArgumentException("You must have at least one combination in your ScoreSheet");
		}
		this.combinations = List.copyOf(combinations);
	}


	public void requirePatternInDiceCombinations(Combination pattern){
		Objects.requireNonNull(pattern);
		
		// on force que les combinaison passé en param fasse partie de la liste des combinaisons 
		if (!combinations.contains(pattern)){
			throw new IllegalArgumentException("this combinaison does not correspond to the combinaisons in this ScoreSheet");
		}
	}

	// fonction pour verifier si le patterne et le board sont valide
	// pour éviter la répétition de code dans addDiceCombination et sacrifyDiceCombination 
	public void verification(Combination pattern, Board board){
		Objects.requireNonNull(pattern);
		Objects.requireNonNull(board);

		// gestion des erreurs
		if (validateDiceCombinations.containsKey(pattern) && combinations.contains(pattern)) {
			throw new IllegalArgumentException("already a score for this combination");
		}
		if (sacrifiedDiceCombination.contains(pattern) && combinations.contains(pattern)){
			throw new IllegalArgumentException("this combiantion is already sacrified");
		}
	}

	public void addCombination(Combination pattern, Board board) {
		requirePatternInDiceCombinations(pattern);
		verification(pattern, board);
		if (!pattern.isValid(board)){
			throw new IllegalArgumentException("this combination is not valid with this board");
		}

		validateDiceCombinations.put(pattern, pattern.score(board));
	}


	// pour rendre l'affichage plus simple par la vue
	public List<CombinationInfo> combinaitionsInfo(Board board){
		Objects.requireNonNull(board);
		var res = new ArrayList<CombinationInfo>();

		// pour chaque combi 
		for (var comb : combinations){
			res.add(toCombinaitionInfo(comb, board));
		}

		return List.copyOf(res);
	}

	public CombinationInfo toCombinaitionInfo(Combination comb, Board board){
		Objects.requireNonNull(comb);
		Objects.requireNonNull(board);
		
		// dans quel état se trouve la combi
		String state = isCombinaisonFree(comb) ? "not chosen yet" : (isValidate(comb) ? "validated" : "sacrified"); 
		return new CombinationInfo(comb, state, comb.howToObtain(), validateDiceCombinations.getOrDefault(comb, 0), comb.scoreInfo(board));
	}

	public void sacrifyCombination(Combination pattern, Board board){
		verification(pattern, board);
		requirePatternInDiceCombinations(pattern);

		sacrifiedDiceCombination.add(pattern);
	}

	public boolean isSacrified(Combination pattern){
		Objects.requireNonNull(pattern);
		requirePatternInDiceCombinations(pattern);

		return sacrifiedDiceCombination.contains(pattern);
	}

	public boolean isValidate(Combination pattern){
		Objects.requireNonNull(pattern);
		requirePatternInDiceCombinations(pattern);

		return validateDiceCombinations.containsKey(pattern);
	}

	public boolean isCombinaisonPossible (Board board) {
		Objects.requireNonNull(board);

		// toutes les combi
		for (var combination : combinations){
			if (combination.isValid(board) && !(isValidate(combination)) && !(isSacrified(combination)) ) {
				return true;
			}
		}
		return false;
	}

	// revoie toutes les combinaisons qui peuvent etre validé
	public List<Combination> combinaisonPossible (Board board) {
		Objects.requireNonNull(board);

		var res = new ArrayList<Combination>();
		for (var combination : combinations){
			if (combination.isValid(board) && !(isValidate(combination)) && !(isSacrified(combination)) ) {
				res.add(combination);
			}
		}
		return List.copyOf(res);
	}

	public boolean isCombinaisonFree(Combination comb){
		Objects.requireNonNull(comb);
		requirePatternInDiceCombinations(comb);

		return !isValidate(comb) && !isSacrified(comb);
	}

	public void clear(){
		// vide toute la scoreSheet pour recommencer une partie 
		validateDiceCombinations.clear();
		sacrifiedDiceCombination.clear();
	}

	public int scoreTotal() {
		return validateDiceCombinations.values().stream().mapToInt(Integer::intValue).sum();
	}

	public List<Combination> combinationChosen(){
		return combinations;
	}
}
