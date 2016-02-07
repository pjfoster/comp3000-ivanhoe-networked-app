package comp3004.ivanhoe.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import comp3004.ivanhoe.util.ClientParser;

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
	
	private ClientParser parser = new ClientParser();
	static Logger logger = Logger.getLogger(AppClient.class);
	
	public AppClient(String ipAddress, int port) {
		PropertyConfigurator.configure("resources/log4j.client.properties");
		this.serverAddress = ipAddress;
		this.serverPort = port;
	}
	
	/**
	 * Responsible for detecting client input and relaying it to the server
	 * Will build commands based on input from the UI
	 */
	public void run() {
		logger.debug(ID + " Client is running");
		while (thread != null) {  
			try {  
				if (streamOut != null) {
					String txt = console.readLine();
					sendMessageToServer(txt);
				} else {
					logger.info(ID + ": Stream Closed");
				}
			}
	         catch(IOException e) {  
	         	logger.error(ID + " Error processing messages: " + e.getMessage());
	         	stop();
	         }
		}
	}
	
	/**
	 * Listens for input coming from the server side; must parse command
	 * and relay it to the view
	 * @param input
	 */
	public void handle(String input) {
		
		// TODO: Create specific commands for things like quitting, connection refused, and connection
		// accepted. There will be a lot more options depending on the type of the command
		
		if (input == null) { return ; }
		
		if (input.equalsIgnoreCase("quit!")) {  
			System.out.println(ID + " Disconnecting...");
			stop();
			
		} else if (input.equalsIgnoreCase("MAX CLIENTS")) {
			logger.error("Server refused the connection; too many clients");
			System.out.println(ID + " Disconnecting...");
			stop();
			
		} else if (input.equalsIgnoreCase("CONNECTION ACCEPTED")) {
			logger.debug(ID + ": Client connected to server");
			
			System.out.println(ID + ": Connected to server: " + socket.getInetAddress());
	    	System.out.println(ID + ": Connected to portid: " + socket.getLocalPort());
		}
		else {
			System.out.println(input);
		}
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
	
	public void sendMessageToServer(String txt) throws IOException {
		streamOut.write(txt + "\n");
		streamOut.flush();
	}
	
	public int getID() { return ID; }
}
