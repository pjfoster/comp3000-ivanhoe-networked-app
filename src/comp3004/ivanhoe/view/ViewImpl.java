package comp3004.ivanhoe.view;

import org.json.simple.JSONObject;

import comp3004.ivanhoe.client.AppClient;
import comp3004.ivanhoe.util.ClientRequestBuilder;

public class ViewImpl implements View {

	AppClient client;
	ClientRequestBuilder requestBuilder;
	
	public ViewImpl (AppClient client) {
		this.client = client;
		requestBuilder = new ClientRequestBuilder();
	}
	
	@Override
	public void displayTournamentView(JSONObject snapshot) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayTurnView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayWelcome() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void launch() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayWaitingMessage() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayStartScreen() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayChooseColor() {
		// TODO Auto-generated method stub
		
	}

	
	
}
