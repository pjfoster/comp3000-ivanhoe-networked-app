package comp3004.ivanhoe.client;

import comp3004.ivanhoe.util.Config;
import comp3004.ivanhoe.view.ViewFactory;
import comp3004.ivanhoe.view.ViewFactoryImpl;

public class StartClient {
	
	public static void main(String[] argv) {
		ViewFactory viewFactory = new ViewFactoryImpl();
		
		String serverIpAddress;
		if (argv.length != 0) {
			serverIpAddress = argv[0];
		}
		else {
			serverIpAddress = Config.DEFAULT_SERVER_ADDRESS;
		}
		
		AppClient client = new AppClient(viewFactory, serverIpAddress, Config.DEFAULT_SERVER_PORT); 
		//boolean success = client.connect();
	}
}
