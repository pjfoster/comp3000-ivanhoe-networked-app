package comp3004.ivanhoe.util;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Transforms UI events into JSON requests for the client app
 * @author PJF
 *
 */
public abstract class RequestBuilder {
	
	/**
	 * Once a connection is accepted, a user must register with the game
	 * @param username
	 * @return
	 */
	public static JSONObject buildRegisterPlayer(String username)
	{ 
		HashMap<String, String> requestMap = new HashMap<String, String>();
		requestMap.put("request_type", "register_player"); 
		requestMap.put("username", username);
		return new JSONObject(requestMap);
	}
	
	/**
	 * Indicate what color the tournament will be
	 * @param color
	 * 	Expected 'red', 'blue', 'green', 'yellow', or 'purple'
	 * @return
	 */
	public static JSONObject buildChooseToken(String color) 
	{ 
		HashMap<String, String> requestMap = new HashMap<String, String>();
		requestMap.put("request_type", "choose_token"); 
		requestMap.put("token_color", color);
		return new JSONObject(requestMap); 
	}
	
	public static JSONObject buildCardMove(String cardCode) {
		HashMap<String, String> requestMap = new HashMap<String, String>();
		requestMap.put("request_type", "turn_move"); 
		requestMap.put("move_type", "play_card");
		requestMap.put("card_code", cardCode);
		return new JSONObject(requestMap); 
	}
	
	public static JSONObject buildFinishTurn() {
		HashMap<String, String> requestMap = new HashMap<String, String>();
		requestMap.put("request_type", "finish_turn"); 
		return new JSONObject(requestMap); 
	}
	
	// Used for testing purposes
	public static JSONObject buildMultipleCardsMove(String[] cards) {
		ArrayList<String> cardsArray = new ArrayList<String>();
		for (String c: cards) {
			cardsArray.add(c);
		}
		return buildMultipleCardsMove(cardsArray);
	}
	
	@SuppressWarnings("unchecked")
	public static JSONObject buildMultipleCardsMove(ArrayList<String> cards) {
		
		System.out.println("Building request for " + cards.size() + " cards");
		
		JSONObject request = new JSONObject();
		request.put("request_type", "turn_move"); 
		request.put("move_type", "play_cards");

		JSONArray cardsArray = new JSONArray();
		for (int i = 0 ; i < cards.size(); ++i) {
			cardsArray.add(cards.get(i));
		}
		
		System.out.println(cardsArray);
		request.put("cards", cardsArray);
		
		return request; 
	}
	
	/**
	 * Withdraw from a tournament
	 * @return
	 */
	public static JSONObject buildWithdrawMove()
	{ 
		HashMap<String, String> requestMap = new HashMap<String, String>();
		requestMap.put("request_type", "turn_move"); 
		requestMap.put("move_type", "withdraw");
		return new JSONObject(requestMap); 
	}
	
	/**
	 * Pick an opponent; typically someone against whom to play an action card
	 * @return
	 */
	public static JSONObject buildSelectOpponent(String opponentId)
	{ 
		HashMap<String, String> requestMap = new HashMap<String, String>();
		requestMap.put("request_type", "select_opponent"); 
		requestMap.put("opponent_id", opponentId); 
		return new JSONObject(requestMap); 
	}
	
	public static JSONObject buildPickCard(String cardCode) {
		HashMap<String, String> requestMap = new HashMap<String, String>();
		requestMap.put("request_type", "pick_card"); 
		requestMap.put("card_code", cardCode); 
		return new JSONObject(requestMap); 
	}
	
}
