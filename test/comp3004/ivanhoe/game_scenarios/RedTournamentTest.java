package comp3004.ivanhoe.game_scenarios;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import comp3004.ivanhoe.controller.MockController;
import comp3004.ivanhoe.model.Player;
import comp3004.ivanhoe.model.Token;
import comp3004.ivanhoe.model.Tournament;
import comp3004.ivanhoe.server.MockServer;
import comp3004.ivanhoe.util.RequestBuilder;

public class RedTournamentTest {

	HashMap<Integer, Player> players;
	Tournament tournament;
	MockController controller;
	Player alexei, luke, jayson, emma;
	ArrayList<String> cardWrapper;
	JSONObject finishTurn, withdraw;
	
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
		tournament.setPlayers(players);
		controller.setTournament(tournament);
		controller.setState(2);

		cardWrapper = new ArrayList<String>();
		
		finishTurn = RequestBuilder.buildFinishTurn();
		withdraw = RequestBuilder.buildWithdrawMove();
		
		Integer[] turnSequence = {60001, 60002, 60003, 60004};
		controller.setPlayerTurns(turnSequence);
		controller.setTurn(60001);
		
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * 1 player starts, all other players withdraw
	 * Player 1 wins tournament and gets a token
	 */
	@Test
	public void testScenarioA() {
		
		alexei.addHandCard(controller.getCardFromDeck("r5"));
		
		JSONObject playCard = RequestBuilder.buildCardMove("r5");
		controller.processPlayerMove(60001, playCard);
		
		assertEquals(tournament.getToken(), Token.RED);
		assertEquals(controller.getState(), 3);
		assertEquals(controller.getCurrentTurnId(), 60001);
		assertEquals(alexei.getDisplay().size(), 1);
		assertEquals(alexei.getDisplayTotal(Token.RED), 5);
		
		controller.processPlayerMove(60001, finishTurn);
		
		assertEquals(controller.getCurrentTurnId(), 60002);
		controller.processPlayerMove(60002, withdraw);
		
		assertEquals(controller.getCurrentTurnId(), 60003);
		controller.processPlayerMove(60003, withdraw);
		
		assertEquals(controller.getCurrentTurnId(), 60004);
		controller.processPlayerMove(60004, withdraw);
		
		assertEquals(controller.getCurrentTurnId(), 60001);
		assertEquals(alexei.getTokens().size(), 1);
		assertTrue(alexei.getTokens().contains(Token.RED));
			
	}
	
	/**
	 * One other player plays a card
	 */
	@Test
	public void testScenarioB1() {
		
		alexei.addHandCard(controller.getCardFromDeck("r3"));
		JSONObject playCard = RequestBuilder.buildCardMove("r3");
		controller.processPlayerMove(60001, playCard);
		
		assertEquals(tournament.getToken(), Token.RED);
		assertEquals(controller.getState(), 3);
		assertEquals(controller.getCurrentTurnId(), 60001);
		assertEquals(alexei.getDisplay().size(), 1);
		assertEquals(alexei.getDisplayTotal(Token.RED), 3);
		
		controller.processPlayerMove(60001, finishTurn);
		
		assertEquals(controller.getCurrentTurnId(), 60002);
		
		luke.addHandCard(controller.getCardFromDeck("r5"));
		playCard = RequestBuilder.buildCardMove("r5");
		controller.processPlayerMove(60002, playCard);
		
		assertEquals(controller.getState(), 3);
		assertEquals(controller.getCurrentTurnId(), 60002);
		assertEquals(luke.getDisplay().size(), 1);
		assertEquals(luke.getDisplayTotal(Token.RED), 5);
		assertEquals(tournament.getPlayerWithHighestDisplay(), luke);
		controller.processPlayerMove(60002, finishTurn);
		
		assertEquals(controller.getCurrentTurnId(), 60003);
		controller.processPlayerMove(60003, withdraw);
		
		assertEquals(controller.getCurrentTurnId(), 60004);
		controller.processPlayerMove(60004, withdraw);
		
		assertEquals(controller.getCurrentTurnId(), 60001);
		assertEquals(alexei.getTokens().size(), 0);
	}
	
