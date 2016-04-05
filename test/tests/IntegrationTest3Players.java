package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import comp3004.ivanhoe.client.AppClient;
import comp3004.ivanhoe.controller.MockController;
import comp3004.ivanhoe.model.Card;
import comp3004.ivanhoe.model.Player;
import comp3004.ivanhoe.model.Token;
import comp3004.ivanhoe.server.MockServer;
import comp3004.ivanhoe.util.Config;
import comp3004.ivanhoe.util.RequestBuilder;
import comp3004.ivanhoe.view.MockViewFactory;
import comp3004.ivanhoe.view.ViewFactory;

/**
 * Runs a simulated game with 3 players. Includes networking.
 * Tests COLOR CARDS, SUPPROTER CARDS, and WITHDRAWING
 * Cases covered:
 * 		Playing color cards & supporter cards (maiden and squire)
 * 		Starting with a supporter card and being able to pick the tournament color
 * 		All 5 color tournaments (and awarding the winner a token)
 * 		Playing supporter cards for green tournaments
 * 		Withdrawing
 * 		Withdrawing with a maiden and having to lose a token
 * 		Playing multiple cards
 * 		Being unable to make invalid moves (such as playing an invalid color)
 * 		Winning a jousting tournament and selecting the color token to win
 * 		Not being able to pick a purple tournament after winning a purple tournament
 * @author PJF
 *
 */
public class IntegrationTest3Players {

	public static final int WAIT_TIME_MILLIS = 400;

