package fr.uge.yams.models;

import java.util.Objects;

public class UserScore implements Comparable<UserScore>{
    
    // rank n'est pas final puisqu'il sera calculé après l'instanciation dans l'User
    private int rank;
    private final String username;
    private final int score;

    public UserScore (String username, int score) {
        Objects.requireNonNull(username);

        if (score < 0){
            throw new IllegalArgumentException();
        }

        this.username = username;
        this.score = score;
    }

    public void setRank(int rank){
        this.rank = rank;
    }

    // pour classer les user par ordre décroissant en fonction de leurs score
    @Override
    public int compareTo(UserScore o) {
        Objects.requireNonNull(o);
        return o.score - score;
    }

    public String getUsername(){
        return username;
    }

    public Integer getScore(){
        return Integer.valueOf(score);
    }

    public Integer getRank(){
        return Integer.valueOf(rank);
    }
}
