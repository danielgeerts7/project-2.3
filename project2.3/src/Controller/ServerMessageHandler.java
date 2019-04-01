package Controller;

import View.Popup;

/**
* ServerMessageHandler handles every incoming server request
* This needs to be a very dynamic class (that means that the request can happen at any time)
*
* @author  Daniël Geerts
* @since   2019-03-31
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
					gotChallenged(command);
				} else if (command.contains("SVR GAME CHALLENGE CANCELLED")) {
					// Challenge is cancelled by opponent
					// Check if nr is still open -> close popup and ignore challenge
					// Al begonnen? Ga dan terug naar 'x' scene
				} else {
					// Match is over. Implement every outcome. There are 3
					// if (command.contains("SVR GAME WIN"))
				}
			}

			if (command.contains("SVR HELP")) {
				// Show all help information to client
				Popup.getInstance().newPopup(command.replace("SVR HELP", ""), Popup.Type.OK);
			}
		}
	}
	
	private void gotChallenged(String msg) {
		msg.subSequence(msg.indexOf('{'), msg.indexOf('}'));
		String byPlayer = "";
		int challengeNr = 0;
		// Split msg for challenge name and number...
		Popup.getInstance().newPopup("You got challenged (" + challengeNr + ") by : " + byPlayer, Popup.Type.YESNO);
	}
}