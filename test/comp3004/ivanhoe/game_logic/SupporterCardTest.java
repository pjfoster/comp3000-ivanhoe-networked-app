package comp3004.ivanhoe.game_logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import comp3004.ivanhoe.controller.MockController;
import comp3004.ivanhoe.model.Player;
import comp3004.ivanhoe.model.SupporterCard;
import comp3004.ivanhoe.model.Token;
import comp3004.ivanhoe.model.Tournament;
import comp3004.ivanhoe.server.MockServer;

public class SupporterCardTest {

	HashMap<Integer, Player> players;
	Tournament tournament;
	MockController controller;
	Player alexei, luke;
	ArrayList<String> s3Wrapper, s2Wrapper, m6Wrapper;
	SupporterCard s3, s2, m6;
	
	@Before
	public void setUp() throws Exception {
		
		alexei = new Player("Alexei");
		luke = new Player("Luke");
		
		players = new HashMap<Integer, Player>();
		players.put(60001, alexei);
		players.put(60002, luke);
		
		controller = new MockController(new MockServer(), 2);
		controller.setPlayers(players);
		
		tournament = new Tournament();
		tournament.setToken(Token.RED);
		tournament.setPlayers(players);
		controller.setTournament(tournament);
		
		s2 = (SupporterCard)controller.getCardFromDeck("s2");
		s3 = (SupporterCard)controller.getCardFromDeck("s3");
		m6 = (SupporterCard)controller.getCardFromDeck("m6");
		
		s3Wrapper = new ArrayList<String>();
		s3Wrapper.add("s3");
		
		s2Wrapper = new ArrayList<String>();
		s2Wrapper.add("s2");
		
		m6Wrapper = new ArrayList<String>();
		m6Wrapper.add("m6");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSquire() {
		
		assertEquals(alexei.getDisplayTotal(tournament.getToken()), 0);
		assertEquals(luke.getDisplayTotal(tournament.getToken()), 0);
		
		controller.setTurn(60001);
		alexei.addHandCard(s2);
		assertTrue(controller.playCard(s2Wrapper));
		assertEquals(alexei.getDisplayTotal(tournament.getToken()), 2);
		
		controller.setTurn(60002);
		luke.addHandCard(s3);
		assertTrue(controller.playCard(s3Wrapper));
		assertEquals(luke.getDisplayTotal(tournament.getToken()), 3);
		
	}
	
	@Test
	public void testSquireGreen() {
		
		tournament.setToken(Token.GREEN);
		
		assertEquals(alexei.getDisplayTotal(tournament.getToken()), 0);
		assertEquals(luke.getDisplayTotal(tournament.getToken()), 0);
		
		controller.setTurn(60001);
		alexei.addHandCard(s2);
		assertTrue(controller.playCard(s2Wrapper));
		assertEquals(alexei.getDisplayTotal(tournament.getToken()), 1);
		
		controller.setTurn(60001);
		alexei.addHandCard(s3);
		assertTrue(controller.playCard(s3Wrapper));
		assertEquals(alexei.getDisplayTotal(tournament.getToken()), 2);
		
	}
	
	@Test
	public void testMaiden() {
		
		assertEquals(alexei.getDisplayTotal(tournament.getToken()), 0);
		
		controller.setTurn(60001);
		alexei.addHandCard(m6);
		assertTrue(controller.playCard(m6Wrapper));
		assertEquals(alexei.getDisplayTotal(tournament.getToken()), 6);
		
	}
	
	@Test
	public void testMaidenAlreadyPlayed() {
		assertEquals(alexei.getDisplayTotal(tournament.getToken()), 0);
		
		controller.setTurn(60001);
		alexei.addHandCard(m6);
		assertTrue(controller.playCard(m6Wrapper));
		assertEquals(alexei.getDisplayTotal(tournament.getToken()), 6);
		
		alexei.addHandCard(m6);
		assertFalse(controller.playCard(m6Wrapper));
		assertEquals(alexei.getDisplayTotal(tournament.getToken()), 6);
	}
	
	@Test
	public void testMaidenGreen() {
		
		tournament.setToken(Token.GREEN);
		
		assertEquals(alexei.getDisplayTotal(tournament.getToken()), 0);
		
		controller.setTurn(60001);
		alexei.addHandCard(m6);
		assertTrue(controller.playCard(m6Wrapper));
		assertEquals(alexei.getDisplayTotal(tournament.getToken()), 1);
		
	}

}
