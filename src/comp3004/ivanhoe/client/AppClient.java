package comp3004.ivanhoe.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import comp3004.ivanhoe.view.TextViewImpl;
import comp3004.ivanhoe.view.View;

/**
 * Relays messages from server and controls 
 * @author PJF
 *
 */
public class AppClient implements Runnable {

	private String serverAddress;
	private int serverPort;
	
	private int ID = 0;
	private Socket socket            = null;
	private Thread thread            = null;
	private ClientThread   client    = null;
	private BufferedReader console   = null;
	private BufferedReader streamIn  = null;
	private BufferedWriter streamOut = null;
	
	private View view;
	private JSONParser parser;
	
	static Logger logger = Logger.getLogger(AppClient.class);
	
	public AppClient(String ipAddress, int port) {
		PropertyConfigurator.configure("resources/log4j.client.properties");
		this.serverAddress = ipAddress;
		this.serverPort = port;	
		parser = new JSONParser();
		
		this.view = new TextViewImpl(this);
	}
	
	/**
	 * Responsible for detecting client input and relaying it to the server
	 * Will build commands based on input from the UI
	 */
	public void run() {
		logger.debug(ID + " Client is running");
		
		// display view to client
		view.launch();
		
		while (thread != null) {  
			// TODO: obsolete method?
		}
	}
	
	/**
	 * Listens for input coming from the server side; must parse command
	 * and relay it to the view
	 * @param input
	 */
	public void handleServerResponse(String input) {
		
		System.out.println("AppClient : " + input);
		
		if (input == null) // case where server dies unexpectedly 
		{ 
			System.out.println("AppClient : " + input);
			return ; 
		}
		
		try {
			JSONObject server_response = (JSONObject)parser.parse(input);
			
			if (server_response.get("response_type").equals("connection_rejected") ||
				server_response.get("response_type").equals("quit")) {
				view.stop();
				stop();
			}
			else if (server_response.get("response_type").equals("connection_accepted")) {
				System.out.println("Connection accepted!"); // test
				// TODO: "waiting for other players screen"
			}
			else if (server_response.get("response_type").equals("start_player_turn")) {
				// TODO: pass in parameters
				view.displayTurnView();
			}
			else if (server_response.get("response_type").equals("begin_game")) {
				// TODO: pass in parameters
				view.displayTournamentView();
			}
			else if (server_response.get("response_type").equals("update_view")) {
				// TODO: pass in parameters - or add updateTournamentView() method?
				view.displayTournamentView();
			}
			else if (server_response.get("response_type").equals("make_move")) {
				// TODO: client may require a back and forth with the server (for example when playing
				// action cards)
			}
			else {
				logger.error(String.format("Invalid server response"));
			}
		}
		catch (ParseException e) {
			logger.error(String.format("Error parsing server response"));
		}
	}
	
	/**
	 * Sends request to server
	 * @param request
	 * 	JSONobject constructed by the view containing the client request
	 * @throws IOException
	 */
	public void handleClientRequest(JSONObject request) throws IOException {
		streamOut.write(request.toJSONString() + "\n");
		streamOut.flush();
	}
	
	public boolean connect() {
		
		System.out.println("Establishing connection. Please wait ...");
		
		try {
			this.socket = new Socket(serverAddress, serverPort);
			this.ID = socket.getLocalPort();
			this.start();
			return true;
			
		}
		catch (UnknownHostException uhe) {  
			logger.error("Error connecting to server. " + uhe.getMessage());
			return false;
		} 
		catch (IOException ioe) {  
			logger.error("Error connecting to server. " + ioe.getMessage());
			return false;
		}
		
	}
	
	public void start() throws IOException {
		try {
		   	console	= new BufferedReader(new InputStreamReader(System.in));
			streamIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			streamOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

			 if (thread == null) {  
				 client = new ClientThread(this, socket);
				 thread = new Thread(this);                   
				 thread.start();
			 }
		} 
		catch (IOException ioe) {
	      	logger.error("Error initializing client. " + ioe.getMessage());
	        throw ioe;
		}
	}
	
	public void stop() {
		try { 
	      	if (thread != null) thread = null;
	    	  	if (console != null) console.close();
	    	  	if (streamIn != null) streamIn.close();
	    	  	if (streamOut != null) streamOut.close();

	    	  	if (socket != null) socket.close();

	    	  	this.socket = null;
	    	  	this.console = null;
	    	  	this.streamIn = null;
	    	  	this.streamOut = null;    	  
	      } catch(IOException ioe) {  
	    	  logger.error("Error stopping client. " + ioe.getMessage());
	      }
	      client.close(); 
	}
	
	public int getID() { return ID; }
}
