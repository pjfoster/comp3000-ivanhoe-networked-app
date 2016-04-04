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
import comp3004.ivanhoe.util.ResponseBuilder;
import comp3004.ivanhoe.util.ServerParser;

public class IvanhoeController {

	protected final int WAITING_FOR_MORE_PLAYERS = 1;
	protected final int WAITING_FOR_TOURNAMENT_COLOR = 2;
	protected final int WAITING_FOR_PLAYER_MOVE = 3;
	protected final int WAITING_FOR_WITHDRAW_TOKEN = 4;
	protected final int WAITING_FOR_WINNING_TOKEN = 5;
	protected final int WAITING_FOR_NEW_TOURNAMENT_COLOR = 6;
	protected final int WAITING_FOR_OPPONENT_SELECTION = 7;
	protected final int WAITING_FOR_OPPONENT_CARD = 8;
	protected final int WAITING_FOR_OWN_CARD = 9;
	protected final int WAITING_FOR_ALL_CARDS = 10;
	protected final int GAME_OVER = 11;

	protected int maxPlayers;
	protected HashMap<Integer, Player> players;
	protected ArrayList<Integer> playerTurns;
	protected AppServer server;
	protected Random rnd = new Random();

	protected Tournament tournament;
	protected Token previousTournament;
	protected int currentTurn;
	protected boolean gameWon;
	protected int state;

	private ArrayList<String> lastPlayed;
	private Player selectedOpponent;

