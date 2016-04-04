package comp3004.invahoe.action_cards;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import comp3004.ivanhoe.controller.MockController;
import comp3004.ivanhoe.model.ActionCard;
import comp3004.ivanhoe.model.Player;
import comp3004.ivanhoe.model.Token;
import comp3004.ivanhoe.model.Tournament;
import comp3004.ivanhoe.server.MockServer;
import comp3004.ivanhoe.util.RequestBuilder;

public class ChangeTournamentColorTest {

	HashMap<Integer, Player> players;
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
		
		controller = new MockController(new MockServer(), 2);
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
	public void testUnhorse() {
		ActionCard unhorse = (ActionCard)controller.getCardFromDeck("unhorse");
		alexei.addHandCard(unhorse);
		cardWrapper.add("unhorse");
		tournament.setToken(Token.PURPLE);
		
		assertTrue(controller.playCard(cardWrapper));
		
		// check that it is still Alexei's turn and that the controller has switched state
		assertEquals(controller.getCurrentTurnPlayer(), alexei);
		assertEquals(controller.getState(), 6);
		
		JSONObject newColor = RequestBuilder.buildChooseToken("blue");
		assertTrue(controller.testChangeTournamentColor(newColor));
		
		assertNotEquals(controller.getCurrentTurnPlayer(), alexei);
		assertEquals(tournament.getToken(), Token.BLUE);
		assertEquals(controller.getState(), 3);
		
		// check that the card wasn't added to the display
		assertEquals(alexei.getDisplay().size(), 0);
		assertEquals(tournament.getDiscardPile().size(), 1);
	}
	
	@Test
	public void testUnhorseInvalid() {
		ActionCard unhorse = (ActionCard)controller.getCardFromDeck("unhorse");
		alexei.addHandCard(unhorse);
		cardWrapper.add("unhorse");
		
		// test that the card can only be played during a jousting tournament
		tournament.setToken(Token.RED);
		assertFalse(controller.playCard(cardWrapper));
		
		tournament.setToken(Token.BLUE);
		assertFalse(controller.playCard(cardWrapper));
		
		tournament.setToken(Token.YELLOW);
		assertFalse(controller.playCard(cardWrapper));
		
		tournament.setToken(Token.GREEN);
		assertFalse(controller.playCard(cardWrapper));
		
		// test that you can only change tournament color to blue, yellow, or red
		tournament.setToken(Token.PURPLE);
		assertTrue(controller.playCard(cardWrapper));
		
		JSONObject newColor = RequestBuilder.buildChooseToken("purple");
		assertFalse(controller.testChangeTournamentColor(newColor));
		newColor = RequestBuilder.buildChooseToken("green");
		assertFalse(controller.testChangeTournamentColor(newColor));
		
	}
	
	@Test
	public void testChangeWeapon() {
		ActionCard changeWeapon = (ActionCard)controller.getCardFromDeck("changeweapon");
		alexei.addHandCard(changeWeapon);
		cardWrapper.add("changeweapon");
		tournament.setToken(Token.RED);
		
		assertTrue(controller.playCard(cardWrapper));
		
		// check that it is still Alexei's turn and that the controller has switched state
		assertEquals(controller.getCurrentTurnPlayer(), alexei);
		assertEquals(controller.getState(), 6);
		
		JSONObject newColor = RequestBuilder.buildChooseToken("blue");
		assertTrue(controller.testChangeTournamentColor(newColor));
		
		assertNotEquals(controller.getCurrentTurnPlayer(), alexei);
		assertEquals(tournament.getToken(), Token.BLUE);
		assertEquals(controller.getState(), 3);
		
		// check that the card wasn't added to the display
		assertEquals(alexei.getDisplay().size(), 0);
		assertEquals(tournament.getDiscardPile().size(), 1);
	}
	
	@Test
	public void testChangeWeaponInvalid() {
		ActionCard changeWeapon = (ActionCard)controller.getCardFromDeck("changeweapon");
		alexei.addHandCard(changeWeapon);
		cardWrapper.add("changeweapon");
		
		// check that the card can't be played during purple or green tournaments
		tournament.setToken(Token.PURPLE);
		assertFalse(controller.playCard(cardWrapper));
		
		tournament.setToken(Token.GREEN);
		assertFalse(controller.playCard(cardWrapper));
		
		// check that the color choices are restricted
		tournament.setToken(Token.RED);
		assertTrue(controller.playCard(cardWrapper));
		
		JSONObject newColor = RequestBuilder.buildChooseToken("purple");
		assertFalse(controller.testChangeTournamentColor(newColor));
		newColor = RequestBuilder.buildChooseToken("green");
		assertFalse(controller.testChangeTournamentColor(newColor));
		newColor = RequestBuilder.buildChooseToken("red");
		assertFalse(controller.testChangeTournamentColor(newColor));
	}
	
	@Test
	public void testDropWeapon() {
		ActionCard dropWeapon = (ActionCard)controller.getCardFromDeck("dropweapon");
		alexei.addHandCard(dropWeapon);
		cardWrapper.add("dropweapon");
		tournament.setToken(Token.RED);
		
		assertTrue(controller.playCard(cardWrapper));
		
		// check that playing the card was successful
		assertNotEquals(controller.getCurrentTurnPlayer(), alexei);
		assertEquals(tournament.getToken(), Token.GREEN);
		
		// check that the card wasn't added to the display
		assertEquals(alexei.getDisplay().size(), 0);
		assertEquals(tournament.getDiscardPile().size(), 1);
	}
	
	@Test
	public void testDropWeaponInvalid() {
		ActionCard dropWeapon = (ActionCard)controller.getCardFromDeck("dropweapon");
		alexei.addHandCard(dropWeapon);
		cardWrapper.add("dropweapon");
		
		// check that the card can't be played in green or purple tournaments
		tournament.setToken(Token.GREEN);
		assertFalse(controller.playCard(cardWrapper));
		
		tournament.setToken(Token.PURPLE);
		assertFalse(controller.playCard(cardWrapper));
	}

}
