package comp3004.ivanhoe.util;

import java.util.HashMap;

import org.json.simple.JSONObject;

import comp3004.ivanhoe.util.Config.RequestType;

/**
 * Transforms UI events into JSON requests for the client app
 * @author PJF
 *
 */
public class ClientRequestBuilder {

	/**
	 * Creates a JSON request based on values obtained from UI
	 * @param requestType
	 * @return
	 */
	public JSONObject buildResponse(RequestType requestType) {
		// TODO: Currently contains dummy values!
		
		HashMap<String, String> requestMap = new HashMap<String, String>();
		
		switch (requestType) {
			case REGISTER_PLAYER:
				requestMap.put("request_type", "register_player");
				requestMap.put("player_name", "TEST_USERNAME");
				break;
			case MAKE_MOVE:
				requestMap.put("request_type", "make_move");
				requestMap.put("move_type", "play_card");
				requestMap.put("card_type", "color_card");
				requestMap.put("card_color", "red");
				requestMap.put("card_value", "5");
				break;
			case QUIT:
				requestMap.put("request_type", "quit");
				break;
			default:
				break;
		}
		
		JSONObject request = new JSONObject(requestMap);
		return request;
		
	}
	
}
