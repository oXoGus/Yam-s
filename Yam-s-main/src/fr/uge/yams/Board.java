package fr.uge.yams;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;


public class Board {

	private final ArrayList<Dice> fiveDice;

	public Board() {
		fiveDice = new ArrayList<>();

		for (var i = 1; i <= 5; i++) {
			fiveDice.add(new Dice());
		}
	}

	public void rerollAllDice(){
		for (var i = 0; i < 5; i++) {
			fiveDice.set(i, new Dice());
		}
	}

	public List<Dice> fiveDice () {
		return List.copyOf(fiveDice);
	}

	// pour les test
	public Board(List<Integer> lm) {
		fiveDice = new ArrayList<>();

		for (var i = 1; i <= 5; i++) {
			fiveDice.add(new Dice(lm.get(i - 1)));
		}
	}

	public List<Integer> dicesMaxOcc(){
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
			builder.append(fiveDice.get(i-1).toString());
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
	public void reroll(Set<Integer> positions) {
		Objects.requireNonNull(positions);
		for (var pos : positions) {
			if (pos<1 || pos>5) {
				throw new IllegalArgumentException();
			}
			fiveDice.set(pos - 1, new Dice());
		}
		
	}

	// somme de tout les dés pour le calcul du score
	public int sum(){
		int s = 0;
		for (var dice : fiveDice){
			s += dice.value();
		}
		return s;
	}

	// revoi un tableau contenant 
	public List<Integer> occurence(){
		// on créer un tableau qui contiendra le nombre 
		// d'occurence de dés de valeur i a l'index i
		ArrayList<Integer> occLst = new ArrayList<Integer>(List.of(0, 0, 0, 0, 0, 0, 0));
		
		for (var dice : fiveDice){
			occLst.set(dice.value(), occLst.get(dice.value()) + 1);
		}
		return List.copyOf(occLst);
	}

	public int maxOcc(){
		return Collections.max(occurence());
	}

	public static void main(String[] args) {

		var board = new Board(List.of(1, 1, 3, 4, 5));
		System.out.println(board);

		System.out.println(board);
	}

}
