package comp3004.ivanhoe.server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import comp3004.ivanhoe.controller.IvanhoeGame;

public class AppServer implements Runnable {
	int clientCount = 0;
	private Thread thread = null;
	private ServerSocket server = null;
	private HashMap<Integer, ServerThread> clients;
	private IvanhoeGame game;
	
	int maxPlayers;
	
	final static Logger logger = Logger.getLogger(StartServer.class);
	
	public AppServer(int port, int maxPlayers, int numRounds) {
		
		PropertyConfigurator.configure("resources/log4j.server.properties");
		
		try {
			/** Set up game object */
			this.maxPlayers = maxPlayers;
			game = new IvanhoeGame(maxPlayers, numRounds);	
			
			logger.debug("Binding to port " + port + ", please wait  ...");
	
			clients = new HashMap<Integer, ServerThread>();
			server = new ServerSocket(port);
			server.setReuseAddress(true);
			start();
			logger.info(String.format("Server: %s: %d: port started", server.getInetAddress(), port));
			
		} 
		catch (IOException ioe) {
			logger.fatal("ERROR: Could not start server: " + ioe.getMessage());
		}
		
	}
	
	
	/** Now we start the servers main thread */
	public void start() {
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}
	
	/**
	 * 
	 * @param id
	 * 	id used to retrieve ServerThread
	 * @param input
	 * 	client's message
	 */
	public synchronized void handle(int id, String input) {
		// will call Game.processInput
		
		if (input == null) { return; }
		if (input.equals("quit!")) 
		{
			logger.info(String.format("Removing Client: %d", id));
			if (clients.containsKey(id)) {
				clients.get(id).send("quit!" + "\n");
				remove(id);
			}
		}
		else if (input.equals("shutdown!")) { shutdown(); }
		else 
		{
			// TOOD: parse the command, process input
		}
			
	}
	
	/**
	 * Broadcast message to all clients
	 * TODO: Do we still need this?
	 * @param message
	 */
	public void broadcast(String message) {
		for (ServerThread client: clients.values()) {
			client.send(message);
		}
	}
	
	/** The main server thread starts and is listening for clients to connect */
	public void run() {
		while (thread != null) {
			try {
				logger.debug("Waiting for a client ...");
				addThread(server.accept());
			} catch (IOException e) {				
				logger.error("ERROR: " + e.getMessage());
			}
		}
	}
	
	/** 
	 * Client connection is accepted and now we need to handle it and register it 
	 * and with the server | HashTable 
	 **/
	public void addThread(Socket socket) {

		if (clientCount < maxPlayers) {
			logger.debug("Client accepted");
			try {
				
				// TODO: Modify what is sent back to the client
				
				/** Create a separate server thread for each client */
				ServerThread serverThread = new ServerThread(this, socket);
				/** Open and start the thread */
				serverThread.open();
				serverThread.start();
				clients.put(serverThread.getID(), serverThread);
				this.clientCount++;
				
				serverThread.send("CONNECTION ACCEPTED");
				serverThread.send("---WELCOME TO THE GAME ENGINE---");
				serverThread.send("Please enter your name: ");
				logger.info(String.format("Client:%s:%d: port connected", 
										   socket.getInetAddress(), serverThread.getID()));
				logger.debug(clientCount + " clients are now connected");
				
			} catch (IOException e) {
				logger.error("Error registering client. " + e.getMessage());
			}
		} else {
			
			try {
				BufferedWriter streamOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				streamOut.write("MAX CLIENTS\n");
				streamOut.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			logger.info(String.format("Client Tried to connect: %s", socket));
			logger.info(String.format("Client refused: maximum number of clients reached: %d", maxPlayers));
				
		}
	}
	
	/** Try and shutdown the client cleanly */
	public synchronized void remove(int id) {
		if (clients.containsKey(id)) {
			ServerThread toTerminate = clients.get(id);
			clients.remove(id);
			clientCount--;

			toTerminate.close();
			toTerminate = null;
		}
	}
	
	/** Shutdown the server cleanly */
	public void shutdown() {
		Set<Integer> keys = clients.keySet();

		if (thread != null) {
			thread = null;
		}

		try {
			for (Integer key : keys) {
				clients.get(key).close();
				clients.put(key, null);
			}
			clients.clear();
			server.close();
		} catch (IOException e) {
			logger.error("Error shutting down server. " + e.getMessage());
		}
		logger.info(String.format("Server Shutdown cleanly %s\n", server));
	}
	
	/** Primarily for testing purposes. Returns number of clients. */
	public int getNumClients() {
		return clients.size();
	}
	
	
}
