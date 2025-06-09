package fr.uge.yams.views;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;



public class Yams extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			//On charge l'interface du menu
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/uge/yams/views/menu.fxml"));
			Parent root = loader.load();

			// on met la taille de la fenêtre pour qu'elle prenne tout l'écran
			Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
			primaryStage.setX(screenBounds.getMinX());
			primaryStage.setY(screenBounds.getMinY());
			primaryStage.setWidth(screenBounds.getWidth());
			primaryStage.setHeight(screenBounds.getHeight());

			// pour empécher le redimensionnement
			primaryStage.setResizable(false);

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
