package comp3004.ivanhoe.view;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class CardButton extends JButton{
	private String code;
	
	public CardButton(String code){
		super(getImage(code));
	}
	
	public String getCode(){
		return code;
	}
	
	public static ImageIcon getImage(String code){
		return new ImageIcon("jpg/" + code);
	}
}
