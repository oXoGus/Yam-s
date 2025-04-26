package fr.uge.yams;

import java.util.Objects;

public class RiskyAI implements AI{
    private final Board board;
    private final ScoreSheet scoreSheet;
    private final String username;

    public RiskyAI(int numAI){
        board=new Board();
        scoreSheet=new ScoreSheet();
        username = "Risky AI #" + numAI;
    }
    
    @Override
    public void reroll() {
        
        //On reroll tant qu'on a pas la combinaison libre qui a le plus de points, sous un maximum de 3 reroll
        var goalComb = goalCombination(scoreSheet, board);
        System.out.println("Goal Combination : " + goalComb);
        

        // on recherche la combi que l'on va sacrifier 
        // celle qui aura le moins de chance d'etre validé
        var combinationToSacrify = sacrifyCombination(scoreSheet, board);

        // on essaye de valider cette combi
        if (goalComb.isValid(board)){

            // on valide la combi
            scoreSheet.addCombination(goalComb, board);
            return;
        }

        for (int i = 0; i<2; i++) {
            
            // si elle n'est pas valide
            var dicesToReroll = goalComb.dicesMissing(board);
            printAIActions(dicesToReroll);
            
            board.reroll(dicesToReroll);
            System.out.println(board);

            if (goalComb.isValid(board)){

                // on valide la combi
                scoreSheet.addCombination(goalComb, board);
                return;
            }
        }

        // si on a fait d'autre combi valides 
        if (scoreSheet.isCombinaisonPossible(board)){

            // on valide celle qui fait le plus de score
            scoreSheet.addCombination(getCombinationPossibleMaxScore(board), board); 
        } 
        else {

            // si on a pas reussit a valider
            // on sacrifie la comb qui a le moins de chance d'etre validé
            scoreSheet.sacrifyCombination(combinationToSacrify, board);
        }
    }

    public Combination getCombinationPossibleMaxScore(Board board){
        Objects.requireNonNull(board);
        var combiPossible = scoreSheet.combinaisonPossible(board);

        // on prend la combi qui rapporte plus plus de pts
        var maxComb = combiPossible.get(0);
        
        for (var comb : combiPossible){
            if (comb.score(board) > maxComb.score(board)){
                maxComb = comb;
            }
        }
        return maxComb;
    }

    @Override
    public int lenUserName() {
        return username.length();
    }

    @Override
    public String username() {
        return username;
    }

    @Override
    public void playRound() {
        System.out.println(username + "'s round : ");

        //On relance tous les dés et on les affiches
        board.rerollAllDice();
        System.out.println(board);

        //On reroll les dés
        reroll();
        
        System.out.println(scoreSheet);

    }
    
    public int lenScore(){
        //Renvoit la longueur du score
		return Integer.toString(scoreSheet.scoreTotal()).length();
	}

    @Override
    public int score() {
        //retourne le score pour les rang dans duoAI
        return scoreSheet.scoreTotal();
    }
}
