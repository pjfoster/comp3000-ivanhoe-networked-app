package comp3004.ivanhoe.client;

import comp3004.ivanhoe.util.Config;

public class StartClient {
	
	public static void main(String[] argv) {
		AppClient client = new AppClient(Config.DEFAULT_SERVER_ADDRESS, Config.DEFAULT_SERVER_PORT); 
		//boolean success = client.connect();
	}
}
