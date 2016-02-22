package comp3004.ivanhoe.util;

import java.util.HashMap;

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
	
	/**
	 * Play a color card
	 * @param cardColor
	 * 	Expected 'red', 'blue', 'green', 'yellow', or 'purple'
	 * @param cardValue
	 * 	Expecting an integer value (as a string)
	 * @return
	 */
	public JSONObject buildColorCardMove(String cardColor, String cardValue)
	{ 
		HashMap<String, String> requestMap = new HashMap<String, String>();
		requestMap.put("request_type", "turn_move"); 
		requestMap.put("move_type", "color_card");
		requestMap.put("card_color", cardColor);
		requestMap.put("card_value", cardValue);
		return new JSONObject(requestMap); 
	}
	
	/**
	 * Play a supporter card
	 * @param supporterType
	 * 	Expected 'squire' or 'maiden'
	 * @param supporterValue
	 * 	Expected value of card - 2,3 for squire, 6 for maiden
	 * @return
	 */
	public JSONObject buildSupporterCardMove(String supporterType, String supporterValue)
	{ 
		HashMap<String, String> requestMap = new HashMap<String, String>();
		requestMap.put("request_type", "turn_move"); 
		requestMap.put("move_type", "supporter_card");
		requestMap.put("supporter_type", supporterType);
		requestMap.put("supporter_value", supporterValue);
		return new JSONObject(requestMap); 
	}
	
	/**
	 * Play an action card
	 * @param actionCardCode
	 * 	String representing action card
	 * @return
	 */
	public JSONObject buildActionCardMove(String actionCardCode)
	{ 
		HashMap<String, String> requestMap = new HashMap<String, String>();
		requestMap.put("request_type", "turn_move"); 
		requestMap.put("move_type", "action_card");
		requestMap.put("card_code", actionCardCode);
		return new JSONObject(requestMap); 
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
	
}
