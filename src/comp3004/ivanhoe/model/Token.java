package comp3004.ivanhoe.model;

public enum Token {
	GREEN, 
	YELLOW, 
	RED, 
	BLUE, 
	PURPLE;
	
	public static Token fromString(String color) {
		if (color.equals("red")) {
			return RED;
		}
		else if (color.equals("green")) {
			return GREEN;
		}
		else if (color.equals("blue")) {
			return BLUE;
		}
		else if (color.equals("yellow")) {
			return YELLOW;
		}
		else {
			return PURPLE;
		}
	}
}
