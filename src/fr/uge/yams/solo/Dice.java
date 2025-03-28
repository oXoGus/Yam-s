package fr.uge.yams.solo;

import java.util.Random;

public record Dice(int value) {

	public Dice {
		if (value > 6 || value < 1) {
			throw new IllegalArgumentException();
		}
	}

	public Dice() {
		this(new Random().nextInt(5) + 1);
	}

	public Dice reroll() {
		return new Dice();
	}

	@Override
	public String toString() {
		return "-------\n" + "|  " + value + "  |\n" + "-------\n";
	}

}
