package comp3004.ivanhoe.controller;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import comp3004.ivanhoe.model.Card;
import comp3004.ivanhoe.model.Tournament;

public class EndOfDeckTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testEndOfDeck() {
		Tournament tournament = new Tournament();
		
		assertEquals(tournament.getDeck().size(), 110);
		
		for (int i = 0; i < 110; ++i) {
			Card c = tournament.drawCard();
			tournament.addToDiscard(c);
		}
		
		assertEquals(tournament.getDeck().size(), 0);
		assertEquals(tournament.getDiscardPile().size(), 110);
		
		// check that the deck is automatically is automatically remade
		// when a card is drawn
		Card c = tournament.drawCard();
		
		assertEquals(tournament.getDeck().size(), 109);
		assertEquals(tournament.getDiscardPile().size(), 0);
	}

}
