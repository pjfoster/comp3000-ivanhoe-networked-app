package comp3004.ivanhoe.controller;

import java.util.HashMap;

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
	
}
