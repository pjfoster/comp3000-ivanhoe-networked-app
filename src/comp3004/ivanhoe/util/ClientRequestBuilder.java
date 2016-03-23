package comp3004.ivanhoe.util;

import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Transforms UI events into JSON requests for the client app
 * @author PJF
 *
 */
public class ClientRequestBuilder {
	
	/**
	 * Once a connection is accepted, a user must register with the game
	 * @param username
	 * @return
	 */
	public JSONObject buildRegisterPlayer(String username)
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
	public JSONObject buildChooseToken(String color) 
	{ 
		HashMap<String, String> requestMap = new HashMap<String, String>();
		requestMap.put("request_type", "choose_token"); 
		requestMap.put("token_color", color);
		return new JSONObject(requestMap); 
	}
	
	public JSONObject buildCardMove(String cardCode) {
		HashMap<String, String> requestMap = new HashMap<String, String>();
		requestMap.put("request_type", "turn_move"); 
		requestMap.put("move_type", "play_card");
		requestMap.put("card_code", cardCode);
		return new JSONObject(requestMap); 
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject buildMultipleCardsMove(String[] cards) {
		
		System.out.println("Building request for " + cards.length + " cards");
		
		JSONObject request = new JSONObject();
		request.put("request_type", "turn_move"); 
		request.put("move_type", "play_cards");

		JSONArray cardsArray = new JSONArray();
		for (int i =1 ; i < cards.length; ++i) {
			cardsArray.add(cards[i]);
		}
		
		System.out.println(cardsArray);
		request.put("cards", cardsArray);
		
		return request; 
	}
	
	/**
	 * Withdraw from a tournament
	 * @return
	 */
	public JSONObject buildWithdrawMove()
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
	public JSONObject buildSelectOpponent(String opponentUsername)
	{ 
		HashMap<String, String> requestMap = new HashMap<String, String>();
		requestMap.put("request_type", "select_opponent"); 
		requestMap.put("opponent_username", opponentUsername); 
		return new JSONObject(requestMap); 
	}
	
	public JSONObject buildPickCard(String cardCode) {
		HashMap<String, String> requestMap = new HashMap<String, String>();
		requestMap.put("request_type", "pick_card"); 
		requestMap.put("card_code", cardCode); 
		return new JSONObject(requestMap); 
	}
	
}
