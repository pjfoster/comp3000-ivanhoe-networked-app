package comp3004.ivanhoe.controller;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import comp3004.ivanhoe.model.ColourCard;
import comp3004.ivanhoe.model.Player;
import comp3004.ivanhoe.model.Token;
import comp3004.ivanhoe.model.Tournament;
import comp3004.ivanhoe.server.MockServer;
import comp3004.ivanhoe.util.RequestBuilder;

public class FinishTurnTest {

	HashMap<Integer, Player> players;
	Tournament tournament;
	MockController controller;
	Player alexei, luke, jayson;
	
	@Before
	public void setUp() throws Exception {
		alexei = new Player("Alexei");
		luke = new Player("Luke");
		jayson = new Player("Jayson");
		
		players = new HashMap<Integer, Player>();
		players.put(60001, alexei);
		players.put(60002, luke);
		players.put(60003, jayson);
		
		controller = new MockController(new MockServer(), 2);
		controller.setPlayers(players);
		
		tournament = new Tournament();
		tournament.setToken(Token.RED);
		tournament.setPlayers(players);
		controller.setTournament(tournament);
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test that the finish turn method correctly switches to the next 
	 * player's turn
	 */
	@Test
	public void testFinishTurn() {
		// set it to Alexei's turn
		alexei.addDisplayCard(new ColourCard("red", 3));
		controller.setTurn(60001);
		controller.finishTurn();
		
		Player p2 = controller.getCurrentTurnPlayer();
		assertNotEquals(p2, alexei);
		
		p2.addDisplayCard(new ColourCard("red", 5));
		controller.finishTurn();
		assertNotEquals(controller.getCurrentTurnPlayer(), alexei);
		assertNotEquals(controller.getCurrentTurnPlayer(), p2);
		
	}
	
	/**
	 * Test that you can't finish a turn if you don't have the highest
	 * display in the tournament
	 */
	@Test
	public void testFinishTurnInvalid() {
		// set it to Alexei's turn
		controller.setTurn(60001);
		
		JSONObject finishTurn = RequestBuilder.buildFinishTurn();
		controller.processPlayerMove(60001, finishTurn);
		
		// check it is still Alexei's turn
		assertEquals(controller.getCurrentTurnPlayer(), alexei);
		
		alexei.addDisplayCard(new ColourCard("red", 3));
		controller.processPlayerMove(60001, finishTurn);
		
		// check that it is no longer Alexei's turn
		assertNotEquals(controller.getCurrentTurnPlayer(), alexei);
	}

}
