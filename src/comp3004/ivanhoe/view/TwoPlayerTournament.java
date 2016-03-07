package comp3004.ivanhoe.view;

import java.awt.Color;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

import org.json.simple.JSONObject;

import comp3004.ivanhoe.util.ClientParser;

@SuppressWarnings("serial")
public class TwoPlayerTournament extends IvanhoeFrame implements TournamentDisplay{

	private ViewImpl view;
	
	private JButton withdraw;
	
	private CardPanel playerHand;
	private CardPanel playerDisplay, opponentDisplay;
	
	private DrawPile drawPile;
	
	private String username;
	private String opponent;
	
	public TwoPlayerTournament(ViewImpl view){
		this.view = view;
		
		setSize(800,600);
		setBackground(Color.GREEN);
		setLayout(null);
		setResizable(false);
		
		withdraw = new JButton("Start");
		withdraw.setBounds(0,550,100,50);
		withdraw.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				withdrawAction();	
			}
		});
		withdraw.setVisible(true);
		add(withdraw);

		ArrayList<Object> players = ClientParser.getPlayerList(view.snapshot);
		if(players.get(0)==view.username){
			username = view.username;
			opponent = (String)players.get(1);
		}
		else{ 
			username = view.username;
			opponent = (String)players.get(0);
		}
		
		playerHand = new CardPanel(this,ClientParser.getPlayerHand(view.username),"h",100,500,600,100);
		playerHand.setVisible(true);
		add(playerHand);

		playerDisplay = new CardPanel(this,ClientParser.getPlayerDisplay(view.username),"h",100,400,600,100);
		playerDisplay.setVisible(true);
		add(playerDisplay);

		opponentDisplay = new CardPanel(this,ClientParser.getPlayerDisplay(opponent),"h",100,0,600,100);
		opponentDisplay.setVisible(true);
		add(opponentDisplay);
		
		drawPile = new DrawPile(this, ClientParser.getDeck(view.snapshot),350,250,100,100);
			
		setVisible(true);
	}
	
	public void selectCardAction(String player, String card){
			view.selectCard(player, card);
	}

	public void withdrawAction() {
		view.withdraw();
	}
	
	public void refresh(JSONObject snapshot) {
		playerHand.refresh(ClientParser.getPlayerHand(username));

		playerDisplay.refresh(ClientParser.getPlayerDisplay(username));

		opponentDisplay.refresh(ClientParser.getPlayerDisplay(opponent));
		
		drawPile.refresh(ClientParser.getDeck(view.snapshot));
	}
	
	public void setActiveTurn(boolean isActive){
		playerHand.setEnabled(isActive);
		
		drawPile.setEnabled(isActive);
	}
	
	/**
	public void addDisplayCard(String player, String card) {
		if(view.client.getUsername()==player){
			playerDisplay.addCard(card);
		}
		else{
			opponentDisplay.addCard(card);
		}
	}
	
	public void addHandCard(String card){
		playerHand.addCard(card);
	}

	public void removeDisplayCard(String player, String card) {
		if(view.client.getUsername()==player){
			playerDisplay.removeCard(card);
		}
		else{
			opponentDisplay.removeCard(card);
		}
	}
	
	public void removeHandCard(String card){
		playerHand.removeCard(card);
	}
	*/
}
