package Controller;

import View.Popup;

public abstract class ClientCommandHandler extends ServerMessageHandler {
	
	/*
	 * Tries to login onto the server
	 * 
	 * @param Return True when succeeded, False when failed
	 */
	public boolean loginOnServer(String name) {
		this.sendMessageToServer("login " + name);
		this.waitForResponse();

		if (getMsgData().contains("OK")) {
			return true;
		} else {
			Popup.getInstance().newPopup(getMsgData(), Popup.Type.ERROR);
			return false;
		}
	}

	public void logoutFromServer() {
		this.sendMessageToServer("logout");
	}

	public String[] getGamelist() {
		this.sendMessageToServer("get gamelist");
		this.waitForResponse();

		if (getMsgData().contains("SVR GAMELIST")) {
			String games = getMsgData().substring(getMsgData().indexOf('['), getMsgData().indexOf(']'));
			String[] availableGames = games.split(",");
			int i = 0;
			for (String game : availableGames) {
				availableGames[i] = game.replace("\"", "").replace(" ", "").replace("]", "").replace("[", "");
				i++;
			}
			return availableGames;
		} else {
			Popup.getInstance().newPopup(getMsgData(), Popup.Type.DEBUG);
			return null;
		}
	}

	public boolean selectGame(String gameName) {
		this.sendMessageToServer("subscribe " + gameName);
		this.waitForResponse();

		if (getMsgData().contains("OK")) {
			return true;
		} else {
			Popup.getInstance().newPopup(getMsgData(), Popup.Type.DEBUG);
			return false;
		}
	}

	public boolean challengeOpponent(String opponentName, String gameName) {
		this.sendMessageToServer("challenge " + opponentName + " " + gameName);
		this.waitForResponse();

		if (getMsgData().contains("OK")) {
			return true;
		} else {
			Popup.getInstance().newPopup(getMsgData(), Popup.Type.DEBUG);
			return false;
		}
	}

	public String[] getPlayerlist() {
		this.sendMessageToServer("get playerlist");
		this.waitForResponse();

		if (getMsgData().contains("OK")) {
			String games = getMsgData().substring(getMsgData().indexOf('['), getMsgData().indexOf(']'));
			String[] availableGames = games.split(",");
			int i = 0;
			for (String game : availableGames) {
				availableGames[i] = game.replace("\"", "").replace(" ", "").replace("]", "").replace("[", "");
				i++;
			}
			return availableGames;
		} else {
			Popup.getInstance().newPopup(getMsgData(), Popup.Type.DEBUG);
			return null;
		}
	}

	public void forfeit() {
		this.sendMessageToServer("forfeit");
		this.waitForResponse();

		if (getMsgData().contains("OK")) {
			// End game - u lose!
			Popup.getInstance().newPopup("Forfeit not implemented", Popup.Type.DEBUG);
		} else {
			Popup.getInstance().newPopup(getMsgData(), Popup.Type.DEBUG);
		}
	}
	
	protected abstract void sendMessageToServer(String msg);

	protected abstract void waitForResponse();
	
	protected abstract String getMsgData();
	
}

/*
 * Server message handler Class
 */
class ServerMessageHandler {
	
	protected void doCommand(String command) {
		if (command.contains("SVR")) {
			if (command.contains("SVR GAME")) {
				if (command.contains("SVR GAME MATCH")) {
					// Server send you a match with another player
					// TODO: react on this
				} else if (command.contains("SVR GAME YOURTURN")) {
					// It is your turn in the game
					// TODO: react on this
				} else if (command.contains("SVR GAME MOVE")) {
					// Your opponent has made its move
					// TODO: react on this
				} else if (command.contains("SVR GAME CHALLENGE")) {
					// Accept incoming challenge
				} else if (command.contains("SVR GAME CHALLENGE CANCELLED")) {
					// Challenge is cancelled by opponent
				} else {
					// Match is over. Implement every outcome. There are 3
					// if (command.contains("SVR GAME WIN"))
				}
			}

			if (command.contains("SVR HELP")) {
				// Show all help information to client
			}
		}
	}
}
