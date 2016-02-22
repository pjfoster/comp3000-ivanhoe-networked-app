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

public class ClientConnectivityTest {

	private AppServer server;
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		server = null;
	}

	@Test
	/**
	 * Tests that the client can connect to the server
	 */
	public void testUniqueConnection() throws IOException {
		
		// create & start the server
		server = new AppServer(10000, 1);
		AppClient client = new AppClient(Config.DEFAULT_SERVER_ADDRESS, 10000);
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
		AppClient client1 = new AppClient(Config.DEFAULT_SERVER_ADDRESS, 10001);
		AppClient spy1 = Mockito.spy(client1);
		AppClient client2 = new AppClient(Config.DEFAULT_SERVER_ADDRESS, 10001);
		AppClient spy2 = Mockito.spy(client2);
		AppClient client3 = new AppClient(Config.DEFAULT_SERVER_ADDRESS, 10001);
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
	public void testCommunication2Clients() throws IOException {
		// create & start the server
		// TODO: how to test the server?
		server = new AppServer(10002, 3);
		AppServer serverSpy = Mockito.spy(server);
		
		AppClient client1 = new AppClient(Config.DEFAULT_SERVER_ADDRESS, 10002);
		AppClient spy1 = Mockito.spy(client1);
		assertTrue(spy1.connect());
		
		AppClient client2 = new AppClient(Config.DEFAULT_SERVER_ADDRESS, 10002);
		AppClient spy2 = Mockito.spy(client2);
		assertTrue(spy2.connect());

		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Mockito.verify(spy1).handleServerResponse(Mockito.matches(".*connection_accepted.*"));
		Mockito.verify(spy2).handleServerResponse(Mockito.matches(".*connection_accepted.*"));
		//Mockito.verify(serverSpy).handle(Mockito.eq(client1.getID()), Mockito.matches(".*register_player.*"));
		//Mockito.verify(serverSpy).handle(Mockito.eq(client2.getID()), Mockito.matches(".*register_player.*"));
		assertEquals(server.getNumClients(), 2);
	}
	
	@Test
	public void testMaxConnections() throws IOException {
		// create & start the server
		server = new AppServer(10006, 1);
		
		AppClient client1 = new AppClient(Config.DEFAULT_SERVER_ADDRESS, 10006);
		AppClient spy1 = Mockito.spy(client1);
		AppClient client2 = new AppClient(Config.DEFAULT_SERVER_ADDRESS, 10006);
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
