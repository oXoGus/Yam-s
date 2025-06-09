package fr.uge.yams.models.AI;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import fr.uge.yams.models.Board;
import fr.uge.yams.models.Combination;
import fr.uge.yams.models.CombinationInfo;
import fr.uge.yams.models.ScoreSheet;
import fr.uge.yams.models.UserScore;
import fr.uge.yams.models.combinations.Chance;
import javafx.scene.Node;

public class SafeAI implements AI{
    private final Board board;
    private final ScoreSheet scoreSheet;
    private final String username;

    public SafeAI(int numAI, List<Combination> combinaitionChosen, Board boardType){
        Objects.requireNonNull(combinaitionChosen);
        Objects.requireNonNull(boardType);
        
        board=boardType;
        scoreSheet = new ScoreSheet(combinaitionChosen);
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
            var dicesToReroll = goalComb.elementsMissing(board);
            //printAIActions(dicesToReroll);
            
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
        board.rerollAll();
        System.out.println(board);

        //On reroll les dés maximum trois fois
        reroll();


        System.out.println(scoreSheet);

    }
    
    @Override
    public List<CombinationInfo> scoreSheet(){
        return scoreSheet.combinaitionsInfo(board);
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
        Objects.requireNonNull(positions);

		board.reroll(positions);
	}

    @Override
	public List<Node> boardShapes(Collection<Integer> pos){
        Objects.requireNonNull(pos);

		return board.gameElementShapes(pos);
	}

    @Override
	public String testCombination(Combination combination){
        Objects.requireNonNull(combination);

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
        Objects.requireNonNull(combination);

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
        Objects.requireNonNull(combination);

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

    @Override
    public void reset() {
        scoreSheet.clear();
    }

    @Override
    public Combination goalCombination() {
        return goalCombination(scoreSheet, board);
    }

    @Override
    public List<Integer> gameElementToReroll(){
        return goalCombination().elementsMissing(board);
    }

    @Override
    public void lastChoice(){
        // on choisi la meilleur combinaison a sacrifier ou a valider en fonction 
        
        // si on a fait d'autre combi valides que celle de la goal
        if (scoreSheet.isCombinaisonPossible(board)){

            // on valide celle qui fait le plus de score
            scoreSheet.addCombination(getCombinationPossibleMaxScore(board), board); 
        } 
        else {

            // si on a pas reussit a valider
            // on sacrifie la comb qui a le moins de chance d'etre validé
            scoreSheet.sacrifyCombination(sacrifyCombination(scoreSheet, board), board);
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
}
