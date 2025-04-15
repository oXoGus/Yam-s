package fr.uge.yams;

import java.util.ArrayList;
import java.util.Collections;

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

    public int lenMaxPlayerRanking(){
        var maxLen = Integer.toString(players.size()).length();
        // si le titre de la colonne est plus grand que le maxLen
        return "rank".length() > maxLen ? "rank".length() : maxLen;
    }

    public int lenMaxUserName(){
        int maxLen = 0;
        for (var player : players){
            if (player.lenUserName() > maxLen){
                maxLen = player.lenUserName();
            }
        }
        return "player".length() > maxLen ? "player".length() :maxLen;
    }

    public int lenMaxScore(){
        int maxLen = 0;
        for (var player : players){
            if (player.lenScore() > maxLen){
                maxLen = player.lenScore();
            }
        }
        return "score".length() > maxLen ? "score".length() :maxLen;
    }

    private String makeBr(){
        // lignes horizontales
        String br = "+-";
        for (int i = 0; i < lenMaxPlayerRanking(); i++){
            br += "-";
        }
        br += "-+-";
        for (int i = 0; i < lenMaxUserName(); i++){
            br += "-";
        }
        br += "-+-";
        for (int i = 0; i < lenMaxScore(); i++){
            br += "-";
        }
        br += "-+\n";

        return br;
    }

    private String makeTopLine(){
        // calcule la premiere ligne du tableau de fin
        String res =  "| rank" ;

		// nombre d'espace manquant pour que ce soit aligné 
		for (int i = 0; i < lenMaxPlayerRanking() - "rank".length(); i++){
			res += " ";
		}
		res += " | player";
		
		for (int i = 0; i < lenMaxUserName() - "player".length(); i++){
			res += " ";
		}
		res += " | score";

		for (int i = 0; i < lenMaxScore() - "score".length(); i++){
			res += " ";
		}
		res += " |\n";

		return res;
    }

    
    @Override
    public void endScreen(){

        // les joueurs sont maintenant ordonné du plus grand score au plus petit
        Collections.sort(players);

        var sb = new StringBuilder(); 

        // pour avoir une taille de colone dynamique
        var lenMaxPlayerRanking = lenMaxPlayerRanking();
        var lenMaxUserName = lenMaxUserName();
        var lenMaxScore = lenMaxScore();
        
        // lignes horizontales
        var br = makeBr();
        sb.append(br);
        sb.append(makeTopLine());
        sb.append(br);

        int playerRanking = 1;
        for (var player : players){
            sb.append(player.result(playerRanking, lenMaxPlayerRanking, lenMaxUserName, lenMaxScore));
            playerRanking++;
        }
        sb.append(br);

        System.out.println(sb.toString());
    }
}
