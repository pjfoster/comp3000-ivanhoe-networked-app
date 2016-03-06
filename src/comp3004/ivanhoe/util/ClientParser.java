package comp3004.ivanhoe.util;

import java.util.ArrayList;

import org.json.simple.JSONObject;

public class ClientParser {

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
	
	public static String getPlayerName(Object player) {
		return (String)((JSONObject)player).get("username");
	}
	
	public static Long getPlayerId(Object player) {
		return (Long)((JSONObject)player).get("id");
	}
	
}
