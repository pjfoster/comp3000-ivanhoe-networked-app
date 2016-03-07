package comp3004.ivanhoe.network;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import comp3004.ivanhoe.client.AppClient;
import comp3004.ivanhoe.server.MockServer;
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
	private MockServer server;

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

		final int SERVER_PORT = 10011;

		// create & start the server
		server = new MockServer(SERVER_PORT, 2);
		server.enableNetworking(true);

		// create clients
		AppClient client1 = new AppClient(viewFactory, Config.DEFAULT_SERVER_ADDRESS, SERVER_PORT);
		AppClient spy1 = Mockito.spy(client1);
		spy1.setUsername("Alexei");
		assertTrue(spy1.connect());

		Thread.sleep(WAIT_TIME_MILLIS);

		// Ensure that game hasn't started yet but that Alexei is connected
		assertTrue(server.isPlayerRegistered("Alexei"));
		Mockito.verify(spy1).handleServerResponse(Mockito.matches(".*connection_accepted.*"));

		Thread.sleep(WAIT_TIME_MILLIS);

		Mockito.verify(spy1).handleServerResponse(Mockito.matches(".*waiting.*"));
		Mockito.verify(spy1, Mockito.atMost(2)).handleServerResponse(Mockito.anyString());

		AppClient client2 = new AppClient(viewFactory, Config.DEFAULT_SERVER_ADDRESS, SERVER_PORT);
		AppClient spy2 = Mockito.spy(client2);
		spy2.setUsername("Emma");
		assertTrue(spy2.connect());

		Thread.sleep(WAIT_TIME_MILLIS);

		assertTrue(server.isPlayerRegistered("Emma"));

		Mockito.verify(spy1).handleServerResponse(Mockito.matches(".*start_game.*"));
		Mockito.verify(spy2).handleServerResponse(Mockito.matches(".*start_game.*"));

		assertEquals(server.getNumClients(), 2);

		// Reject all players after this
		AppClient clientX = new AppClient(viewFactory, Config.DEFAULT_SERVER_ADDRESS, SERVER_PORT);
		AppClient spyX = Mockito.spy(clientX);
		spyX.setUsername("MP");
		assertTrue(spyX.connect());

		Thread.sleep(WAIT_TIME_MILLIS);
		assertFalse(server.isPlayerRegistered("MP"));
		Mockito.verify(spyX).handleServerResponse(Mockito.matches(".*connection_rejected.*"));
		assertEquals(server.getNumClients(), 2);
	}

	@Test
	public void test3Players() throws IOException, InterruptedException {

		final int SERVER_PORT = 10012;

		// create & start the server
		server = new MockServer(SERVER_PORT, 3);
		server.enableNetworking(true);

		// create clients
		AppClient client1 = new AppClient(viewFactory, Config.DEFAULT_SERVER_ADDRESS, SERVER_PORT);
		AppClient spy1 = Mockito.spy(client1);
		spy1.setUsername("Alexei");
		AppClient client2 = new AppClient(viewFactory, Config.DEFAULT_SERVER_ADDRESS, SERVER_PORT);
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

		Thread.sleep(WAIT_TIME_MILLIS);

		Mockito.verify(spy1).handleServerResponse(Mockito.matches(".*waiting.*"));
		Mockito.verify(spy2).handleServerResponse(Mockito.matches(".*waiting.*"));
		Mockito.verify(spy1, Mockito.atMost(2)).handleServerResponse(Mockito.anyString());
		Mockito.verify(spy2, Mockito.atMost(2)).handleServerResponse(Mockito.anyString());

		AppClient client3 = new AppClient(viewFactory, Config.DEFAULT_SERVER_ADDRESS, SERVER_PORT);
		AppClient spy3 = Mockito.spy(client3);
		spy3.setUsername("Emma");
		assertTrue(spy3.connect());

		Thread.sleep(WAIT_TIME_MILLIS);

		assertTrue(server.isPlayerRegistered("Emma"));

		Mockito.verify(spy1).handleServerResponse(Mockito.matches(".*start_game.*"));
		Mockito.verify(spy2).handleServerResponse(Mockito.matches(".*start_game.*"));
		Mockito.verify(spy3).handleServerResponse(Mockito.matches(".*start_game.*"));

		assertEquals(server.getNumClients(), 3);

		// Reject all players after this
		AppClient clientX = new AppClient(viewFactory, Config.DEFAULT_SERVER_ADDRESS, SERVER_PORT);
		AppClient spyX = Mockito.spy(clientX);
		spyX.setUsername("MP");
		assertTrue(spyX.connect());

		Thread.sleep(WAIT_TIME_MILLIS);
		assertFalse(server.isPlayerRegistered("MP"));
		Mockito.verify(spyX).handleServerResponse(Mockito.matches(".*connection_rejected.*"));
		assertEquals(server.getNumClients(), 3);
	}

	@Test
	public void test4Players() throws IOException, InterruptedException {
		final int SERVER_PORT = 10013;

		// create & start the server
		server = new MockServer(SERVER_PORT, 4);
		server.enableNetworking(true);

		// create clients
		AppClient client1 = new AppClient(viewFactory, Config.DEFAULT_SERVER_ADDRESS, SERVER_PORT);
		AppClient spy1 = Mockito.spy(client1);
		spy1.setUsername("Alexei");
		AppClient client2 = new AppClient(viewFactory, Config.DEFAULT_SERVER_ADDRESS, SERVER_PORT);
		AppClient spy2 = Mockito.spy(client2);
		spy2.setUsername("Luke");
		AppClient client3 = new AppClient(viewFactory, Config.DEFAULT_SERVER_ADDRESS, SERVER_PORT);
		AppClient spy3 = Mockito.spy(client3);
		spy3.setUsername("Jayson");

		assertTrue(spy1.connect());
		assertTrue(spy2.connect());
		assertTrue(spy3.connect());

		Thread.sleep(WAIT_TIME_MILLIS);

		assertTrue(server.isPlayerRegistered("Alexei"));
		assertTrue(server.isPlayerRegistered("Luke"));
		assertTrue(server.isPlayerRegistered("Jayson"));

		// Ensure that game hasn't started yet
		Mockito.verify(spy1).handleServerResponse(Mockito.matches(".*connection_accepted.*"));
		Mockito.verify(spy2).handleServerResponse(Mockito.matches(".*connection_accepted.*"));
		Mockito.verify(spy3).handleServerResponse(Mockito.matches(".*connection_accepted.*"));

		Thread.sleep(WAIT_TIME_MILLIS);

		Mockito.verify(spy1).handleServerResponse(Mockito.matches(".*waiting.*"));
		Mockito.verify(spy2).handleServerResponse(Mockito.matches(".*waiting.*"));
		Mockito.verify(spy3).handleServerResponse(Mockito.matches(".*waiting.*"));
		Mockito.verify(spy1, Mockito.atMost(2)).handleServerResponse(Mockito.anyString());
		Mockito.verify(spy2, Mockito.atMost(2)).handleServerResponse(Mockito.anyString());
		Mockito.verify(spy3, Mockito.atMost(2)).handleServerResponse(Mockito.anyString());

		AppClient client4 = new AppClient(viewFactory, Config.DEFAULT_SERVER_ADDRESS, SERVER_PORT);
		AppClient spy4 = Mockito.spy(client4);
		spy4.setUsername("Emma");
		assertTrue(spy4.connect());

		Thread.sleep(WAIT_TIME_MILLIS);

		assertTrue(server.isPlayerRegistered("Emma"));

		Mockito.verify(spy1).handleServerResponse(Mockito.matches(".*start_game.*"));
		Mockito.verify(spy2).handleServerResponse(Mockito.matches(".*start_game.*"));
		Mockito.verify(spy3).handleServerResponse(Mockito.matches(".*start_game.*"));
		Mockito.verify(spy4).handleServerResponse(Mockito.matches(".*start_game.*"));

		assertEquals(server.getNumClients(), 4);

		// Reject all players after this
		AppClient clientX = new AppClient(viewFactory, Config.DEFAULT_SERVER_ADDRESS, SERVER_PORT);
		AppClient spyX = Mockito.spy(clientX);
		spyX.setUsername("MP");
		assertTrue(spyX.connect());

		Thread.sleep(WAIT_TIME_MILLIS);
		assertFalse(server.isPlayerRegistered("MP"));
		Mockito.verify(spyX).handleServerResponse(Mockito.matches(".*connection_rejected.*"));
		assertEquals(server.getNumClients(), 4);
	}

	@Test
	public void test5Players() throws InterruptedException, IOException {
		final int SERVER_PORT = 10014;

		// create & start the server
		server = new MockServer(SERVER_PORT, 5);
		server.enableNetworking(true);

		// create clients
		AppClient client1 = new AppClient(viewFactory, Config.DEFAULT_SERVER_ADDRESS, SERVER_PORT);
		AppClient spy1 = Mockito.spy(client1);
		spy1.setUsername("Alexei");
		AppClient client2 = new AppClient(viewFactory, Config.DEFAULT_SERVER_ADDRESS, SERVER_PORT);
		AppClient spy2 = Mockito.spy(client2);
		spy2.setUsername("Luke");
		AppClient client3 = new AppClient(viewFactory, Config.DEFAULT_SERVER_ADDRESS, SERVER_PORT);
		AppClient spy3 = Mockito.spy(client3);
		spy3.setUsername("Jayson");
		AppClient client4 = new AppClient(viewFactory, Config.DEFAULT_SERVER_ADDRESS, SERVER_PORT);
		AppClient spy4 = Mockito.spy(client4);
		spy4.setUsername("Max");

		assertTrue(spy1.connect());
		assertTrue(spy2.connect());
		assertTrue(spy3.connect());
		assertTrue(spy4.connect());

		Thread.sleep(WAIT_TIME_MILLIS);

		assertTrue(server.isPlayerRegistered("Alexei"));
		assertTrue(server.isPlayerRegistered("Luke"));
		assertTrue(server.isPlayerRegistered("Jayson"));
		assertTrue(server.isPlayerRegistered("Max"));

		// Ensure that game hasn't started yet
		Mockito.verify(spy1).handleServerResponse(Mockito.matches(".*connection_accepted.*"));
		Mockito.verify(spy2).handleServerResponse(Mockito.matches(".*connection_accepted.*"));
		Mockito.verify(spy3).handleServerResponse(Mockito.matches(".*connection_accepted.*"));
		Mockito.verify(spy4).handleServerResponse(Mockito.matches(".*connection_accepted.*"));

		Thread.sleep(WAIT_TIME_MILLIS);

		Mockito.verify(spy1).handleServerResponse(Mockito.matches(".*waiting.*"));
		Mockito.verify(spy2).handleServerResponse(Mockito.matches(".*waiting.*"));
		Mockito.verify(spy3).handleServerResponse(Mockito.matches(".*waiting.*"));
		Mockito.verify(spy4).handleServerResponse(Mockito.matches(".*waiting.*"));
		Mockito.verify(spy1, Mockito.atMost(2)).handleServerResponse(Mockito.anyString());
		Mockito.verify(spy2, Mockito.atMost(2)).handleServerResponse(Mockito.anyString());
		Mockito.verify(spy3, Mockito.atMost(2)).handleServerResponse(Mockito.anyString());
		Mockito.verify(spy4, Mockito.atMost(2)).handleServerResponse(Mockito.anyString());

		AppClient client5 = new AppClient(viewFactory, Config.DEFAULT_SERVER_ADDRESS, SERVER_PORT);
		AppClient spy5 = Mockito.spy(client5);
		spy5.setUsername("Emma");
		assertTrue(spy5.connect());

		Thread.sleep(WAIT_TIME_MILLIS);

		assertTrue(server.isPlayerRegistered("Emma"));

		Mockito.verify(spy1).handleServerResponse(Mockito.matches(".*start_game.*"));
		Mockito.verify(spy2).handleServerResponse(Mockito.matches(".*start_game.*"));
		Mockito.verify(spy3).handleServerResponse(Mockito.matches(".*start_game.*"));
		Mockito.verify(spy4).handleServerResponse(Mockito.matches(".*start_game.*"));
		Mockito.verify(spy5).handleServerResponse(Mockito.matches(".*start_game.*"));

		assertEquals(server.getNumClients(), 5);

		// Reject all players after this
		AppClient clientX = new AppClient(viewFactory, Config.DEFAULT_SERVER_ADDRESS, SERVER_PORT);
		AppClient spyX = Mockito.spy(clientX);
		spyX.setUsername("MP");
		assertTrue(spyX.connect());

		Thread.sleep(WAIT_TIME_MILLIS);
		assertFalse(server.isPlayerRegistered("MP"));
		Mockito.verify(spyX).handleServerResponse(Mockito.matches(".*connection_rejected.*"));
		assertEquals(server.getNumClients(), 5);
	}

}
