package comp3004.ivanhoe.util;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

public class ServerParser {

	private JSONParser parser;
	
	public ServerParser() {
		parser = new JSONParser();
	}
	
	public Command parseCommand(String jsonString) {
		return null;
	}
	
	public static void main(String[] args) {
		
		JSONParser parser = new JSONParser();
		
		String test = "{\"test\":\"test_value\"}";
		
		try {
			JSONObject obj = (JSONObject)parser.parse(test);
			
			System.out.println(obj);
			System.out.println(obj.get("test"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
