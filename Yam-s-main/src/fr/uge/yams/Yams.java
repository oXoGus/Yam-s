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
        var nb = scanner.nextLine();

        // gestions des erreurs 
        // si nb n'est pas un entier commme le test est en premier 
        // et qu'il est suivie d'un OR alors condition du while 
        // donne déja true donc java ne fait meme pas les autres test
        // ce qui permet d'éviter une exception NumberFormatException
        while (!Games.isInteger(nb) || Integer.parseInt(nb) > 3 || Integer.parseInt(nb) < 1) {
            System.out.println("Wrong argument, please try again");
            nb = scanner.nextLine();
        }

        switch (Integer.parseInt(nb)) {
            case 1 : return new NormalGame(1);
            case 2 : return multiplePlayers(scanner);
            
            // default pour le 3ème choix puisque nb ne peux 
            // que valoir 3 si ce n'etait pas 1 ni 2
            default : return new DuoIA(); 
        }
    }

    public static Game multiplePlayers(Scanner scanner) {
        Objects.requireNonNull(scanner);

        System.out.println("Enter how many players");
        var nb = scanner.nextLine();

        // gestions des erreurs
        while (!Games.isInteger(nb) || Integer.parseInt(nb) < 1) {
            System.out.println("Enter how many players");
            nb = scanner.nextLine();
        }
        
        return new NormalGame(Integer.parseInt(nb));
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
