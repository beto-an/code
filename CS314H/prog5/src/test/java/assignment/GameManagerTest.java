package assignment;

import java.awt.Point;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


//Testing class for automated unit testing on the GameManager class
class GameManagerTest {


    public static final char[][] testBoard1 = { //words = word, work, test, zzzz
            { 'w', 'o', 'r', 'd' },
            { 'z', 'z', 'k', 'z' },
            { 'z', 'z', 'z', 'z' },
            { 't', 's', 'e', 't' }
    };

    public static final char[][] testBoard2 = { //words = word, work, hello
            { 'x', 'x', 'x', 'x' },
            { 'h', 'x', 'k', 'x' },
            { 'e', 'd', 'r', 'x' },
            { 'l', 'l', 'o', 'w' }
    };

    public static final char[][] smallBoard = {
            { 'a' }
    };

    public static final char[][] emptyBoard = { };

    public static final char[][] nullBoard = null;


    //Helper method for creating a new dictionary loaded from a specified file
    GameDictionary initNewDict(String fileName) throws IOException {
        GameDictionary dict = new GameDictionary();
        dict.loadDictionary(fileName);
        return dict;
    }


    //Helper method for asserting that two boards are equal
    void assertBoardEquals(char[][] expectedBoard, char[][] actualBoard) {
        assertEquals(expectedBoard.length, actualBoard.length);
        for (int i = 0; i < expectedBoard.length; i++) {
            assertEquals(expectedBoard[i].length, actualBoard[i].length);
            for (int j = 0; j < expectedBoard[i].length; j++) {
                assertEquals(expectedBoard[i][j], actualBoard[i][j]);
            }
        }
    }


    //Helper method for creating a board that has only the character 'a'
    char[][] createAllABoard(int size) {
        char[][] board = new char[size][size];
        for (int i = 0; i < size; i++) {
            Arrays.fill(board[i], 'a');
        }
        return board;
    }


    //Helper method for asserting that two score arrays are equal
    void assertScoreEquals(int[] expectedScores, int[] actualScores) {
        assertEquals(expectedScores.length, actualScores.length);
        for (int i = 0; i < expectedScores.length; i++) {
            assertEquals(expectedScores[i], actualScores[i]);
        }
    }


    //Test that the newGame method works as intended
    @Test
    void newGame() throws IOException { //Interface method

        //Test for a variety of sizes
        BoggleGame game = new GameManager();
        game.newGame(4, 1, "allACubes16.txt", initNewDict("fewWords.txt"));
        assertEquals(4, game.getBoard().length);
        assertEquals(4, game.getBoard()[0].length);
        assertEquals(1, game.getScores().length);
        assertEquals(0, game.getScores()[0]);
        assertBoardEquals(createAllABoard(4), game.getBoard());
        assertScoreEquals(new int[]{0}, game.getScores());

        game = new GameManager();
        game.newGame(1, 10, "allACubes1.txt", initNewDict("fewWords.txt"));
        assertEquals(1, game.getBoard().length);
        assertEquals(1, game.getBoard()[0].length);
        assertEquals(10, game.getScores().length);
        assertEquals(0, game.getScores()[0]);
        assertBoardEquals(createAllABoard(1), game.getBoard());
        assertScoreEquals(new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, game.getScores());

        game = new GameManager();
        game.newGame(0, 2, "allACubes0.txt", initNewDict("fewWords.txt"));
        assertEquals(0, game.getBoard().length);
        assertEquals(2, game.getScores().length);
        assertEquals(0, game.getScores()[0]);
        assertBoardEquals(createAllABoard(0), game.getBoard());
        assertScoreEquals(new int[]{0, 0}, game.getScores());

        game = new GameManager();
        game.newGame(7, 5, "allACubes49.txt", initNewDict("fewWords.txt"));
        assertEquals(7, game.getBoard().length);
        assertEquals(7, game.getBoard()[0].length);
        assertEquals(5, game.getScores().length);
        assertEquals(0, game.getScores()[0]);
        assertBoardEquals(createAllABoard(7), game.getBoard());
        assertScoreEquals(new int[]{0, 0, 0, 0, 0}, game.getScores());


        //Call newGame with invalid file paths
        game = new GameManager();
        boolean check = false;
        try {
            game.newGame(2, 12, "fileDoesntExist.txt", initNewDict("fewWords.txt"));
        } catch (IOException e) {
            check = true;
        }
        assertTrue(check);

        game = new GameManager();
        check = false;
        try {
            game.newGame(2, 12, "fdsoij9 q8yowfiejsnd9  ", initNewDict("fewWords.txt"));
        } catch (IOException e) {
            check = true;
        }
        assertTrue(check);


        //Call newGame with a null dictionary
        game = new GameManager();
        game.newGame(1, 12, "allACubes1.txt", null);

    }


