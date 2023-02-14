package assignment;

import java.io.IOException;
import java.util.Iterator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


//Automated unit tests for our Trie data structure
class TrieTest {


    //Test whether the Trie is able to correctly identify contained words
    @Test
    void contains() throws IOException {

        Trie t = setUpTrie();
        Assertions.assertTrue(t.contains("abcd"));
        Assertions.assertFalse(t.contains("abcz"));
        Assertions.assertFalse(t.contains(""));
        Assertions.assertFalse(t.contains("abc"));
        Assertions.assertTrue(t.contains("abd"));
        Assertions.assertFalse(t.contains("abcz"));

    }


    //Test whether the Trie is able to correctly identify contained prefixes
    @Test
    void containsPrefix() {

        Trie t = setUpTrie();
        Assertions.assertTrue(t.containsPrefix("abcd"));
        Assertions.assertFalse(t.containsPrefix("abcz"));
        Assertions.assertTrue(t.containsPrefix(""));
        Assertions.assertTrue(t.containsPrefix("abc"));
        Assertions.assertTrue(t.containsPrefix("abd"));
        Assertions.assertTrue(t.containsPrefix("aghk"));

    }


    //Test whether the Trie represents itself as a String correctly
    @Test
    void testToString() {

        Trie t = setUpTrie();
        Assertions.assertEquals("abcd\n" +
                "abce\n" +
                "abcf\n" +
                "abcfg\n" +
                "abcg\n" +
                "abd\n" +
                "abdg\n" +
                "abdh\n" +
                "aghk\n" +
                "aghkl\n" +
                "aghkm\n", t.toString());

    }


    //Test whether the Trie keeps track of the number of words correctly
    @Test
    void getSize() {
        Trie t = setUpTrie();
        Assertions.assertEquals(11, t.getSize());
    }


    //Test whether the iterator returns the words in the correct order
    @Test
    void iterator() {

        Trie t = setUpTrie();
        String[] s = {
                "abcd",
                "abce",
                "abcf",
                "abcfg",
                "abcg",
                "abd",
                "abdg",
                "abdh",
                "aghk",
                "aghkl",
                "aghkm",
        };

        //Iterate through all words in the Trie
        Iterator<String> testIterator = t.iterator();
        for (String value : s) {
            Assertions.assertEquals(value, testIterator.next());
        }
        Assertions.assertFalse(testIterator.hasNext());

    }


    //Helper method that sets up a Trie for a specific input file for other tests to use
    static Trie setUpTrie() {

        Trie t = new Trie();
        try{
            t = Trie.loadTrie("wordsLess.txt");
        } catch (IOException e) {
            System.err.println("Invalid File");
        }
        return t;

    }


}