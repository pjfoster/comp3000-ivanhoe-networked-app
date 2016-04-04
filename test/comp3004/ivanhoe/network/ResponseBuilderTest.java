package comp3004.ivanhoe.network;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import comp3004.ivanhoe.model.ActionCard;
import comp3004.ivanhoe.model.Card;
import comp3004.ivanhoe.model.ColourCard;
import comp3004.ivanhoe.model.Player;
import comp3004.ivanhoe.model.SupporterCard;
import comp3004.ivanhoe.model.Token;
import comp3004.ivanhoe.model.Tournament;
import comp3004.ivanhoe.util.ClientParser;
import comp3004.ivanhoe.util.ResponseBuilder;

/**
 * Tests JSONObjects created by ResponseBuilder
 * Also tests that the client can parse and extract values correctly
 * @author PJF
 *
 */
public class ResponseBuilderTest {
	
	JSONParser parser = new JSONParser();
	HashMap<Integer, Player> players;
	Tournament tournament;
	
	@Before
	public void setUp() throws Exception {
		
		players = new HashMap<Integer, Player>();
		players.put(60001, new Player("Alexei"));
		players.put(60002, new Player("Luke"));
		
		tournament = new Tournament(players, Token.RED);
		
	}

	@After
	public void tearDown() throws Exception {
		players = null;
		tournament = null;
	}

	@Test
	public void testCreateGameSnapshot() throws ParseException {
		HashMap<String, String> responseMap = new HashMap<String, String>();
		String temp = ResponseBuilder.createGameSnapshot(responseMap, tournament).toJSONString();
		
		JSONObject snapshot = (JSONObject)parser.parse(temp);

		ArrayList<Object> players = ClientParser.getPlayerList(snapshot);
		assertEquals(players.size(), 2);
		assertEquals(ClientParser.getColorFromSnapshot(snapshot).toLowerCase(), "red");
		assertEquals(ClientParser.getHighestDisplayFromSnapshot(snapshot), "0");
		
		for (Object p: players) {
			Integer id = ClientParser.getPlayerId(p).intValue();
			assertEquals(ClientParser.getPlayerHand(p).size(), 8);
			assertEquals(ClientParser.getPlayerDisplay(p).size(), 0);
			assertEquals(ClientParser.getPlayerTokens(p).size(), 0);
			
			if (id == 60001) {
				assertEquals(ClientParser.getPlayerName(p), "Alexei");
			}
			else {
				assertEquals(ClientParser.getPlayerName(p), "Luke");
			}
			
		}	
		
	}
	
	@Test
	public void testConnectionAccepted() throws ParseException {
		String testMoveString = ResponseBuilder.buildConnectionAccepted().toJSONString();
		JSONObject testMove = (JSONObject)parser.parse(testMoveString);
		
		assertEquals(testMove.size(), 1);
		assertEquals(testMove.get("response_type"), "connection_accepted");
	}
	
	@Test
	public void testConnectionRejected() throws ParseException {
		String testMoveString = ResponseBuilder.buildConnectionRejected().toJSONString();
		JSONObject testMove = (JSONObject)parser.parse(testMoveString);
		
		assertEquals(testMove.size(), 1);
		assertEquals(testMove.get("response_type"), "connection_rejected");
	}
	
	@Test
	public void testChooseColor() throws ParseException {
		String testMoveString = ResponseBuilder.buildChooseColor().toJSONString();
		JSONObject testMove = (JSONObject)parser.parse(testMoveString);
		
		assertEquals(testMove.size(), 1);
		assertEquals(testMove.get("response_type"), "choose_color");
	}
	
	@Test
	public void testPickOpponent() throws ParseException {
		
		HashMap<Integer, Player> players = new HashMap<Integer, Player>();
		players.put(60001, new Player("Alexei"));
		players.put(60002, new Player("Emma"));
		players.put(60003, new Player("Luke"));
		
		Tournament tournament = new Tournament();
		tournament.setPlayers(players);
		
		// Build a repsonse with Alexei as current player
		String pickOpponent = ResponseBuilder.buildPickOpponent(60001, tournament).toJSONString();
		JSONObject testMove = (JSONObject)parser.parse(pickOpponent);
		
		assertEquals(testMove.size(), 2);
		assertEquals(testMove.get("response_type"), "pick_opponent");
		assertNotNull(testMove.get("opponents"));
		
		ArrayList<String> opponents = ClientParser.getOpponentsFromSelectOpponent(testMove);
		assertEquals(opponents.size(), 2);
		assertTrue(opponents.contains("Emma"));
		assertTrue(opponents.contains("Luke"));
		assertFalse(opponents.contains("Alexei"));
		
		// Build a repsonse with Luke as current player
		pickOpponent = ResponseBuilder.buildPickOpponent(60003, tournament).toJSONString();
		testMove = (JSONObject)parser.parse(pickOpponent);

		assertEquals(testMove.size(), 2);
		assertEquals(testMove.get("response_type"), "pick_opponent");
		assertNotNull(testMove.get("opponents"));

		opponents = ClientParser.getOpponentsFromSelectOpponent(testMove);
		assertEquals(opponents.size(), 2);
		assertTrue(opponents.contains("Emma"));
		assertFalse(opponents.contains("Luke"));
		assertTrue(opponents.contains("Alexei"));
	}
	