    //Test that getBoard returns the correct board
    @Test
    void getBoard() { //Interface method

        BoggleGame game = new GameManager();

        //Test null board
        game.setGame(null);
        assertNull(game.getBoard());


        //Test 0x0 board
        char[][] board2 = new char[0][0];
        game.setGame(board2);
        assertBoardEquals(board2, game.getBoard());


        //Test that the method makes a deep copy for a variety of sizes
        char[][] board3 = {
                { 'Z' }
        };
        game.setGame(board3);
        board3[0][0] = 's';
        char[][] board3Copy = {
                { 'Z' }
        };
        assertBoardEquals(board3Copy, game.getBoard());

        char[][] board4 = createAllABoard(4);
        game.setGame(board4);
        board4[0][0] = 'z';
        assertBoardEquals(createAllABoard(4), game.getBoard());

        char[][] board5 = createAllABoard(20);
        game.setGame(board5);
        board5[18][3] = 'z';
        assertBoardEquals(createAllABoard(20), game.getBoard());

    }


    //Test that addWord returns the correct score for a variety of situations and valid/invalid words
    @Test
    void addWord() throws IOException { //Interface method

        //Test the method for invalid words, words guessed by different/same players, etc.
        BoggleGame game = new GameManager();
        game.newGame(4, 3, "allACubes16.txt", initNewDict("fewWords.txt"));
        game.setGame(testBoard1);
        assertEquals(0, game.addWord("98423574298401ueijfwnks", 0));
        assertEquals(0, game.addWord("wordzz", 0));
        assertEquals(0, game.addWord("hello", 0));
        assertEquals(1, game.addWord("word", 0));
        assertEquals(1, game.addWord("word", 1));
        assertEquals(1, game.addWord("work", 0));
        assertEquals(1, game.addWord("test", 0));
        assertEquals(1, game.addWord("zzzz", 0));
        assertEquals(0, game.addWord("w", 2));
        assertEquals(0, game.addWord("wo", 2));
        assertEquals(0, game.addWord("wor", 2));

        game = new GameManager();
        game.newGame(4, 30, "allACubes16.txt", initNewDict("fewWords.txt"));
        game.setGame(testBoard2);
        assertEquals(0, game.addWord("98423574298401ueijfwnks", 0));
        assertEquals(0, game.addWord("wordzz", 0));
        assertEquals(2, game.addWord("hello", 0));
        assertEquals(0, game.addWord("hello", 0));
        assertEquals(1, game.addWord("word", 0));
        assertEquals(1, game.addWord("word", 1));
        assertEquals(1, game.addWord("work", 0));
        assertEquals(0, game.addWord("test", 0));
        assertEquals(2, game.addWord("hello", 14));
        assertEquals(0, game.addWord("zzzz", 0));
        assertEquals(0, game.addWord("w", 2));
        assertEquals(0, game.addWord("wo", 2));
        assertEquals(0, game.addWord("wor", 2));


        //Test addWord for 1x1, 0x0, and null boards
        game = new GameManager();
        game.newGame(1, 300, "allACubes1.txt", initNewDict("fewWords.txt"));
        game.setGame(smallBoard);
        assertEquals(0, game.addWord("wor", 2));
        assertEquals(0, game.addWord("a", 2));

        game = new GameManager();
        game.newGame(0, 3, "allACubes0.txt", initNewDict("fewWords.txt"));
        game.setGame(emptyBoard);
        assertEquals(0, game.addWord("wor", 2));
        assertEquals(0, game.addWord("a", 2));

        game = new GameManager();
        game.newGame(0, 3, "allACubes0.txt", initNewDict("fewWords.txt"));
        game.setGame(nullBoard);
        assertEquals(0, game.addWord("wor", 2));
        assertEquals(0, game.addWord("a", 2));

    }


