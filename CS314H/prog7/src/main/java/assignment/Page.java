package assignment;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * The Page class holds anything that the QueryEngine returns to the server.  The field and method
 * we provided here is the bare minimum requirement to be a Page - feel free to add anything you
 * want as long as you don't break the getURL method.
 *
 * TODO: Implement this!
 */
public class Page implements Serializable {
    // The URL the page was located at.
    private final URL url;

    private final HashSet<String> allWords = new HashSet<>();
    private final HashMap
            <String, HashSet<Long>> wordIndex = new HashMap<>();

    private long wordNumber = 0;


    /**
     * Creates a Page with a given URL.
     * @param url The url of the page.
     */
    public Page(URL url) {
        this.url = url;
    }


    /**
     * Parses a subsection of the page, as specified by CrawlingMarkupHandler
     * @param ch      buffer containing characters; do not modify this buffer
     * @param start   location of 1st character in ch
     * @param length  number of characters in ch
     */
    public void parseURL(char[] ch, int start, int length, int line, int col) {
        StringBuilder lastWord = new StringBuilder();

        for (int i = start; i < start + length; i++) {

            // Splits word if char is not alphanumeric, "'", or "-". Also assigns a location for each word within the page

            if (isValidWordChar(ch[i])) {
                lastWord.append(ch[i]);
            } else {
                if (lastWord.length() == 0) {
                    continue;
                }
                String word = lastWord.toString().toLowerCase();
                allWords.add(word);
                HashSet<Long> wordLocations = wordIndex.get(word);
                if (wordLocations == null) {
                    wordLocations = new HashSet<>();
                    wordLocations.add(wordNumber);
                    wordIndex.put(word, wordLocations);
                } else {
                    wordLocations.add(wordNumber);
                }

                wordNumber++;
                lastWord.setLength(0);
            }
        }
        if (lastWord.length() != 0) {
            String word = lastWord.toString().toLowerCase();
            allWords.add(word);
            HashSet<Long> wordLocations = wordIndex.get(word);
            if (wordLocations == null) {
                wordLocations = new HashSet<>();
                wordLocations.add(wordNumber);
                wordIndex.put(word, wordLocations);
            } else {
                wordLocations.add(wordNumber);
            }
        }

        wordNumber += 2;
    }

    public boolean containsWord(String word) {
        return wordIndex.containsKey(word);
    }

    public boolean containsPhrase(ArrayList<String> phrase) {
        if (phrase.size() == 0) {
            return true;
        } else {
            String firstWord = phrase.get(0);
            HashSet<Long> locations = wordIndex.get(firstWord);
            if (locations == null) {
                return false;
            }
            // Checks for phrases in all possible location of the first word
            for (Long location : locations) {
                if (containsPhrase(phrase, 1, location + 1)) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * Recursively checks if the phrase is present at a location
     */
    public boolean containsPhrase(ArrayList<String> phrase, int index, long location) {
        if (index >= phrase.size()) {
            return true;
        } else {
            // Check if the current word belongs in the page
            String nextWord = phrase.get(index);
            HashSet<Long> locations = wordIndex.get(nextWord);
            if (locations == null) {
                return false;
            }
            if (!locations.contains(location)) {
                return false;
            }

            //Recursively check if the remaining words belong in the page
            return containsPhrase(phrase, index + 1, location + 1);
        }
    }

    public HashSet<String> getAllWords() {
        return allWords;
    }

    /**
     * @return the URL of the page.
     */
    public URL getURL() { return url; }

    public static boolean isValidWordChar(char c) {
        if (c == 39) {
            return true;
        }
        if (c == 45) {
            return true;
        }
        if (c - 48 >= 0 && c - 57 <= 0) {
            return true;
        }
        if (c - 64 >= 0 && c - 90 <= 0) {
            return true;
        }
        if (c - 97 >= 0 && c - 122 <= 0) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return url.toString();
    }
}
