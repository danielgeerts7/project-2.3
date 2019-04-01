package Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import Main.Main;
import Model.Config;
import View.Popup;

/**
* ClientSockerController connects with the server
* Trough the extension ClientCommandHandler, this class can call Commands
* And can handle Server requests
*
* @author  Daniël Geerts
* @since   2019-03-28
*/
public final class ClientSocketController extends ClientCommandHandler {

	public Socket socket = null;
	private boolean msgReceived = false;
	private String msgData = "";
	private Thread runningThread = null;

	private boolean stayConnected = true;

	private static ClientSocketController instance = null;
	private static boolean popupOpen = false;

	/*
	 * getInstance(), else create a error popup
	 */
	public static ClientSocketController getInstance(boolean trySetInstance) {
		if (instance == null && trySetInstance) {
			try {
				instance = new ClientSocketController(Config.REMOTE_IP, Config.REMOTE_PORT);
			} catch (UnknownHostException e) {
				if (!popupOpen) {
					Popup.getInstance().newPopup("Ongeldig IP adres", Popup.Type.OK);
					popupOpen = true;
				}
			} catch (Exception e) {
				if (!popupOpen) {
					Popup.getInstance().newPopup("Kan geen verbinding met Server maken", Popup.Type.OK);
					popupOpen = true;
					Main.switchScene(Main.SceneType.START);
					// is this the way to go?? ^^
				}
			}
			return instance;
		}
		popupOpen = false;
		return instance;
	}

	/*
	 * Constructor
	 */
	public ClientSocketController(String remoteIp, int serverPort) throws Exception {
		this.socket = new Socket(remoteIp, serverPort);

		// New Thread for receiving input coming from the Server
		runningThread = new Thread() {
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

		runningThread.start();

		System.out.println("Connected to Server: " + this.socket.getInetAddress());
	}

	@Override
	protected void sendMessageToServer(String msg) {
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
	
	@Override
	protected void waitForResponse() {

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

	@Override
	protected String getMsgData() {
		return msgData;
	}

	private void readServerInput() throws IOException {
		msgReceived = false;
		String data = null;
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		try {
			while ((data = in.readLine()) != null) {
				System.out.println("Message from server: " + data);
				msgReceived = true;
				msgData = data;
				if (popupOpen && data.contains("ERR")) {
					Popup.getInstance().newPopup(data, Popup.Type.OK);
				}

				super.doCommand(data);
			}

		} catch (Exception e) {
			System.out.println(e);
			this.disconnect();
		}
	}

	public void disconnect() {
		super.logoutFromServer();

		stayConnected = false;
		if (runningThread != null) {
			runningThread.interrupt();
		}
		runningThread = null;

		try {
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		socket = null;

		Main.QuitApp();
	}
}