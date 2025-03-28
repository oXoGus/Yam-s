package fr.uge.yams.solo;

import java.util.HashMap;
import java.util.Objects;

public class ScoreSheet {

	private final HashMap<Combination, Integer> scoreMap = new HashMap<>();

	public void updateScore(Combination pattern, Board board) {
		// meme chose avec board
		Objects.requireNonNull(pattern);

		if (scoreMap.containsKey(pattern)) {
			throw new IllegalArgumentException("already a score for this combination");
		}
		scoreMap.put(pattern, pattern.score(board));
	}

	public int scoreTotal() {
		return scoreMap.values().stream().mapToInt(Integer::intValue).sum();
	}


	// refaire le toString sous forme de tableau
	@Override
	public String toString() {
		return scoreMap.toString();
	}

}
