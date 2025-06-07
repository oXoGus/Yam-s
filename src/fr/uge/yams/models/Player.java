package fr.uge.yams.models;

import java.util.Collection;
import java.util.List;
import java.util.Objects;


import fr.uge.yams.Board;
import fr.uge.yams.User;
import fr.uge.yams.combinations.Combination;
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
	
	/*
	public String[] getDice () {

		System.out.println(username + "> Do you want to reroll a dice? Type 0 for no, 1-5 to reroll this dice.\n");
		var choices = scanner.nextLine().split(" ");
		

		while (!Games.isAllInt(choices) || !Games.userDiceCheck(choices)) {
			System.out.println(username + "> Do you want to reroll a dice? Type 0 for no, 1-5 to reroll this dice.");
			choices = scanner.nextLine().split(" ");
		}
		return choices;
	}

	
	// les donnés pour l'affichage du tableau des scores
	

	public Set<Integer> askReroll() {

		// repose la question si le user rentre pas un int ou un nb > 5
		// demander LES dés, les dés seront séparer par un espace 
		var choices = getDice();

		// on utilise un set pour que le joueur ne puisse pas relancer deux fois le meme dé
		var lstChoice = new HashSet<Integer>();
		for (var nb : choices) {
			lstChoice.add(Integer.valueOf(nb));
		}
		// renvoyer un set non modifiable 
		return Set.copyOf(lstChoice);
	}

	public String askCombination() {
		System.out.println(scoreSheet);
		System.out.println(username + "> Please choose a combination to score in your score sheet by entering its first letter. ");
		var choice = scanner.nextLine();

		// tant que la combinaison n'est pas valide
		while (scoreSheet.parseCombination(choice) == null) {
			System.out.println(username + "> Please choose a combination to score in your score sheet by entering its first letter.");
			choice = scanner.nextLine();
		}
		return choice;
	}

	public void reroll () {
		for (var updateCounter = 0; updateCounter < 3; updateCounter++) {

			//Demande quels dés relancer ou non
			var choice = askReroll();
			
			//si la liste de choix, il y a un zéro, on s'arrête là
			if (choice.contains(0)) {
				break;
			}

			//on reroll les dés choisis 
			board.reroll(choice);

			//affichage de tout les dés
			System.out.println(board);
		}

	}

	public void choice() {

		// pas de combinaison possible pour le board
		if (!scoreSheet.isCombinaisonPossible(board)) {

			// on sacrifie un combinaison 
			// qui n'est pas déja sacrifié 
			// ni validé
			System.out.println(username + "> No combination possible, you should sacrifice one.");
			var pattern = scoreSheet.parseCombination(askCombination());

			// tant que la combinaison choisi a déja été sacrifié ou validé
			while (scoreSheet.isSacrified(pattern) || scoreSheet.isValidate(pattern)){
				System.out.println(username + "> this combination has already be sacrified, choose a other one");
				pattern = scoreSheet.parseCombination(askCombination());
			}
			scoreSheet.sacrifyCombination(pattern, board);
		}
		else {
			var pattern = scoreSheet.parseCombination(askCombination());

			// tant que la combinaison choisi a déja été validé et que la combinaison est valide pour le board
			while (scoreSheet.isValidate(pattern) || !pattern.isValid(board)){
				System.out.println(username + "> this combination has already be validated or isn't valid with this board");
				pattern = scoreSheet.parseCombination(askCombination());
			}
			scoreSheet.addCombination(pattern, board);
		}
		System.out.println(scoreSheet);
	}
	
	*/

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
		return scoreSheet.combinaitionsInfo();
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
		return board.gameElementShapes(pos);
	}

	@Override
	public String testCombination(Combination combination){
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
		
		// si on ne peut pas ajouter cette combinasion
		var errorCode = testCombination(combination);
		if (errorCode != null){
			return errorCode;
		}

		if (!combination.isValid(board)){
			return "this combination is note valid with this board";
		}

		// sinon on ajoute la combi
		scoreSheet.addCombination(combination, board);

		return null;
	}

	@Override
	public boolean isCombinationPossible(){
		return scoreSheet.isCombinaisonPossible(board);
	}


}
