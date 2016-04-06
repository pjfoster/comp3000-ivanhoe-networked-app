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

@SuppressWarnings("serial")
public class PickCardWindow extends JFrame implements ActionListener, SelectionView {

	GUIView masterView;

	JLabel headerLabel;
	JScrollPane cardScrollPane;
	JButton submitButton;

	HashMap<JCheckBoxMenuItem, String> cardsLookup;

	public PickCardWindow(GUIView masterView, ArrayList<String> cards, String message) {
		super("Pick Card");
		this.setSize(600, 300);
		this.setResizable(false);
		this.masterView = masterView;
		this.cardsLookup = new HashMap<JCheckBoxMenuItem, String>();

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent){
				windowEvent.getWindow().setVisible(false);
			}        
		});   

		JPanel mainView = new JPanel();
		mainView.setLayout(new BoxLayout(mainView, BoxLayout.Y_AXIS));
		this.add(mainView);

		headerLabel = new JLabel(message);

		JPanel cardSelector = new JPanel();
		cardSelector.setLayout(new FlowLayout());

		for (int i = 0; i < cards.size(); ++i) {
			ImageIcon cardIcon = ImageHandler.loadCardIcon(cards.get(i));
			JCheckBoxMenuItem checkBox = new JCheckBoxMenuItem(cardIcon);
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

		mainView.add(headerLabel);
		mainView.add(cardScrollPane);
		mainView.add(submitButton);

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		// check the number of cards selected
		ArrayList<String> selectedCards = new ArrayList<String>();
		for (JCheckBoxMenuItem checkBox: cardsLookup.keySet()) {
			if (checkBox.getState()) {
				selectedCards.add(cardsLookup.get(checkBox));
			}
		}

		if (selectedCards.size() == 1) {
			JSONObject cardMove = RequestBuilder.buildPickCard(selectedCards.get(0));
			masterView.handleEvent(cardMove);
			exit();
		} else if (selectedCards.size() > 1) {
			indicateInvalid();
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
