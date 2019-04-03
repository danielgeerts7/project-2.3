package Controller;

import java.util.HashMap;
import java.util.Map;

import View.Popup;
import View.Popup.PopupYesNo;

/**
 * ServerMessageHandler handles every incoming server request This needs to be a
 * very dynamic class (that means that the request can happen at any time)
 *
 * @author Daniël Geerts
 * @since 2019-03-31
 */
abstract class ServerMessageHandler {

	private Map<String, PopupYesNo> challenges = null;

	public ServerMessageHandler() {
		challenges = new HashMap<String, PopupYesNo>();
	}

	protected void doCommand(String command) {
		if (command.contains("SVR") 
				&& !command.contains("SVR GAMELIST")
				&& !command.contains("SVR GAMELIST")) {
			if (command.contains("SVR GAME")) {
				if (command.contains("SVR GAME MATCH")) {
					// Server send you a match with another player
					// TODO: react on this
					Popup.getInstance().newPopup("START MATCH NOW! new GameView();", Popup.Type.DEBUG);
				} else if (command.contains("SVR GAME YOURTURN")) {
					// It is your turn in the game
					// TODO: react on this
					Popup.getInstance().newPopup("Your turn! Send to the new GameView();", Popup.Type.DEBUG);
				} else if (command.contains("SVR GAME MOVE")) {
					// You or your opponent has made its move
					// TODO: react on this
					Popup.getInstance().newPopup("Received move from you or your opponent!", Popup.Type.DEBUG);
				} else if (command.contains("SVR GAME CHALLENGE CANCELLED")) {
					challengeCancelled(command);
				} else if (command.contains("SVR GAME CHALLENGE")) {
					gotChallenged(command);
				} else if (command.contains("WIN")) {
					// Match is over. Implement every outcome. There are 3
					// if (command.contains("SVR GAME WIN"))
					Popup.getInstance().newPopup("You won, winner!", Popup.Type.WIN);
				} else if (command.contains("LOSS")) {
					// Match is over. Implement every outcome. There are 3
					// if (command.contains("SVR GAME WIN"))
					Popup.getInstance().newPopup("You have lost, loser!", Popup.Type.LOSS);
				} else if (command.contains("DRAW")) {
					// Match is over. Implement every outcome. There are 3
					// if (command.contains("SVR GAME WIN"))
					Popup.getInstance().newPopup("You have played a draw!", Popup.Type.DRAW);
				} else {
					// Match is over. Implement every outcome. There are 3
					// if (command.contains("SVR GAME WIN"))
					Popup.getInstance().newPopup("Error! Sever command unknown", Popup.Type.DEBUG);
				}
			}

			if (command.contains("SVR HELP")) {
				// Show all help information to client
				if (!getMsgData().trim().equals("SVR HELP")
						&& !getMsgData().contains("Available commands")
						&& !getMsgData().contains("Help information for Strategic Game Server Fixed")
						&& !getMsgData().contains("For more information on a command")) {
					Popup.getInstance().newPopup(getMsgData().replace("SVR HELP", "").replace("  ", "").trim(), Popup.Type.OK);
				}
			}
		}
	}

	private void gotChallenged(String msg) {
		String server_msg = msg.substring(msg.indexOf('{'), msg.indexOf('}'));
		HashMap<String, String> map = stringToMap(server_msg);

		String challenger = map.get("CHALLENGER");
		String gameType = map.get("GAMETYPE");
		String challengeNr = map.get("CHALLENGENUMBER");
		PopupYesNo popup = new PopupYesNo() {
			@Override
			public void clickedYes() {
				// Our client has accepted the challenge
				SocketController.getInstance(true).acceptChallenge(challengeNr);
				challenges.remove(challengeNr);
			}

			@Override
			public void clickedNo() {
				// Our client declined the challenge
				challenges.remove(challengeNr);
			}
		};
		String popup_msg = "You got challenged(" + challengeNr + ") for " + gameType + ", by : " + challenger;
		Popup.getInstance().newPopup(popup_msg, Popup.Type.YESNO, popup);
		challenges.put(challengeNr, popup);
	}

	private void challengeCancelled(String msg) {
		String temp = msg.substring(msg.indexOf('{'), msg.indexOf('}'));
		HashMap<String, String> map = stringToMap(temp);
		String challengeNr = map.get("CHALLENGENUMBER");
		Popup.getInstance().newPopup("Challenge(" + challengeNr + ") got cancelled", Popup.Type.YESNO);

		if (challenges.containsKey(challengeNr)) {
			challenges.get(challengeNr).clickedNo();
			challenges.remove(challengeNr);
		}
	}

	protected HashMap<String, String> stringToMap(String s) {
		HashMap<String, String> map = new HashMap<String, String>();
		String[] list = s.split(",");
		for (int i = 0; i < list.length; i++) {
			list[i] = list[i].replace("\"", "").replace("{", "").replace("}", "").trim();
			System.out.println("l: " + list[i]);
			String[] keyvalue = list[i].split(":");
			map.put(keyvalue[0].trim(), keyvalue[1].trim());
		}
		return map;
	}

	/*
	 * Wait for server to send response
	 * 
	 * @param skipOK when message from Server contains OK, then wait for next
	 * message
	 */
	protected abstract boolean waitForResponse(boolean skipOK);

	protected abstract String getMsgData();

}