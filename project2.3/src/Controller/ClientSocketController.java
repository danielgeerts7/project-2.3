package Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import Main.Main;
import View.Popup;

public final class ClientSocketController {

	public Socket socket = null;
	private boolean msgReceived = false;
	private String msgData = "";

	private boolean stayConnected = true;

	private static ClientSocketController instance = null;

	public static ClientSocketController getInstance() {
		if (instance == null) {
			try {
				instance = new ClientSocketController(InetAddress.getLocalHost(), 7789);
			} catch (UnknownHostException e) {
				Popup.getInstance().newPopup("Ongeldig IP adres", Popup.Type.ERROR);
			} catch (Exception e) {
				Popup.getInstance().newPopup("Kan geen verbinding met Server maken", Popup.Type.ERROR);
			}
		}

		return instance;
	}

	public ClientSocketController(InetAddress serverAddress, int serverPort) throws Exception {
		this.socket = new Socket(serverAddress, serverPort);

		// New Thread for receiving input coming from the Server
		Thread thread = new Thread() {
			public void run() {
				while (stayConnected) {
					try {
						readServerInput();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		};

		thread.start();

		System.out.println("Connected to Server: " + this.socket.getInetAddress());
	}

	private void sendMessageToServer(String msg) {
		PrintWriter out = null;
		try {
			out = new PrintWriter(this.socket.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		out.println(msg);
		out.flush();
		System.out.println("Client send: " + msg);
	}

	private void readServerInput() throws IOException {
		msgReceived = false;
		String data = null;
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		while ((data = in.readLine()) != null) {
			System.out.println("Message from server: " + data);
			msgReceived = true;
			msgData = data;
			if (data.contains("ERR")) {
				Popup.getInstance().newPopup(data, Popup.Type.ERROR);
			}
		}
	}

	/*
	 * Tries to login onto the server
	 * 
	 * @param Return True when succeeded, False when failed
	 */
	public boolean loginOnServer(String name) {
		this.sendMessageToServer("login " + name);
		this.waitForResponse();

		if (msgData.contains("OK")) {
			return true;
		} else {
			Popup.getInstance().newPopup(msgData, Popup.Type.ERROR);
			return false;
		}
	}

	public void logoutFromServer() {
		this.sendMessageToServer("logout");
	}

	public String[] getGamelist() {
		this.sendMessageToServer("get gamelist");
		this.waitForResponse();

		if (msgData.contains("SVR GAMELIST")) {
			String games = msgData.substring(msgData.indexOf('['), msgData.indexOf(']'));
			String[] availableGames = games.split(",");
			int i = 0;
			for (String game : availableGames) {
				availableGames[i] = game.replace("\"", "").replace(" ", "").replace("]", "").replace("[", "");
				i++;
			}
			return availableGames;
		} else {
			Popup.getInstance().newPopup(msgData, Popup.Type.DEBUG);
			return null;
		}
	}

	public boolean selectGame(String gameName) {
		this.sendMessageToServer("subscribe " + gameName);
		this.waitForResponse();

		if (msgData.contains("OK")) {
			return true;
		} else {
			Popup.getInstance().newPopup(msgData, Popup.Type.DEBUG);
			return false;
		}
	}

	private void waitForResponse() {

		// TODO: change to callback instead of while-loop, this is bad for main Thread

		msgReceived = false;
		if (this.socket.isConnected()) {
			while (msgReceived == false) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} else {
			msgReceived = true;
			msgData = "";
		}
	}

	private void executeConnection() {
		logoutFromServer();

		try {
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		stayConnected = false;
		Main.QuitApp();
	}
}
