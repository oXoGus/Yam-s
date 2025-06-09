package fr.uge.yams.models;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import javafx.scene.Node;


public class Player implements User {
	private final ScoreSheet scoreSheet;
	private final Board board;
	private final String username;


	// passe les éléments de jeu en paramètre pour qu'il puisse dépendre du mode de jeu
	public Player(String username, List<Combination> combinaitionChosen, Board boardType) {
		Objects.requireNonNull(combinaitionChosen);
		Objects.requireNonNull(boardType);
		Objects.requireNonNull(username);

		// initialisation de tout les champs
		scoreSheet = new ScoreSheet(combinaitionChosen);
		board = boardType;
		this.username = username;
	}

	@Override
	public UserScore score(){
		return new UserScore(username, scoreSheet.scoreTotal());
	}
	
	
	@Override
	public void rerollAll(){
		board.rerollAll();
	}

	@Override
	public List<Node> boardShapes(){
		return board.allGameElementShapes();
	}

	// pour que la vue puisse afficher la scoreSheet 
	@Override
	public List<CombinationInfo> scoreSheet(){
		return scoreSheet.combinaitionsInfo(board);
	}
	
	@Override
	public String username(){
		return username;
	}

	@Override
    public boolean isWithCards(){
        return board.isBoardCard();
    }

	@Override
	public void reroll(Collection<Integer> positions){
		board.reroll(positions);
	}
	 
	@Override
	public List<Node> boardShapes(Collection<Integer> pos){
		Objects.requireNonNull(pos);

		return board.gameElementShapes(pos);
	}

	@Override
	public String testCombination(Combination combination){
		Objects.requireNonNull(combination);

		// on fait tout les tests pour ajouter une combinaison
		// si on ne peut pas l'ajouter on renvoie la cause sous forme de String 
		if (scoreSheet.isSacrified(combination)){
			return "this combination has already been sacrified";
		}

		if (scoreSheet.isValidate(combination)){
			return "this combination has already been validated";
		}
		
		// si aucun problème pour l'ajouter ou la supprimer
		return null;
	}

	@Override
	public String trySacrifyCombination(Combination combination){
		Objects.requireNonNull(combination);
		
		// si on ne peut pas sacrifier cette combinasion
		var errorCode = testCombination(combination);
		if (errorCode != null){
			return errorCode;
		}

		// sinon on sacrifie la combi
		scoreSheet.sacrifyCombination(combination, board);

		return null;
	}

	@Override
	public String tryAddCombination(Combination combination){
		Objects.requireNonNull(combination);
		
		// si on ne peut pas ajouter cette combinasion
		var errorCode = testCombination(combination);
		if (errorCode != null){
			return errorCode;
		}

		if (!combination.isValid(board)){
			return "this combination is not valid with this board";
		}

		// sinon on ajoute la combi
		scoreSheet.addCombination(combination, board);

		return null;
	}

	@Override
	public boolean isCombinationPossible(){
		return scoreSheet.isCombinaisonPossible(board);
	}

	@Override
	public void reset() {
		// on réinitialise juste la scoreSheet
		scoreSheet.clear();
	}

	@Override
	public boolean isAI() {
		return false;
	}
}
