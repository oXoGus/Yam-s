package fr.uge.yams.solo;

public record FullHouse() implements Combination {

	@Override
	public int score(Board board) {

		// verification si l
		return 25;
	}

	@Override
	public String toString() {

		return "Full House";
	}

}
