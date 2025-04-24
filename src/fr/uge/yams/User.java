package fr.uge.yams;

import java.util.Objects;

public interface User extends Comparable<User>{

    public void playRound() ;

    public String result(int playerRanking, int lenMaxPlayerRanking, int lenMaxUserName, int lenMaxScore);

    public int score();

    public int lenUserName();

    public int lenScore();

    // pour classer les user par ordre décroissant en fonction de leurs score
    @Override
    default int compareTo(User o) {
        Objects.requireNonNull(o);
        return o.score() - score();
    }

}
