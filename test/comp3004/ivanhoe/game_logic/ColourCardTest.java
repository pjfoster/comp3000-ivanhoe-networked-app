package comp3004.ivanhoe.game_logic;

import static org.junit.Assert.*;

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
	Player alexei, luke;
	
	@Before
	public void setUp() throws Exception {
		
		alexei = new Player("Alexei");
		luke = new Player("Luke");
		
		players = new HashMap<Integer, Player>();
		players.put(60001, alexei);
		players.put(60002, luke);
		
		MockController controller = new MockController(null, responseBuilder, 2);
		controller.setPlayers(players);
		
		tournament = new Tournament();
		tournament.setToken(Token.RED);
		tournament.setPlayers(players);
		controller.setTournament(tournament);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testPlayCardBasic() {
		
		assertEquals(alexei.getDisplayTotal(), 0);
		
		ColourCard r3 = new ColourCard("red", 3);
		alexei.addHandCard(r3);
		assertTrue(alexei.playCard(r3));
		
		assertEquals(alexei.getDisplayTotal(), 3);
		assertEquals(tournament.getPlayerWithHighestDisplay(), alexei);
		
		ColourCard r5 = new ColourCard("red", 5);
		alexei.addHandCard(r5);
		assertTrue(alexei.playCard(r5));
		
		assertEquals(alexei.getDisplayTotal(), 8);
		assertEquals(tournament.getPlayerWithHighestDisplay(), alexei);
		
	}
	
	@Test 
	public void testPlayCardNotInHand() {
		ColourCard r5 = new ColourCard("red", 5);
		assertFalse(alexei.playCard(r5));
	}
	
	@Test
	public void testPlayCardWrongColor() {
		ColourCard p3 = new ColourCard("purple", 3);
		alexei.addHandCard(p3);
		assertFalse(alexei.playCard(p3));
	}
	
	@Test
	public void testPlayCardTooSmall() {
		ColourCard r5 = new ColourCard("red", 5);
		ColourCard r3 = new ColourCard("red", 3);
		
		alexei.addHandCard(r3);
		alexei.addHandCard(r5);
		luke.addHandCard(r5);
		
		assertTrue(luke.playCard(r5));
		assertEquals(tournament.getPlayerWithHighestDisplay(), luke);
		
		// Check that player can't play card of lesser value
		assertFalse(alexei.playCard(r3));
		
		// Check that player can't play card of equal value
		assertFalse(alexei.playCard(r5));
	}

}
