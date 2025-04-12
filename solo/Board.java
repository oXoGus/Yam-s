package fr.uge.yams.solo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Board {

	private final ArrayList<Dice> fiveDice = new ArrayList<Dice>();

	public Board() {
		for (var i = 1; i <= 5; i++) {
			fiveDice.add(new Dice());
		}
	}

	public ArrayList<Dice> fiveDice () {
		return fiveDice;
	}
	// pour les test
	public Board(List<Integer> lm) {
		for (var i = 1; i <= 5; i++) {
			fiveDice.add(new Dice(lm.get(i - 1)));
		}
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
	public void reroll(ArrayList<Integer> positions) {
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

		var lst = new ArrayList<Integer>(2);

		var board = new Board(List.of(1, 1, 3, 4, 5));
		System.out.println(board);
		board.reroll(lst);
		System.out.println(board);
	}

}
