package fr.uge.yams;

import java.util.Collection;
import java.util.List;

import fr.uge.yams.combinations.Combination;
import fr.uge.yams.models.CombinationInfo;
import fr.uge.yams.models.UserScore;
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
}

