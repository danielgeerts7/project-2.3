package Controller;

import View.Popup;

/**
 * ClientCommandHandler handles every command this client can do The extension
 * handles every incoming server Message (server request)
 *
 * @author Daniel Geerts
 * @since 2019-03-31
 */
public abstract class CommandHandler extends ServerMessageHandler {

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
			Popup.getInstance().newPopup("Failed to login\n"+msg, Popup.Type.DEBUG);
			return -1;
		}
	}

	public void logoutFromServer() {
		this.sendMessageToServer("logout");
	}
	
	public void disconnectFromServer() {
		this.sendMessageToServer("disconnect");
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
			Popup.getInstance().newPopup("Failed to get gamelist\n"+msg, Popup.Type.DEBUG);
			return null;
		}
	}

	public void selectGame(String gameName) {
		this.sendMessageToServer("subscribe " + gameName);
		this.waitForResponse(false);

		String msg = getMsgData();
		if (!msg.contains("OK") && !msg.contains("SVR GAMELIST")) {
			Popup.getInstance().newPopup("Failed to select game\n"+msg, Popup.Type.DEBUG);
		}
	}

	public boolean doMove(String pos) {
		this.sendMessageToServer("move " + pos);
		this.waitForResponse(false);

		String msg = getMsgData();
		if (msg.contains("OK")) {
			return true;
		} else {
			System.out.println("Invalid play from our Client");
			Popup.getInstance().newPopup("Failed to move\n"+msg, Popup.Type.DEBUG);
			return false;
		}
	}

	public void challengeOpponent(String opponentName, String gameName) {
		this.sendMessageToServer("challenge " + "\"" + opponentName + "\" \"" + gameName + "\"");
		this.waitForResponse(false);

		String msg = getMsgData();
		if (!msg.contains("OK") && !msg.contains("SVR GAME")) {
			Popup.getInstance().newPopup("Failed to challenge opponent\n"+msg, Popup.Type.DEBUG);
		}
	}

	public void acceptChallenge(String challengeNr) {
		this.sendMessageToServer("challenge accept " + challengeNr);
		this.waitForResponse(false);

		String msg = getMsgData();
		System.out.println("acc" + msg );
		if (!msg.contains("OK") && !msg.contains("SVR GAME")) {
			Popup.getInstance().newPopup("Failed to accept challenge\n"+msg, Popup.Type.DEBUG);
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
			Popup.getInstance().newPopup("Failed to get playerlist\n"+msg, Popup.Type.DEBUG);
			return new String[0];
		}
	}

	public void help() {
		this.sendMessageToServer("help");
	}

	public void forfeit() {
		this.sendMessageToServer("forfeit");
		this.waitForResponse(false);

		String msg = getMsgData();
		if (!msg.contains("OK") && !msg.contains("SVR GAME")) {
			Popup.getInstance().newPopup("Failed to forfeit\n"+msg, Popup.Type.DEBUG);
		}
	}

	protected abstract void sendMessageToServer(String msg);

	protected abstract void readServerInput();

}