    //Test that the isWordInGrid method correctly identifies if a word is in the grid
    @Test
    void isWordInGrid() throws IOException {

        //Test the method for a regular board
        GameManager game = new GameManager();
        game.newGame(4, 3, "allACubes16.txt", initNewDict("fewWords.txt"));
        game.setGame(testBoard1);
        assertFalse(game.isWordInGrid("98423574298401ueijfwnks"));
        assertTrue(game.isWordInGrid("wordzz"));
        assertFalse(game.isWordInGrid("hello"));
        assertTrue(game.isWordInGrid("w"));
        assertTrue(game.isWordInGrid("wo"));
        assertTrue(game.isWordInGrid("wor"));
        assertTrue(game.isWordInGrid("word"));
        assertTrue(game.isWordInGrid("work"));
        assertTrue(game.isWordInGrid("test"));
        assertTrue(game.isWordInGrid("zzzz"));
        assertTrue(game.isWordInGrid(""));

        //Test the method for a small board with edge cases
        game = new GameManager();
        game.newGame(1, 300, "allACubes1.txt", initNewDict("fewWords.txt"));
        game.setGame(smallBoard);
        assertFalse(game.isWordInGrid("hello"));
        assertTrue(game.isWordInGrid(""));
        assertTrue(game.isWordInGrid("a"));
        assertFalse(game.isWordInGrid("98423574298401ueijfwnks"));
        assertFalse(game.isWordInGrid("aa"));

    }


    //Helper method for checking if two collections of Points have the same Points
    void assertListsEqual(HashSet<Point> expectedSet, List<Point> list) {

        //Convert the List to a HashSet for efficient comparison
        HashSet<Point> actualSet = new HashSet<>();
        for (Point p : list) {
            assertFalse(actualSet.contains(p));
            actualSet.add(p);
        }

        //Check that the HashSets have the same elements
        assertEquals(expectedSet.size(), actualSet.size());
        for (Point p : expectedSet){
            assertTrue(actualSet.contains(p));
        }

    }


    //Test that getLastAddedWord returns the correct list of points
    @Test
    void getLastAddedWord() throws IOException { //Interface method

        BoggleGame game = new GameManager();
        game.newGame(4, 4, "allACubes16.txt", initNewDict("fewWords.txt"));
        game.setGame(testBoard1);

        //Test initial case
        assertNull(game.getLastAddedWord());

        //No successful word yet, should still be null
        game.addWord("sjfk9e8w3 0q", 0);
        assertNull(game.getLastAddedWord());

        //Test the method once the game has a successful word
        HashSet<Point> set1 = new HashSet<>();
        set1.add(new Point(0, 0));
        set1.add(new Point(0, 1));
        set1.add(new Point(0, 2));
        set1.add(new Point(0, 3));
        game.addWord("word", 1);
        assertListsEqual(set1, game.getLastAddedWord());

        game.addWord(" 8923r 9", 0);
        assertListsEqual(set1, game.getLastAddedWord());

        game.addWord("test", 2);
        HashSet<Point> set2 = new HashSet<>();
        set2.add(new Point(3, 0));
        set2.add(new Point(3, 1));
        set2.add(new Point(3, 2));
        set2.add(new Point(3, 3));
        assertListsEqual(set2, game.getLastAddedWord());


        //Test the method for 0x0 and null boards
        game = new GameManager();
        game.newGame(0, 1, "allACubes0.txt", initNewDict("fewWords.txt"));
        game.setGame(emptyBoard);
        assertNull(game.getLastAddedWord());

        game.addWord("sjfk9e8w3 0q", 0);
        assertNull(game.getLastAddedWord());

        game.addWord("", 0);
        assertNull(game.getLastAddedWord());

        game = new GameManager();
        game.newGame(0, 1, "allACubes0.txt", initNewDict("fewWords.txt"));
        game.setGame(nullBoard);
        assertNull(game.getLastAddedWord());

        game.addWord("sjfk9e8w3 0q", 0);
        assertNull(game.getLastAddedWord());

        game.addWord("", 0);
        assertNull(game.getLastAddedWord());

    }


