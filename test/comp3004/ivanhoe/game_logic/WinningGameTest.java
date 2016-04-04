package comp3004.ivanhoe.game_logic;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import comp3004.ivanhoe.controller.MockController;
import comp3004.ivanhoe.model.Player;
import comp3004.ivanhoe.model.Token;

public class WinningGameTest {

	HashMap<Integer, Player> players;
	
	@Before
	public void setUp() throws Exception {	
		players = new HashMap<Integer, Player>();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test2PlayersWin() {
		
		players.put(60001, new Player("Alexei"));
		players.put(60002, new Player("Luke"));
		
		MockController controller = new MockController(null, 2);
		controller.setPlayers(players);
		
		assertFalse(controller.checkGameWon());
		
		// Correct number of tokens, but not enough colors
		controller.givePlayerToken(60001, Token.RED);
		controller.givePlayerToken(60001, Token.BLUE);
		controller.givePlayerToken(60001, Token.GREEN);
		controller.givePlayerToken(60001, Token.PURPLE);
		
		assertFalse(controller.checkGameWon());
		
		controller.givePlayerToken(60001, Token.YELLOW);
		
		assertTrue(controller.checkGameWon());
		
		// test different combination of colors
		players = new HashMap<Integer, Player>();
		players.put(60003, new Player("Emma"));
		players.put(60004, new Player("Jayson"));
		controller.setPlayers(players);
		
		assertFalse(controller.checkGameWon());
		
		// Correct number of tokens, but not enough colors
		controller.givePlayerToken(60003, Token.PURPLE);
		controller.givePlayerToken(60003, Token.BLUE);
		controller.givePlayerToken(60003, Token.YELLOW);
		controller.givePlayerToken(60003, Token.RED);
		
		assertFalse(controller.checkGameWon());
		
		controller.givePlayerToken(60003, Token.GREEN);
		
		assertTrue(controller.checkGameWon());
		
		
	}
	
	@Test
	public void test5PlayersWin() {
		players.put(60001, new Player("Alexei"));
		players.put(60002, new Player("Luke"));
		players.put(60003, new Player("Emma"));
		players.put(60004, new Player("Jayson"));
		players.put(60005, new Player("Mark"));
		
		MockController controller = new MockController(null, 2);
		controller.setPlayers(players);
		
		assertFalse(controller.checkGameWon());
		
		// Correct number of tokens, but not enough colors
		controller.givePlayerToken(60001, Token.RED);
		controller.givePlayerToken(60001, Token.BLUE);
		controller.givePlayerToken(60001, Token.GREEN);
		controller.givePlayerToken(60001, Token.RED);
		
		assertFalse(controller.checkGameWon());
		
		controller.givePlayerToken(60001, Token.YELLOW);
		
		assertTrue(controller.checkGameWon());
	}


}
