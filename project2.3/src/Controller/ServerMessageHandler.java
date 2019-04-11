package Controller;

import java.util.HashMap;
import java.util.Map;

import Main.Main;
import View.GameView;
import View.Popup;
import View.Popup.PopupYesNo;
import javafx.application.Platform;

/**
 * ServerMessageHandler handles every incoming server request This needs to be a
 * very dynamic class (that means that the request can happen at any time)
 *
 * @author Daniel Geerts
 * @since 2019-03-31
 */
public abstract class ServerMessageHandler {

	private Map<String, PopupYesNo> challenges = null;

	public ServerMessageHandler() {
		challenges = new HashMap<String, PopupYesNo>();
	}

	/**
	 * Handles every command that the server has send
	 * @param command is the message received from the server
	 */
	protected void doCommand(String command) {
		if (command.contains("SVR") && !command.contains("SVR GAMELIST") && !command.contains("SVR GAMELIST")) {
			if (command.contains("SVR GAME")) {
				if (command.contains("SVR GAME MATCH")) {
					createMatch(command);
				} else if (command.contains("SVR GAME YOURTURN")) {
					yourTurn(command);
				} else if (command.contains("SVR GAME MOVE")) {
					receivedMove(command);
				} else if (command.contains("SVR GAME CHALLENGE CANCELLED")) {
					challengeCancelled(command);
				} else if (command.contains("SVR GAME CHALLENGE")) {
					gotChallenged(command);
				} else if (command.contains("SVR GAME WIN")) {
					gameFinished(command, Popup.Type.WIN);
				} else if (command.contains("SVR GAME LOSS")) {
					gameFinished(command, Popup.Type.LOSS);
				} else if (command.contains("SVR GAME DRAW")) {
					gameFinished(command, Popup.Type.DRAW);
				} else {
					Popup.getInstance().newPopup("Error! Sever command unknown", Popup.Type.DEBUG);
				}
			}

			if (command.contains("SVR HELP")) {
				// Show all help information to client
				if (!getMsgData().trim().equals("SVR HELP") && !getMsgData().contains("Available commands")
						&& !getMsgData().contains("Help information for Strategic Game Server Fixed")
						&& !getMsgData().contains("For more information on a command")) {
					Popup.getInstance().newPopup(getMsgData().replace("SVR HELP", "").replace("  ", "").trim(),
							Popup.Type.OK);
				}
			}
		}
	}

	/**
	 * Create a new match -> load new Scene for a game
	 * @param message from server "SVR GAME MATCh"
	 */
	private void createMatch(String msg) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				HashMap<String, String> map = svrMessageToMap(msg);
				String gametype = map.get("GAMETYPE");

				if (gametype.toLowerCase().contains("reversi")) {
					Main.switchScene(Main.SceneType.REVERSI, true);
				} else if (gametype.toLowerCase().contains("tic-tac-toe")) {
					Main.switchScene(Main.SceneType.TICTACTOE, true);
				}
				GameView.updateSuperView(map);
				System.out.println("Match is created!");
			}
		});
	}

	/**
	 * Your turn, first wait until the game is created then it is time to do a move
	 * @param message from server "SVR GAME YOURTURN"
	 */
	private void yourTurn(String msg) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				while (!GameView.isCreated()) {
					try {
						Thread.sleep(2);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				HashMap<String, String> map = svrMessageToMap(msg);
				String turnmsg = map.get("TURNMESSAGE");
				GameController.doMove();
			}
		});
	}

	/**
	 * Received move from server (this can be from you or your opponent)
	 * @param message from server "SVR GAME MOVE"
	 */
	private void receivedMove(String msg) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				HashMap<String, String> map = svrMessageToMap(msg);
				String player = map.get("PLAYER");
				String move = map.get("MOVE");
				GameController.receivedMove(player, move);
			}
		});
	}

	/**
	 * You got challenged by a player
	 * Open a popup with YES and NO. So you can accept of ignore
	 * @param message from server "SVR GAME CHALLENGE"
	 */
	private void gotChallenged(String msg) {
		HashMap<String, String> map = svrMessageToMap(msg);

		String challenger = map.get("CHALLENGER");
		String gameType = map.get("GAMETYPE");
		String challengeNr = map.get("CHALLENGENUMBER");
		PopupYesNo popup = new PopupYesNo() {
			@Override
			public void clickedYes() {
				// Our client has accepted the challenge
				ClientSocket.getInstance(false).acceptChallenge(challengeNr);
				challenges.remove(challengeNr);
			}

			@Override
			public void clickedNo() {
				// Our client declined the challenge
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						Popup.getInstance().popups.get(challengeNr).close();
						Popup.getInstance().popups.remove(challengeNr);
						challenges.remove(challengeNr);
					}
				});
			}
		};
		String popup_msg = "You got challenged(" + challengeNr + ") for " + gameType + ", by : " + challenger;
		Popup.getInstance().newPopup(popup_msg, Popup.Type.YESNO, popup, challengeNr);
		challenges.put(challengeNr, popup);
	}

	/**
	 * Challenge got cancelled by player. Close the right popup
	 * @param message from server "SVR GAME CHALLENGE CANCELLED"
	 */
	private void challengeCancelled(String msg) {
		HashMap<String, String> map = svrMessageToMap(msg);
		String challengeNr = map.get("CHALLENGENUMBER");
		if (challenges.containsKey(challengeNr)) {
			challenges.get(challengeNr).clickedNo();
			challenges.remove(challengeNr);
		}
	}

	/**
	 * Game is finished, open popup with result
	 * @param message from server with game details
	 * @param result type of result of game
	 */
	private void gameFinished(String msg, Popup.Type result) {
		HashMap<String, String> map = svrMessageToMap(msg);
		String p1score = map.get("PLAYERONESCORE");
		String p2score = map.get("PLAYERTWOSCORE");
		String comment = map.get("COMMENT");
		String temp = "You " + result.toString() + "!" + "\n" + "score P1: " + p1score + "\n" + "score P2: "
				+ p2score + "\n" + "Comment: " + comment;
		Popup.getInstance().newPopup(temp, result);
	}

	/**
	 * Converts a server message to a HashMap
	 * @param message the message you want to convert
	 * @return the result map of the converted string
	 */
	private HashMap<String, String> svrMessageToMap(String msg) {
		String server_msg = msg.substring(msg.indexOf('{') + 1, msg.indexOf('}'));
		HashMap<String, String> map = new HashMap<String, String>();
		String[] list = server_msg.split(",");
		for (int i = 0; i < list.length; i++) {
			list[i] = list[i].trim();
			String[] keyvalue = list[i].split(":");
			map.put(keyvalue[0].trim(), keyvalue[1].replace("\"", "").trim());
		}
		return map;
	}
	
	protected abstract boolean waitForResponse(boolean skipOK);

	protected abstract String getMsgData();

}