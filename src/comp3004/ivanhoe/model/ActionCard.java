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
		output.add(new ActionCard("unhorse"));
		output.add(new ActionCard("changeweapon"));
		output.add(new ActionCard("dropweapon"));
		output.add(new ActionCard("shield"));
		output.add(new ActionCard("stunned"));
		output.add(new ActionCard("ivanhoe"));
		output.add(new ActionCard("breaklance"));
		output.add(new ActionCard("riposte"));
		output.add(new ActionCard("riposte"));
		output.add(new ActionCard("riposte"));
		output.add(new ActionCard("dodge"));
		output.add(new ActionCard("retreat"));
		output.add(new ActionCard("knockdown"));
		output.add(new ActionCard("knockdown"));
		output.add(new ActionCard("outmaneuver"));
		output.add(new ActionCard("charge"));
		output.add(new ActionCard("countercharge"));
		output.add(new ActionCard("disgrace"));
		output.add(new ActionCard("adapt"));
		output.add(new ActionCard("outwit"));
		return output;
	}
}
