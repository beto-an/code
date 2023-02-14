package assignment;

import java.util.ArrayList;
import java.util.HashMap;


public class CharNode {

    // Stores the possible next CharNodes
    private final HashMap<Character, CharNode> nextCharSet = new HashMap<>();

    // Parent of the CharNode
    private CharNode parent;

    // Character value of the CharNode
    private final char val;

    /**
     * Constructs a CharNode with the specified char value
     * @param val the char value that the constructed CharNode stores
     */
    public CharNode(char val) {
        this.val = val;
    }

    /**
     * Constructs a CharNode with the specified char value and parent CharNode
     * @param val the char value that the constructed CharNode stores
     * @param parent the parent CharNode of the constructed CharNode
     */
    public CharNode(char val, CharNode parent) {
        this.val = val;
        this.parent = parent;
    }

    /**
     * Add a CharNode as a child of the current CharNode
     * @param nextNode the next node to be added
     */
    public void addNextChar(CharNode nextNode) {
        nextNode.setParent(this);
        nextCharSet.put(nextNode.val, nextNode);
    }

    /**
     * Add an empty CharNode with the specified char as the contained character value as a child of the current CharNode
     * @param c the char value that the child CharNode stores
     */
    public void addNextChar(char c) {
        nextCharSet.put(c, new CharNode(c, this));

    }

    /**
     * Checks if the current node contains a specified character as a child
     * @param c the char value to check
     * @return true if the specified char has followed the current CharNode, false otherwise
     */
    public boolean containsChar(char c) {
        return nextCharSet.containsKey(c);
    }

    /**
     * Gives the CharNode that follows with the specified char
     * @param c the char value to check
     * @return the CharNode that follows with the specified char, or null if it doesn't exist
     */
    public CharNode get(char c) {
        return nextCharSet.get(c);
    }

    /**
     * Sets the parent of the current Node
     * @param parent the parent node
     */
    public void setParent(CharNode parent) {
        this.parent = parent;
    }

    /**
     * Gives the stored char value of the current CharNode
     * @return the stored char value of the current CharNode
     */
    public char getChar() {
        return val;
    }

    /**
     * Gives the stored parent CharNode of the current CharNode
     * @return the stored parent CharNode of the current CharNode
     */
    public CharNode getParent() {
        return parent;
    }
}

class EndNode extends CharNode {
    ArrayList<CharNode> lastNodes = new ArrayList<>();

    /**
     * Constructs an EndNode with the specified char value
     * @param val the char value that the constructed EndNode stores
     */
    public EndNode(char val) {
        super(val);
    }

    /**
     * Adds the last CharNode of a string to the list possible ends
     * @param cn the last CharNode of a string
     */
    @Override
    public void setParent(CharNode cn) {
        lastNodes.add(cn);
    }

    /**
     * Gets the last CharNode of the ith string connected to the EndNode
     * @param i index of CharNode
     * @return the last CharNode of the ith string added
     */
    public CharNode getNodeAt(int i) {
        return lastNodes.get(i);
    }

    /**
     * Gives the number of Strings connected to the EndNode
     * @return the number of Strings connected to the EndNode
     */
    public int getSize() {
        return lastNodes.size();
    }

    @Override
    public void addNextChar(char c) {
        System.err.println("Cannot add next character to an EndNode");
    }

    @Override
    public void addNextChar(CharNode nextNode) {
        System.err.println("Cannot add next character to an EndNode");
    }
}

class StartNode extends CharNode {

    /**
     * Constructs an StartNode with the specified char value
     * @param val the char value that the constructed StartNode stores
     */
    public StartNode(char val) {
        super(val);
    }

    @Override
    public void setParent(CharNode cn) {
        System.err.println("Cannot add parent character to a StartNode");
    }
}
