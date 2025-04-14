package fr.uge.yams;

import java.util.ArrayList;
import java.util.Scanner;

public class Yams {

	public static String init(Scanner scanner) {

		System.out.println("Welcome, player, please enter your name.");
		return scanner.nextLine();
	} 

	public static boolean isInteger (String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	private static boolean isAllInt (String[] choices) {
		for (var nb : choices) {
			if (!isInteger(nb)) {
				return false;
			}
		}
		return true;
	}

	public static String[] getUserDice (Scanner scanner) {
		System.out.println("Do you want to reroll a die? Type 0 for no, 1-5 to reroll this dice.");
		var choices = scanner.nextLine().split(" ");

		while (!isAllInt(choices) || !userDiceCheck(choices)) {
			System.out.println("Do you want to reroll a die? Type 0 for no, 1-5 to reroll this dice.");
			choices = scanner.nextLine().split(" ");
		}
		return choices;
	}

	public static boolean userDiceCheck (String[] choices) {
		for (var nb : choices) {
			if (Integer.valueOf(nb)>5 || Integer.valueOf(nb)<0) {
				return false;
			}
		}
		return true;
	}

	private static ArrayList<Integer> askReroll(Scanner scanner) {

		// repose la question si le user rentre pas un int ou un nb > 5
		// demander LES dés, les dés seront séparer par un espace 
		var choices = getUserDice(scanner);

		var lstChoice = new ArrayList<Integer>();
		for (var nb : choices) {
			lstChoice.add(Integer.valueOf(nb));
		}
		// renvoyer une liste non modifiable 
		return lstChoice;
	}

	private static String askCombination(Scanner scanner) {

		System.out.println("Please choose a combination to score in your score sheet by entering its first letter. Please ");
		var choice = scanner.nextLine();

		// tant que la combinaison n'est pas valide
		while (parseCombination(choice) == null) {
			System.out.println("Please choose a combination to score in your score sheet by entering its first letter. Please ");
			choice = scanner.nextLine();
		}
		return choice;
	}

	private static void reroll (Scanner scanner, Board board) {
		for (var updateCounter = 0; updateCounter < 3; updateCounter++) {

			//Demande quels dés relancer ou non
			var choice = askReroll(scanner);
			//si la liste de choix, il y a un zéro, on s'arrête là
			if (choice.contains(0)) {
				break;
			}
			//on reroll les dés choisis 
			board.reroll(choice);
			//On retourne les dés
			System.out.println(board);
		}

	}

	private static void choice(ScoreSheet scoreSheet, Board board, Scanner scanner) {
		if (!scoreSheet.isCombinaisonLeft(board)) {
			System.out.println("No combination possible, you should sacrifice one.");
			var pattern = parseCombination(askCombination(scanner));

			// tant que la combinaison choisi a déja été sacrifié ou validé
			while (scoreSheet.isSacrified(pattern) || scoreSheet.isValidate(pattern)){
				System.out.println("this combination has already be secrified");
				pattern = parseCombination(askCombination(scanner));
			}
			scoreSheet.addCombination(pattern, board);
		}
		else {
			var pattern = parseCombination(askCombination(scanner));

			// tant que la combinaison choisi a déja été validé et que la combinaison est valide pour le board
			while (scoreSheet.isValidate(pattern) || !pattern.isValid(board)){
				System.out.println("this combination has already be validated or isn't valid with this board");
				pattern = parseCombination(askCombination(scanner));
			}
			scoreSheet.addCombination(pattern, board);
		}
		System.out.println(scoreSheet);
	}
	private static Combination parseCombination(String combinationName) {

		// ne pas lancer une exception mais reposer la question si default
		return switch (combinationName) {
		case "T" -> new ThreeOfAKind();
		case "Fu" -> new FullHouse();
		case "Fo" -> new FourOfAKind();
		case "L" -> new LargeStraight();
		case "S" -> new SmallStraight();
		case "C" -> new Chance();
		case "Y" -> new Yahtzee();
		default -> null; // null par défaut pour la gestion des erreurs 
		};
	}

	public static void main(String[] args) {
		var game = "yes";

		var scanner = new Scanner(System.in);
		var name = init(scanner);

		while (game.equals("yes")) {
			System.out.println("Hello " + name + ", and good luck !\n");

			var scoreSheet = new ScoreSheet();

			// afichages des combinaisons possible pour que le joueur sache quelles code mettre pour quelles combinaison
			System.out.println(scoreSheet);

			// roundCounter jusqu'a 7
			// Début du tour du joueur
			for (var roundCounter = 0; roundCounter < 7; roundCounter++) {
				System.out.println("Welcome in round " + (roundCounter + 1));

				// 1er lancer de dés 
				var board = new Board();

				System.out.println(board);

				reroll(scanner, board);

				// si il y a un combianisons possibles que le joueur n'a pas déja fait 

				choice(scoreSheet, board, scanner);
				
			}

			// todo stoker le meuilleur score dans un ficher.txt
			System.out.println("C'est fini ! \n");
			System.out.println("Your final score is : " + scoreSheet.scoreTotal() + " pts !\n");
			System.out.println("Do you want to play another game ? Type yes or no");
			var nb = scanner.nextLine();
			while (!(nb.equals("yes")) && !(nb.equals("no"))) {
				System.out.println("Wrong answer, please answer yes or no");
				nb = scanner.nextLine();
			}
			game = nb;
	}
}

}
