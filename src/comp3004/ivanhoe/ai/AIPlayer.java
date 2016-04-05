package comp3004.ivanhoe.ai;

import comp3004.ivanhoe.model.Player;

/**
 * An AI player
 * @author PJF
 *
 */
public class AIPlayer extends Player {

	private AIStrategy strategy;
	
	public AIPlayer(AIStrategy strategy, String name) {
		super(name);
		this.strategy = strategy;
	}
	
}
