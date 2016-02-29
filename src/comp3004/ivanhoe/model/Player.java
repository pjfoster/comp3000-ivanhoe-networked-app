package comp3004.ivanhoe.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The Player class handles all actions associated with the player during a round of Ivanhoe.
 * This class keeps track of player tokens, their hand, and their display
 * It also keeps track of the players display total based on their current cards played
 * The Player object is set once per game so the class comes with a reset method that allows the hand and display to be cleared
 * @author David
 * @since 2016-02-08
 */
public class Player {

	private String name;
	private List<Token> tokens;
	private List<Card> hand;
	private List<Card> display;
	private int displayTotal;
	
	/**
	 * Default for testing
	 */
	public Player(){
		name = "Default Dan";
		hand = new ArrayList<Card>();
		tokens = new ArrayList<Token>();
		display = new ArrayList<Card>();
		displayTotal = 0;
	}
	
	/**
	 * Sets new player with empty hand
	 * @param nm
	 */
	public Player(String nm){
		name = nm;
		hand = new ArrayList<Card>();
		tokens = new ArrayList<Token>();
		display = new ArrayList<Card>();
		displayTotal = 0;
	}
	
	/**
	 * Adds card to player's hand
	 * @param card
	 */
	public boolean addHandCard(Card card) {
		return hand.add(card);
	}
	
	/**
	 * Should not be used for normal play. Use playCard for normal play instead
	 * @param card
	 * @return
	 */
	public boolean removeHandCard(Card card){
		return hand.remove(card);
	}
	
	
	/**
	 * Add card to display and alters display total
	 * @param card
	 * @return
	 */
	public boolean addDisplayCard(Card card){
		// if statement checks if card is not an action card
		if(card.getValue()!=0){
			hand.add(card);
			displayTotal+=card.getValue();
			return true;
		}
		return false;
	}
	
	/**
	 * Removes card from display and alters display total
	 * @param card
	 * @return
	 */
	public boolean removeDisplayCard(Card card){
		if(card.getValue()!=0 && display.contains(card)){
			displayTotal-=card.getValue();
			return display.remove(card);
		}
		return false;
	}
	
	/**
	 * Adds token to player's collection
	 * @param token
	 * @return
	 */
	public boolean addToken(Token token){
		return tokens.add(token);
	}
	
	/**
	 * Adds card from player's hand to their display
	 * @param card
	 * @return
	 */
	public boolean playCard(Card card) {
		if (hand.contains(card)){
			display.add(card);
			hand.remove(card);
			displayTotal+=card.getValue();
			return true;
		}
		return false;
	}
	
	/**
	 * Used to clear hand and display for next tournament
	 */
	public void resetRound(){
		hand = new ArrayList<Card>();
		display = new ArrayList<Card>();
		displayTotal = 0;
	}
	
	/**
	 * Sets hand to input
	 * @param startingHand
	 */
	public void setStartingHand(List<Card> startingHand){
		hand = startingHand;
	}
	
	/**
	 * Returns player name
	 * @return
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Returns player tokens
	 * @return
	 */
	public List<Token> getTokens(){
		return tokens;
	}
	
	/**
	 * Returns player hand
	 * @return
	 */
	public List<Card> getHand(){
		return hand;
	}
	
	/**
	 * Returns player display
	 * @return
	 */
	public List<Card> getDisplay(){
		return display;
	}
	
	/**
	 * Returns player display total
	 * @return
	 */
	public int getDisplayTotal(){
		return displayTotal;
	}
}
