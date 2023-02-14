package assignment;

import java.awt.*;


//Helper enum for storing data related to each PieceType and providing helper functions
public enum PieceDictionaryItem {


    //Enums for the orientations of T
    T_NORTH(Piece.PieceType.T, new Point[] { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(1, 2) }, new int[]{1, 1, 1}, 0),
    T_EAST(Piece.PieceType.T, new Point[] { new Point(1, 0), new Point(1, 1), new Point(2, 1), new Point(1, 2) }, new int[]{Integer.MAX_VALUE, 0, 1}, 1),
    T_SOUTH(Piece.PieceType.T, new Point[] { new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1) }, new int[]{1, 0, 1}, 2),
    T_WEST(Piece.PieceType.T, new Point[] { new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) }, new int[]{1, 0, Integer.MAX_VALUE}, 3),


    //Enums for the orientations of square
    SQUARE_NORTH(Piece.PieceType.SQUARE, new Point[] { new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) }, new int[]{0, 0}, 0),
    SQUARE_EAST(Piece.PieceType.SQUARE, new Point[] { new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) }, new int[]{0, 0}, 1),
    SQUARE_SOUTH(Piece.PieceType.SQUARE, new Point[] { new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) }, new int[]{0, 0}, 2),
    SQUARE_WEST(Piece.PieceType.SQUARE, new Point[] { new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) }, new int[]{0, 0}, 3),


    //Enums for the orientations of stick
    STICK_NORTH(Piece.PieceType.STICK, new Point[] { new Point(0, 2), new Point(1, 2), new Point(2, 2), new Point(3, 2) }, new int[]{2, 2, 2, 2}, 0),
    STICK_EAST(Piece.PieceType.STICK, new Point[] { new Point(2, 0), new Point(2, 1), new Point(2, 2), new Point(2, 3) }, new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE, 0, Integer.MAX_VALUE}, 1),
    STICK_SOUTH(Piece.PieceType.STICK, new Point[] { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) }, new int[]{1, 1, 1, 1}, 2),
    STICK_WEST(Piece.PieceType.STICK, new Point[] { new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) }, new int[]{Integer.MAX_VALUE, 0, Integer.MAX_VALUE, Integer.MAX_VALUE}, 3),


    //Enums for the orientations of left L
    LEFT_L_NORTH(Piece.PieceType.LEFT_L, new Point[] { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 2) }, new int[]{1, 1, 1}, 0),
    LEFT_L_EAST(Piece.PieceType.LEFT_L, new Point[] { new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 2) }, new int[]{Integer.MAX_VALUE, 0, 2}, 1),
    LEFT_L_SOUTH(Piece.PieceType.LEFT_L, new Point[] { new Point(2, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1)}, new int[]{1, 1, 0}, 2),
    LEFT_L_WEST(Piece.PieceType.LEFT_L, new Point[] { new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(1, 2)}, new int[]{0, 0, Integer.MAX_VALUE}, 3),


    //Enums for the orientations of right L
    RIGHT_L_NORTH(Piece.PieceType.RIGHT_L, new Point[] { new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 2) }, new int[]{1, 1, 1}, 0),
    RIGHT_L_EAST(Piece.PieceType.RIGHT_L, new Point[] { new Point(1, 0), new Point(2, 0), new Point(1, 1), new Point(1, 2) }, new int[]{Integer.MAX_VALUE, 0, 0}, 1),
    RIGHT_L_SOUTH(Piece.PieceType.RIGHT_L, new Point[] { new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1) }, new int[]{0, 1, 1}, 2),
    RIGHT_L_WEST(Piece.PieceType.RIGHT_L, new Point[] { new Point(1, 0), new Point(1, 1), new Point(0, 2), new Point(1, 2) }, new int[]{2, 0, Integer.MAX_VALUE}, 3),


    //Enums for the orientations of left dog
    LEFT_DOG_NORTH(Piece.PieceType.LEFT_DOG, new Point[] { new Point(0, 2), new Point(1, 2), new Point(1, 1), new Point(2, 1) }, new int[]{2, 1, 1}, 0),
    LEFT_DOG_EAST(Piece.PieceType.LEFT_DOG, new Point[] { new Point(1, 0), new Point(1, 1), new Point(2, 1), new Point(2, 2) }, new int[]{Integer.MAX_VALUE, 0, 1}, 1),
    LEFT_DOG_SOUTH(Piece.PieceType.LEFT_DOG, new Point[] { new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1) }, new int[]{1, 0, 0}, 2),
    LEFT_DOG_WEST(Piece.PieceType.LEFT_DOG, new Point[] { new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) }, new int[]{0, 1, Integer.MAX_VALUE}, 3),


    //Enums for the orientations of right dog
    RIGHT_DOG_NORTH(Piece.PieceType.RIGHT_DOG, new Point[] { new Point(0, 1), new Point(1, 1), new Point(1, 2), new Point(2, 2) }, new int[]{1, 1, 2}, 0),
    RIGHT_DOG_EAST(Piece.PieceType.RIGHT_DOG, new Point[] { new Point(2, 0), new Point(1, 1), new Point(2, 1), new Point(1, 2) }, new int[]{Integer.MAX_VALUE, 1, 0}, 1),
    RIGHT_DOG_SOUTH(Piece.PieceType.RIGHT_DOG, new Point[] { new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1) }, new int[]{0, 0, 1}, 2),
    RIGHT_DOG_WEST(Piece.PieceType.RIGHT_DOG, new Point[] { new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(0, 2) }, new int[]{1, 0, Integer.MAX_VALUE}, 3);


    //Store a variety of useful information relating to a piece
    private final Piece.PieceType type;
    private final Point[] body;
    private final int[] skirt;
    private final int rotationIndex;


    //Store a reference to the TetrisPiece object that has this enum's PieceType and orientation
    private final TetrisPiece currentPiece;


    //Constructor for creating a PieceDictionary Item with stored data
    PieceDictionaryItem(Piece.PieceType type, Point[] body, int[] skirt, int rotationIndex) {
        this.type = type;
        this.body = body;
        this.skirt = skirt;
        this.rotationIndex = rotationIndex;
        this.currentPiece = new TetrisPiece(this);
    }


    public Piece.PieceType getType() {
        return type;
    }


    public Point[] getBody() {
        return body;
    }


    public int[] getSkirt() {
        return skirt;
    }


    public int getRotationIndex() {
        return rotationIndex;
    }


    public TetrisPiece getCurrentPiece() {
        return currentPiece;
    }


}

