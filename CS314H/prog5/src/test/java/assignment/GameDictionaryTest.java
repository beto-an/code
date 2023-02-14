package assignment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


//Class to run automated unit tests on our GameDictionary helper methods
class GameDictionaryTest {


    //Test whether the dictionary is loaded without problems
    @Test
    void loadDictionary() throws IOException {

        //List of file paths to read from
        String[] dictFilePath = {
                "wordsLess",
                "words",
                "fewWords",
                "oneWord",
                "empty",
                "sameWord",
                "ALLCAPS",
                "alllower",
                "mIxCaSe",
                "wordTest",
        };

        //Read from all the paths
        GameDictionary gd = new GameDictionary();
        for (String s : dictFilePath) {
            gd.loadDictionary("testDictionaries/" + s + ".txt");
        }

    }


    //Test whether the dictionary correctly checks for prefixes
    @Test
    void isPrefix() throws IOException {

        //Load the dictionary with a test file
        String filename = "testDictionaries/wordTest.txt";
        GameDictionary gd = new GameDictionary();
        gd.loadDictionary(filename);

        //Test all prefixes of all the words
        ArrayList<String> allWords = getWords(filename);
        for (String word : allWords) {
            for (int i = 0; i < word.length(); i++) {
                String subWord = word.substring(0, i);
                Assertions.assertTrue(gd.isPrefix(subWord), subWord);
            }
        }

        //A list of special characters
        String[] addStrings = {" ",
                "\n",
                "\t",
                "\f",
                "\u001C",
                "\u000B",
        };

        //Test that words + special characters aren't a prefix
        for (String word : allWords) {
            for (String add : addStrings) {
                Assertions.assertFalse(gd.isPrefix(word + add), word + add);
            }
        }

        //Test some individual cases
        Assertions.assertTrue(gd.isPrefix("unequivocal"));
        Assertions.assertFalse(gd.isPrefix(" "));
        Assertions.assertFalse(gd.isPrefix("\n"));
        Assertions.assertFalse(gd.isPrefix("\t"));
        Assertions.assertFalse(gd.isPrefix("\f"));
        Assertions.assertFalse(gd.isPrefix("\u001C"));
        Assertions.assertFalse(gd.isPrefix("\u000B"));

    }


    //Test that the dictionary correctly checks for contained words
    @Test
    void contains() throws IOException {

        //Load the dictionary with a test file
        String filename = "testDictionaries/wordTest.txt";
        GameDictionary gd = new GameDictionary();
        gd.loadDictionary(filename);

        //Get all the words from the file and check that the dictionary contains each word
        //Check that the dictionary also doesn't return true for pure prefixes
        ArrayList<String> allWords = getWords(filename);
        HashSet<String> fullWords = new HashSet<>(allWords);
        for (String word : allWords) {
            for (int i = 0; i < word.length(); i++) {
                String subWord = word.substring(0, i);
                if (fullWords.contains(subWord)) {
                    Assertions.assertTrue(gd.contains(subWord), subWord);
                } else {
                    Assertions.assertFalse(gd.contains(subWord), subWord);
                }
            }
        }

        //A list of special characters
        String[] addStrings = {" ",
                "\n",
                "\t",
                "\f",
                "\u001C",
                "\u000B",
        };

        //Test that words + a special character aren't contained
        for (String word : allWords) {
            for (String add : addStrings) {
                Assertions.assertFalse(gd.contains(word + add), word + add);
            }
        }

        //Test some individual cases
        Assertions.assertFalse(gd.contains("unequivocal"));
        Assertions.assertFalse(gd.contains("badd"));
        Assertions.assertFalse(gd.contains(""));
        Assertions.assertFalse(gd.contains(" "));
        Assertions.assertFalse(gd.contains("\n"));
        Assertions.assertFalse(gd.contains("\t"));
        Assertions.assertFalse(gd.contains("\f"));
        Assertions.assertFalse(gd.contains("\u001C"));
        Assertions.assertFalse(gd.contains("\u000B"));

    }


    //Test that the iterator returns the list of words correctly
    @Test
    void iterator() throws IOException {

        //Load the dictionary with a test file
        String filename = "testDictionaries/wordTest.txt";
        GameDictionary gd = new GameDictionary();
        gd.loadDictionary(filename);

        //Test that the iterator returns words in the correct order
        ArrayList<String> allWords = getWords(filename);
        Iterator<String> wordList = gd.iterator();
        for (String word : allWords) {
            Assertions.assertEquals(word, wordList.next());
        }

        //Test that the iterator has completed
        Assertions.assertFalse(wordList.hasNext());

    }


    //Helper method for returning a list of all words in a file
    public static ArrayList<String> getWords(String filename) throws IOException {

        //Read input data from a file
        BufferedReader br = new BufferedReader(new FileReader(filename));
        ArrayList<String> words = new ArrayList<>();
        while (br.ready()) {
            words.add(br.readLine());
        }

        //Sort the words alphabetically
        words.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });

        return words;

    }


}