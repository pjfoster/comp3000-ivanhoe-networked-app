package comp3004.ivanhoe.server;

import java.io.Console;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import comp3004.ivanhoe.util.Config;

public class StartServer {
	
	private static Boolean done = Boolean.FALSE;
	private static Boolean started = Boolean.FALSE;

	private static Scanner sc = new Scanner(System.in);
	private static AppServer appServer = null;
	
	final static Logger logger = Logger.getLogger(StartServer.class);
	
	public static void main(String[] argv) {
		
		PropertyConfigurator.configure("resources/log4j.server.properties");
		
		Console c = System.console();
		if (c == null) {
			logger.error("No System Console; use IDE Console");
		}
		
		do {
			String input = sc.nextLine();
			
			if (input.equalsIgnoreCase("START") && !started)
			{
				
				// TODO: Find alternative way to specify number of players and rounds
				
				System.out.println("Enter the number of players: ");
				int numPlayers = sc.nextInt();
				System.out.println("Enter the number of rounds: ");
				int numRounds = sc.nextInt();
				
				logger.info("Starting server ...");
				appServer = new AppServer(Config.DEFAULT_SERVER_PORT, numPlayers, numRounds);
				started = Boolean.TRUE;
			}
			
			if (input.equalsIgnoreCase("SHUTDOWN") && started)
			{
				logger.info("Shutting server down ...");
				appServer.shutdown();
				started = Boolean.FALSE;
				done = Boolean.TRUE;
			}			
		} while (!done);
		
	}
}