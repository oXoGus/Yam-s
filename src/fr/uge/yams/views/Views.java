package fr.uge.yams.views;

import java.util.Optional;

import javafx.scene.control.TextInputDialog;

public class Views {
    
    public static String askUsername(String defaultUsername){
		
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

    
}
