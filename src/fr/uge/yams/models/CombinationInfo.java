package fr.uge.yams.models;

import java.util.Objects;

import fr.uge.yams.combinations.Combination;

public record CombinationInfo(Combination combination, String state, String howToObtain, Integer score, String scoreInfo) {
    
    public CombinationInfo {
        Objects.requireNonNull(combination);
        Objects.requireNonNull(state);
        Objects.requireNonNull(score);
        Objects.requireNonNull(howToObtain);
        Objects.requireNonNull(scoreInfo);

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

    public String getHowToObtain(){
        return howToObtain;
    }

    public String getScoreInfo(){
        // pour la fiche d'aide

        // on affiche pas le "sum of all dices" si on a validé cette combi
        return state.equals("validated") ? score.toString() : scoreInfo;
    }
}
