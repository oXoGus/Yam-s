package fr.uge.yams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class AI {
    private final Board board; 
    private final ScoreSheet scoreSheet;

    public AI () {
        board=new Board();
        scoreSheet=new ScoreSheet();
    }

    public void playRound() {
        board.rerollAllDice();
        if (scoreSheet.isCombinaisonLeft(board)) {
            //On check toutes les combinaisons jusqu'à trouver la bonne
            //S'il y en a plusieurs, on les ajoutes aussi à la map avec leur somme pour choisir après
            HashMap<Combination, Integer> chosen = new HashMap<Combination, Integer>();
            var combC = new Chance();
            if (combC.isValid(board) && !(scoreSheet.isValidate(combC)) && !(scoreSheet.isSacrified(combC)) ) {
                chosen.put(combC, combC.score(board));
            }
            var combF = new FourOfAKind();
            if (combF.isValid(board) && !(scoreSheet.isValidate(combC)) && !(scoreSheet.isSacrified(combC)) ) {
                chosen.put(combF, combF.score(board));
            }
            var combFu = new FullHouse();
            if (combFu.isValid(board) && !(scoreSheet.isValidate(combC)) && !(scoreSheet.isSacrified(combC)) ) {
                chosen.put(combFu, combFu.score(board));
            }
            var combL = new LargeStraight();
            if (combL.isValid(board) && !(scoreSheet.isValidate(combC)) && !(scoreSheet.isSacrified(combC))) {
                chosen.put(combL, combL.score(board));
            }
            var combS = new SmallStraight();
            if (combS.isValid(board) && !(scoreSheet.isValidate(combC)) && !(scoreSheet.isSacrified(combC))) {
                chosen.put(combS, combS.score(board));
            }
            var combT = new ThreeOfAKind();
            if (combT.isValid(board) && !(scoreSheet.isValidate(combC)) && !(scoreSheet.isSacrified(combC))) {
                chosen.put(combT, combT.score(board));
            }
            var combY = new Yahtzee();
            if (combY.isValid(board) && !(scoreSheet.isValidate(combC)) && !(scoreSheet.isSacrified(combC))) {
                chosen.put(combY, combY.score(board));
            }
            int maxSum = 0; 
            Combination maxCom =new Chance();
            for (var comb : chosen.keySet()) {
                if (chosen.get(comb)>maxSum) {
                    maxCom = comb;
                }
            }
            scoreSheet.addCombination(maxCom, board);
        //Comparer et ajouter la combinaison avec la sum la plus élevée 
        }
        else {
            reroll();
        }
        System.out.println(" AI - round : \n" + scoreSheet);
    }

    public boolean checkCombSame () {
        if ((!scoreSheet.isValidate(new ThreeOfAKind()) && !scoreSheet.isSacrified(new ThreeOfAKind()))) {
            return true;
        } 
        if (!scoreSheet.isValidate(new FourOfAKind()) && !scoreSheet.isSacrified(new FourOfAKind())) {
            return true;
        }
        if (!scoreSheet.isValidate(new FullHouse()) && !scoreSheet.isSacrified(new FullHouse())) {
            return true;
        } 
        if (!scoreSheet.isValidate(new Yahtzee()) && !scoreSheet.isSacrified(new Yahtzee())) {
            return true;
        }
        return false;
    }
    //return les valeurs qui sont identiques, s'il n'y en a pas return null ou liste vide, s'il y en a plusieurs, retourne les valeurs dont la somme est la plus élevée 
    public ArrayList<Integer> isSame () {
        ArrayList<Integer> lst = new ArrayList<Integer>();
        if (checkCombSame()) {
            var lstocc = board.occurence();
            for (int i = 0 ; i<5; i++) {
                if (lstocc.get(i)>1) {
                    lst.add(Integer.valueOf(i));
                }
            }
        }
        return lst;

    }

    public ArrayList<Integer> isSequential() {
        ArrayList<Integer> lst = new ArrayList<Integer>();
        //Si les combinaisons ne sont ni sacrifiées ni activées
        if (!(scoreSheet.isValidate(new LargeStraight())) || !scoreSheet.isSacrified(new LargeStraight()) || !scoreSheet.isValidate(new SmallStraight()) || !scoreSheet.isSacrified(new SmallStraight())) {
            for (Dice dice : board.fiveDice()) {
                if (board.fiveDice().contains(new Dice(dice.value()+1))) {
                    if (!lst.contains(dice.value())) {
                        lst.add(Integer.valueOf(dice.value()));
                    }
                    if (!lst.contains(dice.value()+1)) {
                        lst.add(Integer.valueOf(dice.value()+1));
                    }
                }
            }
            return lst;
        }
        return lst;
        //On vérifie qu'il reste des combinaisons utilisant les séquences
        //Cherche des séquences
        //S'il y a plusieurs séquences, on les ajoute à la liste aussi 


    }

    public Integer arraySum (ArrayList<Integer> lst) {
        var sum = 0;
        for (var i : lst) {
            sum+=i;
        }
        return sum;
    }


    //Calculer selon le score.
    public void reroll() {
        for (int i = 0; i<3; i++) {
            if (isSame()!=null && isSequential()!=null) {
                var sameArray = isSame();
                var sequentialArray = isSequential();
    
                if (arraySum(sameArray)>arraySum(sequentialArray)) {
                    askReroll(sameArray);
                }
                else {
                    askReroll(sequentialArray);
                }
                
                //Cherche la somme la plus élevée des deux 
                //Puis reroll les dices qui ne sont pas dans la liste choisie
    
            }
            else if (isSequential()!=null) {
                //reroll les dices qui ne sont pas dans la liste retournée 
                askReroll(isSequential());
    
            }
            else if (isSame()!=null) {
                //reroll les dices qui ne sont pas dans la liste retournée
                askReroll(isSame());
    
            }
            else {
                board.rerollAllDice();
            }
        }
    }


    //A modifier pour faire selon la valeur du dice 
    public void askReroll(ArrayList<Integer> dicesKept) {
        HashSet<Integer> lst = new HashSet<Integer>();
        for (Integer i = 0; i<5 ; i++) {
            if (dicesKept.contains(i)==false) {
                lst.add(i);
            }
        }
        board.reroll(lst);
    }
}
