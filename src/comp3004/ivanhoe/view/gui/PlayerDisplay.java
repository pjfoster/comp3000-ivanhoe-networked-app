package comp3004.ivanhoe.view.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import org.apache.commons.lang3.StringUtils;


@SuppressWarnings("serial")
public class PlayerDisplay extends JPanel {

	private int userId;
	private String username; 
	private ArrayList<String> cards;
	private ArrayList<String> tokens;
	private int displayTotal;
	
	public JPanel userComposite;
	public JPanel displayComposite;
	
	public PlayerDisplay(JLabel imageIcon, int userId, String username, ArrayList<String> tokens,
			             ArrayList<String> displayCards, int displayTotal) {
		this.setLayout(new FlowLayout());
		
		this.userId = userId;
		this.username = username;
		this.cards = displayCards;
		this.tokens = tokens;
		this.displayTotal = displayTotal;
		
		this.userComposite = createUserComposite(this.username, imageIcon);
		this.displayComposite = createDisplayComposite(this.tokens, this.cards, this.displayTotal);
		
		this.add(userComposite);
		this.add(Box.createRigidArea(new Dimension(10,0)));
		this.add(displayComposite);
		
	}
	
	/**
	 * Create the userComposite, which displays a player's username and an icon
	 * @param username
	 * @param imageIcon
	 * @return
	 */
	public JPanel createUserComposite(String username, JLabel imageIcon) {
		JPanel userComposite = new JPanel();
		//userComposite.setBorder(BorderFactory.createLineBorder(Color.black));
		userComposite.setLayout(new BoxLayout(userComposite, BoxLayout.Y_AXIS));
		
		imageIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
		userComposite.add(imageIcon);
		userComposite.add(Box.createRigidArea(new Dimension(0,10)));
		
		JLabel usernameLabel = new JLabel(username.toUpperCase());
		usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		userComposite.add(usernameLabel);
		
		return userComposite;
	}
	
	/**
	 * Creat the displayComposite, which includes the user's display, displayTotal, and tokens
	 * @param tokens
	 * @param displayCards
	 * @param displayTotal
	 * @return
	 */
	public JPanel createDisplayComposite(ArrayList<String> tokens, ArrayList<String> displayCards, int displayTotal) {
		JPanel displayComposite = new JPanel();
		displayComposite.setLayout(new BoxLayout(displayComposite, BoxLayout.Y_AXIS));
		
		JLabel totalLabel = new JLabel("DISPLAY TOTAL: " + displayTotal);
		totalLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		// Display tokens
		JPanel tokensComposite = new JPanel();
		tokensComposite.setSize(250, 25);
		tokensComposite.setAlignmentX(Component.LEFT_ALIGNMENT);
		tokensComposite.setLayout(new FlowLayout(FlowLayout.LEFT));
		for (String t: tokens) {
			JLabel token = ImageHandler.loadToken(t);
			tokensComposite.add(token);
		}
		tokensComposite.add(Box.createHorizontalGlue());
		
		// display cards
		JPanel cardsComposite = new JPanel();
		cardsComposite.setLayout(new FlowLayout());
		for (String c: displayCards) {
			JLabel card = ImageHandler.loadCard(c);
			cardsComposite.add(card);
		}
		JScrollPane cardsScrollPane = new JScrollPane(cardsComposite, JScrollPane.VERTICAL_SCROLLBAR_NEVER,
										              JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		cardsScrollPane.setBorder(null);
		cardsScrollPane.setPreferredSize(new Dimension(250, 115));
		cardsScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		displayComposite.add(totalLabel);
		displayComposite.add(tokensComposite);
		displayComposite.add(cardsScrollPane);
		
		return displayComposite;
	}
	
	
	
	
	
}
