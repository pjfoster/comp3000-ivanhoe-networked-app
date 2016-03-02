package comp3004.ivanhoe.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.json.simple.JSONObject;

import comp3004.ivanhoe.client.AppClient;
import comp3004.ivanhoe.util.ClientParser;
import comp3004.ivanhoe.util.ClientRequestBuilder;

/**
 * Text-based UI. Used for testing purposes
 * @author PJF
 *
 */
public class TextViewImpl implements View, Runnable {

	AppClient client;
	ClientRequestBuilder requestBuilder;
	ClientParser parser;
	BufferedReader console;
	boolean running = false;
	
	public TextViewImpl (AppClient client, ClientRequestBuilder requestBuilder) {
		this.client = client;
		this.requestBuilder = requestBuilder;
		
		console = new BufferedReader(new InputStreamReader(System.in));
		parser = new ClientParser();
	}
	
	@Override
	public void displayWaitingMessage() {
		System.out.println("Please wait for other players to connect...");
	}
	
	@Override
	public void launch() {
		running = true;
		System.out.println("Launching view... Pretend a window is opening");
		System.out.println("Write start_connect <username> to start");
		run();
	}

	@Override
	public void displayStartScreen(JSONObject snapshot) {
		System.out.println("The Game is beginning!");
		
		displayTournamentView(snapshot);
		Integer turnId = Integer.parseInt((String)snapshot.get("current_turn"));
		
		if (turnId == client.getID()) {
			System.out.println("You get to go first!");
			System.out.println("That means whatever card you play will determine the color of the tournament...");
			displayTurnView();
		}
		
		else {
			for (Object p: parser.getPlayerList(snapshot)) {
				System.out.println(parser.getPlayerId(p) + " vs " + turnId);
				if (parser.getPlayerId(p).equals(turnId)) {
					System.out.println("Current turn: " + parser.getPlayerName(p));
				}
			}
		}
		
	}
	
	@Override
	public void displayInvalidMove() {
		System.out.println("INVALID MOVE - try again");
	}
	
	@Override
	public void displayChooseColor() {
		System.out.println("Choose the color of the tournament: ");
		System.out.println("Write choose_color <color> to start: ");
	}
	
	@Override
	public void displayTournamentView(JSONObject snapshot) {
		System.out.println();
		System.out.println("TOURNAMENT COLOR: " + parser.getColor(snapshot));
		System.out.println("- - - - - ");
		
		ArrayList<Object> players = parser.getPlayerList(snapshot);
		for (Object p: players) {
			if (parser.getPlayerId(p) != client.getID()) {
				System.out.println(parser.getPlayerName(p));
				System.out.println("Display: " + parser.getPlayerDisplay(p));
				System.out.println();
				break;
			}
		}
		for (Object p: players) {
			if (parser.getPlayerId(p) == client.getID()) {
				System.out.println("Your hand: " + parser.getPlayerHand(p));
				System.out.println("Your display: " + parser.getPlayerDisplay(p));
				System.out.println("Your tokens: " + parser.getPlayerTokens(p));
				break;
			}
		}
		System.out.println("- - - - - ");
		System.out.println();
	}

	@Override
	public void displayTurnView(String drawnCard) {
		System.out.println("You drew " + drawnCard);
		displayTurnView();
	}
	
	@Override
	public void displayTurnView() {
		System.out.println("It's your turn!");
		System.out.println("Write \"make_move <card>\" to play a card");
		System.out.println("Write \"make_move withdraw\" to withdraw");
	}

	@Override
	public void displayWelcome() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run() {

		while (running) {
			try {
				String text = console.readLine();
				
				if (text.contains("start_connect")) { // ex: start_connect Alexi
					client.setUsername(text.split(" ")[1]);
					client.connect();
				}
				
				else if (text.contains("choose_color")) { // ex: choose_color red
					JSONObject request = requestBuilder.buildChooseToken(text.split(" ")[1]);
					client.handleClientRequest(request);
				}
				
				else if (text.contains("make_move")) {
					
					String move = text.split(" ")[1];
					JSONObject request = null;
					
					if (move.equals("withdraw")) {
						request = requestBuilder.buildWithdrawMove();
					}
					else {
						request = requestBuilder.buildCardMove(move);
					}
					/*else if (move.charAt(0) == 's') {
						request = requestBuilder.buildSupporterCardMove(move);
					}
					else if (move.charAt(0) == 'm') {
						request = requestBuilder.buildSupporterCardMove(move);
					}
					else if (move.charAt(0) == 'r' || move.charAt(0) == 'b' || move.charAt(0) == 'g' ||
							 move.charAt(0) == 'y' || move.charAt(0) == 'p') {
						request = requestBuilder.buildColorCardMove(move);
					}
					else {
						request = requestBuilder.buildActionCardMove(move);
					}*/
					client.handleClientRequest(request);
				}
				
				else {
					System.out.println(text);
				}
			}
			catch (IOException e) {  
	         	System.out.println("ERROR reading console input");
	         	running = false;
	         }
			catch (NullPointerException np) {
				System.out.println("Accidentally tried to read null...");
			}
		}
		
	}

	@Override
	public void stop() {
		System.out.println("Closing client!");
		running = false;
	}

}
