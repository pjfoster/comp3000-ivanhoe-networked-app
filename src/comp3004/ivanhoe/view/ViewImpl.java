package comp3004.ivanhoe.view;

import javax.swing.JFrame;

import org.json.simple.JSONObject;

import comp3004.ivanhoe.client.*;
import comp3004.ivanhoe.model.*;
import comp3004.ivanhoe.util.*;

public class ViewImpl implements View{	
	AppClient client;
	ClientRequestBuilder requestBuilder;
	boolean running = false;
	
	JFrame currentWindow;
	
	JSONObject snapshot;
	
	public ViewImpl(AppClient client, ClientRequestBuilder requestBuilder, JSONObject snapshot){
		this.client = client;
		this.requestBuilder = requestBuilder;
		this.snapshot = snapshot;
	}
	
	public void displayWaitingMessage() {
		if(currentWindow!=null)
			currentWindow.dispose();
		currentWindow = new WaitingDisplay(this);
	}
	
	
	public void launch() {
		running = true;
		displayStartScreen();
	}

	public void displayStartScreen() {
		if(currentWindow!=null)
			currentWindow.dispose();
		currentWindow = new StartDisplay(this);
	}
	
	
	public void displayChooseColor() {
		System.out.println("Choose the color of the next tournament: ");
		if(currentWindow!=null)
			currentWindow.dispose();
		currentWindow = new ChooseColorDisplay();
	}
	
	
	public void displayTournamentView() {
		if(currentWindow!=null)
			currentWindow.dispose();

		int players = ClientParser.getPlayerList(snapshot).size(); // replace with num players
		if(players == 2)
			currentWindow = new TwoPlayerTournament(this);
		if(players == 3)
			currentWindow = new ThreePlayerTournament(this);
		else if(players == 4)
			currentWindow = new FourPlayerTournament(this);
		else
			currentWindow = new FivePlayerTournament(this);
		
	}

	
	public void displayTurnView() {
		if(currentWindow!=null)
			currentWindow.dispose();
	}


	public void stop() {
		currentWindow.dispose();
		running = false;
	}

	public void update(JSONObject snapshot) {
		this.snapshot = snapshot;
	}

	public void exit() {
		currentWindow.dispose();
		System.exit(0);
	}
	
	
	public void chooseColor(){
		//JSONObject request = requestBuilder.buildChooseToken();
		//client.handleClientRequest(request);
	}
	
	public void connect(String username){
		client.setUsername(username);
		client.connect();
	}

}