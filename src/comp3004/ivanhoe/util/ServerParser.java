package comp3004.ivanhoe.util;

import java.util.ArrayList;

import org.json.simple.JSONObject;

import comp3004.ivanhoe.model.Card;
import comp3004.ivanhoe.model.Token;
import comp3004.ivanhoe.model.Tournament;

public abstract class ServerParser {
	
	public static String getParam(JSONObject request, String param) {
		if (request.get(param) != null) {
			return (String)request.get(param);
		}
		else return null;
	}
	
	public static String getRequestType(JSONObject request) {
		return (String)request.get("request_type");
	}
	
	public static String getMoveType(JSONObject request) {
		return (String)request.get("move_type");
	}
	
	public static ArrayList<Card> getCard(JSONObject request, Tournament tournament) {
		String cardCode = (String)request.get("card_code");
		return tournament.getCard(cardCode);
	}
	
	public static Token getToken(JSONObject request) {
		String colorString = (String)request.get("token_color");
		return Token.fromString(colorString);
	}
	
	public static String getOpponentId(JSONObject request) {
		return (String) request.get("opponent_id");
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<String> getCardCodes(JSONObject request) {
		return (ArrayList<String>)request.get("cards");
	}
	
	public static ArrayList<String> getCardCode(JSONObject request) {
		ArrayList<String> cards = new ArrayList<String>();
		String cardCode = (String)request.get("card_code");
		cards.add(cardCode);
		return cards;
	}
	
}
