package comp3004.ivanhoe.view.gui;

import java.io.IOException;
import java.util.ArrayList;

import comp3004.ivanhoe.util.ClientRequestBuilder;

public class TestView {

	public static void main(String[] args) {
		//AppClient client = new AppClient()
		GUIView view;
		try {
			view = new GUIView(null, new ClientRequestBuilder());
			 
			/*ArrayList<String> cards = new ArrayList<String>();
			cards.add("s3");
			cards.add("r5");
			cards.add("y2");
			cards.add("p7");
			cards.add("m6");
			
			ArrayList<String> tokens = new ArrayList<String>();
			tokens.add("purple");
			tokens.add("blue");
			
			PlayerDisplay p1 = new PlayerDisplay(ImageHandler.loadImage("blackknight"), 60001,
					                             "Alexei", tokens, cards, 8);
			
			view.add(p1);*/
			view.launch();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
