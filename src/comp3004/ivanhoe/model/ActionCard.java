package comp3004.ivanhoe.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Unhorse - 1
 * Change Weapon - 1
 * Drop Weapon - 1
 * Shield - 1
 * Stunned - 1
 * Ivanhoe - 1
 * Break Lance - 1
 * Riposte - 3
 * Dodge -1 
 * Retreat - 1
 * Knock Down - 2
 * Outmaneuver - 1
 * Charge - 1
 * Countercharge - 1
 * Disgrace - 1
 * Adapt - 1
 * Outwit - 1
 */
public class ActionCard extends Card {

	public ActionCard(){
		value = 0;
		name = "";
	}
	
	public ActionCard(String nm){
		name = nm;
		value = 0;
	}
	
	public String toString() {
		return name;
	}
	
	public static List<ActionCard> getActionDeck(){
		List<ActionCard> output = new ArrayList<ActionCard>();
		output.add(new ActionCard("Unhorse"));
		output.add(new ActionCard("Change Weapon"));
		output.add(new ActionCard("Drop Weapon"));
		output.add(new ActionCard("Shield"));
		output.add(new ActionCard("Stunned"));
		output.add(new ActionCard("Ivanhoe"));
		output.add(new ActionCard("Break Lance"));
		output.add(new ActionCard("Riposte"));
		output.add(new ActionCard("Riposte"));
		output.add(new ActionCard("Riposte"));
		output.add(new ActionCard("Dodge"));
		output.add(new ActionCard("Retreat"));
		output.add(new ActionCard("Knock Down"));
		output.add(new ActionCard("Knock Down"));
		output.add(new ActionCard("Outmaneuver"));
		output.add(new ActionCard("Charge"));
		output.add(new ActionCard("Countercharge"));
		output.add(new ActionCard("Disgrace"));
		output.add(new ActionCard("Adapt"));
		output.add(new ActionCard("Outwit"));
		return output;
	}
}