	@Test
	public void testPickCard() throws ParseException {
		ArrayList<Card> cards = new ArrayList<Card>();
		cards.add(new ColourCard("red", 3));
		cards.add(new ColourCard("blue", 5));
		cards.add(new SupporterCard("squire", 3));
		cards.add(new SupporterCard("maiden", 6));
		cards.add(new ActionCard("changeweapon"));
		cards.add(new ActionCard("ivanhoe"));
		
		String testMoveString = ResponseBuilder.buildPickCard(cards, 60001).toJSONString();
		JSONObject testMove = (JSONObject)parser.parse(testMoveString);
		
		assertEquals(testMove.size(), 2);
		assertEquals(testMove.get("response_type"), "pick_card");
		assertNotNull(testMove.get("cards"));
		
		ArrayList<String> cardStrings = ClientParser.getCardsFromPickCard(testMove);
		assertEquals(cardStrings.size(), 6);
		assertTrue(cardStrings.contains("r3"));
		assertTrue(cardStrings.contains("b5"));
		assertTrue(cardStrings.contains("s3"));
		assertTrue(cardStrings.contains("m6"));
		assertTrue(cardStrings.contains("changeweapon"));
		assertTrue(cardStrings.contains("ivanhoe"));
	}
	
	@Test
	public void testChooseToken() throws ParseException {
		
		// Test for single token
		Player emma = new Player("Emma");
		emma.addToken(Token.BLUE);
		
		String testMoveString = ResponseBuilder.buildChooseToken(emma.getTokens()).toJSONString();
		JSONObject testMove = (JSONObject)parser.parse(testMoveString);
		
		assertEquals(testMove.size(), 2);
		assertEquals(testMove.get("response_type"), "choose_token");
		assertNotNull(testMove.get("tokens"));
		
		ArrayList<String> tokens = ClientParser.getTokensFromChooseColor(testMove);
		assertEquals(tokens.size(), 1);
		assertEquals(tokens.get(0).toLowerCase(), "blue");
		
		// Test for multiple tokens, same color
		emma.addToken(Token.BLUE);
		
		testMoveString = ResponseBuilder.buildChooseToken(emma.getTokens()).toJSONString();
		testMove = (JSONObject)parser.parse(testMoveString);
		
		assertEquals(testMove.size(), 2);
		assertEquals(testMove.get("response_type"), "choose_token");
		assertNotNull(testMove.get("tokens"));
		
		tokens = ClientParser.getTokensFromChooseColor(testMove);
		assertEquals(tokens.size(), 1);
		assertEquals(tokens.get(0).toLowerCase(), "blue");
		
		// Test for multiple tokens, mutliple colors
		emma.addToken(Token.RED);
		emma.addToken(Token.YELLOW);
		
		testMoveString = ResponseBuilder.buildChooseToken(emma.getTokens()).toJSONString();
		testMove = (JSONObject)parser.parse(testMoveString);
		
		assertEquals(testMove.size(), 2);
		assertEquals(testMove.get("response_type"), "choose_token");
		assertNotNull(testMove.get("tokens"));
		
		tokens = ClientParser.getTokensFromChooseColor(testMove);
		assertEquals(tokens.size(), 3);
		for (String token: tokens) {
			assertTrue(token.toLowerCase().equals("blue") ||
					   token.toLowerCase().equals("red") ||
					   token.toLowerCase().equals("yellow"));
		}
	
		
	}
	
