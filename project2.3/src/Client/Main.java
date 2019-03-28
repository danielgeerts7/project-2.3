package Client;

import java.net.InetAddress;

public class Main {

	public static void main(String[] args) throws Exception {
		ClientSocket client = new ClientSocket(InetAddress.getLocalHost(), 7789);//InetAddress.getByName(args[0]), Integer.parseInt(args[1]));

		System.out.println("Connected to Server: " + client.socket.getInetAddress());
		
		client.sendInputMessage();
	}
}
