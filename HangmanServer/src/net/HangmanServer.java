/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

/**
 *
 * @author Claudio Cusano <claudio.cusano@unipv.it>
 */
public class HangmanServer {
    /**
     * @param args the command line arguments
     */
    @SuppressWarnings("resource")
	public static void main(String[] args) {
    	int port = 8888;
    	ServerSocket serverSocket = null;
    	Socket socket = null;
    	
    	
    	try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    	
		while(true) {
	    	try {
				socket = serverSocket.accept();
		    	ClientHandler clientHandler = new ClientHandler(socket);
				clientHandler.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
    }
    
}
