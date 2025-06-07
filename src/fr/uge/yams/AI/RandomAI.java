package fr.uge.yams.AI;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Random;


import fr.uge.yams.Board;
import fr.uge.yams.combinations.Combination;
import fr.uge.yams.models.CombinationInfo;
import fr.uge.yams.models.ScoreSheet;
import fr.uge.yams.models.UserScore;
import javafx.scene.Node;
import javafx.scene.shape.Shape;

public class RandomAI implements AI{
    private final Board board;
    private final ScoreSheet scoreSheet;
    private final String username;

    public RandomAI(int numAI, List<Combination> combinaitionChosen, Board boardType){
        Objects.requireNonNull(combinaitionChosen);
        Objects.requireNonNull(boardType);
        
        board=boardType;
        scoreSheet = new ScoreSheet(combinaitionChosen);
        username = "Random AI #" + numAI;
    }

   
    @Override
    //Prend un nombre au hasard entre 0 et 3 inclus et relance ce nombre de fois les dés
    public void reroll() {
        int nbRounds = new Random().nextInt(3);
        
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
            var dicesToReroll = goalComb.elementsMissing(board);
            //printAIActions(dicesToReroll);
            
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
        board.rerollAll();
        System.out.println(board);

        //On reroll pour potentiellement relancer les dés
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
	public List<Node> boardShapes(){
		return board.allGameElementShapes();
	}

    @Override
	public String username(){
		return username;
	}

    @Override
    public boolean isWithCards(){
        return board.isBoardCard();
    }

    @Override
	public void reroll(Collection<Integer> positions){
		board.reroll(positions);
	}
    
    @Override
	public List<Node> boardShapes(Collection<Integer> pos){
		return board.gameElementShapes(pos);
	}

    @Override
	public String testCombination(Combination combination){
		// on fait tout les tests pour ajouter une combinaison
		// si on ne peut pas l'ajouter on renvoie la cause sous forme de String 
		if (scoreSheet.isSacrified(combination)){
			return "this combination has already been sacrified";
		}

		if (scoreSheet.isValidate(combination)){
			return "this combination has already been validated";
		}
		
		// si aucun problème pour l'ajouter ou la supprimer
		return null;
	}

	@Override
	public String trySacrifyCombination(Combination combination){
		
		// si on ne peut pas sacrifier cette combinasion
		var errorCode = testCombination(combination);
		if (errorCode != null){
			return errorCode;
		}

		// sinon on sacrifie la combi
		scoreSheet.sacrifyCombination(combination, board);

		return null;
	}

	@Override
	public String tryAddCombination(Combination combination){
		
		// si on ne peut pas ajouter cette combinasion
		var errorCode = testCombination(combination);
		if (errorCode != null){
			return errorCode;
		}

		if (!combination.isValid(board)){
			return "this combination is note valid with this board";
		}

		// sinon on ajoute la combi
		scoreSheet.addCombination(combination, board);

		return null;
	}

	@Override
	public boolean isCombinationPossible(){
		return scoreSheet.isCombinaisonPossible(board);
	}
}
