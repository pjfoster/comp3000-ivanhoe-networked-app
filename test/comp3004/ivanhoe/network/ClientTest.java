package comp3004.ivanhoe.network;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import comp3004.ivanhoe.client.AppClient;
import comp3004.ivanhoe.server.AppServer;
import comp3004.ivanhoe.util.Config;

public class ClientTest {

	public static final int NUM_INITIAL_MESSAGES = 2;
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
	public void testUniqueConnection() {
		
		// create & start the server
		server = new AppServer(10000, 1, 1);
		AppClient client = new AppClient(Config.DEFAULT_SERVER_ADDRESS, 10000);
		AppClient spy = Mockito.spy(client);
		
		assertTrue(spy.connect());
		
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Mockito.verify(spy).handle("CONNECTION ACCEPTED");
		
	}
	
	@Test
	public void testMultipleConnections() {
		// create & start the server
		server = new AppServer(10001, 4, 1);
		
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
		
		Mockito.verify(spy1).handle("CONNECTION ACCEPTED");
		Mockito.verify(spy2).handle("CONNECTION ACCEPTED");
		Mockito.verify(spy3).handle("CONNECTION ACCEPTED");
		
	}
	
	@Test
	public void testCommunication2Clients() {
		// create & start the server
		server = new AppServer(10002, 3, 1);
		
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
		
		Mockito.verify(spy1).handle("---WELCOME TO IVANHOE---");
		Mockito.verify(spy1).handle("Please enter your name: ");
		
		Mockito.verify(spy2).handle("---WELCOME TO IVANHOE---");
		Mockito.verify(spy2).handle("Please enter your name: ");
		
		// verify that client1 did not receive more communications
		Mockito.verify(spy1, Mockito.atMost(3)).handle(Mockito.anyString());
		
	}
	
	@Test
	public void testCommunication3Clients() {
		// create & start the server
		server = new AppServer(10003, 4, 1);
		
		AppClient client1 = new AppClient(Config.DEFAULT_SERVER_ADDRESS, 10003);
		AppClient spy1 = Mockito.spy(client1);
		assertTrue(spy1.connect());
		
		AppClient client2 = new AppClient(Config.DEFAULT_SERVER_ADDRESS, 10003);
		AppClient spy2 = Mockito.spy(client2);
		assertTrue(spy2.connect());
		
		AppClient client3 = new AppClient(Config.DEFAULT_SERVER_ADDRESS, 10003);
		AppClient spy3 = Mockito.spy(client3);
		assertTrue(spy3.connect());

		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Mockito.verify(spy1).handle("---WELCOME TO IVANHOE---");
		Mockito.verify(spy1).handle("Please enter your name: ");
		
		Mockito.verify(spy2).handle("---WELCOME TO IVANHOE---");
		Mockito.verify(spy2).handle("Please enter your name: ");
		
		Mockito.verify(spy3).handle("---WELCOME TO IVANHOE---");
		Mockito.verify(spy3).handle("Please enter your name: ");
		
		// verify that client1 did not receive more communications
		Mockito.verify(spy1, Mockito.atMost(3)).handle(Mockito.anyString());
		Mockito.verify(spy2, Mockito.atMost(3)).handle(Mockito.anyString());
	}
	
	@Test
	public void testCommunication4Clients() {
		// create & start the server
		server = new AppServer(10004, 5, 1);
		
		AppClient client1 = new AppClient(Config.DEFAULT_SERVER_ADDRESS, 10004);
		AppClient spy1 = Mockito.spy(client1);
		assertTrue(spy1.connect());
		
		AppClient client2 = new AppClient(Config.DEFAULT_SERVER_ADDRESS, 10004);
		AppClient spy2 = Mockito.spy(client2);
		assertTrue(spy2.connect());
		
		AppClient client3 = new AppClient(Config.DEFAULT_SERVER_ADDRESS, 10004);
		AppClient spy3 = Mockito.spy(client3);
		assertTrue(spy3.connect());
		
		AppClient client4 = new AppClient(Config.DEFAULT_SERVER_ADDRESS, 10004);
		AppClient spy4 = Mockito.spy(client4);
		assertTrue(spy4.connect());

		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Mockito.verify(spy1).handle("---WELCOME TO IVANHOE---");
		Mockito.verify(spy1).handle("Please enter your name: ");
		
		Mockito.verify(spy2).handle("---WELCOME TO IVANHOE---");
		Mockito.verify(spy2).handle("Please enter your name: ");
		
		Mockito.verify(spy3).handle("---WELCOME TO IVANHOE---");
		Mockito.verify(spy3).handle("Please enter your name: ");
		
		Mockito.verify(spy4).handle("---WELCOME TO IVANHOE---");
		Mockito.verify(spy4).handle("Please enter your name: ");
		
		// verify that client1 did not receive more communications
		Mockito.verify(spy1, Mockito.atMost(3)).handle(Mockito.anyString());
		Mockito.verify(spy2, Mockito.atMost(3)).handle(Mockito.anyString());
		Mockito.verify(spy3, Mockito.atMost(3)).handle(Mockito.anyString());
	}
	
	@Test
	public void testMaxConnections() {
		// create & start the server
		server = new AppServer(10006, 1, 1);
		
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
		
		Mockito.verify(spy1).handle("CONNECTION ACCEPTED");
		Mockito.verify(spy2).handle("MAX CLIENTS");
	}

}
