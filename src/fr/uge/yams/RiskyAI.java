package fr.uge.yams;

import java.util.List;

public class RiskyAI implements AI{
    private final Board board;
    private final ScoreSheet scoreSheet;
    public RiskyAI(){
        board=new Board();
        scoreSheet=new ScoreSheet();
    }

    @Override
    //En fonction de l'IA choisie, on renvoit la liste qu'on va envoyer à Askreroll notamment en fonction de son coeff (probabilité * score)
    public List<Integer> probabilityComb () {
        var arraySame = diceSameList(board, scoreSheet);
        var arraySeq = diceSeqList(board, scoreSheet);
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
            if (isSeq(maxComb)) {
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
        System.out.println("goal comb");
        System.out.println(goalComb);
        for (int i = 0; i<3; i++) {
            if (combinationsFreeAndValid(board, scoreSheet).containsKey(goalComb)) {
                break;
            }
            if (isSeq(goalComb)) {

                askReroll(diceSeqList(board, scoreSheet), board);
            }
            else {
                askReroll(diceSameList(board, scoreSheet), board);
            }
            System.out.println(board);
        }
    }

    @Override
    public void playRound() {
        System.out.println("AI's round : ");

        //On relance tous les dés et on les affiches
        board.rerollAllDice();
        System.out.println(board);

        //On reroll les dés
        reroll();

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
            var freeComb = freeCombinations(scoreSheet, board);
            Combination combination = new Chance();
            double minSum = 100;
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
