package comp3004.ivanhoe.util;

import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import comp3004.ivanhoe.model.Card;
import comp3004.ivanhoe.model.Player;
import comp3004.ivanhoe.model.Token;
import comp3004.ivanhoe.model.Tournament;

public class ServerResponseBuilder {

	public JSONObject buildConnectionAccepted() {
		HashMap<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("response_type", "connection_accepted");
		return new JSONObject(responseMap);
	}
	
	public JSONObject buildConnectionRejected() {
		HashMap<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("response_type", "connection_rejected");
		return new JSONObject(responseMap);
	}
	
	public JSONObject buildStartGame(Tournament tournament, int playerId) {
		HashMap<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("response_type", "start_game");
		responseMap.put("current_turn", ""+playerId);
		return createGameSnapshot(responseMap, tournament);
	}
	
	public JSONObject buildInvalidResponse() {
		HashMap<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("response_type", "invalid_choice");
		return new JSONObject(responseMap);
	}
	
	public JSONObject buildChooseColor() {
		HashMap<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("response_type", "choose_color");
		return new JSONObject(responseMap);
	}
	
	/**
	 * 
	 * @param tournament
	 * @param playerTurn
	 * @return
	 */
	public JSONObject buildStartTournament(Tournament tournament, int playerTurn) {
		HashMap<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("response_type", "start_tournament");
		responseMap.put("current_turn", playerTurn + "");
		return createGameSnapshot(responseMap, tournament);
	}
	
	public JSONObject buildStartPlayerTurn(Card c) {
		HashMap<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("response_type", "start_player_turn");
		responseMap.put("drawn_card", c.toString());
		return new JSONObject(responseMap);
	}
	
	public JSONObject buildIndicateTurn(String playerName) {
		HashMap<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("response_type", "indicate_turn");
		responseMap.put("player_name", playerName);
		return new JSONObject(responseMap);
	}
	
	public JSONObject buildUpdateView(Tournament tournament) {
		HashMap<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("response_type", "update_view");
		return createGameSnapshot(responseMap, tournament);
	}
	
	public JSONObject buildTournamentOverWin(String tokenColor) {
		HashMap<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("response_type", "tournament_over_win");
		responseMap.put("token_color", tokenColor);
		return new JSONObject(responseMap);
	}
	
	public JSONObject buildTournamentOverLoss(String winnerName) {
		HashMap<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("response_type", "tournament_over_loss");
		responseMap.put("winner", winnerName);
		return new JSONObject(responseMap);
	}
	
	public JSONObject buildWaiting() {
		HashMap<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("response_type", "waiting");
		return new JSONObject(responseMap);
	}
	
	public JSONObject buildWithdraw(String playerName) {
		HashMap<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("response_type", "withdraw");
		responseMap.put("player_name", playerName);
		return new JSONObject(responseMap);
	}
	
	public JSONObject buildQuit() {
		HashMap<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("response_type", "quit");
		return new JSONObject(responseMap);
	}
	
	/**
	 * Creates a snapshot of a tournament and adds it to the provided response map
	 * @param responseMap
	 * @param tournament
	 */
	@SuppressWarnings("unchecked")
	public JSONObject createGameSnapshot(HashMap<String, String> responseMap, Tournament tournament) {
		
		JSONObject snapshot = new JSONObject(responseMap);
		snapshot.put("tournament_color", tournament.getToken().toString());
		snapshot.put("highest_display", "" + tournament.getHighestDisplayTotal());

		// Create a list of players
		JSONArray players = new JSONArray();
		for (Integer key : tournament.getPlayers().keySet()) {
			Player p = tournament.getPlayers().get(key);
			JSONObject player = new JSONObject();
			
			player.put("username", p.getName());
			player.put("id", key);
			player.put("display_total", "" + p.getDisplayTotal(tournament.getToken()));
			
			// create hand
			JSONArray hand = new JSONArray();
			for (Card c : p.getHand()) {
				hand.add(c.toString());
			}
			player.put("hand", hand);
			
			// create display
			JSONArray display = new JSONArray();
			for (Card c : p.getDisplay()) {
				display.add(c.toString());
			}
			player.put("display", display);
			
			// create tokens
			JSONArray tokens = new JSONArray();
			for (Token t: p.getTokens()) {
				tokens.add(t.toString());
			}
			player.put("tokens", tokens);
			
			players.add(player);
		}
		snapshot.put("players", players);
		
		// create a deck
		JSONArray deck = new JSONArray();
		for (Card c : tournament.getDeck()) {
			deck.add(c.toString());
		}
		snapshot.put("deck", deck);
		
		return snapshot;
		
	}
	
}
