package comp3004.ivanhoe.view.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.json.simple.JSONObject;

import comp3004.ivanhoe.util.RequestBuilder;
import comp3004.ivanhoe.util.Strings;

/**
 * Requires user to pick a card and then submit their selection
 * @author PJF
 *
 */
@SuppressWarnings("serial")
public class TurnView extends JFrame implements ActionListener, SelectionView {

	GUIView masterView;
	
	JLabel headerLabel;
	JPanel newCardDisplay;
	JScrollPane cardScrollPane;
	JButton submitButton;
	JButton withdrawButton;
	JButton finishTurnButton;
	
	HashMap<JCheckBoxMenuItem, String> cardsLookup;
	
	public TurnView(GUIView masterView, ArrayList<String> cards) {
		super("It's Your Turn!");
		this.setSize(600, 300);
		this.setResizable(false);
		this.masterView = masterView;
		this.cardsLookup = new HashMap<JCheckBoxMenuItem, String>();
		
		this.addWindowListener(new WindowAdapter() {
	         public void windowClosing(WindowEvent windowEvent){
		        System.exit(0);
	         }        
	     });   
		
		JPanel mainView = new JPanel();
		mainView.setLayout(new BoxLayout(mainView, BoxLayout.Y_AXIS));
		this.add(mainView);
		
		headerLabel = new JLabel(Strings.your_turn_to_play);
		
		JPanel cardSelector = new JPanel();
		cardSelector.setLayout(new FlowLayout());
		
		for (int i = 0; i < cards.size(); ++i) {
			ImageIcon cardIcon = ImageHandler.loadCardIcon(cards.get(i));
			JCheckBoxMenuItem checkBox;
			if (i == cards.size() -1) {
				checkBox = new JCheckBoxMenuItem("NEW", cardIcon);
				checkBox.setBorder(BorderFactory.createLineBorder(Color.RED));
			} else {
				checkBox = new JCheckBoxMenuItem(cardIcon);
			}
			checkBox.setOpaque(false);
			cardSelector.add(checkBox);
			cardsLookup.put(checkBox, cards.get(i));
		}
		cardScrollPane = new JScrollPane(cardSelector, JScrollPane.VERTICAL_SCROLLBAR_NEVER,
	              JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		cardScrollPane.setBorder(null);
		cardScrollPane.setPreferredSize(new Dimension(500, 170));
		cardScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		submitButton = new JButton("Submit");
		submitButton.addActionListener(this);
		
		withdrawButton = new JButton("Withdraw");
		withdrawButton.addActionListener(this);
		
		finishTurnButton = new JButton("Finish Turn");
		finishTurnButton.addActionListener(this);
		
		mainView.add(headerLabel);
		mainView.add(cardScrollPane);
		mainView.add(submitButton);
		mainView.add(withdrawButton);
		mainView.add(finishTurnButton);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == submitButton) {
			
			// check the number of cards selected
			ArrayList<String> selectedCards = new ArrayList<String>();
			for (JCheckBoxMenuItem checkBox: cardsLookup.keySet()) {
				if (checkBox.getState()) {
					selectedCards.add(cardsLookup.get(checkBox));
				}
			}
			
			if (selectedCards.size() == 1) {
				JSONObject cardMove = RequestBuilder.buildCardMove(selectedCards.get(0));
				masterView.handleEvent(cardMove);
				exit();
			} else if (selectedCards.size() > 1) {
				JSONObject cardsMove = RequestBuilder.buildMultipleCardsMove(selectedCards);
				masterView.handleEvent(cardsMove);
				exit();
			}
			
		} else if (e.getSource() == withdrawButton) {
			JSONObject withdraw = RequestBuilder.buildWithdrawMove();
			masterView.handleEvent(withdraw);
			exit();
			
		} else if (e.getSource() == finishTurnButton) {
			JSONObject finishTurn = RequestBuilder.buildFinishTurn();
			masterView.handleEvent(finishTurn);
			exit();
		}
		
	}
	
	@Override
	public void indicateInvalid() {
		headerLabel.setForeground(Color.RED);
		headerLabel.setText(Strings.invalid_turn);
	}
	
	public void exit() {
		this.setVisible(false);
		//this.dispose();
	}
	
}
