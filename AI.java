package fr.uge.yams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.Random;

public class AI {
    private final Board board; 
    private final ScoreSheet scoreSheet;
    private final Scanner scanner ;
    private int type ;

    public AI () {
        board=new Board();
        scoreSheet=new ScoreSheet();
        scanner = new Scanner(System.in);
        System.out.println("Please choose your AI by typing its number : \n1. Random \n2. Playing Safe \n3. Playing Risky");
        type=scanner.nextInt();
        if (type!= 1 && type!=2 && type!=3) {
            while (type!=1 && type !=2 && type!=3) {
                System.out.println("Please enter a correct answer");
                type=scanner.nextInt();
            }
        }
        
    }

    public HashMap<Combination, Integer> freeAndValid (Board board) {
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
        return chosen;
    }
    public void playRound() {
        //Adapter le choix de la combinaison au type d'AI choisi
        board.rerollAllDice();
        //Si il y a une combinaison possible à activer 
        if (scoreSheet.isCombinaisonLeft(board)) {
            //Si SAFE
            if (type==2) {
            //On check toutes les combinaisons jusqu'à trouver la bonne
            //S'il y en a plusieurs, on les ajoutes aussi à la map avec leur somme pour choisir après
            HashMap<Combination, Integer> chosen = freeAndValid(board);
            int maxSum = 0; 
            Combination maxCom = new Chance();
            for (var comb : chosen.keySet()) {
                if (chosen.get(comb)>maxSum) {
                    maxCom = comb;
                }
            }
            scoreSheet.addCombination(maxCom, board);
            //Comparer et ajouter la combinaison avec la sum la plus élevée 
            }
            //Si l'AI joue de manière Random 
            else if (type ==1) {
                //On choisit un nb au hasard qui désigne la combinaison choisie et c'est celle ci qu'on renvoie au addComb
                HashMap<Combination, Integer> chosen = freeAndValid(board);
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
            else if (type==3) {
                //Si la combinaison free aux points les plus élevés n'est pas disponible dans free and valid, alors on reroll plutôt que de valider une combinaison
                var comb = highestScore();
                if (freeAndValid(board).containsKey(comb)) {
                    scoreSheet.addCombination(comb, board);
                }
                else {
                    reroll();
                }
            }
        }
        else {
            reroll();
        }
        System.out.println(" AI - round : \n" + scoreSheet);
    }

    public Combination highestScore () {
        //Chercher le plus haut score dans free combinations, puis retourner la combinaison si elle est dans free and valid, sinon retourner null
        var combinations = freeCombination();
        var maxScore = 0;
        Combination maxComb = new Chance();
        for (Combination comb : combinations.keySet()) {
            if (combinations.get(comb)>maxScore) {
                maxComb=comb;
            }
        }
        return maxComb;
    }


    //Vérifie s'il reste des combinaisons de type same 
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
            for (int i = 0; i<5; i++) {
                if (board.fiveDice().get(i).value()<6 && board.fiveDice().contains(new Dice(board.fiveDice().get(i).value()+1))) {
                    if (!lst.contains(board.fiveDice().get(i).value())) {
                        lst.add(Integer.valueOf(i));
                    }
                    if (!lst.contains(board.fiveDice().get(i).value()+1)) {
                        lst.add(Integer.valueOf(board.fiveDice().indexOf(new Dice(board.fiveDice().get(i).value()+1))));
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


    //Calculer selon le score.
    public void reroll() {
        for (int i = 0; i<3; i++) {
            if (isSame()!=null && isSequential()!=null) {
                
                //A modif car fait la somme des index et pas des valeurs des dés
                askReroll(probabilityComb());
                
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

    public int calcComb (int dicesMissing) {
        //Calcule la probabilité d'une Combinaison d'arriver
        return dicesMissing*1/6 ;
    }

    public int dicesMissing (ArrayList<Integer> array) {
        int cmp = 0;
        for (Integer i=0; i<5; i++) {
            //Si le dés i n'est pas dans la liste de séquence ou de same, alors on ajoute un dés manquant
            if (!array.contains(i)) {
                cmp++;
            }
        }
        return cmp;
    }

    public HashMap<Combination, Integer> freeCombination () {
        var freeComb = new HashMap<Combination, Integer>();
        var combS = new SmallStraight();
        if (!(scoreSheet.isValidate(combS)) && !(scoreSheet.isSacrified(combS))) {
            freeComb.put(combS, combS.coefficient(board, dicesMissing(isSequential())));
        }
        var combL = new LargeStraight();
        if (!(scoreSheet.isValidate(combL)) && !(scoreSheet.isSacrified(combL))) {
            freeComb.put(combL, combL.coefficient(board, dicesMissing(isSequential())));
        }
        var combT = new ThreeOfAKind();
        if (!(scoreSheet.isValidate(combT)) && !(scoreSheet.isSacrified(combT))) {
            freeComb.put(combT, combT.coefficient(board, dicesMissing(isSame())));
        }
        var combFo = new FourOfAKind();
        if (!(scoreSheet.isValidate(combFo)) && !(scoreSheet.isSacrified(combFo))) {
            freeComb.put(combFo, combFo.coefficient(board, dicesMissing(isSame())));
        }
        var combFu = new FullHouse();
        if (!(scoreSheet.isValidate(combFu)) && !(scoreSheet.isSacrified(combFu))) {
            freeComb.put(combFu, combFu.coefficient(board, dicesMissing(isSame())));
        }
        var combY = new Yahtzee();
        if (!(scoreSheet.isValidate(combY)) && !(scoreSheet.isSacrified(combY))) {
            freeComb.put(combY, combY.coefficient(board, dicesMissing(isSame())));
        }
        var combC = new Chance();
        if (!(scoreSheet.isValidate(combC)) && !(scoreSheet.isSacrified(combC))) {
            freeComb.put(combC, combC.coefficient(board, 0));
        }
        return freeComb;
    }

    public boolean seq (Combination comb) {
        var s = new SmallStraight();
        var l = new LargeStraight();
        if (comb.equals(s) || comb.equals(l)) {
            return true;
        }
        return false;
    }

    public Combination findCoeffMax () {
        Integer max = 0;
        Combination maxC = new Chance();
        var free = freeCombination();
        for (var c : free.keySet()) {
            if (free.get(c)>=max) {
                max=free.get(c);
                maxC = c;
            }
        }
        return maxC;
    }

    public ArrayList<Integer> probabilityComb () {
        var arraySame = isSame();
        var arraySeq = isSequential();
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
            if (type==2) {
                //Chercher s'il y a plus de Same ou plus de Seq
                return (howManySame()>howManySeq())?arraySame : arraySeq;

            }
            //Si AI risquée 
             else if (type==3) {
                //on parcourt la liste des combinaisons possibles et on cherche le coeff le plus élevé 
                var maxComb = findCoeffMax();
                //Une fois trouvé, si c'est un same on renvoit arraySame,
                if (seq(maxComb)) {
                    return arraySeq;
                }
                else {
                    return arraySame;
                }
                //Sinon on retourne arraySeq
            }
            else {
                var nb = new Random().nextInt(2);
                if (nb == 0) {
                    return arraySame;
                }
            }
        }
        return arraySeq;
        
    }

    public int howManySeq () {
        int countSeq = 0;
        var combS = new SmallStraight();
        if (freeCombination().containsKey(combS)) {
            countSeq ++;
        }
        var combL = new LargeStraight();
        if (freeCombination().containsKey(combL)) {
            countSeq ++;
        }
        return countSeq;
    }

    public int howManySame () {
        int countSame = 0;
        var freeComb = freeCombination();
        var combT = new ThreeOfAKind();
        if (freeComb.containsKey(combT)) {
            countSame ++;
        }
        var combFo = new FourOfAKind();
        if (freeComb.containsKey(combFo)) {
            countSame ++; 
        }
        var combFu = new FullHouse();
        if (freeComb.containsKey(combFu)) {
            countSame ++; 
        }
        var combY = new Yahtzee();
        if (freeComb.containsKey(combY)) {
            countSame ++; 
        }
        return countSame;
    }


    //Pb est la valeur des positions qui sont retournées

    //A modifier pour faire selon la valeur du dice 
    public void askReroll(ArrayList<Integer> dicesKept) {
        Set<Integer> lst = new HashSet<Integer>();
        for (Integer i = 0; i<5 ; i++) {
            if (dicesKept.contains(i)==false) {
                lst.add(i+1);
            }
        }
        board.reroll(lst);
    }
}