	/**
	 * One other player plays several cards
	 */
	@Test
	public void testScenarioB2() {
		alexei.addHandCard(controller.getCardFromDeck("r3"));
		JSONObject playCard = RequestBuilder.buildCardMove("r3");
		controller.processPlayerMove(60001, playCard);
		
		assertEquals(tournament.getToken(), Token.RED);
		assertEquals(controller.getState(), 3);
		assertEquals(controller.getCurrentTurnId(), 60001);
		assertEquals(alexei.getDisplay().size(), 1);
		assertEquals(alexei.getDisplayTotal(Token.RED), 3);
		
		controller.processPlayerMove(60001, finishTurn);
		
		assertEquals(controller.getCurrentTurnId(), 60002);
		
		luke.addHandCard(controller.getCardFromDeck("r5"));
		luke.addHandCard(controller.getCardFromDeck("r3"));
		playCard = RequestBuilder.buildMultipleCardsMove(new String[] {"r5", "r3"});
		controller.processPlayerMove(60002, playCard);
		
		assertEquals(controller.getState(), 3);
		assertEquals(controller.getCurrentTurnId(), 60002);
		assertEquals(luke.getDisplay().size(), 2);
		assertEquals(luke.getDisplayTotal(Token.RED), 8);
		assertEquals(tournament.getPlayerWithHighestDisplay(), luke);
		controller.processPlayerMove(60002, finishTurn);
		
		assertEquals(controller.getCurrentTurnId(), 60003);
		controller.processPlayerMove(60003, withdraw);
		
		assertEquals(controller.getCurrentTurnId(), 60004);
		controller.processPlayerMove(60004, withdraw);
		
		assertEquals(controller.getCurrentTurnId(), 60001);
		assertEquals(alexei.getTokens().size(), 0);
	}
	
	/**
	 * A couple players play one card
	 */
	@Test
	public void testScenarioC1() {
		alexei.addHandCard(controller.getCardFromDeck("r3"));
		JSONObject playCard = RequestBuilder.buildCardMove("r3");
		controller.processPlayerMove(60001, playCard);
		
		assertEquals(tournament.getToken(), Token.RED);
		assertEquals(controller.getState(), 3);
		assertEquals(controller.getCurrentTurnId(), 60001);
		assertEquals(alexei.getDisplay().size(), 1);
		assertEquals(alexei.getDisplayTotal(Token.RED), 3);
		
		controller.processPlayerMove(60001, finishTurn);
		
		assertEquals(controller.getCurrentTurnId(), 60002);
		
		luke.addHandCard(controller.getCardFromDeck("r4"));
		playCard = RequestBuilder.buildCardMove("r4");
		controller.processPlayerMove(60002, playCard);
		
		assertEquals(controller.getState(), 3);
		assertEquals(controller.getCurrentTurnId(), 60002);
		assertEquals(luke.getDisplay().size(), 1);
		assertEquals(luke.getDisplayTotal(Token.RED), 4);
		assertEquals(tournament.getPlayerWithHighestDisplay(), luke);
		controller.processPlayerMove(60002, finishTurn);
		
		assertEquals(controller.getCurrentTurnId(), 60003);
		
		jayson.addHandCard(controller.getCardFromDeck("r5"));
		playCard = RequestBuilder.buildCardMove("r5");
		controller.processPlayerMove(60003, playCard);
		
		assertEquals(controller.getState(), 3);
		assertEquals(controller.getCurrentTurnId(), 60003);
		assertEquals(jayson.getDisplay().size(), 1);
		assertEquals(jayson.getDisplayTotal(Token.RED), 5);
		assertEquals(tournament.getPlayerWithHighestDisplay(), jayson);
		controller.processPlayerMove(60003, finishTurn);
		
		assertEquals(controller.getCurrentTurnId(), 60004);
		controller.processPlayerMove(60004, withdraw);
		
		assertEquals(controller.getCurrentTurnId(), 60001);
		assertEquals(alexei.getTokens().size(), 0);
	}
	
