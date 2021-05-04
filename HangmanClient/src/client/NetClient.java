package client;

import javax.swing.*;

import hangman.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

@SuppressWarnings("serial")
public class NetClient extends JFrame {

	public NetClient() {
		final JLabel word, hangedMan;
		final JTextField attempt;

		Socket socket = null;
		final BufferedReader in;
		final PrintWriter out;

		String message = null;

		try {
			socket = new Socket("localhost", 8888);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		try {
			message = in.readLine();
			in.readLine(); // read WAITING_FOR_ATTEMPT
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		word = new JLabel(message.split(",")[1], SwingConstants.CENTER);
		hangedMan = new JLabel(
				htmlGameRepresentation(Game.MAX_FAILED_ATTEMPTS - Integer.parseInt(message.split(",")[2])),
				SwingConstants.CENTER);
		attempt = new JTextField();
		
		setTitle("Hangman - " + message.split(",")[2] + " tentativi rimasti");

		word.setFont(new Font("Consolas", Font.BOLD, 35));
		hangedMan.setFont(new Font("Consolas", Font.PLAIN, 20));
		attempt.setHorizontalAlignment(JTextField.CENTER);

		Container contentPane = getContentPane();

		contentPane.add(word, BorderLayout.NORTH);
		contentPane.add(hangedMan, BorderLayout.CENTER);
		contentPane.add(attempt, BorderLayout.SOUTH);

		attempt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				String guess = attempt.getText();

				if (guess.length() != 1) {
					JOptionPane.showMessageDialog(null, "Devi scegliere una e una sola lettera!", "Errore",
							JOptionPane.ERROR_MESSAGE);
				} else {
					out.println("ATTEMPT," + guess);

					String response = null;
					try {
						response = in.readLine();
						in.readLine(); // read WAITING_FOR_ATTEMPT
					} catch (IOException e) {
						e.printStackTrace();
						return;
					}

					String[] splittedResponse = response.split(",");
					if (splittedResponse[0].equals(GameResult.FAILED.name())
							|| splittedResponse[0].equals(GameResult.SOLVED.name())) {
						word.setText(splittedResponse[1]);
						word.setForeground(
								splittedResponse[0].equals(GameResult.FAILED.name()) ? Color.RED : Color.GREEN);
						attempt.removeActionListener(this);
						attempt.setEnabled(false);
						setTitle("Hangman - Gioco terminato!");
					} else if (splittedResponse[0].equals(GameResult.OPEN.name())) {
						word.setText(splittedResponse[1]);
						hangedMan.setText(htmlGameRepresentation(
								Game.MAX_FAILED_ATTEMPTS - Integer.parseInt(splittedResponse[2])));
						setTitle("Hangman - " + splittedResponse[2] + " tentativi rimasti");
					}
				}

				attempt.setText("");
			}
		});
	}

	public static void main(String[] args) {
		NetClient netClient = new NetClient();

		netClient.setDefaultCloseOperation(EXIT_ON_CLOSE);
		netClient.setSize(500, 300);
		netClient.setLocationRelativeTo(null);
		netClient.setVisible(true);
	}

	private static String gameRepresentation(int fails) {
		String s = "   ___________\n  /       |   \n  |       ";
        s += (fails == 0 ? "\n" : "O\n");
        s += "  |     " + (fails == 0 ? "\n" : (fails < 5
                ? "  +\n"
                : (fails == 5 ? "--+\n" : "--+--\n")));
        s += "  |       " + (fails < 2 ? "\n" : "|\n");
        s += "  |      " + (fails < 3 ? "\n" : (fails == 3 ? "/\n" : "/ \\\n"));
        s += "  |\n================\n";
		return s;
	}

	private static String htmlGameRepresentation(int fails) {
		String s = gameRepresentation(fails);

		s = s.replaceAll("<", "&lt;");
		s = s.replaceAll(">", "&gt;");
		s = s.replaceAll("\n", "<br>");
		s = s.replaceAll(" ", "&nbsp;");

		return "<html>" + s + "</html>";
	}

}
