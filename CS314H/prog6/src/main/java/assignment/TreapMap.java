package assignment;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class TreapMap<K extends Comparable<K>, V> implements Treap<K, V> {


    // The root of the TreapMap
    public TreapNode<K, V> root;

    // The number of changes (used to determine whether a ConcurrentModificationException is thrown)
    private int numChanges;

    /**
     * Constructs an empty TreapMap with null root
     */
    public TreapMap(){}

    /**
     * Constructs a TreapMap with specified root
     * @param root The node to set as the root of the TreapMap
     */
    public TreapMap(TreapNode<K, V> root) {
        this.root = root;
        if (root != null) {
            root.setParent(null);
        }
    }

    /**
     * Find the value with the specified key
     * @param key   the key whose associated value
     *              should be retrieved
     * @return the corresponding value
     */
    @Override
    public V lookup(K key) {
        if (key == null) {
            return null;
        }

        TreapNode<K, V> targetNode = find(key); // Finds equal/nearest node

        if (targetNode == null) {
            System.err.println("Treap is empty");
            return null;
        }

        if (targetNode.compareKey(key) == 0) { // Checks that result of find() is equal to the key
            return targetNode.getValue();
        }

        System.err.println("Key " + key + " does not exist within Treap");
        return null;
    }

    /**
     * Inserts a key value pair into the Treap
     * @param key      the key to add to this dictionary
     * @param value    the value to associate with the key
     */
    @Override
    public void insert(K key, V value) {
        if (key == null) {
            System.err.println("Cannot insert a null key into the Treap");
            return;
        }
        if (value == null) {
            System.err.println("Cannot insert a null value into the Treap");
            return;
        }

        TreapNode<K, V> newNode = new TreapNode<>(key, value);
        insertNode(newNode);
    }

    /**
     * Inserts a TreapNode into the Treap
     * @param newNode the node to be inserted
     */
    private void insertNode(TreapNode<K, V> newNode) {
        TreapNode<K, V> targetNode = find(newNode.getKey()); // Finds the node with the closest key and an available spot to insert
        if (targetNode == null) {
            root = newNode;
            return;
        }
        // If key is equal to the target, modify the value and priority. Otherwise, set as corresponding child,
        // connect to parent, and swim upwards to correct heap position.
        if (targetNode.compareKey(newNode.getKey()) == 0) {
            targetNode.setValue(newNode.getValue());
            return;
        } else if (targetNode.compareKey(newNode.getKey()) > 0) {
            targetNode.setLeft(newNode);
        } else {
            targetNode.setRight(newNode);
        }

        newNode.setParent(targetNode);
        swim(newNode);
        numChanges++;
    }

    /**
     * Removes the node with the given key
     * @param key      the key to remove
     * @return the value of the removed key
     */
    @Override
    public V remove(K key) {
        if (key == null) {
            System.err.println("Cannot remove a null key from the Treap");
            return null;
        }

        TreapNode<K, V> targetNode = find(key);  // Find the closest node to the current node.
        if (targetNode == null) {
            System.err.println("Treap is empty");
            return null;
        }
        if (targetNode.compareKey(key) != 0) {  // If closest node has different key, key doesn't exist in the treap, return null
            System.err.println("No such node found with key value " + key);
            return null;
        }

        numChanges++;
        return removeNode(targetNode);
    }

    /**
     * Removes a specified node from the TreapMap
     * @param node the node to remove
     * @return the value of the removed node
     */
    private V removeNode(TreapNode<K, V> node) {
        node.priority = -1;
        // Sink the node and remove child pointer from the heap
        sink(node);
        if (root == node) {
            root = null;
        } else if (node.isLeftChild()) {
            node.getParent().setLeft(null);
        } else {
            node.getParent().setRight(null);
        }

        return node.getValue();
    }

    /**
     * Splits the current TreapMap along the given key
     * @param key    the key to split the treap with
     * @return the results of the two TreapMaps
     */
    @Override
    public Treap<K, V>[] split(K key) {
        if (key == null) {
            return new TreapMap[]{null, null};
        }

        TreapNode<K, V> originalNode = find(key);
        if (originalNode == null) {
            return new TreapMap[]{new TreapMap<K, V>(), new TreapMap<K, V>()};
        }

        if (originalNode.compareKey(key) == 0) { // If key already exists within the TreapMap
            removeNode(originalNode);
        }

        // Insert the splitter node and its two children should be the splits
        TreapNode<K, V> splitterNode = new TreapNode<>(key, originalNode.value, Treap.MAX_PRIORITY);
        insertNode(splitterNode);

        TreapMap<K, V> right = new TreapMap<>(root.right);

        if (originalNode.compareKey(key) == 0) {
            right.insert(key, originalNode.value); // Reinsert back to right subtreap if needed
        }

        return new TreapMap[]{new TreapMap<>(root.left), right};
    }

    /**
     * Joins the current TreapMap with another TreapMap
     * @param t    the treap to join with
     */
    @Override
    public void join(Treap<K, V> t) {
        if (!(t instanceof TreapMap)) {
            return;
        }

        TreapNode<K, V> oldRoot = root;
        TreapNode<K, V> rightRoot = ((TreapMap<K, V>) t).root;
        if (root == null) { // If current Treap is empty, set to other Treap
            root = rightRoot;
            return;
        } else if (rightRoot == null) { // If other Treap is empty, do nothing
            return;
        }

        TreapNode<K, V> newRoot = new TreapNode<>(oldRoot.key, null, MAX_PRIORITY);

        this.root = newRoot;

        // Reconnect pointers of both Treaps
        root.setLeft(oldRoot);
        root.setRight(rightRoot);
        oldRoot.setParent(root);
        rightRoot.setParent(root);

        removeNode(newRoot);
    }

    @Override
    public void meld(Treap<K, V> t) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void difference(Treap<K, V> t) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        return new TreapMapIterator(numChanges);
    }

    @Override
    public double balanceFactor() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    /**
     * Gives the string form of the TreapMap
     * @return the string form of the TreapMap
     */
    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        addStringOf(output, root, 0);
        return output.toString();
    }

    /**
     * Recursively constructs the string form of a TreapMap subtree
     * @param traversal StringBuilder to add the TreapMap subtree
     * @param node Root of the subtree
     * @param tabs Current depth of node
     */
    public void addStringOf(StringBuilder traversal, TreapNode<K, V> node, int tabs) {
        if (node == null) {
            return;
        }

        traversal.append("\t".repeat(Math.max(0, tabs)));
        traversal.append(node);

        //Recursively generate the strings for the left and right subtreaps
        addStringOf(traversal, node.left, tabs + 1);
        addStringOf(traversal, node.right, tabs +  1);
    }

    /**
     * Makes the node swim upwards to retain heap property. Uses rotations.
     * @param node the node to swim upwards
     */
    void swim(TreapNode<K, V> node) {
        while (true) {
            // Check if the node is the parent
            if (node.parent == null) {
                root = node;
                break;
            }

            // Rotate upwards if the priority of parent is less than that of the current node
            if (node.parent.comparePriority(node) < 0) {
                // Determine the direction to rotate
                if (node.isLeftChild()) {
                    node.getParent().rotateRight();
                } else {
                    node.getParent().rotateLeft();
                }
            } else {
                break;
            }

        }
    }

    /**
     * Makes a node sink downwards to retain heap property. Uses rotations.
     * @param node the node to sink downwards
     */
    void sink(TreapNode<K, V> node) {
        // Iterate while the current node is not a leaf
        while (node.comparePriority(node.getLeft()) < 0 || node.comparePriority(node.getRight()) < 0) {
            // Rotate the child with the highest priority up. (Null nodes have negative priority)
            if (node.getLeft() != null) {
                if (node.comparePriority(node.getLeft()) < 0 && node.getLeft().comparePriority(node.getRight()) >= 0) {
                    node.rotateRight();
                    if (root == node) {
                        root = node.getParent();
                    }
                }
            }
            if (node.getRight() != null) {
                if (node.comparePriority(node.getRight()) < 0 && node.getRight().comparePriority(node.getLeft()) >= 0) {
                    node.rotateLeft();
                    if (root == node) {
                        root = node.getParent();
                    }
                }
            }

        }
    }

    /**
     * Recursively finds the node with value closest with a key
     * @param key the key to search
     * @return either the node with the same key, or a node with the closest key value that doesn't have a child
     */
    TreapNode<K, V> find(K key) {
        if (root == null) {
            return null;
        }

        TreapNode<K, V> currentNode = root;
        while (true) {
            // If key is greater, go left. If less, go right. If equal, return the current node.
            if (currentNode.compareKey(key) > 0) {
                if (currentNode.getLeft() == null) {
                    return currentNode;
                }
                currentNode = currentNode.getLeft();
            } else if (currentNode.compareKey(key) < 0) {
                if (currentNode.getRight() == null) {
                    return currentNode;
                }
                currentNode = currentNode.getRight();
            } else {
                return currentNode;
            }
        }
    }

    public class TreapMapIterator implements Iterator<K> {
        ArrayList<K> inOrder = new ArrayList<>();
        int changes;

        int currentIndex = 0;

        /**
         * Constructs an Iterator over the keys of the TreapMap and precomputes the sorted order
         * @param changes The number of changes that have been made to the TreapMap
         */
        public TreapMapIterator(int changes) {
            addToTraversal(root);
            this.changes = changes;
        }

        /**
         * Performs an in-order traversal of a Treap subtree
         * @param node root of the subtree
         */
        void addToTraversal(TreapNode<K, V> node) {
            if (node == null) {
                return;
            }
            addToTraversal(node.left);
            inOrder.add(node.key);
            addToTraversal(node.right);
        }

        @Override
        public boolean hasNext() {
            return currentIndex < inOrder.size();
        }

        @Override
        public K next() {
            if (TreapMap.this.numChanges != changes) {
                throw new ConcurrentModificationException();
            }
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return inOrder.get(currentIndex++);
        }
    }
}