    //Test the setGame method to make sure it sets the board correctly
    @Test
    void setGame() throws IOException { //Interface method

        BoggleGame game = new GameManager();
        game.newGame(4, 5, "allACubes16.txt", initNewDict("fewWords.txt"));

        //Set null board
        game.setGame(null);
        assertNull(game.getBoard());
        assertScoreEquals(new int[]{0, 0, 0, 0, 0}, game.getScores());

        //Set 0x0 board
        char[][] board2 = new char[0][0];
        game.setGame(board2);
        assertBoardEquals(board2, game.getBoard());
        assertScoreEquals(new int[]{0, 0, 0, 0, 0}, game.getScores());

        //Make sure the board is set to a deep copy, player stats all reset
        char[][] board3 = {
                { 'Z' }
        };
        game.setGame(board3);
        board3[0][0] = 's';
        char[][] board3Copy = {
                { 'Z' }
        };
        assertBoardEquals(board3Copy, game.getBoard());
        assertScoreEquals(new int[]{0, 0, 0, 0, 0}, game.getScores());

        char[][] board4 = createAllABoard(4);
        game.setGame(board4);
        assertEquals(1, game.addWord("aaaa", 0));
        game.setGame(board4);
        board4[0][0] = 'z';
        assertBoardEquals(createAllABoard(4), game.getBoard());
        assertScoreEquals(new int[]{0, 0, 0, 0, 0}, game.getScores());

        char[][] board5 = createAllABoard(20);
        game.setGame(board5);
        assertEquals(1, game.addWord("aaaa", 4));
        game.setGame(board5);
        board5[18][3] = 'z';
        assertBoardEquals(createAllABoard(20), game.getBoard());
        assertScoreEquals(new int[]{0, 0, 0, 0, 0}, game.getScores());

    }


    //Helper method for asserting that two Collections are equal
    void assertCollectionsEqual(HashSet<String> expectedSet, Collection<String> c) {

        //Convert second collection to HashSet for efficient comparison
        HashSet<String> actualSet = new HashSet<>();
        for (String s : c) {
            assertFalse(actualSet.contains(s));
            actualSet.add(s);
        }

        //Assert that the two HashSets have the same elements
        assertEquals(expectedSet.size(), actualSet.size());
        for (String s : expectedSet){
            assertTrue(actualSet.contains(s));
        }

    }


