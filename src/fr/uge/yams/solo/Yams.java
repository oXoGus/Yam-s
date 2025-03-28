package fr.uge.yams.solo;

import java.util.Scanner;

public class Yams {

	public static String init(Scanner scanner) {

		System.out.println("Welcome, player, please enter your name.");
		return scanner.nextLine();

	}

	private static int askReroll(Scanner scanner) {

		// repose la queston si le user rentre pas un int ou un nb > 5
		// demander LES dés, les dés seront séparer par un espace 
		System.out.println("Do you want to reroll a die? Type 0 for no, 1-5 to reroll this die.");
		var choice = scanner.nextLine();

		// renvoyer une liste non modifiable 
		return Integer.parseInt(choice);
	}

	private static String askCombination(Scanner scanner) {

		// indiquer les combinaisons qu'on peut selectionner avec le  avec leur points associé
		System.out.println("Please choose a combination to score in your score sheet by entering its first letter.");
		var choice = scanner.nextLine();
		return choice;
	}

	private static Combination parseCombination(String combinationName) {

		// implémanter les autres combinaison 
		// ne pas lancer une exeption mais reposer la question si default
		return switch (combinationName) {
		case "T" -> new ThreeOfAKind();
		case "F" -> new FullHouse();
		default -> throw new IllegalArgumentException("Unexpected value: " + combinationName);

		// si la verification échou 
		// on redemande d'entrez la combianison 
		};
	}

	public static void main(String[] args) {

		// mettre scanner en var global
		var scanner = new Scanner(System.in);
		var name = init(scanner);
		System.out.println("Hello " + name + ", and good luck !\n");

		var scoreSheet = new ScoreSheet();

		// todo fonction qui affiche un tableau d'aide contenant les nom des combi 
		// avec leur conditions et leurs poitns associée


		// roundCOunter jusqu'a 7
		// Début du tour du joueur
		for (var roundCounter = 0; roundCounter < 2; roundCounter++) {
			System.out.println("Welcome in round " + (roundCounter + 1));

			// 1er lancer de dés 
			// mettre le borad en var global 
			var board = new Board();

			System.out.println(board);

			// 2 relance restantes 
			// choisir les dés a relancer 

			// Relances dans le tour
			for (var updateCounter = 0; updateCounter < 3; updateCounter++) {

				// regrouper tout ça dans une fonction reroll
				var choice = askReroll(scanner);
				if (choice > 0) {
					board.reroll(choice);
					System.out.println(board);
				} else {
					break;
				}
			}

			// si il y a un combianisons possibles que le joueur n'a pas déja fait 


			// rassembler la demande dans une fonction static getCombination
			var combinationChoice = parseCombination(askCombination(scanner));
			scoreSheet.updateScore(combinationChoice, board);
			System.out.println(scoreSheet);
		}

		// todo stoker le meuilleur score dans un ficher.txt
		System.out.println("C'est fini !");
	}

}
