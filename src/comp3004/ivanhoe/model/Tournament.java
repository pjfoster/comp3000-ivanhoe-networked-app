package comp3004.ivanhoe.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tournament {

	private Token token;
	private List<Player> currentPlayers;
	
	private List<Card> deck;
	private List<Card> discardPile;
	
	public Tournament(){
		currentPlayers = new ArrayList<Player>();
		discardPile = new ArrayList<Card>();
		buildDeck();
	}
	
	public Tournament(List<Player> players){
		currentPlayers=players;
		discardPile = new ArrayList<Card>();
		buildDeck();
	}
	
	/**
	 * This function adds a player to the tournament
	 * @param player
	 */
	public void addPlayer(Player player){
		currentPlayers.add(player);
	}
	
	/**
	 * This function removes a player from the tournament
	 * @param player
	 * @return
	 */
	public boolean removePlayer(Player player){
		return currentPlayers.remove(player);
	}

	/**
	 * This function builds the deck from the Card subclasses
	 */
	public void buildDeck(){
		deck = new ArrayList<Card>();
		deck.addAll(ActionCard.getActionDeck());
		deck.addAll(ColourCard.getColourDeck());
		deck.addAll(SupporterCard.getSupporterDeck());
		shuffle();
	}
	
	/**
	 * This function sets the hands of all players and removes those cards from the deck
	 **/
	public void dealStartingHands(){
		for(int i = 0; i < 8; i++){
			for(Player p: currentPlayers){
				p.addHandCard(drawCard());
			}
		}
	}
	
	/**
	 * this function returns a card at index 0 from the deck and removes it from deck
	 * @return
	 */
	public Card drawCard(){
		if(deck.size()==0)
			resetDiscardtoDeck();
		return deck.remove(0);
	}
	
	/**
	 * This function adds a card to the discard pile
	 * @param card
	 */
	public void addToDiscard(Card card){
		discardPile.add(card);
	}
	
	/**
	 * This function is called by drawCard() when the deck has no cards and it takes the discard deck shuffles and adds it to the empty deck
	 */
	private void resetDiscardtoDeck(){
		deck.addAll(discardPile);
		shuffle();
		discardPile = new ArrayList<Card>();
	}

	/**
	 * This function uses Collections.shuffle to shuffle the deck
	 */
	private void shuffle(){
		Collections.shuffle(deck);
	}
	
	/**
	 * This function returns the player with the highest display
	 * @return
	 */
	public Player getPlayerWithHighestDisplay(){
		Player temp = currentPlayers.get(0);
		for(Player p: currentPlayers){
			if(p.getDisplayTotal()>temp.getDisplayTotal())
				temp = p;
		}
		return temp;
	}
	
	/**
	 * This function returns the current token
	 * @return
	 */
	public Token getToken(){
		return token;
	}
	
	/**
	 * This function allows the user to set the current token
	 * @param tkn
	 */
	public void setToken(Token tkn){
		token  = tkn;
	}
}
