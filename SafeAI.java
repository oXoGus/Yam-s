package fr.uge.yams;

import java.util.ArrayList;
import java.util.HashMap;

public class SafeAI implements IA{
    private final Board board;
    private final ScoreSheet scoreSheet;
    public SafeAI(){
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
            if (isSame(board, scoreSheet)!=null && isSequential(scoreSheet, board)!=null) {
                
                askReroll(probabilityComb(), board);
                
                //Cherche la somme la plus élevée des deux 
                //Puis reroll les dices qui ne sont pas dans la liste choisie
    
            }
            else if (isSequential(scoreSheet, board)!=null) {
                //reroll les dices qui ne sont pas dans la liste retournée 
                askReroll(isSequential(scoreSheet, board), board);
    
            }
            else if (isSame(board, scoreSheet)!=null) {
                //reroll les dices qui ne sont pas dans la liste retournée
                askReroll(isSame(board, scoreSheet), board);
    
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
            HashMap<Combination, Integer> freeComb = freeCombination(scoreSheet, board);
            Combination combination = new Chance();
            int minSum = 100;
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
