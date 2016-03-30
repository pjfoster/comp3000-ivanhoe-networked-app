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

import comp3004.ivanhoe.util.ClientParser;


@SuppressWarnings("serial")
public class PlayerDisplay extends JPanel {

	private int userId;
	private String username; 
	private ClientParser parser = new ClientParser();
	private ArrayList<String> cards;
	private ArrayList<String> tokens;
	private String displayTotal;
	
	public JPanel userComposite;
	public JPanel displayComposite;
	
	public PlayerDisplay(Object player, JLabel imageIcon) {
		this.setLayout(new FlowLayout());
		this.setOpaque(false);
		
		this.userId = parser.getPlayerId(player);
		this.username = parser.getPlayerName(player);
		this.cards = parser.getPlayerDisplay(player);
		this.tokens = parser.getPlayerTokens(player);
		this.displayTotal = parser.getPlayerDisplayTotal(player);
		
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
		userComposite.setOpaque(false);
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
	public JPanel createDisplayComposite(ArrayList<String> tokens, ArrayList<String> displayCards, String displayTotal) {
		JPanel displayComposite = new JPanel();
		displayComposite.setOpaque(false);
		displayComposite.setLayout(new BoxLayout(displayComposite, BoxLayout.Y_AXIS));
		
		JLabel totalLabel = new JLabel("DISPLAY TOTAL: " + displayTotal);
		totalLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		// Display tokens
		JPanel tokensComposite = new JPanel();
		tokensComposite.setOpaque(false);
		tokensComposite.setSize(350, 25);
		tokensComposite.setAlignmentX(Component.LEFT_ALIGNMENT);
		tokensComposite.setLayout(new FlowLayout(FlowLayout.LEFT));
		for (String t: tokens) {
			JLabel token = ImageHandler.loadToken(t);
			tokensComposite.add(token);
		}
		tokensComposite.add(Box.createHorizontalGlue());
		
		// display cards
		JPanel cardsComposite = new JPanel();
		cardsComposite.setOpaque(false);
		cardsComposite.setLayout(new FlowLayout());
		for (String c: displayCards) {
			JLabel card = ImageHandler.loadCard(c);
			cardsComposite.add(card);
		}
		JScrollPane cardsScrollPane = new JScrollPane(cardsComposite, JScrollPane.VERTICAL_SCROLLBAR_NEVER,
										              JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		cardsScrollPane.setBorder(null);
		cardsScrollPane.setPreferredSize(new Dimension(350, 115));
		cardsScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		displayComposite.add(totalLabel);
		displayComposite.add(tokensComposite);
		displayComposite.add(cardsScrollPane);
		
		return displayComposite;
	}
	
	
	
	
	
}
