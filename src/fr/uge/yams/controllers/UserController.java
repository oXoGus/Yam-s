package fr.uge.yams.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.beans.Observable;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;

import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Stack;

import fr.uge.yams.Board;
import fr.uge.yams.GameElement;
import fr.uge.yams.User;
import fr.uge.yams.combinations.Combination;
import fr.uge.yams.models.CombinationInfo;
import fr.uge.yams.models.UserScore;


public class UserController {

    private User user;

    // pour changer de joueur
    private GameController gameController;

    // liste contenant tout les éléments de jeu qui sont séléctionné pour le réroll
    private ArrayList<Integer> gameElementsSelectedToReroll;

    @FXML
    private TableView<CombinationInfo> comboTable;
    @FXML
    private TableColumn<CombinationInfo, String> comboColumn;
    @FXML
    private TableColumn<CombinationInfo, String> statusColumn;
    @FXML
    private TableColumn<CombinationInfo, String> scoreColumn;

    // Tableau de score
    @FXML
    private TableView<UserScore> scoreTable;
    @FXML
    private TableColumn<UserScore, String> playerColumn;
    @FXML
    private TableColumn<UserScore, Integer> playerScoreColumn;
    @FXML
    private TableColumn<UserScore, Integer> playerRankColumn;

    @FXML
    private AnchorPane gameElementContainer;

    @FXML
    private HBox combinationContainer;

    @FXML
    private Button rollButton;
    @FXML
    private Button validateButton;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label rerollLabel;

    @FXML
    private Label validateOrSacrifyCombinationLabel;

    @FXML
    private Button menuBtn;

    @FXML
    private Button openCombinationGuideBtn;

    @FXML
    private Label roundLabel;

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

