package assignment;

import java.util.HashMap;

public class RotationChain {

    private static final PieceDictionaryItem[] T_CHAIN = { PieceDictionaryItem.T_NORTH, PieceDictionaryItem.T_EAST, PieceDictionaryItem.T_SOUTH, PieceDictionaryItem.T_WEST };
    private static final PieceDictionaryItem[] SQUARE_CHAIN = { PieceDictionaryItem.SQUARE_NORTH, PieceDictionaryItem.SQUARE_EAST, PieceDictionaryItem.SQUARE_SOUTH, PieceDictionaryItem.SQUARE_WEST };
    private static final PieceDictionaryItem[] STICK_CHAIN = { PieceDictionaryItem.STICK_NORTH, PieceDictionaryItem.STICK_EAST, PieceDictionaryItem.STICK_SOUTH, PieceDictionaryItem.STICK_WEST };
    private static final PieceDictionaryItem[] LEFT_L_CHAIN = { PieceDictionaryItem.LEFT_L_NORTH, PieceDictionaryItem.LEFT_L_EAST, PieceDictionaryItem.LEFT_L_SOUTH, PieceDictionaryItem.LEFT_L_WEST };
    private static final PieceDictionaryItem[] RIGHT_L_CHAIN = { PieceDictionaryItem.RIGHT_L_NORTH, PieceDictionaryItem.RIGHT_L_EAST, PieceDictionaryItem.RIGHT_L_SOUTH, PieceDictionaryItem.RIGHT_L_WEST };
    private static final PieceDictionaryItem[] LEFT_DOG_CHAIN = { PieceDictionaryItem.LEFT_DOG_NORTH, PieceDictionaryItem.LEFT_DOG_EAST, PieceDictionaryItem.LEFT_DOG_SOUTH, PieceDictionaryItem.LEFT_DOG_WEST };
    private static final PieceDictionaryItem[] RIGHT_DOG_CHAIN = { PieceDictionaryItem.RIGHT_DOG_NORTH, PieceDictionaryItem.RIGHT_DOG_EAST, PieceDictionaryItem.RIGHT_DOG_SOUTH, PieceDictionaryItem.RIGHT_DOG_WEST };

    private static final HashMap<Piece.PieceType, PieceDictionaryItem[]> pieceTypeToChain = makeHashMap();

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

    public static PieceDictionaryItem[] getChain(Piece.PieceType type) {
        return pieceTypeToChain.get(type);
    }

    public static PieceDictionaryItem rotateClockwise(PieceDictionaryItem piece) {
        return getChain(piece.getType())[(piece.getRotationIndex() + 1) % 4];
    }

    public static PieceDictionaryItem rotateCounterClockwise(PieceDictionaryItem piece) {
        return getChain(piece.getType())[(piece.getRotationIndex() + 3) % 4];
    }

}
