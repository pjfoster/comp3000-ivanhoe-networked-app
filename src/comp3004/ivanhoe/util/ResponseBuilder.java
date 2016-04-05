package comp3004.ivanhoe.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import comp3004.ivanhoe.model.Card;
import comp3004.ivanhoe.model.Player;
import comp3004.ivanhoe.model.Token;
import comp3004.ivanhoe.model.Tournament;

public abstract class ResponseBuilder {

	public static JSONObject buildConnectionAccepted() {
		HashMap<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("response_type", "connection_accepted");
		return new JSONObject(responseMap);
	}
	
	public static JSONObject buildConnectionRejected() {
		HashMap<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("response_type", "connection_rejected");
		return new JSONObject(responseMap);
	}
	
	public static JSONObject buildStartGame(Tournament tournament, int playerId) {
		HashMap<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("response_type", "start_game");
		responseMap.put("current_turn", ""+playerId);
		return createGameSnapshot(responseMap, tournament);
	}
	
	public static JSONObject buildInvalidResponse() {
		HashMap<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("response_type", "invalid_choice");
		return new JSONObject(responseMap);
	}
	
	public static JSONObject buildChooseColor() {
		HashMap<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("response_type", "choose_color");
		return new JSONObject(responseMap);
	}
	
	@SuppressWarnings("unchecked")
	public static JSONObject buildChooseToken(HashSet<Token> tokenColors) {
		JSONObject response = new JSONObject();
		response.put("response_type", "choose_token");
		
		JSONArray tokens = new JSONArray();
		for (Token t: tokenColors) {
			tokens.add(t.toString());
		}
		
		response.put("tokens", tokens);
		
		return response;
	}
	
	@SuppressWarnings("unchecked")
	public static JSONObject buildChangeTournamentColor(HashSet<Token> tokenColors) {
		JSONObject response = new JSONObject();
		response.put("response_type", "change_tournament_color");
		
		JSONArray tokens = new JSONArray();
		for (Token t: tokenColors) {
			tokens.add(t.toString());
		}
		
		response.put("tokens", tokens);
		
		return response;
	}
	
	@SuppressWarnings("unchecked")
	public static JSONObject buildPickOpponent(int currentPlayerId, Tournament tournament) {
		JSONObject response = new JSONObject();
		response.put("response_type", "pick_opponent");
		
		JSONArray opponents = new JSONArray();
		for (Integer id: tournament.getPlayers().keySet()) {
			if (id != currentPlayerId) {
				opponents.add(tournament.getPlayers().get(id).getName());
			}
		}
		
		response.put("opponents", opponents);
		
		return response;
	}
	
	@SuppressWarnings("unchecked")
	public static JSONObject buildPickCard(List<Card> cardList, int playerId) {
		JSONObject response = new JSONObject();
		response.put("response_type", "pick_card");
		response.put("player_id", "" + playerId);
		
		JSONArray cards = new JSONArray();
		for (Card c: cardList) {
			cards.add(c.toString());
		}
		
		response.put("cards", cards);
		
		return response;
	}
	
	/**
	 * 
	 * @param tournament
	 * @param playerTurn
	 * @return
	 */
	public static JSONObject buildStartTournament(Tournament tournament, int playerTurn) {
		HashMap<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("response_type", "start_tournament");
		responseMap.put("current_turn", playerTurn + "");
		return createGameSnapshot(responseMap, tournament);
	}
	
	public static JSONObject buildStartPlayerTurn(Card c) {
		HashMap<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("response_type", "start_player_turn");
		responseMap.put("drawn_card", c.toString());
		return new JSONObject(responseMap);
	}
	
	public static JSONObject buildContinueTurn() {
		HashMap<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("response_type", "continue_turn");
		return new JSONObject(responseMap);
	}
	
	public static JSONObject buildIndicateTurn(String playerName) {
		HashMap<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("response_type", "indicate_turn");
		responseMap.put("player_name", playerName);
		return new JSONObject(responseMap);
	}
	
	public static JSONObject buildUpdateView(Tournament tournament) {
		HashMap<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("response_type", "update_view");
		return createGameSnapshot(responseMap, tournament);
	}
	
	public static JSONObject buildTournamentOverWin(String tokenColor) {
		HashMap<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("response_type", "tournament_over_win");
		responseMap.put("token_color", tokenColor);
		return new JSONObject(responseMap);
	}
	
	public static JSONObject buildTournamentOverLoss(String winnerName) {
		HashMap<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("response_type", "tournament_over_loss");
		responseMap.put("winner", winnerName);
		return new JSONObject(responseMap);
	}
	
	public static JSONObject buildGameOverWin() {
		HashMap<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("response_type", "game_over_win");
		return new JSONObject(responseMap);
	}
	
	public static JSONObject buildGameOverLoss(String winnerName) {
		HashMap<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("response_type", "game_over_loss");
		responseMap.put("winner", winnerName);
		return new JSONObject(responseMap);
	}
	
	public static JSONObject buildAnnouncement(String message) {
		HashMap<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("response_type", "announcement");
		responseMap.put("message", message);
		return new JSONObject(responseMap);
	}
	
	public static JSONObject buildWaiting() {
		HashMap<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("response_type", "waiting");
		return new JSONObject(responseMap);
	}
	
	public static JSONObject buildWithdraw(Integer playerId) {
		HashMap<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("response_type", "withdraw");
		responseMap.put("player_id", ""+playerId);
		return new JSONObject(responseMap);
	}
	
	public static JSONObject buildQuit() {
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
	public static JSONObject createGameSnapshot(HashMap<String, String> responseMap, Tournament tournament) {
		
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
			
			// create special display
			JSONArray special = new JSONArray();
			for (Card c : p.getSpecial()) {
				special.add(c.toString());
			}
			player.put("special", special);
			
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
