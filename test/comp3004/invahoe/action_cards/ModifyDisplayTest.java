package comp3004.invahoe.action_cards;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import comp3004.ivanhoe.controller.MockController;
import comp3004.ivanhoe.model.ActionCard;
import comp3004.ivanhoe.model.ColourCard;
import comp3004.ivanhoe.model.Player;
import comp3004.ivanhoe.model.SupporterCard;
import comp3004.ivanhoe.model.Token;
import comp3004.ivanhoe.model.Tournament;
import comp3004.ivanhoe.server.MockServer;
import comp3004.ivanhoe.util.ClientRequestBuilder;
import comp3004.ivanhoe.util.ServerResponseBuilder;

public class ModifyDisplayTest {

	HashMap<Integer, Player> players;
	ServerResponseBuilder responseBuilder = new ServerResponseBuilder();
	ClientRequestBuilder requestBuilder = new ClientRequestBuilder();
	Tournament tournament;
	MockController controller;
	Player alexei, luke;
	ArrayList<String> cardWrapper;
	
	@Before
	public void setUp() throws Exception {
		
		alexei = new Player("Alexei");
		luke = new Player("Luke");
		
		players = new HashMap<Integer, Player>();
		players.put(60001, alexei);
		players.put(60002, luke);
		
		controller = new MockController(new MockServer(), responseBuilder, 2);
		controller.setPlayers(players);
		
		tournament = new Tournament();
		tournament.setToken(Token.RED);
		tournament.setPlayers(players);
		controller.setTournament(tournament);
		controller.setState(3);
		controller.setTurn(60001);
		
		cardWrapper = new ArrayList<String>();
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testBreakLance() {

		ActionCard breaklance = (ActionCard)controller.getCardFromDeck("breaklance");
		alexei.addHandCard(breaklance);
		cardWrapper.add("breaklance");
		
		luke.addDisplayCard(new ColourCard("purple", 3));
		luke.addDisplayCard(new ColourCard("purple", 5));
		luke.addDisplayCard(new SupporterCard("maiden", 6));
		
		assertTrue(controller.playCard(cardWrapper));
		
		// check that Alexei is now required to pick her opponent
		assertEquals(controller.getCurrentTurnPlayer(), alexei);
		assertEquals(controller.getState(), 7);
		
		JSONObject opponent = requestBuilder.buildSelectOpponent("luke");
		controller.processPlayerMove(60001, opponent);
		
		assertNotEquals(controller.getCurrentTurnPlayer(), alexei);
		assertEquals(controller.getState(), 3);
		
		// check that all purple cards were removed from Luke's display
		assertEquals(luke.getDisplay().size(), 1);
		assertEquals(luke.getDisplayTotal(Token.PURPLE), 6);
		
	}
	
	@Test
	public void testBreakLanceInvalid() {

	}
	
	@Test
	public void testRiposte() {

		ActionCard riposte = (ActionCard)controller.getCardFromDeck("riposte");
		alexei.addHandCard(riposte);
		cardWrapper.add("riposte");
		
		luke.addDisplayCard(new ColourCard("blue", 3));
		luke.addDisplayCard(new ColourCard("blue", 5));
		luke.addDisplayCard(new SupporterCard("squire", 2));
		
		assertTrue(controller.playCard(cardWrapper));
		
		// check that Alexei is now required to pick her opponent
		assertEquals(controller.getCurrentTurnPlayer(), alexei);
		assertEquals(controller.getState(), 7);
		
		JSONObject selectOpponent = requestBuilder.buildSelectOpponent("luke");
		controller.processPlayerMove(60001, selectOpponent);
		
		// check that Alexei's turn is done
		assertNotEquals(controller.getCurrentTurnPlayer(), alexei);
		assertEquals(controller.getState(), 3);
		
		// check that the displays were changed
		assertEquals(luke.getDisplay().size(), 2);
		assertEquals(luke.getDisplayTotal(Token.BLUE), 8);
		assertEquals(alexei.getDisplay().size(), 1);
		assertEquals(alexei.getDisplay().get(0).toString(), "s3");
		
	}
	
	@Test
	public void testRiposteInvalid() {

	}
	
	@Test
	public void testDodge() {

		ActionCard dodge = (ActionCard)controller.getCardFromDeck("dodge");
		alexei.addHandCard(dodge);
		cardWrapper.add("dodge");
		
		luke.addDisplayCard(new ColourCard("blue", 3));
		luke.addDisplayCard(new ColourCard("blue", 5));
		luke.addDisplayCard(new SupporterCard("squire", 2));
		
		assertTrue(controller.playCard(cardWrapper));
		
		// check that Alexei is now required to pick her opponent
		assertEquals(controller.getCurrentTurnPlayer(), alexei);
		assertEquals(controller.getState(), 7);
		
		JSONObject selectOpponent = requestBuilder.buildSelectOpponent("luke");
		controller.processPlayerMove(60001, selectOpponent);
		
		// check that Alexei is now required to pick a card
		assertEquals(controller.getCurrentTurnPlayer(), alexei);
		assertEquals(controller.getState(), 8);
		
		JSONObject pickCard = requestBuilder.buildPickCard("b5");
		controller.processPlayerMove(60001, pickCard);
		
		// check that Alexei's turn is done
		assertNotEquals(controller.getCurrentTurnPlayer(), alexei);
		assertEquals(controller.getState(), 3);
		
		// check that the displays were changed
		assertEquals(luke.getDisplay().size(), 2);
		assertEquals(luke.getDisplayTotal(Token.BLUE), 5);
		assertEquals(tournament.getDiscardPile().size(), 1);
		assertEquals(tournament.getDiscardPile().get(0).toString(), "b5");
		
	}
	
	@Test
	public void testDodgeInvalid() {

	}
	
	@Test
	public void testRetreat() {
		ActionCard retreat = (ActionCard)controller.getCardFromDeck("retreat");
		alexei.addHandCard(retreat);
		cardWrapper.add("retreat");
		
		luke.addDisplayCard(new ColourCard("blue", 3));
		luke.addDisplayCard(new ColourCard("blue", 5));
		luke.addDisplayCard(new SupporterCard("squire", 2));
		
		assertTrue(controller.playCard(cardWrapper));
		
		// check that Alexei is now required to pick her opponent
		assertEquals(controller.getCurrentTurnPlayer(), alexei);
		assertEquals(controller.getState(), 7);
		
		JSONObject selectOpponent = requestBuilder.buildSelectOpponent("luke");
		controller.processPlayerMove(60001, selectOpponent);
		
		// check that Alexei is now required to pick a card
		assertEquals(controller.getCurrentTurnPlayer(), alexei);
		assertEquals(controller.getState(), 8);
		
		JSONObject pickCard = requestBuilder.buildPickCard("b5");
		controller.processPlayerMove(60001, pickCard);
		
		// check that Alexei's turn is done
		assertNotEquals(controller.getCurrentTurnPlayer(), alexei);
		assertEquals(controller.getState(), 3);
		
		// check that the displays were changed
		assertEquals(luke.getDisplay().size(), 2);
		assertEquals(luke.getDisplayTotal(Token.BLUE), 5);
		assertEquals(luke.getHand().size(), 1);
		assertEquals(luke.getHand().get(0).toString(), "b5");
	}
	
	@Test
	public void testRetreatInvalid() {

	}

}
