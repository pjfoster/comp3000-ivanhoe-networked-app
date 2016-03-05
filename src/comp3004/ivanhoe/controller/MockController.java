package comp3004.ivanhoe.controller;

import java.util.ArrayList;
import java.util.HashMap;

import comp3004.ivanhoe.model.Card;
import comp3004.ivanhoe.model.Player;
import comp3004.ivanhoe.model.Token;
import comp3004.ivanhoe.model.Tournament;
import comp3004.ivanhoe.server.AppServer;
import comp3004.ivanhoe.util.ServerResponseBuilder;

public class MockController extends IvanhoeController {

	public MockController(AppServer server, ServerResponseBuilder responseBuilder, int maxPlayers) {
		super(server, responseBuilder, maxPlayers);
	}
	
	public Tournament getTournament() { return currentTournament; }
	public int getCurrentTurn() { return currentTurn; }
	
	public void setPlayers(HashMap<Integer, Player> players) {
		this.players = players;
	}
	
	public void setTournament(Tournament tournament) {
		this.currentTournament = tournament;
	}
	
	public void setPreviousTournament(Token color) {
		this.previousTournament = color;
	}
	
	public void givePlayerToken(int playerId, Token token) {
		players.get(playerId).addToken(token);
	}
	
	public void setTurn(int playerId) {
		
		if (playerTurns == null) {
			playerTurns = new ArrayList<Integer>(players.keySet());
		}
		
		for (int i = 0; i < playerTurns.size(); ++i) {
			if (playerTurns.get(i) == playerId) {
				currentTurn = i;
			}
		}
		
	}
	
	public void setState(int state) {
		this.state = state;
	}
	
	public int getState() {
		return state;
	}
	
	public Card getCardFromDeck(String cardCode) {
		for (Card c: currentTournament.getDeck()) {
			if (c.toString().equals(cardCode)) {
				return c;
			}
		}
		return null;
	}
	
	public ArrayList<Integer> getPlayerTurns() {
		return playerTurns;
	}
	
	public HashMap<Integer, Player> getPlayers() { 
		for (Integer key: players.keySet()) {
			System.out.println(key + ": " + players.get(key));
		}
		return players;
	}
	
}
