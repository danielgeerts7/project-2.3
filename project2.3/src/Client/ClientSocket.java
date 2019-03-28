package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class ClientSocket {

	public Socket socket = null;
	private Scanner scanner = null;

	private boolean canRun = true;

	public ClientSocket(InetAddress serverAddress, int serverPort) throws Exception {
		this.socket = new Socket(serverAddress, serverPort);
		this.scanner = new Scanner(System.in);

		// New Thread for receiving input coming from the Server
		Thread thread = new Thread() {
			public void run() {
				while (canRun) {
					try {
						readServerInput();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		};

		thread.start();
	}

	public void sendInputMessage() {
		String input = null;
		while (canRun) {
			input = scanner.nextLine();
			PrintWriter out = null;
			try {
				out = new PrintWriter(this.socket.getOutputStream(), true);
			} catch (IOException e) {
				e.printStackTrace();
			}
			out.println(input);
			out.flush();

			if (input.toLowerCase().contains("stop")) {
				executeConnection();
			}
		}
	}

	private void readServerInput() throws IOException {
		String data = null;
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		while ((data = in.readLine()) != null) {
			System.out.println("Message from server: " + data);
		}
	}

	private void executeConnection() {
		try {
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		canRun = false;
		System.out.println("Goodbye! - Terminated");
	}
}
