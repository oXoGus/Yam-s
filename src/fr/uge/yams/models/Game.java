package fr.uge.yams.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.uge.yams.BoardDice;
import fr.uge.yams.User;
import fr.uge.yams.AI.AI;
import fr.uge.yams.combinations.Chance;
import fr.uge.yams.combinations.Combination;
import fr.uge.yams.combinations.FourOfAKind;
import fr.uge.yams.combinations.FullHouse;
import fr.uge.yams.combinations.LargeStraight;
import fr.uge.yams.combinations.SmallStraight;
import fr.uge.yams.combinations.ThreeOfAKind;
import fr.uge.yams.combinations.Yahtzee;
import fr.uge.yams.views.Views;

public class Game {


    // on met la liste des combinaisons en const
    private final static List<Combination> combinationsChosen = List.of(new Chance(), new ThreeOfAKind(), new FourOfAKind(), new FullHouse(), new SmallStraight(), new LargeStraight(), new Yahtzee());
    
    private final ArrayList<User> users;

    // on enrgistre la position du joueur dans la liste de la partie dont c'est le tour 
    private int userTurnIndex = 0;

    // même chose avec le n° du round 
    private int numRound = 1;

    private String gameStatus;

    
    public Game(int numPlayer) {

        if (numPlayer < 0){
            throw new IllegalArgumentException();
        }
        users = new ArrayList<>();

        // initialisation de tout les joueur 
        for (int i = 0; i < numPlayer; i++){
            // on instancie le model 

            // on demande au joueur son username avec son username par défaut
		    String username = Views.askUsername("Player " + (i + 1));

            // si l'utilisateur a annulé 
            if (username == null){
                gameStatus = "Canceled";
                return;
            } else {
                gameStatus = "OK";
            }

            // le joueur a les combinaison définit et utilise le board avec les dices 
            users.add(new Player(username, combinationsChosen, new BoardDice()));
        }
    }

    public Game(int numPlayer, int numAI) {
        if (numPlayer < 0 || numAI < 0 || (numPlayer == 0 && numAI == 0)){
            throw new IllegalArgumentException();
        }
        users = new ArrayList<>();

        // initialisation de tout les joueur 
        for (int i = 0; i < numPlayer; i++){

            // on demande au joueur son username avec son username par défaut
		    String username = Views.askUsername("Player " + (i + 1));

            // si l'utilisateur a annulé 
            if (username == null){
                gameStatus = "canceled";
                return;
            } else {
                gameStatus = "OK";
            }

            users.add(new Player(username, combinationsChosen, new BoardDice()));
        }

        // initialisation des IA
        for (int i = 0; i < numAI; i++){

            // pour le mode de jeu NormalGame on utilise les dés
            users.add(AI.chooseAI(i + 1, combinationsChosen, new BoardDice()));
        }
        
    }

    public boolean isCanceled(){
        return gameStatus.equals("Canceled");
    }

    public User userTurn(){
        // on renvoie le l'utilisateur dont c'est le tour 
        return users.get(userTurnIndex);
    }

    public int numRound(){
        return numRound;
    }

    public boolean isGameEnded(){
        
        // un partie se termine si on a fait un round par combinaison 
        // et l'index du jour actif est le dernier de la liste des joueurs
        return numRound >= combinationsChosen.size() && userTurnIndex == users.size() - 1;
    }

    public void nextTurn(){
        // on modif les champs pour passer au tour suivant
        if (userTurnIndex + 1 < users.size()){
            userTurnIndex++;
        } else {
            // on change de round
            numRound++;
            userTurnIndex = 0;
        }
    }

    public int maxRound(){
        return combinationsChosen.size();
    }

    public List<Combination> combinationsChosen(){
        return combinationsChosen;
    }

    // pour le transmetre au controlleur
    public List<User> users(){
        return List.copyOf(users);
    }


    public List<UserScore> scoresBoard(){
        // on met dans une liste les utilisateur avec leurs scores
        var res = new ArrayList<UserScore>();

        for (var user : users){
            res.add(user.score());
        }

        // puis on la trie pour faire le classement
        Collections.sort(res);

        // on ajoute ensuite les rank de chacun 
        int rank = 1;
        for (var user : res){
            user.setRank(rank);
            rank++;
        }

        return res;
    }
}
