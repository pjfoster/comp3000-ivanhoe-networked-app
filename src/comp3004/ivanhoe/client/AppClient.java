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

import comp3004.ivanhoe.util.ClientRequestBuilder;
import comp3004.ivanhoe.view.TextViewImpl;
import comp3004.ivanhoe.view.View;
import comp3004.ivanhoe.view.ViewFactory;

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
	
	private String username = "";
	private View view;
	private JSONParser parser;
	private ClientRequestBuilder requestBuilder;
	
	static Logger logger = Logger.getLogger(AppClient.class);
	
	public AppClient(ViewFactory viewFactory, String ipAddress, int port) {
		//PropertyConfigurator.configure("resources/log4j.client.properties");
		this.serverAddress = ipAddress;
		this.serverPort = port;	
		parser = new JSONParser();
		requestBuilder = new ClientRequestBuilder();
		
		this.view = viewFactory.createView(this);
		view.launch();
	}
	
	/**
	 * Responsible for detecting client input and relaying it to the server
	 * Will build commands based on input from the UI
	 */
	public void run() {
		logger.debug(ID + " Client is connected and running");
		while (thread != null) {  
			// TODO: obsolete method?
		}
	}
	
	/**
	 * Listens for input coming from the server side; must parse command
	 * and relay it to the view
	 * @param input
	 * @throws IOException 
	 */
	public void handleServerResponse(String input) throws IOException {
		
		if (input == null) // case where server dies unexpectedly 
		{ return ; }
		
		try {
			JSONObject server_response = (JSONObject)parser.parse(input);
			
			if (server_response.get("response_type").equals("connection_rejected") ||
				server_response.get("response_type").equals("quit")) {
				view.stop();
				stop();
			}
			
			else if (server_response.get("response_type").equals("connection_accepted")) {
				System.out.println("Connection accepted!"); // test
				handleClientRequest(requestBuilder.buildRegisterPlayer(username));
			}
			
			else if (server_response.get("response_type").equals("start_game")) {
				view.displayStartScreen(server_response);
			}
			
			else if (server_response.get("response_type").equals("waiting")) {
				view.displayWaitingMessage();
			}
			
			else if (server_response.get("response_type").equals("indicate_turn")) {
				view.displayTurnPlayer((String)server_response.get("player_name"));
			}
			
			else if (server_response.get("response_type").equals("invalid_choice")) {
				view.displayInvalidMove();
			}
			
			else if (server_response.get("response_type").equals("choose_color")) {
				view.displayChooseColor();
			}
			
			else if (server_response.get("response_type").equals("choose_token")) {
				view.displayChooseToken(server_response);
			}
			
			else if (server_response.get("response_type").equals("withdraw")) {
				view.announceWithdrawal((String)server_response.get("player_name"));
			}
			
			else if (server_response.get("response_type").equals("start_tournament")) {
				view.displayStartScreen(server_response);
			}
			
			else if (server_response.get("response_type").equals("start_player_turn")) {
				view.displayTurnView((String)server_response.get("drawn_card"));
			}
			
			else if (server_response.get("response_type").equals("update_view")) {
				view.displayTournamentView(server_response);
			}
			
			else if (server_response.get("response_type").equals("tournament_over_win")) {
				if (((String)server_response.get("token_color")).toLowerCase().equals("purple")) {
					view.displayPurpleTournamentWonMessage();
				}
				else {
					view.displayTournamentWonMessage((String)server_response.get("token_color"));
				}
			}
			
			else if (server_response.get("response_type").equals("tournament_over_loss")) {
				view.displayTournamentLossMessage((String)server_response.get("winner"));
			}
			
			else if (server_response.get("response_type").equals("game_over_win")) {
				view.displayGameWonMessage();
			}
			
			else if (server_response.get("response_type").equals("game_over_loss")) {
				view.displayGameLossMessage((String)server_response.get("winner"));
			}
			
			
			else {
				logger.error(String.format("Invalid server response"));
				System.out.println(server_response);
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
	
	public void setUsername(String username) {
		this.username = username;
	}
}