	@Test
	public void testStartTournament() throws ParseException {
		String testMoveString = ResponseBuilder.buildStartTournament(tournament, 0).toJSONString();
		JSONObject testMove = (JSONObject)parser.parse(testMoveString);
		
		assertEquals(testMove.size(), 6);
		assertEquals(testMove.get("response_type"), "start_tournament");
		assertEquals(testMove.get("current_turn"), "0");
		assertNotNull(testMove.get("players"));
		assertNotNull(testMove.get("tournament_color"));
		assertNotNull(testMove.get("deck"));
		assertNotNull(testMove.get("highest_display"));
	}
	
	@Test
	public void testStartPlayerTurn() throws ParseException {
		Card r3 = new ColourCard("red", 3);
		String testMoveString = ResponseBuilder.buildStartPlayerTurn(r3).toJSONString();
		JSONObject testMove = (JSONObject)parser.parse(testMoveString);
		
		assertEquals(testMove.size(), 2);
		assertEquals(testMove.get("response_type"), "start_player_turn");
		assertEquals(testMove.get("drawn_card"), "r3");
	}
	
	@Test
	public void testIndicateTurn() throws ParseException {
		String testMoveString = ResponseBuilder.buildIndicateTurn("Jayson").toJSONString();
		JSONObject testMove = (JSONObject)parser.parse(testMoveString);
		
		assertEquals(testMove.size(), 2);
		assertEquals(testMove.get("response_type"), "indicate_turn");
		assertEquals(testMove.get("player_name"), "Jayson");
		
	}
	
	@Test
	public void testUpdateView() throws ParseException {
		String testMoveString = ResponseBuilder.buildUpdateView(tournament).toJSONString();
		JSONObject testMove = (JSONObject)parser.parse(testMoveString);
		
		assertEquals(testMove.size(), 5);
		assertEquals(testMove.get("response_type"), "update_view");
		assertNotNull(testMove.get("players"));
		assertNotNull(testMove.get("tournament_color"));
		assertNotNull(testMove.get("deck"));
		assertNotNull(testMove.get("highest_display"));
	}
	
	@Test
	public void testTournamentOverWin() throws ParseException {
		String testMoveString = ResponseBuilder.buildTournamentOverWin("red").toJSONString();
		JSONObject testMove = (JSONObject)parser.parse(testMoveString);
		
		assertEquals(testMove.size(), 2);
		assertEquals(testMove.get("response_type"), "tournament_over_win");
		assertEquals(testMove.get("token_color"), "red");
	}
	
	@Test
	public void testTournamentOverLoss() throws ParseException {
		String testMoveString = ResponseBuilder.buildTournamentOverLoss("Jayson").toJSONString();
		JSONObject testMove = (JSONObject)parser.parse(testMoveString);
		
		assertEquals(testMove.size(), 2);
		assertEquals(testMove.get("response_type"), "tournament_over_loss");
		assertEquals(testMove.get("winner"), "Jayson");
	}
	
	@Test
	public void testGameOverWin() throws ParseException {
		String testMoveString = ResponseBuilder.buildGameOverWin().toJSONString();
		JSONObject testMove = (JSONObject)parser.parse(testMoveString);
		
		assertEquals(testMove.size(), 1);
		assertEquals(testMove.get("response_type"), "game_over_win");
	}
	
	@Test
	public void testGameOverLoss() throws ParseException {
		String testMoveString = ResponseBuilder.buildGameOverLoss("Jayson").toJSONString();
		JSONObject testMove = (JSONObject)parser.parse(testMoveString);
		
		assertEquals(testMove.size(), 2);
		assertEquals(testMove.get("response_type"), "game_over_loss");
		assertEquals(testMove.get("winner"), "Jayson");
	}
	
	@Test
	public void testWaiting() throws ParseException {
		String testMoveString = ResponseBuilder.buildWaiting().toJSONString();
		JSONObject testMove = (JSONObject)parser.parse(testMoveString);
		
		assertEquals(testMove.size(), 1);
		assertEquals(testMove.get("response_type"), "waiting");
	}
	
	@Test
	public void testWithdraw() throws ParseException {
		String testMoveString = ResponseBuilder.buildWithdraw(60002).toJSONString();
		JSONObject testMove = (JSONObject)parser.parse(testMoveString);
		
		assertEquals(testMove.size(), 2);
		assertEquals(testMove.get("response_type"), "withdraw");
		assertEquals(testMove.get("player_id"), 60002);
	}
	
	@Test
	public void testQuit() throws ParseException {
		String testMoveString = ResponseBuilder.buildQuit().toJSONString();
		JSONObject testMove = (JSONObject)parser.parse(testMoveString);
		
		assertEquals(testMove.size(), 1);
		assertEquals(testMove.get("response_type"), "quit");
	}

}
