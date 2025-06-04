package fr.uge.yams.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import fr.uge.yams.User;
import fr.uge.yams.models.Game;
import fr.uge.yams.models.UserScore;


public class GameController {

    private Game game;

    // le border pane principale
    @FXML
    private BorderPane mainContainer;

    // Partie droite - Tableau de score
    @FXML
    private TableView<UserScore> scoreTable;
    @FXML
    private TableColumn<UserScore, String> playerColumn;
    @FXML
    private TableColumn<UserScore, Integer> scoreColumn;

    // on inject la game depuis le controlleur du menu
    public void setGame(Game game){
        Objects.requireNonNull(game);

        this.game = game;

        // on remplis le tableau
        updateScoreBoard();

        // et on commence la partie
        startGame();
    }

    public void startGame(){
        Objects.requireNonNull(game);

        // on charge le 1er user de la liste
        startUserTurn(game.users().getFirst());
    }

    public void startUserTurn(User user){
        Objects.requireNonNull(user);

        try {
            //On charge l'interface de jeu pour l'utilisateur
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/uge/yams/views/UserView.fxml"));
            Parent userView = loader.load();

            // on récupère le controlleur et on inject le model de l'user
            UserController userController = loader.getController();
            userController.setUser(user);

            // on affiche la vue de l'utilisateur 
            mainContainer.setCenter(userView);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateScoreBoard(){
        Objects.requireNonNull(game);

        // on rrécupère les donné du model
        List<UserScore> scoreBoardData = game.scoresBoard();

        var data = FXCollections.observableArrayList(scoreBoardData);

        // on ajoute toutes les lignes
        scoreTable.setItems(data);
    }

    @FXML
    private void initialize() {

        // on initialise le tableau des score
        playerColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
    }

    
}