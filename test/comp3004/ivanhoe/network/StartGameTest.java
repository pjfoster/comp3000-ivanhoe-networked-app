package comp3004.ivanhoe.network;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import comp3004.ivanhoe.client.AppClient;
import comp3004.ivanhoe.server.AppServer;
import comp3004.ivanhoe.util.ClientRequestBuilder;
import comp3004.ivanhoe.util.Config;
import comp3004.ivanhoe.view.MockViewFactory;
import comp3004.ivanhoe.view.ViewFactory;

/**
 * Tests that the game starts once the required number of players have registered
 * @author PJF
 *
 */
public class StartGameTest {

	public static final int WAIT_TIME_MILLIS = 300;
	
	private ViewFactory viewFactory;
	private AppServer server;
	
	@Before
	public void setUp() throws Exception {
		viewFactory = new MockViewFactory();
	}

	@After
	public void tearDown() throws Exception {
		server = null;
		viewFactory = null;
	}

	@Test
	public void test2Players() throws IOException, InterruptedException {
		
	}
	
	@Test
	public void test3Players() throws IOException, InterruptedException {
		// create & start the server
		server = new AppServer(10001, 3);

		// create clients
		AppClient client1 = new AppClient(viewFactory, Config.DEFAULT_SERVER_ADDRESS, 10001);
		AppClient spy1 = Mockito.spy(client1);
		spy1.setUsername("Alexei");
		AppClient client2 = new AppClient(viewFactory, Config.DEFAULT_SERVER_ADDRESS, 10001);
		AppClient spy2 = Mockito.spy(client2);
		spy2.setUsername("Luke");

		assertTrue(spy1.connect());
		assertTrue(spy2.connect());

		Thread.sleep(WAIT_TIME_MILLIS);
		
		assertTrue(server.isPlayerRegistered("Alexei"));
		assertTrue(server.isPlayerRegistered("Luke"));
		
		// Ensure that game hasn't started yet
		
		Mockito.verify(spy1).handleServerResponse(Mockito.matches(".*connection_accepted.*"));
		Mockito.verify(spy2).handleServerResponse(Mockito.matches(".*connection_accepted.*"));
		Mockito.verify(spy1, Mockito.atMost(1)).handleServerResponse(Mockito.anyString());
		Mockito.verify(spy2, Mockito.atMost(1)).handleServerResponse(Mockito.anyString());
		
		AppClient client3 = new AppClient(viewFactory, Config.DEFAULT_SERVER_ADDRESS, 10001);
		AppClient spy3 = Mockito.spy(client3);
		spy3.setUsername("Emma");
		assertTrue(spy3.connect());
		
		Thread.sleep(WAIT_TIME_MILLIS);
		assertTrue(server.isPlayerRegistered("Emma"));
		
		Mockito.verify(spy1).handleServerResponse(Mockito.matches(".*start_game.*"));
		Mockito.verify(spy2).handleServerResponse(Mockito.matches(".*start_game.*"));
		Mockito.verify(spy3).handleServerResponse(Mockito.matches(".*start_game.*"));
		
		assertEquals(server.getNumClients(), 3);
	}
	
	@Test
	public void test4Players() throws IOException, InterruptedException {
		
	}
	
	@Test
	public void test5Players() throws IOException {
		
	}

}
