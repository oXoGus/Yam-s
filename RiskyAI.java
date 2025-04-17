package fr.uge.yams;

import java.util.ArrayList;
import java.util.HashMap;

public class RiskyAI implements IA{
    private final Board board;
    private final ScoreSheet scoreSheet;
    public RiskyAI(){
        board=new Board();
        scoreSheet=new ScoreSheet();
    }

    @Override
    //En fonction de l'IA choisie, on renvoit la liste qu'on va envoyer à Askreroll notamment en fonction de son coeff (probabilité * score)
    public ArrayList<Integer> probabilityComb () {
        var arraySame = isSame(board, scoreSheet);
        var arraySeq = isSequential(scoreSheet, board);
        int probaSame = calcComb(dicesMissing(arraySame));
        int probaSeq = calcComb(dicesMissing(arraySeq));
        //On prend les deux listes qui contiennent

        if (probaSame>probaSeq) {
            return arraySame;
        }
        else if (probaSeq>probaSame) {
            return arraySeq;
        }
        else {
            //on parcourt la liste des combinaisons possibles et on cherche le coeff le plus élevé 
            var maxComb = findCoeffMax(scoreSheet, board);
            //Une fois trouvé, si c'est un same on renvoit arraySame,
            if (seq(maxComb)) {
                return arraySeq;
            }
            else {
                return arraySame;
            }
            //Sinon on retourne arraySeq
        }
    }

    @Override
    public void reroll() {
        //On reroll tant qu'on a pas la combinaison libre qui a le plus de points, sous un maximum de 3 reroll
        Combination goalComb = highestScore(scoreSheet, board);
        for (int i = 0; i<3; i++) {
            if (combinationsFreeAndValid(board, scoreSheet).containsKey(goalComb)) {
                break;
            }
            if (seq(goalComb)) {
                askReroll(isSequential(scoreSheet, board), board);
            }
            else {
                askReroll(isSame(board, scoreSheet), board);
            }
        }
    }

    @Override
    public void playRound() {
        //On relance tous les dés et on les affiches
        board.rerollAllDice();
        System.out.println(board.fiveDice());

        //On reroll les dés
        reroll();
        System.out.println(board.fiveDice());

        //On choisit la combinaison à activer (celle avec le plus de points) ou celle à sacrifier
        choice();
        
        System.out.println(scoreSheet);

    }
    
    @Override
    public void choice() {
        //On cherche la combinaison avec le coeff (score et proba) le plus élevé
        //Si il est valide et libre on l'active
        var comb = highestScore(scoreSheet, board);
        if (combinationsFreeAndValid(board, scoreSheet).containsKey(comb)) {
            scoreSheet.addCombination(comb, board);
        }
        //Sinon on va chercher la combinaison avec le coeff le plus faible et on la sacrifie
        else {
            HashMap<Combination, Integer> freeComb = freeCombination(scoreSheet, board);
            Combination combination = new Chance();
            int minSum = 100;
            for (var c : freeComb.keySet()) {
                if (freeComb.get(c)<=minSum) {
                    minSum = freeComb.get(c);
                    combination=c;
                }
            }
            scoreSheet.sacrifyCombination(combination, board);
        }
    }
}
