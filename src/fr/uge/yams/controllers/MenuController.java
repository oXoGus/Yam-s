package fr.uge.yams.controllers;

import java.io.IOException;
import java.util.Optional;

import fr.uge.yams.models.Game;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

public class MenuController {
	private Game actualGame; 
    
    @FXML
    private Button soloButton;

    @FXML
    private Button duelButton;

    @FXML
    private Button duelIAButton;

    @FXML
    private Button customButton;

    public void startSoloGame(ActionEvent e) {
        actualGame = new Game(1);
        
        try {
        	loadInterface(e);
        } catch (IOException ev) {
        	ev.printStackTrace();
        }
    }
    
  

    public void startMultiplePlayersGame(ActionEvent e) {

        int numPlayer = askPlayers();

        // l'utilisateur a appuyé sur cancel 
        if (numPlayer == -1){
            return;
        }
        actualGame = new Game(numPlayer);
        try {
        	loadInterface(e);
        } catch (IOException ev) {
        	ev.printStackTrace();
        }
    }
    public void startDuelIAGame(ActionEvent e) {
        actualGame = new Game(1, 1);
        try {
        	loadInterface(e);
        } catch (IOException ev) {
        	ev.printStackTrace();
        }
    }
    public void startCustomGame(ActionEvent e) {

        // pour gérer l'annulation du joueur qui a qu'on détecte dans les fonctions ask
        int numPlayer = askPlayers();
        if (numPlayer == -1){
            return;
        }

        int numAI = askAIs();
         if (numAI == -1){
            return;
        }

        // l'utilisateur a appuyé sur cancel 
        
        actualGame = new Game(numPlayer, numAI);

        try {
        	loadInterface(e);
        } catch (IOException ev) {
        	ev.printStackTrace();
        }
    }

    //Va demander au joueur le nombre de joueurs dans une partie custom ou multiplayer
    private int askPlayers() {
        //La valeur par défault est 1
        TextInputDialog dialog = new TextInputDialog("1");
        dialog.setHeaderText("Entrez le nombre de joueurs");

        Optional<String> result;
        int nb;
        //Tant que la valeur récupérée est null, qu'elle ne contient pas que des chiffres et que la valeur vaut 0, on redemande
        do {
            result = dialog.showAndWait();
            if (result.isPresent() && result.get().matches("\\d+")) {
                nb = Integer.parseInt(result.get());
            } else if (result.isEmpty()){
                // le joueur a appuyé sur cancel 
                return -1;
            } else {
                nb = 0;
            }
        } while (nb == 0);

        return nb;
    }

    //Dans une partie Custom, demande le nombre d'IA
    private int askAIs() {
        TextInputDialog dialog = new TextInputDialog("1");
        dialog.setHeaderText("Entrez le nombre d'IA");

        Optional<String> result;
        int nb;
        
        //Tant que la valeur récupérée est null, qu'elle ne contient pas que des chiffres et que la valeur vaut 0, on redemande
        do {
            result = dialog.showAndWait();
            if (result.isPresent() && result.get().matches("\\d+")) {
                nb = Integer.parseInt(result.get());
            }else if (result.isEmpty()){
                // le joueur a appuyé sur cancel 
                return -1;
            } else {
                nb = 0;
            }
        } while (nb == 0);
        return nb;
    }
    
    
    //Nous permet de charger l'interface jeu après le click sur un bouton
    //On utilise la même interface de base pour tous les types de game
    //En fonction on ajoute des players dans l'affichage etc... 
    @FXML
    private void loadInterface (ActionEvent event) throws IOException {
    	
        //On charge l'interface du jeu
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/uge/yams/views/GameView.fxml"));
    	Parent root = loader.load();

        // on ajoute la game au controleur
        GameController gameController = loader.getController();
        gameController.setGame(actualGame);

        //On va chercher la fenetre de l'evenement
    	Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    	stage.setScene(new Scene(root));
    	stage.show();
    }

    @FXML
    public void initialize() {
        soloButton.setOnAction(e -> startSoloGame(e));
        duelButton.setOnAction(e -> startMultiplePlayersGame(e));
        duelIAButton.setOnAction(e -> startDuelIAGame(e));
        customButton.setOnAction(e -> startCustomGame(e));
    }
}