package assignment;

import java.awt.*;

/**
 * An immutable representation of a tetris piece in a particular rotation.
 * 
 * All operations on a TetrisPiece should be constant time, except for its
 * initial construction. This means that rotations should also be fast - calling
 * clockwisePiece() and counterclockwisePiece() should be constant time! You may
 * need to do pre-computation in the constructor to make this possible.
 */
public final class TetrisPiece implements Piece {


    //Store the PieceDictionaryItem enum that corresponds to the specific piece and orientation
    //The PieceDictionaryItem enums store all the immutable information about each piece
    private final PieceDictionaryItem pd;


    /**
     * Construct a tetris piece of the given type. The piece should be in its spawn orientation,
     * i.e., a rotation index of 0.
     * 
     * You may freely add additional constructors, but please leave this one - it is used both in
     * the runner code and testing code.
     */
    public TetrisPiece(PieceType type) {

        //Based on the given type, initialize the correct PieceDictionaryItem enum
        switch(type) {
            case T:
                pd = PieceDictionaryItem.T_NORTH;
                break;
            case SQUARE:
                pd = PieceDictionaryItem.SQUARE_NORTH;
                break;
            case STICK:
                pd = PieceDictionaryItem.STICK_NORTH;
                break;
            case LEFT_L:
                pd = PieceDictionaryItem.LEFT_L_NORTH;
                break;
            case RIGHT_L:
                pd = PieceDictionaryItem.RIGHT_L_NORTH;
                break;
            case LEFT_DOG:
                pd = PieceDictionaryItem.LEFT_DOG_NORTH;
                break;
            case RIGHT_DOG:
                pd = PieceDictionaryItem.RIGHT_DOG_NORTH;
                break;
            default:
                pd = null;
        }

    }


    //Helper constructor for creating a TetrisPiece given a PieceDictionaryItem enum
    public TetrisPiece(PieceDictionaryItem pd) {
        this.pd = pd;
    }

    @Override
    public PieceType getType() {
        if (pd == null) return null;
        return pd.getType();
    }

    @Override
    public int getRotationIndex() {
        if (pd == null) return 0;
        return pd.getRotationIndex();
    }


    //Uses the RotationChain class to get the rotated piece (clockwise or counterclockwise)
    @Override
    public Piece clockwisePiece() {
        if (pd == null) return null;
        return RotationChain.rotateClockwise(pd).getCurrentPiece();
    }

    @Override
    public Piece counterclockwisePiece() {
        if (pd == null) return null;
        return RotationChain.rotateCounterClockwise(pd).getCurrentPiece();
    }


    @Override
    public int getWidth() {
        if (pd == null) return 0;
        return getType().getBoundingBox().width;
    }

    @Override
    public int getHeight() {
        if (pd == null) return 0;
        return getType().getBoundingBox().height;
    }

    @Override
    public Point[] getBody() {
        if (pd == null) return null;
        return pd.getBody();
    }

    @Override
    public int[] getSkirt() {
        if (pd == null) return null;
        return pd.getSkirt();
    }


    //Check for equality between two TetrisPieces
    @Override
    public boolean equals(Object other) {

        // Ignore objects which aren't also tetris pieces.
        if(!(other instanceof TetrisPiece)) return false;
        TetrisPiece otherPiece = (TetrisPiece) other;

        //All information is stored in the TetrisPiece's PieceDictionaryItem enum, so just compare those two
        return this.pd.equals(otherPiece.pd);

    }


}
