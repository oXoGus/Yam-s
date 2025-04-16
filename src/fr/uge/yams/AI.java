package fr.uge.yams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
        String res=scanner.nextLine();
        
        // gesitons des erreurs
        while (!Games.isInteger(res) || (Integer.parseInt(res) != 1 && Integer.parseInt(res) !=2 && Integer.parseInt(res) !=3)) {
            System.out.println("Please enter a correct answer");
            res=scanner.nextLine();
        }
        type = Integer.parseInt(res);
    }

    public HashMap<Combination, Integer> combinationsFreeAndValid (Board board) {
        HashMap<Combination, Integer> chosen = new HashMap<Combination, Integer>();

        // toutes les combi
		List<Combination> combinations = List.of(new Chance(), new ThreeOfAKind(), new FourOfAKind(), new FullHouse(), new SmallStraight(), new LargeStraight(), new Yahtzee());

        for (var combination : combinations){
            if (combination.isValid(board) && !(scoreSheet.isValidate(combination)) && !(scoreSheet.isSacrified(combination)) ) {
                chosen.put(combination, combination.score(board));
            }
        }
    
        return chosen;
    }
    public void playRound() {
        //Adapter le choix de la combinaison au type d'AI choisi
        board.rerollAllDice();
        System.out.println(" AI - round : \n");
        System.out.println(board);

        //Si il y a une combinaison possible à activer 
        if (scoreSheet.isCombinaisonPossible(board)) {

            //On check toutes les combinaisons jusqu'à trouver la bonne
            //S'il y en a plusieurs, on les ajoutes aussi à la map avec leur somme pour choisir après
            HashMap<Combination, Integer> chosen = combinationsFreeAndValid(board);
            
            // on trouve la meilleur combinaison si il y en a plusieurs
            int maxSum = 0; 
            Combination bestComb = new Chance();
            for (var comb : chosen.keySet()) {
                if (chosen.get(comb)>maxSum) {
                    bestComb = comb;
                }
            }
            
            //Si SAFE
            if (type==2) {

                // on valide directment la combinaison
                scoreSheet.addCombination(bestComb, board);
            }
            //Si l'AI joue de manière Random 
            else if (type ==1) {
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
            else if (type==3) {
                
                //Si la combinaison free aux points les plus élevés n'est pas disponible dans free and valid, alors on reroll plutôt que de valider une combinaison
                var comb = highestScore();
                if (combinationsFreeAndValid(board).containsKey(comb)) {
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
        System.out.println(scoreSheet);
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


    //return les valeurs qui sont identiques, 
    // s'il n'y en a pas return null ou liste vide, 
    // s'il y en a plusieurs, retourne les valeurs dont la somme est la plus élevée 
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
            System.out.println(board);
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

        // hashMap contenant toutes le combinaison 
        // qui ne sont pas encore validé ou sacrifié
        // avec leurs probabilité 
        var freeComb = new HashMap<Combination, Integer>();
        
        // toutes les combi
		List<Combination> combinations = List.of(new Chance(), new ThreeOfAKind(), new FourOfAKind(), new FullHouse(), new SmallStraight(), new LargeStraight(), new Yahtzee());

        for (var combinaison : combinations){
            if (!(scoreSheet.isValidate(combinaison)) && !(scoreSheet.isSacrified(combinaison))) {
                freeComb.put(combinaison, combinaison.coefficient(board, dicesMissing(isSequential())));
            }
        }
        
        return freeComb;
    }

    public boolean seq (Combination comb) {
        // true si comb est un SmallStraight ou un LargeStraight
        return comb.equals(new SmallStraight()) || comb.equals(new LargeStraight());
    }

    public Combination findCoeffMax () {
        // renvoie la combinaison qui a le plus de chance d'etre réalisable
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

    //A modifier pour faire selon la valeur du dice 
    public void askReroll(ArrayList<Integer> dicesKept) {
        Set<Integer> lst = new HashSet<Integer>();
        String s = "";
        for (Integer i = 0; i<5 ; i++) {
            if (dicesKept.contains(i)==false) {
                lst.add(i+1);
                s += (i + 1) + " ";
            }
        }
        // affichages des dés reroll par l'IA
        System.out.println(s);
        board.reroll(lst);
    }
}
