package fr.uge.yams.AI;

import java.util.List;
import java.util.Objects;

import fr.uge.yams.Board;
import fr.uge.yams.combinations.Combination;
import fr.uge.yams.models.CombinationInfo;
import fr.uge.yams.models.ScoreSheet;
import fr.uge.yams.models.UserScore;
import javafx.scene.Node;

public class RiskyAI implements AI{
    private final Board board;
    private final ScoreSheet scoreSheet;
    private final String username;

    public RiskyAI(int numAI, List<Combination> combinaitionChosen, Board boardType){
        Objects.requireNonNull(combinaitionChosen);
        Objects.requireNonNull(boardType);
        
        board=boardType;
        scoreSheet = new ScoreSheet(combinaitionChosen);
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
            var dicesToReroll = goalComb.elementsMissing(board);
            //printAIActions(dicesToReroll);
            
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
    public void playRound() {
        System.out.println(username + "'s round : ");

        //On relance tous les dés et on les affiches
        board.rerollAll();
        System.out.println(board);

        //On reroll les dés
        reroll();
        
        System.out.println(scoreSheet);

    }
    
 

    @Override
    public List<CombinationInfo> scoreSheet(){
        return scoreSheet.combinaitionsInfo();
    }

    @Override
    public UserScore score(){
		return new UserScore(username, scoreSheet.scoreTotal());
	}

    @Override
	public void rerollAll(){
		board.rerollAll();
	}

    @Override
	public String username(){
		return username;
	}

    @Override
	public List<Node> boardShapes(){
		return board.shapes();
	}

    @Override
    public boolean isWithCards(){
        return board.isBoardCard();
    }
}
