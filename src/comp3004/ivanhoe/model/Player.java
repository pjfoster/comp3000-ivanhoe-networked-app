package comp3004.ivanhoe.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

/**
 * The Player class handles all actions associated with the player during a round of Ivanhoe.
 * This class keeps track of player tokens, their hand, and their display
 * It also keeps track of the players display total based on their current cards played
 * The Player object is set once per game so the class comes with a reset method that allows the hand and display to be cleared
 */
public class Player {

	protected String name;
	protected HashSet<Token> tokens;
	protected List<Card> hand;
	protected List<Card> special;
	protected List<Card> display;
	
	/**
	 * Default for testing
	 */
	public Player(){
		name = "Test";
		hand = new ArrayList<Card>();
		tokens = new HashSet<Token>();
		display = new ArrayList<Card>();
		special = new ArrayList<Card>();
	}
	
	/**
	 * Sets new player with empty hand
	 * @param nm
	 */
	public Player(String nm){
		name = nm;
		hand = new ArrayList<Card>();
		tokens = new HashSet<Token>();
		display = new ArrayList<Card>();
		special = new ArrayList<Card>();
	}
	
	public String getName() { return name; }
	public HashSet<Token> getTokens() {	return tokens; }
	public List<Card> getHand() { return hand; }
	public List<Card> getDisplay() { return display; }
	public Card getDisplayTop() { return display.get(display.size()-1); }
	public List<Card> getSpecial() { return special; }
	
	/**
	 * Returns a random card from the player's hand
	 * @return
	 */
	public Card getHandRandom() {
		Random rnd = new Random();
		int i = rnd.nextInt(hand.size());
		return hand.get(i);
	}
	
	/**
	 * Adds card to player's hand
	 * @param card
	 */
	public boolean addHandCard(Card card) {
		return hand.add(card);
	}
	
	public boolean hasCardInHand(Card card) {
		if (hand.contains(card)) { return true; }
		return false;
	}
	
	/**
	 * Returns true if the player has a maiden in their display
	 * @return
	 */
	public boolean hasPlayedMaiden() {
		for (Card c: display) {
			if (c.getName().equals("maiden")) { return true; }
		}
		return false;
	}
	
	/**
	 * Returns true if the player has played the SHIELD action card
	 * @return
	 */
	public boolean hasShield() {
		for (Card c: special) {
			if (c.toString().equals("shield")) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns true is the player has STUNNED card next to their display
	 * @return
	 */
	public boolean isStunned() {
		for (Card c: special) {
			if (c.toString().equals("stunned")) {
				return true;
			}
		}
		return false;
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
		if (card.getValue() != 0) {
			display.add(card);
			return true;
		}
		return false;
	}
	
	public boolean addSpecialCard(Card card) {
		if (card.getName().toLowerCase().equals("shield") ||
			card.getName().toLowerCase().equals("stunned")) {
			special.add(card);
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
		return display.remove(card);
	}
	
	public Card removeDisplayTop() {
		return display.remove(display.size()-1);
	}
	
	public boolean removeSpecial(Card card){
		return display.remove(card);
	}
	
	/**
	 * Adds token to player's collection
	 * @param token
	 * @return
	 */
	public boolean addToken(Token token){
		return tokens.add(token);
	}
	
	public boolean removeToken(Token token) {
		return tokens.remove(token);
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
			return true;
		}
		return false;
	}
	
	/**
	 * Used to clear display for next tournament
	 */
	public void resetRound(){
		display = new ArrayList<Card>();
		special = new ArrayList<Card>();
	}
	
	/**
	 * Sets hand to input
	 * @param startingHand
	 */
	public void setStartingHand(List<Card> startingHand){
		hand = startingHand;
	}
	
	/**
	 * Returns player display total
	 * @return
	 */
	public int getDisplayTotal(Token color){
		
		int displayTotal = 0;
		for (Card c: display) {
			if (color.equals(Token.GREEN)) {
				displayTotal += 1;
			}
			else {
				displayTotal += c.getValue();
			}
		}
		
		return displayTotal;

	}
}
