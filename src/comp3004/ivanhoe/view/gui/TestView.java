package comp3004.ivanhoe.view.gui;

import comp3004.ivanhoe.util.ClientRequestBuilder;

public class TestView {

	public static void main(String[] args) {
		//AppClient client = new AppClient()
		GUIView view = new GUIView(null, new ClientRequestBuilder());
		view.launch();
	}
	
}
