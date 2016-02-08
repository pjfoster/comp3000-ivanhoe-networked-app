package comp3004.ivanhoe.model;

import java.util.List;

public class Player {

	String displayName;
	List<Token> tokens;
	
	List<Card> hand;
	List<Card> display;
	int displayTotal;
	
	/**
	 * Adds card to player's hand
	 * @param card
	 */
	public void addCard(Card card) {
		hand.add(card);
	}
	
	/**
	 * Adds token to player's collection
	 * @param token
	 */
	public void addToken(Token token){
		tokens.add(token);
	}
	
	/**
	 * Adds card from player's hand to their display
	 */
	public void playCard(Card card) {
		if(hand.contains(card)){
			display.add(card);
			hand.remove(card);
			displayTotal++;
		}
	}
	
	/**
	 * Print function
	 * @return
	 */
	public String print(){
		return displayName;
	}
}
