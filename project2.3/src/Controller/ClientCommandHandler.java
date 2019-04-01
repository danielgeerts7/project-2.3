package Controller;

import View.Popup;

/**
 * ClientCommandHandler handles every command this client can do The extension
 * handles every incoming server Message (server request)
 *
 * @author Daniël Geerts
 * @since 2019-03-31
 */
public abstract class ClientCommandHandler extends ServerMessageHandler {

	/*
	 * Tries to login onto the server
	 * 
	 * @param Return True when succeeded, False when failed
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
			Popup.getInstance().newPopup(msg, Popup.Type.OK);
			return -1;
		}
	}

	public void logoutFromServer() {
		this.sendMessageToServer("logout");
	}

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
			Popup.getInstance().newPopup(msg, Popup.Type.DEBUG);
			return null;
		}
	}

	public boolean selectGame(String gameName) {
		this.sendMessageToServer("subscribe " + gameName);
		this.waitForResponse(false);

		String msg = getMsgData();
		if (msg.contains("OK")) {
			return true;
		} else {
			Popup.getInstance().newPopup(msg, Popup.Type.DEBUG);
			return false;
		}
	}

	public boolean challengeOpponent(String opponentName, String gameName) {
		this.sendMessageToServer("challenge " + "\"" + opponentName + "\" \"" + gameName + "\"");
		this.waitForResponse(false);

		String msg = getMsgData();
		if (msg.contains("OK")) {
			return true;
		} else {
			Popup.getInstance().newPopup(msg, Popup.Type.DEBUG);
			return false;
		}
	}
	
	public boolean acceptChallenge(String challengeNr) {
		this.sendMessageToServer("challenge accept " + challengeNr);
		this.waitForResponse(false);

		String msg = getMsgData();
		if (msg.contains("OK")) {
			return true;
		} else {
			Popup.getInstance().newPopup(msg, Popup.Type.DEBUG);
			return false;
		}
	}

	public String[] getPlayerlist() {
		this.sendMessageToServer("get playerlist");
		this.waitForResponse(true);

		String msg = getMsgData();
		if (msg.contains("SVR PLAYERLIST")) {
			String players = msg.substring(msg.indexOf('['), msg.indexOf(']'));
			String[] availablePlayers = players.split(",");
			int i = 0;
			for (String game : availablePlayers) {
				availablePlayers[i] = game.replace("\"", "").replace("]", "").replace("[", "").trim();
				i++;
			}
			if (availablePlayers.length <= 1) {
				// Only our client is connected
				return new String[0];
			}
			return availablePlayers;
		} else {
			Popup.getInstance().newPopup(getMsgData(), Popup.Type.DEBUG);
			return new String[0];
		}
	}

	public void forfeit() {
		this.sendMessageToServer("forfeit");
		this.waitForResponse(false);

		String msg = getMsgData();
		if (msg.contains("OK")) {
			// End game - u lose!
			Popup.getInstance().newPopup("Clients forfeit not implemented", Popup.Type.OK);
		} else {
			Popup.getInstance().newPopup(msg, Popup.Type.DEBUG);
		}
	}

	protected abstract void sendMessageToServer(String msg);

	/* Wait for server to send response
	 * @param skipOK when message from Server contains OK, then wait for next message */
	protected abstract void waitForResponse(boolean skipOK);
	protected abstract String getMsgData();
	protected abstract void readServerInput();

}
