package comp3004.ivanhoe.view;

import org.json.simple.JSONObject;

import comp3004.ivanhoe.client.AppClient;

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
	public void displayTurnView(String drawnCard);
	public void displayWelcome();
	public void displayStartScreen(JSONObject snapshot);
	public void displayStartScreen();
	public void displayChooseColor();
	public void displayInvalidMove();
	public void displayTurnPlayer(String playerName);
	public void announceWithdrawal(String playerName);
	public void displayTournamentWonMessage(String tokenColor);
	public void displayTournamentLossMessage(String winnerName);
	public void displayGameWonMessage();
	public void displayGameLossMessage(String winnerName);
	public void displayPurpleTournamentWonMessage();
	public void displayChooseToken(JSONObject server_response);
	
	
	public void update(JSONObject snapshot);
	public void exit();
	public void withdraw();
	public void selectCard(String player, String card);
}