    //Test that getAllWords returns all words found in the grid
    @Test
    void getAllWords() throws IOException { //Interface method

        //Test the method for a specific board
        BoggleGame game = new GameManager();
        game.newGame(4, 3, "allACubes16.txt", initNewDict("fewWords.txt"));
        game.setGame(testBoard1);
        HashSet<String> set1 = new HashSet<>();
        set1.add("ZZZZ");
        set1.add("WORD");
        set1.add("WORK");
        set1.add("TEST");
        game.setSearchTactic(BoggleGame.SearchTactic.SEARCH_DICT);
        assertCollectionsEqual(set1, game.getAllWords());
        game.setSearchTactic(BoggleGame.SearchTactic.SEARCH_BOARD);
        assertCollectionsEqual(set1, game.getAllWords());

        game = new GameManager();
        game.newGame(4, 3, "allACubes16.txt", initNewDict("fewWords.txt"));
        game.setGame(testBoard2);
        HashSet<String> set2 = new HashSet<>();
        set2.add("WORD");
        set2.add("WORK");
        set2.add("HELLO");
        game.setSearchTactic(BoggleGame.SearchTactic.SEARCH_DICT);
        assertCollectionsEqual(set2, game.getAllWords());
        game.setSearchTactic(BoggleGame.SearchTactic.SEARCH_BOARD);
        assertCollectionsEqual(set2, game.getAllWords());


        //Test the method for 1x1, 0x0, and null boards
        game = new GameManager();
        game.newGame(1, 300, "allACubes1.txt", initNewDict("fewWords.txt"));
        game.setGame(smallBoard);
        HashSet<String> set3 = new HashSet<>();
        game.setSearchTactic(BoggleGame.SearchTactic.SEARCH_DICT);
        assertCollectionsEqual(set3, game.getAllWords());
        game.setSearchTactic(BoggleGame.SearchTactic.SEARCH_BOARD);
        assertCollectionsEqual(set3, game.getAllWords());

        game = new GameManager();
        game.newGame(0, 1, "allACubes0.txt", initNewDict("fewWords.txt"));
        game.setGame(emptyBoard);
        HashSet<String> set4 = new HashSet<>();
        game.setSearchTactic(BoggleGame.SearchTactic.SEARCH_DICT);
        assertCollectionsEqual(set4, game.getAllWords());
        game.setSearchTactic(BoggleGame.SearchTactic.SEARCH_BOARD);
        assertCollectionsEqual(set4, game.getAllWords());

        game = new GameManager();
        game.newGame(0, 1, "allACubes0.txt", initNewDict("fewWords.txt"));
        game.setGame(nullBoard);
        HashSet<String> set5 = new HashSet<>();
        game.setSearchTactic(BoggleGame.SearchTactic.SEARCH_DICT);
        assertCollectionsEqual(set5, game.getAllWords());
        game.setSearchTactic(BoggleGame.SearchTactic.SEARCH_BOARD);
        assertCollectionsEqual(set5, game.getAllWords());

    }


    //Ensure that setSearchTactic responds fine to any possible input
    @Test
    void setSearchTactic() { //Interface method

        BoggleGame game = new GameManager();

        try {
            game.setSearchTactic(BoggleGame.SearchTactic.SEARCH_DICT);
            game.setSearchTactic(BoggleGame.SearchTactic.SEARCH_BOARD);
            game.setSearchTactic(null);
        } catch(Exception e) {
            System.out.println(e.getStackTrace());
        }

    }


