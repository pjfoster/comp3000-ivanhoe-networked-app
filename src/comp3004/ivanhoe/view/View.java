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
	public void displayTurnView(String drawnCard);
	public void displayWelcome();
	public void displayStartScreen(JSONObject snapshot);
	public void displayChooseColor();
	public void displayInvalidMove();
	public void displayTurnPlayer(String playerName);
	public void announceWithdrawal(Integer playerId);
	public void displayTournamentWonMessage(String tokenColor);
	public void displayTournamentLossMessage(String winnerName);
	public void displayGameWonMessage();
	public void displayGameLossMessage(String winnerName);
	public void displayPurpleTournamentWonMessage();
	public void displayChooseToken(JSONObject server_response);
	public void displayPickOpponent();
	public void displayConnectionRejected();
	public void displayChangeTournamentColor(JSONObject server_response);
	public void displayPickCard(JSONObject server_response);
}
