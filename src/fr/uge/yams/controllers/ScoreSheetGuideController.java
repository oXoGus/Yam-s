package fr.uge.yams.controllers;


import java.util.List;
import java.util.Objects;

import fr.uge.yams.models.CombinationInfo;
import fr.uge.yams.models.User;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class ScoreSheetGuideController {

    @FXML
    private VBox mainContainer;
    @FXML
    private Label titleLabel;

    @FXML
    private TableView<CombinationInfo> comboTable;
    @FXML
    private TableColumn<CombinationInfo, String> comboColumn;
    @FXML
    private TableColumn<CombinationInfo, String> statusColumn;
    @FXML
    private TableColumn<CombinationInfo, String> howToObtainColumn;
    @FXML
    private TableColumn<CombinationInfo, String> scoreColumn;

    private User user;

    @FXML
    public void initialize(){
        comboColumn.setCellValueFactory(new PropertyValueFactory<>("combination"));

        statusColumn.setCellValueFactory(new PropertyValueFactory<>("state"));
        
        howToObtainColumn.setCellValueFactory(new PropertyValueFactory<>("howToObtain"));

        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("scoreInfo"));

        comboTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        // on met a jour le tableau dès l'utilisateur clique sur la fenêtre
        mainContainer.setOnMouseClicked(event -> update());
    }

    public void setUser(User user){
        Objects.requireNonNull(user);

        this.user = user;

        // on met le nom de l'utilisateur 
        titleLabel.setText(user.username()+"'s Score Sheet");
        
        // on remplis le tableau
        update();

        // on définit la taille du tableau en fonction du nombre délémént dedans pour ne pas afficher de lignes vides
        comboTable.setFixedCellSize(25);

        // on multiplie la taille d'une row par le nombre de row
        comboTable.prefHeightProperty().bind(Bindings.size(comboTable.getItems()).multiply(comboTable.getFixedCellSize()).add(28));

        // on recharge toutes les info du tableau toutes les deux secondes
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> update()));
        
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play(); 
    }

    public void update(){
        
        // on récupère les info du model 
        List<CombinationInfo> scoreSheetInfo = user.scoreSheet();
        
        var data = FXCollections.observableArrayList(scoreSheetInfo);
        
        // on ajoutes les lignes 
        comboTable.setItems(data);
    }
}
