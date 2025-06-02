package fr.uge.yams.combinations;

import fr.uge.yams.Board;

public interface Combination {

    boolean isValid(Board board);

    int score(Board board);

    String code();

    String toString(String state, String score);
}
