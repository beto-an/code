package assignment;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;


//Interface for implementing a Boggle game player
public interface BoggleEngine {

    //Update board with points to highlight
    public void redraw(List<Point> highlightedPoints) throws InterruptedException;

    //Check if game is running or not
    public boolean isGameRunning() throws IOException;

    //Prompt user for the next word
    public String promptNextWord() throws IOException, InterruptedException;

    //Check if a word is invalid or not
    public void invalidWord(String word) throws InterruptedException;

    //Start the Boggle game
    public void startGame();

    //Restart the game
    public boolean replay() throws IOException;

    //End the game
    public void endGame();

    //Set the game to use a different GameManager
    public void setGame(GameManager game);

    //Set the input stream
    public void setInputStream(InputStream inputStream);

    //Set the output stream
    public void setOutputStream(OutputStream outputStream);

    //Close both the input and output streams
    public void closeStreams() throws IOException;

}
