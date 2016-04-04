package comp3004.ivanhoe.game_logic;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import comp3004.ivanhoe.controller.MockController;
import comp3004.ivanhoe.model.Card;
import comp3004.ivanhoe.model.Player;
import comp3004.ivanhoe.model.Token;
import comp3004.ivanhoe.model.Tournament;
import comp3004.ivanhoe.server.MockServer;
import comp3004.ivanhoe.util.RequestBuilder;

public class StartTournamentTest {

	HashMap<Integer, Player> players;
	Tournament tournament;
	MockController controller;
	Player alexei, luke;
	Card r3, p5, s3, m6;
	ArrayList<Card> r3Wrapper, p5Wrapper, s3Wrapper, m6Wrapper;
	
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
		
		r3 = controller.getCardFromDeck("r3");
		r3Wrapper = new ArrayList<Card>();
		r3Wrapper.add(r3);
		
		p5 = controller.getCardFromDeck("p5");
		p5Wrapper = new ArrayList<Card>();
		p5Wrapper.add(p5);
		
		s3 = controller.getCardFromDeck("s3");
		s3Wrapper = new ArrayList<Card>();
		s3Wrapper.add(s3);
		
		m6 = controller.getCardFromDeck("m6");
		m6Wrapper = new ArrayList<Card>();
		m6Wrapper.add(m6);
		
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * If a user plays a color card on the first round, the color
	 * of the card determines the color of the tournament
	 */
	@Test
	public void testPlayColorCard() {
		
		tournament.setToken(Token.UNDECIDED);
		controller.setTurn(60001);
		controller.setState(2); // WAITING_FOR_TOURNAMENT_COLOR
		
		alexei.addHandCard(r3);
		JSONObject playR3 = RequestBuilder.buildCardMove(r3.toString());
		controller.processPlayerMove(60001, playR3);
		
		assertEquals(controller.getTournament().getToken(), Token.RED);
		assertEquals(controller.getCurrentTurnId(), 60002); // The turn has been changed
		assertEquals(controller.getState(), 3); // WAITING_FOR_PLAYER_MOVE
		assertEquals(alexei.getDisplayTotal(Token.RED), 3);
		
	}
	
	/**
	 * If a user plays a supporter on the first round, they are
	 * required to pick the color of the tournament (given that
	 * the previous tournament was NOT purple)
	 */
	@Test
	public void testPlaySupporterCard() {
		tournament.setToken(Token.UNDECIDED);
		controller.setTurn(60001);
		controller.setState(2); // WAITING_FOR_TOURNAMENT_COLOR
		
		alexei.addHandCard(s3);
		JSONObject playS3 = RequestBuilder.buildCardMove(s3.toString());
		controller.processPlayerMove(60001, playS3);
		
		assertEquals(controller.getTournament().getToken(), Token.UNDECIDED);
		assertEquals(controller.getState(), 2); // WAITING_FOR_TOURNAMENT_COLOR
		assertEquals(controller.getCurrentTurnId(), 60001); // The turn has NOT been changed
		assertEquals(alexei.getDisplayTotal(Token.BLUE), 0);
		
		JSONObject chooseColor = RequestBuilder.buildChooseToken("blue");
		controller.processPlayerMove(60001, chooseColor);
		
		assertEquals(controller.getTournament().getToken(), Token.BLUE);
		assertEquals(controller.getState(), 3); // WAITING_FOR_PLAYER_MOVE
		assertEquals(controller.getCurrentTurnId(), 60002); // The turn has been changed
		assertEquals(alexei.getDisplayTotal(Token.BLUE), 3);
		
	}
	
	/**
	 * If a user plays a supporter on the first round, they are
	 * required to pick the color of the tournament (given that
	 * the previous tournament was purple)
	 */
	@Test
	public void testPlaySupporterCardPreviousPurple() {
		tournament.setToken(Token.UNDECIDED);
		controller.setPreviousTournament(Token.PURPLE);
		controller.setTurn(60001);
		controller.setState(2); // WAITING_FOR_TOURNAMENT_COLOR
		
		alexei.addHandCard(s3);
		JSONObject playS3 = RequestBuilder.buildCardMove(s3.toString());
		controller.processPlayerMove(60001, playS3);
		
		assertEquals(controller.getTournament().getToken(), Token.UNDECIDED);
		assertEquals(controller.getState(), 2); // WAITING_FOR_TOURNAMENT_COLOR
		assertEquals(controller.getCurrentTurnId(), 60001); // The turn has NOT been changed
		assertEquals(alexei.getDisplayTotal(Token.YELLOW), 0);
		
		JSONObject chooseColor = RequestBuilder.buildChooseToken("purple");
		controller.processPlayerMove(60001, chooseColor);
		
		// check that move was rejected
		assertEquals(controller.getTournament().getToken(), Token.UNDECIDED);
		assertEquals(controller.getState(), 2); // WAITING_FOR_TOURNAMENT_COLOR
		assertEquals(controller.getCurrentTurnId(), 60001); // The turn has NOT been changed
		assertEquals(alexei.getDisplayTotal(Token.YELLOW), 0);
		
		chooseColor = RequestBuilder.buildChooseToken("yellow");
		controller.processPlayerMove(60001, chooseColor);
		
		assertEquals(controller.getTournament().getToken(), Token.YELLOW);
		assertEquals(controller.getState(), 3); // WAITING_FOR_PLAYER_MOVE
		assertEquals(controller.getCurrentTurnId(), 60002); // The turn has been changed
		assertEquals(alexei.getDisplayTotal(Token.YELLOW), 3);
	}

}
