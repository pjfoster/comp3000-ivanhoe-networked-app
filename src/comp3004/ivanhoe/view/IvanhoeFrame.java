package comp3004.ivanhoe.view;

import javax.swing.JFrame;

import org.json.simple.JSONObject;

public abstract class IvanhoeFrame extends JFrame{
	
	
	public abstract void refresh(JSONObject snapshot);

}
