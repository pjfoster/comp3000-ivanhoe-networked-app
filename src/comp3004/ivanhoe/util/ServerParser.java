package comp3004.ivanhoe.util;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import comp3004.ivanhoe.model.ColourCard;
import comp3004.ivanhoe.model.Tournament;

public class ServerParser {

	private JSONParser parser;
	
	public ServerParser() {
		parser = new JSONParser();
	}
	
	public String getParam(JSONObject request, String param) {
		if (request.get(param) != null) {
			return (String)request.get(param);
		}
		else return null;
	}
	
	public String getRequestType(JSONObject request) {
		return (String)request.get("request_type");
	}
	
	public String getMoveType(JSONObject request) {
		return (String)request.get("move_type");
	}
	
	public ColourCard getColourCard(JSONObject request, Tournament tournament) {
		String colour = (String)request.get("card_color");
		return null;
	}
	
}
