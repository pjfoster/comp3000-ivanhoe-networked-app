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
import comp3004.ivanhoe.model.ColourCard;
import comp3004.ivanhoe.model.Player;
import comp3004.ivanhoe.model.SupporterCard;
import comp3004.ivanhoe.model.Token;
import comp3004.ivanhoe.model.Tournament;
import comp3004.ivanhoe.server.MockServer;
import comp3004.ivanhoe.util.RequestBuilder;

public class Type4ActionCardsTest {

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
	 * Test the SHIELD action card
	 * Note - individual action cards also have tests cases involving the shield card
	 */
	@Test
	public void testShield() {
		ActionCard shield = (ActionCard)controller.getCardFromDeck("shield");
		alexei.addHandCard(shield);
		cardWrapper.add("shield");
		
		assertTrue(controller.playCard(cardWrapper));
		
		// check that Lexi's special display contains the shield card
		assertEquals(alexei.getSpecial().size(), 1);
		assertEquals(alexei.getSpecial().get(0).getName(), "shield");
		assertTrue(alexei.hasShield());
		assertEquals(controller.getCurrentTurnPlayer(), alexei);
		
		// check that other action cards don't affect Lexi (ex: OUTMANEUVER)
		alexei.addDisplayCard(new ColourCard("red", 3));
		alexei.addDisplayCard(new ColourCard("red", 4));
		alexei.addDisplayCard(new SupporterCard("squire", 3));
		
		emma.addDisplayCard(new ColourCard("red", 3));
		emma.addDisplayCard(new ColourCard("red", 3));
		
		ActionCard outmaneuver = (ActionCard)controller.getCardFromDeck("outmaneuver");
		luke.addHandCard(outmaneuver);
		cardWrapper.add("outmaneuver");
		controller.setTurn(60002);
		
		// check that Emma's display lost a card, but not Alexei's
		JSONObject actionCard = RequestBuilder.buildCardMove("outmaneuver");
		controller.processPlayerMove(60002, actionCard);
		
		assertEquals(emma.getDisplay().size(), 1);
		assertEquals(alexei.getDisplay().size(), 3);
	}
	
	@Test
	public void testStunned() {
		ActionCard stunned = (ActionCard)controller.getCardFromDeck("stunned");
		alexei.addHandCard(stunned);
		cardWrapper.add("stunned");
		
		assertTrue(controller.playCard(cardWrapper));
		
		// check that Alexei is now required to pick an opponent
		assertEquals(controller.getCurrentTurnPlayer(), alexei);
		assertEquals(controller.getState(), 7);

		JSONObject opponent = RequestBuilder.buildSelectOpponent("60002");
		controller.processPlayerMove(60001, opponent);

		assertEquals(controller.getCurrentTurnPlayer(), alexei);
		assertEquals(controller.getState(), 3);
		
		// check that Luke now has the stunned card beside his display
		assertEquals(luke.getSpecial().size(), 1);
		assertEquals(luke.getSpecial().get(0).toString(), "stunned");
		assertTrue(luke.isStunned());
		
		// check that Luke can't play multiple cards
		controller.setTurn(60002);
		tournament.setToken(Token.RED);
		ColourCard r5 = (ColourCard)controller.getCardFromDeck("r5");
		ColourCard r3 = (ColourCard)controller.getCardFromDeck("r3");
		luke.addHandCard(r5);
		luke.addHandCard(r3);
		
		ArrayList<String> cards = new ArrayList<String>();
		cards.add("r3");
		cards.add("r5");
		assertFalse(controller.playMultipleCards(cards));	
	}
	
	@Test
	public void testIvanhoe() {
	
	}

}