	private ViewFactory viewFactory;

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
		controller = new MockController(server, 3);
		server.setController(controller);
		server.enableNetworking(true);

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws InterruptedException, IOException {

		// connect all 3 clients

		final int SERVER_PORT = 10020;
		AppClient alexei = ITM.initClient("Alexei", SERVER_PORT);
		AppClient luke = ITM.initClient("Luke", SERVER_PORT);
		AppClient jayson = ITM.initClient("Jayson", SERVER_PORT);

		Thread.sleep(WAIT_TIME_MILLIS);

		assertTrue(server.isPlayerRegistered("Alexei"));
		assertTrue(server.isPlayerRegistered("Luke"));
		assertTrue(server.isPlayerRegistered("Jayson"));
		
		// Verify the initial state of the game
		ITM.verifyInitialState(controller, 3);
		
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
		
		// Override game defaults
		String[] alexeiCards = {"r3", "r3", "p5", "y2", "y3", "b5", "s2", "s3"};
		String[] lukeCards =   {"r5", "p3", "p5", "g1", "g1", "g1", "s3", "m6"};
		String[] jaysonCards = {"b4", "p3", "p3", "r3", "s3", "s3", "m6", "y4"};
		
		controller.swapHand(alexeiId, controller.getCardsFromStrings(alexeiCards));
		controller.swapHand(lukeId, controller.getCardsFromStrings(lukeCards));
		controller.swapHand(jaysonId, controller.getCardsFromStrings(jaysonCards));
		
		/*for (Player p: controller.getPlayers().values()) {
			System.out.println(p.getName() + " " + p.getHand());
		}*/
		
		Integer[] turnSequence = {alexeiId, lukeId, jaysonId};
		controller.setPlayerTurns(turnSequence);
		controller.setTurn(alexeiId);
		
			// TURN 1
		
		ITM.verifyTournament(controller, Token.UNDECIDED, 3, -1, -1);
		ITM.verifyController(controller, 2, alexeiId);
		
		// Alexei plays a yellow card, then finishes her turn
		JSONObject playCard = RequestBuilder.buildCardMove("y2");
		alexei.handleClientRequest(playCard);
		Thread.sleep(WAIT_TIME_MILLIS);
		
		ITM.verifyPlayer(controller, alexeiId, 1, 2, 7, 0);
		ITM.verifyTournament(controller, Token.YELLOW, 3, alexeiId, 2);
		ITM.verifyController(controller, 3, alexeiId);
		
		JSONObject finishTurn = RequestBuilder.buildFinishTurn();
		alexei.handleClientRequest(finishTurn);
		Thread.sleep(WAIT_TIME_MILLIS);
		
		ITM.verifyController(controller, 3, lukeId);
		
		// Luke tries to play an invalid card, then withdraws
		playCard = RequestBuilder.buildCardMove("p3");
		luke.handleClientRequest(playCard);
		Thread.sleep(WAIT_TIME_MILLIS);
		
		ITM.verifyServerResponse(luke, ".*invalid_choice.*", 1);
		JSONObject withdraw = RequestBuilder.buildWithdrawMove();
		luke.handleClientRequest(withdraw);
		Thread.sleep(WAIT_TIME_MILLIS);
		
		ITM.verifyPlayer(controller, lukeId, 0, 0, -1, 0);
		ITM.verifyTournament(controller, null, 2, alexeiId, 2);
		ITM.verifyController(controller, 3, jaysonId);
		
		// jayson plays y4
		playCard = RequestBuilder.buildCardMove("y4");
		jayson.handleClientRequest(playCard);
		Thread.sleep(WAIT_TIME_MILLIS);
		ITM.finishTurn(jayson);
		
		assertEquals(controller.getPlayers().get(jaysonId).getDisplay().size(), 1);
		assertEquals(controller.getPlayers().get(jaysonId).getDisplayTotal(Token.YELLOW), 4);
		assertEquals(controller.getPlayers().get(jaysonId).getHand().size(), 8);
		assertEquals(controller.getTournament().getPlayerWithHighestDisplay().getName().toLowerCase(), "jayson");
		assertEquals(controller.getTournament().getHighestDisplayTotal(), 4);
		assertEquals(controller.getCurrentTurnId(), alexeiId);
		
		// alexei withdraws
		alexei.handleClientRequest(withdraw);
		
		Thread.sleep(WAIT_TIME_MILLIS);
		
				// TURN 2
		
		// check that a new tournament has been started
		assertEquals(controller.getTournament().getToken(), Token.UNDECIDED);
		assertEquals(controller.getPlayerTurns().size(), 3);
		assertEquals(controller.getCurrentTurnId(), jaysonId);
		assertEquals(controller.getPlayers().get(jaysonId).getTokens().size(), 1);
		assertTrue(controller.getPlayers().get(jaysonId).getTokens().contains(Token.YELLOW));
		
		Mockito.verify(jayson).handleServerResponse(Mockito.matches(".*tournament_over_win.*"));
		Mockito.verify(alexei).handleServerResponse(Mockito.matches(".*tournament_over_loss.*"));
		Mockito.verify(luke).handleServerResponse(Mockito.matches(".*tournament_over_loss.*"));
		
		for (Player p: controller.getPlayers().values()) {
			assertEquals(p.getDisplay().size(), 0);
		}
		
		controller.setPlayerTurns(turnSequence);
		controller.setTurn(jaysonId);
		
		// jayson plays p3
		playCard = RequestBuilder.buildCardMove("p3");
		jayson.handleClientRequest(playCard);
		Thread.sleep(WAIT_TIME_MILLIS);
		ITM.finishTurn(jayson);
		
		assertEquals(controller.getPlayers().get(jaysonId).getDisplay().size(), 1);
		assertEquals(controller.getPlayers().get(jaysonId).getDisplayTotal(Token.PURPLE), 3);
		assertEquals(controller.getTournament().getPlayerWithHighestDisplay().getName().toLowerCase(), "jayson");
		assertEquals(controller.getTournament().getHighestDisplayTotal(), 3);
		
		assertEquals(controller.getTournament().getToken(), Token.PURPLE);
		assertEquals(controller.getCurrentTurnId(), alexeiId);
		
		// alexei withdraws
		alexei.handleClientRequest(withdraw);
				
		Thread.sleep(WAIT_TIME_MILLIS);
		assertEquals(controller.getCurrentTurnId(), lukeId);
		assertEquals(controller.getPlayerTurns().size(), 2);
		
		// luke plays 2 cards
		int lukeHandSize = controller.getCurrentTurnPlayer().getHand().size();
		String[] cardsToPlay = {"p3", "p5"};
		JSONObject multipleCardsMove = RequestBuilder.buildMultipleCardsMove(cardsToPlay);
		luke.handleClientRequest(multipleCardsMove);
		Thread.sleep(WAIT_TIME_MILLIS*2);
		ITM.finishTurn(luke);
		
		System.out.println(controller.getPlayers().get(lukeId).getDisplay());
		assertEquals(controller.getPlayers().get(lukeId).getDisplay().size(), 2);
		assertEquals(controller.getPlayers().get(lukeId).getHand().size(), lukeHandSize - 2);
		assertEquals(controller.getPlayers().get(lukeId).getDisplayTotal(Token.PURPLE), 8);
		assertEquals(controller.getTournament().getPlayerWithHighestDisplay().getName().toLowerCase(), "luke");
		assertEquals(controller.getTournament().getHighestDisplayTotal(), 8);
		assertEquals(controller.getCurrentTurnId(), jaysonId);
		
		// jayson withdraws
		jayson.handleClientRequest(withdraw);
		
		Thread.sleep(WAIT_TIME_MILLIS);
		
		// since luke won a purple tournament, he gets to pick the color of his token
		assertEquals(controller.getCurrentTurnId(), lukeId);
		assertEquals(controller.getTournament().getPlayerWithHighestDisplay().getName().toLowerCase(), "luke");
		assertEquals(controller.getTournament().getToken(), Token.PURPLE);
		assertEquals(controller.getPlayerTurns().size(), 1);
		assertEquals(controller.getPlayers().get(lukeId).getTokens().size(), 0);
		
		Mockito.verify(luke).handleServerResponse(Mockito.matches(".*tournament_over_win.*"));
		
		JSONObject chooseColor = RequestBuilder.buildChooseToken("purple");
		luke.handleClientRequest(chooseColor);
		
		Thread.sleep(WAIT_TIME_MILLIS);
		
		assertEquals(controller.getTournament().getToken(), Token.UNDECIDED);
		assertEquals(controller.getPlayerTurns().size(), 3);
		assertEquals(controller.getCurrentTurnId(), lukeId);
		assertEquals(controller.getPlayers().get(lukeId).getTokens().size(), 1);
		assertTrue(controller.getPlayers().get(lukeId).getTokens().contains(Token.PURPLE));
		
		assertEquals(controller.getPlayers().get(jaysonId).getTokens().size(), 1);
		assertEquals(controller.getPlayers().get(alexeiId).getTokens().size(), 0);
		
		controller.setPlayerTurns(turnSequence);
		controller.setTurn(lukeId);
		
				// TURN 3
		
		// luke opens the turn with a supporter card
		playCard = RequestBuilder.buildCardMove("s3");
		luke.handleClientRequest(playCard);
		
		Thread.sleep(WAIT_TIME_MILLIS);
		
		assertEquals(controller.getTournament().getToken(), Token.UNDECIDED);
		assertEquals(controller.getCurrentTurnId(), lukeId);
		Mockito.verify(luke).handleServerResponse(Mockito.matches(".*choose_color.*"));
		
		// luke tries to start another purple tournament, even though a purple tournament has just been won
		chooseColor = RequestBuilder.buildChooseToken("purple");
		luke.handleClientRequest(chooseColor);
		
		Thread.sleep(WAIT_TIME_MILLIS);
		
		assertEquals(controller.getTournament().getToken(), Token.UNDECIDED);
		assertEquals(controller.getCurrentTurnId(), lukeId);
		Mockito.verify(luke, Mockito.atLeast(2)).handleServerResponse(Mockito.matches(".*invalid_choice.*"));
		
		chooseColor = RequestBuilder.buildChooseToken("green");
		luke.handleClientRequest(chooseColor);
		Thread.sleep(WAIT_TIME_MILLIS);
		ITM.finishTurn(luke);
		
		// test rules for GREEN tournament
		assertEquals(controller.getPlayers().get(lukeId).getDisplay().size(), 1);
		assertEquals(controller.getPlayers().get(lukeId).getDisplayTotal(Token.GREEN), 1);
		assertEquals(controller.getTournament().getPlayerWithHighestDisplay().getName().toLowerCase(), "luke");
		assertEquals(controller.getTournament().getHighestDisplayTotal(), 1);
		
		assertEquals(controller.getTournament().getToken(), Token.GREEN);
		assertEquals(controller.getCurrentTurnId(), jaysonId);
		
		// jayson plays 2 squires
		int jaysonHandSize = controller.getCurrentTurnPlayer().getHand().size();
		String[] jaysonCardsToPlay = {"s3", "s3"};
		multipleCardsMove = RequestBuilder.buildMultipleCardsMove(jaysonCardsToPlay);
		jayson.handleClientRequest(multipleCardsMove);
		ITM.finishTurn(jayson);
		
		Thread.sleep(WAIT_TIME_MILLIS);
		
		assertEquals(controller.getPlayers().get(jaysonId).getDisplay().size(), 2);
		assertEquals(controller.getPlayers().get(jaysonId).getHand().size(), jaysonHandSize - 2);
		assertEquals(controller.getPlayers().get(jaysonId).getDisplayTotal(Token.GREEN), 2);
		assertEquals(controller.getTournament().getPlayerWithHighestDisplay().getName().toLowerCase(), "jayson");
		assertEquals(controller.getTournament().getHighestDisplayTotal(), 2);
		assertEquals(controller.getCurrentTurnId(), alexeiId);
		
		// alexei withdraws
		alexei.handleClientRequest(withdraw);
		Thread.sleep(WAIT_TIME_MILLIS);
		
		assertEquals(controller.getCurrentTurnId(), lukeId);
		assertEquals(controller.getPlayerTurns().size(), 2);
		
		// luke plays three green cards
		String[] lukeCardsToPlay = {"g1", "g1", "g1"};
		multipleCardsMove = RequestBuilder.buildMultipleCardsMove(lukeCardsToPlay);
		luke.handleClientRequest(multipleCardsMove);
		Thread.sleep(WAIT_TIME_MILLIS);
		ITM.finishTurn(luke);
		
		assertEquals(controller.getPlayers().get(lukeId).getDisplay().size(), 4);
		assertEquals(controller.getPlayers().get(lukeId).getDisplayTotal(Token.GREEN), 4);
		assertEquals(controller.getTournament().getPlayerWithHighestDisplay().getName().toLowerCase(), "luke");
		assertEquals(controller.getTournament().getHighestDisplayTotal(), 4);
		assertEquals(controller.getCurrentTurnId(), jaysonId);
		
		// jayson withdraws
		jayson.handleClientRequest(withdraw);
		Thread.sleep(WAIT_TIME_MILLIS);
		
		assertEquals(controller.getTournament().getToken(), Token.UNDECIDED);
		assertEquals(controller.getPlayerTurns().size(), 3);
		assertEquals(controller.getCurrentTurnId(), lukeId);
		assertEquals(controller.getPlayers().get(lukeId).getTokens().size(), 2);
		assertTrue(controller.getPlayers().get(lukeId).getTokens().contains(Token.GREEN));
		
		assertEquals(controller.getPlayers().get(jaysonId).getTokens().size(), 1);
		assertEquals(controller.getPlayers().get(alexeiId).getTokens().size(), 0);
		
		controller.setPlayerTurns(turnSequence);
		controller.setTurn(lukeId);
		
			// TURN 4
		
		// luke plays r5
		playCard = RequestBuilder.buildCardMove("r5");
		luke.handleClientRequest(playCard);
		Thread.sleep(WAIT_TIME_MILLIS);
		ITM.finishTurn(luke);
		
		assertEquals(controller.getTournament().getToken(), Token.RED);
		assertEquals(controller.getCurrentTurnId(), jaysonId);
		assertEquals(controller.getPlayers().get(lukeId).getDisplay().size(), 1);
		assertEquals(controller.getPlayers().get(lukeId).getDisplayTotal(Token.RED), 5);
		assertEquals(controller.getTournament().getPlayerWithHighestDisplay().getName().toLowerCase(), "luke");
		assertEquals(controller.getTournament().getHighestDisplayTotal(), 5);
		
		// jayson plays r3, m6
		String[] jaysonCards2 = {"r3", "m6"};
		multipleCardsMove = RequestBuilder.buildMultipleCardsMove(jaysonCards2);
		jayson.handleClientRequest(multipleCardsMove);
		Thread.sleep(WAIT_TIME_MILLIS);
		ITM.finishTurn(jayson);
		
		assertEquals(controller.getCurrentTurnId(), alexeiId);
		assertEquals(controller.getPlayers().get(jaysonId).getDisplay().size(), 2);
		assertEquals(controller.getPlayers().get(jaysonId).getDisplayTotal(Token.RED), 9);
		assertEquals(controller.getTournament().getPlayerWithHighestDisplay().getName().toLowerCase(), "jayson");
		assertEquals(controller.getTournament().getHighestDisplayTotal(), 9);
		
		// alexei withdraws
		alexei.handleClientRequest(withdraw);
		Thread.sleep(WAIT_TIME_MILLIS);
		
		assertEquals(controller.getCurrentTurnId(), lukeId);
		assertEquals(controller.getPlayerTurns().size(), 2);
		
		// luke plays m6
		playCard = RequestBuilder.buildCardMove("m6");
		luke.handleClientRequest(playCard);
		Thread.sleep(WAIT_TIME_MILLIS);
		ITM.finishTurn(luke);
		
		assertEquals(controller.getCurrentTurnId(), jaysonId);
		assertEquals(controller.getPlayers().get(lukeId).getDisplay().size(), 2);
		assertEquals(controller.getPlayers().get(lukeId).getDisplayTotal(Token.RED), 11);
		assertEquals(controller.getTournament().getPlayerWithHighestDisplay().getName().toLowerCase(), "luke");
		assertEquals(controller.getTournament().getHighestDisplayTotal(), 11);
		
		// jayson withdraws, loses token
		jayson.handleClientRequest(withdraw);
		Thread.sleep(WAIT_TIME_MILLIS);
		
		// since Jayson has a maiden in his hand, he must choose a token to withdraw
		JSONObject chooseToken = RequestBuilder.buildChooseToken("yellow");
		jayson.handleClientRequest(chooseToken);
		Thread.sleep(WAIT_TIME_MILLIS);
		
		assertEquals(controller.getTournament().getToken(), Token.UNDECIDED);
		assertEquals(controller.getPlayerTurns().size(), 3);
		assertEquals(controller.getCurrentTurnId(), lukeId);
		assertEquals(controller.getPlayers().get(lukeId).getTokens().size(), 3);
		assertTrue(controller.getPlayers().get(lukeId).getTokens().contains(Token.RED));
		
		assertEquals(controller.getPlayers().get(jaysonId).getTokens().size(), 0);
		assertEquals(controller.getPlayers().get(alexeiId).getTokens().size(), 0);
		
		controller.setPlayerTurns(turnSequence);
		controller.setTurn(lukeId);
		
			// TURN 5
		
		// give Luke a new hand (for testing purposes)
		String[] newLukeCards = {"m6", "s3", "b4", "b3", "b5"};
		controller.swapHand(lukeId, controller.getCardsFromStrings(newLukeCards));
		
		// luke plays a blue card
		playCard = RequestBuilder.buildCardMove("b3");
		luke.handleClientRequest(playCard);
		Thread.sleep(WAIT_TIME_MILLIS);
		ITM.finishTurn(luke);
		
		assertEquals(controller.getTournament().getToken(), Token.BLUE);
		assertEquals(controller.getCurrentTurnId(), jaysonId);
		assertEquals(controller.getPlayers().get(lukeId).getDisplay().size(), 1);
		assertEquals(controller.getPlayers().get(lukeId).getDisplayTotal(Token.BLUE), 3);
		assertEquals(controller.getTournament().getPlayerWithHighestDisplay().getName().toLowerCase(), "luke");
		assertEquals(controller.getTournament().getHighestDisplayTotal(), 3);
		
		// jayson plays a blue card
		playCard = RequestBuilder.buildCardMove("b4");
		jayson.handleClientRequest(playCard);
		Thread.sleep(WAIT_TIME_MILLIS);
		ITM.finishTurn(jayson);
		
		assertEquals(controller.getCurrentTurnId(), alexeiId);
		assertEquals(controller.getPlayers().get(jaysonId).getDisplay().size(), 1);
		assertEquals(controller.getPlayers().get(jaysonId).getDisplayTotal(Token.BLUE), 4);
		assertEquals(controller.getTournament().getPlayerWithHighestDisplay().getName().toLowerCase(), "jayson");
		assertEquals(controller.getTournament().getHighestDisplayTotal(), 4);
		
		// alexei plays a blue card
		playCard = RequestBuilder.buildCardMove("b5");
		alexei.handleClientRequest(playCard);
		Thread.sleep(WAIT_TIME_MILLIS);
		ITM.finishTurn(alexei);
		
		assertEquals(controller.getCurrentTurnId(), lukeId);
		assertEquals(controller.getPlayers().get(alexeiId).getDisplay().size(), 1);
		assertEquals(controller.getPlayers().get(alexeiId).getDisplayTotal(Token.BLUE), 5);
		assertEquals(controller.getTournament().getPlayerWithHighestDisplay().getName().toLowerCase(), "alexei");
		assertEquals(controller.getTournament().getHighestDisplayTotal(), 5);
		
		// luke plays b5
		playCard = RequestBuilder.buildCardMove("b5");
		luke.handleClientRequest(playCard);
		Thread.sleep(WAIT_TIME_MILLIS);
		ITM.finishTurn(luke);
		
		assertEquals(controller.getCurrentTurnId(), jaysonId);
		assertEquals(controller.getPlayers().get(lukeId).getDisplay().size(), 2);
		assertEquals(controller.getPlayers().get(lukeId).getDisplayTotal(Token.BLUE), 8);
		assertEquals(controller.getTournament().getPlayerWithHighestDisplay().getName().toLowerCase(), "luke");
		assertEquals(controller.getTournament().getHighestDisplayTotal(), 8);
		
		// jayson withdraws
		jayson.handleClientRequest(withdraw);
		Thread.sleep(WAIT_TIME_MILLIS);
		
		assertEquals(controller.getCurrentTurnId(), alexeiId);
		assertEquals(controller.getPlayerTurns().size(), 2);
		
		// alexei withdraws
		alexei.handleClientRequest(withdraw);
		Thread.sleep(WAIT_TIME_MILLIS);
		
		assertEquals(controller.getTournament().getToken(), Token.UNDECIDED);
		assertEquals(controller.getPlayerTurns().size(), 3);
		assertEquals(controller.getCurrentTurnId(), lukeId);
		assertEquals(controller.getPlayers().get(lukeId).getTokens().size(), 4);
		assertTrue(controller.getPlayers().get(lukeId).getTokens().contains(Token.BLUE));
		
		assertEquals(controller.getPlayers().get(jaysonId).getTokens().size(), 0);
		assertEquals(controller.getPlayers().get(alexeiId).getTokens().size(), 0);
		
		controller.setPlayerTurns(turnSequence);
		controller.setTurn(lukeId);
	}

}
