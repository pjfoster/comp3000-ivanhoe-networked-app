package comp3004.ivanhoe.view;

import comp3004.ivanhoe.client.AppClient;
import comp3004.ivanhoe.util.ClientRequestBuilder;

public class ViewImpl implements View {

	AppClient client;
	ClientRequestBuilder requestBuilder;
	
	TournamentDisplay tournament;
	
	
	public ViewImpl (AppClient client) {
		this.client = client;
		requestBuilder = new ClientRequestBuilder();
	}
	
	public void displayTournamentView() {
		int players = 0; // replace with num players
		if(players == 2)
			tournament = new TwoPlayerTournament(this);
		if(players == 3)
			tournament = new ThreePlayerTournament(this);
		else if(players == 4)
			tournament = new FourPlayerTournament(this);
		else
			tournament = new FivePlayerTournament(this);
	}

	public void displayTurnView() {
		
	}

	public void displayWelcome() {
		new WelcomeDisplay(this);
		
	}

	public void launch() {
		displayWelcome();
		
	}

	public void stop() {
		
	}

	public void displayWaitingMessage() {
		// TODO Auto-generated method stub
		
	}

	public void displayStartScreen() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayChooseColor() {
		// TODO Auto-generated method stub
		
	}	
}
