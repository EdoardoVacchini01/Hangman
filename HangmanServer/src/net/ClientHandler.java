package net;
import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread{
	private Socket socket;
	private PrintWriter out = null;
	private BufferedReader in = null;
	private String message = new String();
	
	public ClientHandler(Socket socket) {
		this.socket = socket;
	}
	
	public void run() {
		try {
			out = new PrintWriter(socket.getOutputStream(),true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	    	message = in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		out.println(message);
		try {
			in.close();
			out.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
