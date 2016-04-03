package comp3004.ivanhoe.view.gui;

import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.json.simple.JSONObject;

import comp3004.ivanhoe.client.AppClient;
import comp3004.ivanhoe.util.ClientParser;
import comp3004.ivanhoe.util.ClientRequestBuilder;
import comp3004.ivanhoe.util.Strings;
import comp3004.ivanhoe.view.View;

@SuppressWarnings("serial")
public class GUIView extends JFrame implements View {

	private AppClient client;
	private ClientRequestBuilder requestBuilder;
	private ClientParser parser;
	private JPanel mainPanel;
	
	public GUIView(AppClient client, ClientRequestBuilder requestBuilder) {
		super("Ivanhoe");
		this.requestBuilder = requestBuilder;
		this.parser = new ClientParser();
		this.client = client;
		
		this.setSize(800, 600);
		this.setLayout(new FlowLayout());
		
	    this.addWindowListener(new WindowAdapter() {
	         public void windowClosing(WindowEvent windowEvent){
		        System.exit(0);
	         }        
	     });    
	    
	    mainPanel = new WelcomeView(this);
	    
	    this.add(mainPanel);
	}
	
	public ClientRequestBuilder getRequestBuilder() {
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayTurnView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayTurnView(String drawnCard) {
		// TODO Auto-generated method stub
		
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
		
		if (parser.getCurrentTurn(snapshot) == client.getID()) {
			displayTurnView();
		}
	}

	@Override
	public void displayChooseColor() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayInvalidMove() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayTurnPlayer(String playerName) {
		if (mainPanel instanceof TournamentView) {
			((TournamentView) mainPanel).updateStats(playerName, null);
		}
	}

	@Override
	public void announceWithdrawal(String playerName) {
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
	public void displayGameLossMessage(String winnerName) {
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

}
