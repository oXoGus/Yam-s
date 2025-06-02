package fr.uge.yams.combinations;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import fr.uge.yams.Board;
import fr.uge.yams.Games;

public class StraightFlush implements Combination {
   
    @Override
	public int score(Board board) {
		Objects.requireNonNull(board);
		return 40;
	}

    @Override
	public String code(){
		// code pour parse les combinisons dans le terminal
		return "SF";
	}

	@Override
	public boolean isValid(Board board) {
        

		Objects.requireNonNull(board);

        // il faut que toutes les cartes soient de meme suit
        if (!board.isAllSameSuit()){
            return false;
        }

        // deux combinaisons possible par couleurs
        // A K Q J 10
        // 5 4 3 2 A

        // occurence() renvoie les éléments sans distinctions des couleurs 
        var occList = board.occurence();

        if (occList.get(1) > 0 && occList.get(2) > 0 && occList.get(3) > 0 && occList.get(4) > 0 && occList.get(5) > 0){
            return true;
        } else if (occList.get(1) > 0 && occList.get(13) > 0 && occList.get(12) > 0 && occList.get(11) > 0 && occList.get(10) > 0) {
            return true;
        }
		return false;
	}

	@Override
	public List<Integer> dicesMissing(Board board) {
		Objects.requireNonNull(board);

		var dicesKept = board.elementsFormingSeq();
		var res = new ArrayList<Integer>();
		
		// on créer la list contenant les position 
		// qui ne sont pas des dicesKept
		for (int i = 1; i < 6; i++){
			if (!dicesKept.contains(i)){
				res.add(i);
			}
		}

		return List.copyOf(res);
	}

	@Override
	public double probability (int numDicesMissing) {
		if (numDicesMissing < 0 || numDicesMissing > 6){
			throw new IllegalArgumentException();
		}

		if (numDicesMissing == 0){
			return 1;
		}
	
		// pour les combi de type seq 
		// le premier dé a numDicesMissing/6 de chance de tomber un dé qui convient
		// le deuxieme dé a (numDicesMissing - 1)/6
		// ...
		// donc factoriel de numDicesMissing/6
		return Games.fact(numDicesMissing)/6.0;
	}
}