	/**
	 * A couple players play several cards
	 */
	@Test
	public void testScenarioC2() {
		alexei.addHandCard(controller.getCardFromDeck("r3"));
		JSONObject playCard = RequestBuilder.buildCardMove("r3");
		controller.processPlayerMove(60001, playCard);
		
		assertEquals(tournament.getToken(), Token.RED);
		assertEquals(controller.getState(), 3);
		assertEquals(controller.getCurrentTurnId(), 60001);
		assertEquals(alexei.getDisplay().size(), 1);
		assertEquals(alexei.getDisplayTotal(Token.RED), 3);
		
		controller.processPlayerMove(60001, finishTurn);
		
		//ÊLuke plays multiple cards
		assertEquals(controller.getCurrentTurnId(), 60002);
		
		luke.addHandCard(controller.getCardFromDeck("r5"));
		luke.addHandCard(controller.getCardFromDeck("r3"));
		playCard = RequestBuilder.buildMultipleCardsMove(new String[] {"r5", "r3"});
		controller.processPlayerMove(60002, playCard);
		
		assertEquals(controller.getState(), 3);
		assertEquals(controller.getCurrentTurnId(), 60002);
		assertEquals(luke.getDisplay().size(), 2);
		assertEquals(luke.getDisplayTotal(Token.RED), 8);
		assertEquals(tournament.getPlayerWithHighestDisplay(), luke);
		controller.processPlayerMove(60002, finishTurn);
		
		// Jayson plays multiple cards
		assertEquals(controller.getCurrentTurnId(), 60003);
		
		jayson.addHandCard(controller.getCardFromDeck("r5"));
		jayson.addHandCard(controller.getCardFromDeck("r3"));
		jayson.addHandCard(controller.getCardFromDeck("r5"));
		playCard = RequestBuilder.buildMultipleCardsMove(new String[] {"r5", "r3", "r5"});
		controller.processPlayerMove(60003, playCard);
		
		assertEquals(controller.getState(), 3);
		assertEquals(controller.getCurrentTurnId(), 60003);
		assertEquals(jayson.getDisplay().size(), 3);
		assertEquals(jayson.getDisplayTotal(Token.RED), 13);
		assertEquals(tournament.getPlayerWithHighestDisplay(), jayson);
		controller.processPlayerMove(60003, finishTurn);
		
		// Emma withdraws
		assertEquals(controller.getCurrentTurnId(), 60004);
		controller.processPlayerMove(60004, withdraw);
		
		assertEquals(controller.getCurrentTurnId(), 60001);
		assertEquals(alexei.getTokens().size(), 0);
	}
	
	/**
	 * All players play 1 card
	 */
	@Test
	public void testScenarioD1() {
		alexei.addHandCard(controller.getCardFromDeck("r3"));
		JSONObject playCard = RequestBuilder.buildCardMove("r3");
		controller.processPlayerMove(60001, playCard);
		
		assertEquals(tournament.getToken(), Token.RED);
		assertEquals(controller.getState(), 3);
		assertEquals(controller.getCurrentTurnId(), 60001);
		assertEquals(alexei.getDisplay().size(), 1);
		assertEquals(alexei.getDisplayTotal(Token.RED), 3);
		
		controller.processPlayerMove(60001, finishTurn);
		
		assertEquals(controller.getCurrentTurnId(), 60002);
		
		// Luke plays a card
		luke.addHandCard(controller.getCardFromDeck("r4"));
		playCard = RequestBuilder.buildCardMove("r4");
		controller.processPlayerMove(60002, playCard);
		
		assertEquals(controller.getState(), 3);
		assertEquals(controller.getCurrentTurnId(), 60002);
		assertEquals(luke.getDisplay().size(), 1);
		assertEquals(luke.getDisplayTotal(Token.RED), 4);
		assertEquals(tournament.getPlayerWithHighestDisplay(), luke);
		controller.processPlayerMove(60002, finishTurn);
		
		// Jayson plays a card
		assertEquals(controller.getCurrentTurnId(), 60003);
		
		jayson.addHandCard(controller.getCardFromDeck("r5"));
		playCard = RequestBuilder.buildCardMove("r5");
		controller.processPlayerMove(60003, playCard);
		
		assertEquals(controller.getState(), 3);
		assertEquals(controller.getCurrentTurnId(), 60003);
		assertEquals(jayson.getDisplay().size(), 1);
		assertEquals(jayson.getDisplayTotal(Token.RED), 5);
		assertEquals(tournament.getPlayerWithHighestDisplay(), jayson);
		controller.processPlayerMove(60003, finishTurn);
		
		// Emma plays a card
		assertEquals(controller.getCurrentTurnId(), 60004);
		
		emma.addHandCard(controller.getCardFromDeck("m6"));
		playCard = RequestBuilder.buildCardMove("m6");
		controller.processPlayerMove(60004, playCard);
		
		assertEquals(controller.getState(), 3);
		assertEquals(controller.getCurrentTurnId(), 60004);
		assertEquals(emma.getDisplay().size(), 1);
		assertEquals(emma.getDisplayTotal(Token.RED), 6);
		assertEquals(tournament.getPlayerWithHighestDisplay(), emma);
		controller.processPlayerMove(60004, finishTurn);
		
		assertEquals(controller.getCurrentTurnId(), 60001);
		assertEquals(alexei.getTokens().size(), 0);
	}
	
