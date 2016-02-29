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

public class SupporterCardTest {

	HashMap<Integer, Player> players;
	ServerResponseBuilder responseBuilder = new ServerResponseBuilder();
	Tournament tournament;
	Player alexei, luke;
	
	@Before
	public void setUp() throws Exception {
		
		alexei = new Player("Alexei");
		luke = new Player("Luke");
		
		players = new HashMap<Integer, Player>();
		players.put(60001, alexei);
		players.put(60002, luke);
		
		MockController controller = new MockController(null, responseBuilder, 2);
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
	public void testSquire() {
		
		assertEquals(alexei.getDisplayTotal(), 0);
		assertEquals(luke.getDisplayTotal(), 0);
		
		SupporterCard s2 = new SupporterCard("squire", 2);
		SupporterCard s3 = new SupporterCard("squire", 3);
		
		alexei.addHandCard(s2);
		assertTrue(alexei.playCard(s2));
		assertEquals(alexei.getDisplayTotal(), 2);
		
		luke.addHandCard(s3);
		assertTrue(luke.playCard(s3));
		assertEquals(luke.getDisplayTotal(), 3);
		
	}
	
	@Test
	public void testSquireGreen() {
		
		tournament.setToken(Token.GREEN);
		
		assertEquals(alexei.getDisplayTotal(), 0);
		assertEquals(luke.getDisplayTotal(), 0);
		
		SupporterCard s2 = new SupporterCard("squire", 2);
		SupporterCard s3 = new SupporterCard("squire", 3);
		
		alexei.addHandCard(s2);
		assertTrue(alexei.playCard(s2));
		assertEquals(alexei.getDisplayTotal(), 1);
		
		luke.addHandCard(s3);
		assertTrue(luke.playCard(s3));
		assertEquals(luke.getDisplayTotal(), 1);
		
	}
	
	@Test
	public void testMaiden() {
		
		assertEquals(alexei.getDisplayTotal(), 0);
		
		SupporterCard m6 = new SupporterCard("maiden", 6);
		
		alexei.addHandCard(m6);
		assertTrue(alexei.playCard(m6));
		assertEquals(alexei.getDisplayTotal(), 6);
		
	}
	
	@Test
	public void testMaidenGreen() {
		
		tournament.setToken(Token.GREEN);
		
		assertEquals(alexei.getDisplayTotal(), 0);
		
		SupporterCard m6 = new SupporterCard("maiden", 6);
		
		alexei.addHandCard(m6);
		assertTrue(alexei.playCard(m6));
		assertEquals(alexei.getDisplayTotal(), 1);
		
	}

}
