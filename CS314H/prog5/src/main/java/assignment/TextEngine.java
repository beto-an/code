package assignment;

import java.awt.*;
import java.io.*;
import java.util.HashSet;
import java.util.List;

// The UI Engine that displays a game of Boggle by reading in text streams and outputting gameplay as text.
public class TextEngine implements BoggleEngine {

    // the BufferedReader that reads from the InputStream
    private BufferedReader in;

    // The InputStream to read in from
    private InputStream inputStream;

    // The PrintStream that writes to the OutputStream
    private PrintStream out;

    // The OutputStream to write to
    private OutputStream outputStream;
    private static final String SEPARATOR = "--------------------";

    //The GameManager that the TextEngine is using
    private GameManager game;

    /**
     * Reprints the Boggle Board
     * @param highlightedPoints The points to highlight as selected
     */
    public void redraw(List<Point> highlightedPoints) {
        HashSet<Point> points = new HashSet<>();
        if (highlightedPoints != null) {
            points.addAll(highlightedPoints);
        }
        out.println();
        out.println(SEPARATOR);

        char[][] currBoard = game.getBoard();

        // Prints the board with each row as a line
        for (int i = 0; i < currBoard.length; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < currBoard[i].length; j++) {
                if (points.contains(new Point(i, j))) sb.append(" <").append(currBoard[i][j]).append("> ");  // Highlighted point
                else sb.append("  ").append(currBoard[i][j]).append("  ");  // Un-highlighted point
            }
            out.println(sb);
            out.println(SEPARATOR);
        }
        out.println();
    }

    /**
     * Prompts the user with a message and two options
     * @param initialPromptMsg The prompt description
     * @param option1 The first prompt option
     * @param option2 The second prompt option
     * @param errorMsg Error Message displayed if neither options are selected
     * @return The result of the prompt
     * @throws IOException if the I/O fails
     */
    private String prompt(String initialPromptMsg, String option1, String option2, String errorMsg) throws IOException {
        String s = "";
        out.println(initialPromptMsg);
        while (!s.equals(option1) && !s.equals(option2)) {
            s = in.readLine();
            if (!s.equals(option1) && !s.equals(option2)) out.println(errorMsg);
        }
        return s;
    }

    /**
     * Checks if the game is still running
     * @return true if the game is still running, false otherwise
     */
    @Override
    public boolean isGameRunning() {
        return true;
    }

    /**
     * Prompts a word to be entered
     * @return the word that was entered
     * @throws IOException if the I/O fails
     */
    @Override
    public String promptNextWord() throws IOException {
        out.print("Please enter your next word (q to quit): ");
        return in.readLine();
    }

    /**
     * Displays the error message if an invalid word was given
     * @param word the word that was invalid
     */
    @Override
    public void invalidWord(String word) {
        out.println(game.getErrorMessage(word, 0));
    }

    /**
     * Initializes the Engine
     */
    @Override
    public void startGame() {
        in = new BufferedReader(new InputStreamReader(inputStream));
        out = new PrintStream(outputStream);
        out.println("Welcome to Boggle!");
    }

    /**
     * Checks if the user wants to continue to play the game
     * @return true if the user wants to continue to play the game, false otherwise
     * @throws IOException if the I/O fails
     */
    @Override
    public boolean replay() throws IOException {
        String s = prompt("Play again? (y/n)", "y", "n", "Please enter either 'y' or 'n'");
        if (s.equals("n")) {
            System.out.println("Thanks for playing!");
            return false;
        }
        return true;
    }

    /**
     * Displays the statistics when the user finished the Boggle Game
     */
    @Override
    public void endGame() {
        out.println("Here are the words you found: " + game.getFoundWords(0));
        out.println("Your score: " + game.getScore(0));
        out.println("Here are the words you did not find: " + game.getComputerWords(0));
        out.println("Computer score: " + game.getComputerScore(0));
    }

    /**
     * Sets the GameManager of the engine
     * @param game the GameManager of the engine
     */
    public void setGame(GameManager game) {
        this.game = game;
    }

    /**
     * Sets the InputStream of the engine
     * @param inputStream the InputStream of the engine
     */
    @Override
    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    /**
     * Sets the OutputStream of the engine
     * @param outputStream the OutputStream of the engine
     */
    @Override
    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    /**
     * Closes the streams for the UI
     * @throws IOException if the I/O fails
     */
    @Override
    public void closeStreams() throws IOException {
        in.close();
        out.close();
    }


}
