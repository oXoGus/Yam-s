package fr.uge.yams;

import java.util.Random;

public record Dice(int value) implements GameElement{

	public Dice {
		if (value > 6 || value < 1) {
			throw new IllegalArgumentException();
		}
	}

	public Dice() {
		this(new Random().nextInt(6) + 1);
	}

	public Dice reroll() {
		return new Dice();
	}

	@Override
	public String toString() {
		return "|  " + value + "  |";
	}

}
