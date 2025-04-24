package fr.uge.yams;

import java.util.ArrayList;
import java.util.Collections;

public class NormalGame implements Game {
    private final ArrayList<User> users;
    
    public NormalGame(int numPlayer) {

        if (numPlayer < 0){
            throw new IllegalArgumentException();
        }
        users = new ArrayList<>();

        // initialisation de tout les joueur 
        for (int i = 0; i < numPlayer; i++){
            System.out.println("Welcome, user " + (i + 1) + ", please enter your name.");
            users.add(new Player());
        }
    }

    public NormalGame(int numPlayer, int numAI) {
        if (numPlayer < 0 || numAI < 0 || (numPlayer == 0 && numAI == 0)){
            throw new IllegalArgumentException();
        }
        users = new ArrayList<>();

        // initialisation de tout les joueur 
        for (int i = 0; i < numPlayer; i++){
            System.out.println("Welcome, user " + (i + 1) + ", please enter your name.");
            users.add(new Player());
        }

        // initialisation des IA
        for (int i = 0; i < numAI; i++){
            System.out.println("AI #" + (i + 1));
            users.add(AI.chooseAI(i + 1));
        }
        
    }

    


    @Override
    public void playRounds(){
        // déroulement d'une partie normal
        for (var roundCounter = 0; roundCounter < 7; roundCounter++) {
            System.out.println("Welcome in round " + (roundCounter + 1));
            
            // chaque joueur va jouer son tour 
            for (var user : users){
                user.playRound();
            }
        }
    }

    public int lenMaxPlayerRanking(){
        var maxLen = Integer.toString(users.size()).length();
        // si le titre de la colonne est plus grand que le maxLen
        return "rank".length() > maxLen ? "rank".length() : maxLen;
    }

    public int lenMaxUserName(){
        int maxLen = 0;
        for (var user : users){
            if (user.lenUserName() > maxLen){
                maxLen = user.lenUserName();
            }
        }
        return "user".length() > maxLen ? "user".length() :maxLen;
    }

    public int lenMaxScore(){
        int maxLen = 0;
        for (var user : users){
            if (user.lenScore() > maxLen){
                maxLen = user.lenScore();
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
		res += " | user";
		
		for (int i = 0; i < lenMaxUserName() - "user".length(); i++){
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
        Collections.sort(users);

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

        int userRanking = 1;
        for (var user : users){
            sb.append(user.result(userRanking, lenMaxPlayerRanking, lenMaxUserName, lenMaxScore));
            userRanking++;
        }
        sb.append(br);

        System.out.println(sb.toString());
    }
}
