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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

import fr.uge.yams.models.CombinationInfo;
import fr.uge.yams.models.User;
import fr.uge.yams.models.UserScore;


public class AIController implements UserController{

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
    private Label goalCombinationLabel;

    @FXML
    private Button menuBtn;

    @FXML
    private Button openCombinationGuideBtn;

    @FXML
    private Label roundLabel;

    @FXML
    private HBox cardsContainer;

    private int rerollsLeft = 3;

    // pour arrêter le thread
    private volatile boolean stopAIThread = false;

    private volatile boolean pauseThread = false;

    // nombre de reroll après avoir fait le premier lancer
    private final int numRerollTotal = 2;


    // on injecte l'user depuis le game controller
    @Override
    public void setUser(User user){
        Objects.requireNonNull(user);

        this.user = user;

        // on remplis le tableau des scoreSheet
        updateScoreSheet();

        

        // on affiche le nom de l'user
        usernameLabel.setText(user.username() + "'s turn");

        // tant que l'user n'a pas lancer ces dés on cache le nombre de relance restantes
        rerollLabel.setVisible(false);

        // on affiche les différentes combinaisons 
        setBtnCombinations();

        // ne peut pas intéragir avec les boutons quand c'est l'IA qui joue 
        desactivateAllCombinationBtn();
        
        rollButton.setDisable(true);

        // on retire le container pour les cartes si on utilise des dés
        if (!user.isWithCards()){
            gameElementContainer.getChildren().remove(cardsContainer);
        } else {
            rollButton.setText("Draw");
        }

        // l'IA fait son tour 
        // on attend que la transition pour afficher le tour 
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(e -> AITask());
        pause.play();
    }

    

    @Override
    public void setGameController(GameController gameController){
        Objects.requireNonNull(gameController);
        
        this.gameController = gameController;

        // on charge les info du tableau des scores
        updateScoreBoard();

        // on affiche le n° du round
        roundLabel.setText("Round #" + gameController.numRound() + "/" + gameController.maxRound());
    }

