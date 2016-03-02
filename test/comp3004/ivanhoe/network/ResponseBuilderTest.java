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

import comp3004.ivanhoe.model.Player;
import comp3004.ivanhoe.model.Token;
import comp3004.ivanhoe.model.Tournament;
import comp3004.ivanhoe.util.ClientParser;
import comp3004.ivanhoe.util.ServerResponseBuilder;

public class ResponseBuilderTest {
	
	JSONParser parser = new JSONParser();
	ClientParser clientParser = new ClientParser();
	ServerResponseBuilder responseBuilder = new ServerResponseBuilder();
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
		String temp = responseBuilder.createGameSnapshot(responseMap, tournament).toJSONString();
		
		JSONObject snapshot = (JSONObject)parser.parse(temp);

		ArrayList<Object> players = clientParser.getPlayerList(snapshot);
		assertEquals(players.size(), 2);
		assertEquals(clientParser.getDeck(snapshot).size(), 94);
		assertEquals(clientParser.getColor(snapshot).toLowerCase(), "red");
		
		for (Object p: players) {
			Long id = clientParser.getPlayerId(p);
			assertEquals(clientParser.getPlayerHand(p).size(), 8);
			assertEquals(clientParser.getPlayerDisplay(p).size(), 0);
			assertEquals(clientParser.getPlayerTokens(p).size(), 0);
			
			if (id == 60001) {
				assertEquals(clientParser.getPlayerName(p), "Alexei");
			}
			else {
				assertEquals(clientParser.getPlayerName(p), "Luke");
			}
			
		}	
		
	}
	
	@Test
	public void testConnectionAccepted() throws ParseException {
		String testMoveString = responseBuilder.buildConnectionAccepted().toJSONString();
		JSONObject testMove = (JSONObject)parser.parse(testMoveString);
		
		assertEquals(testMove.size(), 1);
		assertEquals(testMove.get("response_type"), "connection_accepted");
	}
	
	@Test
	public void testConnectionRejected() throws ParseException {
		String testMoveString = responseBuilder.buildConnectionRejected().toJSONString();
		JSONObject testMove = (JSONObject)parser.parse(testMoveString);
		
		assertEquals(testMove.size(), 1);
		assertEquals(testMove.get("response_type"), "connection_rejected");
	}
	
	@Test
	public void testChooseColor() throws ParseException {
		String testMoveString = responseBuilder.buildChooseColor().toJSONString();
		JSONObject testMove = (JSONObject)parser.parse(testMoveString);
		
		assertEquals(testMove.size(), 1);
		assertEquals(testMove.get("response_type"), "choose_color");
	}
	
	@Test
	public void testStartTournament() throws ParseException {
		String testMoveString = responseBuilder.buildStartTournament(tournament, 0).toJSONString();
		JSONObject testMove = (JSONObject)parser.parse(testMoveString);
		
		assertEquals(testMove.size(), 5);
		assertEquals(testMove.get("response_type"), "start_tournament");
		assertEquals(testMove.get("turn"), "0");
		assertNotNull(testMove.get("players"));
		assertNotNull(testMove.get("tournament_color"));
		assertNotNull(testMove.get("deck"));
	}
	
	@Test
	public void testStartPlayerTurn() throws ParseException {
		String testMoveString = responseBuilder.buildStartPlayerTurn().toJSONString();
		JSONObject testMove = (JSONObject)parser.parse(testMoveString);
		
		assertEquals(testMove.size(), 1);
		assertEquals(testMove.get("response_type"), "start_player_turn");
	}
	
	@Test
	public void testUpdateView() throws ParseException {
		String testMoveString = responseBuilder.buildUpdateView(tournament).toJSONString();
		JSONObject testMove = (JSONObject)parser.parse(testMoveString);
		
		assertEquals(testMove.size(), 4);
		assertEquals(testMove.get("response_type"), "update_view");
		assertNotNull(testMove.get("players"));
		assertNotNull(testMove.get("tournament_color"));
		assertNotNull(testMove.get("deck"));
	}
	
	@Test
	public void testTournamentOverWin() throws ParseException {
		String testMoveString = responseBuilder.buildTournamentOverWin().toJSONString();
		JSONObject testMove = (JSONObject)parser.parse(testMoveString);
		
		assertEquals(testMove.size(), 1);
		assertEquals(testMove.get("response_type"), "tournament_over_win");
	}
	
	@Test
	public void testTournamentOverLoss() throws ParseException {
		String testMoveString = responseBuilder.buildTournamentOverLoss().toJSONString();
		JSONObject testMove = (JSONObject)parser.parse(testMoveString);
		
		assertEquals(testMove.size(), 1);
		assertEquals(testMove.get("response_type"), "tournament_over_loss");
	}
	
	@Test
	public void testQuit() throws ParseException {
		String testMoveString = responseBuilder.buildQuit().toJSONString();
		JSONObject testMove = (JSONObject)parser.parse(testMoveString);
		
		assertEquals(testMove.size(), 1);
		assertEquals(testMove.get("response_type"), "quit");
	}

}
