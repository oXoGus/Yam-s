package fr.uge.yams.models.AI;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import fr.uge.yams.models.Board;
import fr.uge.yams.models.Combination;
import fr.uge.yams.models.ScoreSheet;
import fr.uge.yams.models.User;
import fr.uge.yams.models.UserScore;
import fr.uge.yams.models.combinations.Chance;
import fr.uge.yams.models.combinations.FourOfAKind;
import fr.uge.yams.models.combinations.FullHouse;
import fr.uge.yams.models.combinations.LargeStraight;
import fr.uge.yams.models.combinations.SmallStraight;
import fr.uge.yams.models.combinations.ThreeOfAKind;
import fr.uge.yams.models.combinations.Yahtzee;

public interface AI extends User{
    void reroll();
    void playRound();

    // constantes
    List<Combination> combinations = List.of(new Chance(), new ThreeOfAKind(), new FourOfAKind(), new FullHouse(), new SmallStraight(), new LargeStraight(), new Yahtzee());
    List<Combination> combSame = List.of(new ThreeOfAKind(), new FourOfAKind(), new FullHouse(), new Yahtzee());
    List<Combination> combSeq = List.of(new SmallStraight(), new LargeStraight());
    

    public static AI chooseAI(int numAI, String AItype, List<Combination> combinationsChosen, Board boardType) { 
        Objects.requireNonNull(AItype);
        Objects.requireNonNull(combinationsChosen);
        Objects.requireNonNull(boardType);

        if (numAI < 0){
            throw new IllegalArgumentException();
        }

        // on choisie le type d'IA 
        switch (AItype) {
            case "Random AI" : return new RandomAI(numAI, combinationsChosen, boardType);
            case "Safe AI" : return new SafeAI(numAI, combinationsChosen, boardType);
            default : return new RiskyAI(numAI, combinationsChosen, boardType);
        }
	}
    

    //Retourne le score de l'IA avec son pseudo
    UserScore score();

    //Retourne les combinaisons qui ne sont pas encore sacrifiées ou activées 
    default HashMap<Combination, Double> freeCombinations (ScoreSheet scoreSheet, Board board) {
        Objects.requireNonNull(board);
        Objects.requireNonNull(scoreSheet);

        // hashMap contenant toutes le combinaison 
        // qui ne sont pas encore validé ou sacrifié
        // avec leurs coeff
        var freeComb = new HashMap<Combination, Double>();
        
        for (var combinaison : scoreSheet.combinationChosen()){
            if (!(scoreSheet.isValidate(combinaison)) && !(scoreSheet.isSacrified(combinaison))) {
                freeComb.put(combinaison, combinaison.coefficient(board));
            }
        }
        
        return freeComb;
    }


    // recherche le la meilleur combi en fonction de son coef
    default Combination goalCombination(ScoreSheet scoreSheet, Board board) {
        Objects.requireNonNull(board);
        Objects.requireNonNull(scoreSheet);

        var combinations = freeCombinations(scoreSheet, board);
        Combination maxComb = new Chance();
        double maxCoef = 0;

        // on prend comme valeur par défaut les une clé et sa valeur au hasard
        for (var comb : combinations.entrySet()){
            maxComb = comb.getKey();
            maxCoef = comb.getValue();
            break;
        }

        for (var comb : combinations.entrySet()) {
            if (comb.getValue() > maxCoef) {
                maxComb=comb.getKey();
                maxCoef = comb.getValue();
            }
        }

        // combi libre avec le plus grand coef
        return maxComb;
    }

    

    // on sacrifie la combi qui avait le coef le plus bas
    default public Combination sacrifyCombination(ScoreSheet scoreSheet, Board board){
        Objects.requireNonNull(board);
        Objects.requireNonNull(scoreSheet);

        var combinations = freeCombinations(scoreSheet, board);
        Combination minComb = new Chance();
        

        // on prend comme valeur par défaut les une clé et sa valeur au hasard
        for (var comb : combinations.entrySet()){
            minComb = comb.getKey();
            break;
        }

        for (var comb : combinations.entrySet()) {
            if (comb.getValue() < minComb.coefficient(board)) {
                minComb=comb.getKey();
            }
        }

        // combi libre avec le plus petit coef
        return minComb; 
    }

    @Override
    boolean isWithCards();

}
