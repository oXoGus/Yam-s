package fr.uge.yams.views;

import java.util.Objects;
import java.util.Optional;

import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

public class Views {
    
    public static String askUsername(String defaultUsername){
		Objects.requireNonNull(defaultUsername);
		
		// on met la valeur par défaut
		TextInputDialog dialog = new TextInputDialog(defaultUsername);
		dialog.setTitle("choose your name");
        dialog.setHeaderText("Welcome " + defaultUsername + ", enter your username");

		Optional<String> res;

		// tant que le joueur n'a pas valider sont pseudo
		res = dialog.showAndWait();
		
		// si l'utilisateur a appuyé sur cancel
		if (res.isEmpty()){
			return null;
		}
		
		return res.get();
	}

	public static String askAIType(int numAI){
		
		// comme le TextInputDialog
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("Choose the AI type");
		dialog.setHeaderText("Select the type of the AI#" + numAI);
		
		// on et les bouton ok et cancel
		ButtonType okButtonType = new ButtonType("OK", ButtonData.OK_DONE);
   		dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

		// les choix 
		RadioButton option1 = new RadioButton("Risky AI");
		RadioButton option2 = new RadioButton("Safe AI");
		RadioButton option3 = new RadioButton("Random AI");

		ToggleGroup group = new ToggleGroup();
		option1.setToggleGroup(group);
		option2.setToggleGroup(group);
		option3.setToggleGroup(group);

		group.selectToggle(option1);

		VBox radioBtnContainer = new VBox(10);
		radioBtnContainer.getChildren().addAll(option1, option2, option3);
		dialog.getDialogPane().setContent(radioBtnContainer);

		// récup les choix le l'user
		var res = dialog.showAndWait();

		if (res.isPresent()){
			
			// si l'user a bien appuyé sur ok
			if (res.get().equals(okButtonType)){
				Toggle selected = group.getSelectedToggle();
			
				// si l'user a bien selectionné une option 
				if (selected != null){

					// on renvoie le texte associé a l'option 
					return ((RadioButton) selected).getText();
				}
			}
		}

		// si annulé 
		return null;
	}


	public static String askBoardType(){
		
		// comme le TextInputDialog
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setTitle("Board Type");
		dialog.setHeaderText("You want to play with : ");
		
		// on et les bouton ok et cancel
		ButtonType okButtonType = new ButtonType("OK", ButtonData.OK_DONE);
   		dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

		// les choix 
		RadioButton option1 = new RadioButton("Dices");
		RadioButton option2 = new RadioButton("Cards");

		ToggleGroup group = new ToggleGroup();
		option1.setToggleGroup(group);
		option2.setToggleGroup(group);

		group.selectToggle(option1);

		VBox radioBtnContainer = new VBox(10);
		radioBtnContainer.getChildren().addAll(option1, option2);
		dialog.getDialogPane().setContent(radioBtnContainer);

		// récup les choix le l'user
		var res = dialog.showAndWait();

		if (res.isPresent()){
			
			// si l'user a bien appuyé sur ok
			if (res.get().equals(okButtonType)){
				Toggle selected = group.getSelectedToggle();
			
				// si l'user a bien selectionné une option 
				if (selected != null){

					// on renvoie le texte associé a l'option 
					return ((RadioButton) selected).getText();
				}
			}
		}

		// si annulé 
		return null;
	}

    
}
