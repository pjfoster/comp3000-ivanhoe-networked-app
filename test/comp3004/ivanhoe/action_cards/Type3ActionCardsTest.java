package comp3004.ivanhoe.action_cards;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import comp3004.ivanhoe.controller.MockController;
import comp3004.ivanhoe.model.ActionCard;
import comp3004.ivanhoe.model.Card;
import comp3004.ivanhoe.model.ColourCard;
import comp3004.ivanhoe.model.Player;
import comp3004.ivanhoe.model.SupporterCard;
import comp3004.ivanhoe.model.Token;
import comp3004.ivanhoe.model.Tournament;
import comp3004.ivanhoe.server.MockServer;
import comp3004.ivanhoe.util.RequestBuilder;

public class Type3ActionCardsTest {

	HashMap<Integer, Player> players;
	Tournament tournament;
	MockController controller;
	Player alexei, luke, jayson, emma;
	ArrayList<String> cardWrapper;
	
	@Before
	public void setUp() throws Exception {
		alexei = new Player("Alexei");
		luke = new Player("Luke");
		emma = new Player("Emma");
		jayson = new Player("Jayson");

		players = new HashMap<Integer, Player>();
		players.put(60001, alexei);
		players.put(60002, luke);
		players.put(60003, jayson);
		players.put(60004, emma);

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

	/**
	 * Remove card at the top of each player's display
	 */
	@Test
	public void testOutmaneuver() {
		ActionCard outmaneuver = (ActionCard)controller.getCardFromDeck("outmaneuver");
		alexei.addHandCard(outmaneuver);
		cardWrapper.add("outmaneuver");

		luke.addDisplayCard(new ColourCard("blue", 5));
		luke.addDisplayCard(new ColourCard("blue", 3));

		jayson.addDisplayCard(new SupporterCard("squire", 2));
		jayson.addDisplayCard(new SupporterCard("maiden", 6));
		jayson.addDisplayCard(new ColourCard("blue", 3));

		emma.addDisplayCard(new ColourCard("blue", 4));

		assertTrue(controller.playCard(cardWrapper));

		// check that the displays were modified, except for Emma's
		assertEquals(luke.getDisplay().size(), 1);
		assertTrue(luke.getDisplay().get(0).toString() != "b5");
		assertEquals(jayson.getDisplay().size(), 2);
		assertTrue(jayson.getDisplay().get(1).toString() != "s3");
		assertEquals(emma.getDisplay().size(), 1);
		
		assertEquals(tournament.getDiscardPile().size(), 3);
		assertTrue(tournament.getDiscardPile().get(2).toString().equals("outmaneuver"));
	}
	
	/**
	 * Remove card at the top of each player's display
	 */
	@Test
	public void testOutmaneuverShielded() {
		ActionCard outmaneuver = (ActionCard)controller.getCardFromDeck("outmaneuver");
		alexei.addHandCard(outmaneuver);
		cardWrapper.add("outmaneuver");

		luke.addDisplayCard(new ColourCard("blue", 5));
		luke.addDisplayCard(new ColourCard("blue", 3));
		luke.addSpecialCard(new ActionCard("shield"));

		jayson.addDisplayCard(new SupporterCard("squire", 2));
		jayson.addDisplayCard(new SupporterCard("maiden", 6));
		jayson.addDisplayCard(new ColourCard("blue", 3));

		emma.addDisplayCard(new ColourCard("blue", 4));

		assertTrue(controller.playCard(cardWrapper));

		// check that the displays were modified, except for Emma's
		assertEquals(luke.getDisplay().size(), 2);
		assertTrue(luke.getDisplay().get(0).toString().equals("b5") ||
				   luke.getDisplay().get(1).toString().equals("b5"));
		assertEquals(jayson.getDisplay().size(), 2);
		assertTrue(jayson.getDisplay().get(1).toString() != "s3");
		assertEquals(emma.getDisplay().size(), 1);
	}

	/**
	 * Test for CHARGE action card
	 * Identify lowest ranking card in game, remove it from all displays
	 */
	@Test
	public void testCharge() {
		
		ActionCard charge = (ActionCard)controller.getCardFromDeck("charge");
		alexei.addHandCard(charge);
		cardWrapper.add("charge");

		luke.addDisplayCard(new ColourCard("blue", 5));
		luke.addDisplayCard(new ColourCard("blue", 2));
		luke.addDisplayCard(new ColourCard("blue", 3));

		jayson.addDisplayCard(new ColourCard("blue", 3));
		jayson.addDisplayCard(new ColourCard("blue", 2));

		emma.addDisplayCard(new ColourCard("blue", 2));

		assertTrue(controller.playCard(cardWrapper));

		// check that the displays were modified, except for Emma's
		assertEquals(luke.getDisplay().size(), 2);
		assertEquals(jayson.getDisplay().size(), 1);
		assertEquals(emma.getDisplay().size(), 1);

		// check that no one's hand contains b2
		for (Player p: tournament.getPlayers().values()) {
			for (Card c: p.getHand()) {
				assertTrue(c.toString() != "b2");
			}
		}
		
		assertEquals(tournament.getDiscardPile().size(), 3);
		assertTrue(tournament.getDiscardPile().get(2).toString().equals("charge"));
	}
	
	@Test
	public void testChargeGreenTournament() {
		
		ActionCard charge = (ActionCard)controller.getCardFromDeck("charge");
		alexei.addHandCard(charge);
		cardWrapper.add("charge");

		tournament.setToken(Token.GREEN);
		
		luke.addDisplayCard(new ColourCard("green", 1));
		luke.addDisplayCard(new ColourCard("green", 1));
		luke.addDisplayCard(new ColourCard("green", 1));

		jayson.addDisplayCard(new ColourCard("green", 1));
		jayson.addDisplayCard(new ColourCard("green", 1));

		emma.addDisplayCard(new ColourCard("green", 1));

		assertTrue(controller.playCard(cardWrapper));

		// check that the displays only have 1 card
		assertEquals(luke.getDisplay().size(), 1);
		assertEquals(jayson.getDisplay().size(), 1);
		assertEquals(emma.getDisplay().size(), 1);

	}
	
	@Test
	public void testChargeShielded() {
		
		ActionCard charge = (ActionCard)controller.getCardFromDeck("charge");
		alexei.addHandCard(charge);
		cardWrapper.add("charge");

		luke.addDisplayCard(new ColourCard("blue", 5));
		luke.addDisplayCard(new ColourCard("blue", 2));
		luke.addDisplayCard(new ColourCard("blue", 3));
		luke.addSpecialCard(new ActionCard("shield"));

		jayson.addDisplayCard(new ColourCard("blue", 3));
		jayson.addDisplayCard(new ColourCard("blue", 2));

		emma.addDisplayCard(new ColourCard("blue", 2));

		assertTrue(controller.playCard(cardWrapper));

		// check that the displays were modified, except for Emma's
		assertEquals(luke.getDisplay().size(), 3);
		assertEquals(jayson.getDisplay().size(), 1);
		assertEquals(emma.getDisplay().size(), 1);

	}

	/**
	 * Test for COUNTER CHARGE action card
	 * Identify highest valued card in players' displays, and remove
	 * all instances of card from displays
	 */
	@Test
	public void testCounterCharge() {

		ActionCard countercharge = (ActionCard)controller.getCardFromDeck("countercharge");
		alexei.addHandCard(countercharge);
		cardWrapper.add("countercharge");

		luke.addDisplayCard(new ColourCard("blue", 5));
		luke.addDisplayCard(new ColourCard("blue", 2));
		luke.addDisplayCard(new ColourCard("blue", 3));

		jayson.addDisplayCard(new ColourCard("blue", 5));
		jayson.addDisplayCard(new ColourCard("blue", 2));

		emma.addDisplayCard(new ColourCard("blue", 5));

		assertTrue(controller.playCard(cardWrapper));

		// check that the displays were modified, except for Emma's
		assertEquals(luke.getDisplay().size(), 2);
		assertEquals(jayson.getDisplay().size(), 1);
		assertEquals(emma.getDisplay().size(), 1);

		assertEquals(luke.getDisplayTotal(Token.BLUE), 5);
		assertEquals(jayson.getDisplayTotal(Token.BLUE), 2);
		assertEquals(emma.getDisplayTotal(Token.BLUE), 5);
		
		// check that no one's hand contains b2
		for (Player p: tournament.getPlayers().values()) {
			for (Card c: p.getHand()) {
				assertTrue(c.toString() != "b5");
			}
		}
		
		assertEquals(tournament.getDiscardPile().size(), 3);
		assertTrue(tournament.getDiscardPile().get(0).toString().equals("countercharge") ||
				   tournament.getDiscardPile().get(1).toString().equals("countercharge") ||
				   tournament.getDiscardPile().get(2).toString().equals("countercharge"));
	}
	
	@Test
	public void testCounterChargeShielded() {

		ActionCard countercharge = (ActionCard)controller.getCardFromDeck("countercharge");
		alexei.addHandCard(countercharge);
		cardWrapper.add("countercharge");

		luke.addDisplayCard(new ColourCard("blue", 5));
		luke.addDisplayCard(new ColourCard("blue", 2));
		luke.addDisplayCard(new ColourCard("blue", 3));
		luke.addSpecialCard(new ActionCard("shield"));

		jayson.addDisplayCard(new ColourCard("blue", 5));
		jayson.addDisplayCard(new ColourCard("blue", 2));

		emma.addDisplayCard(new ColourCard("blue", 5));

		assertTrue(controller.playCard(cardWrapper));

		// check that the displays were modified, except for Emma's and Luke's
		assertEquals(luke.getDisplay().size(), 3);
		assertEquals(jayson.getDisplay().size(), 1);
		assertEquals(emma.getDisplay().size(), 1);

		assertEquals(luke.getDisplayTotal(Token.BLUE), 10);
		assertEquals(jayson.getDisplayTotal(Token.BLUE), 2);
		assertEquals(emma.getDisplayTotal(Token.BLUE), 5);
	}
	
	/**
	 * Test DISGRACE action card. Remove all supporter cards from all players' displays
	 */
	@Test
	public void testDisgrace() {
		ActionCard disgrace = (ActionCard)controller.getCardFromDeck("disgrace");
		alexei.addHandCard(disgrace);
		cardWrapper.add("disgrace");
		
		luke.addDisplayCard(new ColourCard("blue", 5));
		luke.addDisplayCard(new SupporterCard("squire", 3));
		luke.addDisplayCard(new SupporterCard("maiden", 6));

		jayson.addDisplayCard(new SupporterCard("squire", 2));
		jayson.addDisplayCard(new ColourCard("blue", 2));

		emma.addDisplayCard(new SupporterCard("squire", 3));

		assertTrue(controller.playCard(cardWrapper));

		// check that the displays were modified, except for Emma's
		assertEquals(luke.getDisplay().size(), 1);
		assertEquals(jayson.getDisplay().size(), 1);
		assertEquals(emma.getDisplay().size(), 1);

		assertEquals(luke.getDisplayTotal(Token.BLUE), 5);
		assertEquals(jayson.getDisplayTotal(Token.BLUE), 2);
		assertEquals(emma.getDisplayTotal(Token.BLUE), 3);
		
		// check that no one's display contains a supporter card
		for (Player p: tournament.getPlayers().values()) {
			if (p.getName().equals("Emma")) continue;
			for (Card c: p.getDisplay()) {
				assertFalse(c.toString().charAt(0) == 's' || c.toString().charAt(0) == 'm') ;
			}
		}
		
		assertEquals(tournament.getDiscardPile().size(), 4);
		assertTrue(tournament.getDiscardPile().get(3).toString().equals("disgrace"));
	}
	
	@Test
	public void testDisgraceShielded() {
		ActionCard disgrace = (ActionCard)controller.getCardFromDeck("disgrace");
		alexei.addHandCard(disgrace);
		cardWrapper.add("disgrace");
		
		luke.addDisplayCard(new ColourCard("blue", 5));
		luke.addDisplayCard(new SupporterCard("squire", 3));
		luke.addDisplayCard(new SupporterCard("maiden", 6));
		luke.addSpecialCard(new ActionCard("shield"));

		jayson.addDisplayCard(new SupporterCard("squire", 2));
		jayson.addDisplayCard(new ColourCard("blue", 2));

		emma.addDisplayCard(new SupporterCard("squire", 3));

		assertTrue(controller.playCard(cardWrapper));

		// check that the displays were modified, except for Emma's and Luke's
		assertEquals(luke.getDisplay().size(), 3);
		assertEquals(jayson.getDisplay().size(), 1);
		assertEquals(emma.getDisplay().size(), 1);

		assertEquals(luke.getDisplayTotal(Token.BLUE), 14);
		assertEquals(jayson.getDisplayTotal(Token.BLUE), 2);
		assertEquals(emma.getDisplayTotal(Token.BLUE), 3);

	}
	
	@Test
	public void testAdapt() {
		ActionCard adapt = (ActionCard)controller.getCardFromDeck("adapt");
		alexei.addHandCard(adapt);
		cardWrapper.add("adapt");
		
		// TODO: this is going to require a special state
	}
	
	@Test
	public void testAdaptShielded() {
		ActionCard adapt = (ActionCard)controller.getCardFromDeck("adapt");
		alexei.addHandCard(adapt);
		cardWrapper.add("adapt");
		
		// TODO: this is going to require a special state
	}
	
	@Test
	public void testOutwit() {
		ActionCard outwit = (ActionCard)controller.getCardFromDeck("outwit");
		alexei.addHandCard(outwit);
		cardWrapper.add("outwit");
		
		Card b2 = controller.getCardFromDeck("b2");
		alexei.addDisplayCard(b2);
		
		Card b5 = controller.getCardFromDeck("b5");
		luke.addDisplayCard(new ColourCard("blue", 3));
		luke.addDisplayCard(b5);
		luke.addDisplayCard(new SupporterCard("squire", 2));

		assertTrue(controller.playCard(cardWrapper));

		// check that Alexei is now required to pick her opponent
		assertEquals(controller.getCurrentTurnPlayer(), alexei);
		assertEquals(controller.getState(), 7);

		JSONObject selectOpponent = RequestBuilder.buildSelectOpponent("60002");
		controller.processPlayerMove(60001, selectOpponent);

		// check that Alexei is now required to pick an opponent's card
		assertEquals(controller.getCurrentTurnPlayer(), alexei);
		assertEquals(controller.getState(), 8);

		JSONObject pickCard = RequestBuilder.buildPickCard("b5");
		controller.processPlayerMove(60001, pickCard);

		// check that Alexei is now required to pick one of her own cards
		assertEquals(controller.getCurrentTurnPlayer(), alexei);
		assertEquals(controller.getState(), 9);

		pickCard = RequestBuilder.buildPickCard("b2");
		controller.processPlayerMove(60001, pickCard);
		
		// check that Alexei can continue to play other cards
		assertEquals(controller.getCurrentTurnPlayer(), alexei);
		assertEquals(controller.getState(), 3);

		// check that the displays were changed
		assertEquals(luke.getDisplay().size(), 3);
		assertEquals(luke.getDisplayTotal(Token.BLUE), 7);
		assertEquals(alexei.getDisplay().size(), 1);
		assertEquals(alexei.getDisplayTotal(Token.BLUE), 5);
		
		assertEquals(tournament.getDiscardPile().size(), 1);
		assertTrue(tournament.getDiscardPile().get(0).toString().equals("outwit"));
		
	}
	
	/**
	 * Test that OUTWIT can be used even if the selected opponent is shielded
	 */
	@Test
	public void testOutwitShielded() {
		ActionCard outwit = (ActionCard)controller.getCardFromDeck("outwit");
		alexei.addHandCard(outwit);
		cardWrapper.add("outwit");
		
		Card b2 = controller.getCardFromDeck("b2");
		alexei.addDisplayCard(b2);
		
		Card b5 = controller.getCardFromDeck("b5");
		luke.addDisplayCard(new ColourCard("blue", 3));
		luke.addDisplayCard(b5);
		luke.addDisplayCard(new SupporterCard("squire", 2));
		luke.addSpecialCard(new ActionCard("shield"));

		assertTrue(controller.playCard(cardWrapper));

		// check that Alexei is now required to pick her opponent
		assertEquals(controller.getCurrentTurnPlayer(), alexei);
		assertEquals(controller.getState(), 7);

		JSONObject selectOpponent = RequestBuilder.buildSelectOpponent("60002");
		controller.processPlayerMove(60001, selectOpponent);

		// check that Alexei is now required to pick an opponent's card
		assertEquals(controller.getCurrentTurnPlayer(), alexei);
		assertEquals(controller.getState(), 8);

		JSONObject pickCard = RequestBuilder.buildPickCard("b5");
		controller.processPlayerMove(60001, pickCard);

		// check that Alexei is now required to pick one of her own cards
		assertEquals(controller.getCurrentTurnPlayer(), alexei);
		assertEquals(controller.getState(), 9);

		pickCard = RequestBuilder.buildPickCard("b2");
		controller.processPlayerMove(60001, pickCard);
		
		// check that Alexei can continue to play other cards
		assertEquals(controller.getCurrentTurnPlayer(), alexei);
		assertEquals(controller.getState(), 3);

		// check that the displays were changed
		assertEquals(luke.getDisplay().size(), 3);
		assertEquals(luke.getDisplayTotal(Token.BLUE), 7);
		assertEquals(alexei.getDisplay().size(), 1);
		assertEquals(alexei.getDisplayTotal(Token.BLUE), 5);
		
	}
}
