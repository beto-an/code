package assignment;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class TreapMapTest {

    @RepeatedTest(1)
    void lookup() {
        {
            // Randomly tests that lookup retrieves the right value, or none
            TreapMap<Integer, Integer> test = new TreapMap<>();
            int n = 10;
            HashSet<Integer> keys = new HashSet<>();
            for (int i = 0; i < n; i++) {
                int key = (int) (Math.random() * 100);
                keys.add(key);
                test.insert(key, key + 1);
            }
            System.out.println(test);
            assertCorrectness(test);
            for (int key : keys) {
                assertEquals(key + 1, test.lookup(key));
                assertCorrectness(test);
            }

            for (int key : keys) {
                test.insert(key, key - 1);
                assertEquals(key - 1, test.lookup(key));
                assertCorrectness(test);
            }
        }



    }

    @RepeatedTest(100)
    void remove() {
        // Randomly tests that remove retrieves the right value and actually removes the node from the Treap
        {
            {
                TreapMap<Integer, Integer> test = new TreapMap<>();
                int n = 10;
                HashSet<Integer> keys = new HashSet<>();
                for (int i = 0; i < n; i++) {
                    int key = (int) (Math.random() * 400);
                    keys.add(key);
                    test.insert(key, key + 1);
                    assertCorrectness(test);
                }
                assertCorrectness(test);
                for (int key : keys) {
                    assertEquals(key + 1, test.lookup(key));
                    assertCorrectness(test);
                }
                System.out.println(keys.size());
                int i = 0;
                for (int key : keys) {
                    i++;
                    System.out.println(test);
                    assertEquals(key + 1, test.remove(key));
                    assertNull(test.lookup(key));
                    assertNull(test.remove(key));
                    assertNull(test.lookup(key));
                    assertNull(test.remove(key));
                    assertCorrectness(test);
                }
                for (int key : keys) {
                    test.insert(key, key - 1);
                    assertEquals(key - 1, test.lookup(key));
                    assertEquals(key - 1, test.remove(key));
                    assertNull(test.lookup(key));
                    assertNull(test.remove(key));
                    assertNull(test.lookup(key));
                    assertNull(test.remove(key));
                    assertCorrectness(test);
                    test.insert(key, key);
                    assertEquals(key, test.lookup(key));
                    assertEquals(key, test.remove(key));
                    assertNull(test.lookup(key));
                    assertNull(test.remove(key));
                    assertNull(test.lookup(key));
                    assertNull(test.remove(key));
                    assertCorrectness(test);
                }
            }
        }
    }

    @RepeatedTest(100)
    void split() {
        // Randomly splits a treap between a key, ensuring that both subtreaps contain the correct elements
        int maxKey = 100;
        for (int splitKey = -1; splitKey <= maxKey + 1; splitKey++) {
            TreapMap<Integer, Integer> test = new TreapMap<>();
            int n = 100;
            for (int i = 0; i < n; i++) {
                int key = (int) (Math.random() * maxKey);
                test.insert(key, key);
            }
            assertCorrectness(test);

            Treap<Integer, Integer>[] splits = test.split(splitKey);
            TreapMap<Integer, Integer> lessSplit = (TreapMap<Integer, Integer>) (splits[0]);
            TreapMap<Integer, Integer> greaterSplit = (TreapMap<Integer, Integer>) (splits[1]);
            assertCorrectness(lessSplit);
            assertCorrectness(greaterSplit);

            for (int key : lessSplit) {
                assertTrue(key <= splitKey);
            }
            for (int key : greaterSplit) {
                assertTrue(key >= splitKey);
            }


        }
    }

    @RepeatedTest(100)
    void join() {
        // Joins two randomly generated treaps and checks that all elements are included and no extra elements are found
        {
            int maxKey = 20;
            for (int splitKey = 0; splitKey <= maxKey + 2; splitKey++) {
                TreapMap<Integer, Integer> lessSplit = new TreapMap<>();
                TreapMap<Integer, Integer> greaterSplit = new TreapMap<>();
                HashSet<Integer> allKeys =  new HashSet<>();
                int n = 20;
                for (int i = 0; i < Math.random() * n; i++) {
                    int lessKey = (int) (Math.random() * splitKey);
                    int greaterKey = (int) (Math.random() * (maxKey - splitKey)) + splitKey + 1;
                    lessSplit.insert(lessKey, lessKey);
                    greaterSplit.insert(greaterKey, greaterKey);
                    allKeys.add(lessKey);
                    allKeys.add(greaterKey);
                }

                lessSplit.join(greaterSplit);
                int lastKey = -1;
                for (int key : lessSplit) {
                    assertTrue(allKeys.contains(key));
                    assertTrue(key > lastKey);
                    lastKey = key;
                }
            }

        }
    }

    @RepeatedTest(100)
    void iterator() {
        // Ensures order of iteration is sorted and all keys are included
        TreapMap<Integer, Integer> test = new TreapMap<>();
        int n = 400;
        HashSet<Integer> keys = new HashSet<>();
        for (int i = 0; i < n; i++) {
            int key = (int) (Math.random() * 1000);
            keys.add(key);
            test.insert(key, key);
        }
        assertCorrectness(test);
        Integer lastKey = -1;
        for (int key : test) {
            assertTrue(keys.contains(key));
            assertTrue(lastKey < key);
            lastKey = key;
        }
    }

    @Test
    void fruitTests() {
        // idk why I made these tests, but somehow I failed them
        {
            TreapMap<String, Integer> test = new TreapMap<>();
            assertCorrectness(test);
            assertNull(test.lookup("pineapple"));
            assertCorrectness(test);

            test.insert("apple", 1);
            assertCorrectness(test);
            test.insert("orange", 2);
            assertCorrectness(test);
            test.insert("banana", 3);
            assertCorrectness(test);
            test.insert("grape", 4);
            assertCorrectness(test);

            assertEquals(1, test.lookup("apple"));
            assertCorrectness(test);
            assertEquals(2, test.lookup("orange"));
            assertCorrectness(test);
            assertEquals(3, test.lookup("banana"));
            assertCorrectness(test);
            assertEquals(4, test.lookup("grape"));
            assertCorrectness(test);
            assertNull(test.lookup("pineapple"));
            assertCorrectness(test);

            test.insert("pineapple", 5);
            assertCorrectness(test);
            assertEquals(5, test.lookup("pineapple"));
            assertCorrectness(test);

            test.insert("apple", 6);
            assertCorrectness(test);
            assertEquals(6, test.lookup("apple"));
            assertCorrectness(test);
            assertEquals(2, test.lookup("orange"));
            assertCorrectness(test);
            assertEquals(3, test.lookup("banana"));
            assertCorrectness(test);
            assertEquals(4, test.lookup("grape"));
            assertCorrectness(test);
            assertEquals(5, test.lookup("pineapple"));
            assertCorrectness(test);
        }
        {
            TreapMap<String, Integer> test = new TreapMap<>();
            assertCorrectness(test);
            test.insert("apple", 1);
            assertCorrectness(test);
            test.insert("orange", 2);
            assertCorrectness(test);
            test.insert("banana", 3);
            assertCorrectness(test);
            test.insert("grape", 4);
            assertCorrectness(test);
            assertEquals(1, test.lookup("apple"));
            assertCorrectness(test);
            assertEquals(2, test.lookup("orange"));
            assertCorrectness(test);
            assertEquals(3, test.lookup("banana"));
            assertCorrectness(test);
            assertEquals(4, test.lookup("grape"));
            assertCorrectness(test);
            assertNull(test.lookup("pineapple"));
            assertCorrectness(test);
        }
        {
            TreapMap<String, Integer> test = new TreapMap<>();
            assertCorrectness(test);
            assertNull(test.lookup("pineapple"));
            assertCorrectness(test);
            test.insert("apple", 1);
            assertCorrectness(test);
            assertEquals(1, test.lookup("apple"));
            assertCorrectness(test);
            assertNull(test.lookup("pineapple"));
            assertCorrectness(test);
        }
        {
            TreapMap<String, Integer> test = new TreapMap<>();
            assertCorrectness(test);
            test.insert("apple", 1);
            assertCorrectness(test);
            test.insert("orange", 2);
            assertCorrectness(test);
            test.insert("banana", 3);
            assertCorrectness(test);
            test.insert("grape", 4);
            assertCorrectness(test);

            assertEquals(1, test.lookup("apple"));
            assertCorrectness(test);
            assertEquals(2, test.lookup("orange"));
            assertCorrectness(test);
            assertEquals(3, test.lookup("banana"));
            assertCorrectness(test);
            assertEquals(4, test.lookup("grape"));
            assertCorrectness(test);
            assertNull(test.remove("pineapple"));
            assertCorrectness(test);

            assertEquals(1, test.remove("apple"));
            assertCorrectness(test);
            assertEquals(2, test.lookup("orange"));
            assertCorrectness(test);
            assertEquals(3, test.lookup("banana"));
            assertCorrectness(test);
            assertEquals(4, test.lookup("grape"));
            assertCorrectness(test);
            assertNull(test.lookup("pineapple"));
            assertCorrectness(test);

            assertNull(test.lookup("apple"));
            assertCorrectness(test);
            assertEquals(2, test.remove("orange"));
            assertCorrectness(test);
            assertEquals(3, test.lookup("banana"));
            assertCorrectness(test);
            assertEquals(4, test.lookup("grape"));
            assertCorrectness(test);
            assertNull(test.lookup("pineapple"));
            assertCorrectness(test);


            assertNull(test.lookup("apple"));
            assertCorrectness(test);
            assertNull(test.lookup("orange"));
            assertCorrectness(test);
            assertEquals(3, test.remove("banana"));
            assertCorrectness(test);
            assertEquals(4, test.lookup("grape"));
            assertCorrectness(test);
            assertNull(test.lookup("pineapple"));
            assertCorrectness(test);

            assertNull(test.lookup("apple"));
            assertCorrectness(test);
            assertNull(test.lookup("orange"));
            assertCorrectness(test);
            assertNull(test.lookup("banana"));
            assertCorrectness(test);
            assertEquals(4, test.remove("grape"));
            assertCorrectness(test);
            assertNull(test.lookup("pineapple"));
            assertCorrectness(test);

            assertNull(test.lookup("apple"));
            assertCorrectness(test);
            assertNull(test.lookup("orange"));
            assertCorrectness(test);
            assertNull(test.lookup("banana"));
            assertCorrectness(test);
            assertNull( test.lookup("grape"));
            assertCorrectness(test);
            assertNull(test.lookup("pineapple"));
            assertCorrectness(test);
        }

        {
            TreapMap<String, Integer> test = new TreapMap<>();
            assertCorrectness(test);
            test.insert("apple", 1);
            assertCorrectness(test);
            test.insert("orange", 2);
            assertCorrectness(test);
            test.insert("banana", 3);
            assertCorrectness(test);
            test.insert("grape", 4);
            assertCorrectness(test);
            System.out.println(test);

            assertEquals(1, test.lookup("apple"));
            assertCorrectness(test);
            assertEquals(2, test.lookup("orange"));
            assertCorrectness(test);
            assertEquals(3, test.lookup("banana"));
            assertCorrectness(test);
            assertEquals(4, test.lookup("grape"));
            assertCorrectness(test);
            assertNull(test.remove("pineapple"));
            assertCorrectness(test);

            assertEquals(1, test.remove("apple"));
            assertCorrectness(test);
            assertEquals(2, test.lookup("orange"));
            assertCorrectness(test);
            assertEquals(3, test.lookup("banana"));
            assertCorrectness(test);
            assertEquals(4, test.lookup("grape"));
            assertCorrectness(test);
            assertNull(test.lookup("pineapple"));
            assertCorrectness(test);

            assertNull(test.lookup("apple"));
            assertCorrectness(test);
            assertEquals(2, test.remove("orange"));
            assertCorrectness(test);
            assertEquals(3, test.lookup("banana"));
            assertCorrectness(test);
            assertEquals(4, test.lookup("grape"));
            assertCorrectness(test);
            assertNull(test.lookup("pineapple"));
            assertCorrectness(test);
            test.insert("apple", 5);
            assertCorrectness(test);

            assertEquals(5, test.lookup("apple"));
            assertCorrectness(test);
            assertNull(test.lookup("orange"));
            assertCorrectness(test);
            assertEquals(3, test.remove("banana"));
            assertCorrectness(test);
            assertEquals(4, test.lookup("grape"));
            assertCorrectness(test);
            assertNull(test.lookup("pineapple"));
            assertCorrectness(test);


            assertEquals(5, test.lookup("apple"));
            assertCorrectness(test);
            assertNull(test.lookup("orange"));
            assertCorrectness(test);
            assertNull(test.lookup("banana"));
            assertCorrectness(test);
            assertEquals(4, test.remove("grape"));
            assertCorrectness(test);
            assertNull(test.lookup("pineapple"));
            assertCorrectness(test);
            assertEquals(5, test.lookup("apple"));
            assertCorrectness(test);
            assertNull(test.lookup("orange"));
            assertCorrectness(test);
            assertNull(test.lookup("banana"));
            assertCorrectness(test);
            assertNull( test.lookup("grape"));
            assertCorrectness(test);
            assertNull(test.lookup("pineapple"));
            assertCorrectness(test);

            assertEquals(5, test.remove("apple"));
            assertCorrectness(test);
            assertNull(test.lookup("orange"));
            assertCorrectness(test);
            assertNull(test.lookup("banana"));
            assertCorrectness(test);
            assertNull( test.lookup("grape"));
            assertCorrectness(test);
            assertNull(test.lookup("pineapple"));
            assertCorrectness(test);


            test.insert("apple", 5);
            assertCorrectness(test);
            assertEquals(5, test.lookup("apple"));
            assertCorrectness(test);
            assertEquals(5, test.remove("apple"));
            assertCorrectness(test);
            assertNull(test.lookup("apple"));
            assertCorrectness(test);

        }
    }

    @Test
    void edgeCases() {

        // Edge cases involving null or empty inputs
        TreapMap<Integer, Integer> test = new TreapMap<>();
        TreapMap<Integer, Integer> test2 = new TreapMap<>();
        test.insert(1, 1);
        test.lookup(null);
        test.insert(null, null);
        test.lookup(null);
        test.remove(null);
        test.lookup(null);
        test.join(null);
        System.out.println(test.split(null)[0]);
        test.join(test2);
        for (Integer a : test2) {
            assertNull(a);
        }
    }

    <K extends Comparable<K>, V> void assertCorrectness(TreapMap<K, V> test) {
        // Checks that TreapMap satisfies both BST and heap property
        assertValidHeap(test);
        assertValidBST(test);
    }

    <K extends Comparable<K>, V> void assertValidHeap(TreapMap<K, V> test) {
        assertValidHeapNode(test.root);
    }
    <K extends Comparable<K>, V> void assertValidBST(TreapMap<K, V> test) {
        assertValidBST(test.root);
    }

    <K extends Comparable<K>, V> void assertValidHeapNode(TreapNode<K, V> test) {
        if (test == null) {
            return;
        }
        if (test.left != null) {
            assertTrue(test.priority >= test.left.priority);
            assertValidHeapNode(test.left);
        }
        if (test.right != null) {
            assertTrue(test.priority >= test.right.priority);
            assertValidHeapNode(test.right);
        }
    }

    <K extends Comparable<K>, V> void assertValidBST(TreapNode<K, V> test) {
        if (test == null) {
            return;
        }
        if (test.left != null) {
            assertTrue(test.left.key.compareTo(test.key) <= 0);
            assertValidBST(test.left);
        }
        if (test.right != null) {
            assertTrue(test.right.key.compareTo(test.key) >= 0);
            assertValidBST(test.right);

        }
    }

    @RepeatedTest(100)
    void rotateTest() {
        {
            // Randomly tests that rotations maintain the BST property
            TreapMap<Integer, Integer> test = new TreapMap<>();
            int n = 10;
            HashSet<Integer> keys = new HashSet<>();
            for (int i = 0; i < n; i++) {
                int key = (int) (Math.random() * 100);
                keys.add(key);
                test.insert(key, key + 1);
            }
            System.out.println(test);
            assertCorrectness(test);
            for (int key : keys) {
                test.find(key).rotateLeft();
                assertValidBST(test);
            }
            for (int key : keys) {
                test.find(key).rotateRight();
                assertValidBST(test);
            }

            for (int key : keys) {
                test.insert(key, key - 1);
                assertEquals(key - 1, test.lookup(key));
            }
        }
    }

    @RepeatedTest(100)
    void swimsinkTest() {
        {
            // Randomly tests that sinking and swimming maintain the heap property
            TreapMap<Integer, Integer> test = new TreapMap<>();
            int n = 10;
            HashSet<Integer> keys = new HashSet<>();
            for (int i = 0; i < n; i++) {
                int key = (int) (Math.random() * 100);
                keys.add(key);
                test.insert(key, key + 1);
            }
            System.out.println(test);
            assertCorrectness(test);
            for (int key : keys) {
                TreapNode<Integer, Integer> currentNode = test.find(key);
                currentNode.setPriority((int) (Math.random() * Treap.MAX_PRIORITY));
                test.swim(currentNode);
                test.sink(currentNode);
                assertCorrectness(test);
            }

            for (int key : keys) {
                test.insert(key, key - 1);
                assertEquals(key - 1, test.lookup(key));
            }
        }
    }
}