    public void AITask(){
        // le round de l'IA

        // on est obligé de mettre des condtion d'arret partout puisque si l'utilisateur retourn au menu 
        // le ce code continura d'être executé et provoquera des erreurs

        // on utilise une task pour ne pas bloquer l'action du joueur
        Task<Void> AITask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                // l'écart entre les actions
                int delay = 1000;

                while (pauseThread){
                    Thread.sleep(100);
                }

                if (stopAIThread){
                    return null;
                }

                // on lance les dés 
                Platform.runLater(() -> roll());
                Thread.sleep(delay);

                while (pauseThread){
                    Thread.sleep(100);
                }

                if (stopAIThread){
                    return null;
                }

                // on essaye d'ajouter la combi
                if (user.tryAddCombination(user.goalCombination()) == null  && !stopAIThread){

                    // on a réussi a valider la combi
                    Platform.runLater(() -> setValidateGoalCombinnationSuccess());
                    
                    // on laisse deux seconde pour que l'user puisse voir que la combi a été validé
                    Thread.sleep(delay*2);  
        
                    // et on passe au tour suivant 
                    Platform.runLater(() -> gameController.nextUserTurn());
                    return null;
                }

                Thread.sleep(delay);

                for (int i = 0; i < numRerollTotal; i++){

                    while (pauseThread){
                        Thread.sleep(100);
                    }

                    if (stopAIThread){
                        return null;
                    }    

                    // on clique sur les élément manquant pour la goal comb
                    gameElementsSelectedToReroll = new ArrayList<>(user.gameElementToReroll());

                    for (var pos : gameElementsSelectedToReroll){

                        while (pauseThread){
                            Thread.sleep(100);
                        }
                        
                        if (stopAIThread){
                            return null;
                        }
        
                        Platform.runLater(() -> clickGameElement(pos));
                        Thread.sleep(delay/2);
                    }

                    while (pauseThread){
                        Thread.sleep(100);
                    }

                    if (stopAIThread){
                        return null;
                    }    

                    // on reroll
                    Platform.runLater(() -> roll());
                    Thread.sleep(delay);

                    // on essaye d'ajouter la combi
                    if (user.tryAddCombination(user.goalCombination()) == null){

                        while (pauseThread){
                            Thread.sleep(100);
                        }

                        if (stopAIThread){
                            return null;
                        }

                        // on a réussi a valider la combi
                        Platform.runLater(() -> setValidateGoalCombinnationSuccess());
                        
                        // on laisse deux seconde pour que l'user puisse voir que la combi a été validé
                        Thread.sleep(delay*2);

                        while (pauseThread){
                            Thread.sleep(100);
                        }

                        if (stopAIThread){
                            return null;
                        }        
            
                        // et on passe au tour suivant 
                        Platform.runLater(() -> gameController.nextUserTurn());
                        return null;
                    }
                    
                    Thread.sleep(delay);
                        
                    }

                while (pauseThread){
                    Thread.sleep(100);
                }

                if (stopAIThread){
                    return null;
                }

                // on a toujours pas pu ajouté la combi goal
                // on en sacrifie une ou si on a quand meme réussi a faire une ou plusieurs 
                // combi autre que la combi goal, on valide la meilleur 
                Platform.runLater(() -> user.lastChoice());

                if (stopAIThread){
                    return null;
                }
                
                Platform.runLater(() -> updateScoreSheet());
                Thread.sleep(delay*2);

                while (pauseThread){
                    Thread.sleep(100);
                }

                if (stopAIThread){
                    return null;
                }

                Platform.runLater(() -> gameController.nextUserTurn());
                
                return null;
            }
        };

        Thread AIThread = new Thread(AITask);
        AIThread.setDaemon(true);
        AIThread.start();
    }

    public void setBtnCombinations(){
        
        // on récup la liste des combinaison de la partie
        List<CombinationInfo> scoreSheetInfo = user.scoreSheet();

        // pour chaque combi
        for (int i = 0; i < scoreSheetInfo.size(); i++){
            var combi = scoreSheetInfo.get(i);

            Button combinationBtn = new Button(combi.combination().toString());
         
            // et on ajoute l'id pour pouvoire retrouver la combi correspondante 
            combinationBtn.setId(Integer.toString(i));

            combinationContainer.getChildren().add(combinationBtn);
        }

        // l'utilisateur ne peux pas utiliser les bouton quand c'est l'IA qui joue 
        desactivateAllCombinationBtn();
    }

    public void lastChoice(){
        user.lastChoice();
        
    }

    public void setValidateGoalCombinnationSuccess(){
        updateScoreSheet();
        updateScoreBoard();
    }

    public void displayGoalCombination(){
        goalCombinationLabel.setText("Goal Combination : " + user.goalCombination().toString());
    }




    public void desactivateAllCombinationBtn(){
        
        // on récup la liste des boutons
        for (var btn : combinationContainer.getChildren()){
            
            // obligé d'utiliser le cast pour convertir 
            btn = (Button) btn;
            btn.setDisable(true);
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
            int pos = 1;
            for (var shape : shapes){
                placeCard(pos, shape);
                pos++;
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

    public void placeCard(int pos, Node shape){
        Objects.requireNonNull(shape);

        if (!isPosAlreadyPlaced(pos)){
            shape.setId(Integer.toString(pos));

            // on fait tjr correspondre l'odres des cartes avec leur pos
            if (pos - 1 > cardsContainer.getChildren().size()){
                cardsContainer.getChildren().add(shape);
            } else {
                cardsContainer.getChildren().add(pos - 1, shape);
            }
        }
    }


    public void displayElementReroll(){
        // on supp les élément qui sont séléctionné
        deleteShapesReroll();

        // et on affiche les nouvreau élément reroll
        var shapesReroll = user.boardShapes(gameElementsSelectedToReroll);

        
        if (user.isWithCards()){
            
            // on les place en rangé 
            int i = 0;
            for (var shape : shapesReroll){
                int pos = gameElementsSelectedToReroll.get(i);
                placeCard(pos, shape);
                i++;
            }
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
        Objects.requireNonNull(shape);

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

            // tant que l'élément n'est pas supprimé 
            while (!isGameElementDeleted(pos)){
                deleteGameElement(pos);
            }

            // coords aléatoire
            x = new Random().nextDouble(gameElementContainer.getWidth() - padding);
            y = new Random().nextDouble(gameElementContainer.getHeight() - padding);
            theta =  new Random().nextDouble(360);
            
            placeGameElement(pos, shape, x, y, theta);
        } 

    }

    public void deleteShapesReroll(){
        for (var pos : gameElementsSelectedToReroll){
            deleteGameElement(pos);
        }
    }

    public boolean isGameElementDeleted(int pos){
        for (var shape : List.copyOf(gameElementContainer.getChildren())){
            if (shape.getId() != null && shape.getId().equals(String.valueOf(pos))){
                return false;
            }
        }
        return true;
    }

    public boolean isPosAlreadyPlaced(int pos){
        for (var shape : user.isWithCards() ? List.copyOf(cardsContainer.getChildren()) : List.copyOf(gameElementContainer.getChildren())){
            if (shape.getId() != null && shape.getId().equals(String.valueOf(pos))){
                return true;
            }
        }
        return false;
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
                    return true;
                }
            }
        }

        return false;
    }

    public void deleteGameElement(int pos){

        if (pos < 1 || pos > 5){
            throw new IllegalArgumentException();
        }

        if (user.isWithCards()){

            // on récup la shape avec son id
            var shapes = cardsContainer.getChildren();
            
            // on cherche la shape avec le bon id et on la supp
            for (var shape : shapes){
                if (shape.getId().equals(Integer.toString(pos))){
                    cardsContainer.getChildren().remove(shape);
                    
                    return;
                }
            } 
        } 

        // on récup la shape avec son id
        var shapes = List.copyOf(gameElementContainer.getChildren());

        
        // on cherche la shape avec le bon id et on la supp
        for (var shape : shapes){
            if (shape.getId().equals(Integer.toString(pos))){
                gameElementContainer.getChildren().remove(shape);
                
                return;
            }
        }        
    }

    public void placeGameElement(int pos, Node shape, double x, double y, double theta){
        Objects.requireNonNull(shape);

        if (pos < 1 || pos > 5){
            throw new IllegalArgumentException();
        }

        // les coords
        shape.setLayoutX(Double.valueOf(x));
        shape.setLayoutY(Double.valueOf(y));

        // l'angle thêta
        shape.setRotate(Double.valueOf(theta));

        // on ajoute l'id pour pouvoir le sélectionner plus tard pour les reroll
        shape.setId(Integer.toString(pos));

        if (!isPosAlreadyPlaced(pos)){
            gameElementContainer.getChildren().add(shape);
        }
    }



    public void clickGameElement(int pos){
        if (pos < 1 || pos > 5){
            throw new IllegalArgumentException();
        }

        // on récup l'élément qui correspond a pos
        Node shapeClicked = null;
        for (var shape : user.isWithCards() ? cardsContainer.getChildren() : gameElementContainer.getChildren()){
            
            if (shape.getId().equals(Integer.toString(pos))){
                shapeClicked = shape;
                break;
            }
        }

        if (shapeClicked == null){
            throw new IllegalStateException();
        }


        // et on ajoute l'effet glowy autour de la shape
        Glow glow = new Glow(0.8);
        
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(20);
        dropShadow.setOffsetX(0);
        dropShadow.setOffsetY(0);
        dropShadow.setColor(Color.WHITE);

        glow.setInput(dropShadow);

        shapeClicked.setCache(true);
        shapeClicked.setCacheHint(CacheHint.QUALITY);

        shapeClicked.setEffect(glow);
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
        menuBtn.setOnAction(event -> {
            pauseThread = true;
            backToTheMenu(event);
        });


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
            
            // pour stoper le thread de l'IA 
            stopAIThread = true;

            // on retourn au menu
            gameController.backToTheMenu();

            
        }

        pauseThread = false;
    }

    public void roll(){
        rerollsLeft--;

        String reroll = user.isWithCards() ? "redraw" : "reroll";

        // premier lancer
        if (rerollsLeft == 2){
            // on relance tout les dés
            user.rerollAll();

            // on affiche le board
            displayBoard();

            // on passe le texte du bouton a relancer
            rollButton.setText(user.isWithCards() ? "Redraw" : "Reroll");

            // et on affiche le nombre de reroll restantes
            rerollLabel.setVisible(true);

            // on ajoute un indication pour sélectionner les dés si il n'y en a 
            rerollLabel.setText(rerollsLeft + "/2 " + reroll + " left" );

            // on affiche la goal combi
            displayGoalCombination();

        } else if (rerollsLeft >= 0) {
        
            // on reroll les éléments de jeu 
            user.reroll(gameElementsSelectedToReroll);

            // et on affiche les éléments qui on été relancé
            displayBoard();

            // et en dernier on reset la liste des pos
            gameElementsSelectedToReroll.clear();

            if (rerollsLeft == 0){
                rerollLabel.setText("no "+ reroll +" left");
            } else {
                rerollLabel.setText(rerollsLeft + "/2 "+ reroll +" left");
            }

            // on affiche la goal combi
            displayGoalCombination();
        } 

    }
    
    @FXML
    private void onRollButtonClick(ActionEvent event) {}

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