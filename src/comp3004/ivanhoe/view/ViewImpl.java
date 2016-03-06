package comp3004.ivanhoe.view;

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
	
	public void withdraw(){
		JSONObject request = requestBuilder.buildWithdrawMove();
		try {
			client.handleClientRequest(request);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(currentWindow, "Failed Move or Action");
		}
	}
	
	public void selectCard(String player, String card){
		JSONObject request;
		if(0==0){ //actionCard
			request = requestBuilder.buildActionCardMove(card);
		}
		
		else if(0==0){ // supporterCard
			request = requestBuilder.buildSupporterCardMove("", "");
		} 
		
		else if(0==0){ // colour card
			request = requestBuilder.buildColorCardMove("", "");
		}
		
		
		try {
			client.handleClientRequest(request);
			((TournamentDisplay)currentWindow).setActiveTurn(false);
			
		} catch (IOException e) {
			JOptionPane.showMessageDialog(currentWindow, "Failed Move or Action");
		}
	}

}