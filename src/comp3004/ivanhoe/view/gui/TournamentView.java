package comp3004.ivanhoe.view.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.json.simple.JSONObject;

import comp3004.ivanhoe.util.ClientParser;

@SuppressWarnings("serial")
public class TournamentView extends JPanel {

	private String[] icons = new String[] {"blueknight", "greenknight", "orangeknight", "yellowknight", "blackknight"};
	
	GUIView masterView;
	ClientParser parser;

	JPanel announcements;
	JPanel header;
	
	JPanel handComposite;
	JScrollPane cardsPane;
	
	JPanel statsComposite;
	JScrollPane playersComposite;
	HashMap<Integer, PlayerDisplay> playerDisplays;
	
	JLabel tournamentColor;
	JLabel highestDisplayTotal;
	JLabel currentTurn;

	public TournamentView(GUIView masterView, JSONObject snapshot) {
		this.masterView = masterView;
		this.parser = new ClientParser();
		this.playerDisplays = new HashMap<Integer, PlayerDisplay>();
		
		this.setMinimumSize(new Dimension(800, 600));
		this.setPreferredSize(new Dimension(800, 600));
		
		this.setLayout(new GridBagLayout());

		announcements = new JPanel();
		header = createHeader(parser.getColor(snapshot));
		handComposite = createHandComposite(getPlayerHand(snapshot));
		statsComposite = createStats("We dunno yet", parser.getHighestDisplay(snapshot));
		playersComposite = createPlayersComposite(snapshot);

		GridBagConstraints constraints = new GridBagConstraints();

		// create announcements section
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.weightx = 1;
		constraints.weighty = 0.01;
		constraints.anchor = GridBagConstraints.FIRST_LINE_START;
		constraints.fill = GridBagConstraints.BOTH;
		this.add(header, constraints);
		
		// create header
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.weightx = 1;
		constraints.weighty = 0.04;
		constraints.anchor = GridBagConstraints.FIRST_LINE_START;
		constraints.fill = GridBagConstraints.BOTH;
		this.add(header, constraints);

		// create stats
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.8;
		constraints.weighty = 0.05;
		constraints.anchor = GridBagConstraints.FIRST_LINE_START;
		constraints.fill = GridBagConstraints.BOTH;
		this.add(statsComposite, constraints);

		// create hand composite
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 2;
		constraints.weightx = 0.2;
		constraints.weighty = 0.96;
		constraints.anchor = GridBagConstraints.FIRST_LINE_START;
		constraints.fill = GridBagConstraints.BOTH;
		this.add(handComposite, constraints);

		// create players composite
		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.8;
		constraints.weighty = 0.91;
		constraints.anchor = GridBagConstraints.FIRST_LINE_START;
		constraints.fill = GridBagConstraints.BOTH;
		this.add(playersComposite, constraints);

	}

	/**
	 * Create the header of the tournament view
	 * @param tokenColor
	 * @return
	 */
	private JPanel createHeader(String tokenColor) {
		JPanel header = new JPanel();
		
		header.setBorder(BorderFactory.createLineBorder(Color.black));
		header.setLayout(new FlowLayout(FlowLayout.CENTER));
		JLabel tournamentText = ImageHandler.loadImage("tournament");

		if (tokenColor == null  || tokenColor.toLowerCase().trim().equals("undecided")) {
			tournamentColor = new JLabel("");
		} else {
			tournamentColor = ImageHandler.loadToken(tokenColor.toLowerCase());
		}
		
		header.add(tournamentText);
		header.add(tournamentColor);
		return header;
	}

	private JPanel createStats(String currentTurn, String highestDisplayTotal) {
		JPanel statsPanel = new JPanel();
		statsPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
		
		this.highestDisplayTotal = new JLabel("HIGHEST DISPLAY: " + highestDisplayTotal);
		this.currentTurn = new JLabel("CURRENT TURN: " + currentTurn);
		
		statsPanel.add(this.highestDisplayTotal);
		statsPanel.add(this.currentTurn);
		return statsPanel;
	}

