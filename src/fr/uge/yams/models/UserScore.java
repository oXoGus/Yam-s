package fr.uge.yams.models;

import java.util.Objects;

public record UserScore(String username, int score) implements Comparable<UserScore>{
    public UserScore {
        Objects.requireNonNull(username);

        if (score < 0){
            throw new IllegalArgumentException();
        }
    }

    // pour classer les user par ordre décroissant en fonction de leurs score
    @Override
    public int compareTo(UserScore o) {
        Objects.requireNonNull(o);
        return o.score() - score();
    }

    public String getUsername(){
        return username;
    }

    public Integer getScore(){
        return Integer.valueOf(score);
    }
}
