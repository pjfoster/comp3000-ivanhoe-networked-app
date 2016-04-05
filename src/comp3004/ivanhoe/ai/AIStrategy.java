package comp3004.ivanhoe.ai;

import java.util.HashSet;
import java.util.List;

import org.json.simple.JSONObject;

import comp3004.ivanhoe.model.Card;
import comp3004.ivanhoe.model.ColourCard;
import comp3004.ivanhoe.model.Player;
import comp3004.ivanhoe.model.Token;
import comp3004.ivanhoe.model.Tournament;
import comp3004.ivanhoe.util.RequestBuilder;

public abstract class AIStrategy {
	public abstract JSONObject getFirstMove(Tournament tournament, Player player);
	public abstract JSONObject getMove(Tournament tournament, Player player);
	
	public  JSONObject chooseWinningToken(Player player) {
		if (!player.getTokens().contains(Token.PURPLE)) {
			return RequestBuilder.buildChooseToken("purple");
		}
		
		Token chosenToken = getLeastColor(player.getHand(), player.getTokens());
		if (chosenToken != null) {
			return RequestBuilder.buildChooseToken(chosenToken.toString().toLowerCase());
		}
		
		// otherwise, process of elimination
		if (!player.getTokens().contains(Token.RED)) {
			return RequestBuilder.buildChooseToken("red");
		}
		else if (!player.getTokens().contains(Token.BLUE)) {
			return RequestBuilder.buildChooseToken("blue");
		}
		else if (!player.getTokens().contains(Token.YELLOW)) {
			return RequestBuilder.buildChooseToken("yellow");
		}
		else {
			return RequestBuilder.buildChooseToken("green");
		}
	}
	
	public  JSONObject chooseStartingToken(Player player) {
		Token chosenToken = getMostColor(player.getHand(), player.getTokens());
		if (chosenToken != null) {
			return RequestBuilder.buildChooseToken(chosenToken.toString().toLowerCase());
		}

		// otherwise, process of elimination
		if (!player.getTokens().contains(Token.PURPLE)) {
			return RequestBuilder.buildChooseToken("purple");
		}
		else if (!player.getTokens().contains(Token.RED)) {
			return RequestBuilder.buildChooseToken("red");
		}
		else if (!player.getTokens().contains(Token.BLUE)) {
			return RequestBuilder.buildChooseToken("blue");
		}
		else if (!player.getTokens().contains(Token.YELLOW)) {
			return RequestBuilder.buildChooseToken("yellow");
		}
		else {
			return RequestBuilder.buildChooseToken("green");
		}

	}
	
	public  JSONObject pickOpponent(Tournament tournament, Player player) {
		return null;
	}
	
	public  JSONObject pickOpponentCard(Player player, Player opponent) {
		return null;
	}
	
	private Token getMostColor(List<Card> hand, HashSet<Token> tokens) {
		
		int[] numCards = new int[5];
		Token[] tokenList = new Token[] { Token.RED, Token.BLUE, Token.GREEN, Token.YELLOW, Token.PURPLE};
		
		for (Card c: hand) {
			if (c instanceof ColourCard) {
				if (((ColourCard) c).getColour().equals("red")) { numCards[0] += 1; }
				if (((ColourCard) c).getColour().equals("blue")) { numCards[1] += 1; }
				if (((ColourCard) c).getColour().equals("green")) { numCards[2] += 1; }
				if (((ColourCard) c).getColour().equals("yellow")) { numCards[3] += 1; }
				if (((ColourCard) c).getColour().equals("purple")) { numCards[4] += 1; }
			}
		}
		
		while (true) {
			int maxCards = 0;
			int index = -1;
			for (int i = 0; i < 5; ++i) {
				if (numCards[i] > maxCards) {
					index = i;
					maxCards = numCards[i];
				}
			}
			if (index == -1) { return null; }
			
			numCards[index] = 0;
			if (!tokens.contains(tokenList[index])) {
				return tokenList[index];
			}
		}
		
	}
	
	private Token getLeastColor(List<Card> hand, HashSet<Token> tokens) {
		int[] numCards = new int[5];
		Token[] tokenList = new Token[] { Token.RED, Token.BLUE, Token.GREEN, Token.YELLOW, Token.PURPLE};
		
		for (Card c: hand) {
			if (c instanceof ColourCard) {
				if (((ColourCard) c).getColour().equals("red")) { numCards[0] += 1; }
				if (((ColourCard) c).getColour().equals("blue")) { numCards[1] += 1; }
				if (((ColourCard) c).getColour().equals("green")) { numCards[2] += 1; }
				if (((ColourCard) c).getColour().equals("yellow")) { numCards[3] += 1; }
				if (((ColourCard) c).getColour().equals("purple")) { numCards[4] += 1; }
			}
		}
		
		while (true) {
			int minCards = 20;
			int index = -1;
			for (int i = 0; i < 5; ++i) {
				if (numCards[i] < minCards) {
					index = i;
					minCards = numCards[i];
				}
			}
			if (index == -1) { return null; }
			
			numCards[index] = 0;
			if (!tokens.contains(tokenList[index])) {
				return tokenList[index];
			}
		}
	}
	
}
