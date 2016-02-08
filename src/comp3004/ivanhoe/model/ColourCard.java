package comp3004.ivanhoe.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Purple 3 - 4
 * Purple 4 - 4
 * Purple 5 - 4
 * Purple 7 - 2
 * 
 * Red 3 - 6
 * Red 4 - 6
 * Red 5 - 2
 * 
 * Blue 2 - 4
 * Blue 3 - 4
 * Blue 4 - 4
 * Blue 5 - 2
 * 
 * Yellow 2 - 4
 * Yellow 3 - 8
 * Yellow 4 - 2
 * 
 * Green 1 - 14
 * 
 * @author David Farrar
 *
 */
public class ColourCard extends Card {
	
	public ColourCard(){
		name = null;
		value = 0;
	}
	
	public ColourCard(String clr, int val){
		name = clr;
		value = val;
	}
	
	public String getColour(){
		return name;
	}
	
	public int getValue(){
		return value;
	}
	
	public static List<ColourCard> getColourDeck(){
		List<ColourCard> output = new ArrayList<ColourCard>();
		for(int i = 0; i < 4; i++){
			output.add(new ColourCard("Purple",3));
			output.add(new ColourCard("Purple",4));
			output.add(new ColourCard("Purple",5));
			
			output.add(new ColourCard("Blue",2));
			output.add(new ColourCard("Blue",3));
			output.add(new ColourCard("Blue",4));
			
			output.add(new ColourCard("Yellow",2));
		}
		
		for(int i = 0; i<6; i++){
			output.add(new ColourCard("Red",3));
			output.add(new ColourCard("Red",4));
		}
		
		for(int i = 0; i<2; i++){
			output.add(new ColourCard("Purple",7));
			output.add(new ColourCard("Red",5));
			output.add(new ColourCard("Blue",5));
			output.add(new ColourCard("Yellow",4));
		}
		
		for(int i = 0; i<8; i++){
			output.add(new ColourCard("Yellow",3));
		}

		for(int i = 0; i<14; i++){
			output.add(new ColourCard("Green",1));
		}
		return output;
	}
	
}
