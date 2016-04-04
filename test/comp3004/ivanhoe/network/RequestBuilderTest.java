package comp3004.ivanhoe.network;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import comp3004.ivanhoe.model.Card;
import comp3004.ivanhoe.model.ColourCard;
import comp3004.ivanhoe.model.SupporterCard;
import comp3004.ivanhoe.model.Token;
import comp3004.ivanhoe.model.Tournament;
import comp3004.ivanhoe.util.RequestBuilder;
import comp3004.ivanhoe.util.ServerParser;

public class RequestBuilderTest {

	private JSONParser parser = new JSONParser();
	private Tournament tournament;
	
	@Before
	public void setUp() throws Exception {
		tournament = new Tournament();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testBuildRegisterPlayer() throws ParseException {
		
		String testMoveString = RequestBuilder.buildRegisterPlayer("Alexei").toJSONString();
		JSONObject testMove = (JSONObject)parser.parse(testMoveString);
		
		assertEquals(testMove.size(), 2);
		assertEquals(testMove.get("request_type"), "register_player");
		assertEquals(testMove.get("username"), "Alexei");
		
		// TODO: test weird cases
	}
	
	@Test
	public void testBuildChooseToken() throws ParseException {
		
		String testMoveString = RequestBuilder.buildChooseToken("red").toJSONString();
		JSONObject testMove = (JSONObject)parser.parse(testMoveString);
		
		assertEquals(testMove.size(), 2);
		assertEquals(testMove.get("request_type"), "choose_token");
		assertEquals(testMove.get("token_color"), "red");
		Token t = Token.fromString((String)testMove.get("token_color"));
		assertEquals(t, Token.RED);
		
		testMoveString = RequestBuilder.buildChooseToken("purple").toJSONString();
		testMove = (JSONObject)parser.parse(testMoveString);
		
		assertEquals(testMove.size(), 2);
		assertEquals(testMove.get("request_type"), "choose_token");
		assertEquals(testMove.get("token_color"), "purple");
		t = Token.fromString((String)testMove.get("token_color"));
		assertEquals(t, Token.PURPLE);
		
	}
	
	@Test
	public void testBuildChooseTokenInvalid() throws ParseException {
		String testMoveString = RequestBuilder.buildChooseToken("foo").toJSONString();
		JSONObject testMove = (JSONObject)parser.parse(testMoveString);
		
		assertEquals(testMove.size(), 2);
		assertEquals(testMove.get("request_type"), "choose_token");
		assertEquals(testMove.get("token_color"), "foo");
		Token t = Token.fromString((String)testMove.get("token_color"));
		assertNull(t);
	}
	
	@Test
	public void testBuildCardMove() throws ParseException {
		
		// test some color cards
		String testMoveString = RequestBuilder.buildCardMove("r3").toJSONString();
		JSONObject testMove = (JSONObject)parser.parse(testMoveString);
		
		assertEquals(testMove.size(), 3);
		assertEquals(testMove.get("request_type"), "turn_move"); 
		assertEquals(testMove.get("move_type"), "play_card");
		assertEquals(testMove.get("card_code"), "r3");
		
		ArrayList<Card> cards = ServerParser.getCard(testMove, tournament);
		assertTrue(cards.get(0) instanceof ColourCard);
		assertEquals(cards.get(0).getName(), "red");
		assertEquals(cards.get(0).getValue(), 3);
		
		testMoveString = RequestBuilder.buildCardMove("p5").toJSONString();
		testMove = (JSONObject)parser.parse(testMoveString);
		
		assertEquals(testMove.size(), 3);
		assertEquals(testMove.get("request_type"), "turn_move"); 
		assertEquals(testMove.get("move_type"), "play_card");
		assertEquals(testMove.get("card_code"), "p5");
		
		cards = ServerParser.getCard(testMove, tournament);
		assertTrue(cards.get(0) instanceof ColourCard);
		assertEquals(cards.get(0).getName(), "purple");
		assertEquals(cards.get(0).getValue(), 5);
		
		// test some supporter cards
		testMoveString = RequestBuilder.buildCardMove("s3").toJSONString();
		testMove = (JSONObject)parser.parse(testMoveString);
		
		assertEquals(testMove.size(), 3);
		assertEquals(testMove.get("request_type"), "turn_move"); 
		assertEquals(testMove.get("move_type"), "play_card");
		assertEquals(testMove.get("card_code"), "s3");
		
		cards = ServerParser.getCard(testMove, tournament);
		assertTrue(cards.get(0) instanceof SupporterCard);
		assertEquals(cards.get(0).getName().toLowerCase(), "squire");
		assertEquals(cards.get(0).getValue(), 3);
		
		testMoveString = RequestBuilder.buildCardMove("m6").toJSONString();
		testMove = (JSONObject)parser.parse(testMoveString);
		
		assertEquals(testMove.size(), 3);
		assertEquals(testMove.get("request_type"), "turn_move"); 
		assertEquals(testMove.get("move_type"), "play_card");
		assertEquals(testMove.get("card_code"), "m6");
		
		cards = ServerParser.getCard(testMove, tournament);
		assertTrue(cards.get(0) instanceof SupporterCard);
		assertEquals(cards.get(0).getName().toLowerCase(), "maiden");
		assertEquals(cards.get(0).getValue(), 6);
		
	}
	
	@Test
	public void testBuildWithdrawMove() throws ParseException {
		
		String testMoveString = RequestBuilder.buildWithdrawMove().toJSONString();
		JSONObject testMove = (JSONObject)parser.parse(testMoveString);
		
		assertEquals(testMove.size(), 2);
		assertEquals(testMove.get("request_type"), "turn_move");
		assertEquals(testMove.get("move_type"), "withdraw");
		
	}
	
	@Test
	public void testBuildSelectOpponent() throws ParseException {
		
		String testMoveString = RequestBuilder.buildSelectOpponent("Alexei").toJSONString();
		JSONObject testMove = (JSONObject)parser.parse(testMoveString);
		
		assertEquals(testMove.size(), 2);
		assertEquals(testMove.get("request_type"), "select_opponent");
		assertEquals(testMove.get("opponent_username"), "Alexei");
		
	}
	
	@Test
	public void testBuildPickCard() throws ParseException {
		
		String testMoveString = RequestBuilder.buildPickCard("r3").toJSONString();
		JSONObject testMove = (JSONObject)parser.parse(testMoveString);
		
		assertEquals(testMove.size(), 2);
		assertEquals(testMove.get("request_type"), "pick_card");
		assertEquals(testMove.get("card_code"), "r3");
		
	}

}
