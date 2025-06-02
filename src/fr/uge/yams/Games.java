package fr.uge.yams;

import java.util.Objects;

import fr.uge.yams.combinations.Chance;
import fr.uge.yams.combinations.Combination;
import fr.uge.yams.combinations.FourOfAKind;
import fr.uge.yams.combinations.FullHouse;
import fr.uge.yams.combinations.LargeStraight;
import fr.uge.yams.combinations.SmallStraight;
import fr.uge.yams.combinations.ThreeOfAKind;
import fr.uge.yams.combinations.Yahtzee;

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

	public static int fact(int a){
		int res = 1;
		
		for (int i = 2; i < a; i++){
			res *= i;
		}
		return res;
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

    
}