        // on met les boutons pour selectionner les différentes combinaisons 
        setBtnCombinations();
    }

    public void setGameController(GameController gameController){
        Objects.requireNonNull(gameController);
        
        this.gameController = gameController;

        // on charge les info du tableau des scores
        updateScoreBoard();

        // on affiche le n° du round
        roundLabel.setText("Round #" + gameController.numRound() + "/" + gameController.maxRound());
    }

    public void setBtnCombinations(){
        
        // on récup la liste des combinaison de la partie
        List<CombinationInfo> scoreSheetInfo = user.scoreSheet();

        // pour chaque combi
        for (int i = 0; i < scoreSheetInfo.size(); i++){
            var combi = scoreSheetInfo.get(i);

            Button combinationBtn = new Button(combi.combination().toString());
            
            // on ajoute le event handler pour ajouter ou sacrifier un combinaison
            combinationBtn.setOnAction(event -> handleCombinationSelected(event));

            // et on ajoute l'id pour pouvoire retrouver la combi correspondante 
            combinationBtn.setId(Integer.toString(i));

            combinationContainer.getChildren().add(combinationBtn);
        }

        // par défaut désactivé 
        desactivateAllCombinationBtn();
    }

    public void handleCombinationSelected(Event event){
        // on récup le bouton cliqué 
        Button combinationSelected = (Button) event.getSource();
        
        // on récup l'id pour trouver la combi correspondante
        var combinationId = Integer.parseInt(combinationSelected.getId());
        

        // on trouve la combi correspondante 
        List<CombinationInfo> combinations = user.scoreSheet();
        Combination combinationChosen = combinations.get(combinationId).combination();
        String message;
        // si une combination est possible 
        if (user.isCombinationPossible()){
            
            // on la valide si possible
            message = user.tryAddCombination(combinationChosen);
        } else {
            
            // on essaye de la sacrifier
            message = user.trySacrifyCombination(combinationChosen);
        }

        // si on a pas pu l'ajouter ou la sacrifié 
        if (message != null){
            
            // on affiche ce message sous forme d'alerte
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("bad combination");
            alert.setHeaderText(message);
            
            alert.showAndWait();
        } else {
            
            // on passe au prochain joueur
            gameController.nextUserTurn();
        }
    }

    public void desactivateAllCombinationBtn(){
        
        // on récup la liste des boutons
        for (var btn : combinationContainer.getChildren()){
            
            // obligé d'utiliser le cast pour convertir 
            btn = (Button) btn;
            btn.setDisable(true);
        }
    }

    public void activateAllCombinationBtn(){
        
        // on récup la liste des boutons
        for (var btn : combinationContainer.getChildren()){
            
            // obligé d'utiliser le cast pour convertir 
            btn = (Button) btn;
            btn.setDisable(false);
        }
    }

    public void updateScoreSheet(){
        Objects.requireNonNull(user);

        // on récupère les info du model 
        List<CombinationInfo> scoreSheetInfo = user.scoreSheet();
        
        var data = FXCollections.observableArrayList(scoreSheetInfo);
        
        // on ajoutes les lignes 
        comboTable.setItems(data);

        // on définit la taille du tableau en fonction du nombre délémént dedans pour ne pas afficher de lignes vides
        comboTable.setFixedCellSize(25);

        // on multiplie la taille d'une row par le nombre de row
        comboTable.prefHeightProperty().bind(Bindings.size(comboTable.getItems()).multiply(comboTable.getFixedCellSize()).add(28));
    }


    // avec rien en argument on affiche tout les éléments
    public void displayBoard(){

        if (!gameElementsSelectedToReroll.isEmpty()){
            displayElementReroll();   
            return;         
        }
        

        // on récupère les représentations du board 
        List<Node> shapes = user.boardShapes();

        // si on utilise des cartes
        if (user.isWithCards()){
            
            // on les place en rangé 
            for (var shape : shapes){
                
                
            }
            
        } else {

            // sinon on les dispose aléatoirements
            int pos = 1;
            for (var shape : shapes){
                placeGameElementRandom(pos, shape);
                pos++;
            }
        }
    }


    public void displayElementReroll(){
        // on supp les élément qui sont séléctionné
        deleteShapesReroll();

        // et on affiche les nouvreau élément reroll
        var shapesReroll = user.boardShapes(gameElementsSelectedToReroll);
        
        if (user.isWithCards()){
            
        } else {

            // on affiche uniquement les shapes 
            int i = 0;
            for (var shape : shapesReroll){
                int pos = gameElementsSelectedToReroll.get(i);
                placeGameElementRandom(pos, shape);
                i++;
            }
        }
    }

    public void placeGameElementRandom(int pos, Node shape){

        double padding = 75;

        // coords aléatoire
        double x = new Random().nextDouble(padding, gameElementContainer.getWidth() - padding);
        double y = new Random().nextDouble(padding, gameElementContainer.getHeight() - padding);
        double theta =  new Random().nextDouble(360);

        placeGameElement(pos, shape, x, y, theta);

        // il ne faut pas que les éléments se supperposent
        while (isGameElementOverlaping(shape)){
            
            // on supprime l'élément de jeux et on recommence 
            deleteGameElement(pos);

            System.out.println(gameElementContainer.getChildren());
            // coords aléatoire
            x = new Random().nextDouble(padding, gameElementContainer.getWidth() - padding);
            y = new Random().nextDouble(padding, gameElementContainer.getHeight() - padding);
            theta =  new Random().nextDouble(360);
            
            placeGameElement(pos, shape, x, y, theta);
        } 

        System.out.println("pos " + pos + " placed");
    }

    public void deleteShapesReroll(){
        for (var pos : gameElementsSelectedToReroll){
            deleteGameElement(pos);
        }
    }

    public boolean isGameElementOverlaping(Node shapeAdded){
        Objects.requireNonNull(shapeAdded);
        
        // on récup tout les autres élément Shapes
        var shapes = gameElementContainer.getChildren();

        for (var shape : shapes){
            
            // si la shape n'est pas celle qui vien d'être ajouté
            if (!shape.equals(shapeAdded)){
                
                // on les compare tout les deux
                
                // on récup les bound depuis le parent puisque 
                // si on se mettait en local cela ne prendrait pas en compte la rotation 
 
                Bounds boundsShape = shape.localToScene(shape.getBoundsInLocal());
                Bounds boundsShapeAdded = shapeAdded.localToScene(shapeAdded.getBoundsInLocal());

                // on fait l'intersection 
                if (boundsShape.intersects(boundsShapeAdded)){
                    System.out.println("colision");
                    return true;
                }
            }
        }

        return false;
    }

    public void deleteGameElement(int pos){
        // on récup la shape avec son id
        var shapes = gameElementContainer.getChildren();
        
        // on cherche la shape avec le bon id et on la supp
        for (var shape : shapes){
            if (shape.getId().equals(Integer.toString(pos))){
                gameElementContainer.getChildren().remove(shape);
                
                System.out.println("pos " + pos + " deleted");
                return;
            }
        }        
    }

    public void placeGameElement(int pos, Node shape, double x, double y, double theta){
        Objects.requireNonNull(shape);

        // les coords
        shape.setLayoutX(Double.valueOf(x));
        shape.setLayoutY(Double.valueOf(y));

        // l'angle thêta
        shape.setRotate(Double.valueOf(theta));

        // on ajoute l'id pour pouvoir le sélectionner plus tard pour les reroll
        shape.setId(Integer.toString(pos));

        gameElementContainer.getChildren().add(shape);

        // on ajoute l'évent handler sur l'élément pour le selectionner pour le reroll
        shape.setOnMouseClicked(event -> HandleGameElementClicked(event));
    }

    public void HandleGameElementClicked(MouseEvent e){

        Node shape = (Node) e.getSource();

        // on récup la pos de l'élément qui se trouve être son ID
        int pos = Integer.parseInt(shape.getId());

        // plus d'interaction avec les éléemnts si on n'a plus de reroll dispo
        if (rerollsLeft <= 0){
            return;
        }

        System.out.println("pos = " + pos);
        
        // si l'élément est déja selectionné
        if (gameElementsSelectedToReroll.contains(pos)){

            // on le retire des éléments a reroll
            gameElementsSelectedToReroll.remove(gameElementsSelectedToReroll.indexOf(pos));
            
            // on retire l'effet 
            shape.setEffect(null);
        } else {
            
            // sinon on ajoute le pos a la liste
            gameElementsSelectedToReroll.add(pos);


            // et on ajoute l'effet glowy autour de la shape
            Glow glow = new Glow(0.8);
            
            DropShadow dropShadow = new DropShadow();
            dropShadow.setRadius(20);
            dropShadow.setOffsetX(0);
            dropShadow.setOffsetY(0);
            dropShadow.setColor(Color.WHITE);

            glow.setInput(dropShadow);

            shape.setCache(true);
            shape.setCacheHint(CacheHint.QUALITY);

            shape.setEffect(glow);
        }

        // on désactive le le bouton de reroll si il n'y a aucun élément de selectionné 
        rollButton.setDisable(gameElementsSelectedToReroll.isEmpty());
    }    

    @FXML
    private void initialize() {

        // la dernière colonne prend le reste de l'espace dispo
        comboTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        // Initialisation des colonnes des tableaux
        comboColumn.setCellValueFactory(new PropertyValueFactory<>("combination"));

        statusColumn.setCellValueFactory(new PropertyValueFactory<>("state"));

        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("scoreDisplay"));

        gameElementsSelectedToReroll = new ArrayList<>();

        // on initialise le tableau des score
        playerRankColumn.setCellValueFactory(new PropertyValueFactory<>("rank"));
        playerColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        playerScoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        
        // la dernière colonne prend le reste de l'espace dispo
        scoreTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        // pour revenir au menu
        menuBtn.setOnAction(event -> backToTheMenu(event));


    }

    public void updateScoreBoard(){
        // récup les data du scoreboard
        scoreTable.setItems(gameController.scoreBoardData());


        // on définit la taille du tableau en fonction du nombre délémént dedans pour ne pas afficher de lignes vides
        scoreTable.setFixedCellSize(25);

        // on multiplie la taille d'une row par le nombre de row
        scoreTable.prefHeightProperty().bind(Bindings.size(scoreTable.getItems()).multiply(scoreTable.getFixedCellSize()).add(28));    
        
        
    }

    @FXML
    private void backToTheMenu(ActionEvent event){

        // on demande à l'user une confirmation pour revenir au menu et donc quitter la partie
        Alert confirmation = new Alert(AlertType.CONFIRMATION);
        confirmation.setTitle("Return to Menu");
        confirmation.setContentText("Are you sure you want to quit the game ?");

        // on montre l'alert et on attend le retour de l'utilisateur 
        Optional<ButtonType> res = confirmation.showAndWait();

        // si l'utilisateur a bien confirmer vouloir revenir au menu
        if (res.isPresent() && res.get() == ButtonType.OK){
            
            // on retourn au menu
            gameController.backToTheMenu();
        }
    }
    
    @FXML
    private void onRollButtonClick(ActionEvent event) {
        rerollsLeft--;

        // premier lancer
        if (rerollsLeft == 2){
            // on relance tout les dés
            user.rerollAll();

            // on affiche le board
            displayBoard();

            // on passe le texte du bouton a relancer
            rollButton.setText("Reroll");

            // et on affiche le nombre de reroll restantes
            rerollLabel.setVisible(true);

            // on ajoute un indication pour sélectionner les dés si il n'y en a 
            rerollLabel.setText(rerollsLeft + "/2 reroll left" + (gameController.numRound() == 1 ? ", click on the dices that you want to reroll" : "" ));

            // on désactive par défaut le bouton reroll comme rien n'est selectionné$
            rollButton.setDisable(true);

            

        } else if (rerollsLeft >= 0) {
        
            // on reroll les éléments de jeu 
            user.reroll(gameElementsSelectedToReroll);

            // et on affiche les éléments qui on été relancé
            displayBoard();

            // et en dernier on reset la liste des pos
            gameElementsSelectedToReroll.clear();

            if (rerollsLeft == 0){
                rerollLabel.setText("no reroll left");
                rollButton.setDisable(true);
            } else {
                rerollLabel.setText(rerollsLeft + "/2 reroll left");
            }

            // on désactive par défaut le bouton reroll comme rien n'est selectionné$
            rollButton.setDisable(true);
        } 
        // gestion du message d'indication pour les combinations
        if (user.isCombinationPossible()){
            validateOrSacrifyCombinationLabel.setText("click on the buttons below to add a combination");

            activateAllCombinationBtn();
        } else if (rerollsLeft > 0){
            // pas de combi possible mais l'user peut encore relancer
            validateOrSacrifyCombinationLabel.setText("no combination possible, reroll to make one");
            
            // on désactive tout les boutons
            desactivateAllCombinationBtn();
        } else {
            validateOrSacrifyCombinationLabel.setText("no combination possible, you must sacrify one");
            
            // on les actives pour le sacrifice
            activateAllCombinationBtn();
        }
    }

    public void openCombinationGuide(){
        // ouvre une nouvelle fenêtre contenant le tableau des différentes combinaisons
        
        try {
            Stage popupGuide = new Stage();

            // pour que cette fenêtre ne bloque pas la principale 
            // et qu'elle soit indépendante
            popupGuide.initModality(Modality.NONE); 

            popupGuide.setTitle("Combination Guide");

            //On charge l'interface du jeu
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/uge/yams/views/CombinationGuide.fxml"));
            Parent root = loader.load();

            ScoreSheetGuideController scoreSheetGuideController = loader.getController();
            
            // on lui donne l'user pour récup les état des diffférentes combi
            scoreSheetGuideController.setUser(user);

            popupGuide.setScene(new Scene(root));
            popupGuide.show();
        } catch (IOException e){
            e.printStackTrace();
        }
        
        
    }
}