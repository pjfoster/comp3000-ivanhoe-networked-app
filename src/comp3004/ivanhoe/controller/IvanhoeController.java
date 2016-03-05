package comp3004.ivanhoe.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.json.simple.JSONObject;

import comp3004.ivanhoe.model.ActionCard;
import comp3004.ivanhoe.model.Card;
import comp3004.ivanhoe.model.ColourCard;
import comp3004.ivanhoe.model.Player;
import comp3004.ivanhoe.model.SupporterCard;
import comp3004.ivanhoe.model.Token;
import comp3004.ivanhoe.model.Tournament;
import comp3004.ivanhoe.server.AppServer;
import comp3004.ivanhoe.util.ServerParser;
import comp3004.ivanhoe.util.ServerResponseBuilder;

public class IvanhoeController {
	
	protected final int WAITING_FOR_MORE_PLAYERS = 		1;
	protected final int WAITING_FOR_TOURNAMENT_COLOR = 	2;
	protected final int WAITING_FOR_PLAYER_MOVE = 		3;
	protected final int WAITING_FOR_WITHDRAW_TOKEN = 	4;
	
	protected int maxPlayers;
	protected HashMap<Integer, Player> players;
	protected ArrayList<Integer> playerTurns;
	protected ServerResponseBuilder responseBuilder;
	protected ServerParser parser;
	protected AppServer server;
	protected Random rnd = new Random();
	
	protected Tournament currentTournament;
	protected Token previousTournament;
	protected int currentTurn;
	protected boolean gameWon;
	protected int state;
	
	private ArrayList<Card> lastPlayed;
	
	public IvanhoeController(AppServer server, ServerResponseBuilder responseBuilder, int maxPlayers) {
		this.server = server;
		this.responseBuilder = responseBuilder;
		this.parser = new ServerParser();
		this.maxPlayers = maxPlayers;
		players = new HashMap<Integer, Player>();
		
		gameWon = false;
		state = WAITING_FOR_MORE_PLAYERS;
		currentTournament = null;
		previousTournament = null;
		currentTurn = -1;
		lastPlayed = new ArrayList<Card>();
	}
	
	/**
	 * Registers user as a player
	 * @param playerId: id of player thread, used to identify them
	 * @param playerName: player's username
	 */
	public void addPlayer(int playerId, String playerName) {
		
		if (players.size() >= maxPlayers) {
			// Too many players; can't add any more
			return;
		}
		
		if (!players.containsKey(playerId)) {
			Player newPlayer = new Player(playerName);
			players.put(playerId, newPlayer);
		}
		
		if (players.size() == maxPlayers) {
			startGame();
		}
		else {
			JSONObject waitingForPlayers = responseBuilder.buildWaiting();
			server.sendToClient(playerId, waitingForPlayers);
		}
	
	}
	
	public void startGame() {
		
		state = WAITING_FOR_TOURNAMENT_COLOR;	
		
		// since there is no dealer, pick a random person to choose the first color
		playerTurns = new ArrayList<Integer>(players.keySet());
		int r = rnd.nextInt(players.size());
		currentTurn = r;
		
		currentTournament = new Tournament(players, Token.UNDECIDED);
		
		JSONObject startGameMessage = responseBuilder.buildStartGame(currentTournament, getCurrentTurnId());
		server.broadcast(startGameMessage);
	}
	
