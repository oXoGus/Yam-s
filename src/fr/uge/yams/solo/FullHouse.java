package fr.uge.yams.solo;

public record FullHouse() implements Combination {

	@Override
	public int score(Board board) {

		return 25;
	}

	@Override
	public String toString() {

		return "Full House";
	}

}
