package fr.uge.yams;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

import javafx.scene.Node;
import javafx.scene.shape.Shape;

public class BoardCard implements Board{
    private final ArrayList<Card> cards;

	public BoardCard() {
		cards = new ArrayList<>();

		for (var i = 1; i <= 5; i++) {
			
			
			cards.add(pickNewCard());
		}
	}

	public Card pickNewCard(){
		// pour ne pas avoir des cartes en doublons
		// tant que cette carte est dans le board
		// on re tire une nouvelle carte

		Card cardPicked = new Card();
		while (cards.contains(cardPicked)){
			cardPicked = new Card();
		}

		return cardPicked;
	}

    @Override
    public void rerollAll(){
		for (var i = 0; i < 5; i++) {
			cards.set(i, pickNewCard());
		}
	}

    @Override
    public List<GameElement> getElementsList () {
		return List.copyOf(cards);
	}

    // pour les test
    public BoardCard(List<Integer> lm) {
		Objects.requireNonNull(lm);

		cards = new ArrayList<>();

		for (var i = 1; i <= 5; i++) {
			cards.add(new Card(lm.get(i - 1)));
		}
	}

	@Override
    public List<Integer> elementsMaxOcc(){
		
		// on met l'index de tous les cartes ayant la valeur maxOcc
		var cardsOcc = new ArrayList<Integer>();
		
		// valeur des cartes qui sont le plus dans board
		var cardValMaxOcc = occurence().indexOf(maxOcc());

		for (int i = 0; i < 5; i++){
			var cardVal = cards.get(i).rank();
			
			if (cardVal == cardValMaxOcc){
				cardsOcc.add(i);
			}
		}
		return List.copyOf(cardsOcc);
	}

    // faire un reroll avec comme argument la liste des pos des cartes a modifs
    @Override
	public void reroll(Collection<Integer> positions) {
		Objects.requireNonNull(positions);

		for (var pos : positions) {
			if (pos<1 || pos>5) {
				throw new IllegalArgumentException();
			}
			cards.set(pos - 1, pickNewCard());
		}
		
	}

    	
	// revoie la liste contenant tout les pos des cartes qui forme une séquence
    @Override
	public List<Integer> elementsFormingSeq(){

		// liste contenant tout les séquence de cartes sous forme de set
		var allSeq = new ArrayList<Set<Integer>>();
		
		var seq = new HashSet<Integer>();
		
		// map triée par valeur des cartes avec la premier index qui a cette valeur
		var valIndexSorted = uniqueValAndGameElementIndexSorted();

		var cardIndexLst = new ArrayList<Integer>(valIndexSorted.values());
		var cardValLst = new ArrayList<Integer>(valIndexSorted.keySet());
		
		// on met la première pos
		seq.add(cardIndexLst.get(0) + 1);
		
		var prevVal = cardValLst.get(0);


		// pour chaque valeur, index
		for (int i = 1; i < cardIndexLst.size(); i++) {
			
			var cardVal = cardValLst.get(i);
			var cardIndex = cardIndexLst.get(i);

			// si cardVal ne forme pas une seq avec prevVal
			// + cas particulier ou ou il y a 'un trou' dans la seq
			// exemple
			// -------  -------  -------  -------  -------  
			// |  5  |  |  1  |  |  5  |  |  2  |  |  3  |  
			// -------  -------  -------  -------  ------- 
			// on doit ajouter un des 5 dans la seq 
			if (!(cardVal == prevVal + 1 || cardVal == prevVal + 2)){
				// on arrete la séquence

				// la sauvegarde si elle est plus grande que 1
				
				allSeq.add(new HashSet<Integer>(seq));

				// reset
				seq = new HashSet<Integer>();
			}

			// on ajoute la pos
			seq.add(cardIndex + 1);

			prevVal = cardVal;
		}
		// on oublie pas la dernière seq
		allSeq.add(seq);

		// cas spécifique, l'As peut aller a la fin après le roi
		// on ajoute l'As si dans chaque séquence
		// si on a un as dans la main de départ
		if (occurence().contains(1)){
			addAceSpecificCase(allSeq);
		}

		// on renvoie la seq la plus grande
		return maxSizeLst(allSeq);
    }

	private void addAceSpecificCase(ArrayList<Set<Integer>> allSeq){
		
		// 10 J Q K
		var validSeq = new HashSet<Integer>(List.of(10, 11, 12, 13));
		
		// pour chaque seq 
		for (var seq : allSeq){

			if (seq.size() == 4){
				
				// on retrouve les rank a partir de leur positions
				var nSeq = new HashSet<Integer>();

				// on reconstruit la seq avec les valeurs
				for (var pos : seq){
					nSeq.add(cards.get(pos - 1).rank());
				}

				// si la seq est 10 J Q K 
				if (nSeq.equals(validSeq)){

					int acePos = 0;
					// on trouve la pos de l'as 
					for (int i = 0; i < cards.size(); i++){
						if (cards.get(i).rank() == 1){
							acePos = i + 1;	
						}
					}

					// on ajoute l'as
					seq.add(acePos);
					return;
				}
			}
		}
	}

    @Override
    public TreeMap<Integer, Integer> uniqueValAndGameElementIndexSorted(){
		var res = new TreeMap<Integer, Integer>();
		
		for (int i = 0; i < 5; i++){
			var cardVal = cards.get(i).rank();
			
			// on ajoute uniquement les premier index pour une valeur
			if (!res.containsKey(cardVal)){
				res.put(cardVal, i);
			}
		}
		return res;
	}

    // somme de tout les cartes pour le calcul du score
	@Override
    public int sum(){
		int s = 0;
		for (var card : cards){
			s += card.rank();
		}
		return s;
	}

    @Override
    public List<Integer> occurence(){
		
		// on créer un tableau qui contiendra le nombre 
		// d'occurence de cartes de rank i a l'index i
		ArrayList<Integer> occLst = new ArrayList<Integer>(List.of(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
		
		for (var card : cards){
			occLst.set(card.rank(), occLst.get(card.rank()) + 1);
		}
		return List.copyOf(occLst);
	}

	@Override
	public boolean isAllSameSuit(){

		// on prend le suit du la première carte 
		Suit prevSuit = cards.getFirst().suit();

		for (var card : cards){
			if (!card.suit().equals(prevSuit)){
				return false;
			}
		}
		return true;
	}

	@Override
	public List<Node> allGameElementShapes(){
		var res = new ArrayList<Node>();
		for (var card : cards){
			res.add(card.shape());
		}
		return List.copyOf(res);
	}

	@Override
	public List<Node> gameElementShapes(Collection<Integer> positions) {
		Objects.requireNonNull(positions);

		var res = new ArrayList<Node>();
		for (var pos : positions){

			if (pos < 1 || pos > 5){
				throw new IllegalArgumentException();
			}

			res.add(cards.get(pos - 1).shape());
		}
		return List.copyOf(res);
	}



    public static void main(String[] args) {

		var board = new BoardCard(List.of(5, 1, 5, 2, 3));
		//board.rerollAllCard();
		System.out.println(board);
		System.out.println(board.elementsFormingSeq());
	}

}
