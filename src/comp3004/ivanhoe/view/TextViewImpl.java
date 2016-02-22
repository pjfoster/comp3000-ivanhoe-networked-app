package comp3004.ivanhoe.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.simple.JSONObject;

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
	
	public TextViewImpl (AppClient client, ClientRequestBuilder requestBuilder) {
		this.client = client;
		this.requestBuilder = requestBuilder;
		
		console = new BufferedReader(new InputStreamReader(System.in));
	}
	
	@Override
	public void displayWaitingMessage() {
		System.out.println("Please wait for other players to connect...");
	}
	
	@Override
	public void launch() {
		running = true;
		System.out.println("Launching view... Pretend a window is opening");
		run();
	}

	@Override
	public void displayStartScreen() {
		System.out.println("The Game is beginning!");
	}
	
	@Override
	public void displayChooseColor() {
		System.out.println("Choose the color of the next tournament: ");
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
				
				if (text.contains("start_connect")) { // ex: start_connect Alexi
					client.setUsername(text.split(" ")[1]);
					client.connect();
				}
				
				else if (text.contains("choose_color")) { // ex: choose_color red
					JSONObject request = requestBuilder.buildChooseToken(text.split(" ")[1]);
					client.handleClientRequest(request);
				}
				
				else {
					System.out.println(text);
				}
			}
			catch (IOException e) {  
	         	System.out.println("ERROR reading console input");
	         	running = false;
	         }
			catch (NullPointerException np) {
				System.out.println("Accidentally tried to read null...");
			}
		}
		
	}

	@Override
	public void stop() {
		System.out.println("Closing client!");
		running = false;
	}

}
