package fr.uge.yams.models;

import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class Model {
	
	//--- Code exécuté lorsque l'événement survient ----
	public void insert(TextArea textArea, CheckBox checkBox){
		if (checkBox.isSelected()){
			textArea.appendText("B");
		}
		else {
			textArea.appendText("A");
		}
	}
	
	//--- Code exécuté lorsque l'événement survient ----
	public void delete(TextArea textArea){
		textArea.setDisable(false);
		textArea.deletePreviousChar();
		textArea.setDisable(true);
	}
	
	//--- Code exécuté lorsque l'événement survient ----
	public void quit(Stage stage) {
		stage.close();
	}
	
}