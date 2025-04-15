package fr.uge.yams;

import java.util.ArrayList;

public class NormalGame implements Game {
    private final ArrayList<Player> players;
    private int lenMaxPlayerRanking=1;
    private int lenMaxScore=1;
    private int lenMaxUserName=0;
    
    public NormalGame(int numPlayer) {
        players = new ArrayList<>();

        if (numPlayer>9) {
            if (numPlayer>99) {
                lenMaxPlayerRanking=3;
            }
            lenMaxPlayerRanking=2;
        }
        
        // initialisation de tout les joueur 
        for (int i = 0; i < numPlayer; i++){
            System.out.println("Welcome, player " + (i + 1) + ", please enter your name.");
            var p =new Player();
            players.add(p);
            if (p.lenMaxUserName()>lenMaxUserName) {
                lenMaxUserName=p.lenMaxUserName();
            }
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
            //A la fin d'un tour on update les ranking en fonction du score des joueurs par leur rang dans la liste
            ranking();
            lenMaxScoreUpdate();
        }
    }

    public void lenMaxScoreUpdate() {
        if (players.getFirst().score()>9 && lenMaxScore==1) {
            lenMaxScore=2;
            if (players.getFirst().score()>99 && lenMaxScore==2) {
                lenMaxScore=3;
            }
        } 
    }
    
    @Override
    public void endScreen(){
        System.out.println("End of the game !\nHere are the results of this game :\n");
        for (var player : players) {
            System.out.println(player.result(players.indexOf(player), lenMaxPlayerRanking, lenMaxUserName, lenMaxScore));
        }
    }

    //Besoin d'une liste classée ou d'un nb de ranking qui est update à chaque tour 
    public void ranking () {
        for (int i = 0; i<players.size()-1; i++) {
            if (players.get(i).score() < players.get(i+1).score()) {
                var p1 = players.get(i);
                var p2 = players.get(i+1);
                players.set(i, p2);
                players.set(i+1, p1);
            }
        }
    }
}
