package comp3004.ivanhoe.view.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.json.simple.JSONObject;

import comp3004.ivanhoe.util.ClientParser;

@SuppressWarnings("serial")
public class TournamentView extends JPanel {

	private String[] icons = new String[] {"blueknight", "greenknight", "orangeknight", "yellowknight", "blackknight"};
	
	GUIView masterView;
	JPanel header;
	JLabel tournamentColor;
	JPanel handComposite;
	JScrollPane cardsPane;
	
	JScrollPane playersScrollPane;
	JPanel playersComposite;
	HashMap<Integer, PlayerDisplay> playerDisplays;
	
	// elements of stats composite
	JPanel statsComposite;
	JLabel highestDisplayTotal;
	JLabel currentTurn;
	JLabel announcement;

	ArrayList<String> currentHand;
	JFrame lastMove;
	
	public TournamentView(GUIView masterView, JSONObject snapshot) {
		this.masterView = masterView;
		this.playerDisplays = new HashMap<Integer, PlayerDisplay>();
		this.lastMove = null;
		
		this.setMinimumSize(new Dimension(800, 600));
		this.setPreferredSize(new Dimension(800, 600));
		
		this.setLayout(new GridBagLayout());
		
		header = createHeader(ClientParser.getColorFromSnapshot(snapshot));
		handComposite = createHandComposite(getPlayerHand(snapshot));
		statsComposite = createStats("", ClientParser.getHighestDisplayFromSnapshot(snapshot));
		playersScrollPane = createPlayersComposite(snapshot);

		GridBagConstraints constraints = new GridBagConstraints();
		
		// create header
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.weightx = 1;
		constraints.weighty = 0.06;
		constraints.anchor = GridBagConstraints.FIRST_LINE_START;
		constraints.fill = GridBagConstraints.BOTH;
		this.add(header, constraints);

		// create stats
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.8;
		constraints.weighty = 0.05;
		constraints.anchor = GridBagConstraints.FIRST_LINE_START;
		constraints.fill = GridBagConstraints.BOTH;
		this.add(statsComposite, constraints);

		// create hand composite
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 2;
		constraints.weightx = 0.2;
		constraints.weighty = 0.96;
		constraints.anchor = GridBagConstraints.FIRST_LINE_START;
		constraints.fill = GridBagConstraints.BOTH;
		this.add(handComposite, constraints);

		// create players composite
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.8;
		constraints.weighty = 0.91;
		constraints.anchor = GridBagConstraints.FIRST_LINE_START;
		constraints.fill = GridBagConstraints.BOTH;
		this.add(playersScrollPane, constraints);

	}

	/**
	 * Create the header of the tournament view
	 * @param tokenColor
	 * @return
	 */
	private JPanel createHeader(String tokenColor) {
		JPanel header = new JPanel();
		header.setOpaque(false);
		//header.setBackground(new Color(153, 0, 0));
		
		//header.setBorder(BorderFactory.createLineBorder(Color.black));
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
		//statsPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
		statsPanel.setOpaque(false);
		
		this.highestDisplayTotal = new JLabel("HIGHEST DISPLAY: " + highestDisplayTotal);
		this.currentTurn = new JLabel("CURRENT TURN: " + currentTurn);
		this.announcement = new JLabel("");
		
		statsPanel.add(this.highestDisplayTotal);
		statsPanel.add(this.currentTurn);
		statsPanel.add(this.announcement);
		return statsPanel;
	}

