package comp3004.ivanhoe.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class ClientThread extends Thread {
	
	private Socket         socket   = null;
	private AppClient      client   = null;
	private BufferedReader streamIn = null;
	private boolean done = false;
	
	static Logger logger = Logger.getLogger(ClientThread.class);
	
	public ClientThread(AppClient client, Socket socket) {
		//PropertyConfigurator.configure("resources/log4j.client.properties");
		this.client = client;
		this.socket = socket;
		this.open();  
		this.start();
	}
	
	public void run() {
		logger.debug("Client Thread " + socket.getLocalPort() + " running.");
		while (!done) 
		{ 
			try 
			{  
				String txt = streamIn.readLine();
				logger.debug(client.getID() + " Received message: " + txt);
				client.handleServerResponse(txt);
			} 
			catch(IOException ioe) 
			{  
				logger.error(client.getID() + ": Error reading from server. " + ioe.getMessage());
			}
		}
	}
	
	public void open() {
		try 
		{  
			streamIn  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	    } 
		catch (IOException ioe) 
		{  
			logger.error(client.getID() + ": Error getting input stream: " + ioe.getMessage());
			client.stop();
	    }
	}

	public void close() {
		done = true;
		try 
		{  
			if (streamIn != null) streamIn.close();
			if (socket != null) socket.close();
			this.socket = null;
			this.streamIn = null;
		} 
		catch(IOException ioe) { 
			logger.error(client.getID() + "Error closing thread. " + ioe.getMessage());
	   }	
	}

}
