package comp3004.ivanhoe.view;

import java.util.ArrayList;

import javax.swing.*;

public class DrawPile extends JPanel {

	private TournamentDisplay display;
	
	private JButton deck, discard;
	
	public DrawPile(TournamentDisplay display,ArrayList<String> cards, int x, int y, int width, int height){
		super();
		this.display = display;
		setBounds(x,y,width,height);
		refresh(cards);
	}
	
	public void refresh(ArrayList<String> cardNames){
		
	}
	
	
}