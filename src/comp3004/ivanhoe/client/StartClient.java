package comp3004.ivanhoe.client;

import comp3004.ivanhoe.util.Config;
import comp3004.ivanhoe.view.ViewFactory;
import comp3004.ivanhoe.view.ViewFactoryImpl;

public class StartClient {
	
	public static void main(String[] argv) {
		ViewFactory viewFactory = new ViewFactoryImpl();
		AppClient client = new AppClient(viewFactory, Config.DEFAULT_SERVER_ADDRESS, Config.DEFAULT_SERVER_PORT); 
		//boolean success = client.connect();
	}
}
