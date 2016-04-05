package comp3004.ivanhoe.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.simple.JSONObject;

import comp3004.ivanhoe.model.Card;
import comp3004.ivanhoe.model.CardComparator;
import comp3004.ivanhoe.model.ColourCard;
import comp3004.ivanhoe.model.Player;
import comp3004.ivanhoe.model.SupporterCard;
import comp3004.ivanhoe.model.Token;
import comp3004.ivanhoe.model.Tournament;
import comp3004.ivanhoe.util.RequestBuilder;

public class PassiveAIStrategy extends AIStrategy {

	@Override
	public JSONObject getMove(Tournament tournament, Player player) {
		
		int highestDisplay = tournament.getHighestDisplayTotal();
		Token token = tournament.getToken();
		List<Card> myHand = player.getHand();
		
		// Is there an action card that would benefit me?
		
		// get the max I can play with the cards in my hand
		int maxBonus = 0;
		ArrayList<Card> validCards = new ArrayList<Card>();
		for (Card c: myHand) {
			if (c instanceof ColourCard) {
				if (((ColourCard) c).getColour().equals(token)) {
					maxBonus += c.getValue();
					validCards.add(c);
				}
			}
			else if (c instanceof SupporterCard) {
				if (!c.getName().equals("m6") || !player.hasPlayedMaiden()) {
					if (!player.hasPlayedMaiden()) {
						maxBonus += c.getValue();
						validCards.add(c);
					}
				}
			}
		}
		
		if (player.getDisplayTotal(token) + maxBonus <= highestDisplay) {
			// withdraw
			return RequestBuilder.buildWithdrawMove();
		}
		
		// otherwise, calculate the smallest number of cards that can be played
		ArrayList<String> selectedCards = new ArrayList<String>();
		int total = 0;
		
		while (total + player.getDisplayTotal(token) < highestDisplay) {
			Card minCard = Collections.min(validCards, new CardComparator());
			validCards.remove(minCard);
			total += minCard.getValue();
			selectedCards.add(minCard.toString());
		}
		
		return RequestBuilder.buildMultipleCardsMove(selectedCards);
	}

	@Override
	public JSONObject getFirstMove(Tournament tournament, Player player) {
		List<Card> myHand = player.getHand();
		
		ArrayList<Card> validCards = new ArrayList<Card>();
		
		// if you have less than 4 tokens, play a color card
		if (player.getTokens().size() <= 3) {
			for (Card c: myHand) {
				if (c instanceof ColourCard) {
					String colour = ((ColourCard)c).getColour();
					if (!player.getTokens().contains(Token.fromString(colour))) {
						validCards.add(c);
					}
				}
			}
			if (validCards.isEmpty()) {
				for (Card c: myHand) {
					if (c instanceof ColourCard || c instanceof SupporterCard) {
						validCards.add(c);
					}
				}
			}
		}
		// more than 3 tokens, play a supporter card and select tournament color
		else {
			for (Card c: myHand) {
				if (c instanceof SupporterCard) {
					validCards.add(c);
				}
			}
			if (validCards.isEmpty()) {
				for (Card c: myHand) {
					if (c instanceof ColourCard) {
						validCards.add(c);
					}
				}
			}
		}
		
		if (validCards.isEmpty()) {
			return RequestBuilder.buildWithdrawMove();
		}
		
		else {
			Card minCard = Collections.min(validCards, new CardComparator());
			return RequestBuilder.buildCardMove(minCard.toString());
		}
		
	}


}
