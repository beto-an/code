package assignment;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;


//Class to run all the logic for a game of Boggle
public class GameManager implements BoggleGame {


    //Expected number of characters in each cube
    public static final int FACES = 6;

    //Scores for each player
    protected int[] scores;

    //Score for the computer corresponding to each player, i.e. the score of all words that player hasn't guessed yet
    protected int[] computerScores;

    protected char[][] board;
    protected static final Random RNG = new Random();
    protected int size;
    protected int numPlayers;
    protected ArrayList<String> cubes = new ArrayList<>();
    protected BoggleDictionary dict;
    protected SearchTactic currentSearchTactic;

    //Keeps track of all words each player has guessed
    protected Trie[] playerWords;

    //Keeps track of all words in the board, used for computer
    protected final HashSet<String> allWords = new HashSet<>();

    //Maximum score possible for a given board
    protected int maxScore;

    //Keeps track of words left to guess for each player
    protected final ArrayList<HashSet<String>> computerWords = new ArrayList<>();

    //Helper arrays for traversing a 2D grid
    protected static final int[] DR = { -1, -1, -1, 0, 0, 1, 1, 1 };
    protected static final int[] DC = { -1, 0, 1, -1, 1, -1, 0, 1 };

    //Keeps track of the last successfully added word's location
    protected ArrayList<Point> lastAddedWordLocation;

    //Keeps track of the current word's location
    protected ArrayList<Point> currentWordLocation;

    //Keeps track of whether newGame has been called yet
    protected static boolean gameLoaded = false;

    //Global flag for whether the game should be case-sensitive or not, default is false
    protected static final boolean CASE_SENSITIVE = false;


    //Initialize a new Boggle game
    @Override
    public void newGame(int size, int numPlayers, String cubeFile, BoggleDictionary dict) throws IOException {

        //Input validation
        if (size <= 0) {
            System.err.println("Board size must be at least 1");
            return;
        }
        if (numPlayers <= 0) {
            System.err.println("Number of players must be at least 1");
            return;
        }
        if (cubeFile == null) {
            System.err.println("Cube file must not be null");
            return;
        }

        //Initialize instance variables
        board = new char[size][size];
        currentSearchTactic = SEARCH_DEFAULT;
        this.size = size;
        this.numPlayers = numPlayers;
        lastAddedWordLocation = new ArrayList<>();
        computerScores = new int[numPlayers];
        scores = new int[numPlayers];
        gameLoaded = true;
        this.dict = dict;

        //Read in from @cubeFile
        BufferedReader br = new BufferedReader(new FileReader(cubeFile));
        while (br.ready()) {

            String cube = br.readLine();
            if (!CASE_SENSITIVE) cube = cube.toLowerCase();

            //Ensure each cube has FACES number of faces
            if (cube.length() == FACES) {
                cubes.add(cube);
            } else {
                System.err.println("Cube " + cube + " does not have " + FACES + " faces.");
            }

        }

        //Reset board and data
        randomizeBoard();
        resetPlayerData();
        resetComputerData();

    }


    //Reset the words that players have guessed as well as their scores
    public void resetPlayerData() {

        playerWords = new Trie[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            playerWords[i] = new Trie();
            scores[i] = 0;
        }

    }


    //Reset the words that players haven't guessed
    public void resetComputerData() {

        //Find all words on the board
        allWords.clear();
        allWords.addAll(getAllWords());
        maxScore = 0;
        computerWords.clear();

        //Recalculate the max board score
        for (String word : allWords) {
            maxScore += scoreOf(word);
        }

        //Reset the words not guessed for each player
        for (int i = 0; i < numPlayers; i++) {
            computerWords.add(new HashSet<>(allWords));
            computerScores[i] = maxScore;
        }

    }


    //Return the board
    @Override
    public char[][] getBoard() {
        return board;
    }


