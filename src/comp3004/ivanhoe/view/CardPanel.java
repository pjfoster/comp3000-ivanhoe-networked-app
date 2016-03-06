package comp3004.ivanhoe.view;

import java.util.ArrayList;

import javax.swing.JPanel;

public class CardPanel extends JPanel {
	private ArrayList<CardButton> cards;
	private TournamentDisplay display;
	private String alignment;
	
	
	public CardPanel(TournamentDisplay display,ArrayList<String> cards, String alignment, int x, int y, int width, int height){
		super();
		this.display = display;
		this.alignment = alignment;
		setBounds(x,y,width,height);
		refresh(cards);
	}
	
	/** obsolete
	public void addCard(String name){
		CardButton card = new CardButton(name);
		cards.add(card);
		add(card);
	}
	
	public Boolean removeCard(String name){
		for(CardButton c: cards){
			if(c.getName() == name){
				cards.remove(c);
				c.setVisible(false);
			}
		}
		return false;
	}
	*/
	
	public void refresh(ArrayList<String> cardNames){
		int i;
		for(i = 0; i<cards.size(); i++){
			if(cards.get(i).getName()!=cardNames.get(i)){
				cards.set(i, new CardButton(cardNames.get(i)));
			}
		}
		if(cardNames.size()>i){
			for(i=i; i<cardNames.size();i++){
				cards.add(i, new CardButton(cardNames.get(i)));
			}
		}
	}
}
