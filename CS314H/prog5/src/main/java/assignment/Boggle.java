package assignment;

import java.io.IOException;


//Class to run a Boggle game
public class Boggle {


    //The dictionary to be used for Boggle games
    private static BoggleDictionary dict;

    //Global variable determining what search tactic to use
    private static final BoggleGame.SearchTactic SEARCH_TACTIC = BoggleGame.SearchTactic.SEARCH_BOARD;

    //Keep track of what engine to use for running games with
    private static BoggleEngine currentEngine;


    //Initialize the dictionary with a specific words file
    public static BoggleDictionary initDictionary() throws IOException {
        BoggleDictionary dict = new GameDictionary();
        dict.loadDictionary("words.txt");
        return dict;
    }


    //Run an entire Boggle game
    public static void runGame() throws IOException, InterruptedException {

        currentEngine.startGame();

        //Initialize parameters for the game
        GameManager game = new GameManager();
        game.newGame(4, 1, "cubes.txt", dict);
        game.setSearchTactic(SEARCH_TACTIC);
        currentEngine.setGame(game);

        //While the game isn't over, keep prompting the user for inputs and printing the board
        currentEngine.redraw(null);
        while (currentEngine.isGameRunning()) {

            //Prompt for the next word
            String nextWord = currentEngine.promptNextWord();
            if (nextWord.equals("q")) {
                break;
            } else if (game.addWord(nextWord, 0) <= 0) {
                currentEngine.invalidWord(nextWord);
            } else {

                //If word is valid, then update the board
                currentEngine.redraw(game.getLastAddedWord());
                currentEngine.redraw(null);

            }

        }

        currentEngine.endGame();

    }


    //Do one time initializations and keep running the Boggle game while the user wants to play again
    public static void executeGame() throws IOException, InterruptedException {

        dict = initDictionary();
        currentEngine.setInputStream(System.in);
        currentEngine.setOutputStream(System.out);

        //Allows for replaying
        do {
            runGame();
        } while (currentEngine.replay());

        currentEngine.closeStreams();

    }


    //Set the engine and run the game
    public static void main(String[] args) throws IOException, InterruptedException {
        currentEngine = new TextEngine();
        executeGame();
    }


}
