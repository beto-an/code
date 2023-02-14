package assignment;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TetrisPieceTest {

    public static final int[] DIMENSION = {3, 2, 4, 3, 3, 3, 3};


    @Test
    void getType() {
        for (Piece.PieceType type : Piece.PieceType.values()) {
            TetrisPiece p = new TetrisPiece(type);
            assertEquals(p.getType(), type);
        }
    }

    @Test
    void getRotationIndex() {
        for (Piece.PieceType type : Piece.PieceType.values()) {
            TetrisPiece p = new TetrisPiece(type);
            assertEquals(p.getRotationIndex(), 0);
        }
    }

    @Test
    void getWidth() {
        int i = 0;
        for (PieceDictionaryItem piece : PieceDictionaryItem.values()) {
            assertEquals(piece.getCurrentPiece().getWidth(), DIMENSION[i / 4]);
            i++;
        }

        i = 0;
        for (Piece.PieceType type : Piece.PieceType.values()) {
            TetrisPiece p = new TetrisPiece(type);
            assertEquals(p.getWidth(), DIMENSION[i]);
            i++;
        }
    }

    @Test
    void getHeight() {
        int i = 0;
        for (PieceDictionaryItem piece : PieceDictionaryItem.values()) {
            assertEquals(piece.getCurrentPiece().getHeight(), DIMENSION[i / 4]);
            i++;
        }

        i = 0;
        for (Piece.PieceType type : Piece.PieceType.values()) {
            TetrisPiece p = new TetrisPiece(type);
            assertEquals(p.getHeight(), DIMENSION[i]);
            i++;
        }
    }

    @Test
    void testEquals() {
    }
}