package assignment;

import java.util.HashMap;


//Helper class for getting the rotated object of a certain piece quickly
public class RotationChain {


    //Create an array for each PieceType, storing all four rotations in order
    private static final PieceDictionaryItem[] T_CHAIN = { PieceDictionaryItem.T_NORTH, PieceDictionaryItem.T_EAST, PieceDictionaryItem.T_SOUTH, PieceDictionaryItem.T_WEST };
    private static final PieceDictionaryItem[] SQUARE_CHAIN = { PieceDictionaryItem.SQUARE_NORTH, PieceDictionaryItem.SQUARE_EAST, PieceDictionaryItem.SQUARE_SOUTH, PieceDictionaryItem.SQUARE_WEST };
    private static final PieceDictionaryItem[] STICK_CHAIN = { PieceDictionaryItem.STICK_NORTH, PieceDictionaryItem.STICK_EAST, PieceDictionaryItem.STICK_SOUTH, PieceDictionaryItem.STICK_WEST };
    private static final PieceDictionaryItem[] LEFT_L_CHAIN = { PieceDictionaryItem.LEFT_L_NORTH, PieceDictionaryItem.LEFT_L_EAST, PieceDictionaryItem.LEFT_L_SOUTH, PieceDictionaryItem.LEFT_L_WEST };
    private static final PieceDictionaryItem[] RIGHT_L_CHAIN = { PieceDictionaryItem.RIGHT_L_NORTH, PieceDictionaryItem.RIGHT_L_EAST, PieceDictionaryItem.RIGHT_L_SOUTH, PieceDictionaryItem.RIGHT_L_WEST };
    private static final PieceDictionaryItem[] LEFT_DOG_CHAIN = { PieceDictionaryItem.LEFT_DOG_NORTH, PieceDictionaryItem.LEFT_DOG_EAST, PieceDictionaryItem.LEFT_DOG_SOUTH, PieceDictionaryItem.LEFT_DOG_WEST };
    private static final PieceDictionaryItem[] RIGHT_DOG_CHAIN = { PieceDictionaryItem.RIGHT_DOG_NORTH, PieceDictionaryItem.RIGHT_DOG_EAST, PieceDictionaryItem.RIGHT_DOG_SOUTH, PieceDictionaryItem.RIGHT_DOG_WEST };


    //Use a HashMap to map from a PieceType to the corresponding chain
    private static final HashMap<Piece.PieceType, PieceDictionaryItem[]> pieceTypeToChain = makeHashMap();


    //Helper method that is called once to populate the HashMap
    public static HashMap<Piece.PieceType, PieceDictionaryItem[]> makeHashMap() {

        HashMap<Piece.PieceType, PieceDictionaryItem[]> pieceTypeToChain = new HashMap<>();
        pieceTypeToChain.put(Piece.PieceType.T, T_CHAIN);
        pieceTypeToChain.put(Piece.PieceType.SQUARE, SQUARE_CHAIN);
        pieceTypeToChain.put(Piece.PieceType.STICK, STICK_CHAIN);
        pieceTypeToChain.put(Piece.PieceType.LEFT_L, LEFT_L_CHAIN);
        pieceTypeToChain.put(Piece.PieceType.RIGHT_L, RIGHT_L_CHAIN);
        pieceTypeToChain.put(Piece.PieceType.LEFT_DOG, LEFT_DOG_CHAIN);
        pieceTypeToChain.put(Piece.PieceType.RIGHT_DOG, RIGHT_DOG_CHAIN);
        return pieceTypeToChain;

    }


    //Return the chain for a specific PieceType
    public static PieceDictionaryItem[] getChain(Piece.PieceType type) {
        return pieceTypeToChain.get(type);
    }


    //Get the piece that is rotated 90 degrees clockwise, using the correct chain
    public static PieceDictionaryItem rotateClockwise(PieceDictionaryItem piece) {
        return getChain(piece.getType())[(piece.getRotationIndex() + 1) % 4];
    }


    //Get the piece that is rotated 90 degrees counterclockwise, using the correct chain
    public static PieceDictionaryItem rotateCounterClockwise(PieceDictionaryItem piece) {
        return getChain(piece.getType())[(piece.getRotationIndex() + 3) % 4];
    }


}
