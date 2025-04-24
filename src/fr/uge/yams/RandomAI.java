package fr.uge.yams;

import java.util.ArrayList;
import java.util.Random;

public class RandomAI implements AI{
    private final Board board;
    private final ScoreSheet scoreSheet;
    private final String username;

    public RandomAI(int numAI){
        board=new Board();
        scoreSheet=new ScoreSheet();
        username = "Random AI #" + numAI;
    }

   
    @Override
    //Prend un nombre au hasard entre 0 et 3 inclus et relance ce nombre de fois les dés
    public void reroll() {
        int nbRounds = new Random().nextInt(4);
        
        // combi visé random
        var freeComb = new ArrayList<Combination>(freeCombinations(scoreSheet, board).keySet()); 
        var goalComb = freeComb.get(new Random().nextInt(freeComb.size()));
        System.out.println("Goal Combination : " + goalComb);
        
        // on essaye de valider cette combi
        if (goalComb.isValid(board)){
            // on valide la combi
            scoreSheet.addCombination(goalComb, board);
            return;
        }

        for (int i = 0; i<nbRounds; i++) {
            
            // si elle n'est pas valide
            var dicesToReroll = goalComb.dicesMissing(board);
            printAIActions(dicesToReroll);
            
            board.reroll(dicesToReroll);
            System.out.println(board);

            // on essaye de valider cette combi
            if (goalComb.isValid(board)){
                // on valide la combi
                scoreSheet.addCombination(goalComb, board);
                return;
            }
        }
        // si on a pas reussit a la valider
        // on sacrifie une comb aléatoire
        scoreSheet.sacrifyCombination(freeComb.get(new Random().nextInt(freeComb.size())), board);
    }

    @Override
    public void playRound() {
        //On relance les dés et on les affiche
        board.rerollAllDice();
        System.out.println(board);

        //On reroll pour potentiellement relancer les dés
        reroll();
        System.out.println(scoreSheet);
    }
    
    
    //Retourne la longueur du score
    public int lenScore(){
		return Integer.toString(scoreSheet.scoreTotal()).length();
	}

    //Retourne le score pour le rang dans duoAI
    @Override
    public int score() {
        return scoreSheet.scoreTotal();
    }

    @Override
    public int lenUserName() {
        return username.length();
    }

    @Override
    public String username() {
        return username;
    }
}
