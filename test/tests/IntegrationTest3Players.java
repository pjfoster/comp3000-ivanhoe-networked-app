package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import comp3004.ivanhoe.client.AppClient;
import comp3004.ivanhoe.controller.MockController;
import comp3004.ivanhoe.model.Card;
import comp3004.ivanhoe.model.ColourCard;
import comp3004.ivanhoe.model.Player;
import comp3004.ivanhoe.model.Token;
import comp3004.ivanhoe.model.Tournament;
import comp3004.ivanhoe.server.MockServer;
import comp3004.ivanhoe.util.ClientRequestBuilder;
import comp3004.ivanhoe.util.Config;
import comp3004.ivanhoe.util.ServerResponseBuilder;
import comp3004.ivanhoe.view.MockViewFactory;
import comp3004.ivanhoe.view.ViewFactory;

/**
 * Runs a simulated game with 3 players. Includes networking.
 * Tests COLOR CARDS, SUPPROTER CARDS, and WITHDRAWING
 * @author PJF
 *
 */
public class IntegrationTest3Players {

	public static final int WAIT_TIME_MILLIS = 300;

	private ViewFactory viewFactory;

	private ServerResponseBuilder responseBuilder = new ServerResponseBuilder();
	private ClientRequestBuilder requestBuilder = new ClientRequestBuilder();

	private MockController controller;
	private MockServer server;


	/**
	 * Wraps a card with an ArrayList so that it can be played
	 * @param c
	 * @return
	 */
	public ArrayList<Card> wrapCard(Card c) {
		ArrayList<Card> cardWrapper = new ArrayList<Card>();
		cardWrapper.add(c);
		return cardWrapper;
	}

	@Before
	public void setUp() throws Exception {

		viewFactory = new MockViewFactory();

		server = new MockServer(10020, 3);
		controller = new MockController(server, responseBuilder, 3);
		server.setController(controller);
		server.enableNetworking(true);

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws InterruptedException {

		// connect all 3 clients

		final int SERVER_PORT = 10020;

		AppClient client1 = new AppClient(viewFactory, Config.DEFAULT_SERVER_ADDRESS, SERVER_PORT);
		AppClient alexei = Mockito.spy(client1);
		alexei.setUsername("Alexei");
		assertTrue(alexei.connect());
		
		AppClient client2 = new AppClient(viewFactory, Config.DEFAULT_SERVER_ADDRESS, SERVER_PORT);
		AppClient luke = Mockito.spy(client2);
		luke.setUsername("Luke");
		assertTrue(luke.connect());
		
		AppClient client3 = new AppClient(viewFactory, Config.DEFAULT_SERVER_ADDRESS, SERVER_PORT);
		AppClient jayson = Mockito.spy(client3);
		jayson.setUsername("Jayson");
		assertTrue(jayson.connect());

		Thread.sleep(WAIT_TIME_MILLIS);

		assertTrue(server.isPlayerRegistered("Alexei"));
		assertTrue(server.isPlayerRegistered("Luke"));
		assertTrue(server.isPlayerRegistered("Jayson"));
		
		// Verify the initial state of the game
		assertEquals(controller.getPlayers().size(), 3);
		assertEquals(controller.getTournament().getPlayers().size(), 3);
		assertEquals(controller.getTournament().getToken(), Token.UNDECIDED);
		
		int alexeiId = alexei.getID();
		int lukeId = luke.getID();
		int jaysonId = jayson.getID();
		
		int turn = controller.getCurrentTurnId();
		assertTrue(turn == alexeiId || turn == lukeId || turn == jaysonId);
		
		for (int key: controller.getPlayers().keySet()) {
			assertTrue(key == alexeiId || key == lukeId || key == jaysonId);
			assertEquals(controller.getPlayers().get(key).getHand().size(), 8);
			assertEquals(controller.getPlayers().get(key).getDisplay().size(), 0);
			assertEquals(controller.getPlayers().get(key).getTokens().size(), 0);
		}
		
		
	}

}
