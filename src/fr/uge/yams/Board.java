package fr.uge.yams;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

import javafx.scene.Node;
import javafx.scene.shape.Shape;

public interface Board {
    void rerollAll();
    
    List<GameElement> getElementsList();

	List<Integer> elementsMaxOcc();

	void reroll(Collection<Integer> positions);
	
	default List<Integer> maxSizeLst(ArrayList<Set<Integer>> allSeq){
		Objects.requireNonNull(allSeq);

		var maxSeq = allSeq.get(0); 
		for (var seq : allSeq){
			if (seq.size() > maxSeq.size()){
				maxSeq = seq;
			}
		}
		return List.copyOf(maxSeq);
	}

	List<Integer> elementsFormingSeq();

	TreeMap<Integer, Integer> uniqueValAndGameElementIndexSorted();

	int sum();

	List<Integer> occurence();

	default boolean isAllSameSuit(){
		return false;
	}

    default int maxOcc(){
		return Collections.max(occurence());
	}

	default boolean isBoardCard(){
		return false;
	}

	List<Node> allGameElementShapes();

	List<Node> gameElementShapes(Collection<Integer> positions);
}