	/**
	 * All players play several cards
	 */
	@Test
	public void testScenarioD2() {
		alexei.addHandCard(controller.getCardFromDeck("r3"));
		JSONObject playCard = RequestBuilder.buildCardMove("r3");
		controller.processPlayerMove(60001, playCard);
		
		assertEquals(tournament.getToken(), Token.RED);
		assertEquals(controller.getState(), 3);
		assertEquals(controller.getCurrentTurnId(), 60001);
		assertEquals(alexei.getDisplay().size(), 1);
		assertEquals(alexei.getDisplayTotal(Token.RED), 3);
		
		controller.processPlayerMove(60001, finishTurn);
		
		//ÊLuke plays multiple cards
		assertEquals(controller.getCurrentTurnId(), 60002);
		
		luke.addHandCard(controller.getCardFromDeck("r5"));
		luke.addHandCard(controller.getCardFromDeck("r3"));
		playCard = RequestBuilder.buildMultipleCardsMove(new String[] {"r5", "r3"});
		controller.processPlayerMove(60002, playCard);
		
		assertEquals(controller.getState(), 3);
		assertEquals(controller.getCurrentTurnId(), 60002);
		assertEquals(luke.getDisplay().size(), 2);
		assertEquals(luke.getDisplayTotal(Token.RED), 8);
		assertEquals(tournament.getPlayerWithHighestDisplay(), luke);
		controller.processPlayerMove(60002, finishTurn);
		
		// Jayson plays multiple cards
		assertEquals(controller.getCurrentTurnId(), 60003);
		
		jayson.addHandCard(controller.getCardFromDeck("r5"));
		jayson.addHandCard(controller.getCardFromDeck("r3"));
		jayson.addHandCard(controller.getCardFromDeck("r5"));
		playCard = RequestBuilder.buildMultipleCardsMove(new String[] {"r5", "r3", "r5"});
		controller.processPlayerMove(60003, playCard);
		
		assertEquals(controller.getState(), 3);
		assertEquals(controller.getCurrentTurnId(), 60003);
		assertEquals(jayson.getDisplay().size(), 3);
		assertEquals(jayson.getDisplayTotal(Token.RED), 13);
		assertEquals(tournament.getPlayerWithHighestDisplay(), jayson);
		controller.processPlayerMove(60003, finishTurn);
		
		// Emma withdraws
		assertEquals(controller.getCurrentTurnId(), 60004);
		
		emma.addHandCard(controller.getCardFromDeck("r5"));
		emma.addHandCard(controller.getCardFromDeck("r5"));
		emma.addHandCard(controller.getCardFromDeck("r5"));
		playCard = RequestBuilder.buildMultipleCardsMove(new String[] {"r5", "r5", "r5"});
		controller.processPlayerMove(60004, playCard);
		
		assertEquals(controller.getState(), 3);
		assertEquals(controller.getCurrentTurnId(), 60004);
		assertEquals(emma.getDisplay().size(), 3);
		assertEquals(emma.getDisplayTotal(Token.RED), 15);
		assertEquals(tournament.getPlayerWithHighestDisplay(), emma);
		controller.processPlayerMove(60004, finishTurn);
		
		assertEquals(controller.getCurrentTurnId(), 60001);
		assertEquals(alexei.getTokens().size(), 0);
	}
	
