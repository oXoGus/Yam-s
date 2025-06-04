package fr.uge.yams.models;

import java.util.Objects;

import fr.uge.yams.combinations.Combination;

public record CombinationInfo(Combination combination, String state, Integer score) {
    
    public CombinationInfo {
        Objects.requireNonNull(combination);
        Objects.requireNonNull(state);
        Objects.requireNonNull(score);

        if (!state.equals("sacrified") && !state.equals("not chosen yet") && !state.equals("validated")){
            throw new IllegalArgumentException();
        }
    }

    public String getCombination(){
        return combination.toString();
    }

    public String getState(){
        return state.equals("not chosen yet") ? "" : state;
    }

    public String getScoreDisplay(){
        return state.equals("validated") ? score.toString() : "";
    }
}
