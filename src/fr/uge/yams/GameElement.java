package fr.uge.yams;

import javafx.scene.Node;

public interface GameElement {

    GameElement reroll();
    
    int value();

    Node shape();
}