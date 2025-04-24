package fr.uge.yams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public interface AI extends User{
    void reroll();
    void playRound();
    List<Integer> probabilityComb();
    void choice();
    
    // constantes
    List<Combination> combinations = List.of(new Chance(), new ThreeOfAKind(), new FourOfAKind(), new FullHouse(), new SmallStraight(), new LargeStraight(), new Yahtzee());
    List<Combination> combSame = List.of(new ThreeOfAKind(), new FourOfAKind(), new FullHouse(), new Yahtzee());
    List<Combination> combSeq = List.of(new SmallStraight(), new LargeStraight());
    

    //Renvoie les combinaisons qui sont libres et qui sont valides par rapport aux dés du board
    default HashMap<Combination, Integer> combinationsFreeAndValid (Board board, ScoreSheet scoreSheet) {
        Objects.requireNonNull(board);
        Objects.requireNonNull(scoreSheet);

        HashMap<Combination, Integer> chosen = new HashMap<Combination, Integer>();

        for (var combination : combinations){
            if (combination.isValid(board) && !(scoreSheet.isValidate(combination)) && !(scoreSheet.isSacrified(combination)) ) {
                chosen.put(combination, combination.score(board));
            }
        }
    
        return chosen;
    }

    //Renvoie true si la combinaison entrée est de type séquentielle 
    default boolean isSeq (Combination comb) {
        Objects.requireNonNull(comb);

        // true si comb est un SmallStraight ou un LargeStraight
        return combSeq.contains(comb);
    }

    // Fait la demande de reroll en renvoyant les dés qui ne sont pas 
    // compris dans la liste donnée 
    // (car on utilise des listes qui contiennent les index des dés à garder )
    default void askReroll(List<Integer> dicesKept, Board board) {
        Objects.requireNonNull(board);
        Objects.requireNonNull(dicesKept);

        Set<Integer> lst = new HashSet<Integer>();
        String s = "";
        for (Integer i = 0; i<5 ; i++) {
            if (!dicesKept.contains(i)) {
                lst.add(i+1);
                s += (i + 1) + " ";
            }
        }
        // affichages des dés reroll par l'IA
        System.out.println(s);

        board.reroll(lst);
    }
    //Retourne le score de l'IA 
    public int score () ;

    default int calcComb (int diceMissing) {
        //Calcule la probabilité d'une Combinaison d'arriver
        return diceMissing*1/6 ;
    }

    //Renvoie les dés qui sont à reroll (utilisée pour les calculs de probabilité de combinaisons)
    default int dicesMissing (List<Integer> array) {
        Objects.requireNonNull(array);

        int cmp = 0;
        for (int i=0; i<5; i++) {
            //Si le dés i n'est pas dans la liste de séquence ou de same, alors on ajoute un dés manquant
            if (!array.contains(i)) {
                cmp++;
            }
        }
        return cmp;
    }

    //Retourne les combinaisons qui ne sont pas encore sacrifiées ou activées 
    default HashMap<Combination, Double> freeCombinations (ScoreSheet scoreSheet, Board board) {
        Objects.requireNonNull(board);
        Objects.requireNonNull(scoreSheet);

        // hashMap contenant toutes le combinaison 
        // qui ne sont pas encore validé ou sacrifié
        // avec leurs pcoeff
        var freeComb = new HashMap<Combination, Double>();
        
        for (var combinaison : combinations){
            if (!(scoreSheet.isValidate(combinaison)) && !(scoreSheet.isSacrified(combinaison))) {
                freeComb.put(combinaison, combinaison.coefficient(board, dicesMissing(diceSeqList(board, scoreSheet))));
                System.out.println(diceSeqList(board, scoreSheet));
                System.out.println(dicesMissing(diceSeqList(board, scoreSheet)));
            }
        }
        
        return freeComb;
    }

    //On cherche les dés qui forment une séquence dans le board
    //On vérifie qu'il reste des combinaisons utilisant les séquences
    //Cherche des séquences
    //S'il y a plusieurs séquences, on les ajoute à la liste aussi 
    default List<Integer> diceSeqList(Board board, ScoreSheet scoreSheet) {
        Objects.requireNonNull(board);
        Objects.requireNonNull(scoreSheet);

        HashSet<Integer> lst = new HashSet<Integer>();
        
        //Si reste des combinaison utilisant des séquences
        if (scoreSheet.isCombinaisonFree(new SmallStraight()) || scoreSheet.isCombinaisonFree(new LargeStraight())) {
            
            var fiveDice = board.fiveDice();

            // on ajoute dans lst les dés dont leurs valeur forme une suite 
            for (int i = 0; i<5; i++) {
                var diceVal = fiveDice.get(i).value();

                if (diceVal < 6 && fiveDice.contains(new Dice(diceVal+1))) {
                    lst.add(i);
                    lst.add(fiveDice.indexOf(new Dice(diceVal+1)));
                }
            }
            return List.copyOf(lst);
        }
        return List.copyOf(new ArrayList<>());

    }

    //On cherche les dés qui forment une séquence de dés identiques, pour cela on vérifie qu'il reste des combinaisons de type identiques, s'il n'y en a pas on retourne la liste vide
    //Ensuite on vérifie selon leur occurence s'ils se répètent et on les ajoute ou non à la liste qu'on renvoie 
    default ArrayList<Integer> diceSameList (Board board, ScoreSheet scoreSheet) {
        Objects.requireNonNull(board);
        Objects.requireNonNull(scoreSheet);

        ArrayList<Integer> lst = new ArrayList<Integer>();

        // si il est encore possible de valider une combinaison de type Same 
        if (isCombSameLeft(scoreSheet)) {
            
            var lstOcc = board.occurence();
            for (int i = 0 ; i<6; i++) {
                if (lstOcc.get(i)>1) {
                    lst.add(Integer.valueOf(i));
                }
            }
            return lst;
        }
        return null;
    }

    // Vérifie s'il reste des combinaisons de type identique/Same
    default boolean isCombSameLeft (ScoreSheet scoreSheet) {
        Objects.requireNonNull(scoreSheet);

        for (var comb : combSame){
            if ((!scoreSheet.isValidate(comb) && !scoreSheet.isSacrified(comb))) {
                return true;
            } 
        }
        return false;
    }

    //Cherche le coeff le plus élevé dans free combinations, puis retourner la combinaison si elle est dans libre and valide
    default Combination highestScore (ScoreSheet scoreSheet, Board board) {
        Objects.requireNonNull(board);
        Objects.requireNonNull(scoreSheet);

        var combinations = freeCombinations(scoreSheet, board);
        System.out.println(combinations);
        Combination maxComb = new Chance();
        double maxCoef = 0;

        // on prend comme valeur par défaut les une clé et sa valeur au hasard
        for (var comb : combinations.entrySet()){
            maxComb = comb.getKey();
            maxCoef = comb.getValue();
            break;
        }

        for (Combination comb : combinations.keySet()) {
            if (combinations.get(comb)>maxCoef) {
                maxComb=comb;
            }
        }
        return maxComb;
    }


    //Trouve le coefficient le plus élevé
    default Combination findCoeffMax (ScoreSheet scoreSheet, Board board) {
        Objects.requireNonNull(board);
        Objects.requireNonNull(scoreSheet);

        // renvoie la combinaison qui a le plus de chance d'etre réalisable
        double max = 0;
        Combination maxC = new Chance();
        var free = freeCombinations(scoreSheet, board);
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
        Objects.requireNonNull(board);
        Objects.requireNonNull(scoreSheet);

        int countSeq = 0;
        var freeComb = freeCombinations(scoreSheet, board);
        for (var comb : combSeq){
            if (freeComb.containsKey(comb)) {
                countSeq ++;
            }
        }
        return countSeq;
    }

    //Permet de savoir combien de combinaison de type identiques ne sont pas encore validées ou sacrifiées
    default int howManySame (ScoreSheet scoreSheet, Board board) {
        Objects.requireNonNull(board);
        Objects.requireNonNull(scoreSheet);
        
        int countSame = 0;
        var freeComb = freeCombinations(scoreSheet, board);
        for (var comb : combSame){
            if (freeComb.containsKey(comb)) {
                countSame ++;
            }
        }
        return countSame;
    }

}
