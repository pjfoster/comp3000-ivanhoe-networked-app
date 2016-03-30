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

	@SuppressWarnings("static-access")
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
				if (parser.getPlayerId(p).toString().equals(turnId.toString())) {
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
	
	@SuppressWarnings("static-access")
	@Override
	public void displayChooseToken(JSONObject server_response) {
		System.out.println("You are withdrawing with a maiden in your display...");
		System.out.println("That means you must forfeit a token.");
		System.out.println("Write choose_color <token> to select which token to give up: ");
		System.out.println("Your tokens: " + parser.getTokensFromSnapshot(server_response));
	}
	
	@SuppressWarnings("static-access")
	@Override
	public void displayTournamentView(JSONObject snapshot) {
		System.out.println();
		System.out.println("TOURNAMENT COLOR: " + parser.getColor(snapshot));
		System.out.println("Highest Display: " + parser.getHighestDisplay(snapshot));
		System.out.println("- - - - - ");
		
		ArrayList<Object> players = parser.getPlayerList(snapshot);
		for (Object p: players) {
			if (parser.getPlayerId(p) != client.getID()) {
				System.out.println(parser.getPlayerName(p));
				System.out.println("Display: " + parser.getPlayerDisplay(p) + " -- Total: " + 
												 parser.getPlayerDisplayTotal(p));
				System.out.println();
			}
		}
		for (Object p: players) {
			if (parser.getPlayerId(p) == client.getID()) {
				System.out.println("Your hand: " + parser.getPlayerHand(p));
				System.out.println("Your display: " + parser.getPlayerDisplay(p));
				System.out.println("Your display total: " + parser.getPlayerDisplayTotal(p));
				System.out.println("Your tokens: " + parser.getPlayerTokens(p));
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
		System.out.println("OR \"make_move <card1> <card2> <card3>\" to play multiple cards");
		System.out.println("Write \"make_move withdraw\" to withdraw");
	}
	
	@Override
	public void announceWithdrawal(String playerName) {
		System.out.println(playerName + " has withdrawn from the game");
	}

	public void displayWelcome() {
		System.out.println("Welcome to IVANHOE!");
	}

	@Override
	public void run() {

		while (running) {
			try {
				String text = console.readLine();
				
				if (text.contains("start_connect")) { // ex: start_connect Alexei
					client.setUsername(text.split(" ")[1]);
					client.connect();
				}
				
				else if (text.contains("choose_color")) { // ex: choose_color red
					JSONObject request = requestBuilder.buildChooseToken(text.split(" ")[1]);
					client.handleClientRequest(request);
				}
				
				else if (text.contains("make_move")) {
					
					String[] movesList = text.split(" ");
					String move = movesList[1];
					JSONObject request = null;
					
					if (move.equals("withdraw")) {
						request = requestBuilder.buildWithdrawMove();
					}
					else {
						if (movesList.length <= 2) {
							request = requestBuilder.buildCardMove(move);
						}
						else {
							request = requestBuilder.buildMultipleCardsMove(movesList);
						}
					}
					
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
				
			}
		}
		
	}

	@Override
	public void stop() {
		System.out.println("Closing client!");
		running = false;
	}

	@Override
	public void displayTurnPlayer(String playerName) {
		System.out.println("It is " + playerName + "'s turn");
	}

	@Override
	public void displayTournamentWonMessage(String tokenColor) {
		System.out.println("YOU WIN!!!");
		System.out.println("You get a " + tokenColor.toUpperCase() + " token!");
	}

	@Override
	public void displayTournamentLossMessage(String winnerName) {
		System.out.println("The winner of the tournament is " + winnerName);
	}

	@Override
	public void displayGameWonMessage() {
		System.out.println("***** CONGRATULATIONS ****");
		System.out.println("You won the game!");
	}
		
	public void exit() {
		System.out.println("Closing");
	}

	@Override
	public void displayGameLossMessage(String winnerName) {
		System.out.println("***** GAME OVER ****");
		System.out.println("The winner of the game is: " + winnerName);
	}

	@Override
	public void displayPurpleTournamentWonMessage() {
		System.out.println("YOU WIN!!!");
		System.out.println("Since this is a purple tournament, you get to choose what color token you win.");
		System.out.println("Write \"choose_color <color>\" to pick: ");
		
	}

	@Override
	public void displayConnectionRejected() {
		// TODO Auto-generated method stub
		
	}

}
