package fr.uge.yams.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;

import java.util.List;
import java.util.Objects;

import fr.uge.yams.Board;
import fr.uge.yams.GameElement;
import fr.uge.yams.User;
import fr.uge.yams.models.CombinationInfo;


public class UserController {

    private User user;

    // Partie gauche - Tableau de combinaisons
    @FXML
    private TableView<CombinationInfo> comboTable;
    @FXML
    private TableColumn<CombinationInfo, String> comboColumn;
    @FXML
    private TableColumn<CombinationInfo, String> statusColumn;
    @FXML
    private TableColumn<CombinationInfo, String> scoreColumn;

    // Partie centre - Dés
    @FXML
    private ImageView dice1;
    @FXML
    private ImageView dice2;
    @FXML
    private ImageView dice3;
    @FXML
    private ImageView dice4;
    @FXML
    private ImageView dice5;

    @FXML
    private Button rollButton;
    @FXML
    private Button validateButton;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label rerollLabel;

    private int rerollsLeft = 3;


    // on injecte l'user depuis le game controller
    public void setUser(User user){
        Objects.requireNonNull(user);

        this.user = user;

        // on remplis le tableau des scoreSheet
        updateScoreSheet();

        // on affiche le nom de l'user
        usernameLabel.setText(user.username() + "'s turn");

        // tant que l'user n'a pas lancer ces dés on cache le nombre de relance restantes
        rerollLabel.setVisible(false);
    }

    public void updateScoreSheet(){
        Objects.requireNonNull(user);

        // on récupère les info du model 
        List<CombinationInfo> scoreSheetInfo = user.scoreSheet();
        
        var data = FXCollections.observableArrayList(scoreSheetInfo);
        
        // on ajoutes les lignes 
        comboTable.setItems(data);
    }


    public void displayBoard(){
        
        // on récupère les représentations du board 
        List<Node> shapes = user.boardShapes();

        // si on utilise des cartes
        if (user.isWithCards()){
            
            // on les place en rangé 
            
        } else {
            
            // sinon on les disposent aléatoirements

        }
    }

    public void placeGameElement();

    @FXML
    private void initialize() {

        // Initialisation des colonnes des tableaux
        comboColumn.setCellValueFactory(new PropertyValueFactory<>("combination"));

        statusColumn.setCellValueFactory(new PropertyValueFactory<>("state"));

        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("scoreDisplay"));
    }



    
    @FXML
    private void onRollButtonClick(ActionEvent event) {
        
        // premier lancer
        if (rerollsLeft == 3){
            rerollsLeft--;

            // on relance tout les dés
            user.rerollAll();

            // on récupère le board 
            

            // on passe le texte du bouton a relancer
            rollButton.setText("Reroll");

            // et on affiche le nombre de reroll restantes
            rerollLabel.setVisible(true);
            rerollLabel.setText(rerollsLeft + "/3 reroll left");

        } else if (rerollsLeft > 0) {
            rerollsLeft--;

            user.rerollAll();

        } else {
            rerollLabel.setText("No reroll left");
            rollButton.setDisable(true);
        }
    }

    @FXML
    private void onValidateButtonClick(ActionEvent event) {
        // TODO: Logique de validation de la combinaison choisie
        System.out.println("Validation de la combinaison");
    }

    private void updateRerollLabel() {
        rerollLabel.setText("Relances restantes : " + rerollsLeft + "/3");
    }
}