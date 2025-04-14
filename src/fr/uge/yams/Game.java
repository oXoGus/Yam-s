package fr.uge.yams;

public interface Game {
    // le deéroulement de la partie peut etre 
    // différent por chaque mode de jeu
    void playRounds();

    void endScreen();
}
