package fr.uge.yams.models;

import java.util.List;

import fr.uge.yams.models.combinations.Chance;
import fr.uge.yams.models.combinations.Flush;
import fr.uge.yams.models.combinations.FourOfAKind;
import fr.uge.yams.models.combinations.FourOfAKindCard;
import fr.uge.yams.models.combinations.FullHouse;
import fr.uge.yams.models.combinations.FullHouseCard;
import fr.uge.yams.models.combinations.HightCard;
import fr.uge.yams.models.combinations.LargeStraight;
import fr.uge.yams.models.combinations.OnePair;
import fr.uge.yams.models.combinations.RoyalFlush;
import fr.uge.yams.models.combinations.SmallStraight;
import fr.uge.yams.models.combinations.Straight;
import fr.uge.yams.models.combinations.StraightFlush;
import fr.uge.yams.models.combinations.ThreeOfAKind;
import fr.uge.yams.models.combinations.ThreeOfAKindCard;
import fr.uge.yams.models.combinations.TwoPair;
import fr.uge.yams.models.combinations.Yahtzee;


public class Games {
	public final static List<Combination> DICE_COMBINATIONS = List.of(new Chance(), new ThreeOfAKind(), new FourOfAKind(), new FullHouse(), new SmallStraight(), new LargeStraight(), new Yahtzee());
	public final static List<Combination> CARD_COMBINATIONS = List.of(new HightCard(), new OnePair(), new TwoPair(), new ThreeOfAKindCard(), new Straight(), new Flush(), new FullHouseCard(), new FourOfAKindCard(), new StraightFlush(), new RoyalFlush());   

	public static int fact(int a){
		int res = 1;
		
		for (int i = 2; i < a; i++){
			res *= i;
		}
		return res;
	}
}
