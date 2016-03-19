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
import comp3004.ivanhoe.model.ColourCard;
import comp3004.ivanhoe.model.Player;
import comp3004.ivanhoe.model.Token;
import comp3004.ivanhoe.model.Tournament;
import comp3004.ivanhoe.server.MockServer;
import comp3004.ivanhoe.util.ClientRequestBuilder;
import comp3004.ivanhoe.util.ServerResponseBuilder;

public class WinningTournamentTest {

	HashMap<Integer, Player> players;
	ServerResponseBuilder responseBuilder = new ServerResponseBuilder();
	ClientRequestBuilder requestBuilder = new ClientRequestBuilder();
	Tournament tournament;
	MockController controller;
	Player alexei, luke, jayson;
	Card m6, r3, r5, p5;
	ArrayList<Card> m6Wrapper, r3Wrapper, r5Wrapper, p5Wrapper;
	
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
		tournament.setPlayers(players);
		controller.setTournament(tournament);

		m6 = controller.getCardFromDeck("m6");
		m6Wrapper = new ArrayList<Card>();
		m6Wrapper.add(m6);
		
		r3 = new ColourCard("red", 3);
		r3Wrapper = new ArrayList<Card>();
		r3Wrapper.add(r3);
		
		r5 = new ColourCard("red", 5);
		r5Wrapper = new ArrayList<Card>();
		r5Wrapper.add(r5);
		
		p5 = new ColourCard("purple", 5);
		p5Wrapper = new ArrayList<Card>();
		p5Wrapper.add(p5);
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testBasicTournamentWin() {
		
		assertEquals(alexei.getTokens().size(), 0);
		assertEquals(luke.getTokens().size(), 0);
		assertEquals(jayson.getTokens().size(), 0);
		
		tournament.setToken(Token.RED);
		assertEquals(tournament.getPlayers().size(), 3);
		
		controller.setTurn(60001);
		controller.withdraw();
		assertEquals(tournament.getPlayers().size(), 2);
		
		controller.setTurn(60002);
		controller.withdraw();
		
		// Check that Jayson won and was given the correct Token
		assertEquals(controller.getCurrentTurnPlayer(), jayson);
		assertEquals(jayson.getTokens().size(), 1);
		assertTrue(jayson.getTokens().contains(Token.RED));
		
		assertEquals(alexei.getTokens().size(), 0);
		assertEquals(luke.getTokens().size(), 0);
		
	}
	
	/**
	 * Test that a player cannot receive more than 1 of the same
	 * color token
	 */
	@Test
	public void testTournamentWinDuplicateToken() {
		assertEquals(jayson.getTokens().size(), 0);
		tournament.setToken(Token.RED);
		assertEquals(tournament.getPlayers().size(), 3);
		
		controller.setTurn(60001);
		controller.withdraw();
		assertEquals(tournament.getPlayers().size(), 2);
		
		controller.setTurn(60002);
		controller.withdraw();
		
		// Check that Jayson won and was given the correct Token
		assertEquals(controller.getCurrentTurnPlayer(), jayson);
		assertEquals(jayson.getTokens().size(), 1);
		assertTrue(jayson.getTokens().contains(Token.RED));
		
		// new round
		tournament.setToken(Token.RED);
		
		controller.setTurn(60001);
		controller.withdraw();
		assertEquals(tournament.getPlayers().size(), 2);
		
		controller.setTurn(60002);
		controller.withdraw();
		
		// Check that Jayson won and was given the correct Token
		assertEquals(controller.getCurrentTurnPlayer(), jayson);
		assertEquals(jayson.getTokens().size(), 1);
		assertTrue(jayson.getTokens().contains(Token.RED));

	}
	
	@Test
	public void testJoustingTournamentWin() {
		
		assertEquals(alexei.getTokens().size(), 0);
		assertEquals(luke.getTokens().size(), 0);
		assertEquals(jayson.getTokens().size(), 0);
		
		tournament.setToken(Token.PURPLE);
		assertEquals(tournament.getPlayers().size(), 3);
		
		controller.setTurn(60001);
		controller.withdraw();
		assertEquals(tournament.getPlayers().size(), 2);
		
		controller.setTurn(60002);
		controller.withdraw();
		
		// Check that the tournament has not been reset
		assertEquals(tournament.getPlayers().size(), 1);
		assertEquals(controller.getCurrentTurnPlayer(), jayson);
		assertEquals(controller.getState(), 5); // WAITING_FOR_WINNING_TOKEN
		assertEquals(controller.getTournament().getToken(), Token.PURPLE);
		
		JSONObject chooseColor = requestBuilder.buildChooseToken("blue");
		controller.processPlayerMove(60003, chooseColor);
		
		// Check that Jayson won and was given the correct Token
		assertEquals(controller.getCurrentTurnPlayer(), jayson);
		assertEquals(jayson.getTokens().size(), 1);
		assertTrue(jayson.getTokens().contains(Token.BLUE));
		
		assertEquals(alexei.getTokens().size(), 0);
		assertEquals(luke.getTokens().size(), 0);
	}
	
	/**
	 * Test that the controller's current tournament is correctly reset; players keep their hands
	 * but discard their displays
	 */
	@Test
	public void testResetTournament() {
		
		alexei.addHandCard(r3);
		luke.addHandCard(r3);
		jayson.addHandCard(r3);
		
		assertEquals(alexei.getTokens().size(), 0);
		assertEquals(luke.getTokens().size(), 0);
		assertEquals(jayson.getTokens().size(), 0);
		
		assertEquals(alexei.getHand().size(), 1);
		assertEquals(luke.getHand().size(), 1);
		assertEquals(jayson.getHand().size(), 1);
		
		tournament.setToken(Token.RED);
		assertEquals(tournament.getPlayers().size(), 3);
		
		controller.setTurn(60001);
		controller.withdraw();
		assertEquals(tournament.getPlayers().size(), 2);
		
		controller.setTurn(60002);
		controller.withdraw();
		
		// check that Jayson won and that everything was reset correctly
		assertEquals(controller.getCurrentTurnPlayer(), jayson);
		assertEquals(jayson.getTokens().size(), 1);
		assertTrue(jayson.getTokens().contains(Token.RED));
		
		assertEquals(alexei.getTokens().size(), 0);
		assertEquals(luke.getTokens().size(), 0);
		
		// Ensure that the displays are cleared but the hands are carried over
		assertEquals(controller.getPreviousTournament(), Token.RED);
		assertEquals(controller.getTournament().getToken(), Token.UNDECIDED);
		for (Player p: tournament.getPlayers().values()) {
			assertEquals(p.getDisplay().size(), 0);
			assertTrue(p.getHand().size() == 1 || p.getHand().size() == 2);
			assertTrue(p.getHand().contains(r3));
		}
		
		
	}

}
