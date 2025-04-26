package fr.uge.yams;


public class SafeAI implements AI{
    private final Board board;
    private final ScoreSheet scoreSheet;
    private final String username;

    public SafeAI(int numAI){
        board=new Board();
        scoreSheet=new ScoreSheet();
        username = "Safe AI #" + numAI;
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


            // on recalcul le goal combi puisque les proba on changé 
            if (goalComb != goalCombination(scoreSheet, board)){
                goalComb = goalCombination(scoreSheet, board);
                System.out.println("Changing the strategy...");
                System.out.println("Goal Combination : " + goalComb);
            }
            


            if (goalComb.isValid(board)){

                // on valide la combi
                scoreSheet.addCombination(goalComb, board);
                return;
            }
        }
        if (!scoreSheet.isValidate(new Chance()) && !scoreSheet.isSacrified(new Chance())){
            scoreSheet.addCombination(new Chance(), board);
            return;
        }

        // si on a pas reussit a la valider
        // on sacrifie la comb qui a le moins de chance d'etre validé
        scoreSheet.sacrifyCombination(combinationToSacrify, board);
    }

    @Override
    public void playRound() {
        //On relance tous les dés et on les affiche
        board.rerollAllDice();
        System.out.println(board);

        //On reroll les dés maximum trois fois
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
