package comp3004.ivanhoe.view.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

import org.json.simple.JSONObject;

import comp3004.ivanhoe.util.RequestBuilder;
import comp3004.ivanhoe.util.Strings;

@SuppressWarnings("serial")
public class PickColourWindow extends JFrame implements ActionListener, SelectionView {

	JLabel headerLabel;
	GUIView masterView;
	JButton submitButton;
	ButtonGroup btnGroup;
	
	public PickColourWindow(GUIView masterView, ArrayList<String> tokenColours, String message) {
		super("Pick a Colour");
		this.setSize(600, 200);
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
		
		headerLabel = new JLabel(message);
				
		JPanel colourSelector = new JPanel();
		colourSelector.setLayout(new GridLayout(2, tokenColours.size()));
		 
		for (String token: tokenColours) {
			ImageIcon tokenIcon = ImageHandler.loadTokenIcon(token);
			colourSelector.add(new JLabel(tokenIcon));
		}
		
		btnGroup = new ButtonGroup();
		for (String token: tokenColours) {
			JRadioButton radioButton = new JRadioButton(token);
			radioButton.setActionCommand(token);
			radioButton.setOpaque(false);
			btnGroup.add(radioButton);
			colourSelector.add(radioButton);
		}
		
		JScrollPane colourScrollPane = new JScrollPane(colourSelector, JScrollPane.VERTICAL_SCROLLBAR_NEVER,
	              JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		colourScrollPane.setBorder(null);
		colourScrollPane.setPreferredSize(new Dimension(500, 170));
		colourScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		submitButton = new JButton("Submit");
		submitButton.addActionListener(this);
		
		mainView.add(headerLabel);
		mainView.add(Box.createRigidArea(new Dimension(0, 15)));
		mainView.add(colourScrollPane);
		mainView.add(submitButton);
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (btnGroup.getSelection() == null) return;
		
		String colour = btnGroup.getSelection().getActionCommand();
		JSONObject pickColour = RequestBuilder.buildChooseToken(colour);
		masterView.handleEvent(pickColour);
		this.setVisible(false);
		//this.dispose();
		
	}
	
	@Override
	public void indicateInvalid() {
		headerLabel.setForeground(Color.RED);
		headerLabel.setText(Strings.invalid_colour);
	}

}
