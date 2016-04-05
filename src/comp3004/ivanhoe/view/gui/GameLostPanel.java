package comp3004.ivanhoe.view.gui;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import comp3004.ivanhoe.util.Strings;

@SuppressWarnings("serial")
public class GameLostPanel extends JPanel {

	public GameLostPanel(String winnerName) {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JLabel gameOverLabel = ImageHandler.loadImage("game_over");
		gameOverLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		String gameOverMessage = String.format(Strings.game_over_loss, winnerName);
		JLabel gameOverText = new JLabel(gameOverMessage);
		
		this.add(Box.createRigidArea(new Dimension(0,100)));
		  this.add(gameOverLabel);
		  this.add(Box.createRigidArea(new Dimension(0,40)));
		  this.add(gameOverText);
	
	}
	
}
