package Model;

public class Client {

	private static String username = "";
	private static String gamePlaying = "";
	
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
}
