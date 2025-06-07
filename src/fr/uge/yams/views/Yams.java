package fr.uge.yams.views;

import fr.uge.yams.controllers.GameController;
import fr.uge.yams.controllers.MenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;



public class Yams extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			 //On charge l'interface du menu
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/uge/yams/views/menu.fxml"));
			Parent root = loader.load();

			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Yams");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