	/**
	 * Several players play a single squire
	 */
	@Test
	public void testScenarioE1() {
		alexei.addHandCard(controller.getCardFromDeck("s2"));
		JSONObject playCard = RequestBuilder.buildCardMove("s2");
		controller.processPlayerMove(60001, playCard);
		
		assertEquals(tournament.getToken(), Token.UNDECIDED);
		assertEquals(controller.getState(), 2);
		assertEquals(controller.getCurrentTurnId(), 60001);
		
		JSONObject chooseToken = RequestBuilder.buildChooseToken("red");
		controller.processPlayerMove(60001, chooseToken);
		
		assertEquals(tournament.getToken(), Token.RED);
		assertEquals(controller.getState(), 3);
		assertEquals(controller.getCurrentTurnId(), 60001);
		assertEquals(alexei.getDisplay().size(), 1);
		assertEquals(alexei.getDisplayTotal(Token.RED), 2);
		
		controller.processPlayerMove(60001, finishTurn);
		
		assertEquals(controller.getCurrentTurnId(), 60002);
		
		// Luke plays a squire
		luke.addHandCard(controller.getCardFromDeck("s3"));
		playCard = RequestBuilder.buildCardMove("s3");
		controller.processPlayerMove(60002, playCard);
		
		assertEquals(controller.getState(), 3);
		assertEquals(controller.getCurrentTurnId(), 60002);
		assertEquals(luke.getDisplay().size(), 1);
		assertEquals(luke.getDisplayTotal(Token.RED), 3);
		assertEquals(tournament.getPlayerWithHighestDisplay(), luke);
		controller.processPlayerMove(60002, finishTurn);
		
		// Jayson plays a regular card
		assertEquals(controller.getCurrentTurnId(), 60003);
		
		jayson.addHandCard(controller.getCardFromDeck("r5"));
		playCard = RequestBuilder.buildCardMove("r5");
		controller.processPlayerMove(60003, playCard);
		
		assertEquals(controller.getState(), 3);
		assertEquals(controller.getCurrentTurnId(), 60003);
		assertEquals(jayson.getDisplay().size(), 1);
		assertEquals(jayson.getDisplayTotal(Token.RED), 5);
		assertEquals(tournament.getPlayerWithHighestDisplay(), jayson);
		controller.processPlayerMove(60003, finishTurn);
		
		// Emma withdraws
		assertEquals(controller.getCurrentTurnId(), 60004);
		controller.processPlayerMove(60004, withdraw);
		
		assertEquals(controller.getCurrentTurnId(), 60001);
		assertEquals(alexei.getTokens().size(), 0);
	}
	
	/**
	 * Several players play a single maiden
	 */
	@Test
	public void testScenarioE2() {

		alexei.addHandCard(controller.getCardFromDeck("r3"));
		JSONObject playCard = RequestBuilder.buildCardMove("r3");
		controller.processPlayerMove(60001, playCard);
		
		assertEquals(tournament.getToken(), Token.RED);
		assertEquals(controller.getState(), 3);
		assertEquals(controller.getCurrentTurnId(), 60001);
		assertEquals(alexei.getDisplay().size(), 1);
		assertEquals(alexei.getDisplayTotal(Token.RED), 3);
		
		controller.processPlayerMove(60001, finishTurn);
		
		assertEquals(controller.getCurrentTurnId(), 60002);
		
		// Luke plays a maiden
		luke.addHandCard(controller.getCardFromDeck("m6"));
		playCard = RequestBuilder.buildCardMove("m6");
		controller.processPlayerMove(60002, playCard);
		
		assertEquals(controller.getState(), 3);
		assertEquals(controller.getCurrentTurnId(), 60002);
		assertEquals(luke.getDisplay().size(), 1);
		assertEquals(luke.getDisplayTotal(Token.RED), 6);
		assertEquals(tournament.getPlayerWithHighestDisplay(), luke);
		controller.processPlayerMove(60002, finishTurn);
		
		assertEquals(controller.getCurrentTurnId(), 60003);
		controller.processPlayerMove(60003, withdraw);
		
		assertEquals(controller.getCurrentTurnId(), 60004);
		controller.processPlayerMove(60004, withdraw);
		
		assertEquals(controller.getCurrentTurnId(), 60001);
		assertEquals(alexei.getTokens().size(), 0);
	}
	
