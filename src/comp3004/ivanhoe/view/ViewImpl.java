package comp3004.ivanhoe.view;

import org.json.simple.JSONObject;

import comp3004.ivanhoe.client.AppClient;
import comp3004.ivanhoe.util.ClientRequestBuilder;
import java.io.IOException;

import javax.swing.*;

import org.json.simple.JSONObject;

import comp3004.ivanhoe.client.*;
import comp3004.ivanhoe.model.*;
import comp3004.ivanhoe.util.*;

public class ViewImpl implements View{	
	AppClient client;
	ClientRequestBuilder requestBuilder;
	boolean running = false;
	String username;
	
	IvanhoeFrame currentWindow;
	
	JSONObject snapshot;
	
	public ViewImpl(AppClient client, ClientRequestBuilder requestBuilder, JSONObject snapshot, String username){
		this.client = client;
		this.requestBuilder = requestBuilder;
		this.snapshot = snapshot;
		this.username = username;
	}
	
	@Override
	public void displayTournamentView(JSONObject snapshot) {
		// TODO Auto-generated method stub
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
			((TournamentDisplay)currentWindow).setActiveTurn(true);
	}


	public void stop() {
		currentWindow.dispose();
		running = false;
	}

	public void update(JSONObject snapshot) {
		this.snapshot = snapshot;
		currentWindow.refresh(snapshot);
	}

	@Override
	public void displayStartScreen(JSONObject snapshot) {
		// TODO Auto-generated method stub
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

	@Override
	public void displayInvalidMove() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayTurnView(String drawnCard) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayTurnPlayer(String playerName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void announceWithdrawal(String string) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayTournamentWonMessage(String tokenColor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayTournamentLossMessage(String winnerName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayGameWonMessage() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayGameLossMessage(String string) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayPurpleTournamentWonMessage() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayChooseToken(JSONObject server_response) {
		// TODO Auto-generated method stub
		
	}
	
	public void withdraw(){
		JSONObject request = requestBuilder.buildWithdrawMove();
		try {
			client.handleClientRequest(request);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(currentWindow, "Failed Move or Action");
		}
	}
	
	public void selectCard(String player, String card){
		JSONObject request = null;
		
		try {
			client.handleClientRequest(request);
			((TournamentDisplay)currentWindow).setActiveTurn(false);
			
		} catch (IOException e) {
			JOptionPane.showMessageDialog(currentWindow, "Failed Move or Action");
		}
	}

	@Override
	public void displayWelcome() {
		// TODO Auto-generated method stub
		
	}

}
