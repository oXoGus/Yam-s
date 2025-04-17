package fr.uge.yams;

import java.util.Scanner;

public class DuoIA implements Game {
    private final Player player ;
    private final AI ai ;
    private final Scanner scanner;

    //Inititialise une partie entre une IA et un joueur
    public DuoIA(){
        System.out.println("Welcome ! Please enter your name : ");
        player=new Player();
        scanner = new Scanner(System.in);
        ai = chooseAI();
    }

    public AI chooseAI() { 
        System.out.println("Please choose your AI by typing its number : \n1. Random \n2. Playing Safe \n3. Playing Risky");
        String res=scanner.nextLine();
        
        // gesitons des erreurs
        while (!Games.isInteger(res) || (Integer.parseInt(res) != 1 && Integer.parseInt(res) !=2 && Integer.parseInt(res) !=3)) {
            System.out.println("Please enter a correct answer");
            res=scanner.nextLine();
        }
        var type = Integer.parseInt(res);
        
        switch (type) {
            case 1 : return new RandomAI();
            case 2 : return new SafeAI();
            default : return new RiskyAI();
        }
    }
    
    //Gère les tours entre l'IA et le joueur
    @Override
    public void playRounds(){
        for (int i = 0; i<7; i++) {
            for (int j=0; j<2; j++) {
                if (j==0) {
                    player.playRound();
                } 
                else {
                    ai.playRound();
                }
            }
        }
    }

    //Gère les résultats 
    @Override 
    public void endScreen(){
        System.out.println("End of the game !\n");
        System.out.println("Here are the results :\n");
    }
}