    //Attempt to add a word for a certain player
    @Override
    public int addWord(String word, int player) {

        word = word.toLowerCase();

        //Validate that the word, player, and board state are valid
        if (!gameLoaded) return 0; //Board not loaded
        if (player >= numPlayers || player < 0) return 0; //Invalid player
        int wordScore = scoreOf(word);
        if (wordScore == 0) return 0; //Word too short
        if (playerWords[player].contains(word)) return 0; //Already guessed by this player
        if (isWordInGrid(word) && dict.contains(word)) { //Make sure word in board and dictionary

            //Update words guessed
            playerWords[player].add(word);
            computerWords.get(player).remove(word);

            //Update last successful word position
            lastAddedWordLocation = currentWordLocation;
            currentWordLocation = null;

            //Update scores
            scores[player] += wordScore;
            computerScores[player] -= wordScore;
            return wordScore;

        } else {
            return 0;
        }

    }


    //Helper method for Boggle.java to validate input
    //Returns a variety of self-explanatory error messages if input is invalid
    public String getErrorMessage(String word, int player) {

        word = word.toLowerCase();

        if (player < 0 || player >= numPlayers) return "Invalid player index!";

        if (word.length() < 4) return "Word is too short!";

        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (48 <= c && c <= 57 || 97 <= c && c <= 122) continue;
            return "Word has invalid characters!";
        }

        if (!dict.contains(word)) return "Word is not in the dictionary!";

        if (!isWordInGrid(word)) return "Word not found on the board!";

        if (playerWords[player].contains(word)) return "Word already guessed by this player!";

