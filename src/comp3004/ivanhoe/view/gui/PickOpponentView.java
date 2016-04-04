package comp3004.ivanhoe.view.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collection;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

import org.json.simple.JSONObject;

import comp3004.ivanhoe.util.ClientRequestBuilder;

@SuppressWarnings("serial")
public class PickOpponentView extends JFrame implements ActionListener {

	GUIView masterView;
	JButton submitButton;
	ButtonGroup btnGroup;
	
	public PickOpponentView(GUIView masterView, Collection<PlayerDisplay> playerDisplays) {
		super("Pick an opponent");
		this.setSize(600, 300);
		this.setResizable(false);
		this.masterView = masterView;
		
		this.addWindowListener(new WindowAdapter() {
	         public void windowClosing(WindowEvent windowEvent){
		        System.exit(0);
	         }        
	     });   
		
		JPanel mainView = new JPanel();
		mainView.setLayout(new BoxLayout(mainView, BoxLayout.Y_AXIS));
		this.add(mainView);
		
		JLabel l1 = new JLabel("Pick an opponent:");
				
		JPanel opponentSelector = new JPanel();
		opponentSelector.setLayout(new GridLayout(2, playerDisplays.size() - 1));
		 
		for (PlayerDisplay pd: playerDisplays) {
			opponentSelector.add(pd.getIcon());
		}
		
		btnGroup = new ButtonGroup();
		for (PlayerDisplay pd: playerDisplays) {
			JRadioButton radioButton = new JRadioButton(pd.getName());
			radioButton.setActionCommand("" + pd.getUserId());
			radioButton.setOpaque(false);
			btnGroup.add(radioButton);
			opponentSelector.add(radioButton);
		}
		
		JScrollPane opponentScrollPane = new JScrollPane(opponentSelector, JScrollPane.VERTICAL_SCROLLBAR_NEVER,
	              JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		opponentScrollPane.setBorder(null);
		opponentScrollPane.setPreferredSize(new Dimension(500, 170));
		opponentScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		submitButton = new JButton("Submit");
		submitButton.addActionListener(this);
		
		mainView.add(l1);
		mainView.add(Box.createRigidArea(new Dimension(0, 15)));
		mainView.add(opponentScrollPane);
		mainView.add(submitButton);
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (btnGroup.getSelection() == null) return;
		
		String opponentId = btnGroup.getSelection().getActionCommand();
		JSONObject pickOpponent = ClientRequestBuilder.buildSelectOpponent(opponentId);
		masterView.handleEvent(pickOpponent);
		this.setVisible(false);
		this.dispose();
		
	}

}
