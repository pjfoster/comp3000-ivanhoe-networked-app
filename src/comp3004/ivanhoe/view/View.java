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
	public void displayTournamentView(JSONObject snapshot);
	public void displayTurnView();
	public void displayWelcome();
	public void displayStartScreen();
	public void displayChooseColor();
	
}
