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
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import comp3004.ivanhoe.controller.IvanhoeController;
import comp3004.ivanhoe.util.ResponseBuilder;

public class AppServer implements Runnable {
	protected int clientCount = 0;
	private Thread thread = null;
	private ServerSocket server = null;
	protected HashMap<Integer, ServerThread> clients;
	protected IvanhoeController controller;
	protected JSONParser parser;
	protected ResponseBuilder responseBuilder;
	protected int maxPlayers;
	
	final static Logger logger = Logger.getLogger(StartServer.class);
	
	public AppServer(int port, int maxPlayers) {
		
		PropertyConfigurator.configure("resources/log4j.server.properties");
		
		try {
			/** Set up game object */
			this.maxPlayers = maxPlayers;	
			parser = new JSONParser();
			controller = new IvanhoeController(this, maxPlayers);
			
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
		
		if (input == null) { return; }
		else {
			try {
				JSONObject client_request = (JSONObject)parser.parse(input);
				
				// handle different types of client requests
				if (client_request.get("request_type").equals("quit")) {
					logger.info(String.format("Removing Client: %d", id));
					if (clients.containsKey(id)) {
						clients.get(id).send("quit!" + "\n");
						remove(id);
					}
				}
				
				else if (client_request.get("request_type").equals("register_player")) {
					controller.addPlayer(id, (String)client_request.get("username"));
					logger.info("Registering " + client_request.get("username"));
				} 
				
				else if (client_request.get("request_type").equals("choose_token")) {
					controller.processPlayerMove(id, client_request);
				}
				
				else if (client_request.get("request_type").equals("turn_move")) {
					controller.processPlayerMove(id, client_request);
				}
				
				// TODO: should we keep this?
				else if (client_request.get("request_type").equals("shutdown")) {
					shutdown(); 
				}
				
				else {
					logger.error(String.format("%d: Invalid request from client", id));
				}
				
			}
			catch (ParseException e) {
				logger.error(String.format("%d: Error parsing client message", id));
			}
		}		
	}
	
	/**
	 * Relay message to client
	 * @param id
	 * 	id of client thread
	 * @param message
	 * 	in JSON format
	 */
	public void sendToClient(int id, JSONObject message) {
		ServerThread clientThread = clients.get(id);
		clientThread.send(message.toJSONString());
	}
	
	/**
	 * Broadcast message to all clients
	 * @param message
	 */
	public void broadcast(JSONObject message) {
		for (ServerThread client: clients.values()) {
			client.send(message.toJSONString());
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

		JSONObject connectionResponse = null;
		
		if (clientCount < maxPlayers) {
			logger.debug("Client accepted");
			try {
	
				/** Create a separate server thread for each client */
				ServerThread serverThread = new ServerThread(this, socket);
				/** Open and start the thread */
				serverThread.open();
				serverThread.start();
				clients.put(serverThread.getID(), serverThread);
				this.clientCount++;
				
				connectionResponse = responseBuilder.buildConnectionAccepted();
				serverThread.send(connectionResponse.toJSONString());
				
				logger.info(String.format("Client:%s:%d: port connected", 
										   socket.getInetAddress(), serverThread.getID()));
				logger.debug(clientCount + " clients are now connected");
				
			} catch (IOException e) {
				logger.error("Error registering client. " + e.getMessage());
			}
		} else {
			
			try {
				BufferedWriter streamOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				connectionResponse = responseBuilder.buildConnectionRejected();
				streamOut.write(connectionResponse.toJSONString() + "\n");
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
	

}
