package comp3004.ivanhoe.view;

import javax.swing.*;

import org.json.simple.JSONObject;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Display shown when the app is launched; allows client to connect
 * to server and try to join in the Ivanhoe game
 * @author David Farrar
 *
 */
@SuppressWarnings("serial")
public class StartDisplay extends IvanhoeFrame{
	private ViewImpl view;
	
	private JLabel title, authors;
	private JButton start, exit;
	
	public StartDisplay(ViewImpl v){
		this.view = v;
		
		setSize(800,600);
		setBackground(Color.GREEN);
		setLayout(null);
		setResizable(false);
		
		title = new JLabel("Ivanhoe");
		title.setBounds(300,50,200,100);
		title.setVisible(true);
		add(title);
		
		authors = new JLabel("David Farrar and Patricia Foster");
		authors.setBounds(300,150,200,100);
		authors.setVisible(true);
		add(authors);
		
		start = new JButton("Start");
		start.setBounds(300,250,200,100);
		start.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				startAction();	
			}
		});
		start.setVisible(true);
		add(start);
		
		exit = new JButton("Exit");
		exit.setBounds(300, 400, 200, 100);
		exit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				exitAction();			
			}
		});
		exit.setVisible(true);
		add(exit);
		
		setVisible(true);
	}
	
	public void startAction(){
		view.displayStartScreen();;
	}
	
	public void exitAction(){
		view.exit();
	}

	@Override
	public void refresh(JSONObject snapshot) {
		// Nothing to update
	}
}
