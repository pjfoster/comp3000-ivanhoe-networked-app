package comp3004.ivanhoe.view.gui;

import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.json.simple.JSONObject;

import comp3004.ivanhoe.client.AppClient;
import comp3004.ivanhoe.util.ClientRequestBuilder;
import comp3004.ivanhoe.view.View;

@SuppressWarnings("serial")
public class GUIView extends JFrame implements View {

	private AppClient client;
	private ClientRequestBuilder requestBuilder;
	private JPanel mainPanel;
	
	public GUIView(AppClient client, ClientRequestBuilder requestBuilder) {
		super("Ivanhoe");
		
		this.setSize(800, 600);
		this.setLayout(new FlowLayout());
		
	    this.addWindowListener(new WindowAdapter() {
	         public void windowClosing(WindowEvent windowEvent){
		        System.exit(0);
	         }        
	     });    
	    
	    mainPanel = new WelcomeView();
	    
	    this.add(mainPanel);
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
	public void displayWaitingMessage() {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
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
