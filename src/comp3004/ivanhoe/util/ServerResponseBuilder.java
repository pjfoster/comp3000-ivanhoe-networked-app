package comp3004.ivanhoe.util;

import java.util.HashMap;

import org.json.simple.JSONObject;

import comp3004.ivanhoe.model.Player;
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
	
	public JSONObject buildStartGame() {
		HashMap<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("response_type", "start_game");
		return new JSONObject(responseMap);
	}
	
	public JSONObject buildStartTournament() {
		HashMap<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("response_type", "start_tournament");
		return new JSONObject(responseMap);
	}
	
	public JSONObject buildStartPlayerTurn() {
		HashMap<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("response_type", "start_player_turn");
		return new JSONObject(responseMap);
	}
	
	public JSONObject buildUpdateView(Player player, Tournament tournament) {
		HashMap<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("response_type", "update_view");
		return new JSONObject(responseMap);
	}
	
	public JSONObject buildTournamentOverWin() {
		HashMap<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("response_type", "tournament_over_win");
		return new JSONObject(responseMap);
	}
	
	public JSONObject buildTournamentOverLoss() {
		HashMap<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("response_type", "tournament_over_loss");
		return new JSONObject(responseMap);
	}
	
	public JSONObject buildQuit() {
		HashMap<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("response_type", "quit");
		return new JSONObject(responseMap);
	}
	
}
