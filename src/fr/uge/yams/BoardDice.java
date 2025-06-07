package fr.uge.yams;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

import javafx.scene.Node;

public class BoardDice implements Board{
    private final ArrayList<Dice> fiveDice;

	public BoardDice() {
		fiveDice = new ArrayList<>();

		for (var i = 1; i <= 5; i++) {
			fiveDice.add(new Dice());
		}
	}

    @Override
    public void rerollAll(){
		for (var i = 0; i < 5; i++) {
			fiveDice.set(i, new Dice());
		}
	}

    @Override
    public List<GameElement> getElementsList () {
		return List.copyOf(fiveDice);
	}

    // pour les test
    public BoardDice(List<Integer> lm) {
		Objects.requireNonNull(lm);

		fiveDice = new ArrayList<>();

		for (var i = 1; i <= 5; i++) {
			fiveDice.add(new Dice(lm.get(i - 1)));
		}
	}

    public List<Integer> elementsMaxOcc(){
		// on met l'index de tous les dés ayant la valeur maxOcc
		var dices = new ArrayList<Integer>();
		
		// valeur de dés qui sont le plus dans board
		var diceValMaxOcc = occurence().indexOf(maxOcc());

		for (int i = 0; i < 5; i++){
			var diceVal = fiveDice.get(i).value();
			
			if (diceVal == diceValMaxOcc){
				dices.add(i);
			}
		}
		return List.copyOf(dices);
	}

    @Override
	public String toString() {
		var builder = new StringBuilder();
		for (var i = 1; i <= 5; i++) {
			builder.append("-------  ");
		}
		builder.append("\n");
		for (var i = 1; i <= 5; i++) {
			builder.append(fiveDice.get(i-1));
			builder.append("  ");
		}
		builder.append("\n");
		for (var i = 1; i <= 5; i++) {
			builder.append("-------  ");
		}
		builder.append("\n");
		

		return builder.toString();
	}

    // faire un reroll avec comme argument la liste des pos des dés a modifs
    @Override
	public void reroll(Collection<Integer> positions) {
		Objects.requireNonNull(positions);

		for (var pos : positions) {
			if (pos<1 || pos>5) {
				throw new IllegalArgumentException();
			}
			fiveDice.set(pos - 1, new Dice());
		}
		
	}

    	// revoie la liste contenant tout les pos des dés qui forme une séquence
    public List<Integer> elementsFormingSeq(){

		// liste contenant tout les séquence de dés sous forme de set
		var allSeq = new ArrayList<Set<Integer>>();
		
		var seq = new HashSet<Integer>();
		
		// map triée par valeur des dés avec la premier index qui a cette valeur
		var valIndexSorted = uniqueValAndGameElementIndexSorted();

		var diceIndexLst = new ArrayList<Integer>(valIndexSorted.values());
		var diceValLst = new ArrayList<Integer>(valIndexSorted.keySet());
		
		// on met la première pos
		seq.add(diceIndexLst.get(0) + 1);
		
		var prevVal = diceValLst.get(0);


		// pour chaque valeur, index
		for (int i = 1; i < diceIndexLst.size(); i++) {
			
			var diceVal = diceValLst.get(i);
			var diceIndex = diceIndexLst.get(i);

			// si diceVal ne forme pas une seq avec prevVal
			// + cas particulier ou ou il y a 'un trou' dans la seq
			// exemple
			// -------  -------  -------  -------  -------  
			// |  5  |  |  1  |  |  5  |  |  2  |  |  3  |  
			// -------  -------  -------  -------  ------- 
			// on doit ajouter un des 5 dans la seq 
			if (!(diceVal == prevVal + 1 || diceVal == prevVal + 2)){
				// on arrete la séquence

				// la sauvegarde si elle est plus grande que 1
				
				allSeq.add(new HashSet<Integer>(seq));

				// reset
				seq = new HashSet<Integer>();
			}

			// on ajoute la pos
			seq.add(diceIndex + 1);

			prevVal = diceVal;
		}
		// on oublie pas la dernière seq
		allSeq.add(seq);

		// on renvoie la seq la plus grande
		return maxSizeLst(allSeq);
    }

    @Override
    public TreeMap<Integer, Integer> uniqueValAndGameElementIndexSorted(){
		var res = new TreeMap<Integer, Integer>();
		
		for (int i = 0; i < 5; i++){
			var diceVal = fiveDice.get(i).value();
			
			// on ajoute uniquement les premier index pour une valeur
			if (!res.containsKey(diceVal)){
				res.put(diceVal, i);
			}
		}
		return res;
	}

    // somme de tout les dés pour le calcul du score
	@Override
    public int sum(){
		int s = 0;
		for (var dice : fiveDice){
			s += dice.value();
		}
		return s;
	}

    @Override
    public List<Integer> occurence(){
		
		// on créer un tableau qui contiendra le nombre 
		// d'occurence de dés de valeur i a l'index i
		ArrayList<Integer> occLst = new ArrayList<Integer>(List.of(0, 0, 0, 0, 0, 0, 0));
		
		for (var dice : fiveDice){
			occLst.set(dice.value(), occLst.get(dice.value()) + 1);
		}
		return List.copyOf(occLst);
	}
    
    @Override
    public int maxOcc(){
		return Collections.max(occurence());
	}

	@Override
	public List<Node> allGameElementShapes(){
		var res = new ArrayList<Node>();
		for (var dice : fiveDice){
			res.add(dice.shape());
		}
		return List.copyOf(res);
	}

	@Override
	public List<Node> gameElementShapes(Collection<Integer> positions) {
		Objects.requireNonNull(positions);

		var res = new ArrayList<Node>();
		for (var pos : positions){

			if (pos < 1 || pos > 5){
				throw new IllegalArgumentException();
			}

			res.add(fiveDice.get(pos - 1).shape());
		}
		return List.copyOf(res);
	}

    public static void main(String[] args) {

		var board = new BoardDice(List.of(5, 1, 5, 2, 3));
		//board.rerollAllDice();
		System.out.println(board);
		System.out.println(board.elementsFormingSeq());

	}
}
