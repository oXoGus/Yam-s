package fr.uge.yams;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

// on représentes les cartes en fonction de leurs numéros
// ♥ : 1 pour l'as jusqu'a 13 pour le roi 
// ♦ : 14 pour l'as jusqu'a 26 pour le roi
// ♣ : 27 pour l'as jusqu'a 39 pour le roi
// ♠ : 40 pour l'as jusqu'a 53 pour le roi
public record Card(int value) implements GameElement {

    // la couleur de la carte est la valeur de la liste a l'index de la value() / 13 
    private final static List<Suit> suits = List.of(Suit.HEART, Suit.DIAMOND, Suit.CLUB, Suit.SPADE);

    // meme chose pour les numéros des cartes
    private final static List<String> numbers = List.of("Ace","2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King");

    public Card {
        if (value < 1 || value > 53){
            throw new IllegalArgumentException();
        }
    }

    public Card(){
        this(new Random().nextInt(53) + 1);
    }

    @Override
    public Card reroll(){
        return new Card();
    }

    
    public Suit suit(){

        // couleur de la carte
        return suits.get(value/13);
    }

    public int rank(){

        // calcul du n° de la carte 
        return value%13 == 0 ? 13 : value%13;
    }

    // pour afficher la carte
	@Override
    public Node shape() {
		try {
			// on récupère la liste des shapes des cartes 
			AnchorPane cards = FXMLLoader.load(getClass().getResource("/fr/uge/yams/views/cards.fxml"));
			
			// les cartes sont dans l'ordre
			return cards.getChildren().get(value - 1);

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
