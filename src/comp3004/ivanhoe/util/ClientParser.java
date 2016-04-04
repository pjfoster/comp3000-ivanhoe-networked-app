package comp3004.ivanhoe.util;

import java.util.ArrayList;

import org.json.simple.JSONObject;

public abstract class ClientParser {

	public static String getColor(JSONObject snapshot) {
		return (String)snapshot.get("tournament_color");
	}
	
	public static Integer getCurrentTurn(JSONObject snapshot) {
		return Integer.parseInt((String)snapshot.get("current_turn"));
	}
	
	public static Integer getWithdrawPlayerId(JSONObject response) {
		return Integer.parseInt((String)response.get("player_id"));
	}
	
	public static String getHighestDisplay(JSONObject snapshot) {
		return (String)snapshot.get("highest_display");
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<Object> getPlayerList(JSONObject snapshot) {
		return (ArrayList<Object>)snapshot.get("players");
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<String> getDeck(JSONObject snapshot) {
		return (ArrayList<String>)snapshot.get("deck");
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<String> getPlayerHand(Object player) {
		return (ArrayList<String>)((JSONObject)player).get("hand");
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<String> getPlayerDisplay(Object player) {
		return (ArrayList<String>)((JSONObject)player).get("display");
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<String> getPlayerTokens(Object player) {
		return (ArrayList<String>)((JSONObject)player).get("tokens");
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<String> getOpponents(JSONObject snapshot) {
		return (ArrayList<String>)((JSONObject)snapshot).get("opponents");
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<String> getCards(JSONObject snapshot) {
		return (ArrayList<String>)((JSONObject)snapshot).get("cards");
	}
	
	public static String getPlayerName(Object player) {
		return (String)((JSONObject)player).get("username");
	}
	
	public static String getPlayerDisplayTotal(Object player) {
		return (String)((JSONObject)player).get("display_total");
	}
	
	public static Long getPlayerId(Object player) {
		return (Long)((JSONObject)player).get("id");
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<String> getTokensFromSnapshot(Object snapshot) {
		return (ArrayList<String>)((JSONObject)snapshot).get("tokens");
	}

	public static String getServerResponse(JSONObject response) {
		return (String)response.get("server_response");
	}
	
}
