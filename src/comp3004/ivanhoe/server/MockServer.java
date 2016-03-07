package comp3004.ivanhoe.server;

import org.json.simple.JSONObject;

import comp3004.ivanhoe.controller.IvanhoeController;
import comp3004.ivanhoe.model.Player;

/**
 * Extension of AppServer class with testing capabilities
 * @author PJF
 *
 */
public class MockServer extends AppServer {

	/**
	 * Certain tests require networking; others require that it is shut off
	 * This is controlled through this boolean flag
	 */
	boolean enableNetworking;
	
	public MockServer() {
		super(0, 0);
	}
	
	public MockServer(int port, int maxPlayers) {
		super(port, maxPlayers);
		enableNetworking = false;
	}
	
	@Override
	public void broadcast(JSONObject s) { 
		if (enableNetworking) {
			super.broadcast(s);
		}
		return; 
	}
	
	@Override
	public void sendToClient(int id, JSONObject s) {
		if (enableNetworking) {
			super.sendToClient(id,  s);
		}
		return; 
	}
	
	// METHODS FOR TESTING
	
	/** Primarily for testing purposes. Returns number of clients. */
	public int getNumClients() {
		return clients.size();
	}

	/**
	 * Verifies that the user was correctly registered
	 * @param id
	 * @param username
	 * @return
	 */
	public boolean isPlayerRegistered(String username) {
		return controller.isPlayerRegistered(username);
	}
	
	public void enableNetworking(boolean enabled) {
		enableNetworking = enabled;
	}
	
	public void setController(IvanhoeController controller) {
		this.controller = controller;
	}
}
