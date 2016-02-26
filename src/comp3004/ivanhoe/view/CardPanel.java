package comp3004.ivanhoe.view;

import java.util.ArrayList;

import javax.swing.JPanel;

public class CardPanel extends JPanel {
	private ArrayList<CardButton> cards;
	
	
	public CardPanel(){
		super();
	}
	
	public void addCard(String code){
		CardButton card = new CardButton(code);
		cards.add(card);
		add(card);
	}
	
	public Boolean removeCard(String code){
		for(CardButton c: cards){
			if(c.getCode() == code){
				cards.remove(c);
				c.setVisible(false);
			}
		}
		return false;
	}
	
	
}
