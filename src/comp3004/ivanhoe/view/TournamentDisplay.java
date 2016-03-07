package comp3004.ivanhoe.view;


/**
 * Typical display shown during a tournament. Shows player details (including hand,
 * tokens, etc.) as well as each of their opponent's displays. 
 * @author PJF
 *
 */
public interface TournamentDisplay{
	public void withdrawAction();
	public void selectCardAction(String player, String card);
	public void setActiveTurn(boolean isActive);
	
	/** obsolete with snapshots
	public void addDisplayCard(String player, String card);
	public void removeDisplayCard(String player, String card);
	
	public void addHandCard(String card);
	public void removeHandCard(String card);
	*/
}
