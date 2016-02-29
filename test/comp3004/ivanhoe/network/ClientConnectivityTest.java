package comp3004.ivanhoe.network;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import comp3004.ivanhoe.client.AppClient;
import comp3004.ivanhoe.server.AppServer;
import comp3004.ivanhoe.util.Config;
import comp3004.ivanhoe.view.MockViewFactory;
import comp3004.ivanhoe.view.ViewFactory;

public class ClientConnectivityTest {

	private AppServer server;
	private ViewFactory viewFactory;
	
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
	/**
	 * Tests that the client can connect to the server
	 */
	public void testUniqueConnection() throws IOException {
		
		// create & start the server
		server = new AppServer(10000, 1);
		AppClient client = new AppClient(viewFactory, Config.DEFAULT_SERVER_ADDRESS, 10000);
		AppClient spy = Mockito.spy(client);
		
		assertTrue(spy.connect());
		
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Mockito.verify(spy).handleServerResponse(Mockito.matches(".*connection_accepted.*"));
		assertEquals(server.getNumClients(), 1);
		
	}
	
	@Test
	public void testMultipleConnections() throws IOException {
		// create & start the server
		server = new AppServer(10001, 4);
		
		// create clients
		AppClient client1 = new AppClient(viewFactory, Config.DEFAULT_SERVER_ADDRESS, 10001);
		AppClient spy1 = Mockito.spy(client1);
		AppClient client2 = new AppClient(viewFactory, Config.DEFAULT_SERVER_ADDRESS, 10001);
		AppClient spy2 = Mockito.spy(client2);
		AppClient client3 = new AppClient(viewFactory, Config.DEFAULT_SERVER_ADDRESS, 10001);
		AppClient spy3 = Mockito.spy(client3);
		
		assertTrue(spy1.connect());
		assertTrue(spy2.connect());
		assertTrue(spy3.connect());
		
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Mockito.verify(spy1).handleServerResponse(Mockito.matches(".*connection_accepted.*"));
		Mockito.verify(spy2).handleServerResponse(Mockito.matches(".*connection_accepted.*"));
		Mockito.verify(spy3).handleServerResponse(Mockito.matches(".*connection_accepted.*"));
		assertEquals(server.getNumClients(), 3);
		
	}
	
	@Test
	public void testMaxConnections() throws IOException {
		// create & start the server
		server = new AppServer(10006, 1);
		
		AppClient client1 = new AppClient(viewFactory, Config.DEFAULT_SERVER_ADDRESS, 10006);
		AppClient spy1 = Mockito.spy(client1);
		AppClient client2 = new AppClient(viewFactory, Config.DEFAULT_SERVER_ADDRESS, 10006);
		AppClient spy2 = Mockito.spy(client2);	
		
		try {
			assertTrue(spy1.connect());
			Thread.sleep(300);
			assertTrue(spy2.connect()); // client was able to reach the server		
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Mockito.verify(spy1).handleServerResponse(Mockito.matches(".*connection_accepted.*"));
		Mockito.verify(spy2).handleServerResponse(Mockito.matches(".*connection_rejected.*"));
		assertEquals(server.getNumClients(), 1);
	}

}
