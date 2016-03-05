package comp3004.ivanhoe.game_logic;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import comp3004.ivanhoe.controller.MockController;
import comp3004.ivanhoe.model.Player;
import comp3004.ivanhoe.model.SupporterCard;
import comp3004.ivanhoe.model.Token;
import comp3004.ivanhoe.model.Tournament;
import comp3004.ivanhoe.util.ServerResponseBuilder;

public class WithdrawTest {

	HashMap<Integer, Player> players;
	ServerResponseBuilder responseBuilder = new ServerResponseBuilder();
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
		
		controller = new MockController(null, responseBuilder, 2);
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
	public void testBasicWithdraw() {
		
		assertEquals(tournament.getPlayers().size(), 3);
		//assertTrue(controller.withdraw(60003));
		assertEquals(tournament.getPlayers().size(), 2);
		assertFalse(tournament.getPlayers().values().contains(jayson));
		
	}
	
	@Test
	public void testWithdrawMaidenNoToken() {
		
		assertEquals(tournament.getPlayers().size(), 3);
		
		SupporterCard m6 = new SupporterCard("maiden", 6);
		jayson.addHandCard(m6);
		//assertTrue(controller.withdraw(60003));
		
		assertEquals(tournament.getPlayers().size(), 2);
		assertFalse(tournament.getPlayers().values().contains(jayson));
		
	}
	
	@Test
	public void testWithdrawMaidenWithToken() {
		
		assertEquals(tournament.getPlayers().size(), 3);
		
		SupporterCard m6 = new SupporterCard("maiden", 6);
		jayson.addHandCard(m6);
		jayson.addToken(Token.BLUE);
		//assertFalse(controller.withdraw(60003));
		
		assertEquals(tournament.getPlayers().size(), 3);
		assertFalse(tournament.getPlayers().values().contains(jayson));
		
	}

}
