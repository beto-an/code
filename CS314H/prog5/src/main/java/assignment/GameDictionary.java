package assignment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;


public class GameDictionary implements BoggleDictionary {

    // Underlying Trie to store dictionary
    private final Trie dictionary = new Trie();

    /**
        * Loads a dictionary from a file of words
     * @param filename    the String filename
     * @throws IOException if the I/O fails
     */
    @Override
    public void loadDictionary(String filename) throws IOException {
        if (filename == null) {
            System.err.println("Dictionary file must not be null");
            return;
        }
        BufferedReader br = new BufferedReader(new FileReader(filename));
        while (br.ready()) dictionary.add(br.readLine());

    }

    /**
     * Determines whether a prefix exists within the dictionary
     * @param prefix    the String prefix to test
     * @return true if the prefix exists within the dictionary, false otherwise
     */
    @Override
    public boolean isPrefix(String prefix) {
        return dictionary.containsPrefix(prefix);
    }

    /**
     * Determines whether a word exists within the dictionary
     * @param word    the String word to test
     * @return true if the word exists within the dictionary, false otherwise
     */
    @Override
    public boolean contains(String word) {
        return dictionary.contains(word);
    }

    /**
     * Gives an iterator to iterate through the dictionary in the order in which elements were inserted (presumably alphabetical)
     * @return the iterator for the underlying trie
     */
    @Override
    public Iterator<String> iterator() {
        return dictionary.iterator();
    }

    /**
     * Prints out all of the words in the dictionary
     * @return a string containing all of the words within the dictionary in the order in which elements were inserted (presumably alphabetical)
     */
    @Override
    public String toString() {
        return dictionary.toString();
    }
}