	/**
	 * Display the cards in the player's hand
	 * @param cards
	 * @return
	 */
	private JPanel createHandComposite(ArrayList<String> cards) {
		JPanel handComposite = new JPanel();
		
		//handComposite.setBorder(BorderFactory.createLineBorder(Color.black));
		handComposite.setLayout(new BoxLayout(handComposite, BoxLayout.Y_AXIS));
		handComposite.setOpaque(false);
		
		JLabel text = new JLabel("YOUR HAND:");
		text.setAlignmentX(Component.CENTER_ALIGNMENT);
		handComposite.add(text);
		handComposite.add(Box.createRigidArea(new Dimension(0, 15)));
		
		JPanel cardsPanel = new JPanel();
		cardsPanel.setOpaque(false);
		cardsPanel.setLayout(new BoxLayout(cardsPanel, BoxLayout.Y_AXIS));
		cardsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		for (String c: cards) {
			JLabel card = new JLabel(ImageHandler.loadCardIcon(c));
			card.setAlignmentX(Component.CENTER_ALIGNMENT);
			cardsPanel.add(card);
			cardsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		}
		cardsPane = new JScrollPane(cardsPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
	                                            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		cardsPane.setOpaque(false);
		cardsPane.getViewport().setOpaque(false);
		cardsPane.setBorder(null);
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
		playersComposite = new JPanel();
		//playersComposite.setBackground(Color.YELLOW);
		//playersComposite.setBorder(BorderFactory.createLineBorder(Color.black));
		playersComposite.setLayout(new BoxLayout(playersComposite, BoxLayout.Y_AXIS));
		playersComposite.setOpaque(false);
		
		int x = 0;
		for (Object player: ClientParser.getPlayerList(snapshot)) {
			//if (isPlayer(player)) continue;
			PlayerDisplay newDisplay = new PlayerDisplay(player, ImageHandler.loadImage(icons[x]));
			++x;
			playersComposite.add(newDisplay);
			playersComposite.add(Box.createRigidArea(new Dimension(0, 15)));
			playerDisplays.put(newDisplay.getUserId(), newDisplay);
		}
		
		JScrollPane playersScrollPane = new JScrollPane(playersComposite);
		playersScrollPane.setAlignmentX(LEFT_ALIGNMENT);
		playersScrollPane.setOpaque(false);
		playersScrollPane.setBorder(null);
		playersScrollPane.getViewport().setBorder(null);
		playersScrollPane.getViewport().setOpaque(false);
		
		return playersScrollPane;
	}

	public boolean isPlayer(Object player) {
		if (ClientParser.getPlayerId(player).intValue() == masterView.getId()) return true;
		return false;
	}
	
	/**
	 * Updates the tournament view based on a tournament snapshot
	 * @param snapshot
	 */
	public void updateView(JSONObject snapshot) {
		updateHeader(ClientParser.getColorFromSnapshot(snapshot));
		updateStats(null, ClientParser.getHighestDisplayFromSnapshot(snapshot), null);
		
		for (Object p: ClientParser.getPlayerList(snapshot)) {
			if (ClientParser.getPlayerId(p).intValue() == masterView.getId()) {
				updateHand(ClientParser.getPlayerHand(p));
				break;
			}
		}
		
		updatePlayersComposite(ClientParser.getPlayerList(snapshot));
	}
	
	public void updateHeader(String tokenColor) {
		header.remove(tournamentColor);
		if (tokenColor != null && !tokenColor.toLowerCase().trim().equals("undecided")) {
			tournamentColor = ImageHandler.loadToken(tokenColor.toLowerCase());
		}
		header.add(tournamentColor);
		
		if (tokenColor.equals("BLUE")) {
			this.setBackground(new Color(51, 153, 255));
		} else if (tokenColor.equals("RED")) {
			this.setBackground(new Color(255, 102, 102));
		} else if (tokenColor.equals("YELLOW")) {
			this.setBackground(new Color(255, 255, 0));
		} else if (tokenColor.equals("PURPLE")) {
			this.setBackground(new Color(153, 51, 255));
		} else if (tokenColor.equals("GREEN")) {
			this.setBackground(new Color(102, 204, 0));
		}
	}
	
	public void updateStats(String username, String highestScore, String message) {
		if (username != null) {
			currentTurn.setText("CURRENT TURN: " + username);
		}
		if (highestScore != null) {
			highestDisplayTotal.setText("HIGHEST DISPLAY: " + highestScore);
		}
		if (message != null) {
			announcement.setText(message);
		}
	}
	
	public void updateHand(ArrayList<String> cards) {
		
		currentHand = cards;
		
		handComposite.remove(cardsPane);
		JPanel cardsPanel = new JPanel();
		cardsPanel.setOpaque(false);
		cardsPanel.setLayout(new BoxLayout(cardsPanel, BoxLayout.Y_AXIS));
		cardsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		for (String c: cards) {
			JLabel card = new JLabel(ImageHandler.loadCardIcon(c));
			card.setAlignmentX(Component.CENTER_ALIGNMENT);
			cardsPanel.add(card);
			cardsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		}
		cardsPane = new JScrollPane(cardsPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
	                                            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		cardsPane.setBorder(null);
		cardsPane.setAlignmentX(Component.CENTER_ALIGNMENT);
		cardsPane.setOpaque(false);
		cardsPane.getViewport().setOpaque(false);
		handComposite.add(cardsPane);
		
	}
	
	public void updatePlayersComposite(ArrayList<Object> players) {
		//System.out.println("Updating players composite!");
		for (Object player : players) {
			int playerId = ClientParser.getPlayerId(player).intValue();
			//if (playerId == masterView.getId()) continue;
			playerDisplays.get(playerId).updateDisplay(player);
		}
	}
	
	public ArrayList<String> getPlayerHand(JSONObject snapshot) {
		for (Object player: ClientParser.getPlayerList(snapshot)) {
			if (ClientParser.getPlayerId(player).intValue() == masterView.getId()) {
				this.currentHand = ClientParser.getPlayerHand(player);
				return ClientParser.getPlayerHand(player);
			}
		}
		return null;
	}
	
	public void displayPlayerTurn(String newCard) {
		TurnView turnView = new TurnView(masterView, currentHand);
		lastMove = turnView;
		turnView.setVisible(true);
	}
	
	public void displaySelectOpponent() {
		PickOpponentWindow oppView = new PickOpponentWindow(masterView, playerDisplays.values());
		lastMove = oppView;
		oppView.setVisible(true);
	}
	
	public void displayInvalidMove() {
		((SelectionView)lastMove).indicateInvalid();
		lastMove.setVisible(true);
	}
	
	public void withdrawPlayer(int playerId) {
		PlayerDisplay pd = playerDisplays.get(playerId);
		pd.withdraw();
	}
	
	public void setLastMove(JFrame lastMove) {
		this.lastMove = lastMove;
	}
	
}
