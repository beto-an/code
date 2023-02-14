package assignment;

public class TreapNode<K extends Comparable<K>, V> {

    // The priority of the TreapNode
    int priority;

    // The key stored by the TreapNode
    final K key;

    // The value stored by the TreapNode
    V value;

    // The left child of the current TreapNode
    TreapNode<K, V> left;

    // The right child of the current TreapNode
    TreapNode<K, V> right;

    // The parent of the current TreapNode
    TreapNode<K, V> parent;

    /**
     * Constructs a TreapNode with the specified key and value. Randomly determines the priority.
     * @param key the key of the TreapNode
     * @param value the value of the TreapNode
     */
    public TreapNode(K key, V value) {
        this.key = key;
        this.value = value;
        priority = (int) (Treap.MAX_PRIORITY * Math.random());
    }

    /**
     * Constructs a TreapNode with the specified key, value, and priority.
     * @param key the key of the TreapNode
     * @param value the value of the TreapNode
     * @param priority the priority of the TreapNode
     */
    TreapNode(K key, V value, int priority) {
        this.key = key;
        this.value = value;
        this.priority = priority;
    }

    /**
     * Compares the keys of two TreapNodes
     * @param that the other TreapNode
     * @return the result of comparison between the keys of the two TreapNodes
     */
    public int compareKey(TreapNode<K, V> that) {
        return this.key.compareTo(that.key);
    }

    /**
     * Compares the key of the current TreapNode to another key
     * @param key the other key
     * @return the result of comparison between the current TreapNode's key and the other key
     */
    public int compareKey(K key) {
        return this.key.compareTo(key);
    }

    /**
     * Compares the priority of the current TreapNode to another TreapNode
     * @param that the node to be compared with
     * @return the result of comparison between the priorities of the two TreapNodes
     */
    public int comparePriority(TreapNode<K, V> that) {
        if (that == null) {
            return 1; // Every node has higher priority that a null node (leaf)
        }
        return Double.compare(this.priority, that.priority);
    }

    /**
     * Rotates a node leftwards (so its right child becomes its parent)
     */
    public void rotateLeft() {
        if (right == null) {
            return;
        }
        TreapNode<K, V> middleChild = right.left;

        right.parent = parent;
        if (parent != null) {
            if (isLeftChild()) {
                parent.left = right;
            } else {
                parent.right = right;
            }
        }

        parent = right;
        right.left = this;
        right = middleChild;
        if (middleChild != null) {
            middleChild.parent = this;
        }
    }

    /**
     * Rotates a node rightwards (so its left child becomes its parent)
     */
    public void rotateRight() {
        if (left == null) {
            return;
        }
        TreapNode<K, V> middleChild = left.right;

        left.parent = parent;
        if (parent != null) {
            if (isLeftChild()) {
                parent.left = left;
            } else {
                parent.right = left;
            }
        }

        parent = left;
        left.right = this;
        left = middleChild;
        if (middleChild != null) {
            middleChild.parent = this;
        }
    }

    /**
     * Checks if the current TreapNode is the left child of its parent (if it exists)
     * @return true if the current TreapNode is the left child of its parent, false otherwise
     */
    public boolean isLeftChild() {
        if (parent == null) {
            return false;
        }
        return parent.left == this;
    }

    /**
     * Gives the string form of the TreapNode
     * @return the string form of the TreapNode
     */
    @Override
    public String toString() {
        return "[" +
                priority +
                "] <" +
                key +
                ", " +
                value +
                ">\n";
    }


    public int getPriority() {
        return priority;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public TreapNode<K, V> getLeft() {
        return left;
    }

    public TreapNode<K, V> getRight() {
        return right;
    }

    public TreapNode<K, V> getParent() {
        return parent;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public void setLeft(TreapNode<K, V> left) {
        this.left = left;
    }

    public void setRight(TreapNode<K, V> right) {
        this.right = right;
    }

    public void setParent(TreapNode<K, V> parent) {
        this.parent = parent;
    }

}
