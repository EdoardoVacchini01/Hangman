package net;
import java.io.*;
import java.net.Socket;

import console.NetPlayer;
import hangman.Hangman;

public class ClientHandler extends Thread{
	private Socket socket = null;
	private PrintWriter out = null;
	private BufferedReader in = null;
	
	private NetPlayer player = null;
	private Hangman game = null;
	
	public ClientHandler(Socket socket) {
		this.socket = socket;
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.player = new NetPlayer(in, out);
		this.game = new Hangman();
	}
	
	public void run() {
		game.playGame(player);
		try {
			in.close();
			out.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
