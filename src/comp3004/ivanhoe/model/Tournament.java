package comp3004.ivanhoe.model;

import java.util.ArrayList;
import java.util.List;

public class Tournament {

	Token colour;
	List<Player> players;
	int highestDisplay;
	
	List<Card> deck;
	List<Card> discardPile;
	
	public Tournament(){
		
	}
	
	public void buildDeck(){
		deck = new ArrayList<Card>();
		deck.addAll(ActionCard.getActionDeck());
		deck.addAll(ColourCard.getColourDeck());
		deck.addAll(SupporterCard.getSupporterDeck());
	}
	
	public void shuffle(){
	}
}
