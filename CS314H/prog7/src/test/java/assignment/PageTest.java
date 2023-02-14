package assignment;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class PageTest {
    Page page;


    @Test
    void parseURL() {
        page = new Page(null);
        char[] c = "There are many words in this sentence".toCharArray();
        page.parseURL(c, 0, c.length, 0, 0);
        HashSet<String> words = page.getAllWords();
        assertTrue(words.contains("there"));
        assertTrue(words.contains("are"));
        assertTrue(words.contains("many"));
        assertTrue(words.contains("words"));
        assertTrue(words.contains("in"));
        assertTrue(words.contains("this"));
        assertTrue(words.contains("sentence"));
    }

    @Test
    void containsWord() {
        page = new Page(null);
        char[] c = "There are many words in this sentence".toCharArray();
        page.parseURL(c, 0, c.length, 0, 0);
        assertTrue(page.containsWord("there"));
        assertTrue(page.containsWord("are"));
        assertTrue(page.containsWord("many"));
        assertTrue(page.containsWord("words"));
        assertTrue(page.containsWord("in"));
        assertTrue(page.containsWord("this"));
        assertTrue(page.containsWord("sentence"));
        assertFalse(page.containsWord("in this"));
        assertFalse(page.containsWord("i"));
        assertFalse(page.containsWord("There"));
    }

    @Test
    void containsPhrase() {
        page = new Page(null);
        char[] c = "There are many words in this sentence".toCharArray();
        page.parseURL(c, 0, c.length, 0, 0);
        ArrayList<String> phrase = new ArrayList<>();
        phrase.add("many");
        phrase.add("words");
        phrase.add("in");
        assertTrue(page.containsPhrase(phrase));
        phrase.add("thiS");
        assertFalse(page.containsPhrase(phrase));
        phrase.remove(3);
        phrase.add("this");
        phrase.add("sentence");
        assertTrue(page.containsPhrase(phrase));
        phrase.add(" ");
        assertFalse(page.containsPhrase(phrase));
    }
}