    //Test that getScores returns the correct player scores
    @Test
    void getScores() throws IOException { //Interface method

        //Test the player scores after adding a variety of correct/incorrect words
        BoggleGame game = new GameManager();
        game.newGame(4, 3, "allACubes16.txt", initNewDict("fewWords.txt"));
        game.setGame(testBoard1);
        assertScoreEquals(new int[]{0, 0, 0}, game.getScores());
        game.addWord("word", 0);
        assertScoreEquals(new int[]{1, 0, 0}, game.getScores());
        game.addWord("work", 1);
        assertScoreEquals(new int[]{1, 1, 0}, game.getScores());
        game.addWord("test", 2);
        assertScoreEquals(new int[]{1, 1, 1}, game.getScores());
        game.addWord("work", 0);
        assertScoreEquals(new int[]{2, 1, 1}, game.getScores());
        game.addWord("test", 0);
        assertScoreEquals(new int[]{3, 1, 1}, game.getScores());
        game.addWord("zzzz", 2);
        assertScoreEquals(new int[]{3, 1, 2}, game.getScores());
        game.addWord("", 2);
        assertScoreEquals(new int[]{3, 1, 2}, game.getScores());
        game.addWord("wordzz", 2);
        assertScoreEquals(new int[]{3, 1, 2}, game.getScores());
        game.addWord("s frbeihjnfsdlasnksdjbfisfi", 2);
        assertScoreEquals(new int[]{3, 1, 2}, game.getScores());

        game = new GameManager();
        game.newGame(4, 3, "allACubes16.txt", initNewDict("fewWords.txt"));
        game.setGame(testBoard2);
        assertScoreEquals(new int[]{0, 0, 0}, game.getScores());
        game.addWord("word", 0);
        assertScoreEquals(new int[]{1, 0, 0}, game.getScores());
        game.addWord("work", 1);
        assertScoreEquals(new int[]{1, 1, 0}, game.getScores());
        game.addWord("hello", 2);
        assertScoreEquals(new int[]{1, 1, 2}, game.getScores());
        game.addWord("work", 0);
        assertScoreEquals(new int[]{2, 1, 2}, game.getScores());
        game.addWord("hello", 0);
        assertScoreEquals(new int[]{4, 1, 2}, game.getScores());
        game.addWord("zzzz", 2);
        assertScoreEquals(new int[]{4, 1, 2}, game.getScores());
        game.addWord("", 2);
        assertScoreEquals(new int[]{4, 1, 2}, game.getScores());
        game.addWord("wordzz", 2);
        assertScoreEquals(new int[]{4, 1, 2}, game.getScores());
        game.addWord("s frbeihjnfsdlasnksdjbfisfi", 2);
        assertScoreEquals(new int[]{4, 1, 2}, game.getScores());


        //Do the same tests, but for 1x1, 0x0, and null boards
        game = new GameManager();
        game.newGame(1, 5, "allACubes1.txt", initNewDict("fewWords.txt"));
        game.setGame(smallBoard);
        assertScoreEquals(new int[]{0, 0, 0, 0, 0}, game.getScores());
        game.addWord("word", 0);
        assertScoreEquals(new int[]{0, 0, 0, 0, 0}, game.getScores());
        game.addWord("word", 0);
        assertScoreEquals(new int[]{0, 0, 0, 0, 0}, game.getScores());
        game.addWord("a", 0);
        assertScoreEquals(new int[]{0, 0, 0, 0, 0}, game.getScores());

        game = new GameManager();
        game.newGame(0, 5, "allACubes0.txt", initNewDict("fewWords.txt"));
        game.setGame(emptyBoard);
        assertScoreEquals(new int[]{0, 0, 0, 0, 0}, game.getScores());
        game.addWord("word", 0);
        assertScoreEquals(new int[]{0, 0, 0, 0, 0}, game.getScores());
        game.addWord("word", 0);
        assertScoreEquals(new int[]{0, 0, 0, 0, 0}, game.getScores());
        game.addWord("a", 0);
        assertScoreEquals(new int[]{0, 0, 0, 0, 0}, game.getScores());

        game = new GameManager();
        game.newGame(0, 5, "allACubes0.txt", initNewDict("fewWords.txt"));
        game.setGame(nullBoard);
        assertScoreEquals(new int[]{0, 0, 0, 0, 0}, game.getScores());
        game.addWord("word", 0);
        assertScoreEquals(new int[]{0, 0, 0, 0, 0}, game.getScores());
        game.addWord("word", 0);
        assertScoreEquals(new int[]{0, 0, 0, 0, 0}, game.getScores());
        game.addWord("a", 0);
        assertScoreEquals(new int[]{0, 0, 0, 0, 0}, game.getScores());

    }


