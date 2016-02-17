package comp3004.ivanhoe.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import comp3004.ivanhoe.client.AppClient;
import comp3004.ivanhoe.util.ClientRequestBuilder;

/**
 * Text-based UI. Used for testing purposes
 * @author PJF
 *
 */
public class TextViewImpl implements View, Runnable {

	AppClient client;
	ClientRequestBuilder requestBuilder;
	BufferedReader console;
	boolean running = false;
	
	public TextViewImpl (AppClient client) {
		this.client = client;
		requestBuilder = new ClientRequestBuilder();
		
		console = new BufferedReader(new InputStreamReader(System.in));
	}
	
	@Override
	public void launch() {
		running = true;
		run();
	}

	@Override
	public void displayTournamentView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayTurnView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayWelcome() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run() {

		while (running) {
			try {
				String text = console.readLine();
				System.out.println(text);
			}
			catch(IOException e) {  
	         	System.out.println("ERROR reading console input");
	         	running = false;
	         }
		}
		
	}

	@Override
	public void stop() {
		System.out.println("Closing client!");
		running = false;
	}

}
