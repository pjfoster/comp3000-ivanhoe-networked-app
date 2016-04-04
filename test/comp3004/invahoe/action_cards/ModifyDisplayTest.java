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
import comp3004.ivanhoe.model.Card;
import comp3004.ivanhoe.model.ColourCard;
import comp3004.ivanhoe.model.Player;
import comp3004.ivanhoe.model.SupporterCard;
import comp3004.ivanhoe.model.Token;
import comp3004.ivanhoe.model.Tournament;
import comp3004.ivanhoe.server.MockServer;
import comp3004.ivanhoe.util.RequestBuilder;

// TODO: test with opponents who have withdrawn, test with card not in hand
// TODO: test with single card in opponent display

public class ModifyDisplayTest {

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
	 * Test the BREAK LANCE action card. Discard all purple cards from
	 * a valid opponent's display
	 */
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

		JSONObject opponent = RequestBuilder.buildSelectOpponent("luke");
		controller.processPlayerMove(60001, opponent);

		assertNotEquals(controller.getCurrentTurnPlayer(), alexei);
		assertEquals(controller.getState(), 3);

		// check that all purple cards were removed from Luke's display
		assertEquals(luke.getDisplay().size(), 1);
		assertEquals(luke.getDisplayTotal(Token.PURPLE), 6);

	}

	/**
	 * Test the RIPOSTE action card. Take the card at the top of a valid opponent's
	 * display and add it to the player's display.
	 */
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

		JSONObject selectOpponent = RequestBuilder.buildSelectOpponent("luke");
		controller.processPlayerMove(60001, selectOpponent);

		// check that Alexei's turn is done
		assertNotEquals(controller.getCurrentTurnPlayer(), alexei);
		assertEquals(controller.getState(), 3);

		// check that the displays were changed
		assertEquals(luke.getDisplay().size(), 2);
		assertEquals(luke.getDisplayTotal(Token.BLUE), 8);
		assertEquals(alexei.getDisplay().size(), 1);
		assertEquals(alexei.getDisplay().get(0).toString(), "s2");

	}

	/** 
	 * Test DODGE action card. Pick any card in a valid opponent's display; discard it
	 */
	@Test
	public void testDodge() {

		ActionCard dodge = (ActionCard)controller.getCardFromDeck("dodge");
		alexei.addHandCard(dodge);
		cardWrapper.add("dodge");

		Card b5 = controller.getCardFromDeck("b5");
		luke.addDisplayCard(new ColourCard("blue", 3));
		luke.addDisplayCard(b5);
		luke.addDisplayCard(new SupporterCard("squire", 2));

		assertTrue(controller.playCard(cardWrapper));

		// check that Alexei is now required to pick her opponent
		assertEquals(controller.getCurrentTurnPlayer(), alexei);
		assertEquals(controller.getState(), 7);

		JSONObject selectOpponent = RequestBuilder.buildSelectOpponent("luke");
		controller.processPlayerMove(60001, selectOpponent);

		// check that Alexei is now required to pick a card
		assertEquals(controller.getCurrentTurnPlayer(), alexei);
		assertEquals(controller.getState(), 8);

		JSONObject pickCard = RequestBuilder.buildPickCard("b5");
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

		// card isn't in opponent's display
		ActionCard dodge = (ActionCard)controller.getCardFromDeck("dodge");
		alexei.addHandCard(dodge);
		cardWrapper.add("dodge");
		assertTrue(controller.playCard(cardWrapper));

		JSONObject selectOpponent = RequestBuilder.buildSelectOpponent("luke");
		controller.processPlayerMove(60001, selectOpponent);

		JSONObject pickCard = RequestBuilder.buildPickCard("b5");
		controller.processPlayerMove(60001, pickCard);

		// check that it is still Alexei's turn - the move wasn't processed
		assertEquals(controller.getCurrentTurnPlayer(), alexei);
		assertEquals(controller.getState(), 8);
		assertEquals(tournament.getDiscardPile().size(), 0);
	}

	@Test
	public void testRetreat() {
		ActionCard retreat = (ActionCard)controller.getCardFromDeck("retreat");
		alexei.addHandCard(retreat);
		cardWrapper.add("retreat");

		Card b5 = controller.getCardFromDeck("b5");
		luke.addDisplayCard(new ColourCard("blue", 3));
		luke.addDisplayCard(b5);
		luke.addDisplayCard(new SupporterCard("squire", 2));

		assertTrue(controller.playCard(cardWrapper));

		// check that Alexei is now required to pick her opponent
		assertEquals(controller.getCurrentTurnPlayer(), alexei);
		assertEquals(controller.getState(), 7);

		JSONObject selectOpponent = RequestBuilder.buildSelectOpponent("luke");
		controller.processPlayerMove(60001, selectOpponent);

		// check that Alexei is now required to pick a card
		assertEquals(controller.getCurrentTurnPlayer(), alexei);
		assertEquals(controller.getState(), 8);

		JSONObject pickCard = RequestBuilder.buildPickCard("b5");
		controller.processPlayerMove(60001, pickCard);

		// check that Alexei's turn is done
		assertNotEquals(controller.getCurrentTurnPlayer(), alexei);
		assertEquals(controller.getState(), 3);

		// check that the displays were changed
		assertEquals(luke.getDisplay().size(), 2);
		assertEquals(luke.getDisplayTotal(Token.BLUE), 5);
		assertEquals(luke.getHand().size(), 2); // one card picked on his turn
		assertTrue(luke.getHand().get(0).toString().equals("b5") ||
				   luke.getHand().get(1).toString().equals("b5"));
	}

	@Test
	public void testRetreatInvalid() {
		// card isn't in opponent's display
		ActionCard retreat = (ActionCard)controller.getCardFromDeck("retreat");
		alexei.addHandCard(retreat);
		cardWrapper.add("retreat");
		assertTrue(controller.playCard(cardWrapper));

		JSONObject selectOpponent = RequestBuilder.buildSelectOpponent("luke");
		controller.processPlayerMove(60001, selectOpponent);

		JSONObject pickCard = RequestBuilder.buildPickCard("b5");
		controller.processPlayerMove(60001, pickCard);

		// check that it is still Alexei's turn - the move wasn't processed
		assertEquals(controller.getCurrentTurnPlayer(), alexei);
		assertEquals(controller.getState(), 8);
		assertEquals(tournament.getDiscardPile().size(), 0);
	}

	/**
	 * Select a random card from a valid opponent's hand and add it to
	 * the player's hand
	 */
	@Test
	public void testKnockDown() {
		ActionCard knockDown = (ActionCard)controller.getCardFromDeck("knockdown");
		alexei.addHandCard(knockDown);
		cardWrapper.add("knockdown");

		luke.addHandCard(new ColourCard("blue", 3));
		luke.addHandCard(new ColourCard("blue", 5));
		luke.addHandCard(new SupporterCard("squire", 2));

		assertTrue(controller.playCard(cardWrapper));

		// check that Alexei is now required to pick her opponent
		assertEquals(controller.getCurrentTurnPlayer(), alexei);
		assertEquals(controller.getState(), 7);

		JSONObject selectOpponent = RequestBuilder.buildSelectOpponent("luke");
		controller.processPlayerMove(60001, selectOpponent);

		// check that Alexei's hand now contains one of Luke's cards
		assertEquals(luke.getHand().size(), 3); // one card picked when his turn started
		assertEquals(alexei.getHand().size(), 1);
		assertTrue(alexei.getHand().get(0).toString().equals("b5") ||
				alexei.getHand().get(0).toString().equals("b3") ||
				alexei.getHand().get(0).toString().equals("s2"));
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
	}

	@Test
	public void testCharge() {
		// TODO: would it also be removed from Lexi's hand?

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
	}

	@Test
	public void testCounterCharge() {
		// TODO: would it also be removed from Lexi's hand?

		ActionCard charge = (ActionCard)controller.getCardFromDeck("charge");
		alexei.addHandCard(charge);
		cardWrapper.add("charge");

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

		// check that no one's hand contains b2
		for (Player p: tournament.getPlayers().values()) {
			for (Card c: p.getHand()) {
				assertTrue(c.toString() != "b5");
			}
		}
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
	}
	
	@Test
	public void testAdapt() {
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

		JSONObject selectOpponent = RequestBuilder.buildSelectOpponent("luke");
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
		
		// check that Alexei's turn is done
		assertNotEquals(controller.getCurrentTurnPlayer(), alexei);
		assertEquals(controller.getState(), 3);

		// check that the displays were changed
		assertEquals(luke.getDisplay().size(), 3);
		assertEquals(luke.getDisplayTotal(Token.BLUE), 7);
		assertEquals(alexei.getDisplay().size(), 1);
		assertEquals(alexei.getDisplayTotal(Token.BLUE), 5);
		
	}

}
