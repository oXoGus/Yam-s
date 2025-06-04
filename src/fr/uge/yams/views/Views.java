package fr.uge.yams.views;

import java.util.Optional;

import javafx.scene.control.TextInputDialog;

public class Views {
    
    public static String askUsername(String defaultUsername){
		
		// on met la valeur par défaut
		TextInputDialog dialog = new TextInputDialog(defaultUsername);
        dialog.setHeaderText("Welcome " + defaultUsername + ", enter your username");

		Optional<String> res;

		// tant que le joueur n'a pas valider sont pseudo
		do {
			res = dialog.showAndWait();
		} while (!res.isPresent());
		
		return res.get();
	}

    
}
