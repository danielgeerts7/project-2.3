package Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

import Model.Client;
import Model.Config;
import View.Popup;

/**
 * ClientSockerController connects with the server Trough the extension
 * ClientCommandHandler, this class can call Commands And can handle Server
 * requests
 *
 * @author Daniel Geerts
 * @since 2019-03-28
 */
public final class ClientSocket extends CommandHandler {

	private Socket socket = null;

	public Socket getSocket() {
		return socket;
	}

	private boolean msgReceived = false;
	private String msgData = "";
	private Thread runningThread = null;

	private boolean stayConnected = true;

	private static ClientSocket instance = null;

	/**
	 * getInstance(), else create an error popup
	 * 
	 * @author Daniel Geerts
	 */
	public static ClientSocket getInstance(boolean trySetInstance) {
		boolean doReset = false;
		if (instance != null && instance.getSocket() != null) {
			if (!Config.REMOTE_IP.equals(instance.getSocket().getInetAddress().toString().replace("/", "")) || Config.REMOTE_PORT != instance.getSocket().getPort()) {
				doReset = true;
				instance.logout();
				Client.setConnected(false);
				Client.setUsername("");
			}
		}
		if ((instance == null && trySetInstance) || doReset) {
			try {
				instance = new ClientSocket(Config.REMOTE_IP, Config.REMOTE_PORT);
				Client.setConnected(true);
			} catch (UnknownHostException e) {
				Popup.getInstance().newPopup("Ongeldig IP adres", Popup.Type.OK);
				Client.setConnected(false);
			} catch (Exception e) {
				Popup.getInstance().newPopup("Kan geen verbinding met Server maken", Popup.Type.OK);
				Client.setConnected(false);
			}
		}
		return instance;
	}

	/**
	 * setup connection with remote game server
	 * 
	 * @param remoteIP   ip of the server you want to connect with
	 * @param serverPort port of the server
	 * @throws Exception if socket could not connect with server
	 */
	public ClientSocket(String remoteIp, int serverPort) throws Exception {
		this.socket = new Socket(remoteIp, serverPort);

		// New Thread for receiving input coming from the Server
		runningThread = new Thread() {
			public void run() {
				while (stayConnected) {
					readServerInput();
				}
			}
		};

		runningThread.start();

		System.out.println("Connected to Server: " + this.socket.getInetAddress());
	}

	/**
	 * sends a message to the server
	 * 
	 * @param msg the message you want to send
	 * @throws IOException if socket cannot get output stream
	 */
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

	/**
	 * Reads input from server from another Thread (so main thread will not be
	 * blocked)
	 */
	protected void readServerInput() {
		msgReceived = false;
		String data = null;
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			while ((data = in.readLine()) != null) {
				System.out.println("Message from server: " + data);
				msgReceived = true;
				msgData = data;
				/*
				 * if (data.contains("ERR")) { Popup.getInstance().newPopup(data,
				 * Popup.Type.OK); }
				 */

				super.doCommand(data);
			}

		} catch (Exception e) {
			System.out.println("Read svr err: " + e);
			// this.disconnect();
		}
	}

	/**
	 * Wait until the server has send a response, this with a timeout of 3 seconds
	 * 
	 * @param skipOK when received message is OK, then wait again for the next
	 *               received message
	 * @return timedOut is a boolean that returns a true when the 3 seconds are over
	 */
	@Override
	protected boolean waitForResponse(boolean skipOK) {
		// Timer for when the server is not responding within 3 seconds
		boolean timedOut = false;
		long startTime = System.currentTimeMillis();
		long elapsedTime = 0L;

		msgReceived = false;
		if (this.socket.isConnected()) {
			while (!timedOut && !msgReceived) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				elapsedTime = (new Date()).getTime() - startTime;
				if (elapsedTime > 3000) {
					timedOut = true;
					System.out.println("Server timed-out: main-thread running again");
				}
			}

			if (timedOut) {
				System.out.println(" ====== Server timed-out: " + elapsedTime + "ms ======");
			} else {
				System.out.println(" ====== Server responce time: " + elapsedTime + "ms ======");
			}

			if (skipOK && !timedOut && msgData.contains("OK")) {
				waitForResponse(skipOK);
			}
		} else {
			msgReceived = true;
			msgData = "";
		}

		return timedOut;
	}

	/**
	 * @return msgData returns latest message received from the server
	 */
	@Override
	protected String getMsgData() {
		return msgData;
	}

	/**
	 * disconnects from server and resets all that is needed
	 */
	public void disconnect() {
		super.disconnectFromServer();

		resetSocket();
	}

	/**
	 * logs out from server and resets all that is needed
	 */
	public void logout() {
		super.logoutFromServer();

		resetSocket();
	}

	/**
	 * Reset every available variable and closes socket
	 */
	private void resetSocket() {
		stayConnected = false;
		if (runningThread != null) {
			runningThread.interrupt();
		}

		try {
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		runningThread = null;
		socket = null;
		instance = null;
	}
}