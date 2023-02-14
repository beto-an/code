package assignment;

import java.util.*;

/**
 * A web-index which efficiently stores information about pages. Serialization is done automatically
 * via the superclass "Index" and Java's Serializable interface.
 *
 * TODO: Implement this!
 */
public class WebIndex extends Index {
    /**
     * Needed for Serialization (provided by Index) - don't remove this!
     */
    private static final long serialVersionUID = 1L;

    private final HashSet<Page> allPages = new HashSet<>();

    private final HashMap<String, HashSet<Page>> pagesWithWord = new HashMap<>();

    // TODO: Implement all of this! You may choose your own data structures an internal APIs.
    // You should not need to worry about serialization (just make any other data structures you use
    // here also serializable - the Java standard library data structures already are, for example).

    public WebIndex() {
    }

    public void addPage(Page page) {
        HashSet<String> newWords = page.getAllWords();

        // Checking for words found in the current page
        for (String word : newWords) {
            HashSet<Page> pages = pagesWithWord.get(word);
            if (pages == null) { // Word hasn't been seen yet
                pages = new HashSet<>();
                pages.add(page);
            } else { // Word has already been seen
                pages.add(page);
            }
            pagesWithWord.put(word, pages);
        }
        allPages.add(page);
    }


    public HashSet<Page> getPages() {
        return allPages;
    }

    public boolean wordInPage(String word, Page page) {
        return page.containsWord(word);
    }

    public boolean phraseInPage(ArrayList<String> phrase, Page page) {
        return page.containsPhrase(phrase);
    }

    @Override
    public String toString() {
        return "Pages Detected: " + allPages.size() + "\n" +
                "Pages Found: " + allPages + "\n";
    }
}
