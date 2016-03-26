package comp3004.ivanhoe.view.gui;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class WelcomeView extends JPanel {
	
	JButton connectButton;
	JTextField usernameField;
	
	public WelcomeView() {
		  this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		  //this.setAlignmentX(Component.CENTER_ALIGNMENT);
		  //this.setAlignmentY(Component.CENTER_ALIGNMENT);
		  
		  JLabel welcomeLabel = new JLabel("Welcome to Ivanhoe");
		  welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		  JLabel userLabel = new JLabel("Please enter your username:");
		  userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		  connectButton = new JButton("CONNECT");
		  connectButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		  usernameField = new JTextField();
		  usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
		  
		  this.add(Box.createRigidArea(new Dimension(0,10)));
		  this.add(welcomeLabel);
		  this.add(Box.createRigidArea(new Dimension(0,10)));
		  this.add(userLabel);
		  this.add(usernameField);
		  this.add(Box.createRigidArea(new Dimension(0,10)));
		  this.add(connectButton);
	}
	
}
