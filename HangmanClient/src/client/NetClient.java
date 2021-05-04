package client;

import javax.swing.JFrame;

import hangman.Game;

import java.awt.*;
import java.io.*;
import java.net.*;

import javax.swing.*;

public class NetClient extends JFrame{
	
	public static void main(String[] args) {
		JLabel hangedMan, word;
		JTextField attemptInput = new JTextField();
		
		hangedMan = new JLabel(gameRepresentation(0));
		
		BufferedReader in = null;
		PrintWriter out = null;
		
		try {
			Socket socket = new Socket("localhost", 8888);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(),true);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String message = null;
		try {
			message = in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		word = new JLabel(message.split(",")[1]);
		
		
		NetClient gui = new NetClient();
		Container c = gui.getContentPane();
		c.setLayout(new BorderLayout());
		c.add(hangedMan, BorderLayout.CENTER);
		c.add(word, BorderLayout.NORTH);
		c.add(attemptInput, BorderLayout.SOUTH);
		
		gui.setVisible(true);
		gui.setDefaultCloseOperation(EXIT_ON_CLOSE);
		gui.setLocationRelativeTo(null);
	}
	
	private static String gameRepresentation(int fails) {
        String s = "   ___________\n  /       |   \n  |       ";
        s += (fails == 0 ? "\n" : "O\n");
        s += "  |     " + (fails== 0 ? "\n" : (fails < 5
                ? "  +\n"
                : (fails == 5 ? "--+\n" : "--+--\n")));
        s += "  |       " + (fails < 2 ? "\n" : "|\n");
        s += "  |      " + (fails < 3 ? "\n" : (fails == 3 ? "/\n" : "/ \\\n"));
        s += "  |\n================\n";
        return s;
    }
	
}
