package net;
import java.io.*;
import java.net.Socket;

import console.ConnectionIOException;
import console.NetPlayer;
import hangman.Hangman;

public class ClientHandler extends Thread{
	private Socket socket = null;
	private PrintWriter out = null;
	private BufferedReader in = null;
	
	private NetPlayer player = null;
	private Hangman game = null;
	
	private int clientId;
	
	public ClientHandler(Socket socket, int clientId) {
		this.socket = socket;
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.player = new NetPlayer(in, out);
		this.game = new Hangman();
		this.clientId = clientId;
	}
	
	public void run() {
		System.out.println("Client " + clientId + ": A new player has started playing.");
		try {
			game.playGame(player);
			System.out.println("Client " + clientId + ": Player has finished playing.");
		} catch (ConnectionIOException e) {
			
		}
		try {
			in.close();
			out.close();
			socket.close();
			System.out.println("Client " + clientId + ": Player disconnected.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
