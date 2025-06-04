package fr.uge.yams;

import java.util.List;

import fr.uge.yams.models.CombinationInfo;
import fr.uge.yams.models.UserScore;
import javafx.scene.Node;

public interface User {
    UserScore score();

    List<CombinationInfo> scoreSheet();

    String username();

    void rerollAll();
    
    List<Node> boardShapes();

	boolean isWithCards();
    
}

