package fr.uge.yams;

import java.util.Objects;

// classe contenant des méthodes static utiles pour tous les modes de jeu
public class Games {
    public static boolean isInteger (String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

    public static boolean isAllInt (String[] choices) {
		for (var nb : choices) {
			if (!isInteger(nb)) {
				return false;
			}
		}
		return true;
	}

    public static boolean userDiceCheck (String[] choices) {
		Objects.requireNonNull(choices);

		for (var nb : choices) {
			if (Integer.valueOf(nb)>5 || Integer.valueOf(nb)<0) {
				return false;
			}
		}
		return true;
	}

    public static Combination parseCombination(String combinationName) {

		// ne pas lancer une exception mais reposer la question si default
		return switch (combinationName) {
            case "T" -> new ThreeOfAKind();
            case "Fu" -> new FullHouse();
            case "Fo" -> new FourOfAKind();
            case "L" -> new LargeStraight();
            case "S" -> new SmallStraight();
            case "C" -> new Chance();
            case "Y" -> new Yahtzee();
            default -> null; // null par défaut pour la gestion des erreurs 
		};
	}
}
