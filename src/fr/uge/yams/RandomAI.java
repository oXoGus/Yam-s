package fr.uge.yams;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class RandomAI implements AI{
    private final Board board;
    private final ScoreSheet scoreSheet;
    public RandomAI(){
        board=new Board();
        scoreSheet=new ScoreSheet();
    }

    @Override
    //En fonction de l'IA choisie, on renvoit la liste qu'on va envoyer à Askreroll 
    // notamment en fonction de son coeff (probabilité * score)
    public List<Integer> probabilityComb () {
        var arraySame = diceSameList(board, scoreSheet);
        var arraySeq = diceSeqList(board, scoreSheet);

        // calcul de la probabilité de faire une combinaison de type seq et de type same
        // en fonction de 
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
            var nb = new Random().nextInt(2);
            if (nb == 0) {
                return arraySame;
            }
        }
        return arraySeq;
        
    }
    @Override
    //Prend un nombre au hasard entre 0 et 3 inclus et relance ce nombre de fois les dés
    public void reroll() {
        int nbRounds = new Random().nextInt(4);
        
        for (int i = 0; i<nbRounds; i++) {
            if (diceSameList(board, scoreSheet)!=null && diceSeqList(board, scoreSheet)!=null) {
            
                askReroll(probabilityComb(), board);
                
                //Cherche la somme la plus élevée des deux 
                //Puis reroll les dices qui ne sont pas dans la liste choisie
    
            }
            else if (diceSeqList(board, scoreSheet)!=null) {
                //reroll les dices qui ne sont pas dans la liste retournée 
                askReroll(diceSeqList(board, scoreSheet), board);
    
            }
            else if (diceSameList(board, scoreSheet) !=null) {
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
        //On relance les dés et on les affiche
        board.rerollAllDice();
        System.out.println(board.fiveDice());

        //On reroll pour potentiellement relancer les dés
        reroll();
        System.out.println(board.fiveDice());

        //On fait le choix
        choice();

        System.out.println(scoreSheet);
    }
    
    @Override
    public void choice() {
        if (scoreSheet.isCombinaisonPossible(board)) {
            HashMap<Combination, Integer> chosen = combinationsFreeAndValid(board, scoreSheet);
            // On choisit un nb au hasard qui désigne la combinaison choisie et c'est celle ci qu'on renvoie au addComb
            var nb = new Random().nextInt(chosen.size());
            var cmp=0;
            Combination chosenComb = new Chance();
            for (var comb : chosen.keySet()) {
                if (cmp == nb) {
                    chosenComb=comb;
                    break;
                }
                else {
                    cmp++;
                }
            }
            scoreSheet.addCombination(chosenComb, board);
        }
        else {
            var chosen = freeCombinations(scoreSheet, board);
            // On choisit un nb au hasard qui désigne la combinaison choisie et c'est celle ci qu'on renvoie au addComb
            var nb = new Random().nextInt(chosen.size());
            var cmp=0;
            Combination chosenComb = new Chance();
            for (var comb : chosen.keySet()) {
                if (cmp == nb) {
                    chosenComb=comb;
                    break;
                }
                else {
                    cmp++;
                }
            }
            scoreSheet.sacrifyCombination(chosenComb, board);
        }
    }
}
