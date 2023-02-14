package assignment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;


// A String Trie
public class Trie implements Iterable<String> {

    // Character Stored in any Sentinel Nodes
    public static char SENTINEL_CHAR = '$';

    // Sentinel representing the start node of the Trie
    public StartNode START = new StartNode(SENTINEL_CHAR);

    // Sentinel representing the end node of the Trie
    public EndNode END = new EndNode(SENTINEL_CHAR);


    /**
     * Adds a String to the Trie
     * @param s String to be added
     */
    public void add(String s) {
        if (s == null) {
            return;
        }
        String sLower = s.toLowerCase();
        CharNode lastNode = START;
        for (int i = 0; i < sLower.length(); i++) {
            char letter = sLower.charAt(i);
            CharNode nextLetter = lastNode.get(letter);
            // If next letter doesn't exist, add to trie, otherwise add to next letter
            if (nextLetter == null) {
                lastNode.addNextChar(letter);
                lastNode = lastNode.get(letter);
            } else {
                lastNode = nextLetter;
            }
        }
        lastNode.addNextChar(END);
    }

    /**
     * Adds a collection of strings to the Trie
     * @param s collection of strings to be added
     */
    public void add(Collection<String> s) {
        if (s == null) {
            return;
        }
        for (String word : s) {
            add(word);
        }
    }

    /**
     * Adds an array of strings to the Trie
     * @param s array of strings to be added to the Trie
     */
    public void add(String[] s) {
        if (s == null) {
            return;
        }
        for (String word : s) {
            add(word);
        }
    }


    /**
     * Determines if a string is contained in the Trie
     * @param s String to be checked
     * @return true if s belongs to the Trie, false otherwise
     */
    public boolean contains(String s) {
        if (s == null) {
            return false;
        }
        String sLower = s.toLowerCase();
        CharNode lastNode = START;

        // Recursively search down the trie unless we either find the prefix (true) or a letter doesn't exist (false)
        for (int i = 0; i < sLower.length(); i++) {
            char letter = sLower.charAt(i);
            CharNode nextLetter = lastNode.get(letter);

            if (nextLetter == null) {
                return false;
            } else {
                lastNode = nextLetter;
            }
        }
        return lastNode.containsChar(SENTINEL_CHAR);  // Prefix must also end
    }

    /**
     * Determines if a string prefix is contained in the Trie
     * @param s String prefix to be checked
     * @return true if s belongs to the Trie, false otherwise
     */
    public boolean containsPrefix(String s) {
        if (s == null) {
            return false;
        }
        String sLower = s.toLowerCase();
        CharNode lastNode = START;
        for (int i = 0; i < sLower.length(); i++) {
            char letter = sLower.charAt(i);
            CharNode nextLetter = lastNode.get(letter);

            if (nextLetter == null) {
                return false;
            } else {
                lastNode = nextLetter;
            }
        }
        return true;
    }

    /**
     * Gives all the strings stored in the Trie
     * @return all the strings stored in the Trie
     */
    @Override
    public String toString() {
        StringBuilder wordList = new StringBuilder();
        String newLine = "\n";
        for (String s : this) {
            wordList.append(s);
            wordList.append(newLine);
        }
        return wordList.toString();
    }

    /**
     * Gives the size of the of Trie
     * @return the number of strings contained within the Trie
     */
    public int getSize() {
        return END.getSize();
    }

    /**
     * Returns an Iterator over the Trie
     * @return an Iterator over the Trie
     */
    @Override
    public Iterator<String> iterator() {
        return new TrieIterator();
    }

    // An Iterator over a Trie
    class TrieIterator implements Iterator<String> {

        int iteration = 0;

        @Override
        public boolean hasNext() {
            return iteration < getSize();
        }

        @Override
        public String next() {
            StringBuilder sb = new StringBuilder();
            CharNode currentNode = END.getNodeAt(iteration);
            while (currentNode != START) {
                sb.append(currentNode.getChar());
                currentNode = currentNode.getParent();
            }
            iteration++;
            return sb.reverse().toString();
        }
    }

    public static Trie loadTrie(String filename) throws IOException {
        Trie t = new Trie();
        BufferedReader br = new BufferedReader(new FileReader(filename));
        while (br.ready()) t.add(br.readLine());
        return t;
    }

}
