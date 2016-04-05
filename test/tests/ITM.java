package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.json.simple.JSONObject;
import org.mockito.Mockito;

import comp3004.ivanhoe.client.AppClient;
import comp3004.ivanhoe.controller.MockController;
import comp3004.ivanhoe.model.Token;
import comp3004.ivanhoe.util.Config;
import comp3004.ivanhoe.util.RequestBuilder;
import comp3004.ivanhoe.view.MockViewFactory;

/**
 * ITM: Integration Test Methods
 * Class full of static methods that can be used in integration tests
 * @author PJF
 *
 */
public abstract class ITM {

	public static AppClient initClient(String username, int serverPort) {
		AppClient newClient = new AppClient(new MockViewFactory(), Config.DEFAULT_SERVER_ADDRESS, serverPort);
		AppClient client = Mockito.spy(newClient);
		client.setUsername(username);
		assertTrue(client.connect());
		return client;
	}
	
	public static void verifyInitialState(MockController ctrl, int numPlayers) {
		assertEquals(ctrl.getPlayers().size(), numPlayers);
		assertEquals(ctrl.getTournament().getPlayers().size(), numPlayers);
		assertEquals(ctrl.getTournament().getToken(), Token.UNDECIDED);
	}
	
	/**
	 * Check the state of a player
	 * @param ctrl
	 * @param playerId
	 * @param displaySize
	 * @param displayTotal
	 * @param handSize
	 */
	public static void verifyPlayer(MockController ctrl, int playerId, int displaySize, int displayTotal,
									int handSize, int specialSize) {
		if (displaySize != -1) assertEquals(ctrl.getPlayers().get(playerId).getDisplay().size(), displaySize);
		if (displayTotal != -1) assertEquals(ctrl.getPlayers().get(playerId).getDisplayTotal(ctrl.getTournament().getToken()), displayTotal);
		if (handSize != -1) assertEquals(ctrl.getPlayers().get(playerId).getHand().size(), handSize);		
		if (specialSize != -1) assertEquals(ctrl.getPlayers().get(playerId).getSpecial().size(), specialSize);
	}
	
	/**
	 * Check the state of a tournament
	 * @param ctrl
	 * @param tkn
	 * @param highestPlayerId
	 * @param highestPlayerDisplay
	 */
	public static void verifyTournament(MockController ctrl, Token tkn, int numPlayers,
										int highestPlayerId, int highestPlayerDisplay) {
		if (tkn != null) assertEquals(ctrl.getTournament().getToken(), tkn);
		if (numPlayers != -1) assertEquals(ctrl.getPlayerTurns().size(), numPlayers);
		if (highestPlayerId != -1) assertEquals(ctrl.getTournament().getPlayerWithHighestDisplay(),
					 ctrl.getPlayers().get(highestPlayerId));
		if (highestPlayerDisplay != -1) assertEquals(ctrl.getTournament().getHighestDisplayTotal(), highestPlayerDisplay);
	}
	
	/**
	 * Check the state of the controller
	 * @param ctrl
	 * @param state
	 * @param turnPlayerId
	 */
	public static void verifyController(MockController ctrl, int state, int turnPlayerId) {
		if (state != -1) assertEquals(ctrl.getState(), state);
		if (turnPlayerId != -1) assertEquals(ctrl.getCurrentTurnId(), turnPlayerId);
	}
	
	public static void verifyServerResponse(AppClient client, String re, int itLeastNum) {
		try {
			Mockito.verify(client, Mockito.atLeast(itLeastNum)).handleServerResponse(Mockito.matches(re));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void finishTurn(AppClient client) throws IOException, InterruptedException {
		JSONObject finishTurn = RequestBuilder.buildFinishTurn();
		client.handleClientRequest(finishTurn);
		Thread.sleep(400);
	}
	
}