        return "None";
    }


    //Helper method to check if a word is in the grid
    public boolean isWordInGrid(String word) {

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {

                //For all cells in the grid, start a recursive DFS search there and try to find the word
                boolean[][] visited = new boolean[size][size];
                currentWordLocation = new ArrayList<>();
                if (recursiveSearchWord(word, 0, visited, i, j)) return true;

            }
        }

        return false;

    }


    //Helper method for isWordInGrid, runs a DFS starting from a certain cell, searching for a certain word
    public boolean recursiveSearchWord(String word, int index, boolean[][] visited, int row, int col) {

        //Base case: if word is complete
        if (index == word.length()) return true;

        //If current state is invalid, don't do anything
        if (row < 0 || row >= size || col < 0 || col >= size || visited[row][col] || board[row][col] != word.charAt(index)) return false;

        //Update location, visit neighbors
        currentWordLocation.add(new Point(row, col));
        visited[row][col] = true;
        for (int i = 0; i < 8; i++) {
            int newRow = row + DR[i];
            int newCol = col + DC[i];
            if (recursiveSearchWord(word, index + 1, visited, newRow, newCol)) return true;
        }

        //If there wasn't a success, undo changes made by this search
        visited[row][col] = false;
        currentWordLocation.remove(currentWordLocation.size() - 1);
        return false;

    }


    //Return the last successful word location
    @Override
    public List<Point> getLastAddedWord() {
        return lastAddedWordLocation;
    }


    //Set the game to be a new board, reset player data
    @Override
    public void setGame(char[][] board) {

        if (board == null) { //Special case: null board

            size = 0;
            this.board = null;

        } else if (board.length == 0) { //Special case: 0x0 board

            size = 0;
            this.board = new char[0][0];

        } else { //Default case

            //Make sure the array is not jagged
            int firstLen = board[0].length;
            for (char[] chars : board) {
                if (chars.length != firstLen) {
                    System.err.print("Cannot play with jagged arrays");
                    return;
                }
            }

            //Deep copy the new board
            size = board.length;
            this.board = new char[size][size];
            for (int i = 0; i < size; i++) {
                System.arraycopy(board[i], 0, this.board[i], 0, size);
            }

        }

        //Reset player data
        resetPlayerData();
        resetComputerData();

    }


    //Calculate and return all words in the board, using the current search tactic
    @Override
    public Collection<String> getAllWords() {

        //Special case: dictionary is null
        if (dict == null) return new ArrayList<>();

        //Run specified search tactic
        if (currentSearchTactic == SearchTactic.SEARCH_BOARD) {
            return boardSearch();
        } else if (currentSearchTactic == SearchTactic.SEARCH_DICT) {
            return dictionarySearch();
        } else {
            return null;
        }

    }


    //Helper method to find all words in the board using the SEARCH_BOARD tactic
    public Collection<String> boardSearch() {

        TreeSet<String> list = new TreeSet<>();

        //Run a DFS from all grid cells, keep track of all words found
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                boolean[][] visited = new boolean[size][size];
                recursiveSearchBoard("", visited, i, j, list);
            }
        }

        return list;

    }


    //Helper method for boardSearch, finds all words that start with some prefix
    public void recursiveSearchBoard(String currWord, boolean[][] visited, int row, int col, TreeSet<String> list) {

        //If we found a valid word, then add it to the list
        if (scoreOf(currWord) > 0 && dict.contains(currWord)) list.add(currWord);

        //If the state is invalid, then do nothing
        if (row < 0 || row >= size || col < 0 || col >= size || visited[row][col]) return;

        //If it's not possible to find another word given the current prefix
        currWord += board[row][col];
        if (!dict.isPrefix(currWord)) return;

        //Visit all neighbors and recursively build up all word possibilities
        visited[row][col] = true;
        for (int i = 0; i < 8; i++) {
            int newRow = row + DR[i];
            int newCol = col + DC[i];
            recursiveSearchBoard(currWord, visited, newRow, newCol, list);
        }
        visited[row][col] = false;

    }


    //Helper method to find all words in the board using the SEARCH_DICT tactic
    public Collection<String> dictionarySearch() {

        //Iterate through all words in the dictionary and check to see if each one is in the board
        Iterator<String> it = dict.iterator();
        TreeSet<String> list = new TreeSet<>();
        while (it.hasNext()) {

            //If in the board and a valid word, then add it to the list
            String s = it.next();
            if (scoreOf(s) > 0 && isWordInGrid(s)) {
                list.add(s);
            }

        }

        return list;

    }


    //Sets the search tactic
    @Override
    public void setSearchTactic(SearchTactic tactic) {
        if (tactic == null) currentSearchTactic = SEARCH_DEFAULT;
        else currentSearchTactic = tactic;
    }


    //Return player scores
    @Override
    public int[] getScores() {
        return scores;
    }


    //Return computer scores
    public int[] getComputerScores() {
        return computerScores;
    }


    //Return the score for a specific player
    public int getScore(int player) {
        return scores[player];
    }


    //Return the score for a specific computer
    public int getComputerScore(int computer) {
        return computerScores[computer];
    }

    /**
     * Randomizes the Board using the cubes passed in through newGame()
     */
    private void randomizeBoard() {

        if (cubes.size() >= size * size) {

            //If there are enough cubes to assign a unique cube to each grid cell, choose without repetition
            //Pick one random cube for each cell
            ArrayList<String> newCubes = new ArrayList<>();
            for (int i = 0; i < size * size; i++) {
                newCubes.add(cubes.remove(RNG.nextInt(cubes.size())));
            }
            cubes = newCubes;

            //Pick a random character out of each cube for each cell
            for (int row = 0; row < size; row++) {
                for (int col = 0; col < size; col++) {
                    String cube = cubes.get(row * size + col);
                    board[row][col] = cube.charAt(RNG.nextInt(FACES));
                }
            }

        } else if (cubes.size() > 0) {

            //If there aren't enough cubes, choose with repetition, picking a random character out of each cube as well
            for (int row = 0; row < size; row++) {
                for (int col = 0; col < size; col++) {
                    String cube = cubes.get(RNG.nextInt(cubes.size()));
                    board[row][col] = cube.charAt(RNG.nextInt(FACES));
                }
            }

        }

    }


    //Calculate the score of a word, 0 if invalid
    private static int scoreOf(String s) {
        return Math.max(0, s.length() - 3);
    }


    //Return all the words that a specific player has found
    public HashSet<String> getFoundWords(int playerIndex) {

        HashSet<String> foundWords = new HashSet<>();
        for (String s : playerWords[playerIndex]) {
            if (s.length() <= 3) continue;
            foundWords.add(s);
        }
        return foundWords;

    }


    //Return all the words that a specific computer has found
    public HashSet<String> getComputerWords(int computerIndex) {
        return computerWords.get(computerIndex);
    }


}
