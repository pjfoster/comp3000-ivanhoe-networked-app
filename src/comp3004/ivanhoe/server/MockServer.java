package comp3004.ivanhoe.server;

import comp3004.ivanhoe.model.Player;

/**
 * Extension of AppServer class with testing capabilities
 * @author PJF
 *
 */
public class MockServer extends AppServer {

	public MockServer(int port, int maxPlayers) {
		super(port, maxPlayers);
	}
	
	// METHOD FOR TESTING
	
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
		for (Player p: controller.getPlayers().values()) {
			if (p.getName().toLowerCase().equals(username.toLowerCase())) {
				return true;
			}
		}
		return false;
	}
}
