package comp3004.ivanhoe.controller;

import java.util.HashMap;

import comp3004.ivanhoe.model.Player;

public class IvanhoeController {

	int maxPlayers;
	int numRounds;
	private HashMap<Integer, Player> players;
	
	public IvanhoeController(int maxPlayers, int numRounds) {
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
		}
		
		if (!players.containsKey(playerId)) {
			Player newPlayer = new Player(playerName);
			players.put(playerId, newPlayer);
		}
	}
	
}