	public void processPlayerMove(int id, JSONObject playerMove) {
		
		switch (state) {
		case WAITING_FOR_TOURNAMENT_COLOR:
			if (getCurrentTurnId() == id) {
				
				if (parser.getRequestType(playerMove).equals("choose_token") && !lastPlayed.isEmpty()) {
					
					String color = (String)playerMove.get("token_color");
					currentTournament.setToken(Token.fromString(color));
					
					playCard(lastPlayed);
					lastPlayed = new ArrayList<Card>();
					
					state = WAITING_FOR_PLAYER_MOVE;
					finishTurn();
					return;
					
				}
				if (parser.getRequestType(playerMove).equals("turn_move")) {
					
					if (parser.getMoveType(playerMove).equals("withdraw")) {
						withdraw();
					}
					else if (parser.getMoveType(playerMove).equals("play_card")) {
						ArrayList<Card> card = parser.getCard(playerMove, currentTournament);
						
						if (card.get(0) instanceof SupporterCard) {
							lastPlayed = card;
							JSONObject chooseColor = responseBuilder.buildChooseColor();
							server.sendToClient(getCurrentTurnId(), chooseColor);
							return;
						}
						
						if (playCard(card)) {
							
							state = WAITING_FOR_PLAYER_MOVE;
							finishTurn();
							return;
						}
						else { invalidMove(); return; }
					}
		
				}
				else { invalidMove(); return; }
			}
			break;
		case WAITING_FOR_PLAYER_MOVE:
			if (getCurrentTurnId() == id) {
				
				if (((String)playerMove.get("request_type")).equals("turn_move")) {
					String moveType = (String)playerMove.get("move_type");
					
					if (moveType.equals("withdraw")) {
						withdraw();
					}
					else if (moveType.equals("play_card")) {
						ArrayList<Card> card = parser.getCard(playerMove, currentTournament);
						
						boolean success = playCard(card);
						if (success) { 
							finishTurn(); 
							return; 
						} else {
							invalidMove();
							return;
						}
						
					}

				}
			}
			break;
		default:
		}
		
	}
	
	public void processTournamentWon() {
		
		if (checkGameWon()) {
			// TODO: end the game
		}
		
		Player winner = getCurrentTurnPlayer();
		
		winner.addToken(currentTournament.getToken());
		previousTournament = currentTournament.getToken();
		
		JSONObject tournamentWon = responseBuilder.buildTournamentOverWin(currentTournament.getToken().toString());
		server.sendToClient(getCurrentTurnId(), tournamentWon);
		
		JSONObject tournamentLoss = responseBuilder.buildTournamentOverLoss(winner.getName());
		for (int key: players.keySet()) {
			if (key != getCurrentTurnId()) {
				server.sendToClient(key, tournamentLoss);
			}
		}
		
		resetTournament(getCurrentTurnId());
		
	}
	
	/**
	 * Returns true if one of the players has won the game, false otherwise
	 * @return
	 */
	public boolean checkGameWon() {
		// conditions for 2-3 players
		if (players.size() < 4) {
			return false;
		}
		
		// conditions for 4-5 players
		else {
			return false;
		}
	}
	
	public boolean playCard(ArrayList<Card> c)
	{
		
		if (c.get(0) instanceof ColourCard) {
			
			// Check that the player has the card in their hand
			ColourCard card = null;
			for (Card colourCard: c) {
				if (getCurrentTurnPlayer().hasCardInHand(colourCard)) {
					card = (ColourCard)colourCard;
					break;
				}
			}
			if (card == null) { return false; }
			
			if (currentTournament.getToken().equals(Token.UNDECIDED)) {
				currentTournament.setToken(Token.fromString(card.getColour()));
			}
			else if (!card.getColour().toLowerCase().equals(currentTournament.getToken().toString().toLowerCase())) 
			{ 
				return false; 
			}
			
			int newDisplayTotal = getCurrentTurnPlayer().getDisplayTotal(currentTournament.getToken()) + card.getValue();
			if (newDisplayTotal <= currentTournament.getHighestDisplayTotal()) { return false; }
			
			// Play the card
			getCurrentTurnPlayer().playCard(card);
			return true;

		}
		else if (c.get(0) instanceof SupporterCard) {

			// Check that the player has the card in their hand
			SupporterCard card = null;
			for (Card supporterCard: c) {
				if (getCurrentTurnPlayer().hasCardInHand(supporterCard)) {
					card = (SupporterCard)supporterCard;
					break;
				}
			}
			if (card == null) { return false; }

			// check that the player doesn't already have a maiden
			if (card.getName().equals("maiden")) {
				if (getCurrentTurnPlayer().hasPlayedMaiden()) { return false; }
			}
			
			// Handle Green Tournaments
			int newDisplayTotal;
			if (currentTournament.getToken().equals(Token.GREEN)) {
				newDisplayTotal = getCurrentTurnPlayer().getDisplayTotal(currentTournament.getToken()) + 1;
			}
			else {
				newDisplayTotal = getCurrentTurnPlayer().getDisplayTotal(currentTournament.getToken()) + card.getValue();	
			}
			if (newDisplayTotal <= currentTournament.getHighestDisplayTotal()) { return false; }
			
			// Play the card
			getCurrentTurnPlayer().playCard(card);
			return true;
			
		} 
		
		else if (c.get(0) instanceof ActionCard) {
			System.out.println("It's an action card!");
		}
		return false;
	}
	
