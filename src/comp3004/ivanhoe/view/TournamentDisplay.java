package comp3004.ivanhoe.view;


/**
 * Typical display shown during a tournament. Shows player details (including hand,
 * tokens, etc.) as well as each of their opponent's displays. 
 * @author PJF
 *
 */
public interface TournamentDisplay{
	
	public void addCard(String player, String card);
	public void removeCard(String player, String card);
	public void withdraw(String player);
	public void selectCard(String player, String card);
}
