package fr.uge.yams.models;

import java.util.Collection;
import java.util.List;

import javafx.scene.Node;

public interface User {
    UserScore score();

    List<CombinationInfo> scoreSheet();

    String username();

    void rerollAll();

    void reroll(Collection<Integer> pos);
    
    List<Node> boardShapes();

    List<Node> boardShapes(Collection<Integer> pos);

	boolean isWithCards();
    
    String tryAddCombination(Combination combination);

    String trySacrifyCombination(Combination combination);

    String testCombination(Combination combination);

    boolean isCombinationPossible();

    void reset();

    default boolean isAI(){
        return true;
    }

    // null par défaut pour la classe Player
    default Combination goalCombination(){
        return null;
    }

    default List<Integer> gameElementToReroll(){
        return null;
    }

    default void lastChoice(){
        return;
    }
}

