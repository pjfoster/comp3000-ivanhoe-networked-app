package comp3004.ivanhoe.view.gui;

import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.json.simple.JSONObject;

import comp3004.ivanhoe.client.AppClient;
import comp3004.ivanhoe.util.ClientParser;
import comp3004.ivanhoe.util.RequestBuilder;
import comp3004.ivanhoe.util.Strings;
import comp3004.ivanhoe.view.View;

@SuppressWarnings("serial")
public class GUIView extends JFrame implements View {

	private AppClient client;
	private RequestBuilder requestBuilder;
	private JPanel mainPanel;
	String announcement;
	
	public GUIView(AppClient client) {
		super("Ivanhoe");
		this.client = client;
		
		this.setSize(800, 600);
		this.setResizable(false);
		this.setLayout(new FlowLayout());
		
	    this.addWindowListener(new WindowAdapter() {
	         public void windowClosing(WindowEvent windowEvent){
		        System.exit(0);
	         }        
	     });    
	    
	    mainPanel = new WelcomeView(this);
	    
	    this.add(mainPanel);
	}
	
	public RequestBuilder getRequestBuilder() {
		return requestBuilder;
	}
	
	public Integer getId() {
		return client.getID();
	}
	
	public void connect(String username) {
		client.setUsername(username);
		client.connect();
	}
	
	public void handleEvent(JSONObject request) {
		try {
			System.out.println("Handling request!: " + request);
			client.handleClientRequest(request);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void launch() {
		this.setVisible(true);  
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayConnectionRejected() {
		// check that the main panel is still the welcome view
		if (mainPanel instanceof WelcomeView) {
			((WelcomeView) mainPanel).setErrorMessage(Strings.connection_rejected);
		}
	}

	@Override
	public void displayWaitingMessage() {
		// check that the main panel is still the welcome view
		if (mainPanel instanceof WelcomeView) {
			((WelcomeView) mainPanel).setMessage(Strings.connection_accepted_waiting);
		}
	}

	@Override
	public void displayTournamentView(JSONObject snapshot) {
		//System.out.println("UPDATING TOURNAMENT VIEW " + client.getID());
		if (mainPanel instanceof TournamentView) {
			((TournamentView) mainPanel).updateView(snapshot);
		}
	}

	@Override
	public void displayTurnView() {
		if (mainPanel instanceof TournamentView)  {
			((TournamentView) mainPanel).displayPlayerTurn(null);
		}
	}

	@Override
	public void displayTurnView(String drawnCard) {
		if (mainPanel instanceof TournamentView)  {
			((TournamentView) mainPanel).displayPlayerTurn(drawnCard);
		}	
	}

	@Override
	public void displayWelcome() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayStartScreen(JSONObject snapshot) {
		this.remove(mainPanel);
		
		TournamentView tournamentView = new TournamentView(this, snapshot);
		mainPanel = tournamentView;
		this.add(mainPanel);
		
		this.repaint();
		this.revalidate();
		
		if (announcement != null) {
			((TournamentView) mainPanel).updateStats(null, null, announcement);
			announcement = null;
		}
		
		if (ClientParser.getCurrentTurnFromSnapshot(snapshot) == client.getID()) {
			displayTurnView();
		}
	}

	@Override
	public void displayChooseColor() {
		ArrayList<String> tokens = new ArrayList<String>();
		tokens.add("red");
		tokens.add("blue");
		tokens.add("green");
		tokens.add("yellow");
		tokens.add("purple");
		PickColourWindow colourView = new PickColourWindow(this, tokens, Strings.choose_color);
		((TournamentView) mainPanel).setLastMove(colourView);
		colourView.setVisible(true);	
	}
	
	@Override
	public void displayPickOpponent() {
		if (mainPanel instanceof TournamentView)  {
			((TournamentView) mainPanel).displaySelectOpponent();
		}	
	}
	
	@Override
	public void displayPickCard(JSONObject response) {
		
		Integer playerId = ClientParser.getPlayerIdFromSnapshot(response);
		ArrayList<String> cards = ClientParser.getCardsFromPickCard(response);
		
		PickCardWindow pickCardWindow;
		if (playerId == client.getID()) {
			pickCardWindow = new PickCardWindow(this, cards, Strings.pick_own_card);
		}
		else {
			pickCardWindow = new PickCardWindow(this, cards, Strings.pick_opponent_card);
		}
		
		((TournamentView) mainPanel).setLastMove(pickCardWindow);
		pickCardWindow.setVisible(true);
	}

	@Override
	public void displayInvalidMove() {
		((TournamentView) mainPanel).displayInvalidMove();
	}

	@Override
	public void displayTurnPlayer(String playerName) {
		if (mainPanel instanceof TournamentView) {
			((TournamentView) mainPanel).updateStats(playerName, null, null);
		}
	}

	@Override
	public void announceWithdrawal(Integer playerId) {
		if (mainPanel instanceof TournamentView) {
			((TournamentView) mainPanel).withdrawPlayer(playerId);
		}
	}

	@Override
	public void displayTournamentWonMessage(String tokenColor) {
		announcement = Strings.tournament_won;
		((TournamentView) mainPanel).updateStats(null, null, Strings.tournament_won);
	}

	@Override
	public void displayTournamentLossMessage(String winnerName) {
		String message = String.format(Strings.tournament_loss, winnerName);
		announcement = message;
		((TournamentView) mainPanel).updateStats(null, null, message);
		
	}

	@Override
	public void displayGameWonMessage() {
		this.remove(mainPanel);
		mainPanel = new GameWonPanel();
		this.add(mainPanel);
		
		this.repaint();
		this.revalidate();
	}

	@Override
	public void displayGameLossMessage(String winnerName) {
		this.remove(mainPanel);
		mainPanel = new GameLostPanel(winnerName);
		this.add(mainPanel);
		
		this.repaint();
		this.revalidate();
	}

	@Override
	public void displayPurpleTournamentWonMessage() {
		ArrayList<String> tokens = new ArrayList<String>();
		tokens.add("red");
		tokens.add("blue");
		tokens.add("green");
		tokens.add("yellow");
		tokens.add("purple");
		PickColourWindow colourView = new PickColourWindow(this, tokens, Strings.tournament_won_purple);
		((TournamentView) mainPanel).setLastMove(colourView);
		announcement = Strings.tournament_won;
		colourView.setVisible(true);	
	}

	@Override
	public void displayChooseToken(JSONObject server_response) {
		ArrayList<String> tokens = ClientParser.getTokensFromChooseColor(server_response);
		PickColourWindow colourView = new PickColourWindow(this, tokens, Strings.choose_token);
		colourView.setVisible(true);
	}

	@Override
	public void displayChangeTournamentColor(JSONObject server_response) {
		ArrayList<String> tokens = ClientParser.getTokensFromChooseColor(server_response);
		PickColourWindow colourView = new PickColourWindow(this, tokens, Strings.change_tournament_color);
		colourView.setVisible(true);
	}

	@Override
	public void createAnnouncement(String message) {
		((TournamentView) mainPanel).updateStats(null, null, message);
	}

}
