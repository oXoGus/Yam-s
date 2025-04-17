package fr.uge.yams;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Player implements Comparable<Player> {
	private final Scanner scanner;
	private final String userName;
	private final ScoreSheet scoreSheet;
	private final Board board;

	public Player() {

		// initialisation de tout les champs
		scanner = new Scanner(System.in);
		userName = init();
		scoreSheet = new ScoreSheet();
		board = new Board();
	}
	

	public String init() {
		return scanner.nextLine();
	} 


	public String[] getDice () {

		System.out.println(userName + "> Do you want to reroll a dice? Type 0 for no, 1-5 to reroll this dice.\n");
		var choices = scanner.nextLine().split(" ");
		

		while (!Games.isAllInt(choices) || !Games.userDiceCheck(choices)) {
			System.out.println(userName + "> Do you want to reroll a dice? Type 0 for no, 1-5 to reroll this dice.");
			choices = scanner.nextLine().split(" ");
		}
		return choices;
	}

	

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
		System.out.println(userName + "> Please choose a combination to score in your score sheet by entering its first letter. ");
		var choice = scanner.nextLine();

		// tant que la combinaison n'est pas valide
		while (Games.parseCombination(choice) == null) {
			System.out.println(userName + "> Please choose a combination to score in your score sheet by entering its first letter.");
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
			System.out.println(userName + "> No combination possible, you should sacrifice one.");
			var pattern = Games.parseCombination(askCombination());

			// tant que la combinaison choisi a déja été sacrifié ou validé
			while (scoreSheet.isSacrified(pattern) || scoreSheet.isValidate(pattern)){
				System.out.println(userName + "> this combination has already be sacrified, choose a other one");
				pattern = Games.parseCombination(askCombination());
			}
			scoreSheet.sacrifyCombination(pattern, board);
		}
		else {
			var pattern = Games.parseCombination(askCombination());

			// tant que la combinaison choisi a déja été validé et que la combinaison est valide pour le board
			while (scoreSheet.isValidate(pattern) || !pattern.isValid(board)){
				System.out.println(userName + "> this combination has already be validated or isn't valid with this board");
				pattern = Games.parseCombination(askCombination());
			}
			scoreSheet.addCombination(pattern, board);
		}
		System.out.println(scoreSheet);
	}

	public void playRound(){
		// on lance tout les dés 
		board.rerollAllDice();
		System.out.println("\n\n" + userName + "'s round :");
		System.out.println(board);

		// le joueur peut relancer jusqu'a 3 fois le nombre de dés qu'il veux
		reroll();

		// le joueur doit choisir la combinaison 
		// a validé ou sacrifier
		choice();
	}

	// pour trier les joueurs du plus grand score au plus petit
	@Override
	public int compareTo(Player o){
		return o.scoreSheet.scoreTotal() - scoreSheet.scoreTotal();
	}

	// toujour pour l'affichage de fin
	public int lenUserName(){
		return userName.length();
	}

	public int lenScore(){
		return Integer.toString(scoreSheet.scoreTotal()).length();
	}

	public String result(int playerRanking, int lenMaxPlayerRanking, int lenMaxUserName, int lenMaxScore){
		// affiche sous forme d'une ligne d'un tableau le placement, le nom et le score du joueur
		// meme mise en forme que le toString du ScoreSheet
		// calcule du nombre d'espace apres chaque données
		// pour avoir la meme taille de colonne
		
		String res =  "| " + playerRanking;
		int lenPlayerRanking = Integer.toString(playerRanking).length();
		
		// nombre d'espace manquant pour que ce soit aligné 
		for (int i = 0; i < lenMaxPlayerRanking - lenPlayerRanking; i++){
			res += " ";
		}
		res += " | " + userName;
		
		for (int i = 0; i < lenMaxUserName - userName.length(); i++){
			res += " ";
		}
		res += " | " + scoreSheet.scoreTotal();

		for (int i = 0; i < lenMaxScore - lenScore(); i++){
			res += " ";
		}
		res += " |\n";

		return res;
	}

	

}