	/**
	 * Several players play several supporter cards in ONE round
	 */
	@Test
	public void testScenarioE3() {
		alexei.addHandCard(controller.getCardFromDeck("s2"));
		JSONObject playCard = RequestBuilder.buildCardMove("s2");
		controller.processPlayerMove(60001, playCard);
		
		assertEquals(tournament.getToken(), Token.UNDECIDED);
		assertEquals(controller.getState(), 2);
		assertEquals(controller.getCurrentTurnId(), 60001);
		
		JSONObject chooseToken = RequestBuilder.buildChooseToken("red");
		controller.processPlayerMove(60001, chooseToken);
		
		assertEquals(tournament.getToken(), Token.RED);
		assertEquals(controller.getState(), 3);
		assertEquals(controller.getCurrentTurnId(), 60001);
		assertEquals(alexei.getDisplay().size(), 1);
		assertEquals(alexei.getDisplayTotal(Token.RED), 2);
		
		controller.processPlayerMove(60001, finishTurn);
		
		assertEquals(controller.getCurrentTurnId(), 60002);
		
		//ÊLuke plays multiple cards
		assertEquals(controller.getCurrentTurnId(), 60002);
		
		luke.addHandCard(controller.getCardFromDeck("s2"));
		luke.addHandCard(controller.getCardFromDeck("s3"));
		playCard = RequestBuilder.buildMultipleCardsMove(new String[] {"s2", "s3"});
		controller.processPlayerMove(60002, playCard);
		
		assertEquals(controller.getState(), 3);
		assertEquals(controller.getCurrentTurnId(), 60002);
		assertEquals(luke.getDisplay().size(), 2);
		assertEquals(luke.getDisplayTotal(Token.RED), 5);
		assertEquals(tournament.getPlayerWithHighestDisplay(), luke);
		controller.processPlayerMove(60002, finishTurn);
		
		// Jayson plays multiple cards
		assertEquals(controller.getCurrentTurnId(), 60003);
		
		jayson.addHandCard(controller.getCardFromDeck("s2"));
		jayson.addHandCard(controller.getCardFromDeck("s3"));
		jayson.addHandCard(controller.getCardFromDeck("m6"));
		playCard = RequestBuilder.buildMultipleCardsMove(new String[] {"s2", "s3", "m6"});
		controller.processPlayerMove(60003, playCard);
		
		assertEquals(controller.getState(), 3);
		assertEquals(controller.getCurrentTurnId(), 60003);
		assertEquals(jayson.getDisplay().size(), 3);
		assertEquals(jayson.getDisplayTotal(Token.RED), 11);
		assertEquals(tournament.getPlayerWithHighestDisplay(), jayson);
		controller.processPlayerMove(60003, finishTurn);
		
		// Emma withdraws
		assertEquals(controller.getCurrentTurnId(), 60004);
		controller.processPlayerMove(60004, withdraw);
		
		assertEquals(controller.getCurrentTurnId(), 60001);
		assertEquals(alexei.getTokens().size(), 0);
	}
	
