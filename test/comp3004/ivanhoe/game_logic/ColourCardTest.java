package comp3004.ivanhoe.game_logic;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import comp3004.ivanhoe.controller.MockController;
import comp3004.ivanhoe.model.Card;
import comp3004.ivanhoe.model.ColourCard;
import comp3004.ivanhoe.model.Player;
import comp3004.ivanhoe.model.Token;
import comp3004.ivanhoe.model.Tournament;
import comp3004.ivanhoe.util.ServerResponseBuilder;

public class ColourCardTest {

	HashMap<Integer, Player> players;
	ServerResponseBuilder responseBuilder = new ServerResponseBuilder();
	Tournament tournament;
	MockController controller;
	Player alexei, luke;
	ColourCard r3, r5, p5;
	ArrayList<String> r3Wrapper, r5Wrapper, p5Wrapper;
	
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
		
		r3 = (ColourCard)controller.getCardFromDeck("r3");
		r3Wrapper = new ArrayList<String>();
		r3Wrapper.add("r3");
		
		r5 = (ColourCard)controller.getCardFromDeck("r5");
		r5Wrapper = new ArrayList<String>();
		r5Wrapper.add("r5");
		
		p5 = (ColourCard)controller.getCardFromDeck("p5");
		p5Wrapper = new ArrayList<String>();
		p5Wrapper.add("p5");
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test that users can play colour cards under the right conditions and
	 * that the value of the card is added to their display
	 */
	@Test
	public void testPlayCardBasic() {
		
		controller.setTurn(60001);
		assertEquals(alexei.getDisplayTotal(Token.RED), 0);
		
		alexei.addHandCard(r3);
		assertTrue(controller.playCard(r3Wrapper));
		
		assertEquals(alexei.getDisplayTotal(Token.RED), 3);
		assertEquals(tournament.getPlayerWithHighestDisplay(), alexei);
		assertEquals(tournament.getHighestDisplayTotal(), 3);
		
		alexei.addHandCard(r5);
		assertTrue(controller.playCard(r5Wrapper));
		
		assertEquals(alexei.getDisplayTotal(Token.RED), 8);
		assertEquals(tournament.getPlayerWithHighestDisplay(), alexei);
		
	}
	
	/**
	 * Test that a player can't play a card that isn't in their hand
	 */
	@Test 
	public void testPlayCardNotInHand() {
		controller.setTurn(60001);
		assertFalse(controller.playCard(r5Wrapper));
	}
	
	/**
	 * Test that a player can't play a card that isn't the tournament color
	 */
	@Test
	public void testPlayCardWrongColor() {
		controller.setTurn(60001);
		alexei.addHandCard(p5);
		assertFalse(controller.playCard(p5Wrapper));
	}
	
	/**
	 * Test that a player can't play a card if the value is too small
	 */
	@Test
	public void testPlayCardTooSmall() {
		
		alexei.addHandCard(r3);
		alexei.addHandCard(r5);
		luke.addHandCard(r5);
		
		controller.setTurn(60002);
		assertTrue(controller.playCard(r5Wrapper));
		assertEquals(tournament.getPlayerWithHighestDisplay(), luke);
		
		controller.setTurn(60001);
		// Check that player can't play card of lesser value
		assertFalse(controller.playCard(r3Wrapper));
		
		// Check that player can't play card of equal value
		assertFalse(controller.playCard(r5Wrapper));
	}

}
