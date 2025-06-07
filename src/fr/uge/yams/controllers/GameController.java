package fr.uge.yams.controllers;

import javafx.animation.PauseTransition;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import fr.uge.yams.User;
import fr.uge.yams.models.CombinationInfo;
import fr.uge.yams.models.Game;
import fr.uge.yams.models.UserScore;


public class GameController {

    private Game game;

    // pour revenir au menu
    private MenuController menuController;

    // le border pane principale
    @FXML
    private BorderPane mainContainer;
    @FXML
    private VBox vBoxContainer;
    @FXML
    private TableView<UserScore> scoreTable;
    @FXML
    private TableColumn<UserScore, String> playerColumn;
    @FXML
    private TableColumn<UserScore, Integer> playerScoreColumn;
    @FXML
    private TableColumn<UserScore, Integer> playerRankColumn;

    // on inject la game depuis le controlleur du menu
    public void setGame(Game game){
        Objects.requireNonNull(game);

        this.game = game;

        // et on commence la partie
        startTurn();
    }

    public void backToTheMenu(){
        menuController.backToTheMenu();
    }

    // pour donner les info du scoreboard au controlleur de user car la scoreboard est sur la vue de l'user
    public ObservableList<UserScore> scoreBoardData(){
        Objects.requireNonNull(game);

        // on récupère les donné du model
        List<UserScore> scoreBoardData = game.scoresBoard();

        var data = FXCollections.observableArrayList(scoreBoardData);

        return data;
    }

    public int numRound(){
        return game.numRound();
    }

    public int maxRound(){
        return game.maxRound();
    }

    public void startTurn(){
        Objects.requireNonNull(game);

        // on charge l'user actif 
        User user = game.userTurn(); 
        
        // on récup le num round 
        int numRound = game.numRound();

        // on fait une petite transition ou on affiche
        // le n° du round et le nom du joueur
        
        // on reset l'affichage 
        mainContainer.setCenter(null);

        // on affiche au centre les info du tour
        Label usernameLabel = new Label(user.username() + "'s turn");
        usernameLabel.setTextFill(Paint.valueOf("#ede6d6")); 
        usernameLabel.setFont(new Font("Georgia", 50));

        Label numRoundLabel = new Label("Round #" + numRound + "/" + game.maxRound());
        numRoundLabel.setTextFill(Paint.valueOf("#ede6d6")); 
        numRoundLabel.setFont(new Font("Georgia", 35));

        VBox vBox = new VBox(numRoundLabel, usernameLabel);
        vBox.setAlignment(Pos.CENTER);
        BorderPane.setAlignment(vBox, Pos.CENTER);
        mainContainer.setCenter(vBox);

        // création de la transtion
        PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
        pause.setOnFinished(event -> {
            startUserTurn(user);
        });

        // on commence la pause 
        pause.play();       
    }
    

    public void startUserTurn(User user){
        Objects.requireNonNull(user);

         try {
            //On charge l'interface de jeu pour l'utilisateur
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/uge/yams/views/UserView.fxml"));
            Parent userView = loader.load();

            // on récupère le controlleur et on inject le model de l'user
            UserController userController = loader.getController();
            userController.setGameController(this);
            userController.setUser(user);
            
            // on affiche la vue de l'utilisateur 
            mainContainer.setCenter(userView);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void nextUserTurn(){
        
        // si la partie est termnié 
        if (game.isGameEnded()){        
            // TODO  affichage écran de fin
            return;
        }

        // on passe au tour suivant 
        game.nextTurn();

        // et on lance le tour
        startTurn();
    }

    public void setMenuController(MenuController menuController){
        Objects.requireNonNull(menuController);

        this.menuController = menuController;
    }


    public void endScreen(){
        
        // on remet le tableau des scores
        mainContainer.setCenter(vBoxContainer);

        // on remplis le tableau 
        scoreTable.setItems(scoreBoardData());

        // on définit la taille du tableau en fonction du nombre délémént dedans pour ne pas afficher de lignes vides
        scoreTable.setFixedCellSize(25);

        // on multiplie la taille d'une row par le nombre de row
        scoreTable.prefHeightProperty().bind(Bindings.size(scoreTable.getItems()).multiply(scoreTable.getFixedCellSize()).add(28));    
    }

    @FXML 
    private void initialize(){
        
        // on initialise le tableau des score
        playerRankColumn.setCellValueFactory(new PropertyValueFactory<>("rank"));
        playerColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        playerScoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        
        // la dernière colonne prend le reste de l'espace dispo
        scoreTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        

        // puis on le retire pour laisser place a la vue de l'user
        mainContainer.setCenter(null);

    }


    
}