	/**
	 * Use supporter cards over several rounds
	 */
	@Test
	public void testScenarioF() {
		alexei.addHandCard(controller.getCardFromDeck("s2"));
		JSONObject playCard = RequestBuilder.buildCardMove("s2");
		controller.processPlayerMove(60001, playCard);
		
		assertEquals(tournament.getToken(), Token.UNDECIDED);
		assertEquals(controller.getState(), 2);
		assertEquals(controller.getCurrentTurnId(), 60001);
		
		JSONObject chooseToken = RequestBuilder.buildChooseToken("red");
		controller.processPlayerMove(60001, chooseToken);
		
		assertEquals(tournament.getToken(), Token.RED);
		assertEquals(controller.getState(), 3);
		assertEquals(controller.getCurrentTurnId(), 60001);
		assertEquals(alexei.getDisplay().size(), 1);
		assertEquals(alexei.getDisplayTotal(Token.RED), 2);
		
		controller.processPlayerMove(60001, finishTurn);
		
		assertEquals(controller.getCurrentTurnId(), 60002);
		
		//ÊLuke plays multiple cards
		assertEquals(controller.getCurrentTurnId(), 60002);
		
		luke.addHandCard(controller.getCardFromDeck("s2"));
		luke.addHandCard(controller.getCardFromDeck("s3"));
		playCard = RequestBuilder.buildMultipleCardsMove(new String[] {"s2", "s3"});
		controller.processPlayerMove(60002, playCard);
		
		assertEquals(controller.getState(), 3);
		assertEquals(controller.getCurrentTurnId(), 60002);
		assertEquals(luke.getDisplay().size(), 2);
		assertEquals(luke.getDisplayTotal(Token.RED), 5);
		assertEquals(tournament.getPlayerWithHighestDisplay(), luke);
		controller.processPlayerMove(60002, finishTurn);
		
		// Jayson plays multiple cards
		assertEquals(controller.getCurrentTurnId(), 60003);
		
		jayson.addHandCard(controller.getCardFromDeck("s2"));
		jayson.addHandCard(controller.getCardFromDeck("s3"));
		jayson.addHandCard(controller.getCardFromDeck("r3"));
		playCard = RequestBuilder.buildMultipleCardsMove(new String[] {"s2", "s3", "r3"});
		controller.processPlayerMove(60003, playCard);
		
		assertEquals(controller.getState(), 3);
		assertEquals(controller.getCurrentTurnId(), 60003);
		assertEquals(jayson.getDisplay().size(), 3);
		assertEquals(jayson.getDisplayTotal(Token.RED), 8);
		assertEquals(tournament.getPlayerWithHighestDisplay(), jayson);
		controller.processPlayerMove(60003, finishTurn);
		
		// Emma withdraws
		assertEquals(controller.getCurrentTurnId(), 60004);
		controller.processPlayerMove(60004, withdraw);
		
		// Alexei plays several cards
		assertEquals(controller.getCurrentTurnId(), 60001);
		assertEquals(alexei.getTokens().size(), 0);
		
		alexei.addHandCard(controller.getCardFromDeck("m6"));
		alexei.addHandCard(controller.getCardFromDeck("r5"));
		playCard = RequestBuilder.buildMultipleCardsMove(new String[] {"m6", "r5"});
		controller.processPlayerMove(60001, playCard);
		
		assertEquals(controller.getState(), 3);
		assertEquals(controller.getCurrentTurnId(), 60001);
		assertEquals(alexei.getDisplay().size(), 3);
		assertEquals(alexei.getDisplayTotal(Token.RED), 13);
		assertEquals(tournament.getPlayerWithHighestDisplay(), alexei);
		controller.processPlayerMove(60001, finishTurn);
		
		// Luke plays several cards
		assertEquals(controller.getCurrentTurnId(), 60002);
		
		luke.addHandCard(controller.getCardFromDeck("m6"));
		luke.addHandCard(controller.getCardFromDeck("s3"));
		playCard = RequestBuilder.buildMultipleCardsMove(new String[] {"m6", "s3"});
		controller.processPlayerMove(60002, playCard);
		
		assertEquals(controller.getState(), 3);
		assertEquals(controller.getCurrentTurnId(), 60002);
		assertEquals(luke.getDisplay().size(), 4);
		assertEquals(luke.getDisplayTotal(Token.RED), 14);
		assertEquals(tournament.getPlayerWithHighestDisplay(), luke);
		controller.processPlayerMove(60002, finishTurn);
		
		// jayson plays several cards
		assertEquals(controller.getCurrentTurnId(), 60003);
		
		jayson.addHandCard(controller.getCardFromDeck("s2"));
		jayson.addHandCard(controller.getCardFromDeck("m6"));
		jayson.addHandCard(controller.getCardFromDeck("r3"));
		playCard = RequestBuilder.buildMultipleCardsMove(new String[] {"s2", "m6", "r3"});
		controller.processPlayerMove(60003, playCard);
		
		assertEquals(controller.getState(), 3);
		assertEquals(controller.getCurrentTurnId(), 60003);
		assertEquals(jayson.getDisplay().size(), 6);
		assertEquals(jayson.getDisplayTotal(Token.RED), 19);
		assertEquals(tournament.getPlayerWithHighestDisplay(), jayson);
		controller.processPlayerMove(60003, finishTurn);
		
		assertEquals(controller.getCurrentTurnId(), 60001);
		assertEquals(alexei.getTokens().size(), 0);
	}
	
	/**
	 * Try to play cards where the value is too small
	 */
	@Test
	public void testScenarioG() {
		alexei.addHandCard(controller.getCardFromDeck("r3"));
		JSONObject playCard = RequestBuilder.buildCardMove("r3");
		controller.processPlayerMove(60001, playCard);
		
		assertEquals(tournament.getToken(), Token.RED);
		assertEquals(controller.getState(), 3);
		assertEquals(controller.getCurrentTurnId(), 60001);
		assertEquals(alexei.getDisplay().size(), 1);
		assertEquals(alexei.getDisplayTotal(Token.RED), 3);
		
		controller.processPlayerMove(60001, finishTurn);
		
		assertEquals(controller.getCurrentTurnId(), 60002);
		
		// Luke tries to play a card that is too small
		luke.addHandCard(controller.getCardFromDeck("r3"));
		playCard = RequestBuilder.buildCardMove("r3");
		controller.processPlayerMove(60002, playCard);
		
		// check that it wasn't successful
		assertEquals(controller.getState(), 3);
		assertEquals(controller.getCurrentTurnId(), 60002);
		assertEquals(luke.getDisplay().size(), 0);
		assertEquals(luke.getDisplayTotal(Token.RED), 0);
		assertEquals(tournament.getPlayerWithHighestDisplay(), alexei);
		controller.processPlayerMove(60002, finishTurn);
	}
	