	public void withdraw() {
		
		// TODO: check if player has a maiden in their display
		
		JSONObject announceWithdraw = responseBuilder.buildWithdraw(getCurrentTurnPlayer().getName());
		server.broadcast(announceWithdraw);
		
		currentTournament.removePlayer(getCurrentTurnId());
		playerTurns.remove(currentTurn);
		
		if (currentTurn == 0) { currentTurn = playerTurns.size() - 1; }
		else { currentTurn -= 1; }
		
		finishTurn();
	}
	
	public void invalidMove() {
		JSONObject invalidResponse = responseBuilder.buildInvalidResponse();
		server.sendToClient(getCurrentTurnId(), invalidResponse);
	}
	
	/**
	 * Sends a snapshot of the new turn to all players
	 * Increments the turn, and sends a message to the player
	 * whose turn it now is
	 */
	public void finishTurn() {
		
		if (currentTournament.getPlayers().size() == 1) {
			processTournamentWon();
			return;
		}
		
		nextPlayerTurn();
		Card drawnCard = currentTournament.drawCard();
		getCurrentTurnPlayer().addHandCard(drawnCard);
		
		JSONObject newSnapshot = responseBuilder.buildUpdateView(currentTournament);
		server.broadcast(newSnapshot);
		
		JSONObject playerTurn = responseBuilder.buildStartPlayerTurn(drawnCard);
		server.sendToClient(getCurrentTurnId(), playerTurn);
		
		JSONObject turn = responseBuilder.buildIndicateTurn(getCurrentTurnPlayer().getName());
		for (int key: players.keySet()) {
			if (key != getCurrentTurnId()) {
				server.sendToClient(key, turn);
			}
		}
	}
	
	public void resetTournament(int winnerId) {
		state = WAITING_FOR_TOURNAMENT_COLOR;	
		
		// since there is no dealer, pick a random person to choose the first color
		playerTurns = new ArrayList<Integer>(players.keySet());
		currentTurn = playerTurns.indexOf(winnerId);
		
		currentTournament = new Tournament(players, Token.UNDECIDED);
		
		JSONObject startGameMessage = responseBuilder.buildStartTournament(currentTournament, getCurrentTurnId());
		server.broadcast(startGameMessage);
	}
	
	public int nextPlayerTurn() {
		do {
			currentTurn = (currentTurn + 1) % playerTurns.size();
		} while (!currentTournament.getPlayers().containsKey(getCurrentTurnId()));
		return currentTurn;
	}
	
	/**
	 * Returns ID of the player whose turn it is
	 * @return
	 */
	public int getCurrentTurnId() {
		return playerTurns.get(currentTurn);
	}
	
	/**
	 * Returns player whose turn it is
	 * @return
	 */
	public Player getCurrentTurnPlayer() { 
		return players.get(playerTurns.get(currentTurn));			
	}
	
	public boolean isPlayerRegistered(String username) {
		for (Player p: players.values()) {
			if (p.getName().toLowerCase().equals(username.toLowerCase())) {
				return true;
			}
		}
		return false;
	}
	
}
