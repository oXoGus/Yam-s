package fr.uge.yams;

import java.util.HashMap;
import java.util.List;

public class SafeAI implements AI{
    private final Board board;
    private final ScoreSheet scoreSheet;
    public SafeAI(){
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
            //Chercher s'il y a plus de Same ou plus de Seq
            return (howManySame(scoreSheet, board)>howManySeq(scoreSheet, board))?arraySame : arraySeq;
        } 
    }

    @Override
    public void reroll() {
        //On reroll tant qu'on a pas de combinaison possible, jusqu'à trois fois maximum
        for (int i = 0 ; i<3; i++) {
            if (scoreSheet.isCombinaisonPossible(board)) {
                break;
            }
            if (diceSameList(board, scoreSheet)!=null && diceSeqList(board, scoreSheet)!=null) {
                
                askReroll(probabilityComb(), board);
                
                //Cherche la somme la plus élevée des deux 
                //Puis reroll les dices qui ne sont pas dans la liste choisie
    
            }
            else if (diceSeqList(board, scoreSheet)!=null) {
                //reroll les dices qui ne sont pas dans la liste retournée 
                askReroll(diceSeqList(board, scoreSheet), board);
    
            }
            else if (diceSameList(board, scoreSheet)!=null) {
                //reroll les dices qui ne sont pas dans la liste retournée
                askReroll(diceSameList(board, scoreSheet), board);
    
            }
            else {
                board.rerollAllDice();
            }
            System.out.println(board);
        }

    }

    @Override
    public void playRound() {
        //On relance tous les dés et on les affiche
        board.rerollAllDice();
        System.out.println(board.fiveDice());

        //On reroll les dés maximum trois fois
        reroll();
        System.out.println(board.fiveDice());

        //On choisi la combinaison à activer ou sacrifier
        choice();

        System.out.println(scoreSheet);

    }
    
    @Override
    public void choice() {
        //Si il y a une combinaison possible, on choisi celle ci, S'il y en a plusieurs on choisi celle avec le score le plus haut
        if(scoreSheet.isCombinaisonPossible(board)) {
            //On check toutes les combinaisons jusqu'à trouver la bonne
            //S'il y en a plusieurs, on les ajoutes aussi à la map avec leur somme pour choisir après
            HashMap<Combination, Integer> chosen = combinationsFreeAndValid(board, scoreSheet);
            Combination combination = new Chance();
            // on trouve la meilleur combinaison si il y en a plusieurs
            int maxSum = 0; 
            for (var comb : chosen.keySet()) {
                if (chosen.get(comb)>maxSum) {
                    maxSum = chosen.get(comb);
                    combination = comb;
                }
            }
            scoreSheet.addCombination(combination, board);
        }
        else {
            var freeComb = freeCombinations(scoreSheet, board);
            Combination combination = new Chance();
            double minSum = 100;
            for (var comb : freeComb.keySet()) {
                if (freeComb.get(comb)<=minSum) {
                    minSum = freeComb.get(comb);
                    combination=comb;
                }
            }
            scoreSheet.sacrifyCombination(combination, board);
        }
    }
}
