package comp3004.ivanhoe.util;

import java.util.ArrayList;

import org.json.simple.JSONObject;

public class ClientParser {

	public String getColor(JSONObject snapshot) {
		return (String)snapshot.get("tournament_color");
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Object> getPlayerList(JSONObject snapshot) {
		return (ArrayList<Object>)snapshot.get("players");
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<String> getDeck(JSONObject snapshot) {
		return (ArrayList<String>)snapshot.get("deck");
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<String> getPlayerHand(Object player) {
		return (ArrayList<String>)((JSONObject)player).get("hand");
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<String> getPlayerDisplay(Object player) {
		return (ArrayList<String>)((JSONObject)player).get("display");
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<String> getPlayerTokens(Object player) {
		return (ArrayList<String>)((JSONObject)player).get("tokens");
	}
	
	public String getPlayerName(Object player) {
		return (String)((JSONObject)player).get("username");
	}
	
	public Long getPlayerId(Object player) {
		return (Long)((JSONObject)player).get("id");
	}
	
}