    //Tests getComputerScores, getFoundWords, and getComputerWords and makes sure the data is updated correctly
    @Test
    void testWordCalculation() throws IOException {

        //Test that computer words and scores are initially calculated correctly
        GameManager game = new GameManager();
        game.newGame(4, 3, "allACubes16.txt", initNewDict("fewWords.txt"));
        game.setGame(testBoard1);
        HashSet<String> computerWords = new HashSet<>();
        computerWords.add("work");
        computerWords.add("word");
        computerWords.add("test");
        computerWords.add("zzzz");
        HashSet<String> foundWords = new HashSet<>();

        assertCollectionsEqual(computerWords, game.getComputerWords(0));
        assertCollectionsEqual(computerWords, game.getComputerWords(1));
        assertCollectionsEqual(computerWords, game.getComputerWords(2));
        assertCollectionsEqual(foundWords, game.getFoundWords(0));
        assertCollectionsEqual(foundWords, game.getFoundWords(1));
        assertCollectionsEqual(foundWords, game.getFoundWords(2));

        assertScoreEquals(new int[]{4, 4, 4}, game.getComputerScores());
        game.addWord("word", 0);

        //Test player guessing a word
        computerWords.remove("word");
        assertCollectionsEqual(computerWords, game.getComputerWords(0));
        computerWords.add("word");
        assertCollectionsEqual(computerWords, game.getComputerWords(1));
        assertCollectionsEqual(computerWords, game.getComputerWords(2));
        foundWords.add("word");
        assertCollectionsEqual(foundWords, game.getFoundWords(0));
        foundWords.remove("word");
        assertCollectionsEqual(foundWords, game.getFoundWords(1));
        assertCollectionsEqual(foundWords, game.getFoundWords(2));

        assertScoreEquals(new int[]{3, 4, 4}, game.getComputerScores());
        game.addWord("work", 1);
        assertScoreEquals(new int[]{3, 3, 4}, game.getComputerScores());
        game.addWord("test", 2);
        assertScoreEquals(new int[]{3, 3, 3}, game.getComputerScores());
        game.addWord("work", 0);
        assertScoreEquals(new int[]{2, 3, 3}, game.getComputerScores());
        game.addWord("test", 0);
        assertScoreEquals(new int[]{1, 3, 3}, game.getComputerScores());
        game.addWord("zzzz", 2);
        assertScoreEquals(new int[]{1, 3, 2}, game.getComputerScores());
        game.addWord("", 2);
        assertScoreEquals(new int[]{1, 3, 2}, game.getComputerScores());
        game.addWord("wordzz", 2);
        assertScoreEquals(new int[]{1, 3, 2}, game.getComputerScores());
        game.addWord("s frbeihjnfsdlasnksdjbfisfi", 2);
        assertScoreEquals(new int[]{1, 3, 2}, game.getComputerScores());
        game.addWord("zzzz", 0);
        assertScoreEquals(new int[]{0, 3, 2}, game.getComputerScores());


        //Test computer for 1x1, 0x0, and null boards
        game = new GameManager();
        game.newGame(1, 5, "allACubes1.txt", initNewDict("fewWords.txt"));
        game.setGame(smallBoard);
        assertScoreEquals(new int[]{0, 0, 0, 0, 0}, game.getComputerScores());
        game.addWord("word", 0);
        assertScoreEquals(new int[]{0, 0, 0, 0, 0}, game.getComputerScores());
        game.addWord("word", 0);
        assertScoreEquals(new int[]{0, 0, 0, 0, 0}, game.getComputerScores());
        game.addWord("a", 0);
        assertScoreEquals(new int[]{0, 0, 0, 0, 0}, game.getComputerScores());

        computerWords = new HashSet<>();
        foundWords = new HashSet<>();
        assertCollectionsEqual(computerWords, game.getComputerWords(0));
        assertCollectionsEqual(foundWords, game.getFoundWords(0));

        game = new GameManager();
        game.newGame(0, 5, "allACubes0.txt", initNewDict("fewWords.txt"));
        game.setGame(emptyBoard);
        assertScoreEquals(new int[]{0, 0, 0, 0, 0}, game.getComputerScores());
        game.addWord("word", 0);
        assertScoreEquals(new int[]{0, 0, 0, 0, 0}, game.getComputerScores());
        game.addWord("word", 0);
        assertScoreEquals(new int[]{0, 0, 0, 0, 0}, game.getComputerScores());
        game.addWord("a", 0);
        assertScoreEquals(new int[]{0, 0, 0, 0, 0}, game.getComputerScores());

        assertCollectionsEqual(computerWords, game.getComputerWords(0));
        assertCollectionsEqual(foundWords, game.getFoundWords(0));

        game = new GameManager();
        game.newGame(0, 5, "allACubes0.txt", initNewDict("fewWords.txt"));
        game.setGame(nullBoard);
        assertScoreEquals(new int[]{0, 0, 0, 0, 0}, game.getComputerScores());
        game.addWord("word", 0);
        assertScoreEquals(new int[]{0, 0, 0, 0, 0}, game.getComputerScores());
        game.addWord("word", 0);
        assertScoreEquals(new int[]{0, 0, 0, 0, 0}, game.getComputerScores());
        game.addWord("a", 0);
        assertScoreEquals(new int[]{0, 0, 0, 0, 0}, game.getComputerScores());

        assertCollectionsEqual(computerWords, game.getComputerWords(0));
        assertCollectionsEqual(foundWords, game.getFoundWords(0));

    }


}