	/**
	 * Display the cards in the player's hand
	 * @param cards
	 * @return
	 */
	private JPanel createHandComposite(ArrayList<String> cards) {
		JPanel handComposite = new JPanel();
		
		handComposite.setBorder(BorderFactory.createLineBorder(Color.black));
		handComposite.setLayout(new BoxLayout(handComposite, BoxLayout.Y_AXIS));
		
		JLabel text = new JLabel("YOUR HAND:");
		text.setAlignmentX(Component.CENTER_ALIGNMENT);
		handComposite.add(text);
		
		JPanel cardsPanel = new JPanel();
		cardsPanel.setLayout(new BoxLayout(cardsPanel, BoxLayout.Y_AXIS));
		cardsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		System.out.println("Cards: " + cards);
		for (String c: cards) {
			JLabel card = ImageHandler.loadCard(c);
			card.setAlignmentX(Component.CENTER_ALIGNMENT);
			cardsPanel.add(card);
			cardsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		}
		cardsPane = new JScrollPane(cardsPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
	                                            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		cardsPane.setAlignmentX(Component.CENTER_ALIGNMENT);
		handComposite.add(cardsPane);
		
		return handComposite;
	}

	/**
	 * Display each player as well as their displays and tokens
	 * @param snapshot
	 * @return
	 */
	private JScrollPane createPlayersComposite(JSONObject snapshot) {
		JPanel playersComposite = new JPanel();
		playersComposite.setBackground(Color.YELLOW);
		playersComposite.setBorder(BorderFactory.createLineBorder(Color.black));
		playersComposite.setLayout(new BoxLayout(playersComposite, BoxLayout.Y_AXIS));
		
		int x = 0;
		for (Object player: parser.getPlayerList(snapshot)) {
			if (isPlayer(player)) continue;
			PlayerDisplay newDisplay = new PlayerDisplay(player, ImageHandler.loadImage(icons[x]));
			++x;
			playersComposite.add(newDisplay);
			playersComposite.add(Box.createRigidArea(new Dimension(0, 15)));
			playerDisplays.put(newDisplay.getUserId(), newDisplay);
		}
		
		JScrollPane playersScrollPane = new JScrollPane(playersComposite);
		playersScrollPane.setAlignmentX(LEFT_ALIGNMENT);
		
		return playersScrollPane;
	}

	public boolean isPlayer(Object player) {
		if (parser.getPlayerId(player).intValue() == masterView.getId()) return true;
		return false;
	}
	
	// TODO: methods for announcements
	
	// TODO: create update methods for all the composites
	public void updateView(JSONObject snapshot) {
		updateHeader(null);
		updateStats(null, null);
		updateHand(null);
		updatePlayersComposite(null);
	}
	
	public void updateHeader(String tokenColor) {
		header.remove(tournamentColor);
		tournamentColor = ImageHandler.loadToken(tokenColor.toLowerCase());
		header.add(tournamentColor);
	}
	
	public void updateStats(String username, String highestScore) {
		if (username != null) {
			currentTurn.setText("CURRENT TURN: " + username);
		}
		if (highestScore != null) {
			highestDisplayTotal.setText("HIGHEST DISPLAY: " + highestScore);
		}
	}
	
	public void updateHand(ArrayList<String> cards) {
		
		handComposite.remove(cardsPane);
		JPanel cardsPanel = new JPanel();
		cardsPanel.setLayout(new BoxLayout(cardsPanel, BoxLayout.Y_AXIS));
		cardsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		System.out.println("Cards: " + cards);
		for (String c: cards) {
			JLabel card = ImageHandler.loadCard(c);
			card.setAlignmentX(Component.CENTER_ALIGNMENT);
			cardsPanel.add(card);
			cardsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		}
		cardsPane = new JScrollPane(cardsPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
	                                            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		cardsPane.setAlignmentX(Component.CENTER_ALIGNMENT);
		handComposite.add(cardsPane);
		
	}
	
	public void updatePlayersComposite(ArrayList<Object> players) {
		for (Object player : players) {
			int playerId = parser.getPlayerId(player);
			playerDisplays.get(playerId).updateDisplay(player);
		}
	}
	
	public ArrayList<String> getPlayerHand(JSONObject snapshot) {
		for (Object player: parser.getPlayerList(snapshot)) {
			//if (parser.getPlayerId(player).intValue() == masterView.getId()) {
			if (parser.getPlayerId(player).intValue() == 60001) {
				return parser.getPlayerHand(player);
			}
		}
		return null;
	}
	
}
