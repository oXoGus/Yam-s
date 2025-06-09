package fr.uge.yams.models;

import javafx.scene.Node;

public interface GameElement {

    GameElement reroll();
    
    int value();

    Node shape();

    default int rank(){
        return -1;
    }

    default Suit suit(){
        return null;
    }
}