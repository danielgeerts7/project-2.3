package Controller;

import java.util.ArrayList;
import java.util.List;

import Model.Client;
import View.Popup;

/**
 * ClientCommandHandler handles every command this client can do The extension
 * handles every incoming server Message (server request)
 *
 * @author Daniel Geerts
 * @since 2019-03-31
 */
public abstract class CommandHandler extends ServerMessageHandler {

	/**
	 * Try to login onto the server
	 * 
	 * @param name is the username that the client wants to use
	 * @return int for result
	 */
	public int loginOnServer(String name) {
		this.sendMessageToServer("login " + name);
		this.waitForResponse(false);

		String msg = getMsgData();
		if (msg.contains("OK")) {
			return 1;
		} else if (msg.contains("ERR Already logged in")) {
			return 0;
		} else {
			Popup.getInstance().newPopup("Failed to login\n" + msg, Popup.Type.DEBUG);
			return -1;
		}
	}

	/**
	 * Logout from server
	 */
	protected void logoutFromServer() {
		this.sendMessageToServer("logout");
	}

	/**
	 * Disconnect from server
	 */
	protected void disconnectFromServer() {
		this.sendMessageToServer("disconnect");
	}

	/**
	 * Get available games from the server
	 * 
	 * @return list of games
	 */
	public String[] getGamelist() {
		this.sendMessageToServer("get gamelist");
		this.waitForResponse(true);

		String msg = getMsgData();
		if (msg.contains("SVR GAMELIST")) {
			String games = msg.substring(msg.indexOf('['), msg.indexOf(']'));
			String[] availableGames = games.split(",");
			int i = 0;
			for (String game : availableGames) {
				availableGames[i] = game.replace("\"", "").replace("]", "").replace("[", "").trim();
				i++;
			}
			return availableGames;
		} else {
			Popup.getInstance().newPopup("Failed to get gamelist\n" + msg, Popup.Type.DEBUG);
			return null;
		}
	}

	/**
	 * Subscribe for a game on the server
	 */
	public void selectGame(String gameName) {
		this.sendMessageToServer("subscribe " + gameName);
	}

	/**
	 * Send a move (made by the client) to the server
	 */
	public void sendMove(int pos) {
		this.sendMessageToServer("move " + pos);
	}

	/**
	 * Challenge a opponent
	 * 
	 * @param opponentName name of the player you want to challenge
	 * @param gameName     game you want to play against this player
	 */
	public void challengeOpponent(String opponentName, String gameName) {
		this.sendMessageToServer("challenge " + "\"" + opponentName + "\" \"" + gameName + "\"");
		this.waitForResponse(false);

		String msg = getMsgData();
		if (!msg.contains("OK") && !msg.contains("SVR GAME")) {
			Popup.getInstance().newPopup("Failed to challenge opponent\n" + msg, Popup.Type.DEBUG);
		}
	}

	/**
	 * Accept a received challenge
	 * 
	 * @param challengeNr is the number of the challenge
	 */
	protected void acceptChallenge(String challengeNr) {
		this.sendMessageToServer("challenge accept " + challengeNr);
		this.waitForResponse(false);

		String msg = getMsgData();
		if (!msg.contains("OK") && !msg.contains("SVR GAME")) {
			Popup.getInstance().newPopup("Failed to accept challenge\n" + msg, Popup.Type.DEBUG);
		}
	}

	/**
	 * Get available players from the server
	 * 
	 * @return list of players
	 */
	public String[] getPlayerlist() {
		this.sendMessageToServer("get playerlist");
		this.waitForResponse(true);

		String msg = getMsgData();
		if (msg.contains("SVR PLAYERLIST")) {
			String players = msg.substring(msg.indexOf('['), msg.indexOf(']'));
			String[] availablePlayers = players.split(",");
			List<String> list = new ArrayList<String>();
			for (String player : availablePlayers) {
				String temp = player.replace("\"", "").replace("]", "").replace("[", "").trim();
				list.add(temp);
			}
			list.remove(Client.getUsername());
			String[] result = new String[list.size()];
			list.toArray(result);
			return result;
		} else {
			Popup.getInstance().newPopup("Failed to get playerlist\n" + msg, Popup.Type.DEBUG);
			return new String[0];
		}
	}

	/**
	 * sends 'help' to the server for all the commands
	 */
	public void help() {
		this.sendMessageToServer("help");
	}

	/**
	 * forfeit the current game
	 */
	public void forfeit() {
		this.sendMessageToServer("forfeit");
		this.waitForResponse(false);

		String msg = getMsgData();
		if (!msg.contains("OK") && !msg.contains("SVR GAME")) {
			Popup.getInstance().newPopup("Failed to forfeit\n" + msg, Popup.Type.DEBUG);
		}
	}

	protected abstract void sendMessageToServer(String msg);

	protected abstract void readServerInput();

}
