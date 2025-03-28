package fr.uge.yams.solo;

public record ThreeOfAKind() implements Combination {

	@Override
	public int score(Board board) {

		return 15;
	}

	@Override
	public String toString() {

		// afficher le score associé
		return "Three of A Kind";
	}

}
