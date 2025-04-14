package fr.uge.yams;

import java.util.Objects;
import java.util.Scanner;

public class Yams {
    public static Game menu(Scanner scanner){
        Objects.requireNonNull(scanner);

        // menu créant soit une partie normale avec un nombre de joueur 
        // défini par le joueur
        // soit une partie en duo contre une IA
        return new NormalGame(2);
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
