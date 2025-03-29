package fr.uge.yams.solo;

public interface Combination {
	
	// methode de verification
	default int score(Board board) {
		return 0;
	}

}
