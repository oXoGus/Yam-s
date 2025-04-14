package fr.uge.yams;

import java.util.ArrayList;

public class NormalGame implements Game {
    private final ArrayList<Player> players;
    
    public NormalGame(int numPlayer) {
        players = new ArrayList<>();

        // initialisation de tout les joueur 
        for (int i = 0; i < numPlayer; i++){
            System.out.println("Welcome, player " + (i + 1) + ", please enter your name.");
            players.add(new Player());
        }
    }

    @Override
    public void playRounds(){
        // déroulement d'une partie normal
        for (var roundCounter = 0; roundCounter < 7; roundCounter++) {
            System.out.println("Welcome in round " + (roundCounter + 1));
            
            // chaque joueur va jouer son tour 
            for (var player : players){
                player.playRound();
            }
        }
    }
    
    @Override
    public void endScreen(){
        
    }
}