	public IvanhoeController(AppServer server, int maxPlayers) {
		this.server = server;
		this.maxPlayers = maxPlayers;
		players = new HashMap<Integer, Player>();

		gameWon = false;
		state = WAITING_FOR_MORE_PLAYERS;
		tournament = null;
		previousTournament = null;
		currentTurn = -1;
		lastPlayed = null;
		selectedOpponent = null;
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
			JSONObject waitingForPlayers = ResponseBuilder.buildWaiting();
			server.sendToClient(playerId, waitingForPlayers);
		}

	}

	public void startGame() {

		state = WAITING_FOR_TOURNAMENT_COLOR;

		// since there is no dealer, pick a random person to choose the first color
		playerTurns = new ArrayList<Integer>(players.keySet());
		int r = rnd.nextInt(players.size());
		currentTurn = r;

		tournament = new Tournament(players, Token.UNDECIDED);

		JSONObject startGameMessage = ResponseBuilder.buildStartGame(
				tournament, getCurrentTurnId());
		server.broadcast(startGameMessage);
		
		JSONObject indicateTurn = ResponseBuilder.buildIndicateTurn(getCurrentTurnPlayer().getName());
		server.broadcast(indicateTurn);
	}


	public void processPlayerMove(int id, JSONObject playerMove) {

		System.out.println("STATE: " + state + " ; CURRENT TURN: " + getCurrentTurnPlayer().getName());
		// check if it's the player's turn
		if (getCurrentTurnId() != id) { invalidMove(); return; }

		String requestType = ServerParser.getRequestType(playerMove);
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
			
		case WAITING_FOR_NEW_TOURNAMENT_COLOR:
			if (requestType.equals("choose_token")) { 
				if (!actionCardChangeTournamentColor(playerMove)) { invalidMove(); }
			}
			else { invalidMove(); }
			break;
			
		case WAITING_FOR_OPPONENT_SELECTION:
			if (requestType.equals("select_opponent")) { 
				if (!actionCardPickOpponent(playerMove)) { invalidMove(); }
			}
			else { invalidMove(); }
			break;
			
		case WAITING_FOR_OPPONENT_CARD:
			if (requestType.equals("pick_card")) { 
				if (!actionCardPickCard(playerMove)) { invalidMove(); }
			}
			else { invalidMove(); }
			break;
			
		case WAITING_FOR_OWN_CARD:
			if (requestType.equals("pick_card")) { 
				if (!actionCardOutwit(playerMove)) { invalidMove(); }
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

		tournament.setToken(Token.fromString(tokenColor));

		playCard(lastPlayed);
		lastPlayed = null;

		state = WAITING_FOR_PLAYER_MOVE;
		//finishTurn();
		return true;

	}

	/**
	 * Handles the first card move, with special rules depending on the type
	 * of card that was played
	 * @param playerMove
	 * @return
	 */
	private boolean handleFirstCardMove(JSONObject playerMove) {

		ArrayList<Card> cards = getCardsInHand(ServerParser.getCardCode(playerMove));
		if (cards == null || cards.size() != 1) return false;

		Card card = cards.get(0);

		// if the first card played is a Supporter Card, prompt the user to choose a color
		if (card instanceof SupporterCard) {
			lastPlayed = ServerParser.getCardCode(playerMove);
			JSONObject chooseColor = ResponseBuilder
					.buildChooseColor();
			server.sendToClient(getCurrentTurnId(), chooseColor);
			return true;
		}
		
		else if (card instanceof ColourCard) {
			ColourCard c = (ColourCard)card;
			if (handleColourCard(c)) {
				state = WAITING_FOR_PLAYER_MOVE;
				//finishTurn();
				return true;
			}
			else {
				return false;
			}
		} 
		
		return false;

	}

	private boolean handlePlayCard(JSONObject playerMove) {
		if (playCard(ServerParser.getCardCode(playerMove))) {
			//finishTurn();
			return true;
		} else {
			return false;
		}
	}

	private boolean handlePlayMultipleCards(JSONObject playerMove) {
		if (playMultipleCards(ServerParser.getCardCodes(playerMove))) {
			finishTurn();
			return true;
		} else {
			return false;
		}
	}

	private boolean handleWithdrawWithToken(JSONObject playerMove) {
		Token token = ServerParser.getToken(playerMove);
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

		if (tournament.getToken().equals(Token.PURPLE)) {
			state = WAITING_FOR_WINNING_TOKEN;
		} else {
			winner.addToken(tournament.getToken());
		}

		if (checkGameWon()) {
			processGameWon();
			return;
		}

		JSONObject tournamentWon = ResponseBuilder
				.buildTournamentOverWin(tournament.getToken().toString());
		server.sendToClient(getCurrentTurnId(), tournamentWon);

		JSONObject tournamentLoss = ResponseBuilder
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

		JSONObject gameWon = ResponseBuilder.buildGameOverWin();
		server.sendToClient(getCurrentTurnId(), gameWon);

		JSONObject gameLoss = ResponseBuilder.buildGameOverLoss(winner
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
			ArrayList<Card> possibleCards = tournament.getCard(cardCode);
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

		if (tournament.getToken().equals(Token.GREEN)) {
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
						.equals(tournament.getToken().toString()
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
				tournament.getToken())
				+ cardTotal;
		if (newDisplayTotal <= tournament.getHighestDisplayTotal()) {
			return false;
		}

		// Play each card
		for (Card c : cards) {
			getCurrentTurnPlayer().playCard(c);
		}

		return true;
	}

	/**
	 * Checks that the user has the correct card in hand, and
	 * then plays the card, delegating to a different function based
	 * on the type of card
	 * @param cardCodes
	 * @return
	 */
	public boolean playCard(ArrayList<String> cardCodes) {
		ArrayList<Card> c = getCardsInHand(cardCodes);

		if (c == null || c.size() != 1) {
			return false;
		}

		if (c.get(0) instanceof ColourCard) {
			ColourCard card = (ColourCard)c.get(0);
			return handleColourCard(card);
			
		} else if (c.get(0) instanceof SupporterCard) {
			SupporterCard card = (SupporterCard)c.get(0);
			return handleSupporterCard(card);
		}

		else if (c.get(0) instanceof ActionCard) {
			ActionCard card = (ActionCard)c.get(0);
			return handleActionCard(card);
		}
		
		return false;
	}

	/**
	 * Handles validation for colour cards. If the move is valid,
	 * plays the card and returns true
	 * @param card
	 * @return
	 */
	private boolean handleColourCard(ColourCard card) {

		// handle case where the tournament color hasn't been decided yet
		if (tournament.getToken().equals(Token.UNDECIDED)) {
			tournament.setToken(Token.fromString(card.getColour()));
			
		// check that the card is the right color
		} else if (!card
				.getColour()
				.toLowerCase()
				.equals(tournament.getToken().toString()
						.toLowerCase())) {
			return false;
		}

		// check that the value of the card is sufficient for it to be played
		int newDisplayTotal = getCurrentTurnPlayer().getDisplayTotal(
				tournament.getToken())
				+ card.getValue();
		if (newDisplayTotal <= tournament.getHighestDisplayTotal()) {
			return false;
		}

		// Play the card
		getCurrentTurnPlayer().playCard(card);
		finishTurn();
		return true;
	}

	/**
	 * Handles validation for supporter cards. If the move is valid,
	 * plays the card and returns true
	 * @param card
	 * @return
	 */
	private boolean handleSupporterCard(SupporterCard card) {
		
		// check that the player doesn't already have a maiden
		if (card.getName().equals("maiden")) {
			if (getCurrentTurnPlayer().hasPlayedMaiden()) {
				return false;
			}
		}

		// Handle Green Tournaments
		int newDisplayTotal;
		if (tournament.getToken().equals(Token.GREEN)) {
			newDisplayTotal = getCurrentTurnPlayer().getDisplayTotal(
					tournament.getToken()) + 1;
		} else {
			newDisplayTotal = getCurrentTurnPlayer().getDisplayTotal(
					tournament.getToken())
					+ card.getValue();
		}
		if (newDisplayTotal <= tournament.getHighestDisplayTotal()) {
			return false;
		}

		// Play the card
		getCurrentTurnPlayer().playCard(card);
		finishTurn();
		return true;

	}

	/**
	 * Handles validation for action cards. If the move is valid,
	 * plays the card and returns true
	 * @param card
	 * @return
	 */
	private boolean handleActionCard(ActionCard c) {
		
		if (c.getName().equals("unhorse")) {
			
			// check that the current tournament is a jousting tournament
			if (tournament.getToken() != Token.PURPLE) return false;
			
			HashSet<Token> choices = new HashSet<Token>();
			choices.add(Token.BLUE);
			choices.add(Token.RED);
			choices.add(Token.YELLOW);
			JSONObject choose_token = ResponseBuilder.buildChooseToken(choices);
			server.sendToClient(getCurrentTurnId(), choose_token);
			
			lastPlayed = new ArrayList<String>();
			lastPlayed.add("unhorse");
			
			state = WAITING_FOR_NEW_TOURNAMENT_COLOR;
			return true;
			
		}

		else if (c.getName().equals("changeweapon")) {
			// check that the current tournament is the right color
			if (tournament.getToken() == Token.PURPLE || tournament.getToken() == Token.GREEN) {
				return false;
			}

			HashSet<Token> choices = new HashSet<Token>();
			if (tournament.getToken() != Token.BLUE) choices.add(Token.BLUE);
			if (tournament.getToken() != Token.YELLOW) choices.add(Token.RED);
			if (tournament.getToken() != Token.RED) choices.add(Token.YELLOW);
			JSONObject choose_token = ResponseBuilder.buildChooseToken(choices);
			server.sendToClient(getCurrentTurnId(), choose_token);

			lastPlayed = new ArrayList<String>();
			lastPlayed.add("changeweapon");

			state = WAITING_FOR_NEW_TOURNAMENT_COLOR;
			return true;
		}
		
		else if (c.getName().equals("dropweapon")) {
			// check that the current tournament is the right color
			if (tournament.getToken() == Token.PURPLE || tournament.getToken() == Token.GREEN) {
				return false;
			}
			
			tournament.setToken(Token.GREEN);
			getCurrentTurnPlayer().removeHandCard(c);
			tournament.addToDiscard(c);
			finishTurn();
			return true;
			
		}
		
		else if (c.getName().equals("breaklance") || c.getName().equals("riposte") ||
				 c.getName().equals("dodge") || c.getName().equals("retreat") ||
				 c.getName().equals("knockdown") || c.getName().equals("outwit")) {
			// require player to pick an opponent
			
			lastPlayed = new ArrayList<String>();
			lastPlayed.add(c.getName());
			
			JSONObject selectOpponent = ResponseBuilder.buildPickOpponent(getCurrentTurnId(), tournament);
			server.sendToClient(getCurrentTurnId(), selectOpponent);
			
			state = WAITING_FOR_OPPONENT_SELECTION;
			return true;
		}
		
		else if (c.getName().equals("outmaneuver")) {
			// discard the top of each player's display
			for (int key: tournament.getPlayers().keySet()) {
				if (key != getCurrentTurnId() && players.get(key).getDisplay().size() > 1) {
					Card card = players.get(key).removeDisplayTop();
					tournament.addToDiscard(card);
				}
			}
			
			getCurrentTurnPlayer().removeHandCard(c);
			tournament.addToDiscard(c);
			finishTurn();
			return true;
		}
		
		else if (c.getName().equals("charge")) {
			// identify lowest-valued card in all displays
			int minVal = 20;
			Card chosenCard = null;
			for (Player p: tournament.getPlayers().values()) {
				for (Card card: p.getDisplay()) {
					if (card.getValue() < minVal) {
						chosenCard = card;
						minVal = card.getValue();
					}
				}
			}		
			// remove all instances of the chosen card from displays
			for (Player p: tournament.getPlayers().values()) {
				ArrayList<Card> displayCopy = new ArrayList<Card>(p.getDisplay());
				for (Card card: displayCopy) {
					if (p.getDisplay().size() <= 1) break;
					if (card.toString().equals(chosenCard.toString())) {
						p.removeDisplayCard(card);
						tournament.addToDiscard(card);
					}
				}
			}
			
			getCurrentTurnPlayer().removeHandCard(c);
			tournament.addToDiscard(c);
			finishTurn();
			return true;
			
		}
		
		else if (c.getName().equals("countercharge")) {
			// identify highest-valued card in all displays
			int maxVal = 0;
			Card chosenCard = null;
			for (Player p: tournament.getPlayers().values()) {
				for (Card card: p.getDisplay()) {
					if (card.getValue() > maxVal) {
						chosenCard = card;
						maxVal = card.getValue();
					}
				}
			}
			// remove all instances of the chosen card from displays
			for (Player p: tournament.getPlayers().values()) {
				ArrayList<Card> displayCopy = new ArrayList<Card>(p.getDisplay());
				for (Card card: displayCopy) {
					if (p.getDisplay().size() <= 1) break;
					if (card.toString().equals(chosenCard.toString())) {
						p.removeDisplayCard(card);
						tournament.addToDiscard(card);
					}
				}
			}
			
			getCurrentTurnPlayer().removeHandCard(c);
			tournament.addToDiscard(c);
			finishTurn();
			return true;
		}
		
		else if (c.getName().equals("disgrace")) {
			
			// remove all supporter cards from displays
			for (Player p: tournament.getPlayers().values()) {
				ArrayList<Card> displayCopy = new ArrayList<Card>(p.getDisplay());
				for (Card card: displayCopy) {
					if (p.getDisplay().size() <= 1) break;
					if (card instanceof SupporterCard) {
						p.removeDisplayCard(card);
						tournament.addToDiscard(card);
					}
				}
			}
			
			getCurrentTurnPlayer().removeHandCard(c);
			tournament.addToDiscard(c);
			finishTurn();
			return true;
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
		JSONObject announceWithdraw = ResponseBuilder
				.buildWithdraw(getCurrentTurnId());
		server.broadcast(announceWithdraw);

		tournament.removePlayer(getCurrentTurnId());
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

		if (tournament.getPlayers().size() == 1) {
			processTournamentWon();
			return;
		}

		nextPlayerTurn();
		Card drawnCard = tournament.drawCard();
		getCurrentTurnPlayer().addHandCard(drawnCard);

		JSONObject newSnapshot = ResponseBuilder
				.buildUpdateView(tournament);
		server.broadcast(newSnapshot);

		JSONObject turn = ResponseBuilder
				.buildIndicateTurn(getCurrentTurnPlayer().getName());
		server.broadcast(turn);
		/*for (int key : players.keySet()) {
			if (key != getCurrentTurnId()) {
				server.sendToClient(key, turn);
			}
		}*/
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONObject playerTurn = ResponseBuilder.buildStartPlayerTurn(drawnCard);
		server.sendToClient(getCurrentTurnId(), playerTurn);
	}

	/**
	 * Indicates to the current player that their move is invalid (and that they
	 * should try again)
	 */
	public void invalidMove() {
		JSONObject invalidResponse = ResponseBuilder.buildInvalidResponse();
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
		previousTournament = tournament.getToken();

		// since there is no dealer, pick a random person to choose the first
		// color
		playerTurns = new ArrayList<Integer>(players.keySet());
		currentTurn = playerTurns.indexOf(winnerId);

		tournament.reset(players);

		JSONObject startGameMessage = ResponseBuilder.buildStartTournament(
				tournament, getCurrentTurnId());
		server.broadcast(startGameMessage);
	}
	
	/**
	 * Handles rules for the action cards that change the tournament colour:
	 * UNHORSE and CHANGE WEAPON
	 * @param playerMove
	 * @return
	 */
	protected boolean actionCardChangeTournamentColor(JSONObject playerMove) {
	
		ArrayList<Card> cards = getCardsInHand(lastPlayed);
		if (cards == null || cards.isEmpty()) return false;
		
		Token newColor = ServerParser.getToken(playerMove);
		if (newColor == Token.GREEN || newColor == Token.PURPLE) { return false; }
		
		else if (lastPlayed.get(0).equals("changeweapon")) {
			if (newColor == tournament.getToken()) return false;
		}
		
		tournament.setToken(Token.BLUE);
		
		getCurrentTurnPlayer().removeHandCard(cards.get(0));
		tournament.addToDiscard(cards.get(0));
		state = WAITING_FOR_PLAYER_MOVE;
		finishTurn();
		
		return true;
	}
	
	/**
	 * Handles all action cards that require the player to pick a valid opponent:
	 * BREAK LANCE, RIPOSTE, DODGE, RETREAT, KNOCK DOWN, OUTWIT
	 * @param playerMove
	 * @return
	 */
	protected boolean actionCardPickOpponent(JSONObject playerMove) {
		
		// check that a valid opponent was selected
		Integer opponentId = Integer.parseInt(ServerParser.getOpponentId(playerMove));
		if (opponentId == null) return false;
		Player opponent = tournament.getPlayers().get(opponentId);
		if (opponent == null) return false;
		
		String cardName = lastPlayed.get(0);
		ActionCard actionCard = (ActionCard)tournament.getCard(cardName).get(0);
		
		if (cardName.equals("breaklance")) {
			// remove all purple cards from the opponent's display
			ArrayList<Card> displayCopy = new ArrayList<Card>(opponent.getDisplay());
			for (Card c: displayCopy) {
				if (c.toString().charAt(0) == 'p' && opponent.getDisplay().size() > 1) {
					opponent.removeDisplayCard(c);
				}
			}
			state = WAITING_FOR_PLAYER_MOVE;
			getCurrentTurnPlayer().removeHandCard(actionCard);
			lastPlayed = null;
			finishTurn();
		}
		
		else if (cardName.equals("riposte")) {
			// take the card at the top of the opponent's display
			if (opponent.getDisplay().size() <= 1) return false;
			
			Card c = opponent.getDisplayTop();
			opponent.removeDisplayCard(c);
			getCurrentTurnPlayer().addDisplayCard(c);
			
			state = WAITING_FOR_PLAYER_MOVE;
			getCurrentTurnPlayer().removeHandCard(actionCard);
			lastPlayed = null;
			finishTurn();
			
		}
		
		else if (cardName.equals("knockdown")) {
			// take a random card from the opponent's hand
			Card c = opponent.getHandRandom();
			opponent.removeHandCard(c);
			getCurrentTurnPlayer().addHandCard(c);
			
			state = WAITING_FOR_PLAYER_MOVE;
			lastPlayed = null;
			getCurrentTurnPlayer().removeHandCard(actionCard);
			finishTurn();
		}
		
		else if (cardName.equals("dodge") || cardName.equals("retreat")) {
			// select a card in the opponent's display
			JSONObject pick_card = ResponseBuilder.buildPickCard(opponent.getDisplay(), opponentId);
			server.sendToClient(getCurrentTurnId(), pick_card);
			selectedOpponent = opponent;
			state = WAITING_FOR_OPPONENT_CARD;
		}

		else if (cardName.equals("outwit")) {
			//select an opponent's faceup card
			ArrayList<Card> faceupCards = new ArrayList<Card>(opponent.getDisplay());
			//faceupCards.addAll() TODO: special cards
			
			JSONObject pick_card = ResponseBuilder.buildPickCard(faceupCards, opponentId);
			server.sendToClient(getCurrentTurnId(), pick_card);
			selectedOpponent = opponent;
			state = WAITING_FOR_OPPONENT_CARD;
		}
		
		return true;
	}
	
	/**
	 * Handles action cards where the player is required to select a card:
	 * DODGE, RETREAT, OUTWIT
	 * @param playerMove
	 * @return
	 */
	protected boolean actionCardPickCard(JSONObject playerMove) {
		
		if (selectedOpponent == null) { return false; }
		ArrayList<Card> cards = ServerParser.getCard(playerMove, tournament);
		
		String actionCardPlayed = lastPlayed.get(0);
		ActionCard actionCard = (ActionCard)tournament.getCard(actionCardPlayed).get(0);
		
		// check that the player selected a valid card
		Card selectedCard = null;
		for (Card c: cards) {
			if (selectedOpponent.getDisplay().contains(c)) {
				selectedCard = c;
				break;
			}
		}

		if (selectedCard == null) { 
			if (actionCardPlayed.equals("outwit")) {
				// check if the card is shield or stunned
			}
			return false; 
		}
		
		if (actionCardPlayed.equals("dodge")) {
			selectedOpponent.removeDisplayCard(selectedCard);
			tournament.addToDiscard(selectedCard);
			
			state = WAITING_FOR_PLAYER_MOVE;
			getCurrentTurnPlayer().removeHandCard(actionCard);
			lastPlayed = null;
			finishTurn();
		}
		
		else if (actionCardPlayed.equals("retreat")) {
			selectedOpponent.removeDisplayCard(selectedCard);
			selectedOpponent.addHandCard(selectedCard);
			
			state = WAITING_FOR_PLAYER_MOVE;
			lastPlayed = null;
			finishTurn();
		}
		
		else if (actionCardPlayed.equals("outwit")) {
			
			JSONObject pickCard = ResponseBuilder.buildPickCard(getCurrentTurnPlayer().getDisplay(), 
														        getCurrentTurnId());
			server.sendToClient(getCurrentTurnId(), pickCard);
			state = WAITING_FOR_OWN_CARD;
			lastPlayed.add(selectedCard.toString());
		}
		
		return true;
	}

	/**
	 * Handle the OUTWIT action card, where the player must select two different
	 * cards: one of their own and of their opponent's
	 * @param playerMove
	 * @return
	 */
	protected boolean actionCardOutwit(JSONObject playerMove) {
		
		if (selectedOpponent == null) { return false; }

		// get action card
		String actionCardPlayed = lastPlayed.get(0);
		ActionCard actionCard = (ActionCard)tournament.getCard(actionCardPlayed).get(0);

		// get player card		
		ArrayList<Card> cards = ServerParser.getCard(playerMove, tournament);
		Card playerCard = null;
		for (Card c: cards) {
			if (getCurrentTurnPlayer().getDisplay().contains(c)) {
				playerCard = c;
				break;
			}
		}
		if (playerCard == null) { return false; }

		// get opponent card
		cards = tournament.getCard(lastPlayed.get(1));
		Card opponentCard = null;
		for (Card c: cards) {
			if (selectedOpponent.getDisplay().contains(c)) {
				opponentCard = c;
				break;
			}
		}

		// switch cards
		selectedOpponent.removeDisplayCard(opponentCard);
		selectedOpponent.addDisplayCard(playerCard);
		
		getCurrentTurnPlayer().removeDisplayCard(playerCard);
		getCurrentTurnPlayer().addDisplayCard(opponentCard);

		state = WAITING_FOR_PLAYER_MOVE;
		lastPlayed = null;
		finishTurn();
		return true;
	}
	
	/**
	 * Changes whose turn it is
	 * 
	 * @return
	 */
	public int nextPlayerTurn() {
		do {
			currentTurn = (currentTurn + 1) % playerTurns.size();
		} while (!tournament.getPlayers()
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
	
	/**
	 * Returns the player with the given username, if they are registered
	 * in the tournment. Returns null otherwise
	 * @param username
	 * @return
	 */
	public Player getTournamentPlayer(String username) {
		for (Player p: tournament.getPlayers().values()) {
			if (p.getName().toLowerCase().equals(username.toLowerCase())) {
				return p;
			}
		}
		return null;
	}

}
