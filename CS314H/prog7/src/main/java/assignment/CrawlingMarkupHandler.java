package assignment;

import java.io.File;
import java.io.PrintStream;
import java.util.*;
import java.net.*;
import org.attoparser.simple.*;

/**
 * A markup handler which is called by the Attoparser markup parser as it parses the input;
 * responsible for building the actual web index.
 *
 * TODO: Implement this!
 */
public class CrawlingMarkupHandler extends AbstractSimpleMarkupHandler {

    static final HashSet<String> NO_PARSE_TAGS = new HashSet<>();

    static {
        // Tags that shouldn't be parsed
        NO_PARSE_TAGS.add("script");
        NO_PARSE_TAGS.add("style");
    }

    PrintStream err = System.err;

    HashSet<URL> newURLs = new HashSet<>();

    private Page currentPage;

    private final WebIndex wordIndex = new WebIndex();

    private URL currentURL;

    private boolean parseText = true;

    private final HashSet<URL> visitedURLs = new HashSet<>();

    public CrawlingMarkupHandler() {}

    /**
    * This method returns the complete index that has been crawled thus far when called.
    */
    public Index getIndex() {
        return wordIndex;
    }

    /**
    * This method returns any new URLs found to the Crawler; upon being called, the set of new URLs
    * should be cleared.
    */
    public List<URL> newURLs() {
        return new ArrayList<>(newURLs);
    }

    /*
    * These are some of the methods from AbstractSimpleMarkupHandler.
    * All of its method implementations are NoOps, so we've added some things
    * to do; please remove all the extra printing before you turn in your code.
    *
    * Note: each of these methods defines a line and col param, but you probably
    * don't need those values. You can look at the documentation for the
    * superclass to see all of the handler methods.
    */

    /**
    * Called when the parser first starts reading a document.
    * @param startTimeNanos  the current time (in nanoseconds) when parsing starts
    * @param line            the line of the document where parsing starts
    * @param col             the column of the document where parsing starts
    */
    public void handleDocumentStart(long startTimeNanos, int line, int col) {
        newURLs.clear();
        currentPage = new Page(currentURL);
    }

    /**
    * Called when the parser finishes reading a document.
    * @param endTimeNanos    the current time (in nanoseconds) when parsing ends
    * @param totalTimeNanos  the difference between current times at the start
    *                        and end of parsing
    * @param line            the line of the document where parsing ends
    * @param col             the column of the document where the parsing ends
    */
    public void handleDocumentEnd(long endTimeNanos, long totalTimeNanos, int line, int col) {
        wordIndex.addPage(currentPage);
        visitedURLs.addAll(newURLs);
    }

    /**
    * Called at the start of any tag.
    * @param elementName the element name (such as "div")
    * @param attributes  the element attributes map, or null if it has no attributes
    * @param line        the line in the document where this element appears
    * @param col         the column in the document where this element appears
    */
    public void handleOpenElement(String elementName, Map<String, String> attributes, int line, int col) {
        if (attributes != null) {
            for (String key: attributes.keySet()) {
                if (NO_PARSE_TAGS.contains(key)) { // If current element shouldn't be parsed, modify field to represent
                    parseText = false;
                }
                if (key.equals("href")) {
                    try {
                        // Add unseen URLs to the new URL list
                        URL url = new URL(currentURL, removeHashtag(attributes.get("href")));
                        File file = new File(url.getPath());
                        if (!(visitedURLs.contains(url) || newURLs.contains(url)) && file.exists()) {
                            newURLs.add(url);
                        }
                    } catch (MalformedURLException e) {
                        err.printf("Error: URL '%s' was malformed and will be ignored!%n", currentURL.getPath() + removeHashtag(attributes.get("href")));
                    }
                }
            }
        }

    }

    /**
    * Called at the end of any tag.
    * @param elementName the element name (such as "div").
    * @param line        the line in the document where this element appears.
    * @param col         the column in the document where this element appears.
    */
    public void handleCloseElement(String elementName, int line, int col) {
        parseText = true;
    }


    /**
     * Removes the hashtags in a URL
     * @param ending the URL
     * @return the URL without the hashtag
     */
    public String removeHashtag(String ending) {
        for (int i = ending.length() - 1; i >= 0; i--) {
            if (ending.charAt(i) == '?') {
                return ending.substring(0, i);
            }
            if (ending.charAt(i) == '#') {
                return removeHashtag(ending.substring(0, i));
            }
        }
        return ending;
    }

    /**
    * Called whenever characters are found inside a tag. Note that the parser is not
    * required to return all characters in the tag in a single chunk. Whitespace is
    * also returned as characters.
    * @param ch      buffer containing characters; do not modify this buffer
    * @param start   location of 1st character in ch
    * @param length  number of characters in ch
    */
    public void handleText(char[] ch, int start, int length, int line, int col) {
        // TODO: Implement this.
        if (parseText) {
            // Let the page internally parse the text
            currentPage.parseURL(ch, start, length, line, col);
        }
    }

    void setCurrentURL(URL url) {
        visitedURLs.add(currentURL);
        currentURL = url;
    }

    HashSet<URL> getVisitedURLs() {
        return visitedURLs;
    }

    void setErrorStream(PrintStream err) {
        this.err = err;
    }

    @Override
    public String toString() {
        return wordIndex.toString();
    }

    public URL getCurrentURL() {
        return currentURL;
    }

}