	/**
	 * Checks that a player can only play 1 maiden per tournament
	 */
	@Test
	public void testScenarioH() {
		alexei.addHandCard(controller.getCardFromDeck("r3"));
		JSONObject playCard = RequestBuilder.buildCardMove("r3");
		controller.processPlayerMove(60001, playCard);
		
		assertEquals(tournament.getToken(), Token.RED);
		assertEquals(controller.getState(), 3);
		assertEquals(controller.getCurrentTurnId(), 60001);
		assertEquals(alexei.getDisplay().size(), 1);
		assertEquals(alexei.getDisplayTotal(Token.RED), 3);
		
		controller.processPlayerMove(60001, finishTurn);
		
		assertEquals(controller.getCurrentTurnId(), 60002);
		
		// Luke plays a maiden
		luke.addHandCard(controller.getCardFromDeck("m6"));
		playCard = RequestBuilder.buildCardMove("m6");
		controller.processPlayerMove(60002, playCard);
		
		assertEquals(controller.getState(), 3);
		assertEquals(controller.getCurrentTurnId(), 60002);
		assertEquals(luke.getDisplay().size(), 1);
		assertEquals(luke.getDisplayTotal(Token.RED), 6);
		assertEquals(tournament.getPlayerWithHighestDisplay(), luke);
		
		// Luke tries to play another maiden
		luke.addHandCard(controller.getCardFromDeck("m6"));
		playCard = RequestBuilder.buildCardMove("m6");
		controller.processPlayerMove(60002, playCard);
		
		// check that it wasn't successful
		assertEquals(controller.getState(), 3);
		assertEquals(controller.getCurrentTurnId(), 60002);
		assertEquals(luke.getDisplay().size(), 1);
		assertEquals(luke.getDisplayTotal(Token.RED), 6);
		assertEquals(tournament.getPlayerWithHighestDisplay(), luke);
		
	}
	
	/**
	 * Tests winning a token
	 */
	@Test
	public void testScenarioI() {
		alexei.addHandCard(controller.getCardFromDeck("r5"));
		
		JSONObject playCard = RequestBuilder.buildCardMove("r5");
		controller.processPlayerMove(60001, playCard);
		
		assertEquals(tournament.getToken(), Token.RED);
		assertEquals(controller.getState(), 3);
		assertEquals(controller.getCurrentTurnId(), 60001);
		assertEquals(alexei.getDisplay().size(), 1);
		assertEquals(alexei.getDisplayTotal(Token.RED), 5);
		
		controller.processPlayerMove(60001, finishTurn);
		
		assertEquals(controller.getCurrentTurnId(), 60002);
		controller.processPlayerMove(60002, withdraw);
		
		assertEquals(controller.getCurrentTurnId(), 60003);
		controller.processPlayerMove(60003, withdraw);
		
		assertEquals(controller.getCurrentTurnId(), 60004);
		controller.processPlayerMove(60004, withdraw);
		
		assertEquals(controller.getCurrentTurnId(), 60001);
		assertEquals(alexei.getTokens().size(), 1);
		assertTrue(alexei.getTokens().contains(Token.RED));
	}
	
	/**
	 * Tests withdrawing with a maiden and having to forfeit a token
	 */
	@Test
	public void testScenarioK() {
		alexei.addToken(Token.BLUE);
		
		tournament.setToken(Token.RED);
		controller.setState(3);
		
		// alexei plays a maiden
		alexei.addHandCard(controller.getCardFromDeck("m6"));
		JSONObject playCard = RequestBuilder.buildCardMove("m6");
		controller.processPlayerMove(60001, playCard);
		
		assertEquals(controller.getCurrentTurnId(), 60001);
		assertEquals(alexei.getDisplay().size(), 1);
		assertEquals(alexei.getDisplayTotal(Token.RED), 6);
		assertEquals(tournament.getPlayerWithHighestDisplay(), alexei);
		
		// alexei withdraws
		controller.processPlayerMove(60001, withdraw);
		
		// check that Alexei must choose a token to forfeit
		assertEquals(controller.getState(), 4);
		assertEquals(controller.getCurrentTurnId(), 60001);
		
		JSONObject chooseToken = RequestBuilder.buildChooseToken("blue");
		controller.processPlayerMove(60001, chooseToken);
		
		assertEquals(controller.getState(), 3);
		assertEquals(controller.getCurrentTurnId(), 60002);
		assertEquals(alexei.getTokens().size(), 0);
		
	}

}
