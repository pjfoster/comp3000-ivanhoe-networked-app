package comp3004.ivanhoe.model;

import java.util.Comparator;

public class CardComparator implements Comparator<Card> {

	@Override
	public int compare(Card c1, Card c2) {
		if (c1.getValue() > c2.getValue()) return 1;
		else if (c1.getValue() == c2.getValue()) return 0;
		else return -1;
	}

}
