package comp3004.ivanhoe.game_logic;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import comp3004.ivanhoe.controller.MockController;
import comp3004.ivanhoe.model.Card;
import comp3004.ivanhoe.model.Player;
import comp3004.ivanhoe.model.SupporterCard;
import comp3004.ivanhoe.model.Token;
import comp3004.ivanhoe.model.Tournament;
import comp3004.ivanhoe.server.MockServer;
import comp3004.ivanhoe.util.ClientRequestBuilder;
import comp3004.ivanhoe.util.ServerResponseBuilder;

public class WithdrawTest {

	HashMap<Integer, Player> players;
	ServerResponseBuilder responseBuilder = new ServerResponseBuilder();
	ClientRequestBuilder requestBuilder = new ClientRequestBuilder();
	Tournament tournament;
	MockController controller;
	Player alexei, luke, jayson;
	Card m6;
	ArrayList<Card> m6Wrapper;

	@Before
	public void setUp() throws Exception {

		alexei = new Player("Alexei");
		luke = new Player("Luke");
		jayson = new Player("Jayson");

		players = new HashMap<Integer, Player>();
		players.put(60001, alexei);
		players.put(60002, luke);
		players.put(60003, jayson);

		controller = new MockController(new MockServer(), responseBuilder, 2);
		controller.setPlayers(players);

		tournament = new Tournament();
		tournament.setToken(Token.RED);
		tournament.setPlayers(players);
		controller.setTournament(tournament);

		m6 = controller.getCardFromDeck("m6");
		m6Wrapper = new ArrayList<Card>();
		m6Wrapper.add(m6);

	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Tests that a player can withdraw from a tournament when they have no
	 * maidens in their hand
	 */
	@Test
	public void testBasicWithdraw() {

		controller.setTurn(60003);
		assertEquals(tournament.getPlayers().size(), 3);

		controller.withdraw();

		// Check that the player has been removed from the tournament but not the game
		assertEquals(tournament.getPlayers().size(), 2);
		assertEquals(controller.getPlayers().size(), 3);

		// Check that the turn has been correctly updated
		assertTrue(controller.getCurrentTurn() < 2);
		assertTrue(controller.getCurrentTurn() >= 0);
		assertFalse(tournament.getPlayers().values().contains(jayson));
		assertTrue(controller.getCurrentTurnPlayer() != jayson);

	}

	/**
	 * Tests that a player can withdraw from a tournament when they have a
	 * maidens in their hand BUT no tokens
	 */
	@Test
	public void testWithdrawMaidenNoToken() {

		controller.setTurn(60003);
		assertEquals(tournament.getPlayers().size(), 3);
		jayson.addDisplayCard(m6);

		controller.withdraw();

		// Check that the player has been removed from the tournament but not the game
		assertEquals(tournament.getPlayers().size(), 2);
		assertEquals(controller.getPlayers().size(), 3);

		// Check that the turn has been correctly updated
		assertTrue(controller.getCurrentTurn() < 2);
		assertTrue(controller.getCurrentTurn() >= 0);
		assertFalse(tournament.getPlayers().values().contains(jayson));
		assertTrue(controller.getCurrentTurnPlayer() != jayson);

	}

	/**
	 * Tests the case where a player withdraws from a tournament with a maiden in their
	 * hand and a single token
	 */
	@Test
	public void testWithdrawMaidenWithSingleToken() {

		controller.setTurn(60003);
		assertEquals(tournament.getPlayers().size(), 3);

		SupporterCard m6 = new SupporterCard("maiden", 6);
		jayson.addDisplayCard(m6);
		jayson.addToken(Token.BLUE);
		assertEquals(jayson.getTokens().size(), 1);

		controller.withdraw();

		assertEquals(controller.getState(), 4); // WAITING_FOR_WITHDRAW_TOKEN
		assertEquals(controller.getCurrentTurnPlayer(), jayson); // Turn has not changed yet

		JSONObject chooseToken = requestBuilder.buildChooseToken("blue");
		controller.processPlayerMove(60003, chooseToken);

		// check that Jayson no longer has the blue token, but all other tokens
		assertEquals(jayson.getTokens().size(), 0);

		// Check that the player has been removed from the tournament but not the game
		assertEquals(tournament.getPlayers().size(), 2);
		assertEquals(controller.getPlayers().size(), 3);

		// Check that the turn has been correctly updated
		assertTrue(controller.getCurrentTurn() < 2);
		assertTrue(controller.getCurrentTurn() >= 0);
		assertFalse(tournament.getPlayers().values().contains(jayson));
		assertTrue(controller.getCurrentTurnPlayer() != jayson);

	}
	
	/**
	 * Tests that a player can withdraw from a tournament when they have a
	 * maidens in their hand and more than one token (with no repeated colours)
	 */
	@Test
	public void testWithdrawMaidenWithMultipleTokens() {
		controller.setTurn(60003);
		assertEquals(tournament.getPlayers().size(), 3);

		SupporterCard m6 = new SupporterCard("maiden", 6);
		jayson.addDisplayCard(m6);
		jayson.addToken(Token.BLUE);
		jayson.addToken(Token.RED);
		jayson.addToken(Token.GREEN);
		assertEquals(jayson.getTokens().size(), 3);

		controller.withdraw();

		assertEquals(controller.getState(), 4); // WAITING_FOR_WITHDRAW_TOKEN
		assertEquals(controller.getCurrentTurnPlayer(), jayson); // Turn has not changed yet

		JSONObject chooseToken = requestBuilder.buildChooseToken("blue");
		controller.processPlayerMove(60003, chooseToken);

		// check that Jayson no longer has the blue token, but all other tokens
		assertEquals(jayson.getTokens().size(), 2);
		for (Token t: jayson.getTokens()) {
			assertNotEquals(t, Token.BLUE);
		}

		// Check that the player has been removed from the tournament but not the game
		assertEquals(tournament.getPlayers().size(), 2);
		assertEquals(controller.getPlayers().size(), 3);

		// Check that the turn has been correctly updated
		assertTrue(controller.getCurrentTurn() < 2);
		assertTrue(controller.getCurrentTurn() >= 0);
		assertFalse(tournament.getPlayers().values().contains(jayson));
		assertTrue(controller.getCurrentTurnPlayer() != jayson);
	}
	
	/**
	 * Tests that a player can withdraw from a tournament when they have a
	 * maidens in their hand and more than one token AND repeated colours
	 */
	@Test
	public void testWithdrawMaidenWithMultipleTokensRepeatColor() {
		controller.setTurn(60003);
		assertEquals(tournament.getPlayers().size(), 3);

		SupporterCard m6 = new SupporterCard("maiden", 6);
		jayson.addDisplayCard(m6);
		jayson.addToken(Token.BLUE);
		jayson.addToken(Token.BLUE);
		jayson.addToken(Token.GREEN);
		assertEquals(jayson.getTokens().size(), 3);

		controller.withdraw();

		assertEquals(controller.getState(), 4); // WAITING_FOR_WITHDRAW_TOKEN
		assertEquals(controller.getCurrentTurnPlayer(), jayson); // Turn has not changed yet

		JSONObject chooseToken = requestBuilder.buildChooseToken("blue");
		controller.processPlayerMove(60003, chooseToken);

		// check that Jayson no longer has the blue token, but all other tokens
		assertEquals(jayson.getTokens().size(), 2);
		assertTrue(jayson.getTokens().contains(Token.BLUE));

		// Check that the player has been removed from the tournament but not the game
		assertEquals(tournament.getPlayers().size(), 2);
		assertEquals(controller.getPlayers().size(), 3);

		// Check that the turn has been correctly updated
		assertTrue(controller.getCurrentTurn() < 2);
		assertTrue(controller.getCurrentTurn() >= 0);
		assertFalse(tournament.getPlayers().values().contains(jayson));
		assertTrue(controller.getCurrentTurnPlayer() != jayson);
	}

}
