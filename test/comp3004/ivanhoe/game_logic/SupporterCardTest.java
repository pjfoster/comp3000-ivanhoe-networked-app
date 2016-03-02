package comp3004.ivanhoe.game_logic;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import comp3004.ivanhoe.controller.MockController;
import comp3004.ivanhoe.model.Card;
import comp3004.ivanhoe.model.Player;
import comp3004.ivanhoe.model.SupporterCard;
import comp3004.ivanhoe.model.Token;
import comp3004.ivanhoe.model.Tournament;
import comp3004.ivanhoe.util.ServerResponseBuilder;

public class SupporterCardTest {

	HashMap<Integer, Player> players;
	ServerResponseBuilder responseBuilder = new ServerResponseBuilder();
	Tournament tournament;
	MockController controller;
	Player alexei, luke;
	ArrayList<Card> s3Wrapper, s2Wrapper, m6Wrapper;
	SupporterCard s3, s2, m6;
	
	@Before
	public void setUp() throws Exception {
		
		alexei = new Player("Alexei");
		luke = new Player("Luke");
		
		players = new HashMap<Integer, Player>();
		players.put(60001, alexei);
		players.put(60002, luke);
		
		controller = new MockController(null, responseBuilder, 2);
		controller.setPlayers(players);
		
		tournament = new Tournament();
		tournament.setToken(Token.RED);
		tournament.setPlayers(players);
		controller.setTournament(tournament);
		
		s2 = new SupporterCard("squire", 2);
		s3 = new SupporterCard("squire", 3);
		m6 = new SupporterCard("maiden", 6);
		
		s3Wrapper = new ArrayList<Card>();
		s3Wrapper.add(s3);
		
		s2Wrapper = new ArrayList<Card>();
		s2Wrapper.add(s2);
		
		m6Wrapper = new ArrayList<Card>();
		m6Wrapper.add(m6);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSquire() {
		
		assertEquals(alexei.getDisplayTotal(), 0);
		assertEquals(luke.getDisplayTotal(), 0);
		
		controller.setTurn(60001);
		alexei.addHandCard(s2);
		assertTrue(controller.playCard(s2Wrapper));
		assertEquals(alexei.getDisplayTotal(), 2);
		
		controller.setTurn(60002);
		luke.addHandCard(s3);
		assertTrue(controller.playCard(s3Wrapper));
		assertEquals(luke.getDisplayTotal(), 3);
		
	}
	
	@Test
	public void testSquireGreen() {
		
		tournament.setToken(Token.GREEN);
		
		assertEquals(alexei.getDisplayTotal(), 0);
		assertEquals(luke.getDisplayTotal(), 0);
		
		controller.setTurn(60001);
		alexei.addHandCard(s2);
		assertTrue(controller.playCard(s2Wrapper));
		assertEquals(alexei.getDisplayTotal(), 1);
		
		controller.setTurn(60002);
		luke.addHandCard(s3);
		assertTrue(controller.playCard(s3Wrapper));
		assertEquals(luke.getDisplayTotal(), 1);
		
	}
	
	@Test
	public void testMaiden() {
		
		assertEquals(alexei.getDisplayTotal(), 0);
		
		controller.setTurn(60001);
		alexei.addHandCard(m6);
		assertTrue(controller.playCard(m6Wrapper));
		assertEquals(alexei.getDisplayTotal(), 6);
		
	}
	
	@Test
	public void testMaidenGreen() {
		
		tournament.setToken(Token.GREEN);
		
		assertEquals(alexei.getDisplayTotal(), 0);
		
		controller.setTurn(60001);
		alexei.addHandCard(m6);
		assertTrue(controller.playCard(m6Wrapper));
		assertEquals(alexei.getDisplayTotal(), 1);
		
	}

}
