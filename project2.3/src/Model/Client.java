package Model;

/**
 * Holds information of the Client
 * 
 * @author Daniel Geerts
 * @since 2019-04-06
 */
public final class Client {

	private static String username = "";
	private static String gamePlaying = "";
	private static boolean isConnected = false;

	private Client() {
	}

	public static String getUsername() {
		return username;
	}

	public static void setUsername(String name) {
		username = name;
	}

	public static String getGame() {
		return gamePlaying;
	}

	public static void setGame(String game) {
		gamePlaying = game;
	}

	public static boolean isConnected() {
		return isConnected;
	}

	public static void setConnected(boolean conn) {
		isConnected = conn;
	}
}
