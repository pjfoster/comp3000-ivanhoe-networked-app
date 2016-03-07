package comp3004.ivanhoe.view;

import java.awt.Color;

import javax.swing.JLabel;

import org.json.simple.JSONObject;

public class WaitingDisplay extends IvanhoeFrame{
	private ViewImpl view;
	private JLabel title;
	
	
	public WaitingDisplay(ViewImpl v){
		this.view = view;
		
		setSize(800,600);
		setBackground(Color.GREEN);
		setLayout(null);
		setResizable(false);

		title = new JLabel("Wait For Server Connection");
		title.setBounds(300,50,200,100);
		title.setVisible(true);
		add(title);
		
		setVisible(true);
	}


	@Override
	public void refresh(JSONObject snapshot) {
		// TODO Auto-generated method stub
		
	}

}
