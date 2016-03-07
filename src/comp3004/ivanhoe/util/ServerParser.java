package comp3004.ivanhoe.util;

import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import comp3004.ivanhoe.model.Card;
import comp3004.ivanhoe.model.ColourCard;
import comp3004.ivanhoe.model.Token;
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
	
	public ArrayList<Card> getCard(JSONObject request, Tournament tournament) {
		String cardCode = (String)request.get("card_code");
		return tournament.getCard(cardCode);
	}
	
	public Token getToken(JSONObject request) {
		String colorString = (String)request.get("token_color");
		return Token.fromString(colorString);
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<String> getCardCodes(JSONObject request) {
		return (ArrayList<String>)request.get("cards");
	}
	
}
