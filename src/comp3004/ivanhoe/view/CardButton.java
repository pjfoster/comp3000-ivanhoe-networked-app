package comp3004.ivanhoe.view;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class CardButton extends JButton{
	private String name;
	
	public CardButton(String code){
		super(getImage(code));
	}
	
	public String getName(){
		return name;
	}
	
	public static ImageIcon getImage(String code){
		return new ImageIcon("jpg/" + code);
	}
}
