package comp3004.ivanhoe.util;

public class Config {
	public static final String DEFAULT_SERVER_ADDRESS = "127.0.0.1";
	public static final int DEFAULT_SERVER_PORT = 10000;
	
	public enum RequestType {
		REGISTER_PLAYER, MAKE_MOVE, QUIT;
	}

	public enum ResponseType {
		CONNECTION_ACCEPTED, CONNECTION_REJECTED,
		UPDATE_VIEW, START_PLAYER_TURN, START_GAME,
		MAKE_MOVE, QUIT;
	}
	
}

