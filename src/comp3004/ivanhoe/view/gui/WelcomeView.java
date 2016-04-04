package comp3004.ivanhoe.view.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class WelcomeView extends JPanel implements ActionListener {
	
	GUIView view;
	JLabel centerComposite;
	JButton connectButton;
	JTextField usernameField;
	JLabel messageField;
	
	public WelcomeView(GUIView view) {
		
		  this.view = view;
		  this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		  
		  JLabel welcomeLabel = ImageHandler.loadImage("welcome");
		  welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		  
		  JLabel userLabel = new JLabel("Please enter your username:");
		  userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		  
		  connectButton = new JButton("CONNECT");
		  connectButton.setFont(new Font("Verdana", Font.BOLD, 20));
		  connectButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		  connectButton.addActionListener(this);
		  
		  usernameField = new JTextField();
		  usernameField.setMaximumSize(new Dimension(300, 50));
		  usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
		  
		  centerComposite = ImageHandler.loadImage("blueknight");
		  centerComposite.setAlignmentX(Component.CENTER_ALIGNMENT);
		  
		  messageField = new JLabel("");
		  messageField.setAlignmentX(Component.CENTER_ALIGNMENT);
		  
		  this.add(Box.createRigidArea(new Dimension(0,50)));
		  this.add(welcomeLabel);
		  this.add(Box.createRigidArea(new Dimension(0,10)));
		  this.add(centerComposite);
		  this.add(Box.createRigidArea(new Dimension(0,10)));
		  this.add(userLabel);
		  this.add(usernameField);
		  this.add(Box.createRigidArea(new Dimension(0,10)));
		  this.add(connectButton);
		  this.add(Box.createRigidArea(new Dimension(0,10)));
		  this.add(messageField);
	}
	
	public void setMessage(String message) {
		messageField.setText(message);
		connectButton.setEnabled(false);
		usernameField.setEnabled(false);
	}
	
	public void setErrorMessage(String message) {
		this.messageField = new JLabel(message);
	}
	
	public void actionPerformed(ActionEvent e) {
		view.connect(usernameField.getText());
	}
	
}
