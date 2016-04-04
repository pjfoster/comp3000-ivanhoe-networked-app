package comp3004.ivanhoe.util;

import java.util.ArrayList;

import org.json.simple.JSONObject;

public abstract class ClientParser {

	public static String getServerResponse(JSONObject response) {
		return (String)response.get("server_response");
	}
	
	public static String getColorFromSnapshot(JSONObject snapshot) {
		return (String)snapshot.get("tournament_color");
	}
	
	public static Integer getCurrentTurnFromSnapshot(JSONObject snapshot) {
		return Integer.parseInt((String)snapshot.get("current_turn"));
	}
	
	public static String getHighestDisplayFromSnapshot(JSONObject snapshot) {
		return (String)snapshot.get("highest_display");
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<Object> getPlayerList(JSONObject snapshot) {
		return (ArrayList<Object>)snapshot.get("players");
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
	public static ArrayList<String> getOpponentsFromSelectOpponent(JSONObject snapshot) {
		return (ArrayList<String>)((JSONObject)snapshot).get("opponents");
	}
	
	public static Integer getPlayerIdFromSnapshot(JSONObject response) {
		return Integer.parseInt((String)response.get("player_id"));
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<String> getCardsFromPickCard(JSONObject snapshot) {
		return (ArrayList<String>)((JSONObject)snapshot).get("cards");
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<String> getTokensFromChooseColor(Object snapshot) {
		return (ArrayList<String>)((JSONObject)snapshot).get("tokens");
	}
	
}
