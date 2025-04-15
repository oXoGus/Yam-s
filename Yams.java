package fr.uge.yams;

import java.util.Objects;
import java.util.Scanner;

public class Yams {
    public static Game menu(Scanner scanner){
        Objects.requireNonNull(scanner);

        // menu créant soit une partie normale avec un nombre de joueur 
        // défini par le joueur
        // soit une partie en duo contre une IA
        System.out.println("-----------------------------------");
        System.out.println("Welcome in Yam's ! What do you want to do ?");
        System.out.println("1. Start a game alone\n2. Start a game with other players\n3. Start a game against an AI\n");
        System.out.println("Type 1 2 or 3 to select a game");
        var nb = scanner.nextInt();
        while (nb>3 || nb<1) {
            System.out.println("Wrong argument, please try again");
            nb = scanner.nextInt();
        }
        switch (nb) {
            case 1 : return new NormalGame(1);
            case 2 : return multiplePlayers(scanner);
            case 3 : return new DuoIA();
            default : return null;
        }
    }

    public static Game multiplePlayers(Scanner scanner) {
        Objects.requireNonNull(scanner);
        System.out.println("Enter how many players");
        var nb = scanner.nextInt();
        return new NormalGame(nb);
    }
    
    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
		String quit = "no";

        // tant que le joueur ne veut pas quitter après la fin de la partie 
		while (!quit.equals("yes")) {
            
            // création de la partie 
            Game game = menu(scanner);

            // déroulement de la partie
            game.playRounds();

            // fin de la partie avec affichages de résultats
            game.endScreen();


			System.out.println("Do you want to quit the game ? Type 'yes' or 'no', to return to the menu");
			var res = scanner.nextLine();

            // verification que ce soit bien soit yes soit no
			while (!(res.equals("yes")) && !(res.equals("no"))) {
				System.out.println("Wrong answer, please answer yes or no");
				res = scanner.nextLine();
			}
			quit = res;
	}
}
}
