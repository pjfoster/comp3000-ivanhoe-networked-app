package comp3004.ivanhoe.model;

public enum Token {
	GREEN, 
	YELLOW, 
	RED, 
	BLUE, 
	PURPLE,
	UNDECIDED;
	
	public static Token fromString(String color) {
		if (color.equals("red") || color.equals("r")) {
			return RED;
		}
		else if (color.equals("green") || color.equals("g")) {
			return GREEN;
		}
		else if (color.equals("blue") || color.equals("b")) {
			return BLUE;
		}
		else if (color.equals("yellow") || color.equals("y")) {
			return YELLOW;
		}
		else if  (color.equals("purple") || color.equals("p")) {
			return PURPLE;
		}
		else { return null; }
	}
	
	public static String getFullColor(String colorChar) {
		if (colorChar.equals("r")) {
			return "red";
		}
		else if (colorChar.equals("g")) {
			return "green";
		}
		else if (colorChar.equals("b")) {
			return "blue";
		}
		else if (colorChar.equals("y")) {
			return "yellow";
		}
		else if (colorChar.equals("p")) {
			return "purple";
		}
		
		return null;
	}
}
