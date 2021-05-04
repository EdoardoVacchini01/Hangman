package console;
import hangman.Game;
import hangman.GameResult;

import java.io.*;

public class NetPlayer extends LocalPlayer{

    private BufferedReader in;
    private PrintWriter out;
    
    public NetPlayer(BufferedReader in, PrintWriter out) {
		this.in = in;
		this.out = out;
    }
    
    @Override
    public void update(Game game) {
        switch(game.getResult()) {
            case FAILED:
                out.println("" + GameResult.FAILED + "," + game.getSecretWord());
                break;
            case SOLVED:
            	out.println("" + GameResult.SOLVED + "," + game.getSecretWord());
                break;
            case OPEN:
                int rem = Game.MAX_FAILED_ATTEMPTS - game.countFailedAttempts();
                out.println("" + GameResult.OPEN + "," + game.getKnownLetters() + "," + rem);
                break;
        }
    }

    /**
     * Ask the user to guess a letter.
     * 
     * @param game
     * @return
     */
    @Override
    public char chooseLetter(Game game) {
        for (;;) {
            out.println("WAITING_FOR_ATTEMPT");
            String line = null;
            try {
                line = in.readLine().split(",")[1].trim();
            } catch (IOException e) {
                line = "";
            }
            if (line.length() == 1 && Character.isLetter(line.charAt(0))) 
                return line.charAt(0);
        }
    }

}
