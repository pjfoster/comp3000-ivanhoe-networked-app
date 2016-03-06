package comp3004.ivanhoe.view;

import org.json.simple.JSONObject;

public interface View {

	/**
	 * Opens view
	 */
	public void launch();
	
	/**
	 * Closes view
	 */
	public void stop();
	
	public void displayWaitingMessage();
	public void displayTournamentView();
	public void displayTurnView();
	public void displayStartScreen();
	public void displayChooseColor();
	
	public void update(JSONObject snapshot);
	public void exit();
	
}
