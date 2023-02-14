package assignment;

import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;


import static org.junit.jupiter.api.Assertions.*;


public class MassTest implements Serializable {

    @Test
    void testFakeInternet() throws IOException, ClassNotFoundException {
        String[] sources = {"a.html"};
        WebCrawler.main(sources);
        WebIndex index = (WebIndex) (Index.load("index.db"));
        for (Page p : index.getPages()) {
            assertTrue(p.containsWord("body"));
            assertTrue(p.containsWord("head"));
            assertTrue(p.containsWord("coffee"));
            assertTrue(p.containsWord("place"));
            assertTrue(p.containsWord("mouse"));
            assertTrue(p.containsWord("pop"));
            assertFalse(p.containsWord(" "));
            ArrayList<String> phrase = new ArrayList<>();
            phrase.add("body");
            phrase.add("head");
            phrase.add("coffee");
            phrase.add("place");
            phrase.add("mouse");
            phrase.add("pop");
            assertTrue(p.containsPhrase(phrase));
            phrase.add(" ");
            assertFalse(p.containsPhrase(phrase));
        }
    }

    @Test
    void testQuerying() throws IOException, ClassNotFoundException {
        String[] sources = {"a.html"};
        WebCrawler.main(sources);
        WebIndex index = (WebIndex) (Index.load("index.db"));
        WebQueryEngine wqe = WebQueryEngine.fromIndex(index);
        assertEquals(10, wqe.query("body head coffee place mouse pop").size());
        assertEquals(10, wqe.query("\"body head coffee place mouse pop\"").size());
        assertEquals(10, wqe.query("body pop").size());
        Collection<Page> pages = wqe.query("\"body head coffee place mouse pop\"");
        for (Page p : pages) {
            assertTrue(index.getPages().contains(p));
        }
    }
}
