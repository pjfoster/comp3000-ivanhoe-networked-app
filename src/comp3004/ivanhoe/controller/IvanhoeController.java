package comp3004.ivanhoe.controller;

import java.util.HashMap;

import org.json.simple.JSONObject;

import comp3004.ivanhoe.model.Player;
import comp3004.ivanhoe.server.AppServer;
import comp3004.ivanhoe.util.ServerResponseBuilder;

public class IvanhoeController {

	int maxPlayers;
	int numRounds;
	private HashMap<Integer, Player> players;
	private ServerResponseBuilder responseBuilder;
	private AppServer server;
	
	public IvanhoeController(AppServer server, ServerResponseBuilder responseBuilder, int maxPlayers, int numRounds) {
		this.server = server;
		this.responseBuilder = responseBuilder;
		this.maxPlayers = maxPlayers;
		this.numRounds = numRounds;
		players = new HashMap<Integer, Player>();
	}
	
	/**
	 * Registers user as a player
	 * @param playerId: id of player thread, used to identify them
	 * @param playerName: player's username
	 */
	public void addPlayer(int playerId, String playerName) {
		
		if (players.size() >= maxPlayers) {
			// Too many players; can't add any more
			return;
		}
		
		if (!players.containsKey(playerId)) {
			Player newPlayer = new Player(playerName);
			players.put(playerId, newPlayer);
		}
		
		if (players.size() == maxPlayers) {
			startGame();
		}
	
	}
	
	/**
	 * Controls order and sequence of the game
	 */
	public void startGame() {
		JSONObject startGameMessage = responseBuilder.buildStartGame();
		server.broadcast(startGameMessage);
	}
	
}
