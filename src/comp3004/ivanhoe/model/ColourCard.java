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
	
	public String toString() {
		return "" + name.charAt(0) + value;
	}
	
	public static List<ColourCard> getColourDeck(){
		List<ColourCard> output = new ArrayList<ColourCard>();
		for(int i = 0; i < 4; i++){
			output.add(new ColourCard("purple",3));
			output.add(new ColourCard("purple",4));
			output.add(new ColourCard("purple",5));
			
			output.add(new ColourCard("blue",2));
			output.add(new ColourCard("blue",3));
			output.add(new ColourCard("blue",4));
			
			output.add(new ColourCard("yellow",2));
		}
		
		for(int i = 0; i<6; i++){
			output.add(new ColourCard("red",3));
			output.add(new ColourCard("red",4));
		}
		
		for(int i = 0; i<2; i++){
			output.add(new ColourCard("purple",7));
			output.add(new ColourCard("red",5));
			output.add(new ColourCard("blue",5));
			output.add(new ColourCard("yellow",4));
		}
		
		for(int i = 0; i<8; i++){
			output.add(new ColourCard("yellow",3));
		}

		for(int i = 0; i<14; i++){
			output.add(new ColourCard("green",1));
		}
		return output;
	}
	
}
