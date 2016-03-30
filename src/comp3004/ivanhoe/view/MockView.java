package comp3004.ivanhoe.view;

import org.json.simple.JSONObject;

public class MockView implements View {

	@Override
	public void launch() { }
	
	@Override
	public void stop() { }

	@Override
	public void displayWaitingMessage() { }

	@Override
	public void displayTournamentView(JSONObject snapshot) { }

	@Override
	public void displayTurnView() { }

	@Override
	public void displayWelcome() { }

	@Override
	public void displayStartScreen(JSONObject snapshot) { }

	@Override
	public void displayChooseColor() { }

	@Override
	public void displayInvalidMove() { }

	@Override
	public void displayTurnView(String drawnCard) { }

	@Override
	public void displayTurnPlayer(String playerName) { }

	@Override
	public void announceWithdrawal(String string) { }

	@Override
	public void displayTournamentWonMessage(String tokenColor) { }

	@Override
	public void displayTournamentLossMessage(String winnerName) { }

	@Override
	public void displayGameWonMessage() { }

	@Override
	public void displayGameLossMessage(String string) { }
	
	@Override
	public void displayPurpleTournamentWonMessage() { }

	@Override
	public void displayChooseToken(JSONObject server_response) { }

	@Override
	public void displayConnectionRejected() {
		// TODO Auto-generated method stub
		
	}

}
