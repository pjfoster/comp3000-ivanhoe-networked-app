package comp3004.ivanhoe.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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

	protected final int WAITING_FOR_MORE_PLAYERS = 1;
	protected final int WAITING_FOR_TOURNAMENT_COLOR = 2;
	protected final int WAITING_FOR_PLAYER_MOVE = 3;
	protected final int WAITING_FOR_WITHDRAW_TOKEN = 4;
	protected final int WAITING_FOR_WINNING_TOKEN = 5;
	protected final int GAME_OVER = 6;

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

	private Card lastPlayed;

	public IvanhoeController(AppServer server,
			ServerResponseBuilder responseBuilder, int maxPlayers) {
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
		lastPlayed = null;
	}

	/**
	 * Registers user as a player
	 * 
	 * @param playerId
	 *            : id of player thread, used to identify them
	 * @param playerName
	 *            : player's username
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
		} else {
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

		JSONObject startGameMessage = responseBuilder.buildStartGame(
				currentTournament, getCurrentTurnId());
		server.broadcast(startGameMessage);
	}
	
	
	public void processPlayerMove(int id, JSONObject playerMove) {
		
		// check if it's the player's turn
		if (getCurrentTurnId() != id) { invalidMove(); return; }
		
		String requestType = parser.getRequestType(playerMove);
		if (requestType == null) { invalidMove(); return; }
		
		switch (state) {
			// handle the first move, where tournament color is undecided
			case WAITING_FOR_TOURNAMENT_COLOR:
				if (requestType.equals("choose_token") && lastPlayed != null) { 
					if (!handleSetTournamentColor(playerMove)) { invalidMove(); }
				}
				
				else if (requestType.equals("turn_move")) { 
					String moveType = (String) playerMove.get("move_type");
					if (moveType.equals("withdraw")) { withdraw(); }
					else if (moveType.equals("play_card")) {
						if (!handleFirstCardMove(playerMove)) { invalidMove(); }
					}
					else { invalidMove(); }
				}
				else { invalidMove(); }
				break;
				
			// handle regular turn
			case WAITING_FOR_PLAYER_MOVE:
				if (requestType.equals("turn_move")) {
					String moveType = (String) playerMove.get("move_type");
					if (moveType.equals("withdraw")) { withdraw(); }
					else if (moveType.equals("play_card")) { 
						if (!handlePlayCard(playerMove)) { invalidMove(); }
					}
					else if (moveType.equals("play_cards")) {
						if (!handlePlayMultipleCards(playerMove)) { invalidMove(); }
					}
				}
				else { invalidMove(); }
				break;
				
			// handle case where player must give back a token in order to withdraw
			case WAITING_FOR_WITHDRAW_TOKEN:
				if (requestType.equals("choose_token")) {
					if (!handleWithdrawWithToken(playerMove)) { invalidMove(); }
				}
				else { invalidMove(); }
				break;
				
			// handle case where player can choose the color of the token they are winning
			case WAITING_FOR_WINNING_TOKEN:
				if (requestType.equals("choose_token")) { 
					if (!handleWinningToken(playerMove)) { invalidMove(); }
				}
				else { invalidMove(); }
				break;
			default:
				invalidMove();
		}
		
	}
	
	/**
	 * Sets the color of the tournament based on the color selected by the player
	 * @param tokenColor
	 * @return
	 */
	private boolean handleSetTournamentColor(JSONObject playerMove) {

		String tokenColor = (String)playerMove.get("token_color");
		if (tokenColor == null) return false;
		
		// Prevent people from choosing purple tournaments
		if (previousTournament == Token.PURPLE && tokenColor.equals("purple")) {
			return false;
		}

		currentTournament.setToken(Token.fromString(tokenColor));

		playCard(lastPlayed);
		lastPlayed = null;

		state = WAITING_FOR_PLAYER_MOVE;
		finishTurn();
		return true;

	}
	
	private boolean handleFirstCardMove(JSONObject playerMove) {
		
		ArrayList<Card> cards = getCardsInHand(parser.getCardCode(playerMove));
		if (cards == null || cards.size() != 1) return false;
		
		Card card = cards.get(0);

		// if the first card played is a Supporter Card, prompt the user to choose a color
		if (card instanceof SupporterCard) {
			lastPlayed = card;
			JSONObject chooseColor = responseBuilder
					.buildChooseColor();
			server.sendToClient(getCurrentTurnId(), chooseColor);
			return true;
		}

		if (playCard(card)) {
			state = WAITING_FOR_PLAYER_MOVE;
			finishTurn();
			return true;
		} else { 
			return false; 
		}
		
	}
	
	private boolean handlePlayCard(JSONObject playerMove) {
		if (playCard(parser.getCardCode(playerMove))) {
			finishTurn();
			return true;
		} else {
			invalidMove();
			return false;
		}
	}
	
	private boolean handlePlayMultipleCards(JSONObject playerMove) {
		if (playMultipleCards(parser.getCardCodes(playerMove))) {
			finishTurn();
			return true;
		} else {
			invalidMove();
			return false;
		}
	}
	
	private boolean handleWithdrawWithToken(JSONObject playerMove) {
		Token token = parser.getToken(playerMove);
		if (!withdraw(token)) {
			return false;
		} 
		return true;
	}
	
	private boolean handleWinningToken(JSONObject playerMove) {
		Token token = Token.fromString((String) playerMove
				.get("token_color"));

		if (token == null) { return false; }

		getCurrentTurnPlayer().addToken(token);

		if (checkGameWon()) { 
			processGameWon();	
			return true;
		}

		resetTournament(getCurrentTurnId());
		return true;
	}
	
	public void processTournamentWon() {

		Player winner = getCurrentTurnPlayer();

		if (currentTournament.getToken().equals(Token.PURPLE)) {
			state = WAITING_FOR_WINNING_TOKEN;
		} else {
			winner.addToken(currentTournament.getToken());
		}

		if (checkGameWon()) {
			processGameWon();
			return;
		}

		JSONObject tournamentWon = responseBuilder
				.buildTournamentOverWin(currentTournament.getToken().toString());
		server.sendToClient(getCurrentTurnId(), tournamentWon);

		JSONObject tournamentLoss = responseBuilder
				.buildTournamentOverLoss(winner.getName());
		for (int key : players.keySet()) {
			if (key != getCurrentTurnId()) {
				server.sendToClient(key, tournamentLoss);
			}
		}

		if (state != WAITING_FOR_WINNING_TOKEN) {
			resetTournament(getCurrentTurnId());
		}

	}

	public void processGameWon() {
		Player winner = getCurrentTurnPlayer();

		JSONObject gameWon = responseBuilder.buildGameOverWin();
		server.sendToClient(getCurrentTurnId(), gameWon);

		JSONObject gameLoss = responseBuilder.buildGameOverLoss(winner
				.getName());
		for (int key : players.keySet()) {
			if (key != getCurrentTurnId()) {
				server.sendToClient(key, gameLoss);
			}
		}

		state = GAME_OVER;
		// JSONObject quit = responseBuilder.buildQuit();
		// server.broadcast(quit);

	}

	/**
	 * Returns true if one of the players has won the game, false otherwise
	 * 
	 * @return
	 */
	public boolean checkGameWon() {

		// conditions for 2-3 players
		if (players.size() < 4) {

			for (Player p : players.values()) {
				HashSet<Token> tokens = new HashSet<Token>(p.getTokens());
				if (tokens.size() >= 5) {
					return true;
				}
			}

			return false;
		}

		// conditions for 4-5 players
		else {

			for (Player p : players.values()) {
				HashSet<Token> tokens = new HashSet<Token>(p.getTokens());
				if (tokens.size() >= 4) {
					return true;
				}
			}

			return false;
		}
	}

	/**
	 * Check if the player's hand contains all the cards they are attempting to
	 * play. If so, return a list of the cards objects they are playing. If not,
	 * return null
	 * 
	 * @param cardCodes
	 * @return
	 */
	public ArrayList<Card> getCardsInHand(ArrayList<String> cardCodes) {

		//System.out.println("Looking for " + cardCodes.size() + " cards");
		
		ArrayList<Card> finalCards = new ArrayList<Card>();

		// This is necessary if two cards are the same
		ArrayList<Card> handCopy = new ArrayList<Card>(getCurrentTurnPlayer()
				.getHand());

		for (String cardCode : cardCodes) {
			ArrayList<Card> possibleCards = currentTournament.getCard(cardCode);
			boolean foundCard = false;

			for (Card c : possibleCards) {
				if (handCopy.contains(c)) {
					handCopy.remove(c);
					finalCards.add(c);
					foundCard = true;
					break;
				}
			}
			if (!foundCard) {
				return null;
			}
		}

		return finalCards;

	}

	/**
	 * Returns the total value of the cards being played, depending on whether
	 * or not the tournament is green
	 * 
	 * @param cards
	 * @return
	 */
	public int sumCardTotal(ArrayList<Card> cards) {
		int cardTotal = 0;

		if (currentTournament.getToken().equals(Token.GREEN)) {
			for (Card c : cards) {
				cardTotal += 1;
			}
		} else {
			for (Card c : cards) {
				cardTotal += c.getValue();
			}
		}

		return cardTotal;
	}

	/**
	 * Plays each card in the list, as long as they are all valid Returns true
	 * if valid
	 * 
	 * @param cards
	 * @return
	 */
	public boolean playMultipleCards(ArrayList<String> cardCodes) {

		ArrayList<Card> cards = getCardsInHand(cardCodes);
			
		if (cards == null || cards.isEmpty()) {
			return false;
		}

		for (Card c : cards) {
			// Check color card
			if (c instanceof ColourCard) {
				if (!c.getName()
						.toLowerCase()
						.equals(currentTournament.getToken().toString()
								.toLowerCase())) {
					return false;
				}
			}
			// Check if card is a maiden
			else if (c instanceof SupporterCard) {
				if (c.getName().equals("maiden")) {
					if (getCurrentTurnPlayer().hasPlayedMaiden()) {
						return false;
					}
				}
			} else if (c instanceof ActionCard) {
				return false;
			}
		}

		int cardTotal = sumCardTotal(cards);

		// check that the new value is high enough
		int newDisplayTotal = getCurrentTurnPlayer().getDisplayTotal(
				currentTournament.getToken())
				+ cardTotal;
		if (newDisplayTotal <= currentTournament.getHighestDisplayTotal()) {
			return false;
		}

		// Play each card
		for (Card c : cards) {
			getCurrentTurnPlayer().playCard(c);
		}

		return true;
	}

	
	public boolean playCard(ArrayList<String> cardCodes) {
		ArrayList<Card> c = getCardsInHand(cardCodes);
		
		if (c == null || c.size() != 1) {
			return false;
		}
		
		return playCard(c.get(0));
	}
	
	/**
	 * Plays the card given
	 * 
	 * @param c
	 * @return
	 */
	public boolean playCard(Card c) {

		if (c instanceof ColourCard) {

			// Check that the player has the card in their hand
			ColourCard card = (ColourCard)c;

			if (card == null) {
				return false;
			}

			if (currentTournament.getToken().equals(Token.UNDECIDED)) {
				currentTournament.setToken(Token.fromString(card.getColour()));
			} else if (!card
					.getColour()
					.toLowerCase()
					.equals(currentTournament.getToken().toString()
							.toLowerCase())) {
				return false;
			}

			int newDisplayTotal = getCurrentTurnPlayer().getDisplayTotal(
					currentTournament.getToken())
					+ card.getValue();
			if (newDisplayTotal <= currentTournament.getHighestDisplayTotal()) {
				return false;
			}

			// Play the card
			getCurrentTurnPlayer().playCard(card);
			return true;

		} else if (c instanceof SupporterCard) {

			// Check that the player has the card in their hand
			SupporterCard card = (SupporterCard)c;

			if (card == null) {
				return false;
			}

			// check that the player doesn't already have a maiden
			if (card.getName().equals("maiden")) {
				if (getCurrentTurnPlayer().hasPlayedMaiden()) {
					return false;
				}
			}

			// Handle Green Tournaments
			int newDisplayTotal;
			if (currentTournament.getToken().equals(Token.GREEN)) {
				newDisplayTotal = getCurrentTurnPlayer().getDisplayTotal(
						currentTournament.getToken()) + 1;
			} else {
				newDisplayTotal = getCurrentTurnPlayer().getDisplayTotal(
						currentTournament.getToken())
						+ card.getValue();
			}
			if (newDisplayTotal <= currentTournament.getHighestDisplayTotal()) {
				return false;
			}

			// Play the card
			getCurrentTurnPlayer().playCard(card);
			return true;

		}

		else if (c instanceof ActionCard) {

		}
		return false;
	}

	/**
	 * Checks if a player has a maiden in their display; if not, withdraws them
	 * from tournament
	 */
	public void withdraw() {

		// Check if player has a maiden in their display (and they have tokens)
		if (getCurrentTurnPlayer().getTokens().size() > 0) {
			for (Card c : getCurrentTurnPlayer().getDisplay()) {
				if (c.toString().equals("m6")) {
					//System.out.println("Player has a maiden in their display...");
					state = WAITING_FOR_WITHDRAW_TOKEN;
					return;
				}
			}
		}

		handleWithdraw();
	}

	/**
	 * Removes token from player then withdraws them from tournament
	 * 
	 * @param token
	 * @return
	 */
	private boolean withdraw(Token token) {

		if (getCurrentTurnPlayer().removeToken(token)) {
			handleWithdraw();
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Handles the mechanics of withdrawing a player from a tournament
	 */
	private void handleWithdraw() {
		JSONObject announceWithdraw = responseBuilder
				.buildWithdraw(getCurrentTurnPlayer().getName());
		server.broadcast(announceWithdraw);

		currentTournament.removePlayer(getCurrentTurnId());
		playerTurns.remove(currentTurn);

		if (currentTurn == 0) {
			currentTurn = playerTurns.size() - 1;
		} else {
			currentTurn -= 1;
		}

		finishTurn();
	}

	/**
	 * Sends a snapshot of the new turn to all players Increments the turn, and
	 * sends a message to the player whose turn it now is
	 */
	public void finishTurn() {

		if (currentTournament.getPlayers().size() == 1) {
			processTournamentWon();
			return;
		}

		nextPlayerTurn();
		Card drawnCard = currentTournament.drawCard();
		getCurrentTurnPlayer().addHandCard(drawnCard);

		JSONObject newSnapshot = responseBuilder
				.buildUpdateView(currentTournament);
		server.broadcast(newSnapshot);

		JSONObject playerTurn = responseBuilder.buildStartPlayerTurn(drawnCard);
		server.sendToClient(getCurrentTurnId(), playerTurn);

		JSONObject turn = responseBuilder
				.buildIndicateTurn(getCurrentTurnPlayer().getName());
		for (int key : players.keySet()) {
			if (key != getCurrentTurnId()) {
				server.sendToClient(key, turn);
			}
		}
	}

	/**
	 * Indicates to the current player that their move is invalid (and that they
	 * should try again)
	 */
	public void invalidMove() {
		JSONObject invalidResponse = responseBuilder.buildInvalidResponse();
		server.sendToClient(getCurrentTurnId(), invalidResponse);
	}

	/**
	 * Create a new tournament, with the first turn going to the winner of the
	 * previous tournament
	 * 
	 * @param winnerId
	 */
	public void resetTournament(int winnerId) {
		state = WAITING_FOR_TOURNAMENT_COLOR;
		previousTournament = currentTournament.getToken();

		// since there is no dealer, pick a random person to choose the first
		// color
		playerTurns = new ArrayList<Integer>(players.keySet());
		currentTurn = playerTurns.indexOf(winnerId);

		currentTournament.reset(players);

		JSONObject startGameMessage = responseBuilder.buildStartTournament(
				currentTournament, getCurrentTurnId());
		server.broadcast(startGameMessage);
	}

	/**
	 * Changes whose turn it is
	 * 
	 * @return
	 */
	public int nextPlayerTurn() {
		do {
			currentTurn = (currentTurn + 1) % playerTurns.size();
		} while (!currentTournament.getPlayers()
				.containsKey(getCurrentTurnId()));
		return currentTurn;
	}

	/**
	 * Returns ID of the player whose turn it is
	 * 
	 * @return
	 */
	public int getCurrentTurnId() {
		return playerTurns.get(currentTurn);
	}

	/**
	 * Returns player whose turn it is
	 * 
	 * @return
	 */
	public Player getCurrentTurnPlayer() {
		return players.get(playerTurns.get(currentTurn));
	}

	/**
	 * Checks if the player with a given username is registered
	 * 
	 * @param username
	 * @return
	 */
	public boolean isPlayerRegistered(String username) {
		for (Player p : players.values()) {
			if (p.getName().toLowerCase().equals(username.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

}
