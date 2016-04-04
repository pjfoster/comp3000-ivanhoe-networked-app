package comp3004.ivanhoe.controller;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import comp3004.ivanhoe.model.Player;
import comp3004.ivanhoe.model.Token;
import comp3004.ivanhoe.model.Tournament;
import comp3004.ivanhoe.server.MockServer;
import comp3004.ivanhoe.util.ResponseBuilder;

public class TurnTest {

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

	@Test
	public void testTurns() {
		controller.startGame();
		ArrayList<Player> processedPlayers = new ArrayList<Player>();
		
		for (int i = 0; i < 12; ++i) {
			assertTrue(controller.getCurrentTurn() < 3);
			processedPlayers.add(controller.getCurrentTurnPlayer());
			controller.nextPlayerTurn();
		}
		
		for (int i = 0; i < 3; ++i) {
			Player p = processedPlayers.get(i);
			assertEquals(p, processedPlayers.get(3+i));
			assertEquals(p, processedPlayers.get(6+i));
			assertEquals(p, processedPlayers.get(9+i));
		}
		
	}
	
	@Test
	public void testTurnsRemovedPlayer() {
		controller.startGame();
		ArrayList<Player> processedPlayers = new ArrayList<Player>();
		controller.setTurn(60001);
		controller.withdraw();
		
		for (int i = 0; i < 8; ++i) {
			assertTrue(controller.getCurrentTurn() < 3);
			processedPlayers.add(controller.getCurrentTurnPlayer());
			controller.nextPlayerTurn();
		}
		
		for (int i = 0; i < 2; ++i) {
			Player p = processedPlayers.get(i);
			assertTrue(!p.equals(alexei));
			assertEquals(p, processedPlayers.get(2+i));
			assertEquals(p, processedPlayers.get(4+i));
			assertEquals(p, processedPlayers.get(6+i));
		}
	}

}
