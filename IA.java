package fr.uge.yams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface IA {
    public void reroll();
    public void playRound();
    public ArrayList<Integer> probabilityComb();
    public void choice();

    //Renvoie les combinaisons qui sont libres et qui sont valides par rapport aux dés du board
    default HashMap<Combination, Integer> combinationsFreeAndValid (Board board, ScoreSheet scoreSheet) {
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
    //Renvoie true si la combinaison entrée est de type séquentielle 
    default boolean seq (Combination comb) {
        // true si comb est un SmallStraight ou un LargeStraight
        return comb.equals(new SmallStraight()) || comb.equals(new LargeStraight());
    }

    //Fait la demande de reroll en envoyant les dés qui ne sont pas compris dans la liste donnée (car on utilise des listes qui contiennent les index des dés à garder )
    default void askReroll(ArrayList<Integer> dicesKept, Board board) {
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
    //Retourne le score de l'IA 
    default int score (ScoreSheet scoreSheet) {
        return scoreSheet.scoreTotal();
    }

    default int calcComb (int dicesMissing) {
        //Calcule la probabilité d'une Combinaison d'arriver
        return dicesMissing*1/6 ;
    }

    //Renvoie les dés qui sont à reroll (utilisée pour les calculs de probabilité de combinaisons)
    default int dicesMissing (ArrayList<Integer> array) {
        int cmp = 0;
        for (Integer i=0; i<5; i++) {
            //Si le dés i n'est pas dans la liste de séquence ou de same, alors on ajoute un dés manquant
            if (!array.contains(i)) {
                cmp++;
            }
        }
        return cmp;
    }

    //Retourne les combinaisons qui ne sont pas encore sacrifiées ou activées 
    default HashMap<Combination, Integer> freeCombination (ScoreSheet scoreSheet, Board board) {

        // hashMap contenant toutes le combinaison 
        // qui ne sont pas encore validé ou sacrifié
        // avec leurs pcoeff
        var freeComb = new HashMap<Combination, Integer>();
        
        // toutes les combi
		List<Combination> combinations = List.of(new Chance(), new ThreeOfAKind(), new FourOfAKind(), new FullHouse(), new SmallStraight(), new LargeStraight(), new Yahtzee());

        for (var combinaison : combinations){
            if (!(scoreSheet.isValidate(combinaison)) && !(scoreSheet.isSacrified(combinaison))) {
                freeComb.put(combinaison, combinaison.coefficient(board, dicesMissing(isSequential(scoreSheet, board))));
            }
        }
        
        return freeComb;
    }

    //On cherche les dés qui forment une séquence dans le board
    //On vérifie qu'il reste des combinaisons utilisant les séquences
    //Cherche des séquences
    //S'il y a plusieurs séquences, on les ajoute à la liste aussi 
    default ArrayList<Integer> isSequential(ScoreSheet scoreSheet, Board board) {
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

    }

    //On cherche les dés qui forment une séquence de dés identiques, pour cela on vérifie qu'il reste des combinaisons de type identiques, s'il n'y en a pas on retourne la liste vide
    //Ensuite on vérifie selon leur occurence s'ils se répètent et on les ajoute ou non à la liste qu'on renvoie 
    default ArrayList<Integer> isSame (Board board, ScoreSheet scoreSheet) {
        ArrayList<Integer> lst = new ArrayList<Integer>();
        if (checkCombSame(scoreSheet)) {
            var lstocc = board.occurence();
            for (int i = 0 ; i<5; i++) {
                if (lstocc.get(i)>1) {
                    lst.add(Integer.valueOf(i));
                }
            }
        }
        return lst;

    }

    //Vérifie s'il reste des combinaisons de type identique/Same
    default boolean checkCombSame (ScoreSheet scoreSheet) {
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

    //Cherche le coeff le plus élevé dans free combinations, puis retourner la combinaison si elle est dans libre and valide
    default Combination highestScore (ScoreSheet scoreSheet, Board board) {
        var combinations = freeCombination(scoreSheet, board);
        var maxScore = 0;
        Combination maxComb = new Chance();
        for (Combination comb : combinations.keySet()) {
            if (combinations.get(comb)>maxScore) {
                maxComb=comb;
            }
        }
        return maxComb;
    }


    //Trouve le coefficient le plus élevé
    default Combination findCoeffMax (ScoreSheet scoreSheet, Board board) {
        // renvoie la combinaison qui a le plus de chance d'etre réalisable
        Integer max = 0;
        Combination maxC = new Chance();
        var free = freeCombination(scoreSheet, board);
        for (var c : free.keySet()) {
            if (free.get(c)>=max) {
                max=free.get(c);
                maxC = c;
            }
        }
        return maxC;
    }

    //Permet de savoir combien de combinaison de type séquentielles ne sont pas encore validées ni sacrifiées
    default int howManySeq (ScoreSheet scoreSheet, Board board) {
        int countSeq = 0;
        var combS = new SmallStraight();
        if (freeCombination(scoreSheet, board).containsKey(combS)) {
            countSeq ++;
        }
        var combL = new LargeStraight();
        if (freeCombination(scoreSheet, board).containsKey(combL)) {
            countSeq ++;
        }
        return countSeq;
    }

    //Permet de savoir combien de combinaison de type identiques ne sont pas encore validées ou sacrifiées
    default int howManySame (ScoreSheet scoreSheet, Board board) {
        int countSame = 0;
        var freeComb = freeCombination(scoreSheet, board);